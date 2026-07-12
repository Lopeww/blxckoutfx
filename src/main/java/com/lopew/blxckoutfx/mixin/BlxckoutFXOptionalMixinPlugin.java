package com.lopew.blxckoutfx.mixin;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Keeps compat mixins out of Mixin's target-selection path when their owner
 * is not installed. Resource probing does not define or resolve the target.
 */
public final class BlxckoutFXOptionalMixinPlugin implements IMixinConfigPlugin {
    private static final String JEI_MIXIN_PREFIX = "com.lopew.blxckoutfx.mixin.jei.";
    private static final String JOURNEYMAP_MIXIN_PREFIX = "com.lopew.blxckoutfx.mixin.journeymap.";
    private static final String MC_DRAW_BUTTON_DESC = "(Lnet/minecraft/client/Minecraft;IIF)V";
    private static final Map<String, List<MethodRequirement>> REQUIRED_METHODS = createRequiredMethods();

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (!isOptionalMixin(mixinClassName)) {
            return true;
        }
        return hasRequiredTarget(targetClassName, mixinClassName);
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    private static boolean isOptionalMixin(String mixinClassName) {
        return mixinClassName != null
                && (mixinClassName.startsWith(JEI_MIXIN_PREFIX) || mixinClassName.startsWith(JOURNEYMAP_MIXIN_PREFIX));
    }

    private static boolean hasRequiredTarget(String className, String mixinClassName) {
        if (className == null || className.isEmpty() || mixinClassName == null) {
            return false;
        }

        String resourceName = className.replace('.', '/') + ".class";
        ClassLoader loader = BlxckoutFXOptionalMixinPlugin.class.getClassLoader();
        if (loader == null) {
            return false;
        }

        List<MethodRequirement> requirements = REQUIRED_METHODS.get(mixinClassName);
        if (requirements == null || requirements.isEmpty()) {
            return false;
        }

        try (InputStream stream = loader.getResourceAsStream(resourceName)) {
            if (stream == null) {
                return false;
            }

            final Set<String> methods = new HashSet<>();
            new ClassReader(stream).accept(new ClassVisitor(Opcodes.ASM5) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                    methods.add(name + descriptor);
                    return null;
                }
            }, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

            for (MethodRequirement requirement : requirements) {
                if (requirement.matches(methods)) {
                    return true;
                }
            }
            return false;
        } catch (IOException ignored) {
            return false;
        }
    }

    private static Map<String, List<MethodRequirement>> createRequiredMethods() {
        Map<String, List<MethodRequirement>> methods = new HashMap<>();
        methods.put(JEI_MIXIN_PREFIX + "GuiIconButtonMixin", Collections.singletonList(
                new MethodRequirement(MC_DRAW_BUTTON_DESC, "drawButton", "func_191745_a")
        ));
        methods.put(JEI_MIXIN_PREFIX + "GuiIconButtonSmallMixin", Collections.singletonList(
                new MethodRequirement(MC_DRAW_BUTTON_DESC, "drawButton", "func_191745_a")
        ));
        methods.put(JEI_MIXIN_PREFIX + "GuiIconToggleButtonMixin", Collections.singletonList(
                new MethodRequirement(MC_DRAW_BUTTON_DESC, "draw")
        ));
        methods.put(JEI_MIXIN_PREFIX + "ShapelessIconMixin", Collections.singletonList(
                new MethodRequirement("(Lnet/minecraft/client/Minecraft;I)V", "draw")
        ));
        methods.put(JOURNEYMAP_MIXIN_PREFIX + "JourneyMapDrawUtilMixin", Collections.singletonList(
                new MethodRequirement("(DDDDIF)V", "drawRectangle")
        ));
        methods.put(JOURNEYMAP_MIXIN_PREFIX + "JourneyMapThemeButtonMixin", Collections.singletonList(
                new MethodRequirement("(Lnet/minecraft/client/Minecraft;II)V", "drawNativeButton")
        ));
        return Collections.unmodifiableMap(methods);
    }

    private static final class MethodRequirement {
        private final String descriptor;
        private final List<String> names;

        private MethodRequirement(String descriptor, String... names) {
            this.descriptor = descriptor;
            this.names = Arrays.asList(names);
        }

        private boolean matches(Set<String> methods) {
            for (String name : this.names) {
                if (methods.contains(name + this.descriptor)) {
                    return true;
                }
            }
            return false;
        }
    }
}

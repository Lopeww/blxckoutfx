package com.lopew.blxckoutfx.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class BlxckoutFXAccessTransformer implements IClassTransformer {
    private static final String TEXTURE_MAP_CLASS = "net.minecraft.client.renderer.texture.TextureMap";
    private static final String GUI_RECIPE_BOOK_CLASS = "net.minecraft.client.gui.recipebook.GuiRecipeBook";
    private static final String INGREDIENT_CLASS = "net.minecraft.item.crafting.Ingredient";
    private static final String RENDER_ITEM_CLASS = "net.minecraft.client.renderer.RenderItem";
    private static final String PLAYER_LIST_CLASS = "net.minecraft.server.management.PlayerList";
    private static final String GUI_INGAME_CLASS = "net.minecraft.client.gui.GuiIngame";
    private static final String CONTAINER_CLASS = "net.minecraft.inventory.Container";
    private static final String ENUM_DYE_COLOR_CLASS = "net.minecraft.item.EnumDyeColor";
    private static final String TEXT_FORMATTING_CLASS = "net.minecraft.util.text.TextFormatting";
    private static final String COMMAND_HANDLER_CLASS = "net.minecraft.command.CommandHandler";
    private static final String FONT_RENDERER_CLASS = "net.minecraft.client.gui.FontRenderer";
    private static final String STYLE_CLASS = "net.minecraft.util.text.Style";
    private static final String FTB_ATLAS_SPRITE_ICON_CLASS = "com.feed_the_beast.ftblib.lib.icon.AtlasSpriteIcon";
    private static final String FTB_IMAGE_ICON_CLASS = "com.feed_the_beast.ftblib.lib.icon.ImageIcon";
    private static final String FTB_ICON_DRAW_DESC = "(IIII)V";
    private static final String AE2_GUI_IMG_BUTTON_CLASS = "appeng.client.gui.widgets.GuiImgButton";
    private static final String AE2_GUI_TAB_BUTTON_CLASS = "appeng.client.gui.widgets.GuiTabButton";
    private static final String AE2_GUI_TOGGLE_BUTTON_CLASS = "appeng.client.gui.widgets.GuiToggleButton";
    private static final String AE2_GUI_TEXT_FIELD_CLASS = "appeng.client.gui.widgets.MEGuiTextField";
    private static final String AE2_DRAW_BUTTON_DESC = "(Lnet/minecraft/client/Minecraft;IIF)V";
    private static final String AE2_DRAW_BUTTON_NAME = "drawButton";
    private static final String AE2_DRAW_BUTTON_SRG = "func_191745_a";
    private static final String AE2_DRAW_TEXTURED_RECT_NAME = "drawTexturedModalRect";
    private static final String AE2_DRAW_TEXTURED_RECT_SRG = "func_73729_b";
    private static final String AE2_DRAW_TEXTURED_RECT_DESC = "(IIIIII)V";
    private static final String AE2_DRAW_TEXT_FIELD_NAME = "drawTextBox";
    private static final String AE2_DRAW_TEXT_FIELD_SRG = "func_146194_f";
    private static final String AE2_DRAW_TEXT_FIELD_DESC = "()V";
    private static final String PNC_GUI_BUTTON_SPECIAL_CLASS = "me.desht.pneumaticcraft.client.gui.GuiButtonSpecial";
    private static final String PNC_STAT_ICON_CLASS = "me.desht.pneumaticcraft.client.gui.widget.GuiAnimatedStat$StatIcon";
    private static final String PNC_PROG_WIDGET_CLASS = "me.desht.pneumaticcraft.common.progwidgets.ProgWidget";
    private static final String PNC_DRAW_CUSTOM_TEXTURE_NAME = "drawModalRectWithCustomSizedTexture";
    private static final String PNC_DRAW_CUSTOM_TEXTURE_SRG = "func_146110_a";
    private static final String PNC_DRAW_CUSTOM_TEXTURE_DESC = "(IIFFIIFF)V";
    private static final String PNC_STAT_ICON_RENDER_DESC = "(Lnet/minecraft/client/gui/Gui;IIZ)V";
    private static final String PATCHOULI_BOOK_ICON_CLASS = "vazkii.patchouli.client.book.BookIcon";
    private static final String PATCHOULI_BOOK_BUTTON_CLASS = "vazkii.patchouli.client.book.gui.button.GuiButtonBook";
    private static final String PATCHOULI_GUI_BOOK_CLASS = "vazkii.patchouli.client.book.gui.GuiBook";
    private static final String PATCHOULI_PAGE_IMAGE_CLASS = "vazkii.patchouli.client.book.page.PageImage";
    private static final String PATCHOULI_COMPONENT_IMAGE_CLASS = "vazkii.patchouli.client.book.template.component.ComponentImage";
    private static final String PATCHOULI_BOOK_CLASS_DESC = "Lvazkii/patchouli/common/book/Book;";
    private static final String PATCHOULI_DRAW_LOCK_DESC = "(" + PATCHOULI_BOOK_CLASS_DESC + "II)V";
    private static final String PATCHOULI_DRAW_MARKING_DESC = "(" + PATCHOULI_BOOK_CLASS_DESC
            + "IIILvazkii/patchouli/client/book/EntryDisplayState;)V";
    private static final String PATCHOULI_PAGE_IMAGE_RENDER_DESC = "(IIF)V";
    private static final String PATCHOULI_COMPONENT_IMAGE_RENDER_DESC =
            "(Lvazkii/patchouli/client/book/BookPage;IIF)V";
    private static final String RENDER_CONTEXT_CLASS = "com/lopew/blxckoutfx/client/BlxckoutFXRenderContext";
    private static final String AE2_ICON_HOOKS_CLASS = "com/lopew/blxckoutfx/client/integration/ae2/BlxckoutFXAe2IconHooks";

    private static final String INIT_MISSING_IMAGE_NAME = "initMissingImage";
    private static final String INIT_MISSING_IMAGE_SRG = "func_110569_e";
    private static final String INIT_MISSING_IMAGE_DESC = "()V";

    private static final String RENDER_MODEL_COLOR_NAME = "renderModel";
    private static final String RENDER_MODEL_COLOR_SRG = "func_191965_a";
    private static final String RENDER_MODEL_COLOR_DESC = "(Lnet/minecraft/client/renderer/block/model/IBakedModel;I)V";
    private static final String RENDER_MODEL_STACK_NAME = "renderModel";
    private static final String RENDER_MODEL_STACK_SRG = "func_191961_a";
    private static final String RENDER_MODEL_STACK_DESC = "(Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/item/ItemStack;)V";

    private static final String[] TEXTURE_MAP_FIELD_NAMES = {
            "mapUploadedSprites", "listAnimatedSprites", "missingImage", "basePath", "mapRegisteredSprites"
    };
    private static final String[] TEXTURE_MAP_FIELD_SRGS = {
            "field_94252_e", "field_94258_i", "field_94249_f", "field_94254_c", "field_110574_e"
    };
    private static final String[] GUI_RECIPE_BOOK_FIELD_NAMES = {
            "width", "height", "xOffset", "recipeTabs"
    };
    private static final String[] GUI_RECIPE_BOOK_FIELD_SRGS = {
            "field_191904_o", "field_191905_p", "field_191903_n", "field_193018_j"
    };
    private static final String INGREDIENT_MATCHING_STACKS_NAME = "matchingStacks";
    private static final String INGREDIENT_MATCHING_STACKS_SRG = "field_193371_b";
    private static final String COMMANDS_ALLOWED_FOR_ALL_NAME = "commandsAllowedForAll";
    private static final String COMMANDS_ALLOWED_FOR_ALL_SRG = "field_72407_n";
    private static final String CHAT_LISTENERS_NAME = "chatListeners";
    private static final String CHAT_LISTENERS_SRG = "field_191743_I";
    private static final String CONTAINER_LISTENERS_NAME = "listeners";
    private static final String CONTAINER_LISTENERS_SRG = "field_75149_d";
    private static final String CHAT_COLOR_NAME = "chatColor";
    private static final String CHAT_COLOR_SRG = "field_176793_x";
    private static final String FORMATTING_CODE_NAME = "formattingCode";
    private static final String FORMATTING_CODE_SRG = "field_96329_z";
    private static final String COMMAND_SET_NAME = "commandSet";
    private static final String COMMAND_SET_SRG = "field_71561_b";
    private static final String UNICODE_PAGE_LOCATIONS_NAME = "UNICODE_PAGE_LOCATIONS";
    private static final String UNICODE_PAGE_LOCATIONS_SRG = "field_111274_c";
    private static final String STYLE_GET_PARENT_NAME = "getParent";
    private static final String STYLE_GET_PARENT_SRG = "func_150224_n";
    private static final String STYLE_GET_PARENT_DESC = "()Lnet/minecraft/util/text/Style;";
    private static final String[] STYLE_FIELD_NAMES = {
            "bold", "italic", "strikethrough", "underlined", "obfuscated",
            "color", "clickEvent", "hoverEvent", "insertion"
    };
    private static final String[] STYLE_FIELD_SRGS = {
            "field_150248_c", "field_150245_d", "field_150243_f", "field_150246_e", "field_150244_g",
            "field_150247_b", "field_150251_h", "field_150252_i", "field_179990_j"
    };

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (basicClass == null
                || (!TEXTURE_MAP_CLASS.equals(transformedName)
                && !GUI_RECIPE_BOOK_CLASS.equals(transformedName)
                && !INGREDIENT_CLASS.equals(transformedName)
                && !RENDER_ITEM_CLASS.equals(transformedName)
                && !PLAYER_LIST_CLASS.equals(transformedName)
                && !GUI_INGAME_CLASS.equals(transformedName)
                && !CONTAINER_CLASS.equals(transformedName)
                && !ENUM_DYE_COLOR_CLASS.equals(transformedName)
                && !TEXT_FORMATTING_CLASS.equals(transformedName)
                && !COMMAND_HANDLER_CLASS.equals(transformedName)
                && !FONT_RENDERER_CLASS.equals(transformedName)
                && !STYLE_CLASS.equals(transformedName)
                && !isFtbIconClass(transformedName)
                && !AE2_GUI_IMG_BUTTON_CLASS.equals(transformedName)
                && !AE2_GUI_TAB_BUTTON_CLASS.equals(transformedName)
                && !AE2_GUI_TOGGLE_BUTTON_CLASS.equals(transformedName)
                && !AE2_GUI_TEXT_FIELD_CLASS.equals(transformedName)
                && !PNC_GUI_BUTTON_SPECIAL_CLASS.equals(transformedName)
                && !PNC_STAT_ICON_CLASS.equals(transformedName)
                && !PNC_PROG_WIDGET_CLASS.equals(transformedName)
                && !PATCHOULI_BOOK_ICON_CLASS.equals(transformedName)
                && !PATCHOULI_BOOK_BUTTON_CLASS.equals(transformedName)
                && !PATCHOULI_GUI_BOOK_CLASS.equals(transformedName)
                && !PATCHOULI_PAGE_IMAGE_CLASS.equals(transformedName)
                && !PATCHOULI_COMPONENT_IMAGE_CLASS.equals(transformedName))) {
            return basicClass;
        }

        ClassNode classNode = new ClassNode();
        new ClassReader(basicClass).accept(classNode, 0);

        boolean changed = false;

        if (isFtbIconClass(transformedName)) {
            changed = instrumentFtbIconDraws(classNode);
        } else if (AE2_GUI_IMG_BUTTON_CLASS.equals(transformedName)) {
            changed = instrumentAe2GuiImgButton(classNode);
        } else if (AE2_GUI_TAB_BUTTON_CLASS.equals(transformedName)) {
            changed = instrumentAe2TwoLayerButton(classNode, AE2_GUI_TAB_BUTTON_CLASS);
        } else if (AE2_GUI_TOGGLE_BUTTON_CLASS.equals(transformedName)) {
            changed = instrumentAe2TwoLayerButton(classNode, AE2_GUI_TOGGLE_BUTTON_CLASS);
        } else if (AE2_GUI_TEXT_FIELD_CLASS.equals(transformedName)) {
            changed = instrumentAe2GuiTextField(classNode);
        } else if (PNC_GUI_BUTTON_SPECIAL_CLASS.equals(transformedName)) {
            changed = instrumentPncButtonTextureIcon(classNode);
        } else if (PNC_STAT_ICON_CLASS.equals(transformedName)) {
            changed = instrumentTexturePreservingMethod(classNode, "render", PNC_STAT_ICON_RENDER_DESC);
        } else if (PNC_PROG_WIDGET_CLASS.equals(transformedName)) {
            changed = instrumentTexturePreservingMethod(classNode, "render", "()V");
        } else if (PATCHOULI_BOOK_ICON_CLASS.equals(transformedName)) {
            changed = instrumentTexturePreservingMethod(classNode, "render", "(II)V");
        } else if (PATCHOULI_BOOK_BUTTON_CLASS.equals(transformedName)) {
            changed = instrumentTexturePreservingMethod(classNode, AE2_DRAW_BUTTON_SRG, AE2_DRAW_BUTTON_DESC)
                    || instrumentTexturePreservingMethod(classNode, AE2_DRAW_BUTTON_NAME, AE2_DRAW_BUTTON_DESC);
        } else if (PATCHOULI_GUI_BOOK_CLASS.equals(transformedName)) {
            changed = instrumentTexturePreservingMethod(classNode, "drawLock", PATCHOULI_DRAW_LOCK_DESC);
            changed |= instrumentTexturePreservingMethod(classNode, "drawMarking", PATCHOULI_DRAW_MARKING_DESC);
        } else if (PATCHOULI_PAGE_IMAGE_CLASS.equals(transformedName)) {
            changed = instrumentPatchouliPageImage(classNode);
        } else if (PATCHOULI_COMPONENT_IMAGE_CLASS.equals(transformedName)) {
            changed = instrumentTexturePreservingMethod(
                    classNode,
                    "render",
                    PATCHOULI_COMPONENT_IMAGE_RENDER_DESC
            );
        } else if (TEXTURE_MAP_CLASS.equals(transformedName)) {
            for (FieldNode field : classNode.fields) {
                if (matches(field.name, "mapRegisteredSprites", "field_110574_e") && makePublic(field)) {
                    changed = true;
                } else if (matchesAny(field.name, TEXTURE_MAP_FIELD_NAMES, TEXTURE_MAP_FIELD_SRGS) && makeProtected(field)) {
                    changed = true;
                }
            }
            for (MethodNode method : classNode.methods) {
                if (isInitMissingImage(method) && makeProtected(method)) {
                    changed = true;
                }
            }
        } else if (GUI_RECIPE_BOOK_CLASS.equals(transformedName)) {
            for (FieldNode field : classNode.fields) {
                if (matchesAny(field.name, GUI_RECIPE_BOOK_FIELD_NAMES, GUI_RECIPE_BOOK_FIELD_SRGS) && makePublic(field)) {
                    changed = true;
                }
            }
        } else if (INGREDIENT_CLASS.equals(transformedName)) {
            for (FieldNode field : classNode.fields) {
                if ((INGREDIENT_MATCHING_STACKS_NAME.equals(field.name) || INGREDIENT_MATCHING_STACKS_SRG.equals(field.name)) && makePublic(field)) {
                    changed = true;
                }
            }
        } else if (PLAYER_LIST_CLASS.equals(transformedName)) {
            for (FieldNode field : classNode.fields) {
                if ((COMMANDS_ALLOWED_FOR_ALL_NAME.equals(field.name) || COMMANDS_ALLOWED_FOR_ALL_SRG.equals(field.name)) && makePublic(field)) {
                    changed = true;
                }
            }
        } else if (GUI_INGAME_CLASS.equals(transformedName)) {
            for (FieldNode field : classNode.fields) {
                if ((CHAT_LISTENERS_NAME.equals(field.name) || CHAT_LISTENERS_SRG.equals(field.name)) && makePublic(field)) {
                    changed = true;
                }
            }
        } else if (CONTAINER_CLASS.equals(transformedName)) {
            changed = makeNamedFieldPublic(classNode, CONTAINER_LISTENERS_NAME, CONTAINER_LISTENERS_SRG);
        } else if (ENUM_DYE_COLOR_CLASS.equals(transformedName)) {
            changed = makeNamedFieldPublic(classNode, CHAT_COLOR_NAME, CHAT_COLOR_SRG);
        } else if (TEXT_FORMATTING_CLASS.equals(transformedName)) {
            changed = makeNamedFieldPublic(classNode, FORMATTING_CODE_NAME, FORMATTING_CODE_SRG);
        } else if (COMMAND_HANDLER_CLASS.equals(transformedName)) {
            changed = makeNamedFieldPublic(classNode, COMMAND_SET_NAME, COMMAND_SET_SRG);
        } else if (FONT_RENDERER_CLASS.equals(transformedName)) {
            changed = makeNamedFieldPublic(classNode, UNICODE_PAGE_LOCATIONS_NAME, UNICODE_PAGE_LOCATIONS_SRG);
        } else if (STYLE_CLASS.equals(transformedName)) {
            for (FieldNode field : classNode.fields) {
                if (matchesAny(field.name, STYLE_FIELD_NAMES, STYLE_FIELD_SRGS) && makePublic(field)) {
                    changed = true;
                }
            }
            for (MethodNode method : classNode.methods) {
                if (STYLE_GET_PARENT_DESC.equals(method.desc)
                        && matches(method.name, STYLE_GET_PARENT_NAME, STYLE_GET_PARENT_SRG)
                        && makePublic(method)) {
                    changed = true;
                }
            }
        } else {
            for (MethodNode method : classNode.methods) {
                if (isRenderModelMethod(method) && makePublic(method)) {
                    changed = true;
                }
            }
        }

        if (!changed) {
            return basicClass;
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    private static boolean matchesAny(String name, String[] deobfNames, String[] srgNames) {
        for (String candidate : deobfNames) {
            if (candidate.equals(name)) {
                return true;
            }
        }
        for (String candidate : srgNames) {
            if (candidate.equals(name)) {
                return true;
            }
        }
        return false;
    }

    private static boolean matches(String name, String deobfName, String srgName) {
        return deobfName.equals(name) || srgName.equals(name);
    }

    private static boolean isFtbIconClass(String className) {
        return FTB_ATLAS_SPRITE_ICON_CLASS.equals(className) || FTB_IMAGE_ICON_CLASS.equals(className);
    }

    private static boolean instrumentFtbIconDraws(ClassNode classNode) {
        boolean changed = false;
        for (MethodNode method : classNode.methods) {
            if (!"draw".equals(method.name) || !FTB_ICON_DRAW_DESC.equals(method.desc)) {
                continue;
            }

            method.instructions.insert(new MethodInsnNode(
                    Opcodes.INVOKESTATIC,
                    RENDER_CONTEXT_CLASS,
                    "suppressTextureDarkening",
                    "()V",
                    false
            ));
            for (AbstractInsnNode instruction = method.instructions.getFirst(); instruction != null; ) {
                AbstractInsnNode next = instruction.getNext();
                if (instruction.getOpcode() == Opcodes.RETURN) {
                    InsnList restore = new InsnList();
                    restore.add(new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            RENDER_CONTEXT_CLASS,
                            "allowTextureDarkening",
                            "()V",
                            false
                    ));
                    method.instructions.insertBefore(instruction, restore);
                }
                instruction = next;
            }
            changed = true;
        }
        return changed;
    }

    private static boolean instrumentAe2GuiImgButton(ClassNode classNode) {
        for (MethodNode method : classNode.methods) {
            if (!AE2_DRAW_BUTTON_DESC.equals(method.desc)
                    || !matches(method.name, AE2_DRAW_BUTTON_NAME, AE2_DRAW_BUTTON_SRG)) {
                continue;
            }

            int matchingCalls = 0;
            for (AbstractInsnNode instruction = method.instructions.getFirst(); instruction != null; instruction = instruction.getNext()) {
                if (isAe2IconDrawCall(instruction)) {
                    matchingCalls++;
                }
            }
            if (matchingCalls != 4) {
                return false;
            }

            int callIndex = 0;
            for (AbstractInsnNode instruction = method.instructions.getFirst(); instruction != null; ) {
                AbstractInsnNode next = instruction.getNext();
                if (isAe2IconDrawCall(instruction)) {
                    if (callIndex == 1 || callIndex == 3) {
                        InsnList before = new InsnList();
                        before.add(new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 0));
                        before.add(new MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                AE2_ICON_HOOKS_CLASS,
                                "beginIconRender",
                                "(Ljava/lang/Object;)V",
                                false
                        ));
                        method.instructions.insertBefore(instruction, before);

                        InsnList after = new InsnList();
                        after.add(new MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                AE2_ICON_HOOKS_CLASS,
                                "endIconRender",
                                "()V",
                                false
                        ));
                        method.instructions.insert(instruction, after);
                    }
                    callIndex++;
                }
                instruction = next;
            }
            return true;
        }
        return false;
    }

    private static boolean isAe2IconDrawCall(AbstractInsnNode instruction) {
        if (!(instruction instanceof MethodInsnNode)) {
            return false;
        }

        MethodInsnNode methodCall = (MethodInsnNode) instruction;
        return AE2_GUI_IMG_BUTTON_CLASS.replace('.', '/').equals(methodCall.owner)
                && AE2_DRAW_TEXTURED_RECT_DESC.equals(methodCall.desc)
                && matches(methodCall.name, AE2_DRAW_TEXTURED_RECT_NAME, AE2_DRAW_TEXTURED_RECT_SRG);
    }

    private static boolean instrumentAe2TwoLayerButton(ClassNode classNode, String owner) {
        for (MethodNode method : classNode.methods) {
            if (!AE2_DRAW_BUTTON_DESC.equals(method.desc)
                    || !matches(method.name, AE2_DRAW_BUTTON_NAME, AE2_DRAW_BUTTON_SRG)) {
                continue;
            }

            int matchingCalls = 0;
            for (AbstractInsnNode instruction = method.instructions.getFirst(); instruction != null; instruction = instruction.getNext()) {
                if (isAe2TexturedRectCall(instruction, owner)) {
                    matchingCalls++;
                }
            }
            if (matchingCalls != 2) {
                return false;
            }

            int callIndex = 0;
            for (AbstractInsnNode instruction = method.instructions.getFirst(); instruction != null; ) {
                AbstractInsnNode next = instruction.getNext();
                if (isAe2TexturedRectCall(instruction, owner) && callIndex++ == 1) {
                    method.instructions.insertBefore(instruction, new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            RENDER_CONTEXT_CLASS,
                            "suppressTextureDarkening",
                            "()V",
                            false
                    ));
                    method.instructions.insert(instruction, new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            RENDER_CONTEXT_CLASS,
                            "allowTextureDarkening",
                            "()V",
                            false
                    ));
                }
                instruction = next;
            }
            return true;
        }
        return false;
    }

    private static boolean isAe2TexturedRectCall(AbstractInsnNode instruction, String owner) {
        if (!(instruction instanceof MethodInsnNode)) {
            return false;
        }

        MethodInsnNode methodCall = (MethodInsnNode) instruction;
        return owner.replace('.', '/').equals(methodCall.owner)
                && AE2_DRAW_TEXTURED_RECT_DESC.equals(methodCall.desc)
                && matches(methodCall.name, AE2_DRAW_TEXTURED_RECT_NAME, AE2_DRAW_TEXTURED_RECT_SRG);
    }

    private static boolean instrumentAe2GuiTextField(ClassNode classNode) {
        for (MethodNode method : classNode.methods) {
            if (!AE2_DRAW_TEXT_FIELD_DESC.equals(method.desc)
                    || !matches(method.name, AE2_DRAW_TEXT_FIELD_NAME, AE2_DRAW_TEXT_FIELD_SRG)) {
                continue;
            }

            int colorConstants = 0;
            for (AbstractInsnNode instruction = method.instructions.getFirst(); instruction != null; instruction = instruction.getNext()) {
                if (isAe2SearchFieldColor(instruction)) {
                    colorConstants++;
                }
            }
            if (colorConstants != 2) {
                return false;
            }

            for (AbstractInsnNode instruction = method.instructions.getFirst(); instruction != null; ) {
                AbstractInsnNode next = instruction.getNext();
                if (isAe2SearchFieldColor(instruction)) {
                    method.instructions.insert(instruction, new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            AE2_ICON_HOOKS_CLASS,
                            "adjustSearchFieldBackgroundColor",
                            "(I)I",
                            false
                    ));
                }
                instruction = next;
            }
            return true;
        }
        return false;
    }

    private static boolean isAe2SearchFieldColor(AbstractInsnNode instruction) {
        if (!(instruction instanceof LdcInsnNode)) {
            return false;
        }
        Object constant = ((LdcInsnNode) instruction).cst;
        return constant instanceof Integer
                && (((Integer) constant) == -10461088 || ((Integer) constant) == -5723992);
    }

    private static boolean instrumentPncButtonTextureIcon(ClassNode classNode) {
        for (MethodNode method : classNode.methods) {
            if (!AE2_DRAW_BUTTON_DESC.equals(method.desc)
                    || !matches(method.name, AE2_DRAW_BUTTON_NAME, AE2_DRAW_BUTTON_SRG)) {
                continue;
            }

            MethodInsnNode iconDraw = null;
            for (AbstractInsnNode instruction = method.instructions.getFirst(); instruction != null; instruction = instruction.getNext()) {
                if (isPncCustomTextureDraw(instruction)) {
                    if (iconDraw != null) {
                        return false;
                    }
                    iconDraw = (MethodInsnNode) instruction;
                }
            }
            if (iconDraw == null) {
                return false;
            }

            method.instructions.insertBefore(iconDraw, contextCall("suppressTextureDarkening"));
            method.instructions.insert(iconDraw, contextCall("allowTextureDarkening"));
            return true;
        }
        return false;
    }

    private static boolean isPncCustomTextureDraw(AbstractInsnNode instruction) {
        if (!(instruction instanceof MethodInsnNode)) {
            return false;
        }
        MethodInsnNode methodCall = (MethodInsnNode) instruction;
        return ("net/minecraft/client/gui/Gui".equals(methodCall.owner)
                || PNC_GUI_BUTTON_SPECIAL_CLASS.replace('.', '/').equals(methodCall.owner))
                && PNC_DRAW_CUSTOM_TEXTURE_DESC.equals(methodCall.desc)
                && matches(methodCall.name, PNC_DRAW_CUSTOM_TEXTURE_NAME, PNC_DRAW_CUSTOM_TEXTURE_SRG);
    }

    private static boolean instrumentTexturePreservingMethod(ClassNode classNode, String methodName, String methodDesc) {
        for (MethodNode method : classNode.methods) {
            if (!methodName.equals(method.name) || !methodDesc.equals(method.desc)) {
                continue;
            }

            method.instructions.insert(contextCall("suppressTextureDarkening"));
            for (AbstractInsnNode instruction = method.instructions.getFirst(); instruction != null; ) {
                AbstractInsnNode next = instruction.getNext();
                if (instruction.getOpcode() == Opcodes.RETURN) {
                    method.instructions.insertBefore(instruction, contextCall("allowTextureDarkening"));
                }
                instruction = next;
            }
            return true;
        }
        return false;
    }

    private static boolean instrumentPatchouliPageImage(ClassNode classNode) {
        for (MethodNode method : classNode.methods) {
            if (!"render".equals(method.name) || !PATCHOULI_PAGE_IMAGE_RENDER_DESC.equals(method.desc)) {
                continue;
            }

            MethodInsnNode imageDraw = null;
            for (AbstractInsnNode instruction = method.instructions.getFirst(); instruction != null; instruction = instruction.getNext()) {
                if (isPatchouliPageImageDraw(instruction)) {
                    if (imageDraw != null) {
                        return false;
                    }
                    imageDraw = (MethodInsnNode) instruction;
                }
            }
            if (imageDraw == null) {
                return false;
            }

            method.instructions.insertBefore(imageDraw, contextCall("suppressTextureDarkening"));
            method.instructions.insert(imageDraw, contextCall("allowTextureDarkening"));
            return true;
        }
        return false;
    }

    private static boolean isPatchouliPageImageDraw(AbstractInsnNode instruction) {
        if (!(instruction instanceof MethodInsnNode)) {
            return false;
        }
        MethodInsnNode methodCall = (MethodInsnNode) instruction;
        return "vazkii/patchouli/client/book/gui/GuiBookEntry".equals(methodCall.owner)
                && AE2_DRAW_TEXTURED_RECT_DESC.equals(methodCall.desc)
                && matches(methodCall.name, AE2_DRAW_TEXTURED_RECT_NAME, AE2_DRAW_TEXTURED_RECT_SRG);
    }

    private static MethodInsnNode contextCall(String methodName) {
        return new MethodInsnNode(Opcodes.INVOKESTATIC, RENDER_CONTEXT_CLASS, methodName, "()V", false);
    }

    private static boolean makeNamedFieldPublic(ClassNode classNode, String deobfName, String srgName) {
        boolean changed = false;
        for (FieldNode field : classNode.fields) {
            if (matches(field.name, deobfName, srgName) && makePublic(field)) {
                changed = true;
            }
        }
        return changed;
    }

    private static boolean isInitMissingImage(MethodNode method) {
        if (!INIT_MISSING_IMAGE_DESC.equals(method.desc)) {
            return false;
        }
        return INIT_MISSING_IMAGE_NAME.equals(method.name) || INIT_MISSING_IMAGE_SRG.equals(method.name);
    }

    private static boolean isRenderModelMethod(MethodNode method) {
        if (RENDER_MODEL_COLOR_DESC.equals(method.desc)) {
            return RENDER_MODEL_COLOR_NAME.equals(method.name) || RENDER_MODEL_COLOR_SRG.equals(method.name);
        }
        if (RENDER_MODEL_STACK_DESC.equals(method.desc)) {
            return RENDER_MODEL_STACK_NAME.equals(method.name) || RENDER_MODEL_STACK_SRG.equals(method.name);
        }
        return false;
    }

    private static boolean makeProtected(FieldNode field) {
        int oldAccess = field.access;
        field.access &= ~(Opcodes.ACC_PRIVATE | Opcodes.ACC_PUBLIC);
        field.access |= Opcodes.ACC_PROTECTED;
        return oldAccess != field.access;
    }

    private static boolean makeProtected(MethodNode method) {
        int oldAccess = method.access;
        method.access &= ~(Opcodes.ACC_PRIVATE | Opcodes.ACC_PUBLIC);
        method.access |= Opcodes.ACC_PROTECTED;
        return oldAccess != method.access;
    }

    private static boolean makePublic(FieldNode field) {
        int oldAccess = field.access;
        field.access &= ~(Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED);
        field.access |= Opcodes.ACC_PUBLIC;
        return oldAccess != field.access;
    }

    private static boolean makePublic(MethodNode method) {
        int oldAccess = method.access;
        method.access &= ~(Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED);
        method.access |= Opcodes.ACC_PUBLIC;
        return oldAccess != method.access;
    }
}

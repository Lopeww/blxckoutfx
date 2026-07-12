#version 150

uniform sampler2D Sampler0;
uniform vec4 ColorModulator;
uniform vec3 PerceptionScale;
uniform float DivideFactor;
uniform float Ae2ButtonPaletteMode;
uniform float Ae2GlobalRemapMode;
uniform float Ae2IconRenderMode;
uniform float Ae2TransferButtonMode;
uniform vec4 Ae2TransferButtonRect;
uniform float Ae2SortButtonMode;
uniform vec4 Ae2SortButtonRect;
uniform vec4 Ae2ScrollTrackRect;
uniform vec4 Ae2CraftingSectionRect;
uniform vec4 Ae2MolecularAssemblerGridRect;
uniform float VanillaIconButtonMode;
uniform vec4 VanillaIconButtonRect;
uniform float PneumaticColorWhitelistEnabled;
uniform float PneumaticColorWhitelistCount;
uniform vec3 PneumaticColorWhitelist0;
uniform vec3 PneumaticColorWhitelist1;
uniform vec3 PneumaticColorWhitelist2;
uniform vec3 PneumaticColorWhitelist3;
uniform vec3 PneumaticColorWhitelist4;
uniform vec3 PneumaticColorWhitelist5;
uniform vec3 PneumaticColorWhitelist6;
uniform vec3 PneumaticColorWhitelist7;
uniform vec3 PneumaticColorWhitelist8;
uniform vec3 PneumaticColorWhitelist9;
uniform vec3 PneumaticColorWhitelist10;
uniform vec3 PneumaticColorWhitelist11;

in vec2 texCoord0;

out vec4 fragColor;

bool matchesColor(vec3 sampleColor, vec3 expectedColor) {
    return all(lessThan(abs(sampleColor - expectedColor), vec3(0.002)));
}

bool matchesColorTol(vec3 sampleColor, vec3 expectedColor, float tolerance) {
    return all(lessThanEqual(abs(sampleColor - expectedColor), vec3(tolerance)));
}

vec3 applyAe2GlobalRemap(vec3 rgb) {
    vec3 mapped = rgb;

    
    
    
    if (Ae2TransferButtonMode > 0.5) {
        if (matchesColorTol(mapped, vec3(0.301961, 0.301961, 0.403922), 0.020)) {
            mapped = vec3(0.050980, 0.050980, 0.074510); 
        } else if (matchesColorTol(mapped, vec3(0.411765, 0.427451, 0.533333), 0.020)) {
            mapped = vec3(0.078431, 0.082353, 0.113725); 
        } else if (matchesColorTol(mapped, vec3(0.949020, 0.949020, 0.949020), 0.020)) {
            mapped = vec3(0.949020, 0.949020, 0.949020); 
        }
        return mapped;
    }

    if (matchesColorTol(mapped, vec3(0.254902, 0.247059, 0.329412), 0.020)) {
        mapped = vec3(0.039216, 0.035294, 0.054902); 
    }
    if (matchesColorTol(mapped, vec3(0.529412, 0.560784, 0.647059), 0.020)) {
        mapped = vec3(0.113725, 0.125490, 0.160784); 
    }
    if (matchesColorTol(mapped, vec3(0.949020, 0.949020, 0.949020), 0.020)) {
        mapped = vec3(0.556863, 0.556863, 0.556863); 
    }
    if (matchesColorTol(mapped, vec3(0.796078, 0.800000, 0.831373), 0.020)) {
        mapped = vec3(0.258824, 0.262745, 0.294118); 
    }
    if (matchesColorTol(mapped, vec3(0.870588, 0.874510, 0.890196), 0.020)) {
        mapped = vec3(0.258824, 0.262745, 0.294118); 
    }
    if (matchesColorTol(mapped, vec3(0.678431, 0.690196, 0.768627), 0.020)) {
        mapped = vec3(0.176471, 0.184314, 0.235294); 
    }
    if (matchesColorTol(mapped, vec3(0.603922, 0.623529, 0.705882), 0.020)) {
        mapped = vec3(0.141176, 0.149020, 0.192157); 
    }
    if (matchesColorTol(mapped, vec3(0.411765, 0.427451, 0.533333), 0.020)) {
        mapped = vec3(0.078431, 0.082353, 0.113725); 
    }
    if (matchesColorTol(mapped, vec3(0.301961, 0.301961, 0.403922), 0.020)) {
        mapped = vec3(0.050980, 0.050980, 0.074510); 
    }

    return mapped;
}

vec4 finalizeColor(vec4 color) {
    if (Ae2GlobalRemapMode > 0.5) {
        color.rgb = applyAe2GlobalRemap(color.rgb);
    }
    return color * ColorModulator;
}

vec3 applyAe2MolecularAssemblerSlot(vec3 rgb) {
    float strength = clamp((DivideFactor - 1.0) / 4.5, 0.0, 1.0);
    if (strength <= 0.0) {
        return rgb;
    }

    if (matchesColorTol(rgb, vec3(0.254902, 0.247059, 0.329412), 0.025)) {
        return rgb; 
    }
    if (matchesColorTol(rgb, vec3(1.000000, 0.000000, 0.666667), 0.035)) {
        return mix(rgb, vec3(0.254902, 0.247059, 0.329412), strength); 
    }
    if (matchesColorTol(rgb, vec3(1.000000, 0.745098, 0.666667), 0.035)) {
        return mix(rgb, vec3(0.219608, 0.219608, 0.247059), strength); 
    }
    if (matchesColorTol(rgb, vec3(0.678431, 0.690196, 0.768627), 0.025)) {
        return mix(rgb, vec3(0.219608, 0.219608, 0.247059), strength); 
    }
    if (matchesColorTol(rgb, vec3(0.603922, 0.623529, 0.705882), 0.025)) {
        return mix(rgb, vec3(0.192157, 0.200000, 0.223529), strength); 
    }
    if (matchesColorTol(rgb, vec3(0.949020, 0.949020, 0.949020), 0.025)) {
        return mix(rgb, vec3(0.219608, 0.219608, 0.247059), strength); 
    }
    if (matchesColorTol(rgb, vec3(0.796078, 0.800000, 0.831373), 0.025)) {
        return mix(rgb, vec3(0.192157, 0.200000, 0.223529), strength); 
    }

    return rgb;
}

bool isInVanillaIconButtonRect() {
    return VanillaIconButtonRect.z > VanillaIconButtonRect.x
            && VanillaIconButtonRect.w > VanillaIconButtonRect.y
            && gl_FragCoord.x >= VanillaIconButtonRect.x
            && gl_FragCoord.x < VanillaIconButtonRect.z
            && gl_FragCoord.y >= VanillaIconButtonRect.y
            && gl_FragCoord.y < VanillaIconButtonRect.w;
}

bool isInAccessibilityIconCore() {
    vec2 size = VanillaIconButtonRect.zw - VanillaIconButtonRect.xy;
    if (size.x <= 0.0 || size.y <= 0.0) {
        return false;
    }

    vec2 local = (gl_FragCoord.xy - VanillaIconButtonRect.xy) / size;
    float dist = distance(local, vec2(0.5, 0.5));
    return dist <= 0.395;
}

bool isInModMenuConfigureIconCore() {
    vec2 size = VanillaIconButtonRect.zw - VanillaIconButtonRect.xy;
    if (size.x <= 0.0 || size.y <= 0.0) {
        return false;
    }

    vec2 local = (gl_FragCoord.xy - VanillaIconButtonRect.xy) / size;
    return local.x >= 0.18 && local.x <= 0.86
            && local.y >= 0.14 && local.y <= 0.82;
}

bool isInModMenuFilterIconCore() {
    vec2 size = VanillaIconButtonRect.zw - VanillaIconButtonRect.xy;
    if (size.x <= 0.0 || size.y <= 0.0) {
        return false;
    }

    vec2 local = (gl_FragCoord.xy - VanillaIconButtonRect.xy) / size;
    return local.x >= 0.18 && local.x <= 0.82
            && local.y >= 0.18 && local.y <= 0.78;
}

bool isModMenuLightIconColor(vec3 rgb) {
    return matchesColorTol(rgb, vec3(1.000000, 1.000000, 1.000000), 0.018)
            || matchesColorTol(rgb, vec3(0.925490, 0.925490, 0.925490), 0.018)
            || matchesColorTol(rgb, vec3(0.870588, 0.870588, 0.870588), 0.018)
            || matchesColorTol(rgb, vec3(0.772549, 0.772549, 0.772549), 0.018);
}

bool isModMenuConfigureWarningIconColor(vec3 rgb) {
    return matchesColorTol(rgb, vec3(1.000000, 0.345098, 0.345098), 0.018)
            || matchesColorTol(rgb, vec3(0.933333, 0.250980, 0.250980), 0.018)
            || matchesColorTol(rgb, vec3(0.843137, 0.203922, 0.203922), 0.018);
}

bool shouldPreserveVanillaIconButtonColor(vec3 rgb) {
    if (VanillaIconButtonMode > 0.5 && VanillaIconButtonMode < 1.5) {
        return matchesColorTol(rgb, vec3(0.815686, 0.815686, 0.815686), 0.018)
                || matchesColorTol(rgb, vec3(1.000000, 1.000000, 1.000000), 0.018)
                || matchesColorTol(rgb, vec3(0.207843, 0.207843, 0.207843), 0.018)
                || matchesColorTol(rgb, vec3(0.894118, 0.917647, 1.000000), 0.018)
                || matchesColorTol(rgb, vec3(0.823529, 0.854902, 1.000000), 0.018)
                || matchesColorTol(rgb, vec3(0.784314, 0.815686, 0.996078), 0.018)
                || matchesColorTol(rgb, vec3(0.227451, 0.784314, 0.070588), 0.018)
                || matchesColorTol(rgb, vec3(0.117647, 0.741176, 0.000000), 0.018)
                || matchesColorTol(rgb, vec3(0.235294, 0.317647, 0.796078), 0.018)
                || matchesColorTol(rgb, vec3(0.360784, 0.466667, 0.894118), 0.018)
                || matchesColorTol(rgb, vec3(0.000000, 0.694118, 0.000000), 0.018)
                || matchesColorTol(rgb, vec3(0.513725, 0.623529, 0.960784), 0.018)
                || matchesColorTol(rgb, vec3(0.447059, 0.882353, 0.282353), 0.018)
                || matchesColorTol(rgb, vec3(0.862745, 0.890196, 1.000000), 0.018)
                || matchesColorTol(rgb, vec3(0.196078, 0.262745, 0.752941), 0.018)
                || matchesColorTol(rgb, vec3(0.168627, 0.215686, 0.705882), 0.018);
    }

    if (VanillaIconButtonMode > 1.5 && VanillaIconButtonMode < 2.5) {
        if (!isInAccessibilityIconCore()) {
            return false;
        }

        return matchesColorTol(rgb, vec3(0.988235, 0.988235, 0.988235), 0.018)
                || matchesColorTol(rgb, vec3(0.000000, 0.000000, 0.000000), 0.018)
                || matchesColorTol(rgb, vec3(0.666667, 0.666667, 0.666667), 0.018)
                || matchesColorTol(rgb, vec3(0.164706, 0.164706, 0.164706), 0.018)
                || matchesColorTol(rgb, vec3(0.839216, 0.839216, 0.839216), 0.018);
    }

    if (VanillaIconButtonMode > 2.5 && VanillaIconButtonMode < 3.5) {
        return isInModMenuConfigureIconCore()
                && (isModMenuLightIconColor(rgb) || isModMenuConfigureWarningIconColor(rgb));
    }

    if (VanillaIconButtonMode > 3.5 && VanillaIconButtonMode < 4.5) {
        return isInModMenuFilterIconCore() && isModMenuLightIconColor(rgb);
    }

    return false;
}

bool matchesPneumaticWhitelistColor(vec3 rgb) {
    if (PneumaticColorWhitelistEnabled < 0.5 || PneumaticColorWhitelistCount < 0.5) {
        return false;
    }

    if (PneumaticColorWhitelistCount > 0.5 && matchesColorTol(rgb, PneumaticColorWhitelist0, 0.018)) return true;
    if (PneumaticColorWhitelistCount > 1.5 && matchesColorTol(rgb, PneumaticColorWhitelist1, 0.018)) return true;
    if (PneumaticColorWhitelistCount > 2.5 && matchesColorTol(rgb, PneumaticColorWhitelist2, 0.018)) return true;
    if (PneumaticColorWhitelistCount > 3.5 && matchesColorTol(rgb, PneumaticColorWhitelist3, 0.018)) return true;
    if (PneumaticColorWhitelistCount > 4.5 && matchesColorTol(rgb, PneumaticColorWhitelist4, 0.018)) return true;
    if (PneumaticColorWhitelistCount > 5.5 && matchesColorTol(rgb, PneumaticColorWhitelist5, 0.018)) return true;
    if (PneumaticColorWhitelistCount > 6.5 && matchesColorTol(rgb, PneumaticColorWhitelist6, 0.018)) return true;
    if (PneumaticColorWhitelistCount > 7.5 && matchesColorTol(rgb, PneumaticColorWhitelist7, 0.018)) return true;
    if (PneumaticColorWhitelistCount > 8.5 && matchesColorTol(rgb, PneumaticColorWhitelist8, 0.018)) return true;
    if (PneumaticColorWhitelistCount > 9.5 && matchesColorTol(rgb, PneumaticColorWhitelist9, 0.018)) return true;
    if (PneumaticColorWhitelistCount > 10.5 && matchesColorTol(rgb, PneumaticColorWhitelist10, 0.018)) return true;
    if (PneumaticColorWhitelistCount > 11.5 && matchesColorTol(rgb, PneumaticColorWhitelist11, 0.018)) return true;

    return false;
}

void main() {
    vec4 color = texture(Sampler0, texCoord0);

    if (color.a == 0.0) {
        discard;
    }

    if (matchesPneumaticWhitelistColor(color.rgb)) {
        fragColor = color * ColorModulator;
        return;
    }

    if (isInVanillaIconButtonRect() && shouldPreserveVanillaIconButtonColor(color.rgb)) {
        fragColor = color * ColorModulator;
        return;
    }

    if (Ae2IconRenderMode > 2.5) {
        color.rgb = min(color.rgb * 4.0, vec3(0.968627));
        fragColor = color * ColorModulator;
        return;
    }

    if (Ae2IconRenderMode > 1.5) {
        color.rgb = vec3(0.968627);
        fragColor = color * ColorModulator;
        return;
    }

    if (Ae2IconRenderMode > 0.5) {
        fragColor = color * ColorModulator;
        return;
    }

    
    
    
    
    
    if (Ae2SortButtonMode > 0.5
            && Ae2SortButtonRect.z > Ae2SortButtonRect.x
            && Ae2SortButtonRect.w > Ae2SortButtonRect.y
            && gl_FragCoord.x >= Ae2SortButtonRect.x
            && gl_FragCoord.x < Ae2SortButtonRect.z
            && gl_FragCoord.y >= Ae2SortButtonRect.y
            && gl_FragCoord.y < Ae2SortButtonRect.w) {
        if (matchesColorTol(color.rgb, vec3(0.250980, 0.250980, 0.250980), 0.014)) {
            color.rgb = vec3(0.603922, 0.603922, 0.603922);
            fragColor = color * ColorModulator;
            return;
        }
        if (matchesColorTol(color.rgb, vec3(0.200000, 0.200000, 0.200000), 0.014)) {
            color.rgb = vec3(0.541176, 0.541176, 0.541176);
            fragColor = color * ColorModulator;
            return;
        }
        if (matchesColorTol(color.rgb, vec3(0.101961, 0.101961, 0.101961), 0.012)) {
            color.rgb = vec3(0.478431, 0.478431, 0.478431);
            fragColor = color * ColorModulator;
            return;
        }
        if (matchesColorTol(color.rgb, vec3(0.000000, 0.000000, 0.000000), 0.010)) {
            color.rgb = vec3(0.666667, 0.666667, 0.666667);
            fragColor = color * ColorModulator;
            return;
        }
    }

    if (Ae2MolecularAssemblerGridRect.z > Ae2MolecularAssemblerGridRect.x
            && Ae2MolecularAssemblerGridRect.w > Ae2MolecularAssemblerGridRect.y
            && gl_FragCoord.x >= Ae2MolecularAssemblerGridRect.x
            && gl_FragCoord.x < Ae2MolecularAssemblerGridRect.z
            && gl_FragCoord.y >= Ae2MolecularAssemblerGridRect.y
            && gl_FragCoord.y < Ae2MolecularAssemblerGridRect.w) {
        color.rgb = applyAe2MolecularAssemblerSlot(color.rgb);
        vec4 outputColor = color * ColorModulator;
        outputColor.a = 1.0;
        fragColor = outputColor;
        return;
    }

    if (Ae2ButtonPaletteMode > 0.5) {
        if (Ae2ButtonPaletteMode > 6.5) {
            bool validSectionRect = Ae2CraftingSectionRect.z > Ae2CraftingSectionRect.x
                    && Ae2CraftingSectionRect.w > Ae2CraftingSectionRect.y;
            bool inCraftingSection = validSectionRect
                    && gl_FragCoord.x >= Ae2CraftingSectionRect.x
                    && gl_FragCoord.x < Ae2CraftingSectionRect.z
                    && gl_FragCoord.y >= Ae2CraftingSectionRect.y
                    && gl_FragCoord.y < Ae2CraftingSectionRect.w;

            if (matchesColor(color.rgb, vec3(0.254902, 0.247059, 0.329412))) {
                color.rgb = vec3(0.039216, 0.035294, 0.054902); 
            }
            if (inCraftingSection && matchesColor(color.rgb, vec3(0.949020, 0.949020, 0.949020))) {
                color.rgb = vec3(0.556863, 0.556863, 0.556863); 
            }
            fragColor = finalizeColor(color);
            return;
        }

        if (Ae2ButtonPaletteMode > 5.5) {
            if (matchesColor(color.rgb, vec3(0.949020, 0.949020, 0.949020))) {
                color.rgb = vec3(0.556863, 0.556863, 0.556863); 
            } else if (matchesColor(color.rgb, vec3(0.411765, 0.427451, 0.533333))) {
                color.rgb = vec3(0.078431, 0.082353, 0.113725); 
            } else if (matchesColor(color.rgb, vec3(0.603922, 0.623529, 0.705882))) {
                color.rgb = vec3(0.141176, 0.149020, 0.192157); 
            }
            fragColor = finalizeColor(color);
            return;
        }

        if (Ae2ButtonPaletteMode > 4.5) {
            if (matchesColor(color.rgb, vec3(0.949020, 0.949020, 0.949020))
                    || matchesColor(color.rgb, vec3(0.235294, 0.498039, 0.870588))) {
                
            } else if (matchesColor(color.rgb, vec3(0.301961, 0.301961, 0.403922))) {
                color.rgb = vec3(0.050980, 0.050980, 0.074510); 
            } else if (matchesColor(color.rgb, vec3(0.254902, 0.247059, 0.329412))) {
                color.rgb = vec3(0.039216, 0.035294, 0.054902); 
            } else if (matchesColor(color.rgb, vec3(0.086275, 0.082353, 0.109804))) {
                color.rgb = vec3(0.039216, 0.035294, 0.054902); 
            } else if (matchesColor(color.rgb, vec3(0.411765, 0.427451, 0.533333))) {
                color.rgb = vec3(0.078431, 0.082353, 0.113725); 
            } else if (matchesColor(color.rgb, vec3(0.603922, 0.623529, 0.705882))) {
                color.rgb = vec3(0.141176, 0.149020, 0.192157); 
            } else if (matchesColor(color.rgb, vec3(0.678431, 0.690196, 0.768627))) {
                color.rgb = vec3(0.176471, 0.184314, 0.235294); 
            }
            fragColor = finalizeColor(color);
            return;
        }

        if (Ae2ButtonPaletteMode > 3.5) {
            bool applyEverywhere = Ae2ScrollTrackRect.z <= Ae2ScrollTrackRect.x || Ae2ScrollTrackRect.w <= Ae2ScrollTrackRect.y;
            bool inTrack = applyEverywhere || (gl_FragCoord.x >= Ae2ScrollTrackRect.x
                    && gl_FragCoord.x < Ae2ScrollTrackRect.z
                    && gl_FragCoord.y >= Ae2ScrollTrackRect.y
                    && gl_FragCoord.y < Ae2ScrollTrackRect.w);

            if (inTrack) {
                if (matchesColor(color.rgb, vec3(0.949020, 0.949020, 0.949020))) {
                    color.rgb = vec3(0.556863, 0.556863, 0.556863); 
                } else if (matchesColor(color.rgb, vec3(0.603922, 0.623529, 0.705882))) {
                    color.rgb = vec3(0.141176, 0.149020, 0.192157); 
                } else if (matchesColor(color.rgb, vec3(0.411765, 0.427451, 0.533333))) {
                    color.rgb = vec3(0.078431, 0.082353, 0.113725); 
                }
            }
            fragColor = finalizeColor(color);
            return;
        }

        if (Ae2ButtonPaletteMode < 1.5) {
            if (matchesColor(color.rgb, vec3(0.254902, 0.247059, 0.329412))) {
                color.rgb = vec3(0.039216, 0.035294, 0.054902); 
            } else if (matchesColor(color.rgb, vec3(0.086275, 0.082353, 0.109804))) {
                color.rgb = vec3(0.039216, 0.035294, 0.054902); 
            } else if (matchesColor(color.rgb, vec3(0.301961, 0.301961, 0.403922))) {
                color.rgb = vec3(0.050980, 0.050980, 0.074510); 
            } else if (matchesColor(color.rgb, vec3(0.411765, 0.427451, 0.533333))) {
                color.rgb = vec3(0.078431, 0.082353, 0.113725); 
            } else if (matchesColor(color.rgb, vec3(0.603922, 0.623529, 0.705882))) {
                color.rgb = vec3(0.141176, 0.149020, 0.192157); 
            } else if (matchesColor(color.rgb, vec3(0.192157, 0.200000, 0.223529))) {
                color.rgb = vec3(0.141176, 0.149020, 0.192157); 
            } else if (matchesColor(color.rgb, vec3(0.678431, 0.690196, 0.768627))) {
                color.rgb = vec3(0.176471, 0.184314, 0.235294); 
            } else if (matchesColor(color.rgb, vec3(0.219608, 0.219608, 0.247059))) {
                color.rgb = vec3(0.176471, 0.184314, 0.235294); 
            } else if (matchesColor(color.rgb, vec3(0.141176, 0.145098, 0.180392))) {
                color.rgb = vec3(0.078431, 0.082353, 0.113725); 
            }
        } else if (Ae2ButtonPaletteMode < 2.5) {
            if (matchesColor(color.rgb, vec3(0.254902, 0.247059, 0.329412))) {
                color.rgb = vec3(0.039216, 0.035294, 0.054902); 
            } else if (matchesColor(color.rgb, vec3(0.086275, 0.082353, 0.109804))) {
                color.rgb = vec3(0.039216, 0.035294, 0.054902); 
            } else if (matchesColor(color.rgb, vec3(0.529412, 0.560784, 0.647059))) {
                color.rgb = vec3(0.113725, 0.125490, 0.160784); 
            } else if (matchesColor(color.rgb, vec3(0.301961, 0.301961, 0.403922))) {
                color.rgb = vec3(0.050980, 0.050980, 0.074510); 
            } else if (matchesColor(color.rgb, vec3(0.411765, 0.427451, 0.533333))) {
                color.rgb = vec3(0.078431, 0.082353, 0.113725); 
            } else if (matchesColor(color.rgb, vec3(0.141176, 0.145098, 0.180392))) {
                color.rgb = vec3(0.078431, 0.082353, 0.113725); 
            } else if (matchesColor(color.rgb, vec3(0.219608, 0.219608, 0.247059))) {
                color.rgb = vec3(0.176471, 0.184314, 0.235294); 
            } else if (matchesColor(color.rgb, vec3(0.192157, 0.200000, 0.223529))) {
                color.rgb = vec3(0.141176, 0.149020, 0.192157); 
            }
        } else if (Ae2ButtonPaletteMode < 3.5) {
            if (matchesColor(color.rgb, vec3(0.254902, 0.247059, 0.329412))) {
                color.rgb = vec3(0.039216, 0.035294, 0.054902); 
            } else if (matchesColor(color.rgb, vec3(0.529412, 0.560784, 0.647059))) {
                color.rgb = vec3(0.113725, 0.125490, 0.160784); 
            } else if (matchesColor(color.rgb, vec3(0.411765, 0.427451, 0.533333))) {
                color.rgb = vec3(0.078431, 0.082353, 0.113725); 
            } else if (matchesColor(color.rgb, vec3(0.678431, 0.690196, 0.768627))) {
                color.rgb = vec3(0.113725, 0.125490, 0.160784); 
            } else if (matchesColor(color.rgb, vec3(0.603922, 0.623529, 0.705882))) {
                color.rgb = vec3(0.078431, 0.082353, 0.113725); 
            } else if (matchesColor(color.rgb, vec3(0.141176, 0.145098, 0.180392))) {
                color.rgb = vec3(0.078431, 0.082353, 0.113725); 
            }
        }

        fragColor = finalizeColor(color);
        return;
    }

    float grey = dot(color.rgb, PerceptionScale);
    vec3 dif = abs(color.rgb - vec3(grey));
    vec3 scaled = pow(dif, 1.0 - PerceptionScale);
    float perceivedSaturation = length(scaled);
    color.rgb /= mix(DivideFactor, 1.0, pow(perceivedSaturation, 0.35));

    if (DivideFactor >= 5.55) {
        
        vec3 c = max(vec3(0.0), color.rgb - vec3(0.018));
        c = pow(c, vec3(1.045));
        c = clamp((c - 0.5) * 1.12 + 0.5, 0.0, 1.0);
        vec3 midLift = vec3(0.012) * smoothstep(vec3(0.07), vec3(0.30), c);
        c = min(vec3(1.0), c + midLift);

        
        float luma = dot(c, vec3(0.299, 0.587, 0.114));
        float sat = length(c - vec3(luma));
        float neutralMask = 1.0 - smoothstep(0.055, 0.165, sat);

        float slotFillBand = smoothstep(0.07, 0.18, luma) * (1.0 - smoothstep(0.18, 0.31, luma));
        float slotGridBand = smoothstep(0.17, 0.30, luma) * (1.0 - smoothstep(0.30, 0.46, luma));
        float buttonBand = smoothstep(0.23, 0.37, luma) * (1.0 - smoothstep(0.37, 0.55, luma));

        c = mix(c, vec3(0.133, 0.137, 0.176), neutralMask * slotFillBand * 0.60); 
        c = mix(c, vec3(0.196, 0.196, 0.220), neutralMask * slotGridBand * 0.56); 
        c = mix(c, vec3(0.176, 0.184, 0.235), neutralMask * buttonBand * 0.40);   

        c = vec3(c.r * 0.988, c.g * 0.992, min(1.0, c.b * 1.018));
        color.rgb = c;
    }

    fragColor = finalizeColor(color);
}

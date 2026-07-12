# Changelog

## 1.0.1 - Forge Release Bootstrap Fix

- Bootstrapped Mixin through the production LaunchWrapper tweaker instead of the coremod constructor.
- Embedded and verified the complete Mixin 0.8.3 LaunchWrapper runtime in the release jar.
- Updated the Forge target to 14.23.5.2860.
- Split JEI and JourneyMap mixins into a classpath-gated optional config.
- Prevented optional mod classes from being embedded into release jars.
- Generated the release refmap from real MCP-to-SRG mappings instead of identity mappings.
- Moved the FontRenderer color hook to the stable drawString boundary for SmoothFont compatibility.
- Replaced GuiButtonToggle field shadows with an initTextureValues argument hook for production-safe texture detection.
- Replaced GuiButtonImage field shadows with constructor argument capture while preserving recipe-book icon handling.
- Added descriptors to every required vanilla injection and removed the beacon button ordinal dependency.
- Added class-and-method presence checks for every JEI and JourneyMap compat mixin.
- Restricted optional ASM integrations to the client and removed unused vanilla access transformations.
- Moved client initialization behind a sided proxy to avoid dedicated-server client class resolution.
- Generated refmaps only in the clean build directory and expanded release bytecode, annotation, config, and refmap validation.

## 1.0.0 - Initial Release

- Released BlxckoutFX.
- Configured the FontMixin rendering behavior.
- Added a draggable inventory button.
- Added mod integrations.
- Added the Minecraft 1.12.2 port.

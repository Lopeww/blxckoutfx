# Changelog

## 2.0.0 - Minecraft 26.2 Release

- Ported BlxckoutFX 2.0.0 to Minecraft 26.2 for Fabric and NeoForge.

## 2.0.0 - Major Update (Minecraft 26.1.2, 1.21.1, and 1.20.1)

### New

- Rebuilt the configuration experience with a searchable sidebar, organized settings, live preset previews, and saved category selection.
- Added Fabric Mod Menu integration for direct access to the BlxckoutFX configuration screen.
- Expanded the inventory button with Classic and Short styles, four corner presets, custom placement, and a position lock.
- Added inventory-button shortcuts: left-click for the next theme, right-click for the previous theme, middle-click to open configuration, and drag to move when unlocked.
- Added an AE2 theme selector with Blxckout and Blackout modes. Blackout can be toggled directly from the inventory button in AE2 screens.
- Set the default AE2 theme mode to Blackout.
- AE2 Blackout is available only for Minecraft 1.21.1 and newer versions.
- Added dedicated AE2 GUI, text, icon, tooltip, slot, scrollbar, and add-on compatibility handling for supported 1.21.1+ versions.

### Improved

- Reworked GUI rendering internals and shader pipeline integration for more reliable darkening and text contrast.
- Improved JourneyMap rendering, including waypoint controls, categories, themes, logo treatment, and web-map controls.
- Improved compatibility with JEI, FTB Quests, and Mod Menu so icons and buttons retain their intended visibility and layering.
- Migrates legacy inventory-button coordinates to the new anchored position system automatically.

### Notes

- Applied Energistics 2 does not provide Fabric builds, so the AE2 integration is unavailable when running BlxckoutFX on Fabric.
- Minecraft 26.1.2, 1.21.1 and 1.20.1 receive the 2.0.0 update; the completed 1.12.2 port releases as version 1.0.0.

## 1.0.0 - Initial Release

- Released BlxckoutFX.
- Configured the FontMixin rendering behavior.
- Added a draggable inventory button.
- Added mod integrations.
- Added the Minecraft 1.20.1 port.

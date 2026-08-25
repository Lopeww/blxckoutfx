# Changelog

## 2.1.1

- Fixed startup issues affecting Forge 1.20.1.
- Fixed several Recipe Book compatibility issues.
- Improved overall Forge stability.

## 2.1.0

### Reload:

- Added Developer Options with quick reload controls for BlxckoutFX.
- Reload client state, resources, or both without restarting the game.
- Makes testing changes during development faster and more convenient.

### Lang:

- Added Turkish, Ukrainian, and Russian translations.

### Optimization:

- Reduced the mod's file size by removing unnecessary overhead and optimizing assets.

### WHAT'S NEXT:

- Started working on a new major version of BlxckoutFX.
- It will completely change the core architecture behind how the mod works.
- The mod will keep the same purpose and functionality, but handle it in a much smarter way.
- Instead of continuing to add more and more mod-specific fixes, I'm working on a system that will make compatibility much easier for both developers and myself.
- All other supported Minecraft versions will eventually be updated to use this new system.

### Patchouli Fix:

- Patchouli screens now force the inventory button to use the Short style.
- Images inside books, including UI elements, are no longer incorrectly darkened.
- Added fixes to prevent certain buttons and icons from being incorrectly darkened.
- Fixed incorrect darkening of fluids.

### JEI Fix:

- JEI screens now force the inventory button to use the Short style.
- Fixed incorrect darkening of fluids.

### TerraFirmaCraft Fix:

- Added extensive darkening fixes across general TerraFirmaCraft GUIs.
- Extended compatibility to several TFC addons depending on the Minecraft version, including FirmaLife, Sacks 'N Such, and Firma: Civilization.

## 2.0.0 - Major Update

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

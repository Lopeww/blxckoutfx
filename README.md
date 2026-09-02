<div align="center">
  <img src="./blxckoutfx.png" width="128" height="128" alt="BlxckoutFX Logo">

![Minecraft Versions](https://img.shields.io/badge/MINECRAFT-26.2_%7C_26.1.2_%7C_1.21.1_%7C_1.20.1_%7C_1.12.2-darkgreen?style=for-the-badge)
<br>
![Loaders](https://img.shields.io/badge/Loaders-NeoForge_|_Fabric-orange?style=for-the-badge)
<br>
![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)
</div>

# BlxckoutFX

A lightweight Minecraft mod that applies a consistent dark theme to buttons and in-game GUI surfaces using shader-based rendering.

By transforming the default white panels into a sleek custom **mode**, this shader-based solution brings a refined **dark** aesthetic to your interface **everywhere** you look, delivering an eye-friendly experience across all screens.

*Let darkness fall everywhere for your eyes.*

---

## 📖 About

This is my very first Minecraft mod. I originally created it because bright and white Minecraft interfaces were uncomfortable for my eyes. After building it for myself, I decided to share it with other players who might want the same kind of darker GUI experience.

BlxckoutFX is intentionally focused: it changes GUI rendering, not the world itself. Menu backgrounds, the Minecraft logo, and mod images are kept intact, while buttons and in-game GUI panels receive the darkening effect.

---

## ✨ Features

### ⚙️ Core Principles

- **Shader-Based:** The effect is handled through shader rendering instead of replacing vanilla textures.
- **GUI Focused:** The mod targets interface elements only.
- **Lightweight:** It avoids extra systems and keeps the feature set focused.
- **Menu-Safe:** Main menu backgrounds, logos, and mod list images are not darkened.

### 🎮 Mod Details

- **Button Darkening in Menus:** Menu screens darken buttons only.
- **In-Game GUI Dark Theme:** In-game screens can darken both buttons and GUI panels.
- **Shader-Based Visual Styling:** No manual shader setup is required.
- **Loader Support:** Supports NeoForge and Fabric Loader on this branch.
- **Config Screen:** A redesigned, searchable configuration screen with categories, live preset previews, and persistent settings. It is also available through Mod Menu on Fabric.
- **Enhanced Inventory Button:** Choose Classic or Short styling, use a corner preset or a custom position, and lock the button when its placement is final.
- **Quick Controls:** Left-click cycles forward, right-click cycles backward, middle-click opens the config screen, and drag repositions the unlocked inventory button.
- **AE2 Integration:** Dedicated Applied Energistics 2 rendering fixes and a selectable Blxckout/Blackout theme mode for supported 1.21.1+ versions.
- **Compatibility Fixes:** Improved JourneyMap, JEI, FTB, and Mod Menu GUI/icon handling so supported elements remain readable and correctly layered.
- **Readable Text Handling:** Soft, Balanced, and Dark modes use fixed readable text colors for better consistency.

### 🌑 Available Themes / Modes

- **⚪ Off:** Disables the visual effect.
- **🔘 Soft:** A lighter darkening mode for subtle interface dimming.
- **⚫ Balanced:** The default mode, designed for regular use.
- **⬛ Dark:** A stronger darkening mode for brighter interfaces.
- **👁️‍🗨️ Blxckout:** The strongest preset for users who want the darkest look.

### 🖥️ AE2 Themes

- **Off:** Disables the dedicated AE2 Blackout effect.
- **Blackout:** Applies the dedicated Blackout treatment to supported Applied Energistics 2 screens. It can be toggled directly from the inventory button while an AE2 screen is open.

---

## 📥 Versions

| Minecraft Version | Loader              | Mod Version | Status          |
|------------------|---------------------|-------------|-----------------|
| 26.2             | NeoForge + Fabric   | 2.0.0       | Release         |
| 26.1.2           | NeoForge + Fabric   | 2.0.0       | Main Release    |
| 1.21.1           | NeoForge + Fabric   | 2.1.1       | Release         |
| 1.20.1           | Forge + Fabric      | 2.1.2       | Release         |
| 1.12.2           | Forge               | 1.0.1       | Release         |

---

## 📝 Changelog

[View the full changelog](CHANGELOG.md)

---

## 🚀 Installation

1. Install your target Minecraft version via the Minecraft Launcher.
2. Install NeoForge or Fabric Loader.
3. Place the matching BlxckoutFX jar file into the `mods` folder.
4. Launch the game.

> 💡 **Important Version Notice:** The mod prioritizes support for the latest NeoForge and Fabric versions available for its target Minecraft version. If a compatibility issue appears, update to the minimum loader version listed on the release page.

---

## 📂 Branch Structure

- `main` → Minecraft 26.1.2 main release branch
- `26.2` → Minecraft 26.2 release port branch
- `1.21.1` → Minecraft 1.21.1 support branch
- `1.20.1` → Minecraft 1.20.1 unsupported branch
- `1.12.2` → Minecraft 1.12.2 unsupported branch

---

## 🛠️ Support & Maintenance Policy

> ⚠️ **Note for 1.20.1:** This specific version has no active support. Issues may be fixed in bulk batches or left as-is depending on necessity.

For Minecraft 1.20.1 on Forge, BlxckoutFX 2.1.2 supports JourneyMap 5.10.3 and JourneyMap 6.0.4.

If you encounter bugs, issues, or have suggestions, you can report them through either of the following channels:

- **GitHub Issues:** Open an issue directly in this repository.
- **Discord Server:** Join the community via [Discord](https://discord.gg/rUD6tynDaK).

When reporting an issue, please include:
- Minecraft version
- Loader and loader version
- BlxckoutFX version
- A short description of what happened
- Screenshots if the issue is visual

---

## 💖 Support Me

If you would like to support the project, you can visit my Patreon page:

👉 [Patreon](https://patreon.com/Lopew)

---

## 📜 License

This project is licensed under the MIT License.

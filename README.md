<div align="center">

# WunderLib: New Dawn

**An independently maintained continuation of WunderLib for modern Minecraft versions**

[![CurseForge Downloads](https://img.shields.io/curseforge/dt/1422273?logo=curseforge\&label=CurseForge%20Downloads\&color=F16436)](https://www.curseforge.com/minecraft/mc-mods/wunderlib-neoforge)
[![GitHub Issues](https://img.shields.io/github/issues/Reijin2312/WunderLib-New-Dawn?logo=github)](https://github.com/Reijin2312/WunderLib-New-Dawn/issues)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1%20%7C%201.21.11%20%7C%2026.1.x-62B47A)](https://www.curseforge.com/minecraft/mc-mods/wunderlib-neoforge/files)
[![Loaders](https://img.shields.io/badge/Loaders-NeoForge%20%7C%20Fabric%20%7C%20Quilt-5C6BC0)](https://www.curseforge.com/minecraft/mc-mods/wunderlib-neoforge)
[![Code License](https://img.shields.io/badge/Code-MIT-blue.svg)](LICENSE)

[CurseForge](https://www.curseforge.com/minecraft/mc-mods/wunderlib-neoforge)
·
[Downloads](https://www.curseforge.com/minecraft/mc-mods/wunderlib-neoforge/files)
·
[Source](https://github.com/Reijin2312/WunderLib-New-Dawn)
·
[Issues](https://github.com/Reijin2312/WunderLib-New-Dawn/issues)
·
[Discord](https://discord.gg/BHxhJSn5uR)

</div>

---

## About

**WunderLib: New Dawn** is an independently maintained, unofficial continuation of WunderLib for Fabric, Quilt, and NeoForge.

WunderLib is a shared utility library focused on automated configuration screens, grid-based user-interface layouts, mathematical helpers, simplified networking, and other reusable functionality used by BetterX and compatible mods.

The Fabric edition continues maintenance of the original Fabric codebase with additional fixes and compatibility improvements.

The NeoForge edition ports the same core functionality to NeoForge while including the loader-specific adaptations required for compatibility.

> [!NOTE]
> WunderLib is primarily a developer-facing library. It does not add significant standalone gameplay content and normally needs to be installed only when another mod lists it as a required dependency.

> [!IMPORTANT]
> WunderLib: New Dawn is not maintained or endorsed by the original WunderLib developers or the BetterX Team.
>
> Please do not report New Dawn-specific issues to the upstream WunderLib repository or the official BetterX support channels. Use the [New Dawn issue tracker](https://github.com/Reijin2312/WunderLib-New-Dawn/issues) instead.

---

## Supported versions

| Minecraft version | Mod loader     | Latest published version | Source branch                                                                          |
| ----------------- | -------------- | -----------------------: | -------------------------------------------------------------------------------------- |
| 1.21.1            | NeoForge       |                `21.0.10` | [`master`](https://github.com/Reijin2312/WunderLib-New-Dawn/tree/master)               |
| 1.21.1            | Fabric / Quilt |          `21.0.9-fabric` | [`fabric-1.21.1`](https://github.com/Reijin2312/WunderLib-New-Dawn/tree/fabric-1.21.1) |
| 1.21.11           | NeoForge       |                `21.11.1` | [`1.21.11`](https://github.com/Reijin2312/WunderLib-New-Dawn/tree/1.21.11)             |
| 26.1–26.1.2       | NeoForge       |                 `26.1.1` | [`26.1`](https://github.com/Reijin2312/WunderLib-New-Dawn/tree/26.1)                   |

Always check the [CurseForge files page](https://www.curseforge.com/minecraft/mc-mods/wunderlib-neoforge/files) and download the correct file for your Minecraft version and mod loader.

Quilt users should use the Fabric-compatible file for Minecraft 1.21.1.

---

## Features

### Configuration

* Type-safe configuration files
* Automated configuration-screen generation
* Reusable configuration values and categories
* Client-side configuration interfaces
* Utilities for loading and saving configuration data
* Mod Menu-compatible configuration screens on Fabric

### User interface

* Grid-based UI layout system
* Reusable interface components
* Layout, position, spacing, and sizing utilities
* Shared screen and widget functionality
* Components designed for configuration and utility screens

### Networking

* Simplified network management
* Shared packet-handling utilities
* Client-to-server communication helpers
* Server-to-client communication helpers
* Common abstractions for loader-specific networking

### Mathematics

* Shared mathematical helper classes
* Positioning and sizing calculations
* Interpolation and value utilities
* Reusable calculations for UI and gameplay systems

### General utilities

* Shared functionality used by BetterX projects
* Client and common initialization helpers
* General-purpose Minecraft and Java utilities
* Reusable abstractions that reduce duplicated code
* Common functionality for Fabric, Quilt, and NeoForge projects

---

## New Dawn goals

WunderLib: New Dawn aims to preserve the behavior and APIs of the original project while providing continued maintenance for modern Minecraft versions and mod loaders.

The project focuses on:

* continued maintenance of the Fabric edition;
* compatibility with Quilt through the Fabric edition;
* support for modern NeoForge versions;
* support for newer Minecraft releases;
* shared bug fixes across supported loaders;
* loader-specific compatibility changes;
* consistent behavior across supported platforms;
* maintaining functionality required by the New Dawn ecosystem;
* compatibility with WorldWeaver: New Dawn;
* compatibility with BCLib: New Dawn;
* compatibility with BetterEnd: New Dawn;
* compatibility with BetterNether: New Dawn.

---

## Download and installation

Download WunderLib: New Dawn from the official CurseForge page:

### [Download from CurseForge](https://www.curseforge.com/minecraft/mc-mods/wunderlib-neoforge/files)

1. Install a supported Minecraft version.
2. Install the appropriate NeoForge, Fabric, or Quilt loader.
3. Download the WunderLib: New Dawn file matching your loader and Minecraft version.
4. Install any required dependencies shown on the selected CurseForge file page.
5. Place the downloaded `.jar` files in your Minecraft `mods` directory.
6. Launch the game.

Make sure the Minecraft version, mod loader, WunderLib version, and dependency versions all match.

WunderLib usually needs to be installed only when another mod lists it as a required dependency.

---

## Required dependencies

Requirements vary between Minecraft versions and mod loaders. Always check the dependency list of the selected CurseForge file.

### NeoForge

No additional library dependency is normally required beyond the appropriate NeoForge installation.

### Fabric and Quilt

* [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api)

Quilt users should install a compatible Quilt environment capable of loading the Fabric edition and its dependencies.

---

## Using WunderLib in another mod

Add the BetterX Maven repository to your Gradle configuration:

```groovy
repositories {
    maven {
        name = "BetterX"
        url = "https://maven.ambertation.de/releases"
    }
}
```

Define the WunderLib version in `gradle.properties`.

For NeoForge 1.21.1:

```properties
wunderlib_version=21.0.10
```

For Fabric or Quilt 1.21.1:

```properties
wunderlib_version=21.0.9
```

Use the version matching your Minecraft version and mod loader.

### NeoForge

Add WunderLib to your dependencies:

```groovy
dependencies {
    implementation "de.ambertation:wunderlib:${wunderlib_version}"
}
```

For Minecraft 1.21.1, add WunderLib to `META-INF/neoforge.mods.toml`:

```toml
[[dependencies.your_mod_id]]
modId="wunderlib"
mandatory=true
versionRange="[21.0,21.1)"
ordering="NONE"
side="BOTH"
```

Replace `your_mod_id` with the ID of your mod.

Adjust the version range when targeting a different Minecraft version.

For example:

```toml
# Minecraft 1.21.11
versionRange="[21.11,21.12)"
```

```toml
# Minecraft 26.1.x
versionRange="[26.1,26.2)"
```

### Fabric and Quilt

Add WunderLib to your dependencies:

```groovy
dependencies {
    modImplementation "de.ambertation:wunderlib:${wunderlib_version}"
}
```

When appropriate, WunderLib can also be bundled inside a Fabric mod:

```groovy
dependencies {
    modImplementation "de.ambertation:wunderlib:${wunderlib_version}"
    include "de.ambertation:wunderlib:${wunderlib_version}"
}
```

Add WunderLib to the `depends` section of `fabric.mod.json`:

```json
{
  "depends": {
    "wunderlib": ">=21.0.9 <21.1.0"
  }
}
```

Also declare compatible versions of Minecraft, Java, Fabric Loader, and Fabric API.

---

## Development setup

Clone the repository:

```bash
git clone https://github.com/Reijin2312/WunderLib-New-Dawn.git
cd WunderLib-New-Dawn
```

Select the branch for the Minecraft version and loader you want to work on:

```bash
# NeoForge 1.21.1
git checkout master

# Fabric / Quilt 1.21.1
git checkout fabric-1.21.1

# NeoForge 1.21.11
git checkout 1.21.11

# NeoForge 26.1.x
git checkout 26.1
```

Import the project into IntelliJ IDEA or another Gradle-compatible IDE.

Use the Java version required by the selected Minecraft version and branch.

Minecraft 1.21.1 and 1.21.11 builds require Java 21.

---

## Building

Run:

```bash
./gradlew build
```

On Windows:

```bat
gradlew.bat build
```

The compiled mod files will be available in:

```text
build/libs
```

---

## Local development

WunderLib can be used as a local Gradle project while developing another New Dawn mod.

A local development directory can look like this:

```text
projects/
├── WunderLib-New-Dawn/
├── WorldWeaver-New-Dawn/
├── BCLib-New-Dawn/
├── BetterEnd-New-Dawn/
└── BetterNether-New-Dawn/
```

A dependent project can include the local WunderLib checkout as a composite or project dependency.

When the local source is unavailable or disabled, the dependent project can use the configured Maven artifact instead.

---

## API stability

WunderLib is under active development.

Developers should be aware that:

* APIs may change between Minecraft versions;
* compatibility cannot be assumed across different Minecraft versions;
* Fabric and NeoForge implementations may require loader-specific handling;
* experimental APIs may receive breaking changes;
* development or prerelease builds should be tested before use in production modpacks;
* dependent mods should declare an explicit compatible WunderLib version range.

Do not assume that a WunderLib build for one Minecraft version is compatible with another.

---

## Reporting issues

Before opening an issue:

1. Make sure you are using the latest available WunderLib version.
2. Verify that you downloaded the correct file for your Minecraft version and mod loader.
3. Check that Fabric API is installed where required.
4. Confirm that all dependency versions match.
5. Test without unrelated mods when possible.
6. Include the latest log or crash report.
7. Include a complete mod list.
8. Specify whether you are using Fabric, Quilt, or NeoForge.
9. Include your Minecraft and WunderLib versions.
10. Provide clear steps to reproduce the problem.

Report bugs through the official New Dawn issue tracker:

### [Open an issue](https://github.com/Reijin2312/WunderLib-New-Dawn/issues)

Do not report New Dawn-specific bugs to the original WunderLib developers or the BetterX Team.

---

## Contributing

Contributions are welcome.

You can help by:

* reporting and reproducing bugs;
* submitting fixes;
* improving Fabric, Quilt, or NeoForge compatibility;
* improving API consistency;
* testing new Minecraft versions;
* improving networking compatibility;
* improving UI and configuration functionality;
* improving performance;
* adding documentation;
* improving developer examples.

Please clearly explain your changes and test them before submitting a pull request.

---

## Maintainer

WunderLib: New Dawn is maintained by **Raijin**.

* [CurseForge profile: Raijin2312](https://www.curseforge.com/members/raijin2312/projects)
* [GitHub profile: Reijin2312](https://github.com/Reijin2312)
* [New Dawn Discord](https://discord.gg/BHxhJSn5uR)

---

## Credits and attribution

* WunderLib was originally developed by **Frank Bauer / Quiqueck** and its contributors.
* The upstream source is maintained at [`quiqueck/WunderLib`](https://github.com/quiqueck/WunderLib).
* The original WunderLib project is available on [CurseForge](https://www.curseforge.com/minecraft/mc-mods/wunderlib).
* The original code and project assets belong to their respective developers and contributors.
* WunderLib: New Dawn is an independent, unofficial continuation maintained by Raijin.
* Special thanks to everyone who reports issues, contributes fixes, improves compatibility, and tests releases.

---

## License

The project source code is distributed under the [MIT License](LICENSE).

Copyright:

* © 2025 quiqueck
* © 2026 Raijin

See the [`LICENSE`](LICENSE) file before modifying or redistributing the project.

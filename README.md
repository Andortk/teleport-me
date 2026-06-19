# Teleport Me

Teleport Me is a RuneLite Plugin Hub plugin that adds a fast side-panel phonebook for Old School RuneScape teleports.

## Features

- Searchable teleport phonebook covering standard, Arceuus, Ancient Magicks, and Lunar spellbook teleports.
- Teleport tablets, redirected house tablets, teleport scrolls, jewellery teleports, diary teleports, and common reusable teleport items.
- Continent/region filtering with persistent dropdown state.
- Pinned teleports that can remain visible while browsing or searching.
- Exact requirement rows with item quantities and item icons, e.g. law/air/fire runes for Varrock Teleport.
- Bank helper mode: selecting a teleport can mask unrelated bank items so the required runes/items remain visible.
- Bank masking is opt-in with the `Show in bank` checkbox and automatically clears when the inventory already contains everything required.
- Client-side only. The plugin does not automate gameplay, click items, move the player, or interact with game menus.

## Build, test, and package

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
export PATH="$JAVA_HOME/bin:$PATH"
./gradlew clean test build shadowJar --warning-mode all
```

The Gradle runtime can use JDK 17+, while `build.gradle` compiles plugin classes with `options.release = 11` for RuneLite / Plugin Hub compatibility.

## Run dev client

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
export PATH="$JAVA_HOME/bin:$PATH"
./gradlew run --warning-mode all
```

On macOS/JDK 17, the `run` task includes the required module export/open flags for RuneLite's Swing/macOS startup path.

## Plugin Hub notes

- `runelite-plugin.properties` is present.
- License is BSD-2-Clause.
- `build=standard`; no custom third-party dependencies are required.
- `icon.png` is present at the repository root for Plugin Hub listing.
- Runtime item icons are loaded with `getResourceAsStream` from `src/main/resources/item-icons`, which is compatible with Plugin Hub jar deployment.

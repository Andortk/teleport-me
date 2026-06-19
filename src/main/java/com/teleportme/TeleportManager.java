package com.teleportme;

import static com.teleportme.OsrsItems.*;
import static com.teleportme.TeleportContinent.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public final class TeleportManager
{
	private final List<Teleport> teleports;

	public TeleportManager()
	{
		teleports = Collections.unmodifiableList(createTeleports());
	}

	public List<Teleport> getTeleports()
	{
		return teleports;
	}

	public List<Teleport> search(String query, Set<String> pinnedIds, boolean pinnedOnly, TeleportContinent filterContinent, boolean prioritizePinned)
	{
		String normalized = query == null ? "" : query.toLowerCase(Locale.ROOT).trim();
		return teleports.stream()
			.filter(t -> !pinnedOnly || pinnedIds.contains(t.getId()))
			.filter(t -> filterContinent == null || t.getContinent() == filterContinent)
			.filter(t -> t.matches(normalized))
			.sorted(Comparator.comparing((Teleport t) -> prioritizePinned && !pinnedIds.contains(t.getId())).thenComparing(Teleport::getDisplayName))
			.collect(Collectors.toList());
	}

	public List<Teleport> search(String query, Set<String> pinnedIds, boolean pinnedOnly, TeleportContinent filterContinent)
	{
		return search(query, pinnedIds, pinnedOnly, filterContinent, true);
	}

	private static List<Teleport> createTeleports()
	{
		List<Teleport> list = new ArrayList<>();
		addStandardSpells(list);
		addArceuusSpells(list);
		addAncientSpells(list);
		addLunarSpells(list);
		addStandardTablets(list);
		addAncientTablets(list);
		addLunarTablets(list);
		addArceuusTablets(list);
		addTeleportScrolls(list);
		addJewellery(list);
		addOtherTeleportItems(list);
		addDiaryAndCombatAchievementItems(list);
		return list;
	}

	private static void addStandardSpells(List<Teleport> list)
	{
		list.add(spell("lumbridge-home-spell", "Lumbridge Home Teleport", "Lumbridge", TeleportType.MAGIC_SPELL, MISTHALIN, "Standard Spellbook", 0, reqs(), "Free home teleport; cooldown and combat interrupt apply.", "home", "free"));
		list.add(spell("minigame-teleport-standard", "Minigame Teleport", "Minigames interface", TeleportType.MAGIC_SPELL, NETWORK, "Standard Spellbook", 0, reqs(), "Free minigame teleport; cooldown and unlock requirements apply.", "minigame", "free"));
		list.add(spell("varrock-spell", "Varrock Teleport", "Varrock centre / Grand Exchange diary toggle", TeleportType.MAGIC_SPELL, MISTHALIN, "Standard Spellbook", 25, reqs(rune("Law rune", 1, LAW_RUNE), rune("Air rune", 3, AIR_RUNE), rune("Fire rune", 1, FIRE_RUNE)), "Standard city teleport.", "varrock", "ge", "grand exchange", "city"));
		list.add(spell("lumbridge-spell", "Lumbridge Teleport", "Lumbridge Castle courtyard", TeleportType.MAGIC_SPELL, MISTHALIN, "Standard Spellbook", 31, reqs(rune("Law rune", 1, LAW_RUNE), rune("Earth rune", 1, EARTH_RUNE), rune("Air rune", 3, AIR_RUNE)), "Standard city teleport.", "lumbridge", "city", "home"));
		list.add(spell("falador-spell", "Falador Teleport", "Falador centre", TeleportType.MAGIC_SPELL, ASGARNIA, "Standard Spellbook", 37, reqs(rune("Law rune", 1, LAW_RUNE), rune("Water rune", 1, WATER_RUNE), rune("Air rune", 3, AIR_RUNE)), "Standard city teleport.", "falador", "fally", "city"));
		list.add(spell("house-spell", "Teleport to House", "Player-owned house", TeleportType.MAGIC_SPELL, NETWORK, "Standard Spellbook", 40, reqs(rune("Law rune", 1, LAW_RUNE), rune("Air rune", 1, AIR_RUNE), rune("Earth rune", 1, EARTH_RUNE)), "Destination is your current POH portal location or inside house depending settings.", "house", "poh", "construction"));
		list.add(spell("camelot-spell", "Camelot Teleport", "Camelot / Seers' Village diary toggle", TeleportType.MAGIC_SPELL, KANDARIN, "Standard Spellbook", 45, reqs(rune("Law rune", 1, LAW_RUNE), rune("Air rune", 5, AIR_RUNE)), "Standard members teleport.", "camelot", "seers", "catherby"));
		list.add(spell("kourend-castle-spell", "Kourend Castle Teleport", "Kourend Castle courtyard", TeleportType.MAGIC_SPELL, GREAT_KOUREND, "Standard Spellbook", 48, reqs(rune("Law rune", 2, LAW_RUNE), rune("Fire rune", 1, FIRE_RUNE), rune("Water rune", 1, WATER_RUNE)), "Requires Client of Kourend.", "kourend", "castle", "zeah"));
		list.add(spell("ardougne-spell", "Ardougne Teleport", "East Ardougne market", TeleportType.MAGIC_SPELL, KANDARIN, "Standard Spellbook", 51, reqs(rune("Law rune", 2, LAW_RUNE), rune("Water rune", 2, WATER_RUNE)), "Requires Plague City completion.", "ardougne", "ardy", "plague city"));
		list.add(spell("civitas-illa-fortis-spell", "Civitas illa Fortis Teleport", "Civitas illa Fortis", TeleportType.MAGIC_SPELL, VARLAMORE, "Standard Spellbook", 54, reqs(rune("Law rune", 2, LAW_RUNE), rune("Earth rune", 1, EARTH_RUNE), rune("Fire rune", 1, FIRE_RUNE)), "Requires Twilight's Promise.", "civitas", "fortis", "varlamore"));
		list.add(spell("watchtower-spell", "Watchtower Teleport", "Watchtower / Yanille diary toggle", TeleportType.MAGIC_SPELL, KANDARIN, "Standard Spellbook", 58, reqs(rune("Law rune", 2, LAW_RUNE), rune("Earth rune", 2, EARTH_RUNE)), "Requires Watchtower quest.", "yanille", "watchtower"));
		list.add(spell("trollheim-spell", "Trollheim Teleport", "Trollheim", TeleportType.MAGIC_SPELL, FREMENNIK_PROVINCE, "Standard Spellbook", 61, reqs(rune("Law rune", 2, LAW_RUNE), rune("Fire rune", 2, FIRE_RUNE)), "Requires Eadgar's Ruse; useful for God Wars Dungeon access.", "trollheim", "gwd", "god wars"));
		list.add(spell("ape-atoll-spell", "Ape Atoll Teleport", "Ape Atoll temple", TeleportType.MAGIC_SPELL, APE_ATOLL, "Standard Spellbook", 64, reqs(rune("Law rune", 2, LAW_RUNE), rune("Water rune", 2, WATER_RUNE), rune("Fire rune", 2, FIRE_RUNE), Requirement.item("Banana", 1, BANANA)), "Requires Recipe for Disaster/Awowyai unlock.", "ape atoll", "monkey"));
		list.add(spell("boat-spell", "Teleport to Boat", "Player-owned boat", TeleportType.MAGIC_SPELL, NETWORK, "Standard Spellbook", 67, reqs(rune("Law rune", 2, LAW_RUNE), rune("Earth rune", 2, EARTH_RUNE), rune("Water rune", 2, WATER_RUNE)), "Requires Pandemonium and Greater teleport focus.", "boat", "player owned boat"));
		list.add(spell("teleother-lumbridge", "Teleother Lumbridge", "Lumbridge Castle courtyard", TeleportType.MAGIC_SPELL, MISTHALIN, "Standard Spellbook", 74, reqs(rune("Law rune", 1, LAW_RUNE), rune("Earth rune", 1, EARTH_RUNE), rune("Soul rune", 1, SOUL_RUNE)), "Teleports another Accept Aid player.", "teleother", "lumbridge"));
		list.add(spell("teleother-falador", "Teleother Falador", "Falador centre", TeleportType.MAGIC_SPELL, ASGARNIA, "Standard Spellbook", 82, reqs(rune("Law rune", 1, LAW_RUNE), rune("Water rune", 1, WATER_RUNE), rune("Soul rune", 1, SOUL_RUNE)), "Teleports another Accept Aid player.", "teleother", "falador"));
		list.add(spell("target-spell-standard", "Teleport to Target", "Assigned Bounty Hunter target", TeleportType.MAGIC_SPELL, NETWORK, "Standard Spellbook", 85, reqs(rune("Law rune", 1, LAW_RUNE), rune("Chaos rune", 1, CHAOS_RUNE), rune("Death rune", 1, DEATH_RUNE)), "Bounty Hunter worlds; requires unlock.", "target", "bounty"));
		list.add(spell("teleother-camelot", "Teleother Camelot", "Camelot", TeleportType.MAGIC_SPELL, KANDARIN, "Standard Spellbook", 90, reqs(rune("Law rune", 1, LAW_RUNE), rune("Soul rune", 2, SOUL_RUNE)), "Teleports another Accept Aid player.", "teleother", "camelot"));
	}

	private static void addArceuusSpells(List<Teleport> list)
	{
		list.add(spell("arceuus-home-spell", "Arceuus Home Teleport", "Dark Altar", TeleportType.MAGIC_SPELL, GREAT_KOUREND, "Arceuus Spellbook", 1, reqs(), "Free home teleport; cooldown and combat interrupt apply.", "arceuus", "home", "dark altar"));
		list.add(spell("minigame-teleport-arceuus", "Minigame Teleport", "Minigames interface", TeleportType.MAGIC_SPELL, NETWORK, "Arceuus Spellbook", 0, reqs(), "Free minigame teleport.", "minigame", "free"));
		list.add(spell("arceuus-library-spell", "Arceuus Library Teleport", "North of Arceuus Library", TeleportType.MAGIC_SPELL, GREAT_KOUREND, "Arceuus Spellbook", 6, reqs(rune("Law rune", 1, LAW_RUNE), rune("Earth rune", 2, EARTH_RUNE)), "Arceuus spellbook teleport.", "arceuus", "library"));
		list.add(spell("draynor-manor-spell", "Draynor Manor Teleport", "Draynor Manor", TeleportType.MAGIC_SPELL, MISTHALIN, "Arceuus Spellbook", 17, reqs(rune("Law rune", 1, LAW_RUNE), rune("Earth rune", 1, EARTH_RUNE), rune("Water rune", 1, WATER_RUNE)), "Arceuus spellbook teleport.", "draynor", "manor"));
		list.add(spell("battlefront-spell", "Battlefront Teleport", "Battlefront", TeleportType.MAGIC_SPELL, GREAT_KOUREND, "Arceuus Spellbook", 23, reqs(rune("Law rune", 1, LAW_RUNE), rune("Earth rune", 1, EARTH_RUNE), rune("Fire rune", 1, FIRE_RUNE)), "Arceuus spellbook teleport.", "battlefront", "kebos"));
		list.add(spell("mind-altar-spell", "Mind Altar Teleport", "Mind Altar", TeleportType.MAGIC_SPELL, ASGARNIA, "Arceuus Spellbook", 28, reqs(rune("Law rune", 1, LAW_RUNE), rune("Mind rune", 2, MIND_RUNE)), "Arceuus spellbook teleport.", "mind", "altar"));
		list.add(spell("respawn-spell", "Respawn Teleport", "Respawn point", TeleportType.MAGIC_SPELL, NETWORK, "Arceuus Spellbook", 34, reqs(rune("Law rune", 1, LAW_RUNE), rune("Soul rune", 1, SOUL_RUNE)), "Arceuus spellbook teleport.", "respawn"));
		list.add(spell("salve-graveyard-spell", "Salve Graveyard Teleport", "River Salve graveyard", TeleportType.MAGIC_SPELL, MORYTANIA, "Arceuus Spellbook", 40, reqs(rune("Law rune", 1, LAW_RUNE), rune("Soul rune", 2, SOUL_RUNE)), "Requires Priest in Peril.", "salve", "graveyard", "morytania"));
		list.add(spell("fenkenstrain-spell", "Fenkenstrain's Castle Teleport", "Fenkenstrain's Castle", TeleportType.MAGIC_SPELL, MORYTANIA, "Arceuus Spellbook", 48, reqs(rune("Law rune", 1, LAW_RUNE), rune("Earth rune", 1, EARTH_RUNE), rune("Soul rune", 1, SOUL_RUNE)), "Requires Priest in Peril.", "fenkenstrain", "castle"));
		list.add(spell("west-ardougne-spell", "West Ardougne Teleport", "West Ardougne", TeleportType.MAGIC_SPELL, KANDARIN, "Arceuus Spellbook", 61, reqs(rune("Law rune", 2, LAW_RUNE), rune("Soul rune", 2, SOUL_RUNE)), "Requires Biohazard.", "west ardougne", "ardy"));
		list.add(spell("harmony-island-spell", "Harmony Island Teleport", "Harmony Island", TeleportType.MAGIC_SPELL, MORYTANIA, "Arceuus Spellbook", 65, reqs(rune("Law rune", 1, LAW_RUNE), rune("Nature rune", 1, NATURE_RUNE), rune("Soul rune", 1, SOUL_RUNE)), "Requires The Great Brain Robbery.", "harmony", "island"));
		list.add(spell("cemetery-spell", "Cemetery Teleport", "Forgotten Cemetery", TeleportType.MAGIC_SPELL, WILDERNESS, "Arceuus Spellbook", 71, reqs(rune("Law rune", 1, LAW_RUNE), rune("Blood rune", 1, BLOOD_RUNE), rune("Soul rune", 1, SOUL_RUNE)), "Wilderness teleport.", "cemetery", "forgotten", "wilderness"));
		list.add(spell("barrows-spell", "Barrows Teleport", "Barrows", TeleportType.MAGIC_SPELL, MORYTANIA, "Arceuus Spellbook", 83, reqs(rune("Law rune", 2, LAW_RUNE), rune("Blood rune", 1, BLOOD_RUNE), rune("Soul rune", 2, SOUL_RUNE)), "Arceuus spellbook teleport.", "barrows"));
		list.add(spell("target-spell-arceuus", "Teleport to Target", "Assigned Bounty Hunter target", TeleportType.MAGIC_SPELL, NETWORK, "Arceuus Spellbook", 85, reqs(rune("Law rune", 1, LAW_RUNE), rune("Chaos rune", 1, CHAOS_RUNE), rune("Death rune", 1, DEATH_RUNE)), "Bounty Hunter worlds; requires unlock.", "target", "bounty"));
		list.add(spell("ape-atoll-arceuus-spell", "Ape Atoll Teleport", "Ape Atoll Dungeon", TeleportType.MAGIC_SPELL, APE_ATOLL, "Arceuus Spellbook", 90, reqs(rune("Law rune", 2, LAW_RUNE), rune("Blood rune", 2, BLOOD_RUNE), rune("Soul rune", 2, SOUL_RUNE)), "Requires Monkey Madness I including Daero's training.", "ape atoll", "dungeon", "monkey"));
	}

	private static void addAncientSpells(List<Teleport> list)
	{
		list.add(spell("edgeville-home-spell", "Edgeville Home Teleport", "Edgeville", TeleportType.MAGIC_SPELL, MISTHALIN, "Ancient Magicks", 0, reqs(), "Free home teleport; cooldown and combat interrupt apply.", "edgeville", "home"));
		list.add(spell("minigame-teleport-ancient", "Minigame Teleport", "Minigames interface", TeleportType.MAGIC_SPELL, NETWORK, "Ancient Magicks", 0, reqs(), "Free minigame teleport.", "minigame", "free"));
		list.add(spell("paddewwa-spell", "Paddewwa Teleport", "Edgeville Dungeon", TeleportType.MAGIC_SPELL, MISTHALIN, "Ancient Magicks", 54, reqs(rune("Law rune", 2, LAW_RUNE), rune("Fire rune", 1, FIRE_RUNE), rune("Air rune", 1, AIR_RUNE)), "Ancient Magicks teleport.", "paddewwa", "edgeville dungeon"));
		list.add(spell("senntisten-spell", "Senntisten Teleport", "Digsite Exam Centre", TeleportType.MAGIC_SPELL, MISTHALIN, "Ancient Magicks", 60, reqs(rune("Law rune", 2, LAW_RUNE), rune("Soul rune", 1, SOUL_RUNE)), "Ancient Magicks teleport.", "senntisten", "digsite"));
		list.add(spell("kharyrll-spell", "Kharyrll Teleport", "Canifis", TeleportType.MAGIC_SPELL, MORYTANIA, "Ancient Magicks", 66, reqs(rune("Law rune", 2, LAW_RUNE), rune("Blood rune", 1, BLOOD_RUNE)), "Ancient Magicks teleport.", "kharyrll", "canifis"));
		list.add(spell("lassar-spell", "Lassar Teleport", "Ice Mountain", TeleportType.MAGIC_SPELL, ASGARNIA, "Ancient Magicks", 72, reqs(rune("Law rune", 2, LAW_RUNE), rune("Water rune", 4, WATER_RUNE)), "Ancient Magicks teleport.", "lassar", "ice mountain"));
		list.add(spell("dareeyak-spell", "Dareeyak Teleport", "Ruins west of Bandit Camp", TeleportType.MAGIC_SPELL, WILDERNESS, "Ancient Magicks", 78, reqs(rune("Law rune", 2, LAW_RUNE), rune("Fire rune", 3, FIRE_RUNE), rune("Air rune", 2, AIR_RUNE)), "Wilderness Ancient Magicks teleport.", "dareeyak", "bandit camp", "wilderness"));
		list.add(spell("carrallanger-spell", "Carrallanger Teleport", "Graveyard of Shadows", TeleportType.MAGIC_SPELL, WILDERNESS, "Ancient Magicks", 84, reqs(rune("Law rune", 2, LAW_RUNE), rune("Soul rune", 2, SOUL_RUNE)), "Wilderness Ancient Magicks teleport.", "carrallanger", "graveyard", "wilderness"));
		list.add(spell("target-spell-ancient", "Teleport to Target", "Assigned Bounty Hunter target", TeleportType.MAGIC_SPELL, NETWORK, "Ancient Magicks", 85, reqs(rune("Law rune", 1, LAW_RUNE), rune("Chaos rune", 1, CHAOS_RUNE), rune("Death rune", 1, DEATH_RUNE)), "Bounty Hunter worlds; requires unlock.", "target", "bounty"));
		list.add(spell("annakarl-spell", "Annakarl Teleport", "Demonic Ruins", TeleportType.MAGIC_SPELL, WILDERNESS, "Ancient Magicks", 90, reqs(rune("Law rune", 2, LAW_RUNE), rune("Blood rune", 2, BLOOD_RUNE)), "Deep Wilderness Ancient Magicks teleport.", "annakarl", "demonic ruins", "wilderness"));
		list.add(spell("ghorrock-spell", "Ghorrock Teleport", "Frozen Waste Plateau", TeleportType.MAGIC_SPELL, WILDERNESS, "Ancient Magicks", 96, reqs(rune("Law rune", 2, LAW_RUNE), rune("Water rune", 8, WATER_RUNE)), "Deep Wilderness Ancient Magicks teleport.", "ghorrock", "ice plateau", "wilderness"));
	}

	private static void addLunarSpells(List<Teleport> list)
	{
		list.add(spell("lunar-home-spell", "Lunar Home Teleport", "Lunar Isle", TeleportType.MAGIC_SPELL, FREMENNIK_PROVINCE, "Lunar Spellbook", 0, reqs(), "Free home teleport; cooldown and combat interrupt apply.", "lunar", "home"));
		list.add(spell("minigame-teleport-lunar", "Minigame Teleport", "Minigames interface", TeleportType.MAGIC_SPELL, NETWORK, "Lunar Spellbook", 0, reqs(), "Free minigame teleport.", "minigame", "free"));
		list.add(spell("moonclan-spell", "Moonclan Teleport", "Lunar Isle", TeleportType.MAGIC_SPELL, FREMENNIK_PROVINCE, "Lunar Spellbook", 69, reqs(rune("Law rune", 1, LAW_RUNE), rune("Astral rune", 2, ASTRAL_RUNE), rune("Earth rune", 2, EARTH_RUNE)), "Lunar spellbook teleport.", "moonclan", "lunar isle"));
		list.add(spell("tele-group-moonclan", "Tele Group Moonclan", "Lunar Isle", TeleportType.MAGIC_SPELL, FREMENNIK_PROVINCE, "Lunar Spellbook", 70, reqs(rune("Law rune", 1, LAW_RUNE), rune("Astral rune", 2, ASTRAL_RUNE), rune("Earth rune", 4, EARTH_RUNE)), "Tele Group spell; nearby players must accept aid.", "group", "moonclan"));
		list.add(spell("ourania-spell", "Ourania Teleport", "Ourania Cave", TeleportType.MAGIC_SPELL, KANDARIN, "Lunar Spellbook", 71, reqs(rune("Law rune", 1, LAW_RUNE), rune("Astral rune", 2, ASTRAL_RUNE), rune("Earth rune", 6, EARTH_RUNE)), "Requires talking to Baba Yaga with Lunar spellbook active.", "ourania", "zmi"));
		list.add(spell("waterbirth-spell", "Waterbirth Teleport", "Waterbirth Island", TeleportType.MAGIC_SPELL, FREMENNIK_PROVINCE, "Lunar Spellbook", 72, reqs(rune("Law rune", 1, LAW_RUNE), rune("Astral rune", 2, ASTRAL_RUNE), rune("Water rune", 1, WATER_RUNE)), "Lunar spellbook teleport.", "waterbirth"));
		list.add(spell("tele-group-waterbirth", "Tele Group Waterbirth", "Waterbirth Island", TeleportType.MAGIC_SPELL, FREMENNIK_PROVINCE, "Lunar Spellbook", 73, reqs(rune("Law rune", 1, LAW_RUNE), rune("Astral rune", 2, ASTRAL_RUNE), rune("Water rune", 5, WATER_RUNE)), "Tele Group spell; nearby players must accept aid.", "group", "waterbirth"));
		list.add(spell("barbarian-spell", "Barbarian Teleport", "Barbarian Outpost", TeleportType.MAGIC_SPELL, KANDARIN, "Lunar Spellbook", 75, reqs(rune("Law rune", 2, LAW_RUNE), rune("Astral rune", 2, ASTRAL_RUNE), rune("Fire rune", 3, FIRE_RUNE)), "Lunar spellbook teleport.", "barbarian", "outpost"));
		list.add(spell("tele-group-barbarian", "Tele Group Barbarian", "Barbarian Outpost", TeleportType.MAGIC_SPELL, KANDARIN, "Lunar Spellbook", 76, reqs(rune("Law rune", 2, LAW_RUNE), rune("Astral rune", 2, ASTRAL_RUNE), rune("Fire rune", 6, FIRE_RUNE)), "Tele Group spell; nearby players must accept aid.", "group", "barbarian"));
		list.add(spell("khazard-spell", "Khazard Teleport", "Port Khazard", TeleportType.MAGIC_SPELL, KANDARIN, "Lunar Spellbook", 78, reqs(rune("Law rune", 2, LAW_RUNE), rune("Astral rune", 2, ASTRAL_RUNE), rune("Water rune", 4, WATER_RUNE)), "Lunar spellbook teleport.", "khazard", "port"));
		list.add(spell("tele-group-khazard", "Tele Group Khazard", "Port Khazard", TeleportType.MAGIC_SPELL, KANDARIN, "Lunar Spellbook", 79, reqs(rune("Law rune", 2, LAW_RUNE), rune("Astral rune", 2, ASTRAL_RUNE), rune("Water rune", 8, WATER_RUNE)), "Tele Group spell; nearby players must accept aid.", "group", "khazard"));
		list.add(spell("fishing-guild-spell", "Fishing Guild Teleport", "Fishing Guild", TeleportType.MAGIC_SPELL, KANDARIN, "Lunar Spellbook", 85, reqs(rune("Law rune", 3, LAW_RUNE), rune("Astral rune", 3, ASTRAL_RUNE), rune("Water rune", 10, WATER_RUNE)), "Lunar spellbook teleport.", "fishing", "guild"));
		list.add(spell("target-spell-lunar", "Teleport to Target", "Assigned Bounty Hunter target", TeleportType.MAGIC_SPELL, NETWORK, "Lunar Spellbook", 85, reqs(rune("Law rune", 1, LAW_RUNE), rune("Chaos rune", 1, CHAOS_RUNE), rune("Death rune", 1, DEATH_RUNE)), "Bounty Hunter worlds; requires unlock.", "target", "bounty"));
		list.add(spell("tele-group-fishing-guild", "Tele Group Fishing Guild", "Fishing Guild", TeleportType.MAGIC_SPELL, KANDARIN, "Lunar Spellbook", 86, reqs(rune("Law rune", 3, LAW_RUNE), rune("Astral rune", 3, ASTRAL_RUNE), rune("Water rune", 14, WATER_RUNE)), "Tele Group spell; nearby players must accept aid.", "group", "fishing", "guild"));
		list.add(spell("catherby-spell", "Catherby Teleport", "Catherby", TeleportType.MAGIC_SPELL, KANDARIN, "Lunar Spellbook", 87, reqs(rune("Law rune", 3, LAW_RUNE), rune("Astral rune", 3, ASTRAL_RUNE), rune("Water rune", 10, WATER_RUNE)), "Lunar spellbook teleport.", "catherby"));
		list.add(spell("tele-group-catherby", "Tele Group Catherby", "Catherby", TeleportType.MAGIC_SPELL, KANDARIN, "Lunar Spellbook", 88, reqs(rune("Law rune", 3, LAW_RUNE), rune("Astral rune", 3, ASTRAL_RUNE), rune("Water rune", 15, WATER_RUNE)), "Tele Group spell; nearby players must accept aid.", "group", "catherby"));
		list.add(spell("ice-plateau-spell", "Ice Plateau Teleport", "Ice Plateau", TeleportType.MAGIC_SPELL, WILDERNESS, "Lunar Spellbook", 89, reqs(rune("Law rune", 3, LAW_RUNE), rune("Astral rune", 3, ASTRAL_RUNE), rune("Water rune", 8, WATER_RUNE)), "Deep Wilderness Lunar teleport.", "ice plateau", "wilderness"));
		list.add(spell("tele-group-ice-plateau", "Tele Group Ice Plateau", "Ice Plateau", TeleportType.MAGIC_SPELL, WILDERNESS, "Lunar Spellbook", 90, reqs(rune("Law rune", 3, LAW_RUNE), rune("Astral rune", 3, ASTRAL_RUNE), rune("Water rune", 16, WATER_RUNE)), "Tele Group spell; deep Wilderness warning applies.", "group", "ice plateau", "wilderness"));
	}

	private static void addStandardTablets(List<Teleport> list)
	{
		tablet(list, "varrock-tab", "Varrock teleport tablet", "Varrock", MISTHALIN, VARROCK_TELEPORT_TABLET, "Standard magic tablet.", "varrock", "tablet");
		tablet(list, "lumbridge-tab", "Lumbridge teleport tablet", "Lumbridge", MISTHALIN, LUMBRIDGE_TELEPORT_TABLET, "Standard magic tablet.", "lumbridge", "tablet");
		tablet(list, "falador-tab", "Falador teleport tablet", "Falador", ASGARNIA, FALADOR_TELEPORT_TABLET, "Standard magic tablet.", "falador", "tablet");
		tablet(list, "camelot-tab", "Camelot teleport tablet", "Camelot", KANDARIN, CAMELOT_TELEPORT_TABLET, "Standard magic tablet.", "camelot", "tablet");
		tablet(list, "kourend-castle-tab", "Kourend castle teleport tablet", "Kourend Castle", GREAT_KOUREND, KOUREND_CASTLE_TELEPORT_TABLET, "Standard magic tablet.", "kourend", "tablet");
		tablet(list, "ardougne-tab", "Ardougne teleport tablet", "Ardougne", KANDARIN, ARDOUGNE_TELEPORT_TABLET, "Standard magic tablet; quest restrictions still apply.", "ardougne", "tablet");
		tablet(list, "civitas-illa-fortis-tab", "Civitas illa fortis teleport tablet", "Civitas illa Fortis", VARLAMORE, CIVITAS_ILLA_FORTIS_TELEPORT_TABLET, "Standard magic tablet.", "civitas", "fortis", "tablet");
		tablet(list, "watchtower-tab", "Watchtower teleport tablet", "Watchtower", KANDARIN, WATCHTOWER_TELEPORT_TABLET, "Standard magic tablet; quest restrictions still apply.", "watchtower", "tablet");
		tablet(list, "boat-tab", "Teleport to boat tablet", "Player-owned boat", NETWORK, TELEPORT_TO_BOAT_TABLET, "Standard magic tablet.", "boat", "tablet");
		tablet(list, "house-tab", "Teleport to house tablet", "Player-owned house", NETWORK, TELEPORT_TO_HOUSE_TABLET, "Break tablet; works without runes.", "house", "poh", "tablet");
		tablet(list, "rimmington-tab", "Rimmington teleport tablet", "Rimmington POH portal", ASGARNIA, RIMMINGTON_TELEPORT_TABLET, "Redirected house tablet.", "rimmington", "tablet");
		tablet(list, "taverley-tab", "Taverley teleport tablet", "Taverley POH portal", ASGARNIA, TAVERLEY_TELEPORT_TABLET, "Redirected house tablet.", "taverley", "tablet");
		tablet(list, "pollnivneach-tab", "Pollnivneach teleport tablet", "Pollnivneach POH portal", DESERT, POLLNIVNEACH_TELEPORT_TABLET, "Redirected house tablet.", "pollnivneach", "tablet");
		tablet(list, "rellekka-tab", "Rellekka teleport tablet", "Rellekka POH portal", FREMENNIK_PROVINCE, RELLEKKA_TELEPORT_TABLET, "Redirected house tablet.", "rellekka", "tablet");
		tablet(list, "brimhaven-tab", "Brimhaven teleport tablet", "Brimhaven POH portal", KARAMJA, BRIMHAVEN_TELEPORT_TABLET, "Redirected house tablet.", "brimhaven", "tablet");
		tablet(list, "yanille-tab", "Yanille teleport tablet", "Yanille POH portal", KANDARIN, YANILLE_TELEPORT_TABLET, "Redirected house tablet.", "yanille", "tablet");
		tablet(list, "trollheim-tab", "Trollheim teleport tablet", "Trollheim", FREMENNIK_PROVINCE, TROLLHEIM_TELEPORT_TABLET, "Redirected house tablet / Trollheim tablet.", "trollheim", "tablet");
		tablet(list, "hosidius-tab", "Hosidius teleport tablet", "Hosidius POH portal", GREAT_KOUREND, HOSIDIUS_TELEPORT_TABLET, "Redirected house tablet.", "hosidius", "tablet");
		tablet(list, "prifddinas-tab", "Prifddinas teleport tablet", "Prifddinas POH portal", TIRANNWN, PRIFDDINAS_TELEPORT_TABLET, "Redirected house tablet.", "prifddinas", "tablet");
	}

	private static void addAncientTablets(List<Teleport> list)
	{
		tablet(list, "paddewwa-tab", "Paddewwa teleport tablet", "Edgeville Dungeon", MISTHALIN, PADDEWWA_TELEPORT_TABLET, "Ancient Magicks tablet; Desert Treasure I restrictions apply.", "paddewwa", "tablet");
		tablet(list, "senntisten-tab", "Senntisten teleport tablet", "Digsite", MISTHALIN, SENNTISTEN_TELEPORT_TABLET, "Ancient Magicks tablet.", "senntisten", "digsite", "tablet");
		tablet(list, "kharyrll-tab", "Kharyrll teleport tablet", "Canifis", MORYTANIA, KHARYRLL_TELEPORT_TABLET, "Ancient Magicks tablet.", "kharyrll", "canifis", "tablet");
		tablet(list, "lassar-tab", "Lassar teleport tablet", "Ice Mountain", ASGARNIA, LASSAR_TELEPORT_TABLET, "Ancient Magicks tablet.", "lassar", "tablet");
		tablet(list, "dareeyak-tab", "Dareeyak teleport tablet", "Wilderness ruins", WILDERNESS, DAREEYAK_TELEPORT_TABLET, "Ancient Magicks Wilderness tablet.", "dareeyak", "tablet", "wilderness");
		tablet(list, "carrallanger-tab", "Carrallanger teleport tablet", "Graveyard of Shadows", WILDERNESS, CARRALLANGER_TELEPORT_TABLET, "Ancient Magicks Wilderness tablet.", "carrallanger", "tablet", "wilderness");
		tablet(list, "annakarl-tab", "Annakarl teleport tablet", "Demonic Ruins", WILDERNESS, ANNAKARL_TELEPORT_TABLET, "Ancient Magicks Wilderness tablet.", "annakarl", "tablet", "wilderness");
		tablet(list, "ghorrock-tab", "Ghorrock teleport tablet", "Ice Plateau", WILDERNESS, GHORROCK_TELEPORT_TABLET, "Ancient Magicks Wilderness tablet.", "ghorrock", "tablet", "wilderness");
	}

	private static void addLunarTablets(List<Teleport> list)
	{
		tablet(list, "moonclan-tab", "Moonclan teleport tablet", "Lunar Isle", FREMENNIK_PROVINCE, MOONCLAN_TELEPORT_TABLET, "Lunar teleport tablet.", "moonclan", "lunar", "tablet");
		tablet(list, "ourania-tab", "Ourania teleport tablet", "Ourania Cave", KANDARIN, OURANIA_TELEPORT_TABLET, "Lunar teleport tablet.", "ourania", "zmi", "tablet");
		tablet(list, "waterbirth-tab", "Waterbirth teleport tablet", "Waterbirth Island", FREMENNIK_PROVINCE, WATERBIRTH_TELEPORT_TABLET, "Lunar teleport tablet.", "waterbirth", "tablet");
		tablet(list, "barbarian-tab", "Barbarian teleport tablet", "Barbarian Outpost", KANDARIN, BARBARIAN_TELEPORT_TABLET, "Lunar teleport tablet.", "barbarian", "tablet");
		tablet(list, "khazard-tab", "Khazard teleport tablet", "Port Khazard", KANDARIN, KHAZARD_TELEPORT_TABLET, "Lunar teleport tablet.", "khazard", "tablet");
		tablet(list, "fishing-guild-tab", "Fishing guild teleport tablet", "Fishing Guild", KANDARIN, FISHING_GUILD_TELEPORT_TABLET, "Lunar teleport tablet.", "fishing", "guild", "tablet");
		tablet(list, "catherby-tab", "Catherby teleport tablet", "Catherby", KANDARIN, CATHERBY_TELEPORT_TABLET, "Lunar teleport tablet.", "catherby", "tablet");
		tablet(list, "ice-plateau-tab", "Ice plateau teleport tablet", "Ice Plateau", WILDERNESS, ICE_PLATEAU_TELEPORT_TABLET, "Lunar Wilderness teleport tablet.", "ice plateau", "tablet", "wilderness");
	}

	private static void addArceuusTablets(List<Teleport> list)
	{
		tablet(list, "arceuus-library-tab", "Arceuus Library tablet", "Arceuus Library", GREAT_KOUREND, ARCEUUS_LIBRARY_TELEPORT_TABLET, "Arceuus teleport tablet.", "arceuus", "library", "tablet");
		tablet(list, "draynor-manor-tab", "Draynor Manor tablet", "Draynor Manor", MISTHALIN, DRAYNOR_MANOR_TELEPORT_TABLET, "Arceuus teleport tablet.", "draynor", "manor", "tablet");
		tablet(list, "battlefront-tab", "Battlefront tablet", "Battlefront", GREAT_KOUREND, BATTLEFRONT_TELEPORT_TABLET, "Arceuus teleport tablet.", "battlefront", "tablet");
		tablet(list, "mind-altar-tab", "Mind altar tablet", "Mind Altar", ASGARNIA, MIND_ALTAR_TELEPORT_TABLET, "Arceuus teleport tablet.", "mind", "altar", "tablet");
		tablet(list, "salve-graveyard-tab", "Salve Graveyard tablet", "River Salve graveyard", MORYTANIA, SALVE_GRAVEYARD_TELEPORT_TABLET, "Arceuus teleport tablet.", "salve", "graveyard", "tablet");
		tablet(list, "fenkenstrain-tab", "Fenkenstrain's Castle tablet", "Fenkenstrain's Castle", MORYTANIA, FENKENSTRAINS_CASTLE_TELEPORT_TABLET, "Arceuus teleport tablet.", "fenkenstrain", "tablet");
		tablet(list, "west-ardougne-tab", "West Ardougne tablet", "West Ardougne", KANDARIN, WEST_ARDOUGNE_TELEPORT_TABLET, "Arceuus teleport tablet.", "west ardougne", "tablet");
		tablet(list, "harmony-island-tab", "Harmony Island tablet", "Harmony Island", MORYTANIA, HARMONY_ISLAND_TELEPORT_TABLET, "Arceuus teleport tablet.", "harmony", "island", "tablet");
		tablet(list, "cemetery-tab", "Cemetery tablet", "Forgotten Cemetery", WILDERNESS, CEMETERY_TELEPORT_TABLET, "Arceuus Wilderness teleport tablet.", "cemetery", "tablet", "wilderness");
		tablet(list, "barrows-tab", "Barrows tablet", "Barrows", MORYTANIA, BARROWS_TELEPORT_TABLET, "Arceuus teleport tablet.", "barrows", "tablet");
		tablet(list, "ape-atoll-arceuus-tab", "Ape Atoll tablet", "Ape Atoll Dungeon", APE_ATOLL, APE_ATOLL_TELEPORT_TABLET, "Arceuus teleport tablet.", "ape atoll", "tablet");
	}

	private static void addTeleportScrolls(List<Teleport> list)
	{
		tablet(list, "target-teleport-scroll", "Target teleport", "Assigned Bounty Hunter target", NETWORK, TARGET_TELEPORT, "Bounty Hunter target teleport scroll.", "target", "scroll", "bounty");
		tablet(list, "volcanic-mine-teleport", "Volcanic mine teleport", "Volcanic Mine entrance", FOSSIL_OR_NETWORK(), VOLCANIC_MINE_TELEPORT, "Teleport scroll.", "volcanic", "mine", "scroll");
		tablet(list, "wilderness-crabs-teleport", "Wilderness crabs teleport", "Dark crab fishing spots", WILDERNESS, WILDERNESS_CRABS_TELEPORT, "Wilderness teleport scroll.", "crabs", "dark crab", "scroll", "wilderness");
		tablet(list, "nardah-teleport", "Nardah teleport", "Nardah", DESERT, NARDAH_TELEPORT, "Treasure Trails teleport scroll.", "nardah", "scroll" );
		tablet(list, "digsite-teleport", "Digsite teleport", "Digsite", MISTHALIN, DIGSITE_TELEPORT, "Treasure Trails teleport scroll.", "digsite", "scroll" );
		tablet(list, "feldip-hills-teleport", "Feldip hills teleport", "Feldip Hills", KANDARIN, FELDIP_HILLS_TELEPORT, "Treasure Trails teleport scroll.", "feldip", "scroll" );
		tablet(list, "lunar-isle-teleport", "Lunar isle teleport", "Lunar Isle", FREMENNIK_PROVINCE, LUNAR_ISLE_TELEPORT, "Treasure Trails teleport scroll.", "lunar", "scroll" );
		tablet(list, "mortton-teleport", "Mort'ton teleport", "Mort'ton", MORYTANIA, MORTTON_TELEPORT, "Treasure Trails teleport scroll.", "mortton", "morytania", "scroll" );
		tablet(list, "pest-control-teleport", "Pest control teleport", "Pest Control", ASGARNIA, PEST_CONTROL_TELEPORT, "Treasure Trails teleport scroll.", "pest", "control", "scroll" );
		tablet(list, "piscatoris-teleport", "Piscatoris teleport", "Piscatoris Fishing Colony", KANDARIN, PISCATORIS_TELEPORT, "Treasure Trails teleport scroll.", "piscatoris", "scroll" );
		tablet(list, "tai-bwo-wannai-teleport", "Tai bwo wannai teleport", "Tai Bwo Wannai", KARAMJA, TAI_BWO_WANNAI_TELEPORT, "Treasure Trails teleport scroll.", "tai bwo", "karamja", "scroll" );
		tablet(list, "iorwerth-camp-teleport", "Iorwerth camp teleport", "Iorwerth Camp", TIRANNWN, IORWERTH_CAMP_TELEPORT, "Treasure Trails teleport scroll.", "iorwerth", "elf", "scroll" );
		tablet(list, "mos-le-harmless-teleport", "Mos le'harmless teleport", "Mos Le'Harmless", KARAMJA, MOS_LE_HARMLESS_TELEPORT, "Treasure Trails teleport scroll.", "mos", "harmless", "scroll" );
		tablet(list, "lumberyard-teleport", "Lumberyard teleport", "Lumber Yard", MISTHALIN, LUMBERYARD_TELEPORT, "Treasure Trails teleport scroll.", "lumberyard", "scroll" );
		tablet(list, "zul-andra-teleport", "Zul-andra teleport", "Zul-Andra", TIRANNWN, ZUL_ANDRA_TELEPORT, "Teleport scroll.", "zul-andra", "zulrah", "scroll" );
		tablet(list, "key-master-teleport", "Key master teleport", "Cerberus' Lair", ASGARNIA, KEY_MASTER_TELEPORT, "Teleport scroll.", "key master", "cerberus", "scroll" );
		tablet(list, "revenant-cave-teleport", "Revenant cave teleport", "Revenant Caves", WILDERNESS, REVENANT_CAVE_TELEPORT, "Wilderness teleport scroll.", "revenant", "rev caves", "scroll" );
		tablet(list, "watson-teleport", "Watson teleport", "Watson's house", GREAT_KOUREND, WATSON_TELEPORT, "Teleport scroll.", "watson", "hosidius", "scroll" );
		tablet(list, "guthixian-temple-teleport", "Guthixian temple teleport", "Ancient Guthixian Temple", MISTHALIN, GUTHIXIAN_TEMPLE_TELEPORT, "Teleport scroll.", "guthixian", "temple", "scroll" );
		tablet(list, "spider-cave-teleport", "Spider cave teleport", "Morytania Spider Cave", MORYTANIA, SPIDER_CAVE_TELEPORT, "Teleport scroll.", "spider", "cave", "scroll" );
		tablet(list, "colossal-wyrm-teleport-scroll", "Colossal wyrm teleport scroll", "Colossal Wyrm Remains", VARLAMORE, COLOSSAL_WYRM_TELEPORT_SCROLL, "Teleport scroll.", "colossal", "wyrm", "scroll" );
		tablet(list, "chasm-teleport-scroll", "Chasm teleport scroll", "Chasm of Fire", GREAT_KOUREND, CHASM_TELEPORT_SCROLL, "Teleport scroll.", "chasm", "fire", "scroll" );
	}

	private static void addJewellery(List<Teleport> list)
	{
		jewelry(list, "games-necklace", "Games necklace", "Burthorpe Games Room / Barbarian Assault / Corporeal Beast / Tears of Guthix / Wintertodt", NETWORK, GAMES_NECKLACE8, "Charged games necklace.", "games", "burthorpe", "wintertodt", "barbarian" );
		jewelry(list, "dueling-ring", "Ring of dueling", "Emir's Arena / Ferox Enclave / Castle Wars / Fortis Colosseum", NETWORK, RING_OF_DUELING8, "Charged ring of dueling.", "dueling", "ferox", "castle wars", "emir", "fortis" );
		jewelry(list, "ring-of-life", "Ring of life", "Respawn point emergency teleport", NETWORK, RING_OF_LIFE, "Automatic emergency teleport below 10% Hitpoints; included for teleport completeness.", "emergency", "life", "respawn" );
		jewelry(list, "combat-bracelet", "Combat bracelet", "Warriors' Guild / Champions' Guild / Edgeville Monastery / Ranging Guild", NETWORK, COMBAT_BRACELET6, "Charged combat bracelet.", "combat", "bracelet", "guild", "monastery" );
		jewelry(list, "skills-necklace", "Skills necklace", "Fishing / Mining / Crafting / Cooks' / Woodcutting / Farming Guilds", NETWORK, SKILLS_NECKLACE6, "Charged skills necklace.", "skills", "necklace", "guild" );
		jewelry(list, "glory", "Amulet of glory", "Edgeville / Karamja / Draynor Village / Al Kharid", NETWORK, AMULET_OF_GLORY6, "Charged amulet of glory.", "glory", "edgeville", "karamja", "draynor", "al kharid" );
		jewelry(list, "eternal-glory", "Amulet of eternal glory", "Edgeville / Karamja / Draynor Village / Al Kharid", NETWORK, AMULET_OF_ETERNAL_GLORY, "Unlimited amulet of glory teleports.", "eternal", "glory", "edgeville", "karamja" );
		jewelry(list, "ring-of-wealth", "Ring of wealth", "Miscellania / Grand Exchange / Falador Park / Dondakan", NETWORK, RING_OF_WEALTH5, "Charged ring of wealth.", "wealth", "grand exchange", "miscellania", "dondakan" );
		jewelry(list, "slayer-ring", "Slayer ring", "Slayer Tower / Fremennik Slayer Dungeon / Tarn's Lair / Stronghold Slayer Cave / Dark Beasts", NETWORK, SLAYER_RING8, "Charged slayer ring.", "slayer", "tower", "tarn", "dark beasts" );
		jewelry(list, "eternal-slayer-ring", "Slayer ring (eternal)", "Slayer Tower / Fremennik Slayer Dungeon / Tarn's Lair / Stronghold Slayer Cave / Dark Beasts", NETWORK, SLAYER_RING_ETERNAL, "Unlimited slayer ring teleports.", "slayer", "eternal", "tower" );
		jewelry(list, "digsite-pendant", "Digsite pendant", "Digsite / Fossil Island / Lithkren", NETWORK, DIGSITE_PENDANT5, "Charged digsite pendant.", "digsite", "fossil", "lithkren" );
		jewelry(list, "ring-of-returning", "Ring of returning", "Respawn point", NETWORK, RING_OF_RETURNING5, "Charged ring of returning.", "returning", "respawn" );
		jewelry(list, "necklace-of-passage", "Necklace of passage", "Wizards' Tower / Jorral's Outpost / Desert eagle station", NETWORK, NECKLACE_OF_PASSAGE5, "Charged necklace of passage.", "passage", "wizards", "jorral", "eagle" );
		jewelry(list, "burning-amulet", "Burning amulet", "Chaos Temple / Bandit Camp / Lava Maze", WILDERNESS, BURNING_AMULET5, "Charged burning amulet; Wilderness destinations.", "burning", "chaos temple", "bandit", "lava maze", "wilderness" );
		jewelry(list, "camulet", "Camulet", "Enakhra's Temple", DESERT, CAMULET, "Camulet charges teleport to Enakhra's Temple.", "camulet", "enakhra", "desert" );
		jewelry(list, "pharaohs-sceptre", "Pharaoh's sceptre", "Jalsavrah / Jaleustrophos / Jaldraocht / Jaltevas / Necropolis", DESERT, PHARAOHS_SCEPTRE8, "Charged Pharaoh's sceptre.", "pharaoh", "sceptre", "pyramid", "desert" );
		jewelry(list, "amulet-of-the-eye", "Amulet of the eye", "Temple of the Eye", MISTHALIN, AMULET_OF_THE_EYE, "Guardians of the Rift teleport.", "eye", "gotr", "runecraft" );
		jewelry(list, "ring-of-the-elements", "Ring of the elements", "Air / Water / Earth / Fire altars", NETWORK, RING_OF_THE_ELEMENTS, "Charged ring of the elements.", "elements", "altar", "runecraft" );
		jewelry(list, "ring-of-shadows", "Ring of shadows", "Desert Treasure II vaults / Ghorrock / Lassar / Stranglewood / Shadow Realm", NETWORK, RING_OF_SHADOWS, "Charged ring of shadows.", "shadows", "dt2", "vault" );
		jewelry(list, "giantsoul-amulet", "Giantsoul amulet", "Giants' Plateau", VARLAMORE, GIANTSOUL_AMULET, "Giantsoul amulet teleport.", "giant", "giantsoul", "varlamore" );
		jewelry(list, "sailors-amulet", "Sailors' amulet", "Port Piscarilius / Sunset Coast sailing destinations", NETWORK, SAILORS_AMULET, "Sailing teleport amulet.", "sailor", "sailing" );
		jewelry(list, "cowbell-amulet", "Cowbell amulet", "Cow-related teleport", MISTHALIN, COWBELL_AMULET, "Cowbell amulet teleport item.", "cowbell", "cow" );
	}

	private static void addOtherTeleportItems(List<Teleport> list)
	{
		otherItem(list, "ectophial", "Ectophial", "Ectofuntus", MORYTANIA, ECTOPHIAL, "Reusable after Ghosts Ahoy; refill at Ectofuntus.", "morytania", "ectofuntus", "ghosts ahoy" );
		otherItem(list, "fairy-ring", "Fairy rings", "Network destinations by code", NETWORK, 0, "Requires Fairy Tale II progress; dramen/lunar staff not needed after Lumbridge & Draynor Elite Diary.", "fairy", "ring", "code", "transport" );
		other(list, "charter-ship", "Charter ships", "Ports around Gielinor", NETWORK, TeleportType.TRANSPORT, reqs(Requirement.item("Coins", 1000, COINS, "Fare varies by route and diaries; this is a conservative reminder.")), "Talk to a Trader Crewmember at charter docks.", "ship", "port", "brimhaven", "catherby" );
		otherItem(list, "royal-seed-pod", "Royal seed pod", "Grand Tree", KANDARIN, ROYAL_SEED_POD, "Reusable after Monkey Madness II.", "seed pod", "grand tree", "gnome" );
		otherItem(list, "drakans-medallion", "Drakan's medallion", "Ver Sinhaza / Darkmeyer / Barrows / Slepe", MORYTANIA, DRAKANS_MEDALLION, "Reusable Morytania teleport item.", "drakan", "darkmeyer", "ver sinhaza", "barrows", "slepe" );
		otherItem(list, "xerics-talisman", "Xeric's talisman", "Great Kourend destinations", GREAT_KOUREND, XERICS_TALISMAN, "Charged Kourend teleport talisman.", "xeric", "kourend", "zeah" );
		otherItem(list, "kharedsts-memoirs", "Kharedst's memoirs", "Great Kourend memoir destinations", GREAT_KOUREND, KHAREDSTS_MEMOIRS, "Kourend teleport book.", "memoirs", "kourend", "book" );
		otherItem(list, "book-of-the-dead", "Book of the dead", "Great Kourend memoir destinations", GREAT_KOUREND, BOOK_OF_THE_DEAD, "Upgraded Kharedst's memoirs.", "book", "dead", "kourend" );
		otherItem(list, "teleport-crystal", "Teleport crystal", "Prifddinas / Lletya", TIRANNWN, TELEPORT_CRYSTAL, "Charged elf teleport crystal.", "prifddinas", "priff", "lletya", "elf" );
		otherItem(list, "eternal-teleport-crystal", "Eternal teleport crystal", "Prifddinas / Lletya", TIRANNWN, ETERNAL_TELEPORT_CRYSTAL, "Unlimited elf teleport crystal.", "prifddinas", "priff", "lletya", "elf" );
		otherItem(list, "dorgesh-kaan-sphere", "Dorgesh-kaan sphere", "Dorgesh-Kaan", MISTHALIN, DORGESH_KAAN_SPHERE, "Cave goblin teleport sphere.", "dorgesh", "goblin", "sphere" );
		otherItem(list, "goblin-village-sphere", "Goblin village sphere", "Goblin Village", ASGARNIA, GOBLIN_VILLAGE_SPHERE, "Goblin teleport sphere.", "goblin village", "sphere" );
		otherItem(list, "plain-of-mud-sphere", "Plain of mud sphere", "Goblin Cave", ASGARNIA, PLAIN_OF_MUD_SPHERE, "Goblin teleport sphere.", "plain of mud", "goblin", "sphere" );
		otherItem(list, "grand-seed-pod", "Grand seed pod", "Grand Tree", KANDARIN, GRAND_SEED_POD, "Grand seed pod teleport.", "grand tree", "seed pod", "gnome" );
		otherItem(list, "blue-rum", "Blue rum", "Trouble Brewing", KARAMJA, BLUE_RUM, "Trouble Brewing teleport rum.", "blue rum", "trouble brewing" );
		otherItem(list, "red-rum", "Red rum", "Trouble Brewing", KARAMJA, RED_RUM, "Trouble Brewing teleport rum.", "red rum", "trouble brewing" );
	}

	private static void addDiaryAndCombatAchievementItems(List<Teleport> list)
	{
		otherItem(list, "karamja-gloves", "Karamja gloves 4", "Shilo Village gem mine / Duradel", KARAMJA, KARAMJA_GLOVES4, "Achievement diary teleport item; lower tiers have fewer daily charges.", "karamja", "gloves", "shilo", "duradel" );
		otherItem(list, "ardougne-cloak", "Ardougne cloak 4", "Ardougne monastery / Ardougne farm patch", KANDARIN, ARDOUGNE_CLOAK4, "Achievement diary teleport item; lower tiers have fewer daily charges.", "ardougne", "cloak", "monastery", "farm" );
		otherItem(list, "desert-amulet", "Desert amulet 4", "Nardah / Kalphite Cave", DESERT, DESERT_AMULET4, "Achievement diary teleport item; lower tiers have fewer daily charges.", "desert", "amulet", "nardah", "kalphite" );
		otherItem(list, "morytania-legs", "Morytania legs 4", "Ectofuntus slime pit / Burgh de Rott", MORYTANIA, MORYTANIA_LEGS4, "Achievement diary teleport item; lower tiers have fewer daily charges.", "morytania", "legs", "burgh", "slime" );
		otherItem(list, "fremennik-sea-boots", "Fremennik sea boots 4", "Rellekka", FREMENNIK_PROVINCE, FREMENNIK_SEA_BOOTS4, "Achievement diary teleport item; lower tiers have fewer daily charges.", "fremennik", "boots", "rellekka" );
		otherItem(list, "kandarin-headgear", "Kandarin headgear 4", "Sherlock", KANDARIN, KANDARIN_HEADGEAR4, "Achievement diary teleport item; lower tiers have fewer daily charges.", "kandarin", "headgear", "sherlock" );
		otherItem(list, "wilderness-sword", "Wilderness sword 4", "Fountain of Rune", WILDERNESS, WILDERNESS_SWORD4, "Achievement diary teleport item; lower tiers have fewer daily charges.", "wilderness", "sword", "fountain" );
		otherItem(list, "western-banner", "Western banner 4", "Piscatoris Fishing Colony", KANDARIN, WESTERN_BANNER4, "Achievement diary teleport item; lower tiers have fewer daily charges.", "western", "banner", "piscatoris" );
		otherItem(list, "explorers-ring", "Explorer's ring 4", "Falador cabbage patch", ASGARNIA, EXPLORERS_RING4, "Achievement diary teleport item; lower tiers have fewer daily charges.", "explorer", "cabbage", "falador" );
		otherItem(list, "radas-blessing", "Rada's blessing 4", "Kourend Woodland / Mount Karuulm", GREAT_KOUREND, RADAS_BLESSING4, "Achievement diary teleport item; lower tiers have fewer daily charges.", "rada", "blessing", "karuulm", "woodland" );
		otherItem(list, "ghommals-hilt", "Ghommal's hilt 6", "God Wars Dungeon / Mor Ul Rek", NETWORK, GHOMMALS_HILT6, "Combat Achievement teleport item; lower tiers have fewer daily charges.", "ghommal", "god wars", "mor ul rek" );
		otherItem(list, "ghommals-avernic-defender", "Ghommal's avernic defender 6", "God Wars Dungeon / Mor Ul Rek", NETWORK, GHOMMALS_AVERNIC_DEFENDER6, "Combat Achievement teleport item.", "ghommal", "avernic", "god wars", "mor ul rek" );
	}

	private static TeleportContinent FOSSIL_OR_NETWORK()
	{
		return NETWORK;
	}

	private static Requirement rune(String name, int qty, int id)
	{
		return Requirement.item(name, qty, id);
	}

	private static List<Requirement> reqs(Requirement... reqs)
	{
		return Arrays.asList(reqs);
	}

	private static Teleport spell(String id, String name, String destination, TeleportType type, TeleportContinent continent, String spellbook, int level, List<Requirement> reqs, String note, String... keywords)
	{
		return new Teleport(id, name + " - " + spellbook, destination, type, continent, level, true,
			Arrays.asList(keywords), reqs, Collections.singletonList(note), wiki(name));
	}

	private static void tablet(List<Teleport> list, String id, String name, String destination, TeleportContinent continent, int itemId, String note, String... keywords)
	{
		list.add(new Teleport(id, name, destination, TeleportType.TABLET, continent, 0, true, Arrays.asList(keywords),
			reqs(Requirement.item(name, 1, itemId)), Collections.singletonList(note), wiki(name)));
	}

	private static void jewelry(List<Teleport> list, String id, String name, String destination, TeleportContinent continent, int itemId, String note, String... keywords)
	{
		list.add(new Teleport(id, name, destination, TeleportType.JEWELRY, continent, 0, true, Arrays.asList(keywords),
			reqs(Requirement.reusable(name, itemId, "Charged or unlimited jewellery item required.")), Collections.singletonList(note), wiki(name)));
	}

	private static void otherItem(List<Teleport> list, String id, String name, String destination, TeleportContinent continent, int itemId, String note, String... keywords)
	{
		List<Requirement> requirements = itemId <= 0 ? reqs() : reqs(Requirement.reusable(name, itemId, "Reusable/charged teleport item required."));
		other(list, id, name, destination, continent, TeleportType.OTHER, requirements, note, keywords);
	}

	private static void other(List<Teleport> list, String id, String name, String destination, TeleportContinent continent, TeleportType type, List<Requirement> reqs, String note, String... keywords)
	{
		list.add(new Teleport(id, name, destination, type, continent, 0, true, Arrays.asList(keywords), reqs, Collections.singletonList(note), wiki(name)));
	}

	private static String wiki(String page)
	{
		return "https://oldschool.runescape.wiki/w/" + page.trim().replace(' ', '_');
	}
}

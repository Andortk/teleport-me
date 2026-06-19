package com.teleportme;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("teleportme")
public interface TeleportMeConfig extends Config
{
	@ConfigItem(
		keyName = "favorites",
		name = "Pinned teleports",
		description = "Internal pinned teleport ids."
	)
	default String favorites()
	{
		return "";
	}

	@ConfigItem(
		keyName = "lastSearch",
		name = "Last search",
		description = "Restores the previous Teleport Me search."
	)
	default String lastSearch()
	{
		return "";
	}

	@ConfigItem(
		keyName = "continentFilter",
		name = "Continent filter",
		description = "Restores the previous continent filter selection."
	)
	default String continentFilter()
	{
		return "-";
	}

	@ConfigItem(
		keyName = "showInBank",
		name = "Show in bank",
		description = "When enabled, masks bank items that are not needed for the selected teleport."
	)
	default boolean showInBank()
	{
		return false;
	}
}

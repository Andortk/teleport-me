package com.teleportme;

public enum TeleportContinent
{
	MISTHALIN("Misthalin"),
	ASGARNIA("Asgarnia"),
	KANDARIN("Kandarin"),
	KARAMJA("Karamja"),
	MORYTANIA("Morytania"),
	TIRANNWN("Tirannwn"),
	FREMENNIK_PROVINCE("Fremennik Province"),
	GREAT_KOUREND("Great Kourend"),
	VARLAMORE("Varlamore"),
	DESERT("Kharidian Desert"),
	WILDERNESS("Wilderness"),
	APE_ATOLL("Ape Atoll"),
	NETWORK("Network / Global");

	private final String label;

	TeleportContinent(String label)
	{
		this.label = label;
	}

	public String getLabel()
	{
		return label;
	}
}

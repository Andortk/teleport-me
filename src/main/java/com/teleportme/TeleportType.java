package com.teleportme;

public enum TeleportType
{
	MAGIC_SPELL("Magic Spell"),
	TABLET("Tablet"),
	JEWELRY("Jewelry"),
	FAIRY_RING("Fairy Ring"),
	TRANSPORT("Transport"),
	OTHER("Other");

	private final String label;

	TeleportType(String label)
	{
		this.label = label;
	}

	public String getLabel()
	{
		return label;
	}
}

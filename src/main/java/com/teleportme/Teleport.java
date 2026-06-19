package com.teleportme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public final class Teleport
{
	private final String id;
	private final String displayName;
	private final String destination;
	private final TeleportType type;
	private final TeleportContinent continent;
	private final int magicLevel;
	private final boolean members;
	private final List<String> keywords;
	private final List<Requirement> requirements;
	private final List<String> notes;
	private final String wikiUrl;

	public Teleport(String id, String displayName, String destination, TeleportType type, TeleportContinent continent, int magicLevel,
		boolean members, List<String> keywords, List<Requirement> requirements, List<String> notes, String wikiUrl)
	{
		this.id = id;
		this.displayName = displayName;
		this.destination = destination;
		this.type = type;
		this.continent = continent;
		this.magicLevel = magicLevel;
		this.members = members;
		this.keywords = Collections.unmodifiableList(new ArrayList<>(keywords));
		this.requirements = Collections.unmodifiableList(new ArrayList<>(requirements));
		this.notes = Collections.unmodifiableList(new ArrayList<>(notes));
		this.wikiUrl = wikiUrl;
	}

	public String getId()
	{
		return id;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public String getDestination()
	{
		return destination;
	}

	public TeleportType getType()
	{
		return type;
	}

	public TeleportContinent getContinent()
	{
		return continent;
	}

	public int getMagicLevel()
	{
		return magicLevel;
	}

	public boolean isMembers()
	{
		return members;
	}

	public List<String> getKeywords()
	{
		return keywords;
	}

	public List<Requirement> getRequirements()
	{
		return requirements;
	}

	public List<String> getNotes()
	{
		return notes;
	}

	public String getWikiUrl()
	{
		return wikiUrl;
	}

	public String getRequirementSummary()
	{
		String level = magicLevel > 0 ? "Magic " + magicLevel : "No Magic level";
		String reqs = requirements.isEmpty()
			? "No items"
			: requirements.stream().map(r -> r.getQuantity() + "× " + r.getDisplayName()).collect(Collectors.joining(" • "));
		return level + " • " + reqs;
	}

	public boolean matches(String query)
	{
		if (query == null || query.trim().isEmpty())
		{
			return true;
		}
		String haystack = (displayName + " " + destination + " " + type.getLabel() + " " + continent.getLabel() + " " + String.join(" ", keywords) + " "
			+ String.join(" ", notes)).toLowerCase(Locale.ROOT);
		for (String token : query.toLowerCase(Locale.ROOT).trim().split("\\s+"))
		{
			if (!haystack.contains(token))
			{
				return false;
			}
		}
		return true;
	}

	public int performableCount(Map<Integer, Integer> bankCounts)
	{
		int min = Integer.MAX_VALUE;
		boolean hasConsumedRequirement = false;
		for (Requirement requirement : requirements)
		{
			int count = requirement.performableCount(bankCounts);
			if (count == 0)
			{
				return 0;
			}
			if (requirement.isConsumed())
			{
				hasConsumedRequirement = true;
				min = Math.min(min, count);
			}
		}
		return hasConsumedRequirement ? min : (requirements.stream().allMatch(r -> r.isSatisfied(bankCounts)) ? Integer.MAX_VALUE : 0);
	}
}

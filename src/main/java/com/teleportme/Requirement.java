package com.teleportme;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A required ingredient for a teleport. Alternatives represent equivalent ways
 * to satisfy the same requirement, e.g. air rune OR staff of air.
 */
public final class Requirement
{
	private final String displayName;
	private final int quantity;
	private final List<Integer> itemIds;
	private final String notes;
	private final boolean consumed;

	private Requirement(String displayName, int quantity, List<Integer> itemIds, String notes, boolean consumed)
	{
		this.displayName = displayName;
		this.quantity = quantity;
		this.itemIds = itemIds;
		this.notes = notes;
		this.consumed = consumed;
	}

	public static Requirement item(String displayName, int quantity, int itemId)
	{
		return new Requirement(displayName, quantity, Collections.singletonList(itemId), "", true);
	}

	public static Requirement item(String displayName, int quantity, int itemId, String notes)
	{
		return new Requirement(displayName, quantity, Collections.singletonList(itemId), notes, true);
	}

	public static Requirement reusable(String displayName, int itemId, String notes)
	{
		return new Requirement(displayName, 1, Collections.singletonList(itemId), notes, false);
	}

	public static Requirement anyOf(String displayName, int quantity, String notes, Integer... itemIds)
	{
		return new Requirement(displayName, quantity, Arrays.asList(itemIds), notes, true);
	}

	public static Requirement anyReusable(String displayName, String notes, Integer... itemIds)
	{
		return new Requirement(displayName, 1, Arrays.asList(itemIds), notes, false);
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public int getQuantity()
	{
		return quantity;
	}

	public List<Integer> getItemIds()
	{
		return itemIds;
	}

	public String getNotes()
	{
		return notes;
	}

	public boolean isConsumed()
	{
		return consumed;
	}

	public int countAvailable(Map<Integer, Integer> bankCounts)
	{
		int total = 0;
		for (int itemId : itemIds)
		{
			total += bankCounts.getOrDefault(itemId, 0);
		}
		return total;
	}

	public boolean isSatisfied(Map<Integer, Integer> bankCounts)
	{
		return countAvailable(bankCounts) >= quantity;
	}

	public int performableCount(Map<Integer, Integer> bankCounts)
	{
		if (!consumed)
		{
			return isSatisfied(bankCounts) ? Integer.MAX_VALUE : 0;
		}
		return countAvailable(bankCounts) / quantity;
	}
}

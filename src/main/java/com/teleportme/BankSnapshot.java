package com.teleportme;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;

public final class BankSnapshot
{
	private final boolean bankOpen;
	private final Map<Integer, Integer> itemCounts;

	public BankSnapshot(boolean bankOpen, Map<Integer, Integer> itemCounts)
	{
		this.bankOpen = bankOpen;
		this.itemCounts = Collections.unmodifiableMap(new HashMap<>(itemCounts));
	}

	public static BankSnapshot closed()
	{
		return new BankSnapshot(false, Collections.emptyMap());
	}

	public static BankSnapshot fromContainer(ItemContainer container)
	{
		Map<Integer, Integer> counts = new HashMap<>();
		if (container != null)
		{
			for (Item item : container.getItems())
			{
				if (item != null && item.getId() >= 0 && item.getQuantity() > 0)
				{
					counts.merge(item.getId(), item.getQuantity(), Integer::sum);
				}
			}
		}
		return new BankSnapshot(true, counts);
	}

	public boolean isBankOpen()
	{
		return bankOpen;
	}

	public Map<Integer, Integer> getItemCounts()
	{
		return itemCounts;
	}
}

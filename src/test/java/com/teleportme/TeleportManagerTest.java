package com.teleportme;

import static com.teleportme.OsrsItems.*;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class TeleportManagerTest
{
	@Test
	public void starterDatasetHasUsefulCoverage()
	{
		TeleportManager manager = new TeleportManager();
		assertTrue("Expected broad OSRS teleport dataset", manager.getTeleports().size() >= 120);
		assertFalse(manager.search("varrock", Collections.emptySet(), false, null).isEmpty());
		assertFalse(manager.search("dueling ferox", Collections.emptySet(), false, null).isEmpty());
		assertFalse(manager.search("fairy", Collections.emptySet(), false, null).isEmpty());
		assertFalse(manager.search("paddewwa ancient", Collections.emptySet(), false, null).isEmpty());
		assertFalse(manager.search("moonclan lunar", Collections.emptySet(), false, null).isEmpty());
		assertFalse(manager.search("barrows arceuus", Collections.emptySet(), false, null).isEmpty());
		assertFalse(manager.search("burning amulet", Collections.emptySet(), false, null).isEmpty());
		assertFalse(manager.search("nardah scroll", Collections.emptySet(), false, null).isEmpty());
	}

	@Test
	public void searchHonorsFavoritesAndContinentFilters()
	{
		TeleportManager manager = new TeleportManager();
		List<Teleport> favoriteMisthalin = manager.search("varrock", Collections.singleton("varrock-tab"), true, TeleportContinent.MISTHALIN);
		assertEquals(1, favoriteMisthalin.size());
		assertEquals("varrock-tab", favoriteMisthalin.get(0).getId());
	}

	@Test
	public void performableCountUsesMostConstrainedConsumedRequirement()
	{
		Teleport varrock = new TeleportManager().search("varrock standard", Collections.emptySet(), false, TeleportContinent.MISTHALIN).get(0);
		Map<Integer, Integer> bank = new HashMap<>();
		bank.put(LAW_RUNE, 5);
		bank.put(AIR_RUNE, 100);
		bank.put(FIRE_RUNE, 7);
		assertEquals(5, varrock.performableCount(bank));
	}

	@Test
	public void reusableRequirementDoesNotLimitConsumedCountWhenPresent()
	{
		Teleport fairyRing = new TeleportManager().search("fairy", Collections.emptySet(), false, TeleportContinent.NETWORK).get(0);
		Map<Integer, Integer> bank = new HashMap<>();
		bank.put(DRAMEN_STAFF, 1);
		assertEquals(Integer.MAX_VALUE, fairyRing.performableCount(bank));
	}

	@Test
	public void favoritesRoundTrip()
	{
		assertTrue(TeleportMePlugin.parseFavorites("").isEmpty());
		assertEquals(2, TeleportMePlugin.parseFavorites("a,b").size());
	}
}

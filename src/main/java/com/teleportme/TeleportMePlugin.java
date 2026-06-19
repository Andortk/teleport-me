package com.teleportme;

import com.google.inject.Provides;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import net.runelite.api.gameval.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ImageUtil;

@PluginDescriptor(
	name = "Teleport Me",
	description = "Searchable teleport phonebook with requirements and bank readiness.",
	tags = {"teleport", "travel", "bank", "search"}
)
public class TeleportMePlugin extends Plugin
{
	private static final String CONFIG_GROUP = "teleportme";
	private static final String FAVORITES_KEY = "favorites";
	private static final String LAST_SEARCH_KEY = "lastSearch";
	private static final String CONTINENT_FILTER_KEY = "continentFilter";
	private static final String SHOW_IN_BANK_KEY = "showInBank";

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private ConfigManager configManager;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private TeleportMeConfig config;

	private final TeleportManager teleportManager = new TeleportManager();
	private TeleportMePanel panel;
	private TeleportMeBankOverlay bankOverlay;
	private NavigationButton navigationButton;
	private Teleport selectedTeleport;
	private boolean showInBank;
	private BankSnapshot inventorySnapshot = BankSnapshot.closed();

	@Override
	protected void startUp()
	{
		showInBank = config.showInBank();
		panel = new TeleportMePanel(teleportManager, config.lastSearch(), parseFavorites(config.favorites()),
			config.continentFilter(), showInBank, this::saveLastSearch, this::saveFavorites, this::saveContinentFilter,
			this::saveShowInBank, teleport -> selectedTeleport = teleport);
		bankOverlay = new TeleportMeBankOverlay(() -> selectedTeleport, () -> inventorySnapshot, () -> showInBank);
		overlayManager.add(bankOverlay);
		BufferedImage icon = ImageUtil.loadImageResource(getClass(), "/icon.png");
		navigationButton = NavigationButton.builder()
			.tooltip("Teleport Me")
			.icon(icon)
			.priority(6)
			.panel(panel)
			.build();
		clientToolbar.addNavigation(navigationButton);
	}

	@Override
	protected void shutDown()
	{
		if (navigationButton != null)
		{
			clientToolbar.removeNavigation(navigationButton);
		}
		if (bankOverlay != null)
		{
			overlayManager.remove(bankOverlay);
		}
		bankOverlay = null;
		navigationButton = null;
		selectedTeleport = null;
		inventorySnapshot = BankSnapshot.closed();
		panel = null;
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		if (event.getContainerId() == InventoryID.BANK && panel != null)
		{
			ItemContainer container = event.getItemContainer();
			panel.updateBank(BankSnapshot.fromContainer(container));
		}
		else if (event.getContainerId() == InventoryID.INV)
		{
			inventorySnapshot = BankSnapshot.fromContainer(event.getItemContainer());
			clearBankMaskIfInventoryNowSatisfiesTeleport();
		}
	}

	private void clearBankMaskIfInventoryNowSatisfiesTeleport()
	{
		Teleport selected = selectedTeleport;
		if (selected == null || !inventorySatisfies(selected))
		{
			return;
		}

		selectedTeleport = null;
		if (panel != null)
		{
			panel.clearSelectionIfSelected(selected);
		}
	}

	private boolean inventorySatisfies(Teleport teleport)
	{
		return teleport.getRequirements().stream().allMatch(requirement -> requirement.isSatisfied(inventorySnapshot.getItemCounts()));
	}

	@Provides
	TeleportMeConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TeleportMeConfig.class);
	}

	private void saveLastSearch(String search)
	{
		configManager.setConfiguration(CONFIG_GROUP, LAST_SEARCH_KEY, search == null ? "" : search);
	}

	private void saveFavorites(Set<String> favorites)
	{
		String encoded = favorites.stream().sorted().collect(Collectors.joining(","));
		configManager.setConfiguration(CONFIG_GROUP, FAVORITES_KEY, encoded);
	}

	private void saveContinentFilter(String filter)
	{
		configManager.setConfiguration(CONFIG_GROUP, CONTINENT_FILTER_KEY, filter == null ? "-" : filter);
	}

	private void saveShowInBank(boolean enabled)
	{
		showInBank = enabled;
		configManager.setConfiguration(CONFIG_GROUP, SHOW_IN_BANK_KEY, enabled);
	}

	static Set<String> parseFavorites(String encoded)
	{
		if (encoded == null || encoded.trim().isEmpty())
		{
			return Collections.emptySet();
		}
		return Arrays.stream(encoded.split(","))
			.map(String::trim)
			.filter(s -> !s.isEmpty())
			.collect(Collectors.toCollection(HashSet::new));
	}
}

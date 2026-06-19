package com.teleportme;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.overlay.WidgetItemOverlay;

public final class TeleportMeBankOverlay extends WidgetItemOverlay
{
	private static final Color BANK_MASK = new Color(ColorScheme.DARK_GRAY_COLOR.getRed(), ColorScheme.DARK_GRAY_COLOR.getGreen(), ColorScheme.DARK_GRAY_COLOR.getBlue(), 235);

	private final Supplier<Teleport> selectedTeleportSupplier;
	private final Supplier<BankSnapshot> inventorySnapshotSupplier;
	private final Supplier<Boolean> enabledSupplier;

	public TeleportMeBankOverlay(Supplier<Teleport> selectedTeleportSupplier, Supplier<BankSnapshot> inventorySnapshotSupplier, Supplier<Boolean> enabledSupplier)
	{
		this.selectedTeleportSupplier = selectedTeleportSupplier;
		this.inventorySnapshotSupplier = inventorySnapshotSupplier;
		this.enabledSupplier = enabledSupplier;
		showOnBank();
	}

	@Override
	public void renderItemOverlay(Graphics2D graphics, int itemId, WidgetItem widgetItem)
	{
		Teleport selected = selectedTeleportSupplier.get();
		if (!Boolean.TRUE.equals(enabledSupplier.get()) || selected == null || inventoryAlreadyHasRequirements(selected))
		{
			return;
		}

		if (neededItemIds(selected).contains(itemId))
		{
			return;
		}

		Rectangle bounds = widgetItem.getCanvasBounds();
		graphics.setColor(BANK_MASK);
		graphics.fill(bounds);
	}

	private boolean inventoryAlreadyHasRequirements(Teleport teleport)
	{
		BankSnapshot inventory = inventorySnapshotSupplier.get();
		if (inventory == null)
		{
			return false;
		}
		return teleport.getRequirements().stream().allMatch(requirement -> requirement.isSatisfied(inventory.getItemCounts()));
	}

	private static Set<Integer> neededItemIds(Teleport teleport)
	{
		Set<Integer> ids = new HashSet<>();
		for (Requirement requirement : teleport.getRequirements())
		{
			ids.addAll(requirement.getItemIds());
		}
		return ids;
	}
}

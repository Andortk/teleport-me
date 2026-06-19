package com.teleportme;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

final class ItemIconStore
{
	private static final int DEFAULT_ICON_SIZE = 20;
	private static final Map<String, ImageIcon> CACHE = new HashMap<>();

	private ItemIconStore() {}

	static ImageIcon icon(int itemId)
	{
		return icon(itemId, DEFAULT_ICON_SIZE);
	}

	static ImageIcon icon(int itemId, int size)
	{
		String key = itemId + ":" + size;
		ImageIcon cached = CACHE.get(key);
		if (cached != null)
		{
			return cached;
		}

		try
		{
			BufferedImage image = ImageIO.read(ItemIconStore.class.getResourceAsStream("/item-icons/" + itemId + ".png"));
			if (image == null)
			{
				return null;
			}
			Image scaled = image.getScaledInstance(size, size, Image.SCALE_SMOOTH);
			ImageIcon icon = new ImageIcon(scaled);
			CACHE.put(key, icon);
			return icon;
		}
		catch (Exception ignored)
		{
			return null;
		}
	}
}

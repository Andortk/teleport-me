package com.teleportme;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

public final class TeleportMePanel extends PluginPanel
{
	private static final Color CARD_COLOR = darker(ColorScheme.DARK_GRAY_HOVER_COLOR, 0.30d);
	private static final Color SELECTED_CARD_COLOR = darker(ColorScheme.DARKER_GRAY_COLOR, 0.30d);
	private static final Color SELECTED_BORDER_COLOR = new Color(145, 101, 35);
	private static final Color PINNED_PIN_COLOR = Color.WHITE;
	private static final Color AVAILABLE_PIN_COLOR = new Color(150, 150, 150);
	private static final int CARD_BASE_HEIGHT = 92;

	private final TeleportManager teleportManager;
	private final Consumer<String> lastSearchConsumer;
	private final Consumer<Set<String>> pinnedConsumer;
	private final Consumer<String> continentFilterConsumer;
	private final Consumer<Boolean> showInBankConsumer;
	private final Consumer<Teleport> selectionConsumer;

	private final JTextField searchField = new SearchField();
	private final JCheckBox pinnedOnly = new JCheckBox("Pinned");
	private final JCheckBox showInBank = new JCheckBox("Show in bank");
	private final JComboBox<String> typeFilter = new JComboBox<>();
	private final JPanel results = new VerticalResultsPanel();
	private final JLabel status = new JLabel("Bank not open");
	private final JPanel detail = new JPanel();

	private BankSnapshot bankSnapshot = BankSnapshot.closed();
	private Set<String> pinned = new HashSet<>();
	private Teleport selected;

	public TeleportMePanel(TeleportManager teleportManager, String initialSearch, Set<String> initialPinned,
		String initialContinentFilter, boolean initialShowInBank, Consumer<String> lastSearchConsumer,
		Consumer<Set<String>> pinnedConsumer, Consumer<String> continentFilterConsumer,
		Consumer<Boolean> showInBankConsumer, Consumer<Teleport> selectionConsumer)
	{
		super(false);
		this.teleportManager = teleportManager;
		this.lastSearchConsumer = lastSearchConsumer;
		this.pinnedConsumer = pinnedConsumer;
		this.continentFilterConsumer = continentFilterConsumer;
		this.showInBankConsumer = showInBankConsumer;
		this.selectionConsumer = selectionConsumer;
		this.pinned = new HashSet<>(initialPinned);

		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(8, 8, 8, 8));
		setBackground(ColorScheme.DARK_GRAY_COLOR);

		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
		top.setBackground(ColorScheme.DARK_GRAY_COLOR);
		top.setAlignmentX(LEFT_ALIGNMENT);

		JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
		header.setOpaque(false);
		header.setAlignmentX(LEFT_ALIGNMENT);
		header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
		JLabel title = new JLabel("Teleport Me");
		title.setForeground(Color.WHITE);
		title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
		header.add(title);
		header.add(new JLabel(ItemIconStore.icon(OsrsItems.LAW_RUNE, 24)));
		top.add(header);
		top.add(Box.createRigidArea(new Dimension(0, 6)));

		searchField.setText(initialSearch == null ? "" : initialSearch);
		searchField.setToolTipText("Where do you want to go?");
		searchField.setAlignmentX(LEFT_ALIGNMENT);
		Dimension searchSize = searchField.getPreferredSize();
		searchSize = new Dimension(searchSize.width, (int) Math.ceil(searchSize.height * 1.2d));
		searchField.setPreferredSize(searchSize);
		searchField.setMinimumSize(new Dimension(0, searchSize.height));
		searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchSize.height));
		top.add(searchField);
		top.add(Box.createRigidArea(new Dimension(0, 9)));

		JPanel filters = new JPanel(new GridLayout(1, 2, 6, 0));
		filters.setBackground(ColorScheme.DARK_GRAY_COLOR);
		filters.setAlignmentX(LEFT_ALIGNMENT);
		Dimension filtersSize = new Dimension(0, 54);
		filters.setPreferredSize(filtersSize);
		filters.setMinimumSize(filtersSize);
		filters.setMaximumSize(new Dimension(Integer.MAX_VALUE, filtersSize.height));
		JPanel checkboxes = new JPanel();
		checkboxes.setLayout(new BoxLayout(checkboxes, BoxLayout.Y_AXIS));
		checkboxes.setBackground(ColorScheme.DARK_GRAY_COLOR);
		pinnedOnly.setSelected(true);
		pinnedOnly.setBackground(ColorScheme.DARK_GRAY_COLOR);
		pinnedOnly.setForeground(Color.WHITE);
		showInBank.setSelected(initialShowInBank);
		showInBank.setBackground(ColorScheme.DARK_GRAY_COLOR);
		showInBank.setForeground(Color.WHITE);
		checkboxes.add(pinnedOnly);
		checkboxes.add(Box.createRigidArea(new Dimension(0, 3)));
		checkboxes.add(showInBank);
		typeFilter.addItem("-");
		typeFilter.addItem("All");
		for (TeleportContinent continent : TeleportContinent.values())
		{
			typeFilter.addItem(continent.getLabel());
		}
		selectInitialContinentFilter(initialContinentFilter);
		filters.add(checkboxes);
		filters.add(typeFilter);
		top.add(filters);

		add(top, BorderLayout.NORTH);

		results.setLayout(new BoxLayout(results, BoxLayout.Y_AXIS));
		results.setBackground(ColorScheme.DARK_GRAY_COLOR);
		results.setBorder(new EmptyBorder(20, 0, 0, 0));
		JScrollPane scrollPane = new JScrollPane(results);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		add(scrollPane, BorderLayout.CENTER);

		detail.setLayout(new BoxLayout(detail, BoxLayout.Y_AXIS));
		detail.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		detail.setBorder(new EmptyBorder(8, 8, 8, 8));
		add(detail, BorderLayout.SOUTH);

		searchField.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override public void insertUpdate(DocumentEvent e) { refreshAndSave(); }
			@Override public void removeUpdate(DocumentEvent e) { refreshAndSave(); }
			@Override public void changedUpdate(DocumentEvent e) { refreshAndSave(); }
		});
		pinnedOnly.addActionListener(e -> refreshResults());
		showInBank.addActionListener(e -> showInBankConsumer.accept(showInBank.isSelected()));
		typeFilter.addActionListener(e -> {
			continentFilterConsumer.accept(selectedFilterLabel());
			refreshResults();
		});
		refreshResults();
	}

	public void updateBank(BankSnapshot snapshot)
	{
		bankSnapshot = snapshot == null ? BankSnapshot.closed() : snapshot;
		SwingUtilities.invokeLater(() -> {
			refreshDetail();
			refreshResults();
		});
	}

	public void clearSelectionIfSelected(Teleport teleport)
	{
		if (teleport == null || selected == null || !selected.getId().equals(teleport.getId()))
		{
			return;
		}

		SwingUtilities.invokeLater(() -> {
			selected = null;
			refreshDetail();
			refreshResults();
		});
	}

	private void refreshAndSave()
	{
		lastSearchConsumer.accept(searchField.getText());
		refreshResults();
	}

	private void refreshResults()
	{
		results.removeAll();
		TeleportContinent filter = selectedContinent();
		String query = searchField.getText();
		boolean emptySearch = query == null || query.trim().isEmpty();
		boolean allSelected = typeFilter.getSelectedIndex() == 1;
		boolean hasContinent = filter != null;
		List<Teleport> baseMatches = !emptySearch || allSelected || hasContinent
			? teleportManager.search(query, pinned, false, filter, false)
			: Collections.emptyList();
		List<Teleport> matches = mergePinned(baseMatches);
		for (Teleport teleport : matches)
		{
			results.add(card(teleport));
			results.add(Box.createRigidArea(new Dimension(0, 6)));
		}
		if (matches.isEmpty())
		{
			results.add(emptyStateCard());
		}
		results.revalidate();
		results.repaint();
	}

	private List<Teleport> mergePinned(List<Teleport> baseMatches)
	{
		if (!pinnedOnly.isSelected())
		{
			return baseMatches;
		}

		LinkedHashSet<Teleport> merged = new LinkedHashSet<>();
		merged.addAll(teleportManager.search("", pinned, true, null, true));
		merged.addAll(baseMatches);
		return new ArrayList<>(merged);
	}

	private TeleportContinent selectedContinent()
	{
		int idx = typeFilter.getSelectedIndex();
		return idx <= 1 ? null : TeleportContinent.values()[idx - 2];
	}

	private String selectedFilterLabel()
	{
		Object selectedItem = typeFilter.getSelectedItem();
		return selectedItem == null ? "-" : selectedItem.toString();
	}

	private void selectInitialContinentFilter(String initialContinentFilter)
	{
		String filter = initialContinentFilter == null || initialContinentFilter.trim().isEmpty() ? "-" : initialContinentFilter.trim();
		for (int i = 0; i < typeFilter.getItemCount(); i++)
		{
			if (filter.equals(typeFilter.getItemAt(i)))
			{
				typeFilter.setSelectedIndex(i);
				return;
			}
		}
		typeFilter.setSelectedIndex(0);
	}

	private JPanel card(Teleport teleport)
	{
		boolean expanded = teleport == selected;
		boolean isPinned = pinned.contains(teleport.getId());
		JPanel card = new JPanel(new BorderLayout());
		card.setBackground(expanded ? SELECTED_CARD_COLOR : CARD_COLOR);
		Color borderColor = expanded ? SELECTED_BORDER_COLOR : ColorScheme.MEDIUM_GRAY_COLOR;
		card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(borderColor, expanded ? 2 : 1), new EmptyBorder(6, 6, 6, 6)));

		JPanel text = new JPanel();
		text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
		text.setOpaque(false);
		JLabel name = new JLabel(titleText(teleport));
		name.setForeground(Color.WHITE);
		name.setFont(name.getFont().deriveFont(Font.BOLD));
		text.add(name);
		if (expanded)
		{
			JLabel meta = new JLabel(subtitleText(teleport));
			meta.setForeground(Color.LIGHT_GRAY);
			text.add(meta);
		}
		text.add(requirementIcons(teleport.getRequirements(), false, bankSnapshot.isBankOpen()));
		card.add(text, BorderLayout.CENTER);
		if (expanded || isPinned)
		{
			JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
			actions.setOpaque(false);
			JButton pin = pinButton(teleport, isPinned);
			pin.addActionListener(e -> togglePinned(teleport));
			actions.add(pin);
			card.add(actions, BorderLayout.SOUTH);
		}
		installSelectionHandler(card, teleport);
		Dimension preferred = card.getPreferredSize();
		preferred = new Dimension(preferred.width, Math.max(CARD_BASE_HEIGHT, preferred.height));
		card.setPreferredSize(preferred);
		card.setMinimumSize(new Dimension(0, preferred.height));
		card.setMaximumSize(new Dimension(Integer.MAX_VALUE, preferred.height));
		return card;
	}

	private void togglePinned(Teleport teleport)
	{
		focus(teleport);
		if (!pinned.add(teleport.getId()))
		{
			pinned.remove(teleport.getId());
		}
		pinnedConsumer.accept(Collections.unmodifiableSet(pinned));
		refreshResults();
	}

	private JPanel emptyStateCard()
	{
		JPanel card = new JPanel();
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setBackground(CARD_COLOR);
		card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(ColorScheme.MEDIUM_GRAY_COLOR), new EmptyBorder(10, 10, 10, 10)));
		JLabel title = new JLabel("Search for a place to teleport to.");
		title.setForeground(Color.WHITE);
		title.setFont(title.getFont().deriveFont(Font.BOLD));
		JLabel hint = new JLabel("Or select All / a continent from the dropdown.");
		hint.setForeground(Color.LIGHT_GRAY);
		card.add(title);
		card.add(Box.createRigidArea(new Dimension(0, 4)));
		card.add(hint);
		card.setMaximumSize(new Dimension(Integer.MAX_VALUE, card.getPreferredSize().height));
		return card;
	}

	private JButton pinButton(Teleport teleport, boolean isPinned)
	{
		JButton pin = new JButton(new PinIcon(isPinned ? PINNED_PIN_COLOR : AVAILABLE_PIN_COLOR));
		pin.setToolTipText(isPinned ? "Unpin this teleport" : "Pin this teleport");
		pin.setBorder(new EmptyBorder(2, 4, 2, 4));
		pin.setContentAreaFilled(false);
		pin.setFocusPainted(false);
		pin.setOpaque(false);
		pin.setActionCommand(teleport.getId());
		return pin;
	}

	private void installSelectionHandler(Component component, Teleport teleport)
	{
		if (!(component instanceof JButton))
		{
			component.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mousePressed(MouseEvent e)
				{
					selectFromCard(teleport);
				}
			});
		}

		if (component instanceof JPanel)
		{
			for (Component child : ((JPanel) component).getComponents())
			{
				installSelectionHandler(child, teleport);
			}
		}
	}

	private String titleText(Teleport teleport)
	{
		return teleport.getDisplayName().replace(" - Standard Spellbook", "");
	}

	private String subtitleText(Teleport teleport)
	{
		return teleport.getType().getLabel() + " - " + teleport.getDestination();
	}

	private void selectFromCard(Teleport teleport)
	{
		if (selected == teleport)
		{
			selected = null;
			selectionConsumer.accept(null);
			refreshResults();
			refreshDetail();
			return;
		}
		focus(teleport);
	}

	private void focus(Teleport teleport)
	{
		selected = teleport;
		selectionConsumer.accept(teleport);
		refreshResults();
		refreshDetail();
	}

	private JPanel requirementIcons(List<Requirement> requirements, boolean includeBankCounts, boolean bankOpen)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setOpaque(false);
		panel.setBorder(new EmptyBorder(6, 0, 0, 0));
		for (Requirement requirement : requirements)
		{
			boolean satisfied = !bankOpen || requirement.isSatisfied(bankSnapshot.getItemCounts());
			for (int itemId : requirement.getItemIds())
			{
				JLabel label = itemLabel(requirement, itemId, includeBankCounts, satisfied);
				label.setAlignmentX(LEFT_ALIGNMENT);
				panel.add(label);
			}
		}
		panel.setAlignmentX(LEFT_ALIGNMENT);
		return panel;
	}

	private JLabel itemLabel(Requirement requirement, int itemId, boolean includeBankCounts, boolean satisfied)
	{
		String text = requirement.getQuantity() + "x " + OsrsItems.name(itemId);
		if (includeBankCounts)
		{
			int have = bankSnapshot.getItemCounts().getOrDefault(itemId, 0);
			text += " (bank: " + have + ")";
		}

		JLabel label = new JLabel(text, ItemIconStore.icon(itemId), SwingConstants.LEFT);
		label.setIconTextGap(4);
		label.setForeground(satisfied ? Color.LIGHT_GRAY : Color.PINK);
		label.setBorder(new EmptyBorder(1, 2, 1, 4));
		label.setToolTipText(requirement.getDisplayName() + (requirement.getNotes().isEmpty() ? "" : " - " + requirement.getNotes()));
		return label;
	}

	private void refreshDetail()
	{
		detail.removeAll();
		if (selected == null)
		{
			JLabel hint = new JLabel("Select a teleport for exact requirements and bank status.");
			hint.setForeground(Color.LIGHT_GRAY);
			detail.add(hint);
		}
		else
		{
			JLabel name = new JLabel(titleText(selected));
			name.setForeground(Color.WHITE);
			name.setFont(name.getFont().deriveFont(Font.BOLD));
			detail.add(name);
			detail.add(requirementIcons(selected.getRequirements(), true, bankSnapshot.isBankOpen()));
			String notes = selected.getNotes().stream().collect(Collectors.joining(" "));
			JLabel note = new JLabel("Notes: " + notes);
			note.setForeground(Color.LIGHT_GRAY);
			detail.add(note);
			int count = selected.performableCount(bankSnapshot.getItemCounts());
			String text = !bankSnapshot.isBankOpen() ? "Bank not open" : count == Integer.MAX_VALUE ? "Reusable teleport available" : "Can perform from bank: " + count;
			status.setText(text);
			status.setForeground(count == 0 && bankSnapshot.isBankOpen() ? Color.PINK : Color.WHITE);
			detail.add(status);
		}
		detail.revalidate();
		detail.repaint();
	}

	private static Color darker(Color color, double fraction)
	{
		int red = (int) Math.max(0, color.getRed() * (1.0d - fraction));
		int green = (int) Math.max(0, color.getGreen() * (1.0d - fraction));
		int blue = (int) Math.max(0, color.getBlue() * (1.0d - fraction));
		return new Color(red, green, blue, color.getAlpha());
	}

	private static final class SearchField extends JTextField
	{
		private final Icon icon = new MagnifyingGlassIcon(new Color(145, 145, 145));
		private final Color normalBackground;
		private final Color hoverBackground;

		private SearchField()
		{
			normalBackground = getBackground();
			hoverBackground = lighter(normalBackground, 0.16d);
			setBorder(BorderFactory.createCompoundBorder(getBorder(), new EmptyBorder(0, 24, 0, 0)));
			addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseEntered(MouseEvent e)
				{
					setBackground(hoverBackground);
				}

				@Override
				public void mouseExited(MouseEvent e)
				{
					setBackground(normalBackground);
				}
			});
		}

		@Override
		protected void paintComponent(Graphics graphics)
		{
			super.paintComponent(graphics);
			int y = (getHeight() - icon.getIconHeight()) / 2;
			icon.paintIcon(this, graphics, 7, y);
		}

		private static Color lighter(Color color, double fraction)
		{
			int red = (int) Math.min(255, color.getRed() + ((255 - color.getRed()) * fraction));
			int green = (int) Math.min(255, color.getGreen() + ((255 - color.getGreen()) * fraction));
			int blue = (int) Math.min(255, color.getBlue() + ((255 - color.getBlue()) * fraction));
			return new Color(red, green, blue, color.getAlpha());
		}
	}

	private static final class MagnifyingGlassIcon implements Icon
	{
		private static final int SIZE = 14;

		private final Color color;

		private MagnifyingGlassIcon(Color color)
		{
			this.color = color;
		}

		@Override
		public void paintIcon(Component component, Graphics graphics, int x, int y)
		{
			Graphics2D g = (Graphics2D) graphics.create();
			try
			{
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setColor(color);
				g.drawOval(x + 1, y + 1, 8, 8);
				g.drawLine(x + 8, y + 8, x + 13, y + 13);
			}
			finally
			{
				g.dispose();
			}
		}

		@Override
		public int getIconWidth()
		{
			return SIZE;
		}

		@Override
		public int getIconHeight()
		{
			return SIZE;
		}
	}

	private static final class PinIcon implements Icon
	{
		private static final int SIZE = 16;

		private final Color color;

		private PinIcon(Color color)
		{
			this.color = color;
		}

		@Override
		public void paintIcon(Component component, Graphics graphics, int x, int y)
		{
			Graphics2D g = (Graphics2D) graphics.create();
			try
			{
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setColor(color);
				g.fillOval(x + 5, y + 1, 6, 6);
				g.fillRoundRect(x + 4, y + 5, 8, 5, 2, 2);
				g.drawLine(x + 8, y + 9, x + 8, y + 15);
				g.drawLine(x + 5, y + 12, x + 11, y + 12);
			}
			finally
			{
				g.dispose();
			}
		}

		@Override
		public int getIconWidth()
		{
			return SIZE;
		}

		@Override
		public int getIconHeight()
		{
			return SIZE;
		}
	}

	private static final class VerticalResultsPanel extends JPanel implements Scrollable
	{
		@Override
		public Dimension getPreferredScrollableViewportSize()
		{
			return getPreferredSize();
		}

		@Override
		public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction)
		{
			return 16;
		}

		@Override
		public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction)
		{
			return Math.max(16, visibleRect.height - 16);
		}

		@Override
		public boolean getScrollableTracksViewportWidth()
		{
			return true;
		}

		@Override
		public boolean getScrollableTracksViewportHeight()
		{
			return false;
		}
	}
}

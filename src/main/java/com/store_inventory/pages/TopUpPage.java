package com.store_inventory.pages;

import com.store_inventory.models.Student;
import com.store_inventory.pages.components.UITheme;
import com.store_inventory.services.AppServices;
import com.store_inventory.services.TopUpProviderRegistry;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.Image;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TopUpPage extends JPanel implements Refreshable {
  private static final DecimalFormat CURRENCY = new DecimalFormat("#,##0.00");
  private static final double MIN_TOP_UP = 1;
  private static final double MAX_TOP_UP = 100000;
  private static final double TOP_UP_STEP = 100;

  private final AppServices appServices;
  private final NavigationHandler navigationHandler;
  private final JLabel walletBalanceValue = new JLabel("PHP 0.00");
  private final List<TopUpProviderRegistry.TopUpProvider> providers =
      TopUpProviderRegistry.getProviders();

  public TopUpPage(AppServices appServices, NavigationHandler navigationHandler) {
    this.appServices = appServices;
    this.navigationHandler = navigationHandler;
    setLayout(new BorderLayout());
    setBackground(UITheme.BACKGROUND);

    JPanel content = new JPanel();
    content.setOpaque(false);
    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
    content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JPanel card = UITheme.cardPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
    card.setAlignmentX(Component.LEFT_ALIGNMENT);

    JLabel title = new JLabel("Top Up Wallet");
    title.setFont(UITheme.TITLE_FONT);
    title.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(title);

    JLabel subtitle = new JLabel("Current wallet balance");
    subtitle.setFont(UITheme.SUBTITLE_FONT);
    subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
    subtitle.setForeground(UITheme.MUTED_TEXT);

    walletBalanceValue.setFont(UITheme.customFont(
        UITheme.FONT_FAMILY, UITheme.FONT_WEIGHT_LABEL, 20));
    walletBalanceValue.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(walletBalanceValue);

    card.add(title);
    card.add(Box.createVerticalStrut(6));
    card.add(subtitle);
    card.add(Box.createVerticalStrut(10));
    card.add(walletBalanceValue);

    JPanel providersCard = buildProvidersCard();
    providersCard.setAlignmentX(Component.LEFT_ALIGNMENT);

    content.add(card);
    content.add(Box.createVerticalStrut(12));
    content.add(providersCard);

    JScrollPane scrollPane = new JScrollPane(content);
    scrollPane.setOpaque(false);
    scrollPane.getViewport().setOpaque(false);
    scrollPane.setBorder(null);
    scrollPane.setHorizontalScrollBarPolicy(
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    UITheme.themeScrollPane(scrollPane);

    add(scrollPane, BorderLayout.CENTER);

    refresh();
  }

  private JPanel buildProvidersCard() {
    JPanel card = UITheme.cardPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

    JLabel title = new JLabel("Choose Top-Up Provider");
    title.setFont(UITheme.SUBTITLE_FONT);
    title.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(title);

    JLabel note = new JLabel("Secure mock checkout. No real money transfer.");
    note.setFont(UITheme.LABEL_FONT);
    note.setAlignmentX(Component.LEFT_ALIGNMENT);
    note.setForeground(UITheme.MUTED_TEXT);

    JPanel grid = new JPanel(new GridLayout(1, providers.size(), 10, 0));
    grid.setOpaque(false);
    grid.setAlignmentX(Component.LEFT_ALIGNMENT);
    for (TopUpProviderRegistry.TopUpProvider provider : providers) {
      grid.add(buildProviderTile(provider));
    }

    card.add(title);
    card.add(Box.createVerticalStrut(4));
    card.add(note);
    card.add(Box.createVerticalStrut(12));
    card.add(grid);
    return card;
  }

  private JPanel buildProviderTile(TopUpProviderRegistry.TopUpProvider provider) {
    JPanel tile = UITheme.cardPanel();
    tile.setLayout(new BoxLayout(tile, BoxLayout.Y_AXIS));
    tile.setPreferredSize(new Dimension(200, 190));
    tile.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

    JLabel name = new JLabel(provider.getDisplayName());
    name.setFont(UITheme.SUBTITLE_FONT);
    name.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(name);

    JLabel channel = new JLabel(provider.getChannel());
    channel.setFont(UITheme.customFont(UITheme.FONT_FAMILY,
                                       UITheme.FONT_WEIGHT_LABEL, 13));
    channel.setAlignmentX(Component.LEFT_ALIGNMENT);
    channel.setForeground(UITheme.MUTED_TEXT);

    JLabel hint = new JLabel(provider.getAccountHint());
    hint.setFont(UITheme.customFont(UITheme.FONT_FAMILY,
                                    UITheme.FONT_WEIGHT_LABEL, 12));
    hint.setAlignmentX(Component.LEFT_ALIGNMENT);
    hint.setForeground(UITheme.MUTED_TEXT);

    JLabel logo = providerLogo(provider);
    logo.setAlignmentX(Component.LEFT_ALIGNMENT);

    JButton button = UITheme.primaryButton("Continue");
    button.setAlignmentX(Component.LEFT_ALIGNMENT);
    button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
    button.addActionListener(e -> openTopUpDialog(provider));

    tile.add(name);
    tile.add(Box.createVerticalStrut(2));
    tile.add(channel);
    tile.add(Box.createVerticalStrut(4));
    tile.add(logo);
    tile.add(Box.createVerticalStrut(4));
    tile.add(hint);
    tile.add(Box.createVerticalStrut(6));
    tile.add(button);

    return tile;
  }

  private void openTopUpDialog(TopUpProviderRegistry.TopUpProvider provider) {
    JDialog dialog = new JDialog(
        SwingUtilities.getWindowAncestor(this),
        "Top Up via " + provider.getDisplayName(),
        Dialog.ModalityType.APPLICATION_MODAL);
    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

    JPanel root = new JPanel(new BorderLayout());
    root.setBackground(UITheme.BACKGROUND);
    root.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

    JPanel card = UITheme.cardPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
    card.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

    JPanel headerPanel = new JPanel();
    headerPanel.setOpaque(false);
    headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
    headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

    JLabel title = new JLabel(provider.getDisplayName() + " Checkout");
    title.setFont(UITheme.SUBTITLE_FONT);
    title.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(title);

    JLabel subtitle = new JLabel("Set amount for " + provider.getDisplayName());
    subtitle.setFont(UITheme.customFont(UITheme.FONT_FAMILY,
                                        UITheme.FONT_WEIGHT_LABEL, 13));
    subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
    subtitle.setForeground(UITheme.MUTED_TEXT);

    headerPanel.add(title);
    headerPanel.add(Box.createVerticalStrut(4));
    headerPanel.add(subtitle);
    headerPanel.add(Box.createVerticalStrut(16));

    final double[] amount = new double[] {500};
    JPanel amountPanel = new JPanel();
    amountPanel.setOpaque(false);
    amountPanel.setLayout(new BoxLayout(amountPanel, BoxLayout.Y_AXIS));
    amountPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    amountPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

    JLabel amountLabel = new JLabel("Amount PHP");
    amountLabel.setFont(UITheme.LABEL_FONT);
    amountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(amountLabel);

    JLabel amountValue = new JLabel("PHP " + CURRENCY.format(amount[0]));
    amountValue.setFont(UITheme.customFont(UITheme.FONT_FAMILY,
                                           UITheme.FONT_WEIGHT_TITLE, 30));
    amountValue.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(amountValue);

    amountPanel.add(amountLabel);
    amountPanel.add(Box.createVerticalStrut(8));
    amountPanel.add(amountValue);

    JPanel controls = new JPanel(new GridLayout(1, 5, 10, 10));
    controls.setOpaque(false);
    controls.setAlignmentX(Component.LEFT_ALIGNMENT);
    controls.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

    JButton minusOneButton = UITheme.secondaryButton("-1");
    JButton minusHundredButton = UITheme.secondaryButton("-100");
    JButton resetButton = UITheme.secondaryButton("0");
    JButton plusHundredButton = UITheme.secondaryButton("+100");
    JButton plusOneButton = UITheme.secondaryButton("+1");

    minusOneButton.addActionListener(e -> {
      amount[0] = clampQuickAmount(amount[0] - 1);
      syncAmountLabel(amountValue, amount[0]);
    });
    minusHundredButton.addActionListener(e -> {
      amount[0] = clampQuickAmount(amount[0] - TOP_UP_STEP);
      syncAmountLabel(amountValue, amount[0]);
    });
    resetButton.addActionListener(e -> {
      amount[0] = 0;
      syncAmountLabel(amountValue, amount[0]);
    });
    plusHundredButton.addActionListener(e -> {
      amount[0] = clampQuickAmount(amount[0] + TOP_UP_STEP);
      syncAmountLabel(amountValue, amount[0]);
    });
    plusOneButton.addActionListener(e -> {
      amount[0] = clampQuickAmount(amount[0] + 1);
      syncAmountLabel(amountValue, amount[0]);
    });

    controls.add(minusOneButton);
    controls.add(minusHundredButton);
    controls.add(resetButton);
    controls.add(plusHundredButton);
    controls.add(plusOneButton);

    JPanel actions = new JPanel(new GridLayout(1, 2, 20, 0));
    actions.setOpaque(false);
    actions.setAlignmentX(Component.LEFT_ALIGNMENT);
    actions.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

    JButton cancelButton = UITheme.secondaryButton("Cancel");
    JButton submitButton = UITheme.primaryButton("Top Up Now");
    cancelButton.addActionListener(e -> dialog.dispose());
    submitButton.addActionListener(e -> submitTopUp(dialog, provider, amount[0]));

    actions.add(cancelButton);
    actions.add(submitButton);

    card.add(headerPanel);
    card.add(amountPanel);
    card.add(Box.createVerticalStrut(20));
    card.add(controls);
    card.add(Box.createVerticalGlue());
    card.add(Box.createVerticalStrut(20));
    card.add(actions);

    root.add(card, BorderLayout.CENTER);
    dialog.setContentPane(root);
    dialog.pack();
    dialog.setSize(new Dimension(650, 450));
    dialog.setLocationRelativeTo(this);
    dialog.setResizable(false);
    dialog.setVisible(true);
  }

  private void submitTopUp(JDialog dialog,
                           TopUpProviderRegistry.TopUpProvider provider,
                           double amount) {
    String validationError = validateTopUpAmount(amount);
    if (validationError != null) {
      JOptionPane.showMessageDialog(dialog, validationError, "Top Up Error",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    }

    Student currentStudent = appServices.getCurrentStudent();
    if (currentStudent == null) {
      JOptionPane.showMessageDialog(
          dialog, "No active student session found. Please login again.",
          "Session Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    try {
      appServices.getStudentManager().topUpWallet(currentStudent, amount);
      refresh();
      dialog.dispose();

      String reference = buildReference(provider);
      String message = "Top-up successful!\n\n"
                       + "Provider: " + provider.getDisplayName() + "\n"
                       + "Amount: PHP " + CURRENCY.format(amount) + "\n"
                       + "Reference: " + reference + "\n"
                       + "New Wallet Balance: PHP "
                       + CURRENCY.format(currentStudent.getWalletBalance());
      JOptionPane.showMessageDialog(this, message, "Top Up Success",
                                    JOptionPane.INFORMATION_MESSAGE);

      if (navigationHandler != null) {
        navigationHandler.navigate(Navigation.STUDENT_PAGE);
      }
    } catch (IllegalArgumentException ex) {
      JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Top Up Error",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }

  private String validateTopUpAmount(double amount) {
    if (amount < MIN_TOP_UP || amount > MAX_TOP_UP) {
      return "Amount must be between PHP " + CURRENCY.format(MIN_TOP_UP)
             + " and PHP " + CURRENCY.format(MAX_TOP_UP) + ".";
    }
    return null;
  }

  private String buildReference(TopUpProviderRegistry.TopUpProvider provider) {
    String prefix = provider.getKey().toUpperCase();
    long stamp = System.currentTimeMillis();
    return prefix + "-" + stamp;
  }

  private JLabel providerLogo(TopUpProviderRegistry.TopUpProvider provider) {
    JLabel logo = new JLabel();
    logo.setPreferredSize(new Dimension(0, 86));
    logo.setMinimumSize(new Dimension(0, 86));
    logo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 86));
    logo.setOpaque(true);
    logo.setBackground(UITheme.SUMMARY_CARD_BACKGROUND);
    logo.setBorder(UITheme.roundedBorder(UITheme.BORDER, 1, UITheme.RADIUS_MD));
    logo.setHorizontalAlignment(JLabel.CENTER);
    logo.setVerticalAlignment(JLabel.CENTER);

    ImageIcon icon = resolveProviderIcon(provider.getKey(), 72, 72);
    if (icon != null) {
      logo.setIcon(icon);
      logo.setText("");
    } else {
      logo.setText(provider.getDisplayName().substring(0, 1).toUpperCase());
      logo.setForeground(UITheme.DARK_TEXT);
      logo.setFont(UITheme.customFont(UITheme.FONT_FAMILY, UITheme.FONT_WEIGHT_TITLE, 22));
    }
    return logo;
  }

  private ImageIcon resolveProviderIcon(String providerKey, int width, int height) {
    String[] candidates = new String[] {
      providerKey + ".png",
      providerKey + ".jpg",
      providerKey + ".jpeg",
      "image.png",
      "icon.png"
    };
    for (String fileName : candidates) {
      ImageIcon icon = tryLoadIcon(fileName, width, height);
      if (icon != null) {
        return icon;
      }
    }
    return null;
  }

  private ImageIcon tryLoadIcon(String fileName, int width, int height) {
    java.net.URL resource = getClass().getResource("/assets/" + fileName);
    if (resource != null) {
      return scaledIcon(new ImageIcon(resource), width, height);
    }

    Path path = Paths.get("src", "main", "resources", "assets", fileName);
    if (Files.exists(path)) {
      return scaledIcon(new ImageIcon(path.toAbsolutePath().toString()), width, height);
    }

    return null;
  }

  private ImageIcon scaledIcon(ImageIcon icon, int width, int height) {
    if (icon.getIconWidth() <= 0 || icon.getIconHeight() <= 0) {
      return null;
    }
    Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    return new ImageIcon(image);
  }

  private double clampQuickAmount(double value) {
    return Math.max(0, Math.min(MAX_TOP_UP, value));
  }

  private void syncAmountLabel(JLabel amountValue, double amount) {
    amountValue.setText("PHP " + CURRENCY.format(amount));
  }

  @Override
  public void refresh() {
    Student currentStudent = appServices.getCurrentStudent();
    double walletBalance = currentStudent == null ? 0 : currentStudent.getWalletBalance();
    walletBalanceValue.setText("Wallet Balance: PHP " + CURRENCY.format(walletBalance));
  }
}

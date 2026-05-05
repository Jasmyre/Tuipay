package com.tuipay.pages.components;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import com.tuipay.pages.Navigation;
import com.tuipay.pages.NavigationHandler;

public class Header extends JPanel {
  private final JLabel titleLabel = new JLabel();
  private final JLabel userLabel = new JLabel();
  private final JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 12));
  private final NavigationHandler handler;
  private String currentUser = "";

  public Header(String title, String user, NavigationHandler handler) {
    this.handler = handler;
    setLayout(new BorderLayout());
    setBackground(UITheme.HEADER_BACKGROUND);
    setBorder(new MatteBorder(0, 0, 1, 0, UITheme.BORDER));

    JPanel left = new JPanel();
    left.setOpaque(false);
    left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
    left.setBorder(new EmptyBorder(12, 16, 12, 16));

    titleLabel.setFont(UITheme.TITLE_FONT);
    UITheme.themeLabel(titleLabel);
    setTitle(title);

    userLabel.setFont(UITheme.SUBTITLE_FONT);
    UITheme.themeLabel(userLabel);
    setUser(user);

    left.add(titleLabel);
    left.add(Box.createVerticalStrut(2));
    left.add(userLabel);

    JPanel leftWrapper = new JPanel(new GridBagLayout());
    leftWrapper.setOpaque(false);
    leftWrapper.add(left, new GridBagConstraints());

    right.setOpaque(false);
    right.setBorder(new EmptyBorder(0, 0, 0, 12));
    rebuildNavigation();

    add(leftWrapper, BorderLayout.WEST);
    add(right, BorderLayout.EAST);
  }

  public void setTitle(String title) { titleLabel.setText(title); }

  public void setUser(String user) {
    if (user == null || user.trim().isEmpty()) {
      currentUser = "";
      userLabel.setText("");
      userLabel.setVisible(false);
      return;
    }
    currentUser = user.trim();
    userLabel.setText("User: " + currentUser);
    userLabel.setVisible(true);
    rebuildNavigation();
  }

  public String getUser() { return currentUser; }

  private void rebuildNavigation() {
    right.removeAll();

    right.add(navButton("Student Page", Navigation.STUDENT_PAGE, handler));
    right.add(navButton("Top Up Wallet", Navigation.TOP_UP, handler));
    right.add(navButton("Pay Tuition", Navigation.PAY_TUITION, handler));
    right.add(navButton("Transaction History", Navigation.TRANSACTION_HISTORY, handler));

    right.add(modeToggleButton(handler));
    right.add(logoutButton(handler));
    right.revalidate();
    right.repaint();
  }

  private JButton navButton(String label, String destination,
                            NavigationHandler handler) {
    JButton button = UITheme.secondaryButton(label);
    button.setFont(new Font("SansSerif", Font.PLAIN, 14));
    button.setMargin(new Insets(10, 14, 10, 14));
    button.addActionListener(e -> handler.navigate(destination));
    return button;
  }

  private JButton logoutButton(NavigationHandler handler) {
    JButton button = UITheme.secondaryButton("Logout");
    button.setFont(new Font("SansSerif", Font.PLAIN, 14));
    button.setMargin(new Insets(10, 14, 10, 14));
    button.addActionListener(e -> handler.logout());
    return button;
  }

  private JButton modeToggleButton(NavigationHandler handler) {
    JButton button = UITheme.secondaryButton(modeGlyph());
    button.setFont(UITheme.customFont(UITheme.FONT_FAMILY, Font.BOLD, 16));
    button.setPreferredSize(new Dimension(44, 44));
    button.setMinimumSize(new Dimension(44, 44));
    button.setMaximumSize(new Dimension(44, 44));
    button.setMargin(new Insets(0, 0, 0, 0));
    button.setToolTipText("Toggle theme");

    JPopupMenu menu = new JPopupMenu();
    styleThemeMenu(menu);

    JMenuItem light = themeItem("Light", UITheme.ThemeMode.LIGHT, handler);
    JMenuItem dark = themeItem("Dark", UITheme.ThemeMode.DARK, handler);
    JMenuItem bubblegum =
        themeItem("Bubblegum", UITheme.ThemeMode.BUBBLEGUM, handler);
    menu.add(light);
    menu.add(dark);
    menu.add(bubblegum);

    button.addActionListener(e -> menu.show(button, 0, button.getHeight()));
    return button;
  }

  private JMenuItem themeItem(String label, UITheme.ThemeMode mode,
                              NavigationHandler handler) {
    String activePrefix = UITheme.getThemeMode() == mode ? "* " : "";
    JMenuItem item = new JMenuItem(activePrefix + label);
    item.setFont(UITheme.LABEL_FONT);
    item.setBackground(UITheme.CARD_BACKGROUND);
    item.setForeground(UITheme.DARK_TEXT);
    item.setOpaque(true);
    item.setBorder(new EmptyBorder(8, 12, 8, 12));
    item.addActionListener(e -> handler.changeTheme(mode));
    return item;
  }

  private void styleThemeMenu(JPopupMenu menu) {
    menu.setBackground(UITheme.CARD_BACKGROUND);
    menu.setBorder(UITheme.roundedBorder(UITheme.BORDER, 1, UITheme.RADIUS_MD));
  }

  private String modeGlyph() {
    UITheme.ThemeMode mode = UITheme.getThemeMode();
    if (mode == UITheme.ThemeMode.DARK) {
      return "D";
    }
    if (mode == UITheme.ThemeMode.BUBBLEGUM) {
      return "B";
    }
    return "L";
  }
}

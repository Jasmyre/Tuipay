package com.store_inventory.pages;

import com.store_inventory.models.Admin;
import com.store_inventory.pages.components.UITheme;
import com.store_inventory.services.AppServices;
import java.awt.Component;
import java.awt.GridBagLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AdminPage extends JPanel implements Refreshable {
  private final AppServices appServices;
  private final JLabel adminIdValue = new JLabel("-");
  private final JLabel totalStudentsValue = new JLabel("0");
  private final JLabel totalTransactionsValue = new JLabel("0");

  public AdminPage(AppServices appServices) {
    this.appServices = appServices;
    setLayout(new GridBagLayout());
    setBackground(UITheme.BACKGROUND);

    JPanel card = UITheme.cardPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

    JLabel title = new JLabel("Admin Dashboard");
    title.setFont(UITheme.TITLE_FONT);
    title.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(title);

    JLabel subtitle = new JLabel("Early prototype overview");
    subtitle.setFont(UITheme.SUBTITLE_FONT);
    subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
    subtitle.setForeground(UITheme.MUTED_TEXT);

    styleValue(adminIdValue);
    styleValue(totalStudentsValue);
    styleValue(totalTransactionsValue);

    card.add(title);
    card.add(Box.createVerticalStrut(6));
    card.add(subtitle);
    card.add(Box.createVerticalStrut(12));
    card.add(adminIdValue);
    card.add(Box.createVerticalStrut(6));
    card.add(totalStudentsValue);
    card.add(Box.createVerticalStrut(6));
    card.add(totalTransactionsValue);
    add(card);

    refresh();
  }

  private void styleValue(JLabel label) {
    label.setFont(UITheme.customFont(UITheme.FONT_FAMILY,
                                     UITheme.FONT_WEIGHT_LABEL, 18));
    label.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(label);
  }

  @Override
  public void refresh() {
    Admin currentAdmin = appServices.getCurrentAdmin();
    String adminId = currentAdmin == null ? "-" : currentAdmin.getAdminId();
    int totalStudents = appServices.getAdminManager().getAllStudents().size();
    int totalTransactions = appServices.getAdminManager().getAllTransactions().size();

    adminIdValue.setText("Admin ID: " + adminId);
    totalStudentsValue.setText("Total Students: " + totalStudents);
    totalTransactionsValue.setText("Total Transactions: " + totalTransactions);
  }
}

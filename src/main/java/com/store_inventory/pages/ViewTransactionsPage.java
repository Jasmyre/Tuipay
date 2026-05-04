package com.store_inventory.pages;

import com.store_inventory.models.Transaction;
import com.store_inventory.pages.components.UITheme;
import com.store_inventory.services.AppManagerService;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class ViewTransactionsPage extends JPanel implements Refreshable {
  private final AppManagerService appManagerService;
  private final JLabel totalTransactionsValue = new JLabel("0");

  public ViewTransactionsPage(AppManagerService appManagerService) {
    this.appManagerService = appManagerService;
    setLayout(new GridBagLayout());
    setBackground(UITheme.BACKGROUND);

    JPanel card = UITheme.cardPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

    JLabel title = new JLabel("View Transactions");
    title.setFont(UITheme.TITLE_FONT);
    title.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(title);

    JLabel subtitle = new JLabel("All recorded transactions from storage");
    subtitle.setFont(UITheme.SUBTITLE_FONT);
    subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(subtitle);

    totalTransactionsValue.setFont(UITheme.customFont(
        UITheme.FONT_FAMILY, UITheme.FONT_WEIGHT_LABEL, 20));
    totalTransactionsValue.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(totalTransactionsValue);

    card.add(title);
    card.add(Box.createVerticalStrut(6));
    card.add(subtitle);
    card.add(Box.createVerticalStrut(10));
    card.add(totalTransactionsValue);
    add(card);

    refresh();
  }

  @Override
  public void refresh() {
    List<Transaction> transactions = appManagerService.getTransactionManager()
                                     .getAllTransactions(appManagerService.getStudentManager()
                                                             .getAllStudents());
    totalTransactionsValue.setText("Total Transactions: " + transactions.size());
  }
}

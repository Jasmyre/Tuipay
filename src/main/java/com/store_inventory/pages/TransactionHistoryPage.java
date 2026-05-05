package com.store_inventory.pages;

import com.store_inventory.models.Student;
import com.store_inventory.models.Transaction;
import com.store_inventory.pages.components.UITheme;
import com.store_inventory.services.AppServices;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.*;

public class TransactionHistoryPage extends JPanel implements Refreshable {
  private static final DecimalFormat CURRENCY = new DecimalFormat("#,##0.00");
  private static final SimpleDateFormat DATE_FORMAT =
      new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);

  private final AppServices appServices;
  private final JLabel totalHistoryValue = new JLabel("0");
  private final JLabel lastUpdatedValue = new JLabel("-");
  private final JPanel rowsPanel = new JPanel();

  public TransactionHistoryPage(AppServices appServices) {
    this.appServices = appServices;
    setLayout(new BorderLayout());
    setBackground(UITheme.BACKGROUND);

    JPanel content = new JPanel();
    content.setOpaque(false);
    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
    content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JPanel headerCard = UITheme.cardPanel();
    headerCard.setLayout(new BoxLayout(headerCard, BoxLayout.Y_AXIS));
    headerCard.setAlignmentX(Component.LEFT_ALIGNMENT);
    JLabel title = new JLabel("Transaction History");
    title.setFont(UITheme.TITLE_FONT);
    title.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(title);

    JLabel subtitle = new JLabel("Compact transaction records");
    subtitle.setFont(UITheme.SUBTITLE_FONT);
    subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
    subtitle.setForeground(UITheme.MUTED_TEXT);

    JPanel summaryRow = new JPanel(new GridLayout(1, 2, 12, 0));
    summaryRow.setOpaque(false);
    summaryRow.setAlignmentX(Component.LEFT_ALIGNMENT);

    totalHistoryValue.setFont(
        UITheme.customFont(UITheme.FONT_FAMILY, UITheme.FONT_WEIGHT_TITLE, 24));
    totalHistoryValue.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(totalHistoryValue);
    JPanel totalCard = metricCard(totalHistoryValue, "Total Records");

    lastUpdatedValue.setFont(
        UITheme.customFont(UITheme.FONT_FAMILY, UITheme.FONT_WEIGHT_LABEL, 16));
    lastUpdatedValue.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(lastUpdatedValue);
    JPanel updatedCard = metricCard(lastUpdatedValue, "Last Updated");

    summaryRow.add(totalCard);
    summaryRow.add(updatedCard);

    headerCard.add(title);
    headerCard.add(Box.createVerticalStrut(4));
    headerCard.add(subtitle);
    headerCard.add(Box.createVerticalStrut(12));
    headerCard.add(summaryRow);

    JPanel listCard = UITheme.cardPanel();
    listCard.setLayout(new BoxLayout(listCard, BoxLayout.Y_AXIS));
    listCard.setAlignmentX(Component.LEFT_ALIGNMENT);

    JLabel listTitle = new JLabel("Rows");
    listTitle.setFont(UITheme.SUBTITLE_FONT);
    listTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(listTitle);

    JPanel columnsRow = new JPanel(new GridLayout(1, 4, 10, 0));
    columnsRow.setOpaque(false);
    columnsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
    columnsRow.add(columnLabel("Date"));
    columnsRow.add(columnLabel("Type"));
    columnsRow.add(columnLabel("Amount"));
    columnsRow.add(columnLabel("Transaction ID"));

    rowsPanel.setOpaque(false);
    rowsPanel.setLayout(new BoxLayout(rowsPanel, BoxLayout.Y_AXIS));
    rowsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

    JScrollPane scrollPane = new JScrollPane(rowsPanel);
    scrollPane.setBorder(null);
    scrollPane.setOpaque(false);
    scrollPane.getViewport().setOpaque(false);
    scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    UITheme.themeScrollPane(scrollPane);
    scrollPane.setPreferredSize(new Dimension(760, 320));
    scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 420));

    listCard.add(listTitle);
    listCard.add(Box.createVerticalStrut(8));
    listCard.add(columnsRow);
    listCard.add(Box.createVerticalStrut(8));
    listCard.add(scrollPane);

    content.add(headerCard);
    content.add(Box.createVerticalStrut(12));
    content.add(listCard);

    add(content, BorderLayout.CENTER);

    refresh();
  }

  private JPanel metricCard(JLabel valueLabel, String labelText) {
    JPanel card = UITheme.cardPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
    card.setAlignmentX(Component.LEFT_ALIGNMENT);
    card.add(valueLabel);
    card.add(Box.createVerticalStrut(4));
    JLabel label = new JLabel(labelText);
    label.setFont(UITheme.LABEL_FONT);
    label.setForeground(UITheme.MUTED_TEXT);
    label.setAlignmentX(Component.LEFT_ALIGNMENT);
    card.add(label);
    return card;
  }

  private JLabel columnLabel(String text) {
    JLabel label = new JLabel(text);
    label.setFont(UITheme.customFont(UITheme.FONT_FAMILY, UITheme.FONT_WEIGHT_LABEL, 13));
    label.setForeground(UITheme.MUTED_TEXT);
    return label;
  }

  private JPanel historyRow(Transaction transaction) {
    JPanel card = UITheme.cardPanel();
    card.setLayout(new GridLayout(1, 4, 10, 0));
    card.setAlignmentX(Component.LEFT_ALIGNMENT);
    card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));

    JLabel date = new JLabel(formatDate(transaction.getTransactionDate()));
    date.setFont(UITheme.LABEL_FONT);
    UITheme.themeLabel(date);

    JLabel type = new JLabel(String.valueOf(transaction.getType()));
    type.setFont(UITheme.LABEL_FONT);
    UITheme.themeLabel(type);

    JLabel amount = new JLabel("PHP " + CURRENCY.format(transaction.getAmount()));
    amount.setFont(UITheme.LABEL_FONT);
    UITheme.themeLabel(amount);

    JLabel id = new JLabel(safe(transaction.getTransactionId()));
    id.setFont(UITheme.customFont(UITheme.FONT_FAMILY, UITheme.FONT_WEIGHT_LABEL, 12));
    id.setForeground(UITheme.MUTED_TEXT);

    card.add(date);
    card.add(type);
    card.add(amount);
    card.add(id);
    return card;
  }

  private void renderHistory(List<Transaction> history) {
    rowsPanel.removeAll();
    if (history == null || history.isEmpty()) {
      JPanel emptyCard = UITheme.cardPanel();
      emptyCard.setLayout(new BorderLayout());
      emptyCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
      JLabel empty = new JLabel("No transactions yet.");
      empty.setFont(UITheme.LABEL_FONT);
      empty.setForeground(UITheme.MUTED_TEXT);
      emptyCard.add(empty, BorderLayout.WEST);
      rowsPanel.add(emptyCard);
      rowsPanel.revalidate();
      rowsPanel.repaint();
      return;
    }

    for (int i = history.size() - 1; i >= 0; i--) {
      rowsPanel.add(historyRow(history.get(i)));
      if (i > 0) {
        rowsPanel.add(Box.createVerticalStrut(8));
      }
    }
    rowsPanel.revalidate();
    rowsPanel.repaint();
  }

  private String formatDate(Date date) {
    Date safeDate = date == null ? new Date() : date;
    return DATE_FORMAT.format(safeDate);
  }

  private String safe(String value) {
    if (value == null || value.isBlank()) {
      return "-";
    }
    return value;
  }

  @Override
  public void refresh() {
    Student currentStudent = appServices.getCurrentStudent();
    List<Transaction> history = currentStudent == null
        ? List.of()
        : appServices.getStudentManager().getPaymentHistory(currentStudent);
    totalHistoryValue.setText(String.valueOf(history.size()));
    lastUpdatedValue.setText(DATE_FORMAT.format(new Date()));
    renderHistory(history);
  }
}


package com.tuipay.pages;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.tuipay.models.Student;
import com.tuipay.models.Transaction;
import com.tuipay.pages.components.UITheme;
import com.tuipay.services.AppServices;

public class StudentPage extends JPanel implements Refreshable {
  private static final DecimalFormat CURRENCY = new DecimalFormat("#,##0.00");
  private static final SimpleDateFormat DATE_FORMAT =
      new SimpleDateFormat("MMM dd, yyyy hh:mm a");
  private static final int HISTORY_PREVIEW_LIMIT = 5;

  private final NavigationHandler navigationHandler;
  private final AppServices appServices;

  private final JLabel fullNameValue = new JLabel("-");
  private final JLabel studentIdValue = new JLabel("-");
  private final JLabel usernameValue = new JLabel("-");
  private final JLabel tuitionBalanceValue = new JLabel("PHP 0.00");
  private final JLabel walletBalanceValue = new JLabel("PHP 0.00");
  private final JLabel transactionCountValue = new JLabel("0");
  private final JPanel historyList = new JPanel();

  public StudentPage(NavigationHandler navigationHandler, AppServices appServices) {
    this.navigationHandler = navigationHandler;
    this.appServices = appServices;

    setLayout(new BorderLayout());
    setBackground(UITheme.BACKGROUND);

    JPanel content = new JPanel();
    content.setOpaque(false);
    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
    content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    content.add(buildProfileCard());
    content.add(Box.createVerticalStrut(12));
    content.add(buildSummaryRow());
    content.add(Box.createVerticalStrut(12));
    content.add(buildBottomRow());

    JScrollPane scrollPane = new JScrollPane(content);
    scrollPane.setBorder(null);
    scrollPane.setOpaque(false);
    scrollPane.getViewport().setOpaque(false);
    scrollPane.setHorizontalScrollBarPolicy(
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    UITheme.themeScrollPane(scrollPane);

    add(scrollPane, BorderLayout.CENTER);
    refresh();
  }

  private JPanel buildProfileCard() {
    JPanel card = UITheme.cardPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

    JLabel title = new JLabel("Student Page");
    title.setFont(UITheme.TITLE_FONT);
    title.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(title);

    JLabel subtitle = new JLabel("Your account details");
    subtitle.setFont(UITheme.SUBTITLE_FONT);
    subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
    subtitle.setForeground(UITheme.MUTED_TEXT);

    styleInfoLabel(fullNameValue, "Full Name");
    styleInfoLabel(studentIdValue, "Student ID");
    styleInfoLabel(usernameValue, "Username");

    card.add(title);
    card.add(Box.createVerticalStrut(4));
    card.add(subtitle);
    card.add(Box.createVerticalStrut(12));
    card.add(fullNameValue);
    card.add(Box.createVerticalStrut(6));
    card.add(studentIdValue);
    card.add(Box.createVerticalStrut(6));
    card.add(usernameValue);
    return card;
  }

  private JPanel buildSummaryRow() {
    JPanel row = new JPanel(new GridLayout(1, 3, 12, 0));
    row.setOpaque(false);
    row.setAlignmentX(Component.LEFT_ALIGNMENT);

    tuitionBalanceValue.setFont(UITheme.TITLE_FONT);
    walletBalanceValue.setFont(UITheme.TITLE_FONT);
    transactionCountValue.setFont(UITheme.TITLE_FONT);
    UITheme.themeLabel(tuitionBalanceValue);
    UITheme.themeLabel(walletBalanceValue);
    UITheme.themeLabel(transactionCountValue);

    row.add(UITheme.statCard("Tuition Balance", tuitionBalanceValue));
    row.add(UITheme.statCard("Wallet Balance", walletBalanceValue));
    row.add(UITheme.statCard("Transactions", transactionCountValue));
    return row;
  }

  private JPanel buildBottomRow() {
    JPanel row = new JPanel(new GridLayout(1, 2, 12, 0));
    row.setOpaque(false);
    row.setAlignmentX(Component.LEFT_ALIGNMENT);

    row.add(buildHistoryCard());
    row.add(buildActionsCard());
    return row;
  }

  private JPanel buildHistoryCard() {
    JPanel card = UITheme.cardPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

    JLabel title = new JLabel("Transaction History");
    title.setFont(UITheme.SUBTITLE_FONT);
    title.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(title);

    JLabel subtitle = new JLabel("Latest activity");
    subtitle.setFont(UITheme.LABEL_FONT);
    subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
    subtitle.setForeground(UITheme.MUTED_TEXT);

    historyList.setOpaque(false);
    historyList.setLayout(new BoxLayout(historyList, BoxLayout.Y_AXIS));
    historyList.setAlignmentX(Component.LEFT_ALIGNMENT);

    card.add(title);
    card.add(Box.createVerticalStrut(2));
    card.add(subtitle);
    card.add(Box.createVerticalStrut(10));
    card.add(historyList);
    return card;
  }

  private JPanel buildActionsCard() {
    JPanel card = UITheme.cardPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

    JLabel title = new JLabel("Actions");
    title.setFont(UITheme.SUBTITLE_FONT);
    title.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(title);

    JButton topUpButton = actionButton("Top Up Wallet", Navigation.TOP_UP);
    JButton payTuitionButton = actionButton("Pay Tuition", Navigation.PAY_TUITION);

    card.add(title);
    card.add(Box.createVerticalStrut(10));
    card.add(topUpButton);
    card.add(Box.createVerticalStrut(10));
    card.add(payTuitionButton);
    card.add(Box.createVerticalGlue());
    return card;
  }

  private JButton actionButton(String label, String destination) {
    JButton button = UITheme.primaryButton(label);
    button.setAlignmentX(Component.LEFT_ALIGNMENT);
    button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    button.addActionListener(e -> navigationHandler.navigate(destination));
    return button;
  }

  private void styleInfoLabel(JLabel label, String prefix) {
    label.setFont(UITheme.LABEL_FONT);
    label.setAlignmentX(Component.LEFT_ALIGNMENT);
    label.setText(prefix + ": -");
    UITheme.themeLabel(label);
  }

  @Override
  public void refresh() {
    Student currentStudent = appServices.getCurrentStudent();
    if (currentStudent == null) {
      fullNameValue.setText("Full Name: -");
      studentIdValue.setText("Student ID: -");
      usernameValue.setText("Username: -");
      tuitionBalanceValue.setText("PHP 0.00");
      walletBalanceValue.setText("PHP 0.00");
      transactionCountValue.setText("0");
      renderHistory(null);
      return;
    }

    fullNameValue.setText("Full Name: " + safe(currentStudent.getFullName()));
    studentIdValue.setText("Student ID: " + safe(currentStudent.getStudentId()));
    usernameValue.setText("Username: " + safe(currentStudent.getUsername()));
    tuitionBalanceValue.setText("PHP " + CURRENCY.format(currentStudent.getTuitionBalance()));
    walletBalanceValue.setText("PHP " + CURRENCY.format(currentStudent.getWalletBalance()));

    List<Transaction> history =
        appServices.getStudentManager().getPaymentHistory(currentStudent);
    transactionCountValue.setText(String.valueOf(history.size()));
    renderHistory(history);
  }

  private void renderHistory(List<Transaction> history) {
    historyList.removeAll();

    if (history == null || history.isEmpty()) {
      JLabel empty = new JLabel("No transactions yet.");
      empty.setFont(UITheme.LABEL_FONT);
      empty.setForeground(UITheme.MUTED_TEXT);
      empty.setAlignmentX(Component.LEFT_ALIGNMENT);
      historyList.add(empty);
      historyList.revalidate();
      historyList.repaint();
      return;
    }

    int shown = 0;
    for (int i = history.size() - 1; i >= 0 && shown < HISTORY_PREVIEW_LIMIT; i--) {
      Transaction transaction = history.get(i);
      historyList.add(historyRow(transaction));
      if (i > 0 && shown < HISTORY_PREVIEW_LIMIT - 1) {
        historyList.add(Box.createVerticalStrut(8));
      }
      shown++;
    }

    historyList.revalidate();
    historyList.repaint();
  }

  private JPanel historyRow(Transaction transaction) {
    JPanel row = new JPanel(new BorderLayout(8, 2));
    row.setOpaque(false);
    row.setAlignmentX(Component.LEFT_ALIGNMENT);

    String amount = "PHP " + CURRENCY.format(transaction.getAmount());
    JLabel left = new JLabel(transaction.getType() + " - " + amount);
    left.setFont(UITheme.LABEL_FONT);
    UITheme.themeLabel(left);

    Date transactionDate = transaction.getTransactionDate();
    JLabel right =
        new JLabel(DATE_FORMAT.format(transactionDate == null ? new Date() : transactionDate));
    right.setFont(UITheme.customFont(UITheme.FONT_FAMILY, UITheme.FONT_WEIGHT_LABEL, 13));
    right.setForeground(UITheme.MUTED_TEXT);

    row.add(left, BorderLayout.WEST);
    row.add(right, BorderLayout.EAST);
    return row;
  }

  private String safe(String value) {
    return value == null || value.isBlank() ? "-" : value;
  }
}

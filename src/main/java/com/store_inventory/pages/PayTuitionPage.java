package com.store_inventory.pages;

import com.store_inventory.models.Student;
import com.store_inventory.models.Transaction;
import com.store_inventory.pages.components.UITheme;
import com.store_inventory.services.AppServices;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

public class PayTuitionPage extends JPanel implements Refreshable {
  private static final DecimalFormat CURRENCY = new DecimalFormat("#,##0.00");
  private static final SimpleDateFormat RECEIPT_DATE =
      new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
  private static final SimpleDateFormat FILE_DATE =
      new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.ENGLISH);

  private final AppServices appServices;
  private final NavigationHandler navigationHandler;
  private final JLabel tuitionBalanceValue = new JLabel("PHP 0.00");
  private final JLabel walletBalanceValue = new JLabel("PHP 0.00");
  private final JLabel maxPayableValue = new JLabel("PHP 0.00");

  public PayTuitionPage(AppServices appServices,
                        NavigationHandler navigationHandler) {
    this.appServices = appServices;
    this.navigationHandler = navigationHandler;
    setLayout(new BorderLayout());
    setBackground(UITheme.BACKGROUND);

    JPanel content = new JPanel();
    content.setOpaque(false);
    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
    content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JPanel headerPanel = buildHeaderPanel();
    headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    JPanel balanceContainer = buildBalanceContainer();
    balanceContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
    JPanel actionPanel = buildActionPanel();
    actionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

    content.add(headerPanel);
    content.add(Box.createVerticalStrut(16));
    content.add(balanceContainer);
    content.add(Box.createVerticalStrut(16));
    content.add(actionPanel);
    content.add(Box.createVerticalGlue());

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

  private JPanel buildHeaderPanel() {
    JPanel header = new JPanel();
    header.setOpaque(false);
    header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

    JLabel title = new JLabel("Pay Tuition");
    title.setFont(UITheme.TITLE_FONT);
    title.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(title);

    JLabel subtitle = new JLabel("Review balances before payment");
    subtitle.setFont(UITheme.SUBTITLE_FONT);
    subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
    subtitle.setForeground(UITheme.MUTED_TEXT);

    header.add(title);
    header.add(Box.createVerticalStrut(4));
    header.add(subtitle);
    return header;
  }

  private JPanel buildBalanceContainer() {
    JPanel container = new JPanel(new GridLayout(1, 3, 20, 0));
    container.setOpaque(false);

    styleSummaryValue(tuitionBalanceValue);
    styleSummaryValue(walletBalanceValue);
    styleSummaryValue(maxPayableValue);

    container.add(buildBalanceCard(tuitionBalanceValue, "Tuition Balance"));
    container.add(buildBalanceCard(walletBalanceValue, "Wallet Balance"));
    container.add(buildBalanceCard(maxPayableValue, "Amount To Pay"));
    return container;
  }

  private JPanel buildBalanceCard(JLabel valueLabel, String caption) {
    JPanel card = UITheme.cardPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
    valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

    JLabel small = new JLabel(caption);
    small.setFont(UITheme.LABEL_FONT);
    small.setAlignmentX(Component.LEFT_ALIGNMENT);
    small.setForeground(UITheme.MUTED_TEXT);

    card.add(valueLabel);
    card.add(Box.createVerticalStrut(6));
    card.add(small);
    return card;
  }

  private JPanel buildActionPanel() {
    JPanel actionPanel = new JPanel(new GridLayout(1, 2, 20, 0));
    actionPanel.setOpaque(false);

    JButton backButton = UITheme.secondaryButton("Back to Dashboard");
    JButton proceedButton = UITheme.primaryButton("Proceed to Payment");

    backButton.addActionListener(e -> {
      if (navigationHandler != null) {
        navigationHandler.navigate(Navigation.STUDENT_PAGE);
      }
    });
    proceedButton.addActionListener(e -> openPaymentDialog());

    actionPanel.add(backButton);
    actionPanel.add(proceedButton);
    return actionPanel;
  }

  private void styleSummaryValue(JLabel label) {
    label.setFont(UITheme.TITLE_FONT);
    label.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(label);
  }

  private void processPayment(double amount) {
    Student student = appServices.getCurrentStudent();
    if (student == null) {
      JOptionPane.showMessageDialog(this,
                                    "No active student session found.",
                                    "Payment Error",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    }

    if (amount <= 0) {
      JOptionPane.showMessageDialog(this,
                                    "Enter a valid amount greater than zero.",
                                    "Payment Error",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    }

    double maxPayable = Math.min(student.getTuitionBalance(),
                                 student.getWalletBalance());
    if (maxPayable <= 0) {
      JOptionPane.showMessageDialog(this,
                                    "No payable amount. Check tuition and wallet balance.",
                                    "Payment Error",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    }

    if (amount > maxPayable) {
      JOptionPane.showMessageDialog(this,
                                    "Amount exceeds your payable balance (PHP "
                                    + CURRENCY.format(maxPayable) + ").",
                                    "Payment Error",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    }

    boolean success = appServices.getStudentManager().payTuition(student, amount);
    if (!success) {
      JOptionPane.showMessageDialog(this,
                                    "Unable to process tuition payment.",
                                    "Payment Error",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    }

    refresh();
    Transaction transaction = latestPaymentTransaction(student);
    showReceiptDialog(student, transaction, amount);
  }

  private void openPaymentDialog() {
    double maxPayable = getCurrentMaxPayable();
    if (maxPayable <= 0) {
      JOptionPane.showMessageDialog(this,
                                    "No payable amount. Check tuition and wallet balance.",
                                    "Payment Error",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    }

    JDialog dialog = new JDialog(
        SwingUtilities.getWindowAncestor(this),
        "Proceed to Payment",
        Dialog.ModalityType.APPLICATION_MODAL);
    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

    JPanel root = new JPanel(new BorderLayout());
    root.setBackground(UITheme.BACKGROUND);
    root.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

    JPanel card = UITheme.cardPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
    card.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

    JLabel title = new JLabel("Tuition Payment Checkout");
    title.setFont(UITheme.SUBTITLE_FONT);
    title.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(title);

    JLabel subtitle = new JLabel("Set amount and confirm tuition payment");
    subtitle.setFont(UITheme.customFont(UITheme.FONT_FAMILY,
                                        UITheme.FONT_WEIGHT_LABEL, 13));
    subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
    subtitle.setForeground(UITheme.MUTED_TEXT);

    final double[] amount = new double[] {maxPayable};
    JLabel amountLabel = new JLabel("Amount PHP");
    amountLabel.setFont(UITheme.LABEL_FONT);
    amountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(amountLabel);

    JLabel amountValue = new JLabel("PHP " + CURRENCY.format(amount[0]));
    amountValue.setFont(UITheme.customFont(
        UITheme.FONT_FAMILY, UITheme.FONT_WEIGHT_TITLE, 30));
    amountValue.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(amountValue);

    JLabel maxHint = new JLabel("Maximum payable: PHP " + CURRENCY.format(maxPayable));
    maxHint.setFont(UITheme.LABEL_FONT);
    maxHint.setAlignmentX(Component.LEFT_ALIGNMENT);
    maxHint.setForeground(UITheme.MUTED_TEXT);

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
      amount[0] = clampAmount(amount[0] - 1, maxPayable);
      syncAmountLabel(amountValue, amount[0]);
    });
    minusHundredButton.addActionListener(e -> {
      amount[0] = clampAmount(amount[0] - 100, maxPayable);
      syncAmountLabel(amountValue, amount[0]);
    });
    resetButton.addActionListener(e -> {
      amount[0] = 0;
      syncAmountLabel(amountValue, amount[0]);
    });
    plusHundredButton.addActionListener(e -> {
      amount[0] = clampAmount(amount[0] + 100, maxPayable);
      syncAmountLabel(amountValue, amount[0]);
    });
    plusOneButton.addActionListener(e -> {
      amount[0] = clampAmount(amount[0] + 1, maxPayable);
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
    JButton submitButton = UITheme.primaryButton("Pay Tuition Now");
    cancelButton.addActionListener(e -> dialog.dispose());
    submitButton.addActionListener(e -> {
      processPayment(amount[0]);
      dialog.dispose();
    });

    actions.add(cancelButton);
    actions.add(submitButton);

    card.add(title);
    card.add(Box.createVerticalStrut(4));
    card.add(subtitle);
    card.add(Box.createVerticalStrut(16));
    card.add(amountLabel);
    card.add(Box.createVerticalStrut(8));
    card.add(amountValue);
    card.add(Box.createVerticalStrut(4));
    card.add(maxHint);
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

  private Transaction latestPaymentTransaction(Student student) {
    List<Transaction> history =
        appServices.getStudentManager().getPaymentHistory(student);
    if (history.isEmpty()) {
      return null;
    }
    return history.get(history.size() - 1);
  }

  private void showReceiptDialog(Student student, Transaction transaction,
                                 double paidAmount) {
    JDialog dialog = new JDialog(
        SwingUtilities.getWindowAncestor(this),
        "Payment Receipt",
        Dialog.ModalityType.APPLICATION_MODAL);
    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

    JPanel root = new JPanel(new BorderLayout());
    root.setBackground(UITheme.BACKGROUND);
    root.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

    JPanel card = UITheme.cardPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
    card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

    JLabel title = new JLabel("Receipt Preview");
    title.setFont(UITheme.SUBTITLE_FONT);
    title.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(title);

    String receiptBody = buildReceiptContent(student, transaction, paidAmount);
    JTextArea receiptArea = new JTextArea(receiptBody);
    receiptArea.setEditable(false);
    receiptArea.setLineWrap(true);
    receiptArea.setWrapStyleWord(true);
    receiptArea.setFont(UITheme.customFont(UITheme.FONT_FAMILY,
                                           UITheme.FONT_WEIGHT_LABEL, 13));
    receiptArea.setBackground(UITheme.INPUT_BACKGROUND);
    receiptArea.setForeground(UITheme.INPUT_TEXT);
    receiptArea.setCaretColor(UITheme.DARK_TEXT);
    receiptArea.setSelectionColor(UITheme.STATUS_LOW_BG);
    receiptArea.setSelectedTextColor(UITheme.DARK_TEXT);
    receiptArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JScrollPane receiptScroll = new JScrollPane(receiptArea);
    receiptScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
    receiptScroll.setPreferredSize(new Dimension(520, 220));
    receiptScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
    UITheme.themeScrollPane(receiptScroll);

    JPanel actions = new JPanel();
    actions.setOpaque(false);
    actions.setLayout(new BoxLayout(actions, BoxLayout.X_AXIS));

    JButton closeButton = UITheme.secondaryButton("Close");
    JButton saveAndBackButton = UITheme.primaryButton("Save to /export and Back");

    closeButton.addActionListener(e -> dialog.dispose());
    saveAndBackButton.addActionListener(e -> {
      try {
        Path savedFile = saveReceiptToExport(student, receiptBody);
        JOptionPane.showMessageDialog(
            dialog,
            "Receipt saved to:\n" + savedFile.toString(),
            "Saved",
            JOptionPane.INFORMATION_MESSAGE);
        dialog.dispose();
        if (navigationHandler != null) {
          navigationHandler.navigate(Navigation.STUDENT_PAGE);
        }
      } catch (IOException ex) {
        JOptionPane.showMessageDialog(dialog,
                                      "Failed to save receipt: " + ex.getMessage(),
                                      "Save Error",
                                      JOptionPane.ERROR_MESSAGE);
      }
    });

    actions.add(closeButton);
    actions.add(Box.createHorizontalStrut(8));
    actions.add(saveAndBackButton);
    actions.add(Box.createHorizontalGlue());

    card.add(title);
    card.add(Box.createVerticalStrut(8));
    card.add(receiptScroll);
    card.add(Box.createVerticalStrut(10));
    card.add(actions);

    root.add(card, BorderLayout.CENTER);
    dialog.setContentPane(root);
    dialog.pack();
    dialog.setSize(new Dimension(620, 460));
    dialog.setLocationRelativeTo(this);
    dialog.setResizable(false);
    dialog.setVisible(true);
  }

  private String buildReceiptContent(Student student, Transaction transaction,
                                     double paidAmount) {
    StringBuilder content = new StringBuilder();
    content.append("Tuipay - Tuition Payment Receipt\n");
    content.append("----------------------------------------\n");
    content.append("Student ID: ").append(safe(student.getStudentId())).append("\n");
    content.append("Student Name: ").append(safe(student.getFullName())).append("\n");
    content.append("Amount Paid: PHP ").append(CURRENCY.format(paidAmount)).append("\n");
    content.append("Remaining Tuition Balance: PHP ")
        .append(CURRENCY.format(student.getTuitionBalance())).append("\n");
    content.append("Remaining Wallet Balance: PHP ")
        .append(CURRENCY.format(student.getWalletBalance())).append("\n");
    content.append("Date: ").append(RECEIPT_DATE.format(new Date())).append("\n");
    if (transaction != null) {
      content.append("Transaction ID: ")
          .append(safe(transaction.getTransactionId())).append("\n");
    }
    return content.toString();
  }

  private Path saveReceiptToExport(Student student, String receiptBody)
      throws IOException {
    Path exportDir = Paths.get("export");
    Files.createDirectories(exportDir);
    String timestamp = FILE_DATE.format(new Date());
    String studentId = safe(student.getStudentId()).replaceAll("[^a-zA-Z0-9_-]", "_");
    Path file = exportDir.resolve(
        "tuition-receipt-" + studentId + "-" + timestamp + ".txt");
    Files.writeString(file, receiptBody, StandardCharsets.UTF_8);
    return file.toAbsolutePath();
  }

  private double clampAmount(double value, double maxPayable) {
    return Math.max(0, Math.min(maxPayable, value));
  }

  private void syncAmountLabel(JLabel amountValue, double amount) {
    amountValue.setText("PHP " + CURRENCY.format(amount));
  }

  private double getCurrentMaxPayable() {
    Student student = appServices.getCurrentStudent();
    if (student == null) {
      return 0;
    }
    return Math.max(0, Math.min(student.getTuitionBalance(),
                                student.getWalletBalance()));
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
    double tuitionBalance = currentStudent == null ? 0 : currentStudent.getTuitionBalance();
    double walletBalance = currentStudent == null ? 0 : currentStudent.getWalletBalance();
    double maxPayable = Math.max(0, Math.min(tuitionBalance, walletBalance));

    tuitionBalanceValue.setText("PHP " + CURRENCY.format(tuitionBalance));
    walletBalanceValue.setText("PHP " + CURRENCY.format(walletBalance));
    maxPayableValue.setText("PHP " + CURRENCY.format(maxPayable));
  }
}

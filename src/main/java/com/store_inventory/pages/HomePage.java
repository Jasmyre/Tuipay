package com.store_inventory.pages;

import com.store_inventory.dto.StudentDashboardDTO;
import com.store_inventory.models.Student;
import com.store_inventory.pages.components.UITheme;
import com.store_inventory.services.AppManagerService;
import java.awt.*;
import java.text.DecimalFormat;
import javax.swing.*;

public class HomePage extends JPanel implements Refreshable {
  private static final DecimalFormat CURRENCY = new DecimalFormat("#,##0.00");
  private final NavigationHandler navigationHandler;
  private final AppManagerService appManagerService;
  private final JLabel studentNameValue = new JLabel("-");
  private final JLabel studentIdValue = new JLabel("-");
  private final JLabel tuitionBalanceValue = new JLabel("PHP 0.00");
  private final JLabel walletBalanceValue = new JLabel("PHP 0.00");

  public HomePage(NavigationHandler navigationHandler,
                  AppManagerService appManagerService) {
    this.navigationHandler = navigationHandler;
    this.appManagerService = appManagerService;
    setLayout(new BorderLayout());
    setBackground(UITheme.BACKGROUND);

    JPanel content = new JPanel();
    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
    content.setOpaque(false);
    content.setBorder(new javax.swing.border.EmptyBorder(28, 20, 28, 20));

    content.add(headerCard());
    content.add(Box.createVerticalStrut(12));
    content.add(balanceSection());
    content.add(Box.createVerticalStrut(12));
    content.add(actionsSection());

    JScrollPane scroll = new JScrollPane(content);
    scroll.setBorder(null);
    scroll.setOpaque(false);
    scroll.getViewport().setOpaque(false);
    scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    scroll.getVerticalScrollBar().setUnitIncrement(16);
    UITheme.themeScrollPane(scroll);

    add(scroll, BorderLayout.CENTER);
    refresh();
  }

  private JPanel headerCard() {
    JPanel card = UITheme.cardPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

    JLabel title = new JLabel("Student Dashboard");
    title.setFont(UITheme.TITLE_FONT);
    title.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(title);

    JLabel studentNameLabel = new JLabel("Student Name:");
    studentNameLabel.setFont(UITheme.SUBTITLE_FONT);
    studentNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(studentNameLabel);

    studentNameValue.setFont(UITheme.LABEL_FONT);
    studentNameValue.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(studentNameValue);

    JLabel studentIdLabel = new JLabel("Student ID:");
    studentIdLabel.setFont(UITheme.SUBTITLE_FONT);
    studentIdLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(studentIdLabel);

    studentIdValue.setFont(UITheme.LABEL_FONT);
    studentIdValue.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(studentIdValue);

    card.add(title);
    card.add(Box.createVerticalStrut(6));
    card.add(studentNameLabel);
    card.add(Box.createVerticalStrut(2));
    card.add(studentNameValue);
    card.add(Box.createVerticalStrut(8));
    card.add(studentIdLabel);
    card.add(Box.createVerticalStrut(2));
    card.add(studentIdValue);
    return card;
  }

  private JPanel balanceSection() {
    JPanel container = new JPanel(new GridLayout(1, 2, 12, 0));
    container.setOpaque(false);
    container.setAlignmentX(Component.LEFT_ALIGNMENT);

    tuitionBalanceValue.setFont(UITheme.TITLE_FONT);
    tuitionBalanceValue.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(tuitionBalanceValue);

    walletBalanceValue.setFont(UITheme.TITLE_FONT);
    walletBalanceValue.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(walletBalanceValue);

    container.add(balanceCard("Tuition Balance", tuitionBalanceValue));
    container.add(balanceCard("Wallet Balance", walletBalanceValue));
    return container;
  }

  private JPanel balanceCard(String title, JLabel valueLabel) {
    JPanel card = UITheme.cardPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
    card.setAlignmentX(Component.LEFT_ALIGNMENT);

    JLabel titleLabel = new JLabel(title);
    titleLabel.setFont(UITheme.SUBTITLE_FONT);
    titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(titleLabel);

    card.add(titleLabel);
    card.add(Box.createVerticalStrut(8));
    card.add(valueLabel);
    return card;
  }

  private JPanel actionsSection() {
    JPanel card = UITheme.cardPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

    JLabel actionsLabel = new JLabel("Quick Actions");
    actionsLabel.setFont(UITheme.SUBTITLE_FONT);
    actionsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(actionsLabel);

    JButton topUpButton = actionButton("Top Up Wallet",
                                       () -> navigationHandler.navigate(
                                           Navigation.TOP_UP));
    JButton payTuitionButton = actionButton("Pay Tuition",
                                            () -> navigationHandler.navigate(
                                                Navigation.PAY_TUITION));
    JButton historyButton =
        actionButton("View Transaction History",
                     () -> navigationHandler.navigate(
                         Navigation.TRANSACTION_HISTORY));
    JButton logoutButton =
        actionButton("Logout", () -> navigationHandler.logout());

    card.add(actionsLabel);
    card.add(Box.createVerticalStrut(10));
    card.add(topUpButton);
    card.add(Box.createVerticalStrut(8));
    card.add(payTuitionButton);
    card.add(Box.createVerticalStrut(8));
    card.add(historyButton);
    card.add(Box.createVerticalStrut(8));
    card.add(logoutButton);

    return card;
  }

  private JButton actionButton(String label, Runnable onClick) {
    JButton button = UITheme.primaryButton(label);
    button.setAlignmentX(Component.LEFT_ALIGNMENT);
    button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    button.addActionListener(e -> onClick.run());
    return button;
  }

  public void setStudent(Student student) {
    if (student == null) {
      setDashboardData(new StudentDashboardDTO("-", "-", 0, 0));
      return;
    }
    setDashboardData(new StudentDashboardDTO(student.getFullName(),
                                             student.getStudentId(),
                                             student.getTuitionBalance(),
                                             student.getWalletBalance()));
  }

  public void setDashboardData(StudentDashboardDTO dto) {
    if (dto == null) {
      studentNameValue.setText("-");
      studentIdValue.setText("-");
      tuitionBalanceValue.setText("PHP 0.00");
      walletBalanceValue.setText("PHP 0.00");
      return;
    }

    studentNameValue.setText(dto.getFullName());
    studentIdValue.setText(dto.getStudentId());
    tuitionBalanceValue.setText(formatCurrency(dto.getTuitionBalance()));
    walletBalanceValue.setText(formatCurrency(dto.getWalletBalance()));
  }

  @Override
  public void refresh() {
    Student currentStudent = appManagerService.getCurrentStudent();
    if (currentStudent == null) {
      setDashboardData(new StudentDashboardDTO("-", "-", 0, 0));
      return;
    }
    setDashboardData(new StudentDashboardDTO(currentStudent.getFullName(),
                                             currentStudent.getStudentId(),
                                             currentStudent.getTuitionBalance(),
                                             currentStudent.getWalletBalance()));
  }

  private String formatCurrency(double amount) {
    return "PHP " + CURRENCY.format(amount);
  }
}

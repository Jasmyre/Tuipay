package com.store_inventory.pages;

import com.store_inventory.models.Student;
import com.store_inventory.pages.components.UITheme;
import com.store_inventory.services.SessionService;
import com.store_inventory.services.StudentService;
import java.awt.*;
import javax.swing.*;

public class TransactionHistoryPage extends JPanel implements Refreshable {
  private final StudentService studentService;
  private final SessionService sessionService;
  private final JLabel totalHistoryValue = new JLabel("0");

  public TransactionHistoryPage(StudentService studentService,
                                SessionService sessionService) {
    this.studentService = studentService;
    this.sessionService = sessionService;
    setLayout(new GridBagLayout());
    setBackground(UITheme.BACKGROUND);

    JPanel card = UITheme.cardPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

    JLabel title = new JLabel("Transaction History");
    title.setFont(UITheme.TITLE_FONT);
    title.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(title);

    JLabel subtitle = new JLabel("Student transaction records");
    subtitle.setFont(UITheme.SUBTITLE_FONT);
    subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(subtitle);

    totalHistoryValue.setFont(UITheme.customFont(
        UITheme.FONT_FAMILY, UITheme.FONT_WEIGHT_LABEL, 20));
    totalHistoryValue.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(totalHistoryValue);

    card.add(title);
    card.add(Box.createVerticalStrut(6));
    card.add(subtitle);
    card.add(Box.createVerticalStrut(10));
    card.add(totalHistoryValue);
    add(card);

    refresh();
  }

  @Override
  public void refresh() {
    Student currentStudent = sessionService.getCurrentStudent();
    int totalHistory = currentStudent == null
        ? 0
        : studentService.getPaymentHistory(currentStudent).size();
    totalHistoryValue.setText("Total History Records: " + totalHistory);
  }
}

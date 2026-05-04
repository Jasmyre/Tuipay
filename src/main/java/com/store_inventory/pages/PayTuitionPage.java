package com.store_inventory.pages;

import com.store_inventory.models.Student;
import com.store_inventory.pages.components.UITheme;
import com.store_inventory.services.AppServices;
import java.awt.*;
import java.text.DecimalFormat;
import javax.swing.*;

public class PayTuitionPage extends JPanel implements Refreshable {
  private static final DecimalFormat CURRENCY = new DecimalFormat("#,##0.00");
  private final AppServices AppServices;
  private final JLabel tuitionBalanceValue = new JLabel("PHP 0.00");

  public PayTuitionPage(AppServices AppServices) {
    this.AppServices = AppServices;
    setLayout(new GridBagLayout());
    setBackground(UITheme.BACKGROUND);

    JPanel card = UITheme.cardPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

    JLabel title = new JLabel("Pay Tuition");
    title.setFont(UITheme.TITLE_FONT);
    title.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(title);

    JLabel subtitle = new JLabel("Current tuition balance");
    subtitle.setFont(UITheme.SUBTITLE_FONT);
    subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(subtitle);

    tuitionBalanceValue.setFont(UITheme.customFont(
        UITheme.FONT_FAMILY, UITheme.FONT_WEIGHT_LABEL, 20));
    tuitionBalanceValue.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(tuitionBalanceValue);

    card.add(title);
    card.add(Box.createVerticalStrut(6));
    card.add(subtitle);
    card.add(Box.createVerticalStrut(10));
    card.add(tuitionBalanceValue);
    add(card);

    refresh();
  }

  @Override
  public void refresh() {
    Student currentStudent = AppServices.getCurrentStudent();
    double tuitionBalance = currentStudent == null ? 0 : currentStudent.getTuitionBalance();
    tuitionBalanceValue.setText("Tuition Balance: PHP " + CURRENCY.format(tuitionBalance));
  }
}


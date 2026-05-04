package com.store_inventory.pages;

import com.store_inventory.pages.components.UITheme;
import com.store_inventory.services.AppManagerService;
import java.awt.*;
import javax.swing.*;

public class ManageStudentsPage extends JPanel implements Refreshable {
  private final AppManagerService appManagerService;
  private final JLabel totalStudentsValue = new JLabel("0");

  public ManageStudentsPage(AppManagerService appManagerService) {
    this.appManagerService = appManagerService;
    setLayout(new GridBagLayout());
    setBackground(UITheme.BACKGROUND);

    JPanel card = UITheme.cardPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

    JLabel title = new JLabel("Manage Students");
    title.setFont(UITheme.TITLE_FONT);
    title.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(title);

    JLabel subtitle = new JLabel("Student records from AppManagerService");
    subtitle.setFont(UITheme.SUBTITLE_FONT);
    subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(subtitle);

    totalStudentsValue.setFont(UITheme.customFont(UITheme.FONT_FAMILY,
                                                  UITheme.FONT_WEIGHT_LABEL, 20));
    totalStudentsValue.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(totalStudentsValue);

    card.add(title);
    card.add(Box.createVerticalStrut(6));
    card.add(subtitle);
    card.add(Box.createVerticalStrut(10));
    card.add(totalStudentsValue);
    add(card);

    refresh();
  }

  @Override
  public void refresh() {
    int totalStudents = appManagerService.getAdminManager().getAllStudents().size();
    totalStudentsValue.setText("Total Students: " + totalStudents);
  }
}

package com.store_inventory.pages;

import com.store_inventory.pages.components.UITheme;
import com.store_inventory.services.AppManagerService;
import java.awt.*;
import javax.swing.*;

public class UpdateTuitionPage extends JPanel implements Refreshable {
  private final AppManagerService appManagerService;
  private final JLabel availableStudentsValue = new JLabel("0");

  public UpdateTuitionPage(AppManagerService appManagerService) {
    this.appManagerService = appManagerService;
    setLayout(new GridBagLayout());
    setBackground(UITheme.BACKGROUND);

    JPanel card = UITheme.cardPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

    JLabel title = new JLabel("Update Tuition");
    title.setFont(UITheme.TITLE_FONT);
    title.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(title);

    JLabel subtitle = new JLabel("Tuition data source is ready");
    subtitle.setFont(UITheme.SUBTITLE_FONT);
    subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(subtitle);

    availableStudentsValue.setFont(UITheme.customFont(
        UITheme.FONT_FAMILY, UITheme.FONT_WEIGHT_LABEL, 20));
    availableStudentsValue.setAlignmentX(Component.LEFT_ALIGNMENT);
    UITheme.themeLabel(availableStudentsValue);

    card.add(title);
    card.add(Box.createVerticalStrut(6));
    card.add(subtitle);
    card.add(Box.createVerticalStrut(10));
    card.add(availableStudentsValue);
    add(card);

    refresh();
  }

  @Override
  public void refresh() {
    int count = appManagerService.getAdminManager().getAllStudents().size();
    availableStudentsValue.setText("Students Available: " + count);
  }
}

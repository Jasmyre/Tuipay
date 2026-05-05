package com.store_inventory.pages.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class DialogTitleBar extends JPanel {
  private final Dialog dialog;
  private Point dragStart = null;

  public DialogTitleBar(Dialog dialog, String title) {
    this.dialog = dialog;
    setLayout(new BorderLayout());
    setBackground(UITheme.TITLEBAR_BACKGROUND);
    setBorder(new EmptyBorder(6, 10, 6, 10));

    JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
    left.setOpaque(false);
    JLabel titleLabel = new JLabel(title);
    titleLabel.setFont(UITheme.customFont(UITheme.FONT_FAMILY, Font.BOLD, 14));
    titleLabel.setForeground(UITheme.TITLEBAR_TEXT);
    left.add(titleLabel);

    JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
    right.setOpaque(false);
    JButton close = titleButton("×");
    close.addActionListener(e -> dialog.dispose());
    right.add(close);

    add(left, BorderLayout.WEST);
    add(right, BorderLayout.EAST);

    MouseAdapter drag = new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        dragStart = e.getPoint();
      }

      @Override
      public void mouseDragged(MouseEvent e) {
        if (dragStart == null) {
          return;
        }
        Point location = dialog.getLocation();
        int x = location.x + e.getX() - dragStart.x;
        int y = location.y + e.getY() - dragStart.y;
        dialog.setLocation(x, y);
      }
    };
    addMouseListener(drag);
    addMouseMotionListener(drag);
  }

  private JButton titleButton(String text) {
    JButton button = new JButton(text);
    button.setFont(UITheme.customFont(UITheme.FONT_FAMILY, Font.BOLD, 12));
    button.setForeground(UITheme.TITLEBAR_TEXT);
    button.setBackground(UITheme.TITLEBAR_BACKGROUND);
    button.setBorder(new EmptyBorder(4, 8, 4, 8));
    button.setFocusPainted(false);
    button.setContentAreaFilled(false);
    button.setOpaque(true);
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));

    Color base = UITheme.TITLEBAR_BACKGROUND;
    Color hover = UITheme.TITLEBAR_BUTTON_CLOSE_HOVER;
    button.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e) {
        button.setBackground(hover);
      }

      @Override
      public void mouseExited(MouseEvent e) {
        button.setBackground(base);
      }
    });

    return button;
  }
}

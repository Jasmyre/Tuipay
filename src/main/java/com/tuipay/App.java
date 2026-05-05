package com.tuipay;

import java.awt.Color;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

import com.tuipay.pages.AppFrame;
import com.tuipay.services.AppServices;

public class App {
  public static void main(String[] args) {
    UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));

    SwingUtilities.invokeLater(() -> {
      AppServices services = new AppServices();
      TestDataSeeder.seed(services);

      AppFrame frame = new AppFrame(services);
      frame.setVisible(true);
    });
  }
}

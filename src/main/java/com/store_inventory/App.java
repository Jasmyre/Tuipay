package com.store_inventory;

import com.store_inventory.pages.AppFrame;
import javax.swing.SwingUtilities;

public class App {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      AppServices services = new AppServices();
      TestDataSeeder.seed(services);

      AppFrame frame = new AppFrame(services);
      frame.setVisible(true);
    });
  }
}

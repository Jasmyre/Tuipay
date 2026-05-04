package com.store_inventory;

import com.store_inventory.pages.AppFrame;
import com.store_inventory.services.AppManagerService;
import javax.swing.SwingUtilities;

public class App {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      AppManagerService services = new AppManagerService();
      TestDataSeeder.seed(services);

      AppFrame frame = new AppFrame(services);
      frame.setVisible(true);
    });
  }
}

package com.store_inventory;

import com.store_inventory.services.InventoryManager;
import com.store_inventory.services.SalesManager;

public class AppServices {
  private final InventoryManager inventoryManager;
  private final SalesManager salesManager;

  public AppServices() {
    inventoryManager = new InventoryManager();
    salesManager = new SalesManager();
  }

  public InventoryManager getInventoryManager() { return inventoryManager; }

  public SalesManager getSalesManager() { return salesManager; }
}

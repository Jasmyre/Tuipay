package com.store_inventory;

import com.store_inventory.models.Product;
import com.store_inventory.models.SaleItem;
import com.store_inventory.models.SaleTransaction;
import java.util.UUID;

public final class TestDataSeeder {
  private TestDataSeeder() {}

  public static void seed(AppServices services) {
    if (services == null) {
      return;
    }

    services.getInventoryManager().addProduct(
        new Product("101", "Wireless Earbuds", "Audio", 799.0, 32, 10));
    services.getInventoryManager().addProduct(
        new Product("102", "Bluetooth Speaker", "Audio", 1299.0, 12, 8));
    services.getInventoryManager().addProduct(
        new Product("103", "Phone Case", "Accessories", 249.0, 68, 15));
    services.getInventoryManager().addProduct(
        new Product("104", "USB-C Charger", "Accessories", 399.0, 45, 12));
    services.getInventoryManager().addProduct(
        new Product("105", "Laptop Stand", "Office", 1199.0, 7, 10));
    services.getInventoryManager().addProduct(
        new Product("106", "Smart Bulb", "Home", 499.0, 0, 6));

    recordSeedSale(services, "101", 2);
    recordSeedSale(services, "105", 1);
    recordSeedSale(services, "103", 5);
  }

  private static void recordSeedSale(AppServices services, String sku,
                                     int quantity) {
    var inventoryManager = services.getInventoryManager();
    var salesManager = services.getSalesManager();
    Product product = inventoryManager.findProduct(sku);
    if (product == null || product.getQuantity() < quantity) {
      return;
    }
    SaleTransaction transaction =
        new SaleTransaction("TX-" + UUID.randomUUID());
    transaction.addItem(new SaleItem(product, quantity));
    salesManager.recordTransaction(transaction);
    inventoryManager.reduceStock(sku, quantity);
  }
}

package com.store_inventory;

public final class TestDataSeeder {
  private TestDataSeeder() {}

  public static void seed(AppServices services) {
    if (services == null) {
      return;
    }

    services.getTemporaryStorageService().initializeSeedData();
  }
}

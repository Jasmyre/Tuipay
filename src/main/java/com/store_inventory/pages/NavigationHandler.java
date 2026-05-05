package com.store_inventory.pages;

import com.store_inventory.pages.components.UITheme;

public interface NavigationHandler {
  abstract void navigate(String destination);
  abstract void logout();
  abstract void changeTheme(UITheme.ThemeMode mode);
}

package com.tuipay.pages;

import com.tuipay.pages.components.UITheme;

public interface NavigationHandler {
  abstract void navigate(String destination);
  abstract void logout();
  abstract void changeTheme(UITheme.ThemeMode mode);
}

package com.store_inventory.services;

import java.util.List;

public final class TopUpProviderRegistry {
  private static final List<TopUpProvider> PROVIDERS = List.of(
      new TopUpProvider("maya", "Maya", "E-Wallet",
                        "Maya Mobile Number", "11-digit number", 11, 11),
      new TopUpProvider("gcash", "GCash", "E-Wallet",
                        "GCash Mobile Number", "11-digit number", 11, 11),
      new TopUpProvider("chinabank", "China Bank", "Online Banking",
                        "China Bank Account Number", "10-16 digit account", 10, 16));

  private TopUpProviderRegistry() {}

  public static List<TopUpProvider> getProviders() {
    return PROVIDERS;
  }

  public static final class TopUpProvider {
    private final String key;
    private final String displayName;
    private final String channel;
    private final String accountLabel;
    private final String accountHint;
    private final int minDigits;
    private final int maxDigits;

    public TopUpProvider(String key, String displayName, String channel,
                         String accountLabel, String accountHint, int minDigits,
                         int maxDigits) {
      this.key = key;
      this.displayName = displayName;
      this.channel = channel;
      this.accountLabel = accountLabel;
      this.accountHint = accountHint;
      this.minDigits = minDigits;
      this.maxDigits = maxDigits;
    }

    public String getKey() {
      return key;
    }

    public String getDisplayName() {
      return displayName;
    }

    public String getChannel() {
      return channel;
    }

    public String getAccountLabel() {
      return accountLabel;
    }

    public String getAccountHint() {
      return accountHint;
    }

    public int getMinDigits() {
      return minDigits;
    }

    public int getMaxDigits() {
      return maxDigits;
    }
  }
}

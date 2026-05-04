package com.store_inventory.pages;

import com.store_inventory.AppServices;
import com.store_inventory.models.Admin;
import com.store_inventory.models.Student;
import com.store_inventory.pages.components.Header;
import com.store_inventory.pages.components.UITheme;
import com.store_inventory.pages.components.WindowTitleBar;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class AppFrame extends JFrame implements NavigationHandler {
  private static final String ROOT_LOGIN = "rootLogin";
  private static final String ROOT_APP = "rootApp";
  private CardLayout rootLayout;
  private CardLayout pageLayout;
  private JPanel rootPanel;
  private JPanel pagePanel;
  private LoginPage loginPage;
  private HomePage studentDashboardPage;
  private Header header;
  private WindowTitleBar titleBar;
  private final Map<String, String> titles = new HashMap<>();
  private final Map<String, JPanel> pages = new HashMap<>();
  private final AppServices services;
  private String currentDestination = Navigation.HOME;
  private String currentUser = "";
  private boolean appVisible = false;

  public AppFrame(AppServices services) {
    this.services = services;
    setTitle("Store Inventory");
    setUndecorated(true);
    setSize(1100, 700);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setExtendedState(JFrame.MAXIMIZED_BOTH);

    initializeTitles();
    rebuildUI();
  }

  private void initializeTitles() {
    titles.put(Navigation.HOME, "Dashboard");
    titles.put(Navigation.MANAGE_STUDENTS, "Manage Students");
    titles.put(Navigation.VIEW_TRANSACTIONS, "View Transactions");
    titles.put(Navigation.UPDATE_TUITION, "Update Tuition");
    titles.put(Navigation.TOP_UP, "Top Up");
    titles.put(Navigation.PAY_TUITION, "Pay Tuition");
    titles.put(Navigation.TRANSACTION_HISTORY, "Transaction History");
  }

  private void rebuildUI() {
    rootLayout = new CardLayout();
    pageLayout = new CardLayout();
    rootPanel = new JPanel(rootLayout);
    pagePanel = new JPanel(pageLayout);
    loginPage = new LoginPage();

    String currentTitle = titles.getOrDefault(currentDestination, "Page");
    header = new Header(currentTitle, currentUser, this);
    titleBar = new WindowTitleBar(this, "Store Inventory");

    getContentPane().removeAll();
    buildRoot();
    setLayout(new BorderLayout());
    add(titleBar, BorderLayout.NORTH);
    add(rootPanel, BorderLayout.CENTER);
    wireLoginAction();

    if (appVisible) {
      rootLayout.show(rootPanel, ROOT_APP);
      header.setUser(currentUser);
      navigate(resolveDestination(currentDestination));
    } else {
      rootLayout.show(rootPanel, ROOT_LOGIN);
    }

    revalidate();
    repaint();
  }

  private void buildRoot() {
    pages.clear();
    rootPanel.add(loginPage, ROOT_LOGIN);

    JPanel appPanel = new JPanel(new BorderLayout());
    appPanel.setBackground(UITheme.BACKGROUND);
    appPanel.add(header, BorderLayout.NORTH);

    pagePanel.setBackground(UITheme.BACKGROUND);
    studentDashboardPage = new HomePage(this, services);
    addPage(Navigation.HOME, studentDashboardPage);
    addPage(Navigation.MANAGE_STUDENTS, new PlaceholderPage("Manage Students"));
    addPage(Navigation.VIEW_TRANSACTIONS,
            new PlaceholderPage("View Transactions"));
    addPage(Navigation.UPDATE_TUITION, new PlaceholderPage("Update Tuition"));
    addPage(Navigation.TOP_UP, new PlaceholderPage("Top Up"));
    addPage(Navigation.PAY_TUITION, new PlaceholderPage("Pay Tuition"));
    addPage(Navigation.TRANSACTION_HISTORY,
            new PlaceholderPage("Transaction History"));

    appPanel.add(pagePanel, BorderLayout.CENTER);

    rootPanel.add(appPanel, ROOT_APP);
  }

  private void wireLoginAction() {
    loginPage.getLoginButton().addActionListener(e -> {
      Object account =
          services.getAuthService().login(loginPage.getId(),
                                          loginPage.getPassword());
      if (account != null) {
        if (account instanceof Admin) {
          currentUser = ((Admin)account).getAdminId();
        } else if (account instanceof Student) {
          currentUser = ((Student)account).getStudentId();
        } else {
          currentUser = loginPage.getId();
        }
        currentDestination = Navigation.HOME;
        appVisible = true;
        showApp();
      } else {
        JOptionPane.showMessageDialog(this, "Invalid ID or password", "Error",
                                      JOptionPane.ERROR_MESSAGE);
      }
    });
  }

  private void showApp() {
    rootLayout.show(rootPanel, ROOT_APP);
    header.setUser(currentUser);
    navigate(resolveDestination(currentDestination));
  }

  @Override
  public void navigate(String destination) {
    currentDestination = resolveDestination(destination);
    String defaultTitle = isAdminUser() ? "Admin Dashboard" : "Dashboard";
    String title = titles.getOrDefault(currentDestination, defaultTitle);
    if (Navigation.HOME.equals(currentDestination)) {
      title = defaultTitle;
    }
    header.setTitle(title);
    pageLayout.show(pagePanel, currentDestination);
    JPanel page = pages.get(currentDestination);
    if (page instanceof Refreshable) {
      ((Refreshable) page).refresh();
    }
  }

  @Override
  public void logout() {
    services.getAuthService().logout();
    currentUser = "";
    appVisible = false;
    currentDestination = Navigation.HOME;
    loginPage.clearFields();
    rootLayout.show(rootPanel, ROOT_LOGIN);
  }

  @Override
  public void changeTheme(UITheme.ThemeMode mode) {
    if (mode == null || mode == UITheme.getThemeMode()) {
      return;
    }
    UITheme.setThemeMode(mode);
    rebuildUI();
  }

  private String resolveDestination(String destination) {
    if (destination == null || !pages.containsKey(destination)) {
      return Navigation.HOME;
    }
    return destination;
  }

  private void addPage(String key, JPanel page) {
    pages.put(key, page);
    pagePanel.add(page, key);
  }

  private boolean isAdminUser() {
    return services.getSessionService().getCurrentAdmin() != null;
  }

  private static final class PlaceholderPage extends JPanel implements Refreshable {
    private final JLabel titleLabel = new JLabel();

    private PlaceholderPage(String title) {
      setLayout(new GridBagLayout());
      setBackground(UITheme.BACKGROUND);

      JPanel card = UITheme.cardPanel();
      card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

      titleLabel.setText(title);
      titleLabel.setFont(UITheme.TITLE_FONT);
      titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
      UITheme.themeLabel(titleLabel);

      JLabel subtitle = new JLabel("Page scaffold ready.");
      subtitle.setFont(UITheme.SUBTITLE_FONT);
      subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
      UITheme.themeLabel(subtitle);

      card.add(titleLabel);
      card.add(Box.createVerticalStrut(8));
      card.add(subtitle);
      add(card);
    }

    @Override
    public void refresh() {}
  }
}

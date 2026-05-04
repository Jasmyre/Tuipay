# Store Inventory System Design

## 1) Purpose
This document describes how the application is connected across:
- Models
- Services/Managers
- Pages (UI)
- Application composition (`AppServices`)
- Startup backend bootstrap (`TestDataSeeder`)

It is based on the current source implementation.

## 2) High-Level Architecture
The app is a desktop Java Swing application with an in-memory backend.

```text
App.main
  -> AppServices (creates managers)
  -> TestDataSeeder.seed(services) (preloads sample data + sample sales)
  -> AppFrame(services) (wires pages to managers)

Pages (UI) <-> Managers/Services <-> Models (in-memory objects)
```

No database is used in the current implementation. Data is stored in process memory via `ArrayList` collections inside managers.

## 3) Startup and Composition Flow

### 3.1 `App.java`
Startup sequence:
1. Create `AppServices`
2. Seed demo domain data with `TestDataSeeder.seed(services)`
3. Build and show `AppFrame(services)` on the Swing EDT

### 3.2 `AppServices.java`
`AppServices` is the central dependency container:
- Instantiates one `InventoryManager`
- Instantiates one `SalesManager`
- Exposes them via getters

This creates shared singleton-like manager instances for the app session.

### 3.3 `TestDataSeeder.java`
`TestDataSeeder` performs bootstrap backend logic:
- Adds initial products through `InventoryManager.addProduct(...)`
- Records initial sales through:
  - `SaleTransaction` + `SaleItem`
  - `SalesManager.recordTransaction(...)`
  - `InventoryManager.reduceStock(...)`

It also guards invalid seed sales:
- Skips transaction when product not found
- Skips transaction when stock is insufficient

## 4) Domain Model Layer

### 4.1 `Product`
Represents stock item data:
- `sku`, `name`, `category`
- `price`, `quantity`, `reorderLevel`

Core behavior:
- `isLowStock()` -> `quantity <= reorderLevel`
- `getInventoryValue()` -> `price * quantity`

### 4.2 `SaleItem`
Represents one line item in a sale:
- Holds `Product`, `quantity`
- Captures `unitPrice` at sale time
- Computes subtotal via `getSubtotal()`

### 4.3 `SaleTransaction`
Represents one transaction:
- `transactionID`, `date`, `items`, `totalAmount`
- `addItem(...)` mutates `items` and increments `totalAmount`

## 5) Service/Manager Layer

### 5.1 `InventoryManager`
In-memory inventory service over `ArrayList<Product>`.

Responsibilities:
- CRUD-like operations for products (`add`, `update`, `remove`, `find`)
- Stock changes (`restockProduct`, `reduceStock`)
- Aggregates and status counters:
  - total inventory value
  - total units
  - in-stock / low-stock / out-of-stock counts
  - low/out-of-stock lists

### 5.2 `SalesManager`
In-memory sales service over `ArrayList<SaleTransaction>`.

Responsibilities:
- Record transactions
- Retrieve full list and recent list
- Compute:
  - total revenue
  - total units sold
  - total sales count

### 5.3 Reporting Services
Reporting is file-export based:
- `Report` (abstract base): export directory and CSV escaping helpers
- `ProductReport`: product inventory snapshot CSV
- `SalesReport`: transaction and revenue CSV
- `InventoryReport`: stock/value/status CSV

All reports write under `exports/`.

## 6) Page Layer and Service Connections

`AppFrame` injects shared managers into pages.

### 6.1 `AppFrame`
Acts as the shell/router:
- Owns login state + navigation
- Uses `CardLayout` for page switching
- Creates pages with dependencies:
  - `HomePage(InventoryManager, SalesManager)`
  - `ProductsPage(InventoryManager)`
  - `InventoryPage(InventoryManager)`
  - `SalesPage(InventoryManager, SalesManager)`
  - `ReportsPage(NavigationHandler, InventoryManager, SalesManager)`

On navigation, pages implementing `Refreshable` are refreshed.

### 6.2 `HomePage`
Read-only dashboard over managers:
- Product count from `inventory.getAllProducts().size()`
- Total units from `inventory.getTotalUnits()`
- Revenue from `sales.getTotalRevenue()`

### 6.3 `ProductsPage`
Product CRUD UI over `InventoryManager`:
- Add/edit -> creates `Product`, then `addProduct` or `updateProduct`
- Delete -> `removeProduct`
- Table always reloaded from `getAllProducts()`

### 6.4 `InventoryPage`
Inventory status UI over `InventoryManager`:
- Uses aggregate counters for top stats
- Uses product list for searchable/filterable inventory rows
- Derives visual status from `quantity` and `reorderLevel`

### 6.5 `SalesPage`
Transaction UI using both managers:
- Builds sale transaction from selected product + quantity
- Validates stock before sale
- Writes sale to `SalesManager.recordTransaction(...)`
- Decrements stock via `InventoryManager.reduceStock(...)`
- Refreshes table from `sales.getAllTransactions()`

### 6.6 `ReportsPage`
Report viewing/export UI using both managers:
- In-memory display from manager aggregates + lists
- Export actions call:
  - `new ProductReport(inventory).generateReport()`
  - `new SalesReport(sales).generateReport()`
  - `new InventoryReport(inventory).generateReport()`

## 7) Backend Logic: End-to-End Data Flows

### 7.1 Product Creation Flow
1. User submits product form in `ProductsPage`
2. `Product` object created
3. `InventoryManager.addProduct(product)`
4. Other pages read updated manager state on refresh

### 7.2 Sale Recording Flow
1. User submits sale form in `SalesPage`
2. Validate stock via selected `Product.getQuantity()`
3. Create `SaleTransaction`
4. Add `SaleItem`
5. `SalesManager.recordTransaction(transaction)`
6. `InventoryManager.reduceStock(sku, qty)`
7. Dashboard, inventory, sales, and reports read updated state

### 7.3 Startup Seed Flow
1. `App` creates `AppServices`
2. `TestDataSeeder.seed(services)` inserts products
3. `TestDataSeeder` records seed sales and reduces stock
4. `AppFrame` starts with populated inventory and sales metrics

## 8) Design Constraints and Implications
- Data is volatile: all state is in-memory and resets on app restart.
- Managers return mutable lists directly (`ArrayList`), so callers can mutate shared state.
- No concurrency controls are present (single desktop session assumptions).
- Authentication is local/static in `LoginPage.authenticate()` (`admin` / `password`), not service-backed.

## 9) Relationship Map (Summary)

```text
App
  -> AppServices
     -> InventoryManager ----> Product
     -> SalesManager --------> SaleTransaction -> SaleItem -> Product

App
  -> TestDataSeeder
     -> InventoryManager.addProduct(...)
     -> SalesManager.recordTransaction(...)
     -> InventoryManager.reduceStock(...)

AppFrame
  -> HomePage(InventoryManager, SalesManager)
  -> ProductsPage(InventoryManager)
  -> InventoryPage(InventoryManager)
  -> SalesPage(InventoryManager, SalesManager)
  -> ReportsPage(InventoryManager, SalesManager)
     -> ProductReport / SalesReport / InventoryReport
```


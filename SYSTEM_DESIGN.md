
# Tuipay System Design

## 1) Purpose

This document describes how the Tuipay application is connected across:

* Models
* Services/Managers
* Pages (UI)
* Application composition (`AppServices`)
* Startup backend bootstrap (`TestDataSeeder`)

It is based on the current Java Swing prototype implementation.

---

## 2) High-Level Architecture

Tuipay is a **Java Swing desktop application** using in-memory storage only.

```text
App.main
  -> AppServices (creates managers)
  -> TestDataSeeder.seed(services) (preloads students, admin, sample transactions)
  -> AppFrame(services) (connects UI pages)

Pages (UI) <-> Managers/Services <-> Models (in-memory objects)
```

No database is used. All data is stored in `ArrayList` inside service classes and resets when the app closes.

---

## 3) Startup and Composition Flow

### 3.1 `App.java`

Startup sequence:

1. Create `AppServices`
2. Seed demo data via `TestDataSeeder.seed(services)`
3. Launch `AppFrame(services)` on Swing EDT

---

### 3.2 `AppServices.java`

Acts as the **central dependency container**:

* `StudentManager`
* `TransactionManager`
* `AdminManager`

Provides shared instances for the whole session.

👉 This ensures all pages use the same in-memory data.

---

### 3.3 `TestDataSeeder.java`

Bootstraps demo data:

* Creates seeded **Student accounts**
* Creates seeded **Admin account**
* Preloads sample:

  * wallet top-ups
  * tuition payments
  * transaction history

Validation rules:

* skips invalid student IDs
* skips payments if insufficient wallet or tuition logic fails

---

## 4) Domain Model Layer

### 4.1 `Student`

Represents a student account:

* `studentId`
* `username`
* `fullName`
* `password`
* `tuitionBalance`
* `walletBalance`
* `transactions`

Core behavior:

* `topUp(amount)`
* `payTuition(amount)`
* `addTransaction(transaction)`
* `getTransactionHistory()`

👉 Represents both identity + financial state of student

---

### 4.2 `Admin`

Represents system administrator:

* `adminId`
* `username`
* `password`

Core behavior:

* `authenticate(id, password)`

👉 Used only for access control

---

### 4.3 `Transaction`

Represents all financial actions:

* `transactionId`
* `studentId`
* `type (TOP_UP / TUITION_PAYMENT)`
* `amount`
* `transactionDate`
* `remainingTuitionBalance`

Core behavior:

* `generateReceipt()`

---

### 4.4 `TransactionType`

Enum values:

* `TOP_UP`
* `TUITION_PAYMENT`

---

## 5) Service / Manager Layer

### 5.1 `AppManagerService`

Main system controller:

* connects all managers
* tracks current logged-in user
* stores session state:

  * `currentAccountId`
  * `currentAccountRole`

Functions:

* initialize managers
* get managers
* set/clear session

👉 Acts like “session manager + system coordinator”

---

### 5.2 `StudentManager`

Handles student operations:

* get all students
* find student by ID
* wallet top-up
* tuition payment
* update balances

👉 Main business logic for student financial actions

---

### 5.3 `TransactionManager`

Handles transaction records:

* create top-up transaction
* create tuition payment transaction
* retrieve all transactions

👉 Central history system for all money actions

---

### 5.4 `AdminManager`

Handles admin-level operations:

* get admin account
* view student records
* view all transactions

👉 Read-only monitoring + management layer

---

## 6) Page Layer and Service Connections

### 6.1 `AppFrame`

Main UI shell:

* uses `CardLayout` for navigation
* injects managers into all pages
* manages login session state

Pages injected:

* `HomePage(StudentManager, TransactionManager)`
* `StudentPage(StudentManager)`
* `TransactionPage(TransactionManager)`
* `AdminPage(AdminManager, StudentManager, TransactionManager)`
* `ReportsPage(...)`

---

### 6.2 `HomePage`

Dashboard view:

Shows:

* total students
* total transactions
* system summary

👉 Read-only overview using managers

---

### 6.3 `StudentPage`

Student management UI:

* view student info
* wallet balance
* tuition balance
* transaction history

Actions:

* top up wallet
* pay tuition

---

### 6.4 `TransactionPage`

Displays all transactions:

* type
* amount
* date
* remaining tuition balance

Uses `TransactionManager`

---

### 6.5 `AdminPage`

Admin control panel:

* view all students
* view all transactions
* monitor balances

---

### 6.6 `ReportsPage`

Export system reports:

* student summary
* transaction history
* financial overview

Outputs:

* CSV files via report classes

---

## 7) Backend Logic: End-to-End Flows

### 7.1 Student Login Flow

1. User enters ID + password
2. `AdminManager` or `StudentManager` validates
3. `AppManagerService` sets session role
4. UI routes to correct dashboard

---

### 7.2 Tuition Payment Flow

1. Student selects amount
2. Validate wallet balance
3. Deduct wallet
4. Deduct tuition balance
5. Create `Transaction`
6. Store in `TransactionManager`
7. Update UI

---

### 7.3 Top-Up Flow

1. Student enters amount
2. Add to wallet balance
3. Create TOP_UP transaction
4. Store transaction
5. Refresh UI

---

### 7.4 Admin Monitoring Flow

1. Admin logs in
2. System loads all managers
3. Admin views:

   * students
   * transactions
   * balances

---

## 8) Design Constraints and Implications

* ❌ No database (in-memory only)
* ❌ No real payment integration (simulated only)
* ❌ No registration system
* ❌ No persistence after restart
* ⚠️ Direct object mutation (ArrayList exposed)
* ⚠️ Single-session desktop application only

---

## 9) Relationship Map (Summary)

```text
App
  -> AppServices
     -> StudentManager ----> Student -> Transaction
     -> AdminManager ------> Admin
     -> TransactionManager -> Transaction

App
  -> TestDataSeeder
     -> StudentManager.add/update balances
     -> TransactionManager.record transactions

AppFrame
  -> HomePage(StudentManager, TransactionManager)
  -> StudentPage(StudentManager)
  -> TransactionPage(TransactionManager)
  -> AdminPage(AdminManager, StudentManager, TransactionManager)
  -> ReportsPage(...)
```

---

## 10) Key Design Idea

Tuipay is structured as:

* **Models = data (Student, Admin, Transaction)**
* **Managers = business logic**
* **AppServices = system container**
* **AppFrame = UI router**
* **Seeder = demo data initializer**

# Tuipay System Design

## 1) Purpose

This document describes the current student-only architecture of Tuipay across:

* Models
* Services
* Swing pages
* App composition (`AppServices`)
* Startup seed flow (`TestDataSeeder`)

## 2) High-Level Architecture

Tuipay is a Java Swing desktop app that uses in-memory storage for the active session.

```text
App.main
  -> AppServices (creates StudentManager + TransactionManager)
  -> TestDataSeeder.seed(services) (preloads student accounts + sample transactions)
  -> AppFrame(services)

Pages (UI) <-> Services <-> Models
```

All runtime data is stored in memory and resets when the app closes.

## 3) Startup and Composition

### 3.1 `App.java`

Startup sequence:

1. Create `AppServices`
2. Seed demo data via `TestDataSeeder.seed(services)`
3. Launch `AppFrame(services)` on Swing EDT

### 3.2 `AppServices.java`

Acts as the session container:

* `StudentManager`
* `TransactionManager`
* `currentAccountId`

Responsibilities:

* initialize shared services
* handle student login/logout
* expose current logged-in student

### 3.3 `TestDataSeeder.java`

Bootstraps demo data:

* creates seeded student accounts
* creates sample top-up and tuition payment transactions

Validation rules:

* skips invalid student IDs
* avoids duplicate student records

## 4) Domain Model Layer

### 4.1 `Student`

Fields:

* `studentId`
* `username`
* `fullName`
* `password`
* `tuitionBalance`
* `walletBalance`
* `transactions`

Behavior:

* `topUp(amount)`
* `payTuition(amount)`
* `addTransaction(transaction)`
* `getTransactionHistory()`

### 4.2 `Transaction`

Fields:

* `transactionId`
* `studentId`
* `type`
* `amount`
* `transactionDate`
* `remainingTuitionBalance`

Behavior:

* `generateReceipt()`

### 4.3 `TransactionType`

Enum values:

* `TOP_UP`
* `TUITION_PAYMENT`

## 5) Service Layer

### 5.1 `StudentManager`

Handles student-side operations:

* authenticate student credentials
* retrieve student records
* top up wallet
* pay tuition
* fetch payment history

### 5.2 `TransactionManager`

Handles transaction creation and aggregation:

* create top-up transactions
* create tuition payment transactions
* aggregate transactions across students

## 6) UI Layer

### 6.1 `AppFrame`

Main application shell:

* controls root/login/app card layouts
* routes between student pages
* refreshes current page after navigation

### 6.2 Student Pages

Current student-facing pages include:

* `StudentPage`
* `TopUpPage`
* `PayTuitionPage`
* `TransactionHistoryPage`

### 6.3 Shared Components

Common page chrome and theming:

* `Header` (student navigation + theme toggle + logout)
* `UITheme`
* `WindowTitleBar`

## 7) End-to-End Flows

### 7.1 Student Login Flow

1. Student submits Student ID and password
2. `StudentManager` authenticates
3. `AppServices` stores `currentAccountId`
4. `AppFrame` routes to `StudentPage`

### 7.2 Top-Up Flow

1. Student enters top-up amount
2. Wallet balance is updated
3. `TransactionManager` creates TOP_UP transaction
4. UI refreshes balances and history

### 7.3 Tuition Payment Flow

1. Student enters payment amount
2. App validates wallet and tuition balance constraints
3. Wallet and tuition balances are updated
4. `TransactionManager` creates TUITION_PAYMENT transaction
5. Receipt is shown and optionally exported

## 8) Constraints

* No persistent database
* No real payment gateway integration
* No registration/forgot-password flow
* Single-process desktop session only

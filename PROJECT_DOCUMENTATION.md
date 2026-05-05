# PROJECT_DOCUMENTATION.md

## Project Title

**Tuipay – Student Tuition Payment Prototype**

## Project Overview

Tuipay is a Java Swing desktop prototype focused on student tuition workflows.
It is intentionally student-only and covers login, wallet top-up, tuition payment,
and transaction history.

This project is a demo prototype:

* No real payment gateway integration
* No bank API integration
* No database connection
* Session-only in-memory data (resets on app restart)
* Seeded student demo accounts

## System Scope

The system supports:

* Student login using Student ID and password
* Viewing student profile and balances
* Simulated wallet top-up
* Simulated tuition payment with validation
* Viewing transaction history
* Logging out and clearing session

## Project Limitations

* No real e-wallet or card processing
* No persistent storage
* No student registration flow
* No forgot-password flow
* No miscellaneous fee categories
* Demo-only behavior and sample data

## Tech Stack

* Java 21
* Java Swing
* In-memory collections (`ArrayList`) for runtime data

## Seeded Demo Accounts

| Student ID  | Username  | Password   |
| ----------- | --------- | ---------- |
| 202509-0033 | student01 | password   |
| 202311-1002 | student02 | student123 |

## Student Flow

### 1. Login

Student enters:

* Student ID
* Password

If valid, the app opens the Student Page.
If invalid, the app shows an error message.

### 2. Student Page

Student can view:

* Full Name
* Student ID
* Username
* Tuition Balance
* Wallet Balance
* Recent transaction activity

Available actions:

* Top Up Wallet
* Pay Tuition
* Transaction History
* Logout

### 3. Top Up Wallet

Student enters an amount to add to wallet balance.
If valid, the wallet is updated and a top-up transaction is recorded.

### 4. Pay Tuition

Student enters a payment amount.
The app validates available wallet funds and remaining tuition balance.
If valid, it deducts from wallet and tuition balance, records a tuition payment transaction,
and shows a receipt dialog.

### 5. Transaction History

Student can view total transaction count and historical records from the current session.

### 6. Logout

Session is cleared and the app returns to the login screen.

## Core Features

* Student-only authentication
* Student dashboard and account summary
* Wallet top-up simulation
* Tuition payment simulation
* Transaction history tracking
* Session-scoped in-memory storage

## Future Improvements

* Persistent database integration
* Real payment provider integration
* Notification support (email/SMS)
* Enhanced account management features

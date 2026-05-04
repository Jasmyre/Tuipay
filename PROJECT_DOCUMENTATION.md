# PROJECT_DOCUMENTATION.md

## Project Title

**Tuipay – Tuition Payment Management System**

## Project Overview

Tuipay is a **Java Swing desktop application** designed to simplify tuition payments for students of Sumulong College of Arts and Sciences.

This project is built as a **demo prototype only**, meaning:

* No real payment gateways
* No real bank integration
* No database connection
* Uses **session-based storage only** (data resets when the app closes)
* Uses **seeded/demo student and admin accounts** (including profile usernames)

The goal is to simulate a simple tuition payment process while focusing only on the **core features**.

---

# System Scope

The system allows:

* Users to log in using ID and password
* View tuition balance
* Simulate wallet top-up
* Pay tuition
* View payment history

Admins can:

* Log in
* View student records
* Monitor payments
* Update tuition balances

---

# Limitations

* No real GCash integration
* No real credit/debit card payments
* No real database storage
* No registration system
* No forgot password feature
* No miscellaneous fee payments
* Data is temporary and resets after application restart
* For demonstration purposes only

---

# Tech Stack

* Java
* Java Swing
* ArrayList / HashMap (temporary storage)
* OOP principles

---

# Seeded Demo Accounts

## Student Accounts

| Student ID  | Username  | Password   |
| ----------- | --------- | ---------- |
| 202311-1001 | student01 | student123 |
| 202311-1002 | student02 | student123 |

## Admin Account

| Admin ID | Username | Password |
| -------- | -------- | -------- |
| ADMIN001 | admin    | admin123 |

---

# Unified Login Flow

## 1. Login

User enters:

* ID
* Password

System validates credentials from seeded data and automatically determines role by ID.
Username is profile data loaded from seed records and is not used for login.

If ID belongs to a student and password is correct:
→ Redirect to Student Dashboard

If ID belongs to admin and password is correct:
→ Redirect to Admin Dashboard

If invalid:
→ Show error message

---

## 2. Student Dashboard

Student can view:

* Name
* Student ID
* Current tuition balance
* Wallet balance

Available actions:

* Top Up Wallet
* Pay Tuition
* View Payment History
* Logout

---

## 3. Top Up Wallet

Student enters amount to add.

Example:

* Current wallet: ₱500
* Top-up: ₱1,000

New wallet balance:
→ ₱1,500

System records transaction.

---

## 4. Pay Tuition

Student selects payment amount.

System checks:

* If wallet balance is enough
* If tuition balance exists

If valid:

* Deduct from wallet
* Deduct from tuition balance
* Save transaction history
* Generate receipt popup

Example:

* Tuition balance: ₱10,000
* Wallet balance: ₱5,000
* Payment: ₱3,000

Result:

* Wallet → ₱2,000
* Tuition → ₱7,000

---

## 5. View Payment History

Student can see:

* Date
* Amount paid
* Remaining tuition balance
* Transaction type

---

## 6. Logout

Session ends.

User returns to login screen.

---

# Admin Flow

## 1. Access via Unified Login

Admin enters:

* Admin ID
* Password

System validates admin credentials and routes to the Admin Dashboard automatically.

---

## 2. Admin Dashboard

Admin can view:

* Total students
* Total payments made
* Total remaining balances

Available actions:

* View Students
* View Transactions
* Update Tuition Balances
* Logout

---

## 3. View Students

Admin can view all students:

* Student ID
* Name
* Tuition balance
* Wallet balance

---

## 4. View Transactions

Admin can monitor all:

* Top-up transactions
* Tuition payments

Shows:

* Student name
* Amount
* Date
* Transaction type

---

## 5. Update Tuition Balance

Admin can manually:

* Add tuition fees
* Reduce balances

Example:
Semester reset:

* Add ₱15,000 tuition fee

---

## 6. Logout

Admin session ends.

---

# Core Functionalities Only

This project intentionally focuses only on essential features:

✅ Unified ID + password login authentication
✅ Student dashboard
✅ Wallet top-up simulation
✅ Tuition payment simulation
✅ Payment history
✅ Admin monitoring
✅ Session-based temporary storage

---

# Future Improvements

If upgraded in the future:

* MySQL database integration
* Real GCash API integration
* SMS/email receipts
* Mobile application version
* Real-time notifications

---

# Conclusion

Tuipay is a simple prototype that demonstrates how schools can reduce long payment lines by digitizing tuition transactions in a controlled demo environment.

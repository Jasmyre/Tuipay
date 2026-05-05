# Tuipay

Tuipay is a Java Swing desktop prototype for student tuition workflows.

It is intentionally student-only and demo-focused, covering login, wallet top-up, tuition payment, and transaction history in a single desktop session.

## Features

- Student login using Student ID and password
- Student dashboard with profile, tuition balance, wallet balance, and recent activity
- Wallet top-up simulation with selectable providers
- Tuition payment flow with validation and receipt preview
- Transaction history view for current-session records
- Receipt export to local text files (`export/`)
- Light/Dark theme toggle in the app shell

## Tech Stack

- Java 21
- Java Swing
- Maven (project build)
- In-memory storage (`ArrayList`) for runtime state

## Project Structure

```text
src/main/java/com/store_inventory/
  App.java
  TestDataSeeder.java
  models/
  pages/
  services/
src/main/resources/
  assets/
```

## How It Runs

Startup flow:

1. `App.main` initializes `AppServices`
2. `TestDataSeeder.seed(services)` loads demo student accounts
3. `AppFrame` starts and shows the login UI

All data is in-memory and resets when the app is restarted.

## Seeded Demo Accounts

| Student ID  | Username  | Password   |
| ----------- | --------- | ---------- |
| 202509-0033 | student01 | password   |
| 202311-1002 | student02 | student123 |

## Requirements

- JDK 21 installed
- Maven installed and available in `PATH`

## Run Locally

### Option 1: Maven

```bash
mvn compile
mvn exec:java -Dexec.mainClass=com.store_inventory.App
```

### Option 2: Plain Java (no Maven plugin)

```bash
javac -d target/classes $(find src/main/java -name "*.java")
java -cp target/classes com.store_inventory.App
```

On Windows PowerShell:

```powershell
New-Item -ItemType Directory -Force target/classes | Out-Null
Get-ChildItem -Path src/main/java -Recurse -Filter *.java |
  ForEach-Object { $_.FullName } | Set-Content sources.txt
javac -d target/classes @sources.txt
Remove-Item sources.txt
java -cp target/classes com.store_inventory.App
```

## User Flow

1. Login with a seeded student account
2. Open **Top Up Wallet** to add funds
3. Open **Pay Tuition** to pay from wallet against tuition balance
4. Review transactions in **Transaction History**
5. Save tuition receipt text files from the payment receipt dialog

## Limitations

- No real payment gateway integration
- No bank or wallet API integration
- No persistent database/storage
- No account registration or forgot-password flow
- Session-only runtime data

## Documentation

- [PROJECT_DOCUMENTATION.md](PROJECT_DOCUMENTATION.md)
- [SYSTEM_DESIGN.md](SYSTEM_DESIGN.md)

## License

No license file is currently included in this repository.

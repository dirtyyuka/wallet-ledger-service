# Wallet Ledger Service
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-000?style=for-the-badge&logo=apache-kafka&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

## Overview
This project involves a high-performance, asynchronous ledger system designed to process financial transactions, manage real-time account balances, and ensure data integrity using a scheduled polling and messaging architecture.

## Workflow
1. Ingestion: Transactions are received via API and persisted to the database with a PENDING status.

2. Scheduling & Polling: A background scheduler identifies unprocessed transactions.

3. Messaging: Transactions are published to Kafka to decouple processing from ingestion.

4. Consumption: A dedicated listener processes the Kafka events.

5. Processing: The service calculates the new account balance, updates the user's record, and creates a permanent entry in the ledger.

## Architecture 
<strong>1. Data Integrity </strong>
Transactions happen inside a @Transactional block which rolls back the database operations if the method is not successfully completed. This also prevents the same transactions to be recorded again by checking the ledger for any transaction with the same ID.

<strong>2. Database cleaning </strong>
A scheduled background worker performs cleanup operations on the transactions table to delete all rows with PROCESSED status. This prevents the database from piling up and lowering performance.

## Setup
1. Clone the repository
```git
git clone https://github.com/dirtyyuka/wallet-ledger-service
```

2. Run the tests
```git
./mvnw test
```

Running the tests requires having docker installed.

### Contact
Mayank Joshi - @dirtyyuka - mayankjoshi455@gmail.com
<br>
Project link: https://github.com/dirtyyuka/wallet-ledger-service

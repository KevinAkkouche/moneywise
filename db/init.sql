-- MoneyWise DB initialization
-- Run automatically by Docker on first start

CREATE DATABASE IF NOT EXISTS moneywise_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE moneywise_db;

-- Hibernate will auto-create tables via ddl-auto=update
-- This script ensures the DB and permissions are correctly set up

GRANT ALL PRIVILEGES ON moneywise_db.* TO 'moneywise_user'@'%';
FLUSH PRIVILEGES;

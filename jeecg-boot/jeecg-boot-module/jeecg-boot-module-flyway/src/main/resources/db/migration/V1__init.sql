-- Example Flyway migration: create a simple table used by demo
CREATE TABLE IF NOT EXISTS jeecg_demo_table (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


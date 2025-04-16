CREATE TABLE appeals
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(255),
    description VARCHAR(255),
    appeal_date DATETIME
);
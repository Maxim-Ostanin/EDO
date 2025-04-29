CREATE TABLE approvals
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    appeal_id     BIGINT NOT NULL,
    status        VARCHAR(255),
    comment       TEXT,
    response_date DATETIME,
    FOREIGN KEY (appeal_id) REFERENCES appeals (id)
);
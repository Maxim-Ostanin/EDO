CREATE TABLE appeals
(
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    description VARCHAR(255),
    appeal_date TIMESTAMP
);
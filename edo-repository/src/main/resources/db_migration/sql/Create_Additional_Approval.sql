CREATE TABLE additional_approval
(
    id BIGSERIAL PRIMARY KEY,
    approval_id BIGINT NOT NULL,
    type VARCHAR(10),
    status VARCHAR(40),
    comment VARCHAR(500),
    response_date TIMESTAMP,
        FOREIGN KEY (approval_id)
        REFERENCES approvals(id)
);
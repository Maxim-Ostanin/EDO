CREATE TABLE approvals
(
    id BIGSERIAL PRIMARY KEY,
    appeal_id BIGINT NOT NULL,
    status VARCHAR(40) NOT NULL,
    comment VARCHAR(500) NOT NULL,
    response_date TIMESTAMP NOT NULL,
    FOREIGN KEY (appeal_id) REFERENCES appeals(id)
)
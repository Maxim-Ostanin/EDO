CREATE TABLE additional_approval
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    approval_id BIGINT NOT NULL,
    type VARCHAR(10),
    status VARCHAR(40),
    comment VARCHAR(500),
    response_date DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT fk_additional_approval_approvals
        FOREIGN KEY (approval_id) REFERENCES approvals(id)
)
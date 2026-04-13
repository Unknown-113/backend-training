CREATE TABLE terminal_session_token (
    terminal_session_token_id bigserial    NOT NULL,
    token                     varchar(36)  NOT NULL UNIQUE,
    training_run_id           bigint       NOT NULL,
    created_at                timestamp    NOT NULL,
    expires_at                timestamp    NOT NULL,
    used                      boolean      NOT NULL DEFAULT false,
    jwt_token                 TEXT         NOT NULL DEFAULT '',
    session_state             varchar(64),
    access_token              varchar(64),
    PRIMARY KEY (terminal_session_token_id),
    FOREIGN KEY (training_run_id) REFERENCES training_run
);

CREATE INDEX terminal_session_token_token_index ON terminal_session_token (token);
CREATE SEQUENCE terminal_session_token_seq AS bigint INCREMENT 50 MINVALUE 1;

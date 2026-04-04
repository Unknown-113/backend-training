CREATE TABLE training_access_restriction (
    id                     bigserial NOT NULL,
    participant_ref_id     int8      NOT NULL,
    training_definition_id int8      NOT NULL,
    access_count           int4      NOT NULL DEFAULT 0,
    banned                 boolean   NOT NULL DEFAULT FALSE,
    last_accessed_at       timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    banned_at              timestamp,
    PRIMARY KEY (id),
    FOREIGN KEY (training_definition_id) REFERENCES training_definition,
    UNIQUE (participant_ref_id, training_definition_id)
);

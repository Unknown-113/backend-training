ALTER TABLE terminal_session_token
    DROP CONSTRAINT IF EXISTS terminal_session_token_training_run_id_fkey;

ALTER TABLE terminal_session_token
    ADD CONSTRAINT terminal_session_token_training_run_id_fkey
    FOREIGN KEY (training_run_id) REFERENCES training_run ON DELETE CASCADE;

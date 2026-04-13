ALTER TABLE training_instance
    ADD COLUMN IF NOT EXISTS max_access_attempts INT NOT NULL DEFAULT 10;

-- Add dynamic flag fields to training_definition table
ALTER TABLE training_definition ADD COLUMN enable_dynamic_flag BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE training_definition ADD COLUMN flag_change_interval INT;
ALTER TABLE training_definition ADD COLUMN initial_secret VARCHAR(255);
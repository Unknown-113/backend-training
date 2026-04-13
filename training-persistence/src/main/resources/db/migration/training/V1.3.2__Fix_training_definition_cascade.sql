ALTER TABLE training_access_restriction
    DROP CONSTRAINT IF EXISTS training_access_restriction_training_definition_id_fkey;

ALTER TABLE training_access_restriction
    ADD CONSTRAINT training_access_restriction_training_definition_id_fkey
    FOREIGN KEY (training_definition_id) REFERENCES training_definition ON DELETE CASCADE;

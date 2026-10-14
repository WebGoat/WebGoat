ALTER TABLE CONTAINER.assignment_progress ADD COLUMN assignment_id_new BIGINT;

UPDATE CONTAINER.assignment_progress
SET assignment_id_new = assignment_id;

ALTER TABLE CONTAINER.assignment_progress DROP COLUMN assignment_id CASCADE;

ALTER TABLE CONTAINER.assignment_progress
    ALTER COLUMN assignment_id_new RENAME TO assignment_id;

ALTER TABLE CONTAINER.assignment_progress
    ADD CONSTRAINT FK7o6abukma83ku3xrge9sy0qnr
    FOREIGN KEY (assignment_id) REFERENCES CONTAINER.assignment;

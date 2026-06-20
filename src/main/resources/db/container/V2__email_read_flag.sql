-- Track whether a mail has been read so the mailbox button can show the number of unread messages.
ALTER TABLE CONTAINER.email ADD COLUMN read_flag BOOLEAN DEFAULT FALSE NOT NULL;

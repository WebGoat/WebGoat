ALTER TABLE container.lesson_tracker
    RENAME TO container.lesson_progress;

ALTER TABLE container.lesson_tracker_all_assignments
    ALTER COLUMN lesson_tracker_id RENAME TO lesson_progress_id;
ALTER TABLE container.lesson_tracker_all_assignments
    RENAME TO container.lesson_progress_all_assignments;

ALTER TABLE container.lesson_tracker_solved_assignments
    ALTER COLUMN lesson_tracker_id RENAME TO lesson_progress_id;
ALTER TABLE container.lesson_tracker_solved_assignments
    RENAME TO container.lesson_progress_solved_assignments;

ALTER TABLE container.user_tracker
    RENAME TO container.user_progress;

ALTER TABLE container.user_tracker_lesson_trackers
    ALTER COLUMN user_tracker_id RENAME TO user_progress_id;
ALTER TABLE container.user_tracker_lesson_trackers
    ALTER COLUMN lesson_trackers_id RENAME TO lesson_progress_id;
ALTER TABLE container.user_tracker_lesson_trackers
    RENAME TO container.user_progress_lesson_progress;

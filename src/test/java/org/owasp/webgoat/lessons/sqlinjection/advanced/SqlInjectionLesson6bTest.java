package org.owasp.webgoat.lessons.sqlinjection.advanced;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AttackResult;

/**
 * Delta tests for SqlInjectionLesson6b focusing on behavior around completed()
 * after the logging change (printStackTrace removed).
 *
 * These tests ensure:
 *  - completed() still returns success when userid_6b equals getPassword().
 *  - completed() still returns failure when userid_6b does not equal getPassword().
 *
 * The exact logging strategy is not asserted here; tests focus on preserved behavior.
 */
public class SqlInjectionLesson6bTest {

    private LessonDataSource dataSource;
    private SqlInjectionLesson6b lesson;

    @BeforeEach
    void setUp() {
        dataSource = Mockito.mock(LessonDataSource.class);
        lesson = Mockito.spy(new SqlInjectionLesson6b(dataSource));
    }

    @Test
    void completed_shouldSucceedWhenUserIdMatchesPassword() throws IOException {
        // Arrange: stub getPassword to return a known secret
        Mockito.doReturn("expected-secret").when(lesson).getPassword();

        // Act
        AttackResult result = lesson.completed("expected-secret");

        // Assert
        assertTrue(result.getLessonCompleted(), "completed() must return success when userid_6b equals getPassword()");
    }

    @Test
    void completed_shouldFailWhenUserIdDoesNotMatchPassword() throws IOException {
        // Arrange: stub getPassword to return a known secret
        Mockito.doReturn("expected-secret").when(lesson).getPassword();

        // Act
        AttackResult result = lesson.completed("wrong-secret");

        // Assert
        assertFalse(result.getLessonCompleted(), "completed() must return failure when userid_6b does not equal getPassword()");
    }
}

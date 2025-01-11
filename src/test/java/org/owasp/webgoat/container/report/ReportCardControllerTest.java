package org.owasp.webgoat.container.report;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.webgoat.container.i18n.PluginMessages;
import org.owasp.webgoat.container.lessons.Lesson;
import org.owasp.webgoat.container.session.Course;
import org.owasp.webgoat.container.users.LessonProgress;
import org.owasp.webgoat.container.users.UserProgress;
import org.owasp.webgoat.container.users.UserProgressRepository;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(MockitoExtension.class)
// TODO: Rewrite this as end-to-end test this mocks too many classes
public class ReportCardControllerTest {

  private MockMvc mockMvc;
  @Mock private Course course;
  @Mock private UserProgress userTracker;
  @Mock private Lesson lesson;
  @Mock private LessonProgress lessonTracker;
  @Mock private UserProgressRepository userTrackerRepository;
  @Mock private PluginMessages pluginMessages;

  @BeforeEach
  void setup() {
    this.mockMvc =
        standaloneSetup(new ReportCardController(userTrackerRepository, course, pluginMessages))
            .build();
    when(pluginMessages.getMessage(anyString())).thenReturn("Test");
  }

  @Test
  @WithMockUser(username = "guest", password = "guest")
  void withLessons() throws Exception {
    when(lesson.getTitle()).thenReturn("Test");
    when(course.getTotalOfLessons()).thenReturn(1);
    when(course.getTotalOfAssignments()).thenReturn(10);
    when(course.getLessons()).thenAnswer(x -> List.of(lesson));
    when(userTrackerRepository.findByUser(any())).thenReturn(userTracker);
    when(userTracker.getLessonProgress(any(Lesson.class))).thenReturn(lessonTracker);
    mockMvc
        .perform(MockMvcRequestBuilders.get("/service/reportcard.mvc"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalNumberOfLessons", is(1)))
        .andExpect(jsonPath("$.numberOfAssignmentsSolved", is(0)))
        .andExpect(jsonPath("$.totalNumberOfAssignments", is(10)))
        .andExpect(jsonPath("$.lessonStatistics[0].name", is("Test")))
        .andExpect(jsonPath("$.numberOfAssignmentsSolved", is(0)));
  }
}

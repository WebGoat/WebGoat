package org.owasp.webgoat.service;

import com.beust.jcommander.internal.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.owasp.webgoat.i18n.PluginMessages;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.session.Course;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.users.LessonTracker;
import org.owasp.webgoat.users.UserTracker;
import org.owasp.webgoat.users.UserTrackerRepository;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class ReportCardServiceTest {

    private MockMvc mockMvc;
    @Mock
    private Course course;
    @Mock
    private UserTracker userTracker;
    @Mock
    private AbstractLesson lesson;
    @Mock
    private LessonTracker lessonTracker;
    @Mock
    private UserTrackerRepository userTrackerRepository;
    @Mock
    private WebSession websession;
    @Mock
    private PluginMessages pluginMessages;

    @Before
    public void setup() {
        this.mockMvc = standaloneSetup(new ReportCardService(websession, userTrackerRepository, course, pluginMessages)).build();
        when(pluginMessages.getMessage(anyString())).thenReturn("Test");
    }

    @Test
    @WithMockUser(username = "guest", password = "guest")
    public void withLessons() throws Exception {
        when(lesson.getTitle()).thenReturn("Test");
        when(course.getTotalOfLessons()).thenReturn(1);
        when(course.getTotalOfAssignments()).thenReturn(10);
        when(course.getLessons()).thenReturn(Lists.newArrayList(lesson));
        when(userTrackerRepository.findByUser(anyString())).thenReturn(userTracker);
        when(userTracker.getLessonTracker(any(AbstractLesson.class))).thenReturn(lessonTracker);
        mockMvc.perform(MockMvcRequestBuilders.get("/service/reportcard.mvc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalNumberOfLessons", is(1)))
                .andExpect(jsonPath("$.solvedLessons", is(0)))
                .andExpect(jsonPath("$.numberOfAssignmentsSolved", is(0)))
                .andExpect(jsonPath("$.totalNumberOfAssignments", is(10)))
                .andExpect(jsonPath("$.lessonStatistics[0].name", is("Test")))
                .andExpect(jsonPath("$.numberOfAssignmentsSolved", is(0)));
    }
}

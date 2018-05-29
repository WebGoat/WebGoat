package org.owasp.webgoat.service;

import com.beust.jcommander.internal.Lists;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.NewLesson;
import org.owasp.webgoat.session.Course;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.users.LessonTracker;
import org.owasp.webgoat.users.UserTracker;
import org.owasp.webgoat.users.UserTrackerRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.owasp.webgoat.service.LessonMenuService.URL_LESSONMENU_MVC;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * @author nbaars
 * @since 4/16/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class LessonMenuServiceTest {

    @Mock
    private Course course;
    @Mock
    private UserTracker userTracker;
    @Mock
    private UserTrackerRepository userTrackerRepository;
    @Mock
    private WebSession webSession;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = standaloneSetup(new LessonMenuService(course, webSession, userTrackerRepository)).build();
    }

    @Test
    public void lessonsShouldBeOrdered() throws Exception {
        NewLesson l1 = Mockito.mock(NewLesson.class);
        NewLesson l2 = Mockito.mock(NewLesson.class);
        when(l1.getTitle()).thenReturn("ZA");
        when(l2.getTitle()).thenReturn("AA");
        when(l1.getCategory()).thenReturn(Category.ACCESS_CONTROL);
        when(l2.getCategory()).thenReturn(Category.ACCESS_CONTROL);
        LessonTracker lessonTracker = Mockito.mock(LessonTracker.class);
        when(lessonTracker.isLessonSolved()).thenReturn(false);
        when(course.getLessons(any())).thenReturn(Lists.newArrayList(l1, l2));
        when(course.getCategories()).thenReturn(Lists.newArrayList(Category.ACCESS_CONTROL));
        when(userTracker.getLessonTracker(any(AbstractLesson.class))).thenReturn(lessonTracker);
        when(userTrackerRepository.findByUser(anyString())).thenReturn(userTracker);

        mockMvc.perform(MockMvcRequestBuilders.get(URL_LESSONMENU_MVC))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].children[0].name", CoreMatchers.is("AA")))
                .andExpect(jsonPath("$[0].children[1].name", CoreMatchers.is("ZA")));
    }

    @Test
    public void lessonCompleted() throws Exception {
        NewLesson l1 = Mockito.mock(NewLesson.class);
        when(l1.getTitle()).thenReturn("ZA");
        when(l1.getCategory()).thenReturn(Category.ACCESS_CONTROL);
        LessonTracker lessonTracker = Mockito.mock(LessonTracker.class);
        when(lessonTracker.isLessonSolved()).thenReturn(true);
        when(course.getLessons(any())).thenReturn(Lists.newArrayList(l1));
        when(course.getCategories()).thenReturn(Lists.newArrayList(Category.ACCESS_CONTROL));
        when(userTracker.getLessonTracker(any(AbstractLesson.class))).thenReturn(lessonTracker);
        when(userTrackerRepository.findByUser(anyString())).thenReturn(userTracker);


        mockMvc.perform(MockMvcRequestBuilders.get(URL_LESSONMENU_MVC))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$[0].children[0].complete", CoreMatchers.is(true)));
    }
}
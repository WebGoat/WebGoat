/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.httpproxies;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(MockitoExtension.class)
public class HttpBasicsQuizTest extends LessonTest {

    @Test
  void quizSuccess() throws Exception {
      mockMvc
          .perform(
                  MockMvcRequestBuilders.post("/HttpBasics/quiz")
                  .header("Content-Type", "application/x-www-form-urlencoded")
                  .content("question_0_solution=Solution+2%3A+Hyper+text+transfer+protocol&question_1_solution=Solution+3%3A+To+identify+the+status+of+a+client's+request&question_2_solution=Solution+1%3A+GET&question_3_solution=Solution+2%3A+False&question_4_solution=Solution+3%3A+To+store+state+on+a+client's+browser+across+multiple+HTTP+requests&question_5_solution=Solution+4%3A+Certificate+authorities&question_6_solution=Solution+4%3A+All+of+the+above")
                  )

        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
  }

    @Test
  void quizFail() throws Exception {
      mockMvc
          .perform(
                  MockMvcRequestBuilders.post("/HttpBasics/quiz")
                  .header("Content-Type", "application/x-www-form-urlencoded")
                  .content("question_0_solution=Solution+9%3A+Hyper+text+transfer+protocol&question_1_solution=Solution+3%3A+To+identify+the+status+of+a+client's+request&question_2_solution=Solution+1%3A+GET&question_3_solution=Solution+2%3A+False&question_4_solution=Solution+3%3A+To+store+state+on+a+client's+browser+across+multiple+HTTP+requests&question_5_solution=Solution+4%3A+Certificate+authorities&question_6_solution=Solution+4%3A+All+of+the+above")
                  )

        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
  }

}

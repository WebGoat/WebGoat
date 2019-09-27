package org.owasp.webgoat.deserialization;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.Serializable;

import org.dummy.insecure.framework.DummySerializable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.owasp.webgoat.plugins.LessonTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
public class DeserializeTest extends LessonTest {

	@Before
	public void setup() {
		InsecureDeserialization insecureDeserializationTest = new InsecureDeserialization();
		when(webSession.getCurrentLesson()).thenReturn(insecureDeserializationTest);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		when(webSession.getUserName()).thenReturn("unit-test");
	}

	@Test
	public void solveAssignment() throws Exception {

		try {
			DummySerializable someObject = new DummySerializable("test", 20);

			String token = SerializationHelper.toString((Serializable) someObject);
			mockMvc.perform(
					MockMvcRequestBuilders.post("/InsecureDeserialization/task").param("token", token).content(""))
					.andExpect(status().isOk()).andExpect(jsonPath("$.lessonCompleted", is(true)));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
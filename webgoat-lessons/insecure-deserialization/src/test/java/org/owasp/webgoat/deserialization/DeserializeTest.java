package org.owasp.webgoat.deserialization;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.dummy.insecure.framework.DummySerializable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.owasp.webgoat.assignments.AssignmentEndpointTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(MockitoJUnitRunner.class)
public class DeserializeTest extends AssignmentEndpointTest {

	private MockMvc mockMvc;
	
	private static String OS = System.getProperty("os.name").toLowerCase();
	
	@Before
    public void setup() {
        InsecureDeserializationTask insecureTask = new InsecureDeserializationTask();
        init(insecureTask);
        this.mockMvc = standaloneSetup(insecureTask).build();
        when(webSession.getCurrentLesson()).thenReturn(new InsecureDeserialization());
    }

    @Test
    public void success() throws Exception {
    	if (OS.indexOf("win")>-1) {
    		mockMvc.perform(MockMvcRequestBuilders.post("/InsecureDeserialization/task")
                    .header("x-request-intercepted", "true")
                    .param("token", SerializationHelper.toString(new DummySerializable("wait", "ping localhost -n 5"))))
            		.andExpect(status().isOk()).andExpect(jsonPath("$.lessonCompleted", is(true)));
    	} else {
    		mockMvc.perform(MockMvcRequestBuilders.post("/InsecureDeserialization/task")
                .header("x-request-intercepted", "true")
                .param("token", SerializationHelper.toString(new DummySerializable("wait", "sleep 5"))))
        		.andExpect(status().isOk()).andExpect(jsonPath("$.lessonCompleted", is(true)));
    	}
    }
    
    @Test
    public void fail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/InsecureDeserialization/task")
                .header("x-request-intercepted", "true")
                .param("token", SerializationHelper.toString(new DummySerializable("delete", "rm *"))))
        		.andExpect(status().isOk()).andExpect(jsonPath("$.lessonCompleted", is(false)));
    }

}
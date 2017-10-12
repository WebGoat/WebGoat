package org.owasp.webgoat.plugin;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.owasp.webgoat.users.UserService;
import org.owasp.webgoat.users.WebGoatUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class MissingFunctionACUsersTest {
    private MockMvc mockMvc;
    @Mock
    private UserService userService;

    @Before
    public void setup() {
        MissingFunctionACUsers usersController = new MissingFunctionACUsers();
        this.mockMvc = standaloneSetup(usersController).build();
        ReflectionTestUtils.setField(usersController,"userService",userService);
        when(userService.getAllUsers()).thenReturn(getUsersList());
    }

    @Test
    public void TestContentTypeApplicationJSON () throws  Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                .header("Content-type","application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username", CoreMatchers.is("user1")))
                .andExpect(jsonPath("$[0].userHash",CoreMatchers.is("cplTjehjI/e5ajqTxWaXhU5NW9UotJfXj+gcbPvfWWc=")))
                .andExpect(jsonPath("$[1].admin",CoreMatchers.is(true)));

    }

    private List<WebGoatUser> getUsersList() {
        List <WebGoatUser> tempUsers = new ArrayList<>();
        tempUsers.add(new WebGoatUser("user1","password1"));
        tempUsers.add(new WebGoatUser("user2","password2","WEBGOAT_ADMIN"));
        tempUsers.add(new WebGoatUser("user3","password3", "WEBGOAT_USER"));
        return tempUsers;
    }



}

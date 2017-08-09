package org.owasp.webgoat.plugin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.owasp.webgoat.users.WebGoatUser;

@RunWith(MockitoJUnitRunner.class)
public class DisplayUserTest {

    @Test
    public void TestDisplayUserCreation() {
        DisplayUser displayUser = new DisplayUser(new WebGoatUser("user1","password1"));
        assert(!displayUser.isAdmin());
    }

    @Test
    public void TesDisplayUserHash() {
        DisplayUser displayUser = new DisplayUser(new WebGoatUser("user1","password1"));
        assert(displayUser.getUserHash().equals("cplTjehjI/e5ajqTxWaXhU5NW9UotJfXj+gcbPvfWWc="));
    }
}

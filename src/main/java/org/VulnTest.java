package org;

public class VulnTest {
public void test(String userId) {
        String query = "SELECT * FROM users WHERE id=" + userId;
        System.out.println(query);
    }
}

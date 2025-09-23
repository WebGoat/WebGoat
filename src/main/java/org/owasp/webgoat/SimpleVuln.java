public class SimpleVuln {
    public void sqlInject(String input) {
        String sql = "SELECT * FROM users WHERE id = " + input;
        System.out.println(sql);
    }
}

package org.owasp.webgoat.vulnerable_components;


import com.thoughtworks.xstream.XStream;
import org.owasp.webgoat.LessonDataSource;
import org.springframework.web.bind.annotation.*;

import java.sql.*;

/** Handle contact management */
@RestController
public final class ContactController {

    private final LessonDataSource dataSource;

    public ContactController(LessonDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/get-contact-phone")
    public @ResponseBody
    String getContactPhone(@RequestParam String userId) throws SQLException {
      // get the phone number from the database
      Connection conn = dataSource.getConnection();
      String sql = "select phone from contacts where userid = '" + userId + "'";
      Statement statement = conn.createStatement();
      ResultSet rs = statement.executeQuery(sql);
      if(!rs.next()) {
         throw new IllegalArgumentException("invalid contact");
      }
      return rs.getString("phone");
    }

    @GetMapping("/update-contact")
    public @ResponseBody
    void updateContact(@RequestBody String xml) throws SQLException {
        // get the xml from our partner to update our contact record
        Connection connection = dataSource.getConnection();
        XStream xstream = new XStream();
        Contact contact = (Contact) xstream.fromXML(xml);
        String sql = "update contacts set phone = ? where userid = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, contact.name);
        stmt.setString(2, contact.phone);
        stmt.executeUpdate();
    }

    private static class Contact {
        private String name;
        private String phone;
    }
}

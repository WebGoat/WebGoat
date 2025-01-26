
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.jdbc.core.JdbcTemplate;

@RestController
public class VulnerableController {

    private final JdbcTemplate jdbcTemplate;

    public VulnerableController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/vulnerable-endpoint")
    public String vulnerableEndpoint(@RequestParam String param) {
        String sql = "SELECT * FROM users WHERE username = '" + param + "'";
        // This is where the SQL Injection can occur
        return jdbcTemplate.queryForObject(sql, String.class);
    }
}

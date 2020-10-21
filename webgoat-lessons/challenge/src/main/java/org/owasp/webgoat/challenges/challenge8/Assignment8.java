package org.owasp.webgoat.challenges.challenge8;

import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.challenges.Flag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author nbaars
 * @since 4/8/17.
 */
@RestController
@Slf4j
public class Assignment8 extends AssignmentEndpoint {

    private static final Map<Integer, Integer> votes = new HashMap<>();

    static {
        votes.put(1, 400);
        votes.put(2, 120);
        votes.put(3, 140);
        votes.put(4, 150);
        votes.put(5, 300);
    }

    @GetMapping(value = "/challenge/8/vote/{stars}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> vote(@PathVariable(value = "stars") int nrOfStars, HttpServletRequest request) {
        //Simple implementation of VERB Based Authentication
        String msg = "";
        if (request.getMethod().equals("GET")) {
            var json = Map.of("error", true, "message", "Sorry but you need to login first in order to vote");
            return ResponseEntity.status(200).body(json);
        }
        Integer allVotesForStar = votes.getOrDefault(nrOfStars, 0);
        votes.put(nrOfStars, allVotesForStar + 1);
        return ResponseEntity.ok().header("X-Flag", "Thanks for voting, your flag is: " + Flag.FLAGS.get(8)).build();
    }

    @GetMapping("/challenge/8/votes/")
    public ResponseEntity<?> getVotes() {
        return ResponseEntity.ok(votes.entrySet().stream().collect(Collectors.toMap(e -> "" + e.getKey(), e -> e.getValue())));
    }

    @GetMapping("/challenge/8/votes/average")
    public ResponseEntity<Map<String, Integer>> average() {
        int totalNumberOfVotes = votes.values().stream().mapToInt(i -> i.intValue()).sum();
        int categories = votes.entrySet().stream().mapToInt(e -> e.getKey() * e.getValue()).reduce(0, (a, b) -> a + b);
        var json = Map.of("average", (int) Math.ceil((double) categories / totalNumberOfVotes));
        return ResponseEntity.ok(json);
    }

    @GetMapping("/challenge/8/notUsed")
    public AttackResult notUsed() {
        throw new IllegalStateException("Should never be called, challenge specific method");
    }
}


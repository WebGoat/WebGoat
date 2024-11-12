package org.owasp.webgoat.container.users;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.owasp.webgoat.container.i18n.PluginMessages;
import org.owasp.webgoat.container.lessons.Lesson;
import org.owasp.webgoat.container.session.Course;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Temp endpoint just for the CTF.
 *
 * @author nbaars
 * @since 3/23/17.
 */
@RestController
@AllArgsConstructor
public class Scoreboard {

  private final UserProgressRepository userTrackerRepository;
  private final UserRepository userRepository;
  private final Course course;
  private final PluginMessages pluginMessages;

  @AllArgsConstructor
  @Getter
  private class Ranking {
    private String username;
    private List<String> flagsCaptured;
  }

  @GetMapping("/scoreboard-data")
  public List<Ranking> getRankings() {
    return userRepository.findAll().stream()
        .filter(user -> !user.getUsername().startsWith("csrf-"))
        .map(
            user ->
                new Ranking(
                    user.getUsername(),
                    challengesSolved(userTrackerRepository.findByUser(user.getUsername()))))
        .sorted((o1, o2) -> o2.getFlagsCaptured().size() - o1.getFlagsCaptured().size())
        .collect(Collectors.toList());
  }

  private List<String> challengesSolved(UserProgress userTracker) {
    List<String> challenges =
        List.of(
            "Challenge1",
            "Challenge2",
            "Challenge3",
            "Challenge4",
            "Challenge5",
            "Challenge6",
            "Challenge7",
            "Challenge8",
            "Challenge9");
    return challenges.stream()
        .map(userTracker::getLessonProgress)
        .flatMap(Optional::stream)
        .filter(LessonProgress::isLessonSolved)
        .map(LessonProgress::getLessonName)
        .map(this::toLessonTitle)
        .toList();
  }

  private String toLessonTitle(String id) {
    String titleKey =
        course.getLessons().stream()
            .filter(l -> l.getId().equals(id))
            .findFirst()
            .map(Lesson::getTitle)
            .orElse("No title");
    return pluginMessages.getMessage(titleKey, titleKey);
  }
}

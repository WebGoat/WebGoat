package org.owasp.webgoat.lessons.path_traversal;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Base64;

@RestController
@AssignmentHints({
        "path-traversal-profile-retrieve.hint1",
        "path-traversal-profile-retrieve.hint2",
        "path-traversal-profile-retrieve.hint3",
        "path-traversal-profile-retrieve.hint4",
        "path-traversal-profile-retrieve.hint5",
        "path-traversal-profile-retrieve.hint6"})
@Slf4j
public class ProfileUploadRetrieval extends AssignmentEndpoint {

    private final File catPicturesDirectory;

    public ProfileUploadRetrieval(@Value("${webgoat.server.directory}") String webGoatHomeDirectory) {
        this.catPicturesDirectory = new File(webGoatHomeDirectory, "/PathTraversal/" + "/cats");
        this.catPicturesDirectory.mkdirs();
    }

    @PostConstruct
    public void initAssignment() {
        for (int i = 1; i <= 10; i++) {
            try (InputStream is = new ClassPathResource("lessons/path_traversal/images/cats/" + i + ".jpg").getInputStream()) {
                FileCopyUtils.copy(is, new FileOutputStream(new File(catPicturesDirectory, i + ".jpg")));
            } catch (Exception e) {
                log.error("Unable to copy pictures" + e.getMessage());
            }
        }
        var secretDirectory = this.catPicturesDirectory.getParentFile().getParentFile();
        try {
            Files.writeString(secretDirectory.toPath().resolve("path-traversal-secret.jpg"), "You found it submit the SHA-512 hash of your username as answer");
        } catch (IOException e) {
            log.error("Unable to write secret in: {}", secretDirectory, e);
        }
    }

    @PostMapping("/PathTraversal/random")
    @ResponseBody
    public AttackResult execute(@RequestParam(value = "secret", required = false) String secret) {
        if (Sha512DigestUtils.shaHex(getWebSession().getUserName()).equalsIgnoreCase(secret)) {
            return success(this).build();
        }
        return failed(this).build();
    }

    @GetMapping("/PathTraversal/random-picture")
    @ResponseBody
    public ResponseEntity<?> getProfilePicture(HttpServletRequest request) {
        var queryParams = request.getQueryString();
        if (queryParams != null && (queryParams.contains("..") || queryParams.contains("/"))) {
            return ResponseEntity.badRequest().body("Illegal characters are not allowed in the query params");
        }
        try {
            var id = request.getParameter("id");
            var catPicture = new File(catPicturesDirectory, (id == null ? RandomUtils.nextInt(1, 11) : id) + ".jpg");

            if (catPicture.getName().toLowerCase().contains("path-traversal-secret.jpg")) {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(MediaType.IMAGE_JPEG_VALUE))
                        .body(FileCopyUtils.copyToByteArray(catPicture));
            }
            if (catPicture.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(MediaType.IMAGE_JPEG_VALUE))
                        .location(new URI("/PathTraversal/random-picture?id=" + catPicture.getName()))
                        .body(Base64.getEncoder().encode(FileCopyUtils.copyToByteArray(catPicture)));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .location(new URI("/PathTraversal/random-picture?id=" + catPicture.getName()))
                    .body(StringUtils.arrayToCommaDelimitedString(catPicture.getParentFile().listFiles()).getBytes());
        } catch (IOException | URISyntaxException e) {
            log.error("Image not found", e);
        }

        return ResponseEntity.badRequest().build();
    }
}

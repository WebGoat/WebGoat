package org.owasp.webgoat.lessons.path_traversal;

import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.session.WebSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@AssignmentHints({"path-traversal-profile-remove-user-input.hint1", "path-traversal-profile-remove-user-input.hint2", "path-traversal-profile-remove-user-input.hint3"})
public class ProfileUploadRemoveUserInput extends ProfileUploadBase {

    public ProfileUploadRemoveUserInput(@Value("${webgoat.server.directory}") String webGoatHomeDirectory, WebSession webSession) {
        super(webGoatHomeDirectory, webSession);
    }

    @PostMapping(value = "/PathTraversal/profile-upload-remove-user-input", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public AttackResult uploadFileHandler(@RequestParam("uploadedFileRemoveUserInput") MultipartFile file) {
        return super.execute(file, file.getOriginalFilename());
    }
}

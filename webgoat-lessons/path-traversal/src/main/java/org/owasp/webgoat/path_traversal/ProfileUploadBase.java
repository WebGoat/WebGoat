package org.owasp.webgoat.path_traversal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.WebSession;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;

@AllArgsConstructor
@Getter
public class ProfileUploadBase extends AssignmentEndpoint {

    private String webGoatHomeDirectory;
    private WebSession webSession;

    protected AttackResult execute(MultipartFile file, String fullName) {
        if (file.isEmpty()) {
            return failed(this).feedback("path-traversal-profile-empty-file").build();
        }
        if (StringUtils.isEmpty(fullName)) {
            return failed(this).feedback("path-traversal-profile-empty-name").build();
        }

        var uploadDirectory = new File(this.webGoatHomeDirectory, "/PathTraversal/" + webSession.getUserName());
        if (uploadDirectory.exists()) {
            FileSystemUtils.deleteRecursively(uploadDirectory);
        }

        try {
            uploadDirectory.mkdirs();
            var uploadedFile = new File(uploadDirectory, fullName);
            uploadedFile.createNewFile();
            FileCopyUtils.copy(file.getBytes(), uploadedFile);

            if (attemptWasMade(uploadDirectory, uploadedFile)) {
                return solvedIt(uploadedFile);
            }
            return informationMessage(this).feedback("path-traversal-profile-updated").feedbackArgs(uploadedFile.getAbsoluteFile()).build();

        } catch (IOException e) {
            return failed(this).output(e.getMessage()).build();
        }
    }

    private boolean attemptWasMade(File expectedUploadDirectory, File uploadedFile) throws IOException {
        return !expectedUploadDirectory.getCanonicalPath().equals(uploadedFile.getParentFile().getCanonicalPath());
    }

    private AttackResult solvedIt(File uploadedFile) throws IOException {
        if (uploadedFile.getCanonicalFile().getParentFile().getName().endsWith("PathTraversal")) {
            return success(this).build();
        }
        return failed(this).attemptWasMade().feedback("path-traversal-profile-attempt").feedbackArgs(uploadedFile.getCanonicalPath()).build();
    }

    public ResponseEntity<?> getProfilePicture() {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(MediaType.IMAGE_JPEG_VALUE))
                .body(getProfilePictureAsBase64());
    }

    protected byte[] getProfilePictureAsBase64() {
        var profilePictureDirectory = new File(this.webGoatHomeDirectory, "/PathTraversal/" + webSession.getUserName());
        var profileDirectoryFiles = profilePictureDirectory.listFiles();

        if (profileDirectoryFiles != null && profileDirectoryFiles.length > 0) {
            try (var inputStream = new FileInputStream(profileDirectoryFiles[0])) {
                return Base64.getEncoder().encode(FileCopyUtils.copyToByteArray(inputStream));
            } catch (IOException e) {
                return defaultImage();
            }
        } else {
            return defaultImage();
        }
    }

    @SneakyThrows
    private byte[] defaultImage() {
        var inputStream = getClass().getResourceAsStream("/images/account.png");
        return Base64.getEncoder().encode(FileCopyUtils.copyToByteArray(inputStream));
    }
}

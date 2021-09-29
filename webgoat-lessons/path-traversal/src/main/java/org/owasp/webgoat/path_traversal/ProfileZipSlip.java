package org.owasp.webgoat.path_traversal;

import lombok.SneakyThrows;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.WebSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@AssignmentHints({"path-traversal-zip-slip.hint1", "path-traversal-zip-slip.hint2", "path-traversal-zip-slip.hint3", "path-traversal-zip-slip.hint4"})
public class ProfileZipSlip extends ProfileUploadBase {

    public ProfileZipSlip(@Value("${webgoat.server.directory}") String webGoatHomeDirectory, WebSession webSession) {
        super(webGoatHomeDirectory, webSession);
    }

    @PostMapping(value = "/PathTraversal/zip-slip", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public AttackResult uploadFileHandler(@RequestParam("uploadedFileZipSlip") MultipartFile file) {
        if (!file.getOriginalFilename().toLowerCase().endsWith(".zip")) {
            return failed(this).feedback("path-traversal-zip-slip.no-zip").build();
        } else {
            return processZipUpload(file);
        }
    }

    @SneakyThrows
    private AttackResult processZipUpload(MultipartFile file) {
        var tmpZipDirectory = Files.createTempDirectory(getWebSession().getUserName());
        var uploadDirectory = new File(getWebGoatHomeDirectory(), "/PathTraversal/" + getWebSession().getUserName());
        var currentImage = getProfilePictureAsBase64();

        Files.createDirectories(uploadDirectory.toPath());

        try {
            var uploadedZipFile = tmpZipDirectory.resolve(file.getOriginalFilename());
            FileCopyUtils.copy(file.getBytes(), uploadedZipFile.toFile());

            ZipFile zip = new ZipFile(uploadedZipFile.toFile());
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry e = entries.nextElement();
                File f = new File(tmpZipDirectory.toFile(), e.getName());
                InputStream is = zip.getInputStream(e);
                Files.copy(is, f.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            return isSolved(currentImage, getProfilePictureAsBase64());
        } catch (IOException e) {
            return failed(this).output(e.getMessage()).build();
        }
    }

    private AttackResult isSolved(byte[] currentImage, byte[] newImage) {
        if (Arrays.equals(currentImage, newImage)) {
            return failed(this).output("path-traversal-zip-slip.extracted").build();
        }
        return success(this).output("path-traversal-zip-slip.extracted").build();
    }

    @GetMapping("/PathTraversal/zip-slip/")
    @ResponseBody
    public ResponseEntity<?> getProfilePicture() {
        return super.getProfilePicture();
    }

    @GetMapping("/PathTraversal/zip-slip/profile-image/{username}")
    @ResponseBody
    public ResponseEntity<?> getProfilePicture(@PathVariable("username") String username) {
        return ResponseEntity.notFound().build();
    }

}

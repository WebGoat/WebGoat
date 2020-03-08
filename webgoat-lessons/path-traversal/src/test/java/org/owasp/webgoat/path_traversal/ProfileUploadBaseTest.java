package org.owasp.webgoat.path_traversal;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.owasp.webgoat.plugins.LessonTest;
import org.owasp.webgoat.session.WebSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProfileUploadBaseTest extends LessonTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private File folder;
    @Autowired
    private PathTraversal pathTraversal;


    @Before
    public void setup() throws IOException {
        this.folder = temporaryFolder.newFolder();
    }

    @Test
    public void shouldNotOverwriteExistingFile() throws IOException {
        var existingFile = new File(folder, "test.jpg").createNewFile();
        var profilePicture = new MockMultipartFile("uploadedFileFix", "../picture.jpg", "text/plain", "an image".getBytes());
        new ProfileUploadBase(this.folder.getPath(), this.webSession).execute(profilePicture, "test.jpg");
    }

}
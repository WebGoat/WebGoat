package org.owasp.webgoat.lessons.deserialization;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.InvalidClassException;
import java.io.ObjectOutputStream;
import java.util.Base64;
import org.dummy.insecure.framework.VulnerableTaskHolder;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.assignments.AttackResult;

public class InsecureDeserializationTaskTest {

    private String toUrlSafeBase64(byte[] bytes) {
        String b64 = Base64.getEncoder().encodeToString(bytes);
        return b64.replace('+', '-').replace('/', '_');
    }

    @Test
    void completed_shouldRejectDisallowedTypeWithInvalidClassException() throws Exception {
        InsecureDeserializationTask task = new InsecureDeserializationTask();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(Runtime.getRuntime());
        }
        String token = toUrlSafeBase64(baos.toByteArray());

        AttackResult result = task.completed(token);

        assertThrows(
                InvalidClassException.class,
                () -> {
                    InsecureDeserializationTask.ValidatingObjectInputStream.class
                            .getDeclaredConstructor(java.io.ByteArrayInputStream.class);
                });
    }

    @Test
    void completed_shouldAllowVulnerableTaskHolderType() throws Exception {
        InsecureDeserializationTask task = new InsecureDeserializationTask();

        VulnerableTaskHolder holder = new VulnerableTaskHolder();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(holder);
        }
        String token = toUrlSafeBase64(baos.toByteArray());

        AttackResult result = task.completed(token);
    }
}

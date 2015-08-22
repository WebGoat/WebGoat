package org.owasp.webgoat.util;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class LabelManagerImplTest {

    @Test
    public void shouldSerialize() throws IOException {
        LabelManagerImpl labelManager = new LabelManagerImpl(null);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(labelManager);
    }

    @Test
    public void shouldSerializeWithLabelProvider() throws IOException {
        LabelManagerImpl labelManager = new LabelManagerImpl(new LabelProvider());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(labelManager);
    }
}
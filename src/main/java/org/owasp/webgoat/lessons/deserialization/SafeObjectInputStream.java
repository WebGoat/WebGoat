package org.owasp.webgoat.lessons.deserialization;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * SafeObjectInputStream is a custom ObjectInputStream that prevents unsafe deserialization
 * attacks by restricting which classes are allowed to be deserialized.
 * 
 * This class maintains a whitelist of permitted classes and throws an InvalidClassException
 * if an attempt is made to deserialize any class not on the whitelist. This protects against
 * arbitrary code execution vulnerabilities that can occur when untrusted data is deserialized.
 * 
 * The class also provides a static utility method to validate that a byte array represents
 * a genuine Java serialized object stream.
 * 
 * @author OWASP WebGoat
 * @version 1.0
 */
public class SafeObjectInputStream extends ObjectInputStream {

    /**
     * ALLOWED_CLASSES is a Set containing the fully qualified names of all classes
     * that are permitted to be deserialized. Any attempt to deserialize a class
     * not in this set will result in an InvalidClassException.
     */
    private static final Set<String> ALLOWED_CLASSES = new HashSet<>();

    /**
     * Static initializer block that populates the ALLOWED_CLASSES set.
     * This block executes once when the class is first loaded.
     * 
     * The array of strings contains the fully qualified class names that are
     * safe to deserialize. These are typically classes that do not have
     * dangerous behavior in their readObject() or readResolve() methods.
     */
    static {
        String[] allowedClassNames = new String[] {
            "org.dummy.insecure.framework.VulnerableTaskHolder",
            "java.lang.String",
            "java.lang.Integer",
            "java.lang.Long",
            "java.util.ArrayList",
            "java.util.HashMap"
        };

        for (int i = 0; i < allowedClassNames.length; i++) {
            ALLOWED_CLASSES.add(allowedClassNames[i]);
        }
    }

    /**
     * Constructs a SafeObjectInputStream that wraps the provided input stream.
     * 
     * The constructed stream will use this SafeObjectInputStream's resolveClass() method
     * to validate all classes during deserialization, ensuring only whitelisted classes
     * are deserialized.
     * 
     * @param inputStream the underlying InputStream to be wrapped by this SafeObjectInputStream
     * @throws IOException if an I/O error occurs while reading the stream header, or if
     *         the stream does not have a valid serialization header
     */
    public SafeObjectInputStream(InputStream inputStream) throws IOException {
        super(inputStream);
    }

    /**
     * Overrides the resolveClass method from ObjectInputStream to enforce whitelist validation.
     * 
     * Before deserializing any class, this method checks whether the class name exists in the
     * ALLOWED_CLASSES set. If the class is not whitelisted, an InvalidClassException is thrown.
     * If the class is whitelisted, deserialization proceeds by calling the parent class's
     * resolveClass method.
     * 
     * This method is called internally by the ObjectInputStream during the deserialization
     * process whenever a class object needs to be resolved.
     * 
     * @param desc an ObjectStreamClass object that describes the class being deserialized,
     *             including its fully qualified name and other metadata
     * @return the resolved Class object corresponding to the class name in desc, if it is
     *         in the whitelist
     * @throws InvalidClassException if the class name is not in the ALLOWED_CLASSES whitelist,
     *         indicating that deserialization of this class has been blocked for security reasons
     * @throws IOException if an I/O error occurs while processing the stream
     * @throws ClassNotFoundException if the whitelisted class cannot be found by the class loader
     */
    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc)
            throws IOException, ClassNotFoundException {
        
        String className = desc.getName();

        if (!ALLOWED_CLASSES.contains(className)) {
            throw new InvalidClassException(
                    "Deserialization blocked: class not in whitelist: " + className);
        }

        return super.resolveClass(desc);
    }

    /**
     * Checks whether the provided byte array represents a valid Java serialized object stream.
     * 
     * This method validates the presence of the Java serialization magic number, which consists
     * of two specific bytes at the beginning of all Java serialized objects:
     * - First byte (magic): 0xAC
     * - Second byte (version): 0xED
     * 
     * These magic bytes are a standard marker that indicates the data has been serialized using
     * Java's ObjectOutputStream. This check helps prevent attempts to deserialize non-serialized
     * or corrupted data.
     * 
     * The method first checks that the data array is not null and contains at least 2 bytes,
     * then verifies that the first two bytes match the expected magic number.
     * 
     * @param data the byte array to be checked for valid Java serialization magic bytes
     * @return true if the data array is at least 2 bytes long and begins with the correct
     *         Java serialization magic bytes (0xAC 0xED); false otherwise
     */
    public static boolean isSafeSerializedStream(byte[] data) {
        
        if (data == null) {
            return false;
        }

        if (data.length < 2) {
            return false;
        }

        int firstByte = data[0] & 0xFF;
        int secondByte = data[1] & 0xFF;

        if (firstByte == 0xAC && secondByte == 0xED) {
            return true;
        }

        return false;
    }
}

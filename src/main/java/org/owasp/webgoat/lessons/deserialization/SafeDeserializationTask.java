package org.owasp.webgoat.lessons.deserialization;

import org.dummy.insecure.framework.VulnerableTaskHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.InvalidClassException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Safe Deserialization Endpoint — Module 5 demonstration.
 * 
 * This REST controller demonstrates secure deserialization practices by:
 * 1. Validating the binary magic bytes of Java serialized streams (0xAC 0xED)
 * 2. Using a whitelist-based deserialization approach via SafeObjectInputStream
 * 3. Performing explicit type checking on deserialized objects
 * 4. Providing detailed feedback for each step of the validation process
 * 
 * The endpoint protects against deserialization attacks by ensuring only
 * trusted object types (VulnerableTaskHolder) can be deserialized.
 * 
 * @author WebGoat Security Team
 * @version 1.0
 */
@RestController
public class SafeDeserializationTask {

    /**
     * Handles POST requests for safe deserialization validation.
     * 
     * This method processes Base64-encoded serialized Java objects and validates them
     * through multiple security checks before allowing deserialization. The process
     * includes magic byte validation, whitelist-based deserialization, and type checking.
     * 
     * @param token A Base64-encoded string containing the serialized object data
     * @return ResponseEntity containing a Map with success status and output message.
     *         The Map includes:
     *         - "success" (Boolean): true if deserialization was successful and secure, false otherwise
     *         - "output" (String): Detailed message describing the result or error
     */
    @PostMapping("/InsecureDeserialization/safe-task")
    public ResponseEntity<Map<String, Object>> submitTask(@RequestParam String token) {
        // Initialize result map to store response data
        Map<String, Object> result = new HashMap<>();
        
        try {
            // ============================================================================
            // STEP 1: BASE64 DECODE THE INPUT TOKEN
            // ============================================================================
            // The token parameter is expected to be Base64-encoded serialized data.
            // We decode it to get the raw binary bytes for validation.
            byte[] decodedBytes = Base64.getDecoder().decode(token);
            
            // ============================================================================
            // STEP 2: VALIDATE MAGIC BYTES (0xAC 0xED)
            // ============================================================================
            // Java serialized streams always begin with the magic bytes 0xAC 0xED.
            // This is a fundamental characteristic of Java serialized objects.
            // We check this first to quickly reject non-serialized data.
            // This helps prevent deserialization attacks from malformed or spoofed data.
            
            boolean isSafeStream = SafeObjectInputStream.isSafeSerializedStream(decodedBytes);
            
            // If the magic bytes are not present, this is not a valid Java serialized stream
            if (!isSafeStream) {
                // Create error message with diagnostic information
                result.put("success", false);
                
                // Build output message explaining what went wrong
                String outputMessage = "BLOCKED: Not a valid Java serialized stream. " +
                        "Missing magic bytes 0xAC 0xED. ";
                
                // Add diagnostic information about the actual first bytes received
                if (decodedBytes.length > 0) {
                    String firstByteHex = String.format("0x%02X", decodedBytes[0]);
                    outputMessage = outputMessage + "First bytes: " + firstByteHex;
                } else {
                    outputMessage = outputMessage + "First bytes: empty";
                }
                
                result.put("output", outputMessage);
                return ResponseEntity.ok(result);
            }
            
            // ============================================================================
            // STEP 3: DESERIALIZE USING SAFE OBJECT INPUT STREAM
            // ============================================================================
            // SafeObjectInputStream enforces a whitelist of allowed classes.
            // Only classes in the whitelist can be deserialized, blocking potentially
            // dangerous gadget chains and other attack payloads.
            // We use try-with-resources to ensure streams are properly closed.
            
            Object deserializedObject = null;
            
            try {
                // Create a ByteArrayInputStream from the decoded bytes
                ByteArrayInputStream byteStream = new ByteArrayInputStream(decodedBytes);
                
                // Create a SafeObjectInputStream that enforces the whitelist policy
                SafeObjectInputStream safeStream = new SafeObjectInputStream(byteStream);
                
                // Attempt to deserialize the object
                deserializedObject = safeStream.readObject();
                
                // Close the SafeObjectInputStream
                safeStream.close();
                
                // Close the ByteArrayInputStream
                byteStream.close();
                
            } catch (InvalidClassException invalidClassException) {
                // InvalidClassException is thrown when the whitelist rejects a class
                result.put("success", false);
                result.put("output", "BLOCKED by whitelist: " + invalidClassException.getMessage());
                return ResponseEntity.ok(result);
            }
            
            // ============================================================================
            // STEP 4: TYPE CHECK - VERIFY DESERIALIZED OBJECT TYPE
            // ============================================================================
            // After successful deserialization, we verify the object is of the expected type.
            // The deserialized object must be an instance of VulnerableTaskHolder.
            // This additional type check provides defense in depth.
            
            boolean isCorrectType = deserializedObject instanceof VulnerableTaskHolder;
            
            if (isCorrectType) {
                // Object is of the correct type - deserialization was secure and successful
                result.put("success", true);
                result.put("output", "Safe deserialization successful! " +
                        "Object verified as VulnerableTaskHolder. Whitelist protection working correctly.");
            } else {
                // Object type does not match expected type - reject it
                result.put("success", false);
                
                // Provide diagnostic information about what type was actually received
                String actualTypeName = deserializedObject.getClass().getName();
                result.put("output", "Unexpected object type: " + actualTypeName);
            }
            
        } catch (IllegalArgumentException illegalArgException) {
            // IllegalArgumentException is thrown if Base64 input is malformed
            result.put("success", false);
            result.put("output", "Invalid Base64 input: " + illegalArgException.getMessage());
            
        } catch (Exception genericException) {
            // Catch any other unexpected exceptions during the process
            result.put("success", false);
            result.put("output", "Error: " + genericException.getMessage());
        }
        
        // Return the result map wrapped in a ResponseEntity with HTTP 200 OK status
        return ResponseEntity.ok(result);
    }
}

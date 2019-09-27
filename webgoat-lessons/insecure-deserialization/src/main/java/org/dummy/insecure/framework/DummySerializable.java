package org.dummy.insecure.framework;

import java.io.ObjectInputStream;
import java.io.Serializable;

public class DummySerializable implements Serializable {

	private static final long serialVersionUID = -467856807060328693L;

	private String name;
	private int age;
	
	public DummySerializable(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}
	
	private void readObject( ObjectInputStream stream ) throws Exception {
        try {
            long start = System.currentTimeMillis();
            //Thread.sleep(5000);
            long end = System.currentTimeMillis();
            System.out.println("read bla object performed"+(end-start));
        } catch (Exception e) {

        }
    }
	
}

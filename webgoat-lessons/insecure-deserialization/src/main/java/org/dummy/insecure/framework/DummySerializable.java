package org.dummy.insecure.framework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class DummySerializable implements Serializable {

	private static final long serialVersionUID = -467856807060328693L;

	private String taskName;
	private String taskAction;
	
	public DummySerializable(String taskName, String taskAction) {
		super();
		this.taskName = taskName;
		this.taskAction = taskAction;
	}
	
	/**
	 * Execute a task when de-serializing a saved or received object.
	 * @author stupid develop
	 */
	private void readObject( ObjectInputStream stream ) throws Exception {
        //unserialize data so taskName and taskAction are available
		stream.defaultReadObject();
		
		//do something with the data
		System.out.println("restoring task: "+taskName);
		
		//condition is here to prevent you from destroying the goat altogether
		if ((taskAction.startsWith("sleep")||taskAction.startsWith("ping"))
				&& taskAction.length() < 22) {
		System.out.println("about to execute: "+taskAction);
		try {
            Process p = Runtime.getRuntime().exec(taskAction);
            BufferedReader in = new BufferedReader(
                                new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		}
       
    }
	
}

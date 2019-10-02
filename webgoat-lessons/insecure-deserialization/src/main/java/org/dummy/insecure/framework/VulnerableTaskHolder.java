package org.dummy.insecure.framework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.LocalDateTime;

public class VulnerableTaskHolder implements Serializable {

	private static final long serialVersionUID = 2;

	private String taskName;
	private String taskAction;
	private LocalDateTime requestedExecutionTime;
	
	public VulnerableTaskHolder(String taskName, String taskAction) {
		super();
		this.taskName = taskName;
		this.taskAction = taskAction;
		this.requestedExecutionTime = LocalDateTime.now();
	}
	
	@Override
	public String toString() {
		return "VulnerableTaskHolder [taskName=" + taskName + ", taskAction=" + taskAction + ", requestedExecutionTime="
				+ requestedExecutionTime + "]";
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
		System.out.println("restoring time: "+requestedExecutionTime);
		
		if (requestedExecutionTime!=null && 
				(requestedExecutionTime.isBefore(LocalDateTime.now().minusMinutes(10))
				|| requestedExecutionTime.isAfter(LocalDateTime.now()))) {
			//do nothing is the time is not within 10 minutes after the object has been created
			System.out.println(this.toString());
			throw new IllegalArgumentException("outdated");
		}
		
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

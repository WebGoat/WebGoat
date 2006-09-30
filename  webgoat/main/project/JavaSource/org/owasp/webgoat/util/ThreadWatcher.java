package org.owasp.webgoat.util;

import java.util.BitSet;


/**
 *  Copyright (c) 2002 Free Software Foundation developed under the custody of
 *  the Open Web Application Security Project (http://www.owasp.org) This
 *  software package org.owasp.webgoat.is published by OWASP under the GPL. You should read and
 *  accept the LICENSE before you use, modify and/or redistribute this software.
 *
 *@author     jwilliams@aspectsecurity.com
 *@created    November 6, 2002
 */
public class ThreadWatcher implements Runnable
{
    // time to live in milliseconds
    private BitSet myInterrupted;
    private Process myProcess;
    private int myTimeout;

    /**
     *  Constructor for the ThreadWatcher object
     *
     *@param  p            Description of the Parameter
     *@param  interrupted  Description of the Parameter
     *@param  timeout      Description of the Parameter
     */
    public ThreadWatcher(Process p, BitSet interrupted, int timeout)
    {
        myProcess = p;

        // thread used by whoever constructed this watcher
        myTimeout = timeout;
        myInterrupted = interrupted;
    }

    /*
     *  Interrupt the thread by marking the interrupted bit and killing the process
     */

    /**
     *  Description of the Method
     */
    public void interrupt()
    {
        myInterrupted.set(0);

        // set interrupted bit (bit 0 of the bitset) to 1
        myProcess.destroy();

        /*
         *  try
         *  {
         *  myProcess.getInputStream().close();
         *  }
         *  catch( IOException e1 )
         *  {
         *  / do nothing -- input streams are probably already closed
         *  }
         *  try
         *  {
         *  myProcess.getErrorStream().close();
         *  }
         *  catch( IOException e2 )
         *  {
         *  / do nothing -- input streams are probably already closed
         *  }
         *  myThread.interrupt();
         */
    }


    /**
     *  Main processing method for the ThreadWatcher object
     */
    public void run()
    {
        try
        {
            Thread.sleep(myTimeout);
        }
        catch (InterruptedException e)
        {
            // do nothing -- if watcher is interrupted, so is thread
        }

        interrupt();
    }
}

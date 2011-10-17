package alice.tucson.service;

import java.util.*;

// Report relativo all'installazione del nodo
public class TucsonInstallReport
{
	private boolean succeeded;
	private Date date;
	private String descr;
	private Object synch;

	public TucsonInstallReport()
	{
		descr = "";
		succeeded = false;
		synch = new Object();
	}

	public synchronized void setReport(boolean success, String descr, Date date)
	{
		succeeded = success;
		this.descr = descr;
		this.date = date;
		
		synchronized (synch)
		{
			synch.notifyAll();
		}
	}

	public void waitForReport() throws InterruptedException
	{
		synchronized (synch)
		{
			synch.wait();
		}
	}

	public synchronized boolean isSucceeded()
	{
		return succeeded;
	}

	public synchronized String getDescr()
	{
		return descr;
	}

	public synchronized Date getDate()
	{
		return date;
	}
}

/*
 * Copyright (C) 2001-2002  aliCE team at deis.unibo.it
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package alice.tucson.network;

import alice.tucson.service.ACCDescription;

import java.io.*;

import java.util.*;

/**
 * 
 */
@SuppressWarnings("serial")
public abstract class TucsonProtocol implements java.io.Serializable{
	
	abstract public ObjectInputStream getInputStream();

	abstract public ObjectOutputStream getOutputStream();

	abstract public TucsonProtocol acceptNewDialog() throws Exception;

	abstract public void end() throws Exception;

	abstract protected String receiveString() throws Exception;

	abstract protected int receiveInt() throws Exception;

	abstract protected boolean receiveBoolean() throws Exception;

	abstract protected Object receiveObject() throws Exception;

	abstract protected void send(int value) throws Exception;

	abstract protected void send(boolean value) throws Exception;

	abstract protected void send(String value) throws Exception;

	abstract protected void send(byte[] value) throws Exception;

	abstract protected void send(Object value) throws Exception;

	abstract protected void flush() throws Exception;

	/**
	 * 
	 * @param context
	 * @throws Exception
	 */
	public void sendEnterRequest(ACCDescription context) throws Exception{
		
		send(REQ_ENTERCONTEXT);

		String agentName = context.getProperty("agent-identity");
		if(agentName == null)
			agentName = "anonymous";
		send(agentName);

		String agentProfile = context.getProperty("agent-role");
		if(agentProfile == null)
			agentProfile = "default";
		send(agentProfile);

		String tcName = context.getProperty("tuple-centre");
		if(tcName == null)
			tcName = "_";
		send(tcName);
		
		flush();
		
	}

	public void receiveEnterRequestAnswer() throws Exception{
		request_allowed = receiveBoolean();
	}

	public boolean isEnterRequestAccepted() throws Exception{
		return request_allowed;
	}

	public void receiveFirstRequest() throws Exception{
		request_type = receiveInt();
	}

	public void receiveEnterRequest() throws Exception{
		String agentName = receiveString();
		String agentRole = receiveString();
		String tcName = receiveString();
		Properties profile = new Properties();
		profile.setProperty("agent-identity", agentName);
		profile.setProperty("agent-role", agentRole);
		profile.setProperty("tuple-centre", tcName);
		context = new ACCDescription(profile);
	}

	public void sendEnterRequestRefused(ACCDescription context) throws Exception{
		send(false);
		flush();
	}

	public void sendEnterRequestAccepted(ACCDescription context) throws Exception{
		send(true);
		flush();
	}

	public ACCDescription getContextDescription(){
		return context;
	}

	public boolean isEnterRequest(){
		return request_type == REQ_ENTERCONTEXT;
	}
	
	/* MODIFIED BY <s.mariani@unibo.it> */
	public boolean isTelnet(){
		return request_type == REQ_TELNET;
	}
	
	private int request_type;
	private boolean request_allowed;

//	private static final int TUCSON_MARK = 303;
//	is this the value sent when I do a telnet via command line?
	private static final int REQ_TELNET = 0;
	private static final int REQ_ENTERCONTEXT = 1;
	private ACCDescription context;

}

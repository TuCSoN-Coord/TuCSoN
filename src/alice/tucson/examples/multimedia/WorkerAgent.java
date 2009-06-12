/*
 * Multimedia TuCSoN Demo - Copyright (C) 2001 deis.unibo.it
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
package alice.tucson.examples.multimedia;

import java.util.*;

import alice.tuplecentre.core.TupleCentreOperation;

import alice.logictuple.*;

import alice.tucson.api.*;
import alice.tucson.api.exception.TucsonException;

/**
 * the worker agent is responsible to build pieces of the image
 * 
 * @author Alessandro Ricci
 * 
 */
public class WorkerAgent extends alice.tucson.api.Automaton
{
	private static final long serialVersionUID = 1L;
	private Random gen = new Random();
	private TucsonTupleCentreId tid;

	public WorkerAgent(String aid) throws TucsonException
	{
		super(aid, null);
		become("boot");
	}

	public void boot()
	{
		try
		{
			WorkerInputGUI gui = new WorkerInputGUI();
			gui.setVisible(true);
			String address = gui.getTupleCentreAddress();
			
			if (address.equals(""))
			{
				address = "localhost";
			}
			
			gui.dispose();
			
			tid = new TucsonTupleCentreId("default@'" + address + "'");
			
			become("mainLoop");
		}
		catch (Exception ex)
		{
		}
	}

	/**
	 * the main loop consists in retriving the details about the job to execute
	 * (in terms of the piece of the image), then to execute the job (creating
	 * the piece of the image) and finally placing the result in the tuple
	 * centre
	 */
	public synchronized void mainLoop() throws Exception
	{
		try
		{
			// get job details
			LogicTuple tuple = super.getContext().in(tid, LogicTuple.parse("zone('" + getTucsonAgentId() + "',X,Y,W,H,ByteSizeX,ByteSizeY)"), (Long) null);
			float x = tuple.getArg(1).floatValue();
			float y = tuple.getArg(2).floatValue();
			float w = tuple.getArg(3).floatValue();
			float h = tuple.getArg(4).floatValue();
			int sizeX = tuple.getArg(5).intValue();
			int sizeY = tuple.getArg(6).intValue();
			// System.out.println(getId()+" works on "+x+" "+y);

			// create the piece of the image
			byte[] info = new byte[sizeX * sizeY];
			elaborateZone(x, y, w, h, sizeX, sizeY, info);

			// place the result on the space
			super.getContext().out(tid, new LogicTuple("zone_produced", new Value(getTucsonAgentId().toString()), new Value(new String(info))), (Long) null);

			// loop again
			//become("mainLoop");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			become("end");
		}
	}

	private void elaborateZone(float x, float y, float w, float h, int sizeX, int sizeY, byte[] info)
	{
		// float constraints=(x*y+0.5f)/1.5f;
		int i = 0;
		float stepX = w / sizeX;
		float stepY = h / sizeY;

		for (int iy = 0; iy < sizeY; iy++)
		{
			for (int ix = 0; ix < sizeX; ix++)
			{
				float dx = x + ix * stepX;
				float dy = y + iy * stepY;
				float constraints = (float) Math.sqrt(dx * dx + dy * dy);
				info[i++] = (byte) (0xFF * (gen.nextFloat() * constraints));
			}
		}
	}

	public static void main(String[] args) throws Exception
	{
		if (args.length != 1)
		{
			System.err.println("args: <name>");
		}
		else
		{
			new WorkerAgent(args[0]).spawn();
		}
	}

	@Override
	public void operationCompleted(TupleCentreOperation op)
	{
		// TODO Auto-generated method stub		
	}
}

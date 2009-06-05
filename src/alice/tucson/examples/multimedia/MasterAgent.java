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

import alice.logictuple.*;

import alice.tucson.api.*;
import alice.tucson.api.exception.TucsonException;

import alice.tuplecentre.core.TupleCentreOperation;

/**
 * the master agent is responsible to setup coordination specification in the
 * tuple centre and to visualize/update the image during its distributed
 * creation
 * 
 * @author Alessandro Ricci
 * 
 */
public class MasterAgent extends Automaton
{
	private int nx = 16;
	private int ny = 16;
	private float deltax = 1.0f / nx;
	private float deltay = 1.0f / ny;
	private int imageSizeX = 512;
	private int imageSizeY = 512;
	private Gui gui;
	private int[] image = new int[imageSizeX * imageSizeY];
	private TucsonTupleCentreId tid;

	public MasterAgent(String name, String node) throws TucsonException
	{		
		super(new TucsonAgentId(name), null);
		tid = new TucsonTupleCentreId("default@'" + node + "'");
		become("boot");
	}

	public void boot()
	{
		try
		{
			gui = new Gui(imageSizeX, imageSizeY);
			gui.setVisible(true);
			
			become("installSpec");
		}
		catch (Exception ex)
		{
		}
	}

	/**
	 * setups the tuple centre
	 */
	public void installSpec() throws Exception
	{
		// installs coordination specification
		super.getContext().setSpec(tid, alice.util.Tools.loadText(new java.io.BufferedInputStream(ClassLoader
				.getSystemResourceAsStream("alice/tucson/examples/multimedia/spec.rsp"))), (Long) null);
		
		// setups basic data structure on the tuple centre
		for (int x = 0; x < nx; x++)
		{
			for (int y = 0; y < ny; y++)
			{
				TupleArgument zoneId = new Value(y + "_" + x);
				
				super.getContext().out(tid, new LogicTuple("zone_extent", zoneId, new Value(deltax * x), new Value(deltay * y), new Value(deltax),
						new Value(deltay)), (Long) null);
				super.getContext().out(tid, new LogicTuple("free", zoneId), (Long) null);
			}
		}
		
		super.getContext().out(tid, new LogicTuple("patch_pixel_size", new Value(imageSizeX / nx), new Value(imageSizeY / ny)), (Long) null);
		// start looping
		become("mainLoop");
	}

	// main agent cycle
	public void mainLoop() throws Exception
	{
		// gets updated image zone
		LogicTuple tuple = super.getContext().in(tid, LogicTuple.parse("zone_updated(ZoneId)"), (Long) null);
		tuple = super.getContext().rd(tid, new LogicTuple("zone_image", tuple.getArg(0), new Var("Image")), (Long) null);
		// System.out.println("work finisehd for zone "+tuple.getTerm(0));

		// update the global image
		updateImage(tuple.getArg(0), tuple.getArg(1).toString().getBytes());
		// loop again
		become("mainLoop");
	}

	void updateImage(TupleArgument zoneId, byte[] imagePatch)
	{
		try
		{
			LogicTuple tuple = super.getContext().rd(tid, new LogicTuple("zone_extent", zoneId, new Var("X"), new Var("Y"), new Var("W"), new Var(
					"H")), (Long) null);
			
			float x = tuple.getArg(1).floatValue();
			float y = tuple.getArg(2).floatValue();
			float w = tuple.getArg(3).floatValue();
			float h = tuple.getArg(4).floatValue();
			int ix = (int) (x * imageSizeX);
			int iy = (int) (y * imageSizeY);
			int iw = (int) (w * imageSizeX);
			int ih = (int) (h * imageSizeY);
			int offset = iy * imageSizeX + ix;
			int index = 0;
			int totcount = 0;
			
			for (int i = 0; i < imagePatch.length; i++)
			{
				image[offset + index] = (0xFF << 24) + (imagePatch[i] + (imagePatch[i] << 8) + (imagePatch[i] << 16));
				index++;
				totcount++;
				if (index >= iw)
				{
					index = 0;
					offset += imageSizeX;
				}
				if (totcount >= iw * ih)
				{
					break;
				}
			}
			gui.update(image);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception
	{
		String address = "localhost";
		
		if (args.length == 1)
		{
			address = args[0];
		}
		
		new MasterAgent("master", address).spawn();
	}

	@Override
	public void operationCompleted(TupleCentreOperation op)
	{
		// TODO Auto-generated method stub		
	}
}

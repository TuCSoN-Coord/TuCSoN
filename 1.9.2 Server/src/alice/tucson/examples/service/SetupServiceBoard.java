/*
 * TuCSoN - Copyright (C) 2001-2002  aliCE team at deis.unibo.it
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
package alice.tucson.examples.service;

import alice.tucson.api.*;

import alice.tucson.service.TucsonAgentLanguageManager;
import alice.tucson.service.TucsonLanguageManager;

public class SetupServiceBoard
{
	public static void main(String args[])
	{
		try
		{
			TucsonAgentLanguageManager.useLanguage(TucsonLanguageManager.DEFLANGUAGE);
			TucsonTupleCentreId tid;
			
			if (args.length == 0)
			{
				tid = new TucsonTupleCentreId("default");
			}
			else if (args.length == 1)
			{
				tid = new TucsonTupleCentreId(args[0]);
			}
			else
			{
				System.err.println("args: {tuple centre identifier}");
				System.exit(-1);
				return;
			}
			
			TucsonContext ctx = Tucson.getContext(new TucsonAgentId("service_board"), TucsonAgentLanguageManager.getCurLanguage());

			ctx.setSpec(tid, "reaction(in(chops(C1,C2)), (pre, out_r(required(C1,C2)))).\n"
					+ "reaction(in(chops(C1,C2)), (post,in_r(required(C1,C2)))).\n"
					+ "reaction(out_r(required(C1,C2)),(in_r(chop(C1)),in_r(chop(C2)),out_r(chops(C1,C2)))).\n"
					+ "reaction(out(chops(C1,C2)), ( in_r(chops(C1,C2)),out_r(chop(C1)),out_r(chop(C2)))).\n"
					+ "reaction(out_r(chop(C1)), (rd_r(required(C1,C)), in_r(chop(C1)),in_r(chop(C)),out_r(chops(C1,C)))).\n"
					+ "reaction(out_r(chop(C2)), (rd_r(required(C,C2)), in_r(chop(C)),in_r(chop(C2)),out_r(chops(C,C2)))).\n",
					(Long) null);

			ctx.exit();
			
			System.out.println("Service board initialized.");

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

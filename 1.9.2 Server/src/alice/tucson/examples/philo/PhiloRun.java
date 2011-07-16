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
package alice.tucson.examples.philo;

import alice.logictuple.*;

import alice.tuplecentre.core.TupleCentreOperation;

import alice.tucson.api.*;
import alice.tucson.api.exception.TucsonException;
import alice.tucson.service.TucsonAgentLanguageManager;
import alice.tucson.service.TucsonLanguageManager;

class Philosopher extends TucsonAgent
{
	TucsonTupleCentreId tid;
	int c1, c2;

	public Philosopher(String id, TucsonTupleCentreId tid, String language) throws TucsonException
	{
		super(id, language);
		this.tid = tid;
	}

	protected void body()
	{
		System.out.println("[ " + this.getTucsonAgentId() + " ] run");
		try
		{
			LogicTuple tc = this.getContext().rd(tid, new LogicTuple("philo", new Value(this.getTucsonAgentId().toString()), new Var("C1"), new Var("C2")), (Long) null);
			c1 = (int) tc.getArg(1).intValue();
			c2 = (int) tc.getArg(2).intValue();
			
			while (true)
			{
				think();
				this.getContext().in(tid, new LogicTuple("forks", new Value(c1), new Value(c2)), (Long) null);
				eat();
				this.getContext().out(tid, new LogicTuple("forks", new Value(c1), new Value(c2)), (Long) null);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void think()
	{
		System.out.println("[ " + this.getTucsonAgentId() + " ] thinking");
	}

	private void eat()
	{
		System.out.println("[ " + this.getTucsonAgentId() + " ] eating " + c1 + " " + c2);
	}

	@Override
	protected void main()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void operationCompleted(TupleCentreOperation op)
	{
		// TODO Auto-generated method stub
		
	}
}

/**
 * 5 Philosophers example using Tucson Service as coordination model with 3
 * prolog philosophers and 2 java philosophers
 * 
 */
public class PhiloRun
{
	public static void main(String args[])
	{
		try
		{
			TucsonAgentLanguageManager.useLanguage(TucsonLanguageManager.DEFLANGUAGE);
			TucsonTupleCentreId tid;
			if (args.length == 0)
			{
				tid = new TucsonTupleCentreId("table");
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

			TucsonContext ctx = Tucson.getContext(new TucsonAgentId("manager"), TucsonAgentLanguageManager.getCurLanguage());
			ctx.setSpec(tid, "reaction(in(forks(C1,C2)), (pre, out_r(required(C1,C2)))).\n"
					+ "reaction(in(forks(C1,C2)), (post,in_r(required(C1,C2)))).\n"
					+ "reaction(out_r(required(C1,C2)),(in_r(fork(C1)),in_r(fork(C2)),out_r(forks(C1,C2)))).\n"
					+ "reaction(out(forks(C1,C2)), ( in_r(forks(C1,C2)),out_r(fork(C1)),out_r(fork(C2)))).\n"
					+ "reaction(out_r(fork(C1)), (rd_r(required(C1,C)), in_r(fork(C1)),in_r(fork(C)),out_r(forks(C1,C)))).\n"
					+ "reaction(out_r(fork(C2)), (rd_r(required(C,C2)), in_r(fork(C)),in_r(fork(C2)),out_r(forks(C,C2)))).\n", 
					(Long) null);

			for (int i = 0; i < 5; i++)
			{
				ctx.out(tid, new LogicTuple("fork", new Value(i)), (Long) null);
			}

			// we make philosophers known what forks they must use
			ctx.out(tid, new LogicTuple("philo", new Value("arnaldo"), new Value(0), new Value(1)), (Long) null);
			ctx.out(tid, new LogicTuple("philo", new Value("etles"), new Value(1), new Value(2)), (Long) null);
			ctx.out(tid, new LogicTuple("philo", new Value("beppo"), new Value(2), new Value(3)), (Long) null);
			ctx.out(tid, new LogicTuple("philo", new Value("cledes"), new Value(3), new Value(4)), (Long) null);
			ctx.out(tid, new LogicTuple("philo", new Value("rosa"), new Value(4), new Value(0)), (Long) null);

			new Philosopher("arnaldo", tid, TucsonAgentLanguageManager.getCurLanguage().getName()).spawn();
			new Philosopher("etles", tid, TucsonAgentLanguageManager.getCurLanguage().getName()).spawn();
			new Philosopher("beppo", tid, TucsonAgentLanguageManager.getCurLanguage().getName()).spawn();
			new Philosopher("cledes", tid, TucsonAgentLanguageManager.getCurLanguage().getName()).spawn();
			new Philosopher("rosa", tid, TucsonAgentLanguageManager.getCurLanguage().getName()).spawn();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

package aaa.myAgent191;
import alice.logictuple.LogicTuple;
import alice.logictuple.Value;
import alice.tucson.api.*;
import alice.tucson.service.TucsonAgentLanguageManager;
import alice.tucson.service.TucsonLanguageManager;



public class SetSpec
{
	public static void main(String args[])
	{
		try
		{
			TucsonAgentLanguageManager.useLanguage(TucsonLanguageManager.DEFLANGUAGE);
			
			TucsonTupleCentreId tid;
			
			
			tid = new TucsonTupleCentreId("mossBologna");
			
			
			TucsonContext ctx = Tucson.getContext(new TucsonAgentId("superman"), TucsonAgentLanguageManager.getCurLanguage());

			ctx.setSpec(tid, "reaction(out(p(X)),(post, out_r(backup(p(X))))).",(Long) null);
			//LogicTuple tuple = new LogicTuple("p", new Value(67));
			//ctx.out(tid, tuple, (Long) null);
			ctx.exit();
			
			System.out.println("Service board initialized.");

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}


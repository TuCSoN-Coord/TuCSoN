package alice.tucson.test;

import alice.tucson.service.*;

public class TestInstall
{
	public static void main(String[] args) throws Exception
	{	
		TucsonNodeLanguageManager.useLanguage(TucsonLanguageManager.DEFLANGUAGE);
		
		TucsonInstallReport repo = new TucsonInstallReport();
		new TucsonNodeManager(repo).start();
		repo.waitForReport();
		
		if (repo.isSucceeded())
		{
			System.out.println("TuCSoN installation OK - " + repo.getDate());
		}
		else
		{
			System.out.println("TuCSoN installation FAILED.");
		}
	}

	static class TucsonNodeManager extends Thread
	{
		private TucsonInstallReport repo;

		TucsonNodeManager(TucsonInstallReport repo)
		{
			this.repo = repo;
		}

		public void run()
		{
			try
			{
				new alice.tucson.service.Node().install(repo);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
}

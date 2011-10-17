/*
 * TuCSoN coordination infrastructure - Copyright (C) 2001-2002  aliCE team at deis.unibo.it
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

package alice.tucson.service;

import java.util.*;

import java.io.*;

import alice.logictuple.*;

import alice.tuprolog.Prolog;
import alice.tuprolog.Theory;

import alice.tucson.api.exception.InvalidConfigException;
import alice.tucson.api.exception.TCInstantiationNotPossibleException;
import alice.tucson.api.exception.TucsonException;
import alice.tucson.api.exception.TucsonInvalidLogicTupleException;
import alice.tucson.api.exception.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exception.TucsonOperationNotPossibleException;

import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonTupleCentreId;

/**
 * Node class provides the basic daemon of a TuCSoN infrastructure node.
 * 
 */
public class Node
{
	/** reference to the instance representing the service running */
	private static Node instance = null;

	/** booting date */
	private Date installationDate;

	/** current tuple media installed */
	private HashMap<String, TucsonTCUsers> cores;

	private TucsonAgentId nodeAid;
	private TucsonTupleCentreId idConfigTC;
	private TucsonTupleCentreId idObsTC;
	private ObservationService obsService;

	private Prolog configManager;
	private String configFile;

	private static final String DEFAULT_BOOT_SPEC_FILE = "alice/tucson/service/config/boot_spec.rsp";
	private static final String DEFAULT_OBS_SPEC_FILE = "alice/tucson/service/config/obs_spec.rsp";
	private static final String PERSISTENCY_PATH = "./persistent/";
	private static final String BOOT_SETUP_THEORY = "alice/tucson/service/config/boot.pl";
	private static final int DEFAULT_TCP_PORT = 20504;  

	private int tcp_port = DEFAULT_TCP_PORT;
	private TupleArgument persistencyTemplate;
	private boolean observed;

	private ArrayList<TucsonAgentId> agents;

	public Node(String configFile, int portNumber, TupleArgument persistTempl) throws TucsonException
	{
		// file di configurazione
		this.configFile = configFile;
		tcp_port = portNumber;

		// persistenza
		persistencyTemplate = persistTempl;
		nodeAid = new TucsonAgentId("node_agent");
		idConfigTC = new TucsonTupleCentreId("'$ORG'");
		
		// observation service
		idObsTC = new TucsonTupleCentreId("'$OBS'");		
		observed = false;
		agents = new ArrayList<TucsonAgentId>();
	}

	public Node() throws TucsonException
	{
		this(null, DEFAULT_TCP_PORT, null);
	}

	public synchronized void install()
	{
		install(null);		
	}

	public synchronized void install(TucsonInstallReport repo)
	{
		try
		{
			outMsg("TuCSoN Infrastructure (version " + Node.getVersion() + ") - start installation ...");
			
			configManager = new Prolog();

			cores = new HashMap<String, TucsonTCUsers>();

			if (configFile != null)
			{
				outMsg("Setup configuration (source file " + configFile + ").");
			}
			else
			{
				outMsg("Setup configuration (default configuration file).");
			}
			
			// configurazione
			setupConfiguration(configFile);

			outMsg("Setup observation tuple centre.");
			setupObsTupleCentre();

			outMsg("Setup configuration tuple centre.");
			setupConfigTupleCentre();

			outMsg("Check persistent tuple centres.");
			checkPersistentTupleCentres(PERSISTENCY_PATH);

			installationDate = new Date();
			instance = this;

			outMsg("TuCSoN Infrastructure (version " + getVersion() + ")  installed.\n" + installationDate);
			outMsg("Spawning management agents.");
			
			bootManagementAgents();

			if (repo != null)
			{
				repo.setReport(true, "Installed", installationDate);
				outMsg("report created.");
			}			
			
			synchronized (this)
			{
				wait();
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			if (repo != null)
			{
				repo.setReport(false, "Failed", null);
			}
		}		
	}

	/**
	 * Gets the TuCSoN version number
	 * 
	 * @return the version number
	 */
	public static String getVersion()
	{
		return "1.9.3";
	}

	/**
	 * resolves a tuple medium reference given its name.
	 * <p>
	 * if the tuple medium is not found, then a new VM is created and registered
	 * with specified name
	 */
	public synchronized TucsonTCUsers resolveCore(String tcName)
	{
		try
		{
			TucsonTupleCentreId tid = new TucsonTupleCentreId(tcName);

			String realName = tid.getName();
			TucsonTCUsers core = cores.get(realName);
			
			if (core == null)
			{
				// tuple centre not present --> create new one
				log("create new tuple centre " + realName);
				core = bootTupleCentre(realName);

				try
				{
					TucsonTCContainer.out(nodeAid, tid, new LogicTuple("tuple_centre", TupleArgument.parse(realName)));
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}

			}
			return (TucsonTCUsers) core;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	private TucsonTCUsers bootTupleCentre(String name) throws TucsonInvalidTupleCentreIdException,
			TCInstantiationNotPossibleException
	{
		TucsonTupleCentreId id = new TucsonTupleCentreId(name);				
		TucsonTCContainer.createTC(id, 10);
		
		if (observed)
		{
			//TucsonTCContainer.activateObservability(id, obsService);
			try
			{
				TucsonTCContainer.doManagementOperation(TucsonOperation.addObsCode(), id, obsService);
			}
			catch (TucsonOperationNotPossibleException e)
			{
				e.printStackTrace();
			}
			catch (TucsonInvalidLogicTupleException e)
			{
				e.printStackTrace();
			}
			obsService.tcCreated(id);
		}
		
		TucsonTCUsers tcUsers = new TucsonTCUsers(id);
		cores.put(id.getName(), tcUsers);

		try
		{
			TupleArgument tid = Value.parse(name);
			if (persistencyTemplate != null && persistencyTemplate.match(tid))
			{
				TucsonTCContainer.enablePersistence(id, PERSISTENCY_PATH + "tc_" + tid.toString() + ".dat");
				TucsonTCContainer.out(nodeAid, id, new LogicTuple("is_persistent", new Value(name)));
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			log("failed to enable persistency on tuple centre " + name);
		}
		
		log("new tuple centre " + name + " booted.");
		return tcUsers;
	}

	/**
	 * destroys a tuple medium reference given its name.
	 */
	public synchronized boolean destroyCore(String tcName)
	{
		try
		{
			TucsonTupleCentreId tid = new TucsonTupleCentreId(tcName);
			// String realName = tid.getName();
			String realName = tcName;
			TucsonTupleCentreId core = cores.get(realName).getTucsonTupleCentreId();

			if (core != null)
			{
				// tuple centre not present --> create new one
				log("destroy tuple centre " + tcName);

				// prev. vers.: vmContext.getThread().interrupt();
				TucsonTCContainer.destroyTC(cores.get(tcName).getTucsonTupleCentreId());

				if (observed)
				{
					obsService.tcDestroyed(tid);
				}

				try
				{
					try
					{
						TupleArgument tcArg = TupleArgument.parse(tcName);
						TucsonTCContainer.inp(nodeAid, idConfigTC, new LogicTuple("tuple_centre", tcArg));
						TucsonTCContainer.inp(nodeAid, idConfigTC, new LogicTuple("is_persistent", new Value(tcName)));
					}
					catch (Exception ex)
					{
						TucsonTCContainer.inp(nodeAid, idConfigTC, new LogicTuple("tuple_centre", new Value(tcName)));
					}
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
				cores.remove(realName);
				return true;

			}
			else
			{
				return false;
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	}

	// Persistency

	public synchronized void enablePersistence(TupleArgument template)
	{
		persistencyTemplate = template;
		try
		{
			Iterator<TucsonTCUsers> it = cores.values().iterator();
			while (it.hasNext())
			{
				TucsonTCUsers tc = it.next();
				try
				{
					TupleArgument tid = Value.parse(tc.getTucsonTupleCentreId().getName());
					if (template.match(tid))
					{
						TucsonTCContainer.enablePersistence(tc.getTucsonTupleCentreId(), PERSISTENCY_PATH + "tc_"
								+ tc.getTucsonTupleCentreId().getName() + ".dat");
						TucsonTCContainer.out(nodeAid, tc.getTucsonTupleCentreId(), new LogicTuple("is_persistent", new Value(tc
								.getTucsonTupleCentreId().getName())), null);
						log("persistency on tuple centre " + tc.getTucsonTupleCentreId().getName() + " enabled.");
					}
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}

	public synchronized void enablePersistence(String tc)
	{
		TucsonTCUsers tar = cores.get(tc);
		
		try
		{
			TucsonTCContainer.enablePersistence(tar.getTucsonTupleCentreId(), PERSISTENCY_PATH + "tc_"
					+ tar.getTucsonTupleCentreId().getName() + ".dat");
			TucsonTCContainer.out(nodeAid, tar.getTucsonTupleCentreId(), new LogicTuple("is_persistent", new Value(tar
					.getTucsonTupleCentreId().getName())), null);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		log("persistency on tuple centre " + tar.getTucsonTupleCentreId().getName() + " enabled.");
	}

	public synchronized void disablePersistence(TupleArgument template)
	{
	 if (persistencyTemplate!=null){
		try
		{
			Iterator<TucsonTCUsers> it = cores.values().iterator();
			while (it.hasNext())
			{
				TucsonTCUsers tc = it.next();
				try
				{
					TupleArgument tid = Value.parse(tc.getTucsonTupleCentreId().getName());
					if (template.match(tid))
					{
						TucsonTCContainer.disablePersistence(tc.getTucsonTupleCentreId());
						TucsonTCContainer.in(nodeAid, tc.getTucsonTupleCentreId(), new LogicTuple("is_persistent", new Value(tc
								.getTucsonTupleCentreId().getName())), null);
						log("persistency on tuple centre " + tc.getTucsonTupleCentreId().getName() + " disabled.");
					}
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		if (persistencyTemplate.match(template))
		{
			persistencyTemplate = null;
		}
	 }
	}

	public synchronized void disablePersistence(String tc)
	{
		TucsonTCUsers tar = cores.get(tc);
		try
		{
			TucsonTCContainer.disablePersistence(tar.getTucsonTupleCentreId());
			TucsonTCContainer.in(nodeAid, tar.getTucsonTupleCentreId(), new LogicTuple("is_persistent", new Value(tar
					.getTucsonTupleCentreId().getName())), null);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		log("persistency on tuple centre " + tar.getTucsonTupleCentreId().getName() + " disabled.");
	}

	private void checkPersistentTupleCentres(String dirName)
	{
		try
		{
			File dir = new File(dirName);
			if (dir.exists() && dir.isDirectory())
			{
				String[] files = dir.list();
				for (int i = 0; i < files.length; i++)
				{
					if (files[i].startsWith("tc_") && files[i].endsWith(".dat"))
					{
						String tcName = files[i].substring(3, files[i].length() - 4);
						bootTupleCentre(tcName);
						TucsonTCContainer.loadPersistentInformation(cores.get(tcName).getTucsonTupleCentreId(), PERSISTENCY_PATH
								+ files[i]);
						TucsonTCContainer.enablePersistence(cores.get(tcName).getTucsonTupleCentreId(), PERSISTENCY_PATH
								+ files[i]);
						TucsonTCContainer.out(nodeAid, cores.get(tcName).getTucsonTupleCentreId(), new LogicTuple(
								"is_persistent", new Value(tcName)), null);
						outMsg("restore persistent tuple centre: " + tcName);
					}
				}
			}
			else
			{
				dir.mkdir();
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Gets the exact date in which the node was installed
	 * 
	 */
	public Date getInstallationDate()
	{
		return installationDate;
	}

	private void log(String m)
	{
		System.out.println("[TuCSoN NODE] " + m);
	}

	private void outMsg(String m)
	{
		System.out.println(m);
	}

	private void setupConfiguration(String configFile) throws TucsonException
	{
		//System.out.println(configFile);	
		try
		{
			alice.tuprolog.lib.JavaLibrary jlib = (alice.tuprolog.lib.JavaLibrary) configManager
					.getLibrary("alice.tuprolog.lib.JavaLibrary");
			jlib.register(new alice.tuprolog.Struct("config"), this);
		}
		catch (Exception ex)
		{
			throw new TucsonException("Internal Failure: loading JavaLibrary in Prolog Configuration Engine.");
		}
		
		if (configFile != null)
		{
			try
			{
				InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(BOOT_SETUP_THEORY);
				Theory setup_th = new Theory(new BufferedInputStream(is));
				configManager.setTheory(setup_th);
			}
			catch (alice.tuprolog.InvalidTheoryException ex)
			{
				throw new TucsonException("Internal Failure: invalid Prolog Configuration Engine theory.");
			}
			catch (IOException ex)
			{
				throw new TucsonException("Internal Failure: invalid Prolog Configuration Engine theory.");
			}
			try
			{
				Theory cfg_th = new Theory(new FileInputStream(configFile));
				configManager.addTheory(cfg_th);
				alice.tuprolog.SolveInfo info = configManager.solve("setup.");
				
				if (!info.isSuccess())
				{
					throw new InvalidConfigException();
				}
			}
			catch (alice.tuprolog.PrologException ex)
			{
				throw new InvalidConfigException();
			}
			catch (IOException ex)
			{
				throw new TucsonException("Internal Failure: invalid Prolog Configuration Engine theory.");
			}
		}
	}

	private void setupConfigTupleCentre()
	{
		//
		// setup management tuple centre
		//
		try
		{
			bootTupleCentre(idConfigTC.getName());
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(DEFAULT_BOOT_SPEC_FILE);
			String spec = alice.util.Tools.loadText(new BufferedInputStream(is));
			LogicTuple specTuple = new LogicTuple("spec", new Value(spec));
			
			TucsonTCContainer.setSpec(nodeAid, idConfigTC, specTuple);
			//System.out.println("GET_SPEC: " + TucsonTCContainer.getSpec(nodeAid, idConfigTC));
		}
		catch (Exception ex)
		{
			if (ex.getClass().toString().equals("TucsonInvalidSpecificationException"))
			{
				log("Invalid reaction specification for the Management tuple centre");
			}
			else
			{
				ex.printStackTrace();
			}
		}
		//
		// config spec initialisation
		//
		try
		{
			TucsonTCContainer.out(nodeAid, idConfigTC, new LogicTuple("boot"), null);
			addAgent(nodeAid);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void setupObsTupleCentre()
	{
		//
		// setup observer tuple centre
		//
		try
		{
			bootTupleCentre(idObsTC.getName());

			InputStream is = ClassLoader.getSystemResourceAsStream(DEFAULT_OBS_SPEC_FILE);
			String spec = alice.util.Tools.loadText(new BufferedInputStream(is));
			// File file = new File(DEFAULT_OBS_SPEC_FILE);
			// String spec = this.readFile(file);
			LogicTuple specTuple = new LogicTuple("spec", new Value(spec));
			TucsonTCContainer.setSpec(nodeAid, idObsTC, specTuple);
			//System.out.println(spec);
		}
		catch (Exception ex)
		{
			if (ex.getClass().toString().equals("TucsonInvalidSpecificationException"))
			{
				log("Invalid reaction specification for the observation tuple centre");
			}
			else
			{
				ex.printStackTrace();
			}
		}		
			    
		//
		// observation spec initialisation
		//
		try
		{
			TucsonTCContainer.out(nodeAid, idObsTC, new LogicTuple("boot"), null);
			obsService = new ObservationService(idObsTC);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void bootManagementAgents()
	{
		// risponde alle richieste inserite nel tuple centre $ORG
		new NodeManagementAgent(idConfigTC, this);

		ContextManager ctxman = new ContextManager(this, idConfigTC);
		
		new WelcomeAgent(tcp_port, this, ctxman);
	}

	/**
	 * Add an agent to the infrastructure
	 * 
	 */
	synchronized public void addAgent(TucsonAgentId aid)
	{
		boolean present = false;
		if (!agents.contains(aid))
		{
			agents.add(aid);
		}
		else
		{
			present = true;
		}
		if (observed && !present)
		{
			obsService.accEntered(aid);
		}
	}

	/**
	 * Remove an agent from the infrastructure
	 * 
	 */
	synchronized public void removeAgent(TucsonAgentId aid)
	{
		boolean present = true;
		
		if (agents.contains(aid))
		{
			agents.remove(aid);
		}
		else
		{
			present = false;
		}
		
		if (observed && present)
		{
			obsService.accQuit(aid);
		}

		// remove the agent from all the tc
		Iterator<TucsonTCUsers> it = cores.values().iterator();
		while (it.hasNext())
		{
			it.next().removeUser(aid);
		}
	}

	// configuration ---------------------------------

	public static boolean isInstalled()
	{
		return instance != null;
	}

	public static Node getInstance()
	{
		return instance;
	}
	
	
	/*insert by Enrico Rossignoli*/
	public static Prolog getEngineProlog()
	{
		return Node.getInstance().configManager;
	}
	/*end insert*/

	// observation service ----------------------------------

	synchronized public void activateObservability()
	{
		observed = true;
		try
		{
			Iterator<TucsonTCUsers> it = cores.values().iterator();
			while (it.hasNext())
			{
				TucsonTCUsers tc = it.next();
				try
				{
					TucsonTCContainer.doManagementOperation(TucsonOperation.addObsCode(), tc.getTucsonTupleCentreId(), obsService);
				}
				catch (TucsonOperationNotPossibleException e)
				{
					e.printStackTrace();
				}
				catch (TucsonInvalidLogicTupleException e)
				{
					e.printStackTrace();
				}		
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		log(" observation service enabled.");
	}

	synchronized public void deactivateObservability()
	{
		observed = false;
		try
		{
			Iterator<TucsonTCUsers> it = cores.values().iterator();
			while (it.hasNext())
			{
				TucsonTCUsers tc = it.next();
				
				try
				{
					TucsonTCContainer.doManagementOperation(TucsonOperation.rmvObsCode(), tc.getTucsonTupleCentreId(), obsService);
				}
				catch (TucsonOperationNotPossibleException e)
				{
					e.printStackTrace();
				}
				catch (TucsonInvalidLogicTupleException e)
				{
					e.printStackTrace();
				}	
				//TucsonTCContainer.deactivateObservability(tc.getTucsonTupleCentreId());
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		log(" observation service disabled.");
	}

	public NodeEventListener getListener()
	{
		return obsService;
	}

	public HashMap<String, TucsonTCUsers> getCores()
	{
		return cores;
	}

	/**
	 * Add an agent to a TC (see AgentContextSkeleton)
	 * 
	 */
	public void addTCAgent(TucsonAgentId agentId, TucsonTupleCentreId tid)
	{
		cores.get(tid.getName()).addUser(agentId);
	}

	// ------------------------------------------------------------------------------------

	public static void main(String args[])
	{
		if (alice.util.Tools.isOpt(args, "-help") || alice.util.Tools.isOpt(args, "-?"))
		{
			System.err.println("Arguments: -help -port {portNumber} -config {configFile} } ");
		}
		else
		{
			String languageInfo = alice.util.Tools.getOpt(args, "-language");
			
			if (languageInfo == null)
			{
				languageInfo = TucsonLanguageManager.DEFLANGUAGE;
			}
			
			String configInfo = alice.util.Tools.getOpt(args, "-config");
			String portInfo = alice.util.Tools.getOpt(args, "-port");
			String persistencyInfo = alice.util.Tools.getOpt(args, "-persistency");
			
			try
			{
				int portNumber = Node.DEFAULT_TCP_PORT;
				
				if (portInfo != null)
				{
					try
					{
						portNumber = Integer.parseInt(portInfo);
						if (portNumber < 0 || portNumber > 64000)
						{
							System.err.println("Invalid port number.");
							System.exit(-1);
						}
					}
					catch (Exception ex)
					{
						System.err.println("Invalid port number.");
						System.exit(-1);
					}
				}
				
				TupleArgument template = null;
				
				if (persistencyInfo != null)
				{
					try
					{
						template = TupleArgument.parse(persistencyInfo);
					}
					catch (Exception ex)
					{
						System.err.println("Invalid persistency template.");
						System.exit(-1);
					}
				}
				
				TucsonNodeLanguageManager.useLanguage(languageInfo);
				
				new Node(configInfo, portNumber, template).install();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
}

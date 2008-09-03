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

import alice.tucson.api.*;
import alice.logictuple.*;

import alice.tuprolog.Prolog;
import alice.tuprolog.Theory;
import java.util.*;
import java.io.*;



/**
 * Node class provides the basic daemon of a
 * TuCSoN infrastructure node.
 *
 */
public class Node  {

	/** indicates if the (only) service is actually running */
	private static boolean active = false;

	/** reference to the instance representing the service running */
	private static Node instance = null;


	/** current tuple media installed */
	private HashMap cores;

	/** booting date */
	private Date installationDate;

	private TucsonVMUsers configTupleCentreInfo;
	private TupleCentreId idConfigTC;
	private LocalTupleCentreContext configContext;

	private TucsonVMUsers obsTupleCentreInfo;
	private TupleCentreId idObsTC;
	private LocalTupleCentreContext obsContext;
	private ObservationService obsService;
	
	private Prolog configManager;
	private String configFile;

	// config data
	private String bootSpecFile;

	private static final String DEFAULT_BOOT_SPEC_FILE = "alice/tucson/service/config/boot_spec.rsp";
	private static final String DEFAULT_OBS_SPEC_FILE = "alice/tucson/service/config/obs_spec.rsp";
	private static final String PERSISTENCY_PATH = "./persistent/";
	private static final String BOOT_SETUP_THEORY = "alice/tucson/service/config/boot.pl";
	private static final int DEFAULT_TCP_PORT = 20504;

	private int tcp_port = DEFAULT_TCP_PORT;
	private TupleArgument persistencyTemplate;
	private boolean observed;

	private ArrayList agents;
	
	public Node(String configFile, int portNumber,TupleArgument persistTempl) throws TucsonException {
		this.configFile = configFile;
		tcp_port = portNumber;
		persistencyTemplate = persistTempl;
		idConfigTC = new TupleCentreId("'$ORG'");
		idObsTC = new TupleCentreId("'$OBS'");	
		observed = false;
		agents = new ArrayList();
	}

	public Node() throws TucsonException {
		this(null,DEFAULT_TCP_PORT,null);
	}


	public synchronized void install() {
		install(null);
	}
	
	public synchronized void install(TucsonInstallReport repo) {
		try {
			outMsg("TuCSoN Infrastructure (version " + Node.getVersion() + ") - start installation ...");
			cores = new HashMap();
			configManager = new Prolog();

			if (configFile!=null){
				outMsg("Setup configuration (source file "+configFile+").");
			} else {
				outMsg("Setup configuration (default configuration file).");
			}
			setupConfiguration(configFile);

			outMsg("Setup observation tuple centre.");
			setupObsTupleCentre();

			outMsg("Setup configuration tuple centre.");
			setupConfigTupleCentre();

			outMsg("Check persistent tuple centres.");			checkPersistentTupleCentres(PERSISTENCY_PATH);

			installationDate = new Date();
			active = true;
			instance = this;

			outMsg("TuCSoN Infrastructure (version " + getVersion() + ")  installed.\n" + installationDate);
			outMsg("Spawning management agents.");
			bootManagementAgents();
		
			if (repo!=null){
				repo.setReport(true, "Installed", installationDate);
				outMsg("report created.");
			} 
			
			synchronized(this){
				wait();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			if (repo!=null){
				repo.setReport(false, "Failed", null);
			} 
		}
	}

    /**
     *  Gets the TuCSoN version number
     * @return the version number
     */
    public static String getVersion(){
        return "1.5.1";
    }

	/**
	 * resolves a tuple medium reference givent its name.
	 * <p>
	 * if the tuple medium is not found, then a new VM is created
	 * and registered with specified name
	 */
	public synchronized TucsonVMUsers resolveCore(String tcName) {
			try {
				TupleCentreId tid = new TupleCentreId(tcName);
				String realName = tid.getName();
				Object core = cores.get(realName);
				if (core == null) {
					// tuple centre not present --> create new one
					log("create new tuple centre " + realName);
					core = bootTupleCentre(realName);

					try {
						configContext.out(new LogicTuple("tuple_centre", TupleArgument.parse(realName)));
					} catch (Exception ex) {
						ex.printStackTrace();
					}

				}
				return (TucsonVMUsers) core;
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
	}

	private TucsonVMUsers bootTupleCentre(String name) throws InvalidTupleCentreIdException {
		TupleCentreId id = new TupleCentreId(name);
		TucsonVM newCore = new TucsonVM(id,this);
		Thread thread = new Thread(newCore);
		if (observed){
			newCore.addObserver(obsService);
			obsService.tcCreated(id);
		}
		TucsonVMUsers vmContext = new TucsonVMUsers(name, newCore, thread);
		cores.put(name, vmContext);
		//
		try {
			TupleArgument tid = Value.parse(name);
			if (persistencyTemplate!=null && persistencyTemplate.match(tid)){
				newCore.enablePersistence(PERSISTENCY_PATH+"tc_"+tid.toString()+".dat");
				configContext.out(new LogicTuple("is_persistent", new Value(name)));
			}
		} catch (Exception ex){
			ex.printStackTrace();
			log("failed to enable persistency on tuple centre "+name);
		}
		log("new tuple centre "+name+" booted.");
		thread.start();
		return vmContext;
	}


	/**
	 * destroys a tuple medium reference given its name.
	 */
	public synchronized boolean destroyCore(String tcName) {
			try {
				TupleCentreId tid = new TupleCentreId(tcName);
				String realName = tid.getName();
				Object core = cores.get(realName);

				if (core != null) {
					// tuple centre not present --> create new one
					log("destroy tuple centre " + tcName);

					TucsonVMUsers vmContext = (TucsonVMUsers) core;
					vmContext.removeAllUsers();
					vmContext.getThread().interrupt();

					if (observed){
						obsService.tcDestroyed(tid);
					}

					try {
						try {
							TupleArgument tcArg = TupleArgument.parse(tcName);
							configContext.inp(new LogicTuple("tuple_centre", tcArg));
							configContext.inp(new LogicTuple("is_persistent", new Value(tcName)));
						} catch (Exception ex) {
							configContext.inp(new LogicTuple("tuple_centre", new Value(tcName)));
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					return true;

				} else {
					return false;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			}
	}


	/**
	 * gets the reference to a previosly created local tuple centre.
	 */
	public synchronized TucsonVMUsers getCore(TupleCentreId tid) {
		String realName = tid.getName();
		Object core = cores.get(realName);
		if (core != null) {
			return (TucsonVMUsers) core;
		} else {
			return null;
		}
	}
	
	// Persistency
	
	public synchronized void enablePersistence(TupleArgument template) {
		persistencyTemplate=template;	
			try {
				Iterator it = cores.values().iterator();
				while (it.hasNext()){
					TucsonVMUsers vmContext = (TucsonVMUsers) it.next();
					TucsonVM vm = vmContext.getVirtualMachine();
					try {
						String tcName = vm.getId().toString();
						TupleArgument tid = Value.parse(tcName);
						if (template.match(tid)){
							vm.enablePersistence(PERSISTENCY_PATH+"tc_"+tcName+".dat");
							configContext.out(new LogicTuple("is_persistent", new Value(tcName)));
							log("persistency on tuple centre " + tcName + "enabled.");
						}
					} catch (Exception ex){
						ex.printStackTrace();
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
	}
	
	public synchronized void disablePersistence(TupleArgument template) {
		//if (persistencyTemplate!=null){
			try {
				Iterator it = cores.values().iterator();
				while (it.hasNext()){
					TucsonVMUsers vmContext = (TucsonVMUsers) it.next();
					TucsonVM vm = vmContext.getVirtualMachine();
					try {
						String tcName = vm.getId().toString();
						TupleArgument tid = Value.parse(tcName);
						if (template.match(tid)){
							vm.disablePersistence();
							configContext.inp(new LogicTuple("is_persistent", new Value(tcName)));
							log("persistency on tuple centre " + tcName + "disenbled.");
						}
					} catch (Exception ex){
						ex.printStackTrace();
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			if (persistencyTemplate.match(template)){
				persistencyTemplate=null;
			}
		//}
	}


	private void checkPersistentTupleCentres(String dirName){
		try {
			File dir = new File(dirName);
			if (dir.exists() && dir.isDirectory()){
				String[] files = dir.list();
				for (int i=0; i<files.length; i++){
					if (files[i].startsWith("tc_") && files[i].endsWith(".dat")){
						String tcName = files[i].substring(3,files[i].length()-4);
						TucsonVM vm = bootTupleCentre(tcName).getVirtualMachine();
						vm.loadPersistentInformation(PERSISTENCY_PATH+files[i]);
						vm.enablePersistence(PERSISTENCY_PATH+files[i]);
						configContext.out(new LogicTuple("is_persistent", new Value(tcName)));
						outMsg("restore persistent tuple centre: "+tcName);
					}
				}
			} else {
				dir.mkdir();
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}

	/**
	 * Gets the exact date in which the node was installed
	 *
	 */
	public Date getInstallationDate(){
		return installationDate;
	}

	private void log(String m) {
		System.out.println("[TuCSoN NODE] " + m);
	}

	private void outMsg(String m) {
		System.out.println(m);
	}

	private void setupConfiguration(String configFile) throws TucsonException {
		try {
			alice.tuprolog.lib.JavaLibrary jlib = (alice.tuprolog.lib.JavaLibrary)
					configManager.getLibrary("alice.tuprolog.lib.JavaLibrary");
			jlib.register(new alice.tuprolog.Struct("config"), this);
		} catch (Exception ex) {
			throw new TucsonException("Internal Failure: loading JavaLibrary in Prolog Configuration Engine.");
		}
		if (configFile != null) {
			try {
				InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(BOOT_SETUP_THEORY);
				Theory setup_th = new Theory(new BufferedInputStream(is));
				configManager.setTheory(setup_th);
			} catch (alice.tuprolog.InvalidTheoryException ex) {
				throw new TucsonException("Internal Failure: invalid Prolog Configuration Engine theory.");
			} catch (IOException ex) {
				throw new TucsonException("Internal Failure: invalid Prolog Configuration Engine theory.");
			}
			try {
				Theory cfg_th = new Theory(new FileInputStream(configFile));
				configManager.addTheory(cfg_th);
				alice.tuprolog.SolveInfo info = configManager.solve("setup.");
				if (!info.isSuccess()) {
					throw new InvalidConfigException();
				}
			} catch (alice.tuprolog.PrologException ex) {
				throw new InvalidConfigException();
			} catch (IOException ex) {
				throw new TucsonException("Internal Failure: invalid Prolog Configuration Engine theory.");
			}
		}
	}


	private void setupConfigTupleCentre() {
		//
		// setup management tuple centre
		//
		try {
			configTupleCentreInfo = bootTupleCentre(idConfigTC.getName());
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(DEFAULT_BOOT_SPEC_FILE);
			String spec = alice.util.Tools.loadText(new BufferedInputStream(is));
			boolean ok = configTupleCentreInfo.getVirtualMachine().setReactionSpec(spec);
			if (!ok) {
				log("Invalid reaction specification for the Management tuple centre");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//
		// config spec initialisation
		//
		try {
			configContext = new LocalTupleCentreContext(
					new AgentId("node_agent"), configTupleCentreInfo.getVirtualMachine());
			configContext.out(new LogicTuple("boot"));
			//
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//
	}
	
	private void setupObsTupleCentre() {
		//
		// setup management tuple centre
		//
		try {

			obsTupleCentreInfo = bootTupleCentre(idObsTC.getName());

			InputStream is = ClassLoader.getSystemResourceAsStream(DEFAULT_OBS_SPEC_FILE);
			String spec = alice.util.Tools.loadText(new BufferedInputStream(is));
			boolean ok = obsTupleCentreInfo.getVirtualMachine().setReactionSpec(spec);
			if (!ok) {
				log("Invalid reaction specification for the observation tuple centre");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//
		// config spec initialisation
		//
		try {
			obsContext = new LocalTupleCentreContext(
					new AgentId("node_agent"), obsTupleCentreInfo.getVirtualMachine());
			obsContext.out(new LogicTuple("boot"));
			obsService = new ObservationService(obsContext);
			//
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//
	}
	

	private void bootManagementAgents() {

		new NodeManagementAgent(configTupleCentreInfo.getVirtualMachine(),this);

		ContextManager ctxman = new ContextManager(this, configTupleCentreInfo.getVirtualMachine());
		new WelcomeAgent(tcp_port,this,ctxman);
	}

	
	synchronized public void addAgent(AgentId aid){
		boolean present = false;
		if (!agents.contains(aid)){
			agents.add(aid);
		} else {
			present = true;
		}
		if (observed && !present){
			obsService.accEntered(aid);
		}
	}
	
	synchronized public void removeAgent(AgentId aid){
		boolean present = true;
		if (agents.contains(aid)){
			agents.remove(aid);
		} else {
			present = false;
		}
		if (observed && present){
			obsService.accQuit(aid);
		}
	}

	// configuration ---------------------------------

	public static boolean isInstalled() {
		return instance != null;
	}

	public static Node getInstance() {
		return instance;
	}

	// observation service ----------------------------------


	synchronized public void activateObservability(){
		observed = true;
		try {
			Iterator it = cores.values().iterator();
			while (it.hasNext()){
				TucsonVMUsers vmContext = (TucsonVMUsers) it.next();
				TucsonVM vm = vmContext.getVirtualMachine();
				try {
					if (!vm.getId().equals(idObsTC)){
						vm.addObserver(obsService);
					}
				} catch (Exception ex){
					ex.printStackTrace();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		log(" observation service enabled.");
	}

	synchronized public void deactivateObservability(){
		observed = false;
		try {
			Iterator it = cores.values().iterator();
			while (it.hasNext()){
				TucsonVMUsers vmContext = (TucsonVMUsers) it.next();
				TucsonVM vm = vmContext.getVirtualMachine();
				try {
					if (!vm.getId().equals(idObsTC)){
						vm.removeObserver(obsService);
					}
				} catch (Exception ex){
					ex.printStackTrace();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		log(" observation service disabled.");
	}

	public NodeEventListener getListener(){
		return obsService;
	}
	

	// ------------------------------------------------------------------------------------



	public static void main(String args[]) {
		if (alice.util.Tools.isOpt(args, "-help") || alice.util.Tools.isOpt(args, "-?")) {
			System.err.println("Arguments: -help -port {portNumber} -config {configFile} } ");
		} else {
			String configInfo = alice.util.Tools.getOpt(args, "-config");
			String portInfo = alice.util.Tools.getOpt(args, "-port");
			String persistencyInfo = alice.util.Tools.getOpt(args, "-persistency");
			try {
				int portNumber = Node.DEFAULT_TCP_PORT;
				if (portInfo!=null){
					try {
						portNumber = Integer.parseInt(portInfo);
						if (portNumber<0 || portNumber>64000){
							System.err.println("Invalid port number.");
							System.exit(-1);
						}
					} catch (Exception ex){
						System.err.println("Invalid port number.");
						System.exit(-1);
					}
				}
				TupleArgument template = null;
				if (persistencyInfo!=null){
					try {
						template = TupleArgument.parse(persistencyInfo);
					} catch (Exception ex){
						System.err.println("Invalid persistency template.");
						System.exit(-1);
					}
				}
				new Node(configInfo,portNumber,template).install();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	
	
}


TuCSoN version 1.4.0
---------------------------

This directory includes the sources, binaries and documentation 
concerning TuCSoN technology, version 1.4.0.


Main Directories description -------------------------------------------------

src

  contains all the java sources of the developed technologies, organised in
  related packages;  in particular:

  - alice.tucson: the TuCSoN coordination infrastructure, with
    related tools and ide. In particular:
    
    * alice.tucson.runtime package contains the runtime of the infrastructure,
      which can be installed by launching the alice.tucson.runtime.Node application
    
    * alice.tucson.api package contains the API to build TuCSoN agents
    
    The packages used by this package are  alice.respect, alice.logictuple, alice.tuplecentre


  - alice.logictuple:  contains classes and interfaces to define a logic tuple
    communication language, used in particular in TuCSoN API
    
    The packages used by this package are alice.tuplecentre, alice.tuprolog

  - alice.demo.tucson: contains demos of the technology.

	In particular 
	
	* simple and simple2 - these are naive demos to get in touch with TuCSoN API
	* chat - a simple distributed chat application
	* philo - classic philosopher problems solved in TuCSoN
	* multimedia - a task allocation problem concerning distributed rendering of an image
	* tictactoe - a distributed version of the classic game
	


  Packages used by the technology:

  - alice.tuprolog:  contains sources of the 2P prolog system.

  - alice.tuprologx:  contains extensions for 2P prolog system, such as
                      libraries, runtimes, IDEs.
  - alice.tuplecentre: contains the (abstract) framework of tuple centre
    coordination  media, defining interfaces and classes for the
    coordination language, the communication language, the behaviour
    specification language and the tuple centre virtual machine.

  - alice.respect: contains classes and interfaces to define the logic tuple
    centre model based on the ReSpecT reaction specification language
    * main packages used:    alice.tuplecentre, alice.logictuple, alice.tuprolog

  - alice.util: contains helper classes

lib

  Contains the tucson.jar file, containing the packages alice.tucson.*.

bin

  Contains the classes of the technology, including the demos.
   
doc
  
  Contains TuCSoN documentation:
  
  - guide.pdf (User and Developer guide)
  - apidocs contains the JavaDoc of the TuCSoN API
  

-------------------------------------------------------------------------

Brief HOWTO (refer to the Guide for details) 

The TuCSoN infrastructure can be installed by launching:

	java -cp tucson.jar alice.tucson.runtime.Node
	
or from the build directory:	

	java alice.tucson.runtime.Node

In order to spawn the CLIAgent tool:

	java -cp tucson.jar alice.tucson.ide.CLIAgent
	
In order to spwan an Inspector:

	java -cp tucson.jar alice.tucson.ide.Inspector

In order to use TuCSoN API to build agents import
the classes of alice.tucson.api and alice.logictuple 
packages.

In order to execute the demos, open a terminal 
(command line interpreter),  change your current 
directory to the TuCSoN build directory, then 
you can execute the application according to 
the specific demo  (refer to readme.txt files found  
in each demo src directory for details).



	
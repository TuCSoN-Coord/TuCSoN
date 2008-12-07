ReSpecT Library v. 2.2 (Trunk version: next 2.3)

The library implements the ReSpecT tuple centre model,
that can be used for  developing agent-based concurrent
systems.

This is ReSpecT's second version implementing the syntax and semantics as
defined in "Formal ReSpecT in the A&A Perspective".

Not all the feature of this new version are implemented. A list of the 
implemented features follows:

-the new computational model based on the concept of "guard"
-all the guard are implemented except for "after" and "before"
-all the predicates are implemented except for the ones beginning with "start"
-in this version, only the internal execution model is implemented: neither 
	the concept of linkability nor the concept of time is implemented.

Furthermore, this version lacks in the implementation of the operations ending
in _s (representing operations affecting the behaviour specification)

***Additions:

- fixed some errors in time-reaction management
- introduced linkability
- "before" and "after"
- timed reactions
- the predicates to manage time 

***dependecies:

- tuProlog 1.3 
- tuplecentre (next 3.0.1)
- utils 1.0

Package structure:

- alice.respect.api: 
contains the basic API classes and interfaces
for using ReSpecT tuple centres.

- alice.respect.core: 
contains the ReSpecT tuple centre virtual machine.

- alice.respect.tools:
contains the tools for managining / debugging tuple centres.




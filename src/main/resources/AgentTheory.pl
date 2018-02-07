%:-initialization((agent_name(N), node_address(A), node_port(P), acquire_acc(N, A, P))).

agent_name($agentName).
node_address($nodeAddress).
node_port($nodePort).

agent_execution :-
  agent_name(Name),
  node_address(Address),
  node_port(Port),
  acquire_acc(Name, Address, Port),
  write("Acquired ACC on node "), write(Address), write(":"), write(Port), write(" for agent "), write(Name), nl,
  (agent_loop; true),
  release_acc,
  write("Released ACC for agent "), write(Name).
  
agent_loop :- !,
  agent_loop_step,
  agent_loop.
  
agent_loop_step :-
  write("Doing something..."), nl,
  thread_sleep(1000). % ms  
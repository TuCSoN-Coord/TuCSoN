
:- load_library('alice.respect.api.Respect2PLibrary').


go:-
    stdout <- println('agent consumer started.),
    consume.
	
consume:-
    default ? in(q(X),1000),!,
    stdout <- println(X),
    consume.

consume:-
	stdout <- println('time out, agent consumed quitting.').
    
 
    

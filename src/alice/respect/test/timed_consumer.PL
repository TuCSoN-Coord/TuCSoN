
:- load_library('alice.respect.api.Respect2PLibrary').

go:-
    agent_id(Id),
    mylog(['[AGENT CONSUMER ',Id,'] started.']),
    consume.
	
consume:-
    default ? in(q(X),2000,Op),
    completed(Op),!,
    stdout <- println(X),
    consume.

consume:-
    agent_id(Id),
	log(['[AGENT CONSUMER ',Id,'] time out, going to quit.']).
    
    
mylog([]):-
	stdout <- println.
mylog([X|T]):-
	stdout <- print(X), !, mylog(T).    

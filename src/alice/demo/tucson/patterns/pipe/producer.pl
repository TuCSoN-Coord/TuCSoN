
:- load_library('alice.tuprologx.lib.TucsonLibrary').
:- solve(loop(100)).


loop(0):-!.

loop(N):-
    out(channel(p(N))),
    current_thread <- sleep(20),
    N1 is N - 1,
    loop(N1).
    
    
    

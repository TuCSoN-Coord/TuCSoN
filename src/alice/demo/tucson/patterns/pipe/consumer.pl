
:- load_library('alice.tuprologx.lib.TucsonLibrary').
:- solve(loop).


loop:-
    in(channel(X)),
    stdout <- println(X),
    current_thread <- sleep(50),
    loop.
    
    
    

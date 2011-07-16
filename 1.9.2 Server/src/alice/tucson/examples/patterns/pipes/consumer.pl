
:- load_library('alice.tucson.api.Tucson2PLibrary').
:- solve(loop).


loop:-
    in(channel(X)),
    stdout <- println(X),
    current_thread <- sleep(50),
    loop.
    
    
    

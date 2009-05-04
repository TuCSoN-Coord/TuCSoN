
:- load_library('alice.respect.api.Respect2PLibrary').

produce(100):-!.
produce(X):-
    default ? out(p(X)),
    Y is X + 1,
    produce(Y).
    

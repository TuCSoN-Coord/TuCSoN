
:- load_library('alice.respect.api.Respect2PLibrary').


go:-
    default ? set_spec('reaction(out(p(X)),(in_r(p(X)),out_r(q(X)) )).'),
    stdout <- println('spec set.'),
    default ? get_spec(S),
    stdout <- println(S).
    

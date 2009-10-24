:- load_library('alice.tucson.api.Tucson2PLibrary').

:- solve(go).

go:-
test tc ? out(p('hello world')),
test tc ? in(p(X)),
write(X), nl.
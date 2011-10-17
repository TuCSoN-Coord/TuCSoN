:- load_library('alice.tucson.api.Tucson2PLibrary').
:- solve(go).

go:-
  enter_context([agent_id(hank)]),
  test_tc ? out(p('hello world')),
  test_tc ? in(p(X)),
  exit_context, write(X), nl.
:- load_library('alice.tucson.api.Tucson2PLibrary').


go:-
	enter_default_context,
	test ? out(p(1)),
	exit_context.
	
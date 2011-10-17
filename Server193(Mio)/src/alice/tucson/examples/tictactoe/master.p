%% **** Title:	TicTacToe in LuCe - Master Agent
%% **** File: 	master.p
%% **** Author:	Enrico Denti, Andrea Omicini
%% **** Date: 	30/08/1999

:- load_library('alice.tucson.api.Tucson2PLibrary').
:- solve(master).

master:-
	initialise(tictactoe),
	main_loop(tictactoe,'alice/tucson/examples/tictactoe/expert.p').

initialise(TC):-
        text_from_file('alice/tucson/examples/tictactoe/tictactoe.rsp',S),
        TC ? set_spec(S),
	TC ? out(init([
		first(x),
		opponent(x,o),
		opponent(o,x),
		init(g(_,_,_, _,_,_, _,_,_)),
		nextID(1)
	])).

main_loop(TC,File):-
	TC ? in(new(ID)),
        text_from_file(File,Source),
        agent(Source,expert(TC,ID,1)),
	% *** EACH AGENT STARTS WITH N=1
	write('New expert created for game '),write(ID),nl,!,
	main_loop(TC,File).


%% **** Title:  TicTacToe in LuCe - Expert Agent
%% **** File: 	expert.p
%% **** Author:	Enrico Denti
%% **** Date: 	31/08/1999

:- load_library('alice.tuprologx.lib.TucsonLibrary').

expert(TC,ID,N):-
        current_thread <- sleep(250),
	TC ? in(game(ID,N,Grid,Status)),
	perform(TC,ID,N,NewN,Grid,Status),!,
	expert(TC,ID,NewN).


perform(TC,ID,N,NewN,_,past):-
        NewN is N + 1.

perform(TC,ID,N,N,_,end):-
	halt.

perform(TC,ID,N,N,Grid,proposed(Role,Move)):-
	validate(TC,ID,N,Grid,Role,Move).

perform(TC,ID,N,N,Grid,turn(Role)):-
	play(TC,ID,N,Grid,Role).

validate(TC,ID,N,Grid,Role,Move):-
        valid(Role,Move,Grid,Status), !,
	NewN is N+1,
	TC ? out(step(ID,Role,Move,NewN,Grid,Status)).

validate(TC,ID,N,Grid,Role,Move):-
	TC ? out(step(ID,Role,Move,N,Grid,playing(Role))).

play(TC,ID,N,Grid,Role):-
	mymove(Grid,Role,Move,Status),
	NewN is N + 1,
	TC ? out(step(ID,Role,Move,NewN,Grid,Status)).

%------------------- PARTE NON MODIFICATA -------------

valid(Role,Move,Grid,Status):-
	next(Move,Role1,Grid),
	unbound(Role1), !, Role = Role1,
	gameStatus(Grid,Role,Status).


gameStatus(Grid,Role,win(Role)):- win(Role,Grid), !.
gameStatus(Grid,_,stalemate):- gridFull(Grid), !.
gameStatus(Grid,Role,playing(Opp)):- opponent(Role,Opp).


gridFull(g(V0,V1,V2,V3,V4,V5,V6,V7,V8)):-
	atom(V0), atom(V1), atom(V2),
	atom(V3), atom(V4), atom(V5),
	atom(V6), atom(V7), atom(V8).


mymove(Grid,Role,Move,win(Role)):-
	canWin(Grid,Role,Move),
	!.
mymove(Grid,Role,Move,Status):-
	next(Move,Role,Grid),
	opponent(Role,Other),
	not canWin(Grid,Other,_),
	!,
	gameStatus(Grid,Role,Status).

mymove(Grid,Role,Move,Status):-
	% --- hai già perso
	next(Move,Role,Grid),
	!,
	gameStatus(Grid,Role,Status),
	true.


canWin(Grid,Role,Move):-
	next(Move,Role,Grid),
	win(Role,Grid).

%% --------------------------

next(on(0,0), Role, g(X,_,_, _,_,_, _,_,_)):- unbound(X), Role = X.
next(on(0,1), Role, g(_,X,_, _,_,_, _,_,_)):- unbound(X), Role = X.
next(on(0,2), Role, g(_,_,X, _,_,_, _,_,_)):- unbound(X), Role = X.
next(on(1,0), Role, g(_,_,_, X,_,_, _,_,_)):- unbound(X), Role = X.
next(on(1,1), Role, g(_,_,_, _,X,_, _,_,_)):- unbound(X), Role = X.
next(on(1,2), Role, g(_,_,_, _,_,X, _,_,_)):- unbound(X), Role = X.
next(on(2,0), Role, g(_,_,_, _,_,_, X,_,_)):- unbound(X), Role = X.
next(on(2,1), Role, g(_,_,_, _,_,_, _,X,_)):- unbound(X), Role = X.
next(on(2,2), Role, g(_,_,_, _,_,_, _,_,X)):- unbound(X), Role = X.

%% --------------------------

win(Role, g(X,Y,Z, _,_,_, _,_,_)):- checkWin(Role,X,Y,Z).
win(Role, g(_,_,_, X,Y,Z, _,_,_)):- checkWin(Role,X,Y,Z).
win(Role, g(_,_,_, _,_,_, X,Y,Z)):- checkWin(Role,X,Y,Z).
win(Role, g(X,_,_, Y,_,_, Z,_,_)):- checkWin(Role,X,Y,Z).
win(Role, g(_,X,_, _,Y,_, _,Z,_)):- checkWin(Role,X,Y,Z).
win(Role, g(_,_,X, _,_,Y, _,_,Z)):- checkWin(Role,X,Y,Z).
win(Role, g(X,_,_, _,Y,_, _,_,Z)):- checkWin(Role,X,Y,Z).
win(Role, g(_,_,X, _,Y,_, Z,_,_)):- checkWin(Role,X,Y,Z).

checkWin(Role,X,Y,Z):-
	atom(X), atom(Y), atom(Z),
	Role = X,  X = Y,  Y = Z.

%% --------------------------

opponent(x,o).
opponent(o,x).

%% --------------------------

%% EOF

% doActivity(+Tc):- performs the factorial computation reading jobs from
% tuple centre <Tc>.
doActivity(Tc):-
    write("Waiting for jobs in <"), write(Tc), write(">..."), nl,
    in(fact(num(Fact))),
    write("Computing factorial for <"), write(Fact), write(">..."), nl,
    computeFact(Fact, Res),
    write("Computed factorial <"), write(Res), write(">."), nl,
    out(res(fact(Res))).

% computeFact(+Fact, -Res):- computes factorial of <Fact>, storing the
% result in <Res>.
computeFact(Fact, Res):-
    computeFact(Fact, Res, 1).

    computeFact(0, Res, Res):- !.
    computeFact(1, Res, Res):- !.
    computeFact(Fact, Res, Temp):-
        Fact2 is Fact-1, Temp2 is Fact * Temp,
        computeFact(Fact2, Res, Temp2).

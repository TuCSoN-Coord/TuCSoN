% runHelloWorld(+Aid, +Where):- run the classic "hello world" example
% using a Prolog TuCSoN agent named <Aid> on tuple centre <Where>.

runHelloWorld(Aid, Where):-
    acquireACC(Aid),
    writeHello(Where),
    readHello(Where, Res),
    (
        Res == hello(world), !,
        write("Success: "), write(Res), nl
    ;
        Res \== hello(world),
        write("Failure:("), nl
    ),
    releaseACC.
    
% acquireACC(+Aid):- acquires the (default) ACC, necessary to 
% interact with a TuCSoN node.
acquireACC(Aid):-
    write("Acquiring ACC as <"), write(Aid), write(">..."), nl,
    acquire_acc(Aid),
    write("ACC acquired."), nl.
    
% writeHello(+Where):- writes the <hello(world)> tuple in the
% given TuCSoN tuple centre.
writeHello(Where):-
    write("Writing tuple..."), nl,
    out(hello(world), Where),
    write("Tuple written."), nl.
    
% readHello(+Where, -Res):- reads the <hello(world)> tuple from
% the given TuCSoN tuple centre, blocking if necessary.
readHello(Where, Res):-
    write("Reading tuple..."), nl,
    rd(hello(world), Where),
    write("Tuple read."), nl,
    Res = hello(world).
    
% releaseACC:- releases the ACC held, if any.
releaseACC:-
    write("Releasing ACC..."), nl,
    release_acc,
    write("ACC released."), nl.

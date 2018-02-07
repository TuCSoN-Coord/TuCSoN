% runMasterAgent(+Aid, +Where, +Iters, +MaxFact):- runs a master-workers architecture,
% in which the workers are spawned Prolog computations. The <Aid> master submits <Iters> jobs
% regarding factorial computations (up to <MaxFact>) into <Where> tuple centres.
runMasterAgent(Aid, Where, Iters, MaxFact):-
    acquireACC(Aid),
    submitJobs(Where, Iters, MaxFact),
    spawnWorkers(Where, Iters),
    collectResults(Where, Iters),
    releaseACC.
    
% acquireACC(+Aid):- acquires the (default) ACC, necessary to 
% interact with a TuCSoN node.
acquireACC(Aid):-
    write("Acquiring ACC as <"), write(Aid), write(">..."), nl,
    acquire_acc(Aid),
    write("ACC acquired."), nl.
    
% submitJobs(+Where, +Iters, +MaxFact):- submits <Iters> jobs regarding factorial computations
% (up to <MaxFact>) into <Where> tuple centres.
submitJobs([], _, _).
submitJobs([Tc|List], Iters, MaxFact):-
    write("Submitting jobs to <"), write(Tc), write("> tuple centre..."), nl,
    submit(Tc, Iters, MaxFact),
    write("Jobs submitted."), nl,
    submitJobs(List, Iters, MaxFact).
    
    submit(_, Left, _):- Left =< 0, !.
    submit(Tc, Left, MaxFact):- Left > 0,
        Left2 is Left-1,
        rand_int(MaxFact, Fact),
        write("Submitting factorial of <"), write(Fact), write(">..."), nl,
        out(fact(num(Fact)), Tc),
        write("Factorial submitted."), nl,
        submit(Tc, Left2, MaxFact).

% spawnWorkers(+Where, +Iters):- spawns <Iters> workers into each of <Where> tuple centres.
spawnWorkers([], _).
spawnWorkers([Tc|List], Iters):-
    write("Spawning workers in <"), write(Tc), write(">..."), nl,
    spawnW(Tc, Iters),
    write("Workers spawned."), nl,
    spawnWorkers(List, Iters).
    
    spawnW(_, Left):- Left =< 0, !.
    spawnW(Tc, Left):- Left > 0,
        Left2 is Left-1,
        write("Spawning worker..."), nl,
        spawn(solve('alice/tucson/examples/spawnedWorkers/spawnedWorkingActivity.pl', doActivity(Tc)), Tc),
        write("Worker spawned."), nl,
        spawnW(Tc, Left2).

% collectResults(+Where, +Iters):- collects the <Iters> results form each of <Where>
% tuple centres.
collectResults([], _).
collectResults([Tc|List], Iters):-
    write("Collecting results from <"), write(Tc), write("> tuple centre..."), nl,
    collect(Tc, Iters),
    write("Results collected."), nl,
    collectResults(List, Iters).
    
    collect(_, Left):- Left =< 0, !.
    collect(Tc, Left):- Left > 0,
        in(res(fact(R)), Tc),
        Left2 is Left-1,
        write("Collected factorial <"), write(res(fact(R))), write(">."), nl,
        collect(Tc, Left2).

% releaseACC:- releases the ACC held, if any.
releaseACC:-
    write("Releasing ACC..."), nl,
    release_acc,
    write("ACC released."), nl.

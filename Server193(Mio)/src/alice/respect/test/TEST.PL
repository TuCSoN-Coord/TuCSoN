
:- load_library('alice.tuprolog.lib.RespectTCLibrary').

test:-
    text_from_file('../src/alice/respect/test/consumer.pl',X), agent(X,consume),
    text_from_file('../src/alice/respect/test/producer.pl',Y), agent(Y,produce(0)).

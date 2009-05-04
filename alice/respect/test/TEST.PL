
:- load_library('alice.tuprologx.lib.RespectTCLibrary').

test:-
    text_from_file('../src/alice/test/respect/consumer.pl',X), agent(X,consume),
    text_from_file('../src/alice/test/respect/producer.pl',Y), agent(Y,produce(0)).

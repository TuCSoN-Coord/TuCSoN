





:- load_library('alice.tuprologx.lib.TucsonLibrary').

main(Address):-
    write(Address),nl,
    text_from_file('alice/demo/tucson/whiteboard/spec.rsp',Text),
    whiteboard @ Address ? set_spec(Text),
    write('ok.').
    
    
    
    

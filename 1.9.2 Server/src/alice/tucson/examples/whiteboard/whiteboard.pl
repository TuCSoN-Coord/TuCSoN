:- load_library('alice.tucson.api.Tucson2PLibrary').

main(Address):-
    write(Address),nl,
    text_from_file('alice/tucson/examples/whiteboard/spec.rsp',Text),
    whiteboard @ Address ? set_spec(Text),
    write('ok.').
    
    
    
    

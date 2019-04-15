
%
% TuCSoN setup theory
% 

setup:-
    stdout <- println('evaluating configuration info...'),
    %
    setup_port,
    %    
    setup_boot_spec_file,
    %
    stdout <- println('evaluating configuration info... OK').        
        
setup_port:-
    port(X) -> (config <- setPort(X)), 
        writel(['infrastructure port set to ',X,nl]); true.

setup_boot_spec_file:-
    boot_spec_file(BF) -> (config <- setBootSpecFile(BF)),
        writel(['boot specification file set to ',BF,nl]);true.

writel([]).
writel([nl|L]):-!,
    stdout <- println, writel(L).        
writel([X|L]):-
    stdout <- print(X), writel(L).        
    

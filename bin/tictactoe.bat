start java  -cp ../lib/tucson-full.jar alice.tucson.runtime.Node
java  -cp ../lib/tucson-full.jar alice.util.Sleep 5000

start java -cp ../lib/tucson-full.jar alice.tuprolog.Agent alice/demo/tucson/tictactoe/master.p 
java  -cp ../lib/tucson-full.jar alice.util.Sleep 6000
start java  -cp ../lib/tucson-full.jar alice.demo.tucson.tictactoe.AgentPlayer hank localhost


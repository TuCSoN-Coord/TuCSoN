start java -cp ../lib/tucson-full.jar alice.tucson.runtime.Node
java -cp ../lib/tucson-full.jar alice.util.Sleep 6000

start java -cp ../lib/tucson_full.jar java alice.demo.tucson.multimedia.MasterAgent localhost
java -cp ../lib/tucson-full.jar alice.util.Sleep 4000

start java -cp ../lib/tucson-full.jar java alice.demo.tucson.multimedia.WorkerAgent worker_0
start java -cp ../lib/tucson-full.jar java alice.demo.tucson.multimedia.WorkerAgent worker_1

start java -cp ../lib/tucson-full.jar alice.tucson.runtime.Node
java  -cp ../lib/tucson-full.jar alice.util.Sleep 5000

start java -cp ../lib/tucson-full.jar alice.demo.tucson.chat.ChatRoom localhost
java  -cp ../lib/tucson-full.jar alice.util.Sleep 4000

start java -cp ../lib/tucson-full.jar alice.demo.tucson.chat.ChatAgent Bob     localhost
start java -cp ../lib/tucson-full.jar alice.demo.tucson.chat.ChatAgent Alice   localhost
start java -cp ../lib/tucson-full.jar alice.demo.tucson.chat.ChatAgent Paula   localhost

TicTacToe demo
--------------

This is distributed version of the tic tac toe game.

A Master agent is required to setup the application, installing the behaviour
specification on the tictactoe tuple centre.

The Master agent is a Prolog agent and master.p contains its source code.

Players can join the game using spawning using the java agent
GUIAgent, which provides a graphical interface to the game.

If there is only one player, the computer plays as opponent.

Be sure to have the alice/demo/tucson package accessible from your path.

To spawn the master agent:

        java -cp .;tucson.jar alice.tuprolog.Agent
                                alice/demo/tucson/tictactoe/master.p

Then to spawn (distribute) players:

        java -cp .;tucson.jar alice.demo.tucson.tictactoe.GUIAgent <address>

where <address> is the IP address of the TuCSoN node where the game is played.

es:
        java -cp .;lib/tucson.jar demo.tucson.tictactoe.GUIAgent localhost




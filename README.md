# Battleship
Battleship game for CS2043

## General
The main file of ther server is BattleShip, it will print the port number for the two clients. The first to connect will be player one, the second will be player two.

The main file for running a client is Client, enter the port given by the server and the server IP address when prompted to connect.

## Running the Server
In order to run the updated server, the database and sqlite driver need to be in the same folder as the Server code. If you are running Windows, use these calls to compile and run the Server 

javac -classpath ".;sqlite-jdbc-3.16.1.jar" BattleShip.java

java -classpath ".;sqlite-jdbc-3.16.1.jar" BattleShip

If you’re using Linux make these calls:

javac -classpath ".:sqlite-jdbc-3.16.1.jar" BattleShip.java

java -classpath ".:sqlite-jdbc-3.16.1.jar" BattleShip

## Other Important Information
There must be a folder named "Images" in the Client folder; however, the game should run without the images.

There must be a folder named "music" in the Client folder; however, the game should run without the music and FX.

Returning to the home screen after signing in or registering will cause errors in the game. The protocol does not support moving back and forth between game states. Although, returing home at the leaderboards screen is possible as the server will restart each time and run until the connection is broken with a client. Returning home at this stage in the game will reset everything and you will be able to play again.

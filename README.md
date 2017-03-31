# Battleship
Battleship game for CS2043

## General
The main file of ther server is BattleShip, it will print the port number for the two clients. The first to connect will be player one, the second will be player two.

The main file for running a client is Client, enter the port given by the server to connect.

## Running the Server
In order to run the updated server, the database and sqlite driver need to be in the same folder as the Server code. If you are running Windows, use these calls to compile and run the Server 

javac -classpath ".;sqlite-jdbc-3.16.1.jar" BattleShip.java

java -classpath ".;sqlite-jdbc-3.16.1.jar" BattleShip

If you’re using Linux make these calls:

javac -classpath ".:sqlite-jdbc-3.16.1.jar" BattleShip.java

java -classpath ".:sqlite-jdbc-3.16.1.jar" BattleShip

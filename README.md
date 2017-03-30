# Battleship
Battleship game for CS2043

## General
The main file of ther server is BattleShip, it will print two port numbers. The player to connect to the first port will be "player1" and the other player will be "player2".

The main file for running a client is Client, enter one of the ports given by the server to connect.

## Running Server
In order to run the updated server, the database and sqlite driver need to be in the same folder as the Server code. If you are running Windows, use these calls to compile and run the Server 

javac -classpath ".;sqlite-jdbc-3.16.1.jar" BattleShip.java

java -classpath ".;sqlite-jdbc-3.16.1.jar" BattleShip

If you’re using Linux make these calls:

javac -classpath ".:sqlite-jdbc-3.16.1.jar" BattleShip.java
java -classpath ".:sqlite-jdbc-3.16.1.jar" BattleShip

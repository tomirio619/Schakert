# Schakert
![](https://travis-ci.org/tomirio619/Schakert.svg?branch=master)
Schakert, a variation of the dutch word for playing chess (schaken), is a chess engine written in Java.
Although I am aware of the fact that Java probably isn't the best language to write a chess engine in,
it does help me to get a better understanding of the game.
While using an Object Oriented (OO), you learn a lot about common misstakes when writing such a complex piece of sofware.

Currently, Schakert is in its infacy.
Somewhere below is a list of features that are currently supported/implemented.
Also new features which are planned to be implemented are shown.

##Features
* Comes with own GUI
* When a chess piece is selected, all the possible legal moves are shown
* Game navitagation buttons enable the user to undo and redo moves
* FEN strings can be exported and loaded
* Detection of stalemate, check and checkmate
* A very basic AI, which will definitely be improved in upcomming commits
* When a pawn promotes, the user is able to select the new type of the pawn
* Perft divide
* Negamax search
* Piece-square tables
 
##TODO general
* Draw by insuficcient material, three fold repitition and the fifty move rule.
* Let the user decide what kind of players plays which colour (eg. AI vs AI, Human vs AI)
* Start a new match
* UCI support
* Loading other engines
* Bitboard implementation

##TODO search
* Principal variation search
* Iterative deepening
* Transposition table (Zobrist)
* Null and Killer moves
* Late move reductions
* Quiescence search
* Mate distance pruning

##TODO Heuristics
* Mobility
* Pawn structure
* Phase Interpolation

##Building
If you want to build the application yourself, this is very easy.

###Building from CMD
<ol>
<li>Make sure you have installed **Maven**.</li>
<li>Make sure you have set your `JAVA_HOME` environment variable to your JDK installation (e.g. `C:\Program Files\Java\jdk1.8.0_101`)
<li>Clone the project, extract it to a folder and navigate to this folder with the command prompt.</li>
<li>Now run Maven with the following command in cmd: `mvn jfx:jar `</li>
<li>The jar-file will be placed at `target/jfx/app`
</ol>

###Using an IDE
This project is correctly setup for the Netbeans IDE.
Although any IDE should suffice, you can easily clone the project into your Netbeans IDE and you are ready to go

##Preview


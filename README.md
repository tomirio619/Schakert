# Schakert
Schakert, a variation of the dutch word for playing chess (schaken), is a chess engine written in Java.
Although I am aware of the fact that Java probably isn't the best language to write a chess engine in,
it does help me to get a better understanding of the game.
While using an Object Oriented (OO), you learn a lot about common misstakes when writing such a complex piece of sofware.

Currently, Schakert is in its infacy.
Below is an emuration of features that are currently supported/implemented.
Also a list of new features which are planned to be implemented is shown.

##Features
* Comes with own GUI
* When a chess piece is selected, all the possible legal moves are shown
* Game navitagation buttons enable the user to undo and redo moves
* FEN strings can be exported and loaded
* Detection of stalemate, check and checkmate
* A very basic AI, which will definitely be improved in upcomming commits
* When a pawn promotes, the user is able to select the new type of the pawn (almost done)
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

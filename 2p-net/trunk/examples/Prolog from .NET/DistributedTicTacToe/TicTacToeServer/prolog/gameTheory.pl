/*      TIC-TAC-TOE
 *
 *  Filename:  tictac.pro
 *
 *  Programmer:  Br. David Carlson
 *
 *  Date:  September 23, 1989
 *
 *  Modified:  March 15, 1991 to include new strategy.
 *             November 11, 1993 to block a known way to beat the program.
 *             November 13, 1993 to block another way to beat the program.
 *             December 7, 1999 to run under yap.
 *
 *  This is an intelligent tic-tac-toe program.  The computer chooses its
 *  moves based on certain rules which embody human intuitions about how
 *  to play the game.
 *
 *  A board is represented by a structure of the form:
 *      board(A,B,C,D,E,F,G,H,I).
 *  For example, board(x,o,x,_,o,_,o,_,_) represents the board:
 *
 *       x | o | x
 *      -----------
 *         | o |
 *      -----------
 *       o |   |
 *     
 */

go :- nl, write('Playing tic-tac-toe.'), nl, explainMoves, !, games, !, nl, nl.
         
explainMoves :- nl, write('You choose a move by giving the number of the'),
   nl, write('square to which you wish to move:'), nl, nl,
   write('  1 | 2 | 3'), nl, printline,
   write('  4 | 5 | 6'), nl, printline,
   write('  7 | 8 | 9'), nl.
   /* show square numbers */      

printline :- write(' -----------'), nl.

games :- game, !, optionalGames.
   
optionalGames :- nl, write('Care for another game (y/n)? '),
   inputResponse(Response), !, process(Response).
      
inputResponse(Response) :- read(Answer), okResponse(Answer, Response), !.
   /* error check and put in standard form */
inputResponse(Response) :- write('Not a valid response -- type y or n: '),
   inputResponse(Response).   /* reinput */
      
/* The standard response forms are yes and no: */
okResponse(y, yes).   okResponse('Y', yes).   okResponse(yes, yes).
okResponse('YES', yes).   okResponse('Yes', yes).
okResponse(n, no).   okResponse('N', no).   okResponse(no, no).
okResponse('NO', no).   okResponse('No', no).

process(no) :- !.   /* end the program */
process(yes) :- games.   /* continue play */
  
game :- nl, write('Do you wish to be x or o? '), inputPlayer(Player), nl,
      write('Do you want to go first (y/n)? '), inputResponse(PlayerMoves), !,
      play(Player, PlayerMoves, 9, board(_,_,_,_,_,_,_,_,_)).
      /* 3rd arg of play is # of open squares, originally 9 */

printBoard(board(V1, V2, V3, V4, V5, V6, V7, V8, V9)) :- 
   write(' '), writeVal(V1), write('|'), writeVal(V2), write('|'),
      writeVal(V3), nl, printline,
   write(' '), writeVal(V4), write('|'), writeVal(V5), write('|'),
      writeVal(V6), nl, printline,
   write(' '), writeVal(V7), write('|'), writeVal(V8), write('|'),
      writeVal(V9), nl, nl.

writeVal(V) :- var(V), !, write('   ').
writeVal(x) :- write(' x ').
writeVal(o) :- write(' o ').

inputPlayer(Player) :- read(Player), x_or_o(Player).   /*  error check */
inputPlayer(Player) :- write('Not a valid response -- type x or o: '),
   inputPlayer(Player).   /* reinput Player */
      
x_or_o(x).   x_or_o(o).
      
play(Player, PlayerMoves, NumOpen, Board) :- 
   oneMove(Player, PlayerMoves, Board), !, 
   negate(PlayerMoves, NewPlayerMoves),
   Open is NumOpen - 1, 
   continuePlay(Player, NewPlayerMoves, Open, Board).
      
oneMove(Player, yes, Board) :- !, inputMove(Board, Location),
   makeMove(m(Location, Player), Board).   /* human player moves */
oneMove(Player, no, Board) :- opposite(Player, Computer),
   generateMove(Computer, Board, Location),
   write('My move: '), write(Location), nl,
   makeMove(m(Location, Computer), Board).  /* computer moves */
      
negate(yes, no).   negate(no, yes).
   
opposite(x, o).   opposite(o, x).
   
inputMove(Board, Location) :- nl, write('Your move: '), read(Location),
   location(Location), argb(Location, Board, Val), var(Val). /* must be open */
inputMove(Board, Location) :- nl, write('Not a valid location.'),
   inputMove(Board, Location).   /* reinput location */
           
location(1).   location(2).   location(3).   location(4).   location(5).
location(6).   location(7).   location(8).   location(9).   
      
argb(1, board(Val,_,_,_,_,_,_,_,_), Val) :- !.
argb(2, board(_,Val,_,_,_,_,_,_,_), Val) :- !.
argb(3, board(_,_,Val,_,_,_,_,_,_), Val) :- !.
argb(4, board(_,_,_,Val,_,_,_,_,_), Val) :- !.
argb(5, board(_,_,_,_,Val,_,_,_,_), Val) :- !.
argb(6, board(_,_,_,_,_,Val,_,_,_), Val) :- !.
argb(7, board(_,_,_,_,_,_,Val,_,_), Val) :- !.
argb(8, board(_,_,_,_,_,_,_,Val,_), Val) :- !.
argb(9, board(_,_,_,_,_,_,_,_,Val), Val).
   
makeMove(m(Location, Val), Board) :- argb(Location, Board, Val),
   printBoard(Board).
      
position(1, 1, 2).   position(2, 1, 6).   position(3, 1, 10).   
position(4, 3, 2).   position(5, 3, 6).   position(6, 3, 10).
position(7, 5, 2).   position(8, 5, 6).   position(9, 5, 10).
   
generateMove(Computer, Board, Location) :- slice(Pos1, Pos2, Pos3),
   argb(Pos1, Board, Val1), argb(Pos2, Board, Val2), argb(Pos3, Board, Val3),
   winningMove(Computer, Pos1, Pos2, Pos3, Val1, Val2, Val3, Location), !.
   /* First check if computer can win with this move */
generateMove(_, Board, Location) :- slice(Pos1, Pos2, Pos3),
   argb(Pos1, Board, Val1), argb(Pos2, Board, Val2), argb(Pos3, Board, Val3),
   forcedMove(Pos1, Pos2, Pos3, Val1, Val2, Val3, Location), !.
   /* Then check if computer is forced to move */
generateMove(Computer, Board, Pos3) :- diagonal(Pos1, Pos2, Pos3),
   argb(Pos1, Board, Val1), argb(Pos2, Board, Val2), argb(Pos3, Board, Val3),
   neighbors(Pos3, NbrA, NbrB), 
   argb(NbrA, Board, ValA), argb(NbrB, Board, ValB),
   specialMove(Computer, Val1, Val2, Val3, ValA, ValB), !.
   /* Computer tries to get 2 in a row along a diagonal with 3rd square
    * open and with enemy occupied cells as neighbors of location to which
    * to move, which is a corner.
    */
generateMove(Computer, Board, Pos3) :- diagonal(Pos1, Pos2, Pos3),
   argb(Pos1, Board, Val1), argb(Pos2, Board, Val2), argb(Pos3, Board, Val3),
   neighbors(Pos3, NbrA, NbrB), 
   argb(NbrA, Board, ValA), argb(NbrB, Board, ValB),
   special2Move(Computer, Val1, Val2, Val3, ValA, ValB), !.
   /* Computer tries to get 2 in a row along a diagonal with 3rd square
    * open, location to move to is a corner, and neighbors of this 
    * corner are such that 1 is open and 1 is held by the opponent.
    */
generateMove(Computer, Board, Location) :- slice(Pos1, Pos2, Pos3),
   argb(Pos1, Board, Val1), argb(Pos2, Board, Val2), argb(Pos3, Board, Val3),
   goodMove(Computer, Pos1, Pos3, Val1, Val2, Val3, Location), !.
   /* Computer tries to get 2 in a row with 3rd square open */
generateMove(_, Board, 5) :- argb(5, Board, Val), var(Val), !.
   /* Computer takes the center square, if open */
generateMove(_, Board, Location) :- openCorner(Board, Location), !.
   /* Computer prefers a corner position */
generateMove(_, Board, Location) :- location(Location),
   argb(Location, Board, Val), var(Val), !.   /* take any open square */
   
slice(1,2,3).   slice(4,5,6).   slice(7,8,9).   slice(1,4,7).
slice(2,5,8).   slice(3,6,9).   slice(9,5,1).   slice(7,5,3).

diagonal(1,5,9).   diagonal(9,5,1).   /* list both orders */
diagonal(3,5,7).   diagonal(7,5,3).
   
neighbors(1,2,4).   neighbors(3,2,6).
neighbors(7,4,8).   neighbors(9,6,8).
   
/* A winning move is found if computer has 2 in a row, 3rd square open */
winningMove(Computer, Pos1,_,_, Val1, Val2, Val3, Pos1) :- var(Val1),
   nonvar(Val2), Val2 == Computer, nonvar(Val3), Val3 == Computer, !.
winningMove(Computer,_, Pos2,_, Val1, Val2, Val3, Pos2) :- var(Val2),
   nonvar(Val1), Val1 == Computer, nonvar(Val3), Val3 == Computer, !.
winningMove(Computer,_,_, Pos3, Val1, Val2, Val3, Pos3) :- var(Val3),
   nonvar(Val1), Val1 == Computer, nonvar(Val2), Val2 == Computer, !.

/* Computer is forced to move if Player has 2 in a row, 3rd square open */
forcedMove(_,_, Pos3, Val1, Val2, Val3, Pos3) :- var(Val3),
   nonvar(Val1), nonvar(Val2), Val1 == Val2, !.
forcedMove(_, Pos2,_, Val1, Val2, Val3, Pos2) :- var(Val2),
   nonvar(Val1), nonvar(Val3), Val1 == Val3, !.
forcedMove(Pos1,_,_, Val1, Val2, Val3, Pos1) :- var(Val1),
   nonvar(Val2), nonvar(Val3), Val2 == Val3, !.

/* This special move was added 11/13/93 to block a known way to beat
 * the program.  The suggested move is to Pos3.
 */
specialMove(Computer, Val1, Val2, Val3, ValA, ValB) :-
   var(Val2), var(Val3), nonvar(Val1), Val1 == Computer,
   nonvar(ValA), ValA \== Computer, nonvar(ValB), ValB \== Computer, !.
specialMove(Computer, Val1, Val2, Val3, ValA, ValB) :-
   var(Val1), var(Val3), nonvar(Val2), Val2 == Computer,
   nonvar(ValA), ValA \== Computer, nonvar(ValB), ValB \== Computer, !.

/* This special move was also added 11/13/93 to block a known way to
 * beat the program.  The suggested move is to Pos3.
 */
special2Move(Computer, Val1, Val2, Val3, ValA, ValB) :-
   var(Val2), var(Val3), nonvar(Val1), Val1 == Computer,
   nonvar(ValA), ValA \== Computer, var(ValB), !.
special2Move(Computer, Val1, Val2, Val3, ValA, ValB) :-
   var(Val1), var(Val3), nonvar(Val2), Val2 == Computer,
   var(ValA), nonvar(ValB), ValB \== Computer, !.
special2Move(Computer, Val1, Val2, Val3, ValA, ValB) :-
   var(Val2), var(Val3), nonvar(Val1), Val1 == Computer,
   var(ValA), nonvar(ValB), ValB \== Computer, !.
special2Move(Computer, Val1, Val2, Val3, ValA, ValB) :-
   var(Val1), var(Val3), nonvar(Val2), Val2 == Computer,
   nonvar(ValA), ValA \== Computer, var(ValB), !.
      
/*  A good move is for the computer to get 2 in a row, 3rd square open */
goodMove(Computer,_, Pos3, Val1, Val2, Val3, Pos3) :- var(Val2),
   var(Val3), nonvar(Val1), Val1 == Computer, !.
goodMove(Computer, Pos1,_, Val1, Val2, Val3, Pos1) :- var(Val1),
   var(Val3), nonvar(Val2), Val2 == Computer, !.
goodMove(Computer, Pos1, _, Val1, Val2, Val3, Pos1) :- var(Val1),
   var(Val2), nonvar(Val3), Val3 == Computer, !.

openCorner(Board, Location) :- corner(Location),
   argb(Location, Board, Val), var(Val).

corner(1).   corner(3).   corner(7).   corner(9).
   
continuePlay(Player,_,_, Board) :- won(Who, Board),   /* check for win */
   !, winnerOut(Who, Player).
continuePlay(Player, PlayerMoves, NumOpen, Board) :- NumOpen < 3,
   getMover(Player, PlayerMoves, Mover),
   openSquares(Board, Positions),
   willBeDraw(Mover, Positions, Board), !,
   write('The game is a draw.'), nl.
   /* If NumOpen is 1 or 2 we check for a draw */
continuePlay(Player, PlayerMoves, NumOpen, Board) :-
   play(Player, PlayerMoves, NumOpen, Board).
   /* Continue play if no win or draw has been found */
    
won(Who, Board) :- slice(Pos1, Pos2, Pos3), argb(Pos1, Board, Val1),
   nonvar(Val1), Who = Val1,    /* Who is potential winner */
   argb(Pos2, Board, Val2), nonvar(Val2), Val1 == Val2,
   argb(Pos3, Board, Val3), nonvar(Val3), Val1 == Val3.
      
winnerOut(Player, Player) :- !, write('You won.  Congratulations!'), nl.
winnerOut(_,_) :- write('I won!'), nl.
   
/* getMover gets letter (x or o) of player ready to move */
getMover(Player, yes, Player) :- !.
getMover(Player, no, Computer) :- opposite(Player, Computer).
   
openSquares(Board, Positions) :- 
   findall(Pos, openPos(Board, Pos), Positions).
   /* Positions becomes list of open positions in Board */
     
openPos(Board, Pos) :- location(Pos), argb(Pos, Board, Val), var(Val).

willBeDraw(Mover, Positions, Board) :- 
   not(couldBeWin(Mover, Positions, Board)).
   
couldBeWin(Mover, [Pos1, Pos2], Board) :-
   test2(Mover, Pos1, Pos2, Board), !.
   /* Test if the possible last 2 moves could lead to a win */
couldBeWin(Mover, [Pos1, Pos2], Board) :- !,   
   test2(Mover, Pos2, Pos1, Board).
couldBeWin(Mover, [Pos], Board) :- test1(Mover, Pos, Board).
   /* Test if last possible move gives a draw */
   
test2(Mover, Pos1, Pos2, Board) :- argb(Pos1, Board, Mover),
   opposite(Mover, Next), argb(Pos2, Board, Next), win(Board).
   /* Test if move to Pos1 and next move to Pos2 leads to winner */

test1(Mover, Pos, Board) :- argb(Pos, Board, Mover), win(Board).
   /* Test if move to Pos leads to winner */

win(Board) :- won(x, Board), !.
win(Board) :- won(o, Board).
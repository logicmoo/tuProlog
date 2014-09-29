/* Original file available @ http://cis.stvincent.edu/html/tutorials/prolog/tictac.pro
 * Modified to work under tuProlog.NET, using an external class written in one of the 
 * following languages: C#, VisualBasic.NET, F# or Java.
*/


	

% Predicati che controllano se uno dei due giocatori ha vinto
won(Player, Game) :- Game <- checkWin returns Winner, Winner == 1, Player = x.
won(Player, Game) :- Game <- checkWin returns Winner, Winner == 2, Player = o.

winnerOut(Player, Player) :- !, print('You won.  Congratulations!'), nl.
winnerOut(_,_) :- print('Computer won!'), nl.

% %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% %  
% Generazione delle mosse del computer %
% %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% %

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
   
% %%%%%%%%%%%%%% %
% Gestione input %
% %%%%%%%%%%%%%% %

/*
inputResponse(Response) :- read(Answer), okResponse(Answer, Response), !.
inputResponse(Response) :- print('Not a valid response -- type y or n: '), inputResponse(Response).

inputPlayer(Player) :- read(Player), x_or_o(Player).
inputPlayer(Player) :- print('Not a valid response -- type x or o: '), inputPlayer(Player).
 
inputMove(Board, Location) :- nl, print('Your move: '), read(Location),
	location(Location), 
	argb(Location, Board, Val), 
	var(Val). /* must be open 
	
inputMove(Board, Location) :- nl, print('Not a valid location.'), inputMove(Board, Location).
*/

% %%%%% %
% Fatti %
% %%%%% %

% fatti usati per il cambio del turno
negate(yes, no).   
negate(no, yes).

opposite(x, o).   
opposite(o, x).

% fatti usati per verificare l'input
location(1).   location(2).   location(3).   location(4).   location(5).
location(6).   location(7).   location(8).   location(9).   

x_or_o(x).   x_or_o(o).

okResponse(y, yes).   okResponse('Y', yes).   okResponse(yes, yes). 
okResponse('YES', yes).   okResponse('Yes', yes).
okResponse(n, no).   okResponse('N', no).   okResponse(no, no). 
okResponse('NO', no).   okResponse('No', no).

% fatti usati per la generazione delle mosse
slice(1,2,3).   slice(4,5,6).   slice(7,8,9).   slice(1,4,7).
slice(2,5,8).   slice(3,6,9).   slice(9,5,1).   slice(7,5,3).

diagonal(1,5,9).   diagonal(9,5,1).   
diagonal(3,5,7).   diagonal(7,5,3).
   
neighbors(1,2,4).   neighbors(3,2,6).
neighbors(7,4,8).   neighbors(9,6,8).

corner(1).   corner(3).   corner(7).   corner(9).

% I fatti argb(Pos, Board, Val) indicano se alla posizione Pos si trova il segnalino
% indicato da Val (x oppure o), è inoltre sempre vero se Pos non è occupata.
% mantenuti poichè usati per generare le mosse
argb(1, board(Val,_,_,_,_,_,_,_,_), Val).
argb(2, board(_,Val,_,_,_,_,_,_,_), Val).
argb(3, board(_,_,Val,_,_,_,_,_,_), Val).
argb(4, board(_,_,_,Val,_,_,_,_,_), Val).
argb(5, board(_,_,_,_,Val,_,_,_,_), Val).
argb(6, board(_,_,_,_,_,Val,_,_,_), Val).
argb(7, board(_,_,_,_,_,_,Val,_,_), Val).
argb(8, board(_,_,_,_,_,_,_,Val,_), Val).
argb(9, board(_,_,_,_,_,_,_,_,Val), Val).
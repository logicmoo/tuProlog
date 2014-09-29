fact(0,1):-!.
fact(X,R):-	Y is X - 1,
			fact(Y,S),
			R is X * S.
			

		
prova0(X):-
		write(X),nl.
prova1:-
		write('Mario').

loadConvetion(JavaConv):-
		load_convention('Conventions.dll','Conventions.JavaConvention',JavaConv).
creatClient(Client):-
		loadConvetion(JavaConv),
		new_object(JavaConv,'client',[],Client).
login(Id):-				
		creatClient(Client),
		Client <- login returns Id.
selectMode(Id,Mode,Ex):-		
		creatClient(Client),
		Client <- selectMode(Id,Mode) returns Ex.
play(Id,Place,Board):-						
		creatClient(Client),
		Client <- play(Id,Place) returns Board.
logout(Id,Ex):-		
		creatClient(Client),
		Client <- logout(Id) returns Ex.
getBoard(Id,Board):-		
		creatClient(Client),
		Client <- getBoard(Id) returns Board.		
getRemaning(BoardStr,Remaning):-
		loadConvetion(JavaConv),
		new_object(JavaConv,'board',[BoardStr],Board),
		Board <- getRemaning returns Remaning.		
checkWin(BoardStr,Winner):-
		loadConvetion(JavaConv),
		new_object(JavaConv,'board',[BoardStr],Board),
		Board <- checkWin returns Winner.
printBoard(BoardStr):-
		loadConvetion(JavaConv),
		new_object(JavaConv,'board',[BoardStr],Board),
		Board <- printBoard.
		
		
		

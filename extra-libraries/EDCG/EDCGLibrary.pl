:- op(1200, xfx, '-->'). 
:- op(200, xfx, '\'). 
:- op(200, xfx, ';').
:- op(200, fx, '*'). %Zero or more productions
:- op(200, fx, '+'). %One or more productions
:- op(200, fx, '?'). %Zero or one production
:- op(200, fx, '^'). %Exactly N productions (for parsing only)
:- op(200, fx, '#'). %Exactly N productions (for AST gen)

dcg_parse(*(A,_,[]),LO \ LO).
dcg_parse(*(A,X,[X|L]), LI \ LO) :- !,(dcg_parse(A, LI \ L1), LI\=L1,
					dcg_parse(*(A,X,L),L1 \ LO)).
dcg_parse(*(A), LI \ LO) :- ((dcg_parse(A,LI \ L1), LI\=L1,
				dcg_parse(*(A), L1 \ LO));LI=LO). 
dcg_parse(+(A,X,[X|L]), LI \ LO) :- dcg_parse(A, LI \ L1),
					dcg_parse(*(A,X,L),L1 \ LO). 
dcg_parse(+(A), LI \ LO) :- (dcg_parse(A,LI \ L1), LI\=L1,
				dcg_parse(*(A),L1 \ LO)).
dcg_parse(?(A,_,E2,E2), LO \ LO).
dcg_parse(?(A,E1,_,E1), LI \ LO) :- dcg_parse(A, LI \ LO).
dcg_parse(?(A), LI \ LO) :- dcg_parse(A, LI \ LO);LI=LO.				
dcg_parse((A;B), Tokens) :- dcg_parse(A, Tokens);dcg_parse(B, Tokens).
dcg_parse(#(A,N,X,L), LI \ LO) :- dcg_power(#(A,N,0,X,L),LI \ LO).
dcg_power(#(A,N,N,_,[]), LO \ LO).
dcg_power(#(A,N,M,X,[X|L]), LI \ LO) :- M1 is M+1, !, dcg_parse(A, LI \ L1),
					dcg_power(#(A,N,M1,X,L),L1 \ LO). 
dcg_parse(^(A,N), LI \ LO) :- dcg_power(^(A,N,0),LI \ LO).
dcg_power(^(A,N,N),LO \ LO).
dcg_power(^(A,N,M), LI \ LO) :- M1 is M+1, !,dcg_parse(A, LI \ L1),
					dcg_power(^(A,N,M1),L1 \ LO). 
dcg_nonterminal(X) :- list(X), !, fail. 
dcg_nonterminal(_). 
dcg_terminals(Xs) :- list(Xs). 
phrase(Category, String, Left) :- dcg_parse(Category, String \ Left), !. 
phrase(Category, [H | T]) :- dcg_parse(Category, [H | T] \ []), !. 
phrase(Category,[]) :- dcg_parse(Category, [] \ []), !. 
dcg_parse(A, Tokens) :- dcg_nonterminal(A), (A --> B), dcg_parse(B, Tokens). 
dcg_parse((A, B), Tokens \ Xs) :- dcg_parse(A, Tokens \ Tokens1), 
					dcg_parse(B, Tokens1 \ Xs). 
dcg_parse(A, Tokens) :- dcg_terminals(A), dcg_connect(A, Tokens). 
dcg_parse({A}, Xs \ Xs) :- call(A). 
dcg_connect([], Xs \ Xs). 
dcg_connect([W | Ws], [W | Xs] \ Ys) :- dcg_connect(Ws, Xs \ Ys). 

any([X|Xs],X,Xs).
any([X|Xs],E,[X|Ys]) :- any(Xs,E,Ys).
perm([],[]).
perm(Xs,[X|Ys]) :- any(Xs,X,Zs), perm(Zs,Ys).

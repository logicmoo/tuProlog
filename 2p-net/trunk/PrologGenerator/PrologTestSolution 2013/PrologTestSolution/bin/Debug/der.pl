dExpr(T,DT) :- dTerm(T,DT).
dExpr(E+T,[DE+DT]) :- dExpr(E,DE), dTerm(T,DT).
dExpr(E-T,[DE-DT]) :- dExpr(E,DE), dTerm(T,DT).
dTerm(F,DF) :- dFactor(F,DF).
dTerm(T*F,[[DT*F]+[T*DF]]) :- dTerm(T,DT), dFactor(F,DF).
dTerm(T/F,[[F*DT]-[T*DF]]/[F*F]) :- dTerm(T,DT), dFactor(F,DF).
dFactor(x,1).
dFactor(N,0) :- number(N).
dFactor([E],DE) :- dExpr(E,DE).
dFactor(-E,-DE) :- dExpr(E,DE).
dFactor(sin(E), [cos(E)*DE] ) :- dExpr(E,DE).
dFactor(cos(E), [-sin(E)*DE] ):- dExpr(E,DE).
dFactor(E^N, [N*E^(Nm1)*(DE)] ) :- dExpr(E,DE), Nm1 is N-1.
dExpr(T,DT) :- dTerm(T,DT).
dExpr(E+T,[DE+DT]) :- dExpr(E,DE), dTerm(T,DT).
dExpr(E-T,[DE-DT]) :- dExpr(E,DE), dTerm(T,DT).
dTerm(F,DF) :- dFactor(F,DF).
dTerm(T*F,[[DT*F]+[T*DF]]) :- dTerm(T,DT), dFactor(F,DF).
dTerm(T/F,[[F*DT]-[T*DF]]/[F*F]) :- dTerm(T,DT), dFactor(F,DF).
dFactor(x,1).
dFactor(N,0) :- number(N).
dFactor([E],DE) :- dExpr(E,DE).
dFactor(-E,-DE) :- dExpr(E,DE).
dFactor(sin(x), cos(x)) :- !.
dFactor(cos(x), -sin(x)) :- !.
dFactor(sin(E), [cos(E)*DE] ) :- dExpr(E,DE).
dFactor(cos(E), [-sin(E)*DE] ):- dExpr(E,DE).
dFactor(x^N, [N*x^(N1)] ) :- N1 is N-1, !.
dFactor(E^N, [N*E^(Nm1)*(DE)] ) :- dExpr(E,DE), Nm1 is N-1.

pretty_print(X+Y):- pretty_print(X), write('+'), pretty_print(Y).
pretty_print(X-Y):- pretty_print(X), write('-'), pretty_print(Y).
pretty_print(X*Y):- pretty_print(X), write('*'), pretty_print(Y).
pretty_print(X/Y):- pretty_print(X), write('/'), pretty_print(Y).
pretty_print(X^Y):- pretty_print(X), write('^'), pretty_print(Y).
pretty_print([X]):- write('('), pretty_print(X), write(')').
pretty_print(-X):- write('-'), pretty_print(X).
pretty_print(sin(X)):- write('sin('), pretty_print(X), write(')').
pretty_print(cos(X)):- write('cos('), pretty_print(X), write(')').
pretty_print(T):- write(T). % default
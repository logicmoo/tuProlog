% A solution to the Map Coloring problem.
% From "The Art of Prolog", by L. Sterling and E. Shapiro, page 212.

color_map([Region | Regions], Colors) :-
    color_region(Region, Colors),
    color_map(Regions, Colors).
color_map([], _).

color_region(region(Name, Color, Neighbors), Colors) :-
    select(Color, Colors, OtherColors),
    members(Neighbors, OtherColors).
    
select(X, [X | Xs], Xs).
select(X, [Y | Ys], [Y | Zs]) :- select(X, Ys, Zs).

members([X | Xs], Ys) :- member(X, Ys), members(Xs, Ys).
members([], _).

test_color(Name, Map) :-
    map(Name, Map),
    colors(Name, Colors),
    color_map(Map, Colors).
    
colors(X, [red, yellow, blue, green]).

map(test, [
                region(a, A, [B, C, D]),
                region(b, B, [A, C, E]),
                region(c, C, [A, B, D, E, F]),
                region(d, D, [A, C, F]),
                region(e, E, [B, C, F]),
                region(f, F, [C, D, E])
               ]).
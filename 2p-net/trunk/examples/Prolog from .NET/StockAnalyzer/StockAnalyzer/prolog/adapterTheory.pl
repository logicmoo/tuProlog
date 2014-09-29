addStockAnalysis(SA,RS):-
		set_classpath(['.']),															% Imposto il classpath
		load_convention('Conventions.dll','Conventions.JavaConvention',JavaConv),		% Carico la convention
		new_object(JavaConv,'com.jDBCadapter',[],Jadapter), 							% Manca la gestione dell'eccezione
		Jadapter <- stockAnalysisAdderStr(SA) returns RS.								% Richiamo il metodo stockAnalysisAdderStr e ottengo il risultato dell'inserimento
	
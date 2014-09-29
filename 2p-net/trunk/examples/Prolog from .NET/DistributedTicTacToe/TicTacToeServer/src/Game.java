import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Theory;
import alice.tuprolog.UnknownVarException;

public class Game{


	private Theory theory;
	private Prolog engine;
	private Board board;
	//Costrutture che accetta come parametro la teoria che verrà impostata nel motore
	public Game(String theory) {
		try {			
			this.theory=new Theory(theory);		
		} catch (InvalidTheoryException e) {
			e.printStackTrace();
		}
		engine =new Prolog();
		engine.addOutputListener(new WriterListener());
		engine.addExceptionListener(new WriterListener());
		try {
			engine.setTheory(this.theory);
		} catch (InvalidTheoryException e) {			
			e.printStackTrace();
		}
		board=new Board();	
	}
	//Accetta come parametro la casella scelta dal giocatore e restitusce la scelta del computer
	public int play(int placeSelected)
	{
		try {
			Board b=board;
			if(b.setCase('x', placeSelected)==-1)
			{
				return -1;
			}			
			if(b.getRemaning()==0)
				return b.checkWin()+1000;
			SolveInfo si=engine.solve("generateMove('o',"+b.getBoard()+",Loc).");			
			int place=0;
			if(si.isSuccess())
			{
				place=Integer.parseInt(""+si.getTerm("Loc"));
			}
			b.setCase('o', place);									
			return place;
		} catch (MalformedGoalException e) {		
			e.printStackTrace();
		} catch (NoSolutionException e) {		
			e.printStackTrace();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	//Fa eseguire la prima mossa al computer e la restituisce   
	public int play()
	{

		try {
			SolveInfo si=engine.solve("generateMove('o',"+board.getBoard()+",Loc).");
			int place=0;
			if(si.isSuccess())
			{
				place=Integer.parseInt(""+si.getTerm("Loc"));
			}
			board.setCase('o', place);
			return place;
		} catch (MalformedGoalException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {			
			e.printStackTrace();
		} catch (NoSolutionException e) {			
			e.printStackTrace();
		} catch (UnknownVarException e) {			
			e.printStackTrace();
		}
		return 0;
	}
	public String getBoard()
	{
		return board.getBoard();
	}
}

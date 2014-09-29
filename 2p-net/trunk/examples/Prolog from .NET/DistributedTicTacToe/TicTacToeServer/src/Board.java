
import java.util.Scanner;

public class Board {
	
	private char caselle[];
	private int remaning;

	private static final int[] PowArray = {1, 2, 4, 8, 16, 32, 64, 128, 256};
	private static final int[] WinArray = {7, 56, 73, 84, 146, 273, 292, 448};

	private int Xstatus, Ostatus;

	public Board() {
		remaning=9;
		Xstatus=0;
		Ostatus=0;
		caselle=new char[9];
		for(int i=0;i<caselle.length;i++)
		{
			caselle[i]='_';
		}
	}	
	// Costruisce un oggetto board a partire da una stinga formattata in questo modo 
	// board(_,_,_,_,_,_,_,_,_)
	// dove _ può essere sia o che x 
	public Board(String board)
	{
		this();
		Scanner sc=new Scanner(board);
		sc.useDelimiter("[\\(,\\)]");
		sc.next();
		int i=1;
		while(sc.hasNext())
		{
			String line=sc.next();
			if(line.charAt(0)!='_')
				this.setCase(line.charAt(0), i);
			i++;
		}
		sc.close();
	}
	// Stampa la griglia in questo formato
	/*
	 * _ | _ | _
	 * _ | _ | _
	 * _ | _ | _ 
	 * 
	 */
	public void printBoard()
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < caselle.length; i += 3)
		{
			sb.append("\n ");
			sb.append(caselle[i]); sb.append(" | ");
			sb.append(caselle[i + 1]); sb.append(" | ");
			sb.append(caselle[i + 2]);
			sb.append(" \n");
		}
		System.out.println(sb);//TODO
	}
	// Controlla se e chi ha vinto
	public int checkWin() {
		for (int i = 0; i < 8; i++)
			if((Xstatus & WinArray[i]) == WinArray[i])
				return 1;
		for (int i = 0; i < 8; i++)
			if((Ostatus & WinArray[i]) == WinArray[i])
				return 2;
		return 0;
	}
	// Ritorna una stringa nel formato  board(_,_,_,_,_,_,_,_,_)
	public String getBoard()
	{		
		StringBuilder sb=new StringBuilder();
		sb.append("board(");
		for(int i=0;i<caselle.length-1;i++)
		{
			sb.append(caselle[i]+",");
		}
		sb.append(caselle[8]+")");
		return sb.toString();		
	}
	//Assegna la casella in posizione place al giocatore player
	public int setCase(char player,int place)
	{
		place--;
		if(caselle[place]=='_')
		{
			caselle[place]=player;
			remaning--;
			switch(player){
			case 'x':Xstatus+=PowArray[place];break;
			case 'o':Ostatus+=PowArray[place];break;
			default: return -1;
			}
			return 0;
		}
		return -1;		
	}
	public int setCaseStr(String player,int place)
	{
		return setCase(player.charAt(0),place);
	}
	public char getCase(int place)
	{
		place--;		
		return caselle[place];		
	}
	// Restituisce il numero di caselle libere
	public int getRemaning() {
		return remaning;
	}

}

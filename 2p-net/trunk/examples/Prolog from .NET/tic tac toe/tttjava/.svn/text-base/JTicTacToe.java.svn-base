package tttjava;

import java.util.Scanner;

public class JTicTacToe {
	//giocatore 1 usa TRUE, giocatore 2 usa FALSE
	private char caselle[];
	//giocatore 1 usa X, giocatore 2 usa O
	private int Xstatus, Ostatus;
	//caselle vuote rimanenti
	private int remaining;
	
	private static final int[] PowArray = {1, 2, 4, 8, 16, 32, 64, 128, 256};
	private static final int[] WinArray = {7, 56, 73, 84, 146, 273, 292, 448};
	
	public JTicTacToe() {
		caselle = new char[9];
		for(int i = 0; i < 9; i++)
			caselle[i] = (""+(i+1)).charAt(0);
		Xstatus = 0;
		Ostatus = 0;
		remaining = 9;
	}
	
	public int getRemaining() { return remaining; }
	
	public String getBoard()
	{
		StringBuilder sb = new StringBuilder();
        sb.append("board(");
        for (int i = 0; i < caselle.length; i++)
        {
            if (caselle[i] == 'x' || caselle[i] == 'o')
                sb.append(caselle[i]);
            else
                sb.append('_');

            if (i != (caselle.length - 1))
                sb.append(',');
        }
        sb.append(')');
        return sb.toString();
	}
	
	public void gioca(int casella, String player) {
		casella--;
		if(caselle[casella] != 'x' || caselle[casella] != 'o')
		{
			if(player.equals("x"))
			{
				caselle[casella] ='x';
				Xstatus += PowArray[casella];
			}
			else if (player.equals("o"))
			{
				caselle[casella] = 'o';
				Ostatus += PowArray[casella];
			}
		}	
		remaining--;		
	}

	public int checkWin() {
		for (int i = 0; i < 8; i++)
		    if((Xstatus & WinArray[i]) == WinArray[i])
		        return 1;
		for (int i = 0; i < 8; i++)
		    if((Ostatus & WinArray[i]) == WinArray[i])
		        return 2;
		return 0;
	}

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
         System.out.println(sb);
	}
	
	public int inputMove()
	{
		int casella = 0;
		String read = null;
		Scanner in = new Scanner(System.in);
		do
		{
			try
			{
				System.out.print("Your move: ");
				read = in.nextLine();
				casella = Integer.parseInt(read);
			}
			catch(Exception e){	}
		}while( casella < 0 || casella > 10 
				|| caselle[(casella-1)] != read.charAt(0) );
		
		return casella;
	}
		
	
	public static String inputResponse() {
		String response = null;
		Scanner in = new Scanner(System.in);
		do
		{
			response = in.nextLine();
			if(response.startsWith("y"))
				response = "yes";
			else if (response.startsWith("n"))
				response = "no";
			else 
			{
				System.out.print("Not a valid response! (y/n): ");
				response = null;
			}
		}while( response == null );
		
		return response;				
	}
	
	public static String inputPlayer() {
		String player = null;
		Scanner in = new Scanner(System.in);
		do
		{
			player = in.nextLine();
			if(player.startsWith("x"))
				player = "x";
			else if (player.startsWith("o"))
				player = "o";
			else 
			{
				System.out.print("Not a valid response! (o/x): ");
				player = null;
			}
		}while( player == null );
		
		return player;				
	}
	
	
}

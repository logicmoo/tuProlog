
import java.util.Scanner;

public class Main {


	//Main di prova
	public static void main(String[]  Args){	
		Client cl=new Client();
		int id=cl.login();
		int place=0;
		Board b;
		Scanner sc=new Scanner(System.in);;
		System.out.println("Vuoi partire per primo?");
		String risp=sc.next();
		if(risp.trim().toLowerCase().equals(("si")))
				cl.selectMode(id, 0);
		else
		{
			cl.selectMode(id, 1);
			b=new Board(cl.getBoard(id));
			b.printBoard();
		}		
		do{
			System.out.println("Inserisci casella");			
			place=Integer.parseInt(sc.next());			
			cl.play(id, place);
			b=new Board(cl.getBoard(id));
			b.printBoard();
		}while(b.getRemaning()>0);
		cl.logout(id);
		sc.close();
		System.out.println(b.checkWin());    
	}
}


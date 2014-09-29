

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerTicTacToe extends Remote {	
	int login() throws RemoteException;							//Esegue il login
	String selectMode(int id,int mode) throws RemoteException;	//Seleziona chi eseguirà la prima mossa
	String getBoard(int id) throws RemoteException;				//Restituisce lo stato della tavola di gioco
	String play(int id,int place) throws RemoteException;		//Accetta come parametro la posizione in cui il giocatore vuole posizionare il suo simbolo e restituisce lo stato della griglia nel formato board(_,_,_,_,_,_,_,_,_) 
	void logout(int id) throws RemoteException;					//esegue il logout
}
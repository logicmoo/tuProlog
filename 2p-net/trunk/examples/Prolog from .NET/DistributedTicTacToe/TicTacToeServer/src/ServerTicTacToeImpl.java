

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class ServerTicTacToeImpl extends UnicastRemoteObject implements ServerTicTacToe 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int maxGames=100;
	private Game[] games;
	private int[] avaiables;	
	private String theory;

	protected ServerTicTacToeImpl() throws RemoteException {
		super();		
		BufferedReader bf=null;
		try {
			bf = new BufferedReader(new FileReader(new File("prolog/gameTheory.pl")));
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
		String str=new String();
		String line;
		try {
			while((line=bf.readLine())!=null)
			{
				str+=line+'\n';
			}
		} catch (IOException e2) {		
			e2.printStackTrace();
		}
		theory=str;		
		games=new Game[maxGames];	//Inizializza l'array
		avaiables=new int [maxGames];
		for(int i=0;i<maxGames;i++)
		{
			avaiables[i]=0;
		}		
	}	
	public static void main(String[] args) {
		final int REGISTRYPORT = 1099;
		String registryHost = "127.0.0.1";
		String serviceName = "ServerTicTacToe";
		try {
			LocateRegistry.createRegistry(REGISTRYPORT);			//Crea un registry RMI
		} catch (RemoteException e1) {		
			e1.printStackTrace();
		}
		String completeName = "//" + registryHost + ":" + REGISTRYPORT + "/"
				+ serviceName;
		try{
			ServerTicTacToeImpl  serverRMI = new ServerTicTacToeImpl();
			Naming.rebind(completeName, serverRMI);						//Registra il servizio al registry
			System.out.println("Server RMI: Servizio \"" + serviceName
					+ "\" registrato");
		}
		catch(Exception e){
			System.err.println("Server RMI \"" + serviceName + "\": "
					+ e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}
	//Cerca il primo id disponibile e lo restituisce 
	@Override
	public int login() throws RemoteException {
		int id=-1;
		for(int i=0;i<maxGames&&id==-1;i++)
		{
			if(avaiables[i]==0)
				id=i;
		}
		avaiables[id]=1;
		return id;
	}
	//Seleziona la modalità di gioco
	@Override
	public String selectMode(int id, int mode) throws RemoteException {
		switch (mode) {
		case 0://Viene creato un nuovo gioco e ritorna lo stato della griglia
			games[id]=new Game(theory);
			return games[id].getBoard();			
		case 1:
			games[id]=new Game(theory);//Viene creato un nuovo gioco, il computer esegue la prima mossa e ritorna lo stato della griglia
			games[id].play();
			return games[id].getBoard();			
		default:
			break;
		}
		return "";
	}
	//Esegue il metodo play sull'oggetto Game relativo
	@Override
	public String play(int id, int place) throws RemoteException {
		Game g=games[id];
		g.play(place);
		System.out.println(id+":"+g.getBoard());
		return new String(g.getBoard());
	}
	//Esegue il metodo getBoard sull'oggetto Game relativo
	@Override
	public String getBoard(int id) throws RemoteException {		
		return games[id].getBoard();
	}
	// "Libera" le risorse
	@Override
	public void logout(int id) throws RemoteException {
		avaiables[id]=0;
		games[id]=null;		
	}
}

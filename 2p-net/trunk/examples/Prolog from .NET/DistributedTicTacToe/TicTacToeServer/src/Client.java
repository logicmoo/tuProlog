import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public class Client {
	private ServerTicTacToe serverRMI;
	//Esegue la connessione al server RMI
	public Client() {
		final int REGISTRYPORT = 1099;
		String registryHost = "127.0.0.1";
	    String serviceName = "ServerTicTacToe";
	    String completeName = "//" + registryHost + ":" + REGISTRYPORT + "/"
	            + serviceName;
	        try {	        	
				serverRMI = (ServerTicTacToe) Naming.lookup(completeName);				
			} catch (MalformedURLException | RemoteException| NotBoundException e) {				
				e.printStackTrace();
			}
	}
	//Viene creato un metodo locale per ogni metodo remoto esposto dall'interfaccia ServerTicTacToe 
	public int login() 
	{
		System.out.println("Login in corso");
		try {
			return serverRMI.login();
		} catch (RemoteException e) {			
			e.printStackTrace();
		}
		return -1;
	}
	public int logout(int id)
	{
		try {
			serverRMI.logout(id);
		} catch (RemoteException e) {			
			e.printStackTrace();
		}
		return -1;
	}
	public String play (int id,int place)
	{
		try {
			return serverRMI.play(id, place);
		} catch (RemoteException e) {			
			e.printStackTrace();
		}
		return null;		
	}
	public String getBoard(int id)
	{
		try{
			return serverRMI.getBoard(id);
		}catch (RemoteException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	public String selectMode(int id,int mode){
		try{
			return serverRMI.selectMode(id, mode);
		}catch (RemoteException e)
		{
			e.printStackTrace();
		}
		return "";
	}

}

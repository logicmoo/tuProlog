package alice.tuprolog;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.List;


public class Server_Socket extends AbstractSocket{
	private ServerSocket socket;
	private static final long serialVersionUID = 1L;
	public Server_Socket(ServerSocket s){
		socket=s;
	}
	@Override
	public ServerSocket getSocket(){
		return socket;
	}
	@Override
	public boolean isClientSocket() {
		return false;
	}

	@Override
	public boolean isServerSocket() {
		return true;
	}
	
	@Override
	public boolean unify(List<TuVar> varsUnifiedArg1, List<TuVar> varsUnifiedArg2, Term t, boolean isOccursCheckEnabled) {
		t = t.getTerm();
        if (t instanceof TuVar) {
            return t.unify(varsUnifiedArg1, varsUnifiedArg2, this,  isOccursCheckEnabled);
        } else if (t instanceof AbstractSocket && ((AbstractSocket) t).isServerSocket()) {
        	InetAddress addr= ((AbstractSocket) t).getAddress();
            return socket.getInetAddress().toString().equals(addr.toString());
        } else {
            return false;
        }
	}
	
	@Override
	public InetAddress getAddress() {
		if(socket.isBound())return socket.getInetAddress();
		else return null;
	}
	@Override
	public boolean isDatagramSocket() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String toString(){
		return socket.toString();
	}
	@Override
	public  boolean unify(List<TuVar> varsUnifiedArg1, List<TuVar> varsUnifiedArg2, Term t) {
		return unify(varsUnifiedArg1, varsUnifiedArg2, t, true);
	}


}

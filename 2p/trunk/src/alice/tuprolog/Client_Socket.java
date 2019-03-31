package alice.tuprolog;

import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

public class Client_Socket extends TuAbstractSocket {
    private static final long serialVersionUID = 1L;
    private Socket socket;

    public Client_Socket(Socket s) {
        socket = s;
    }

    @Override
    public boolean isClientSocket() {
        return true;
    }

    @Override
    public boolean isServerSocket() {
        return false;
    }

    @Override
    public Socket getSocket() {
        return socket;
    }

    @Override
    public boolean unify(List<TuVar> varsUnifiedArg1, List<TuVar> varsUnifiedArg2, Term t,
            boolean isOccursCheckEnabled) {
        t = t.dref();
        if (t.isVar()) {
            return t.unify(varsUnifiedArg1, varsUnifiedArg2, this, isOccursCheckEnabled);
        } else if (t instanceof TuAbstractSocket && ((TuAbstractSocket) t).isServerSocket()) {
            InetAddress addr = ((TuAbstractSocket) t).getAddress();
            return socket.getInetAddress().toString().equals(addr.toString());
        } else {
            return false;
        }
    }

    @Override
    public InetAddress getAddress() {
        if (socket.isBound())
            return socket.getInetAddress();
        else
            return null;
    }

    @Override
    public boolean isDatagramSocket() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String toString() {
        return socket.toString();
    }

    @Override
    public boolean unify(List<TuVar> varsUnifiedArg1, List<TuVar> varsUnifiedArg2, Term t) {
        return unify(varsUnifiedArg1, varsUnifiedArg2, t, true);
    }

}

package alice.tuprolog;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

public class Datagram_Socket extends TuAbstractSocket {
    private static final long serialVersionUID = 1L;

    private DatagramSocket socket;

    public Datagram_Socket(DatagramSocket socket) {
        super();
        this.socket = socket;
    }

    @Override
    public boolean isClientSocket() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isServerSocket() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isDatagramSocket() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public DatagramSocket getSocket() {
        // TODO Auto-generated method stub
        return socket;
    }

    @Override
    public InetAddress getAddress() {
        if (socket.isBound())
            return socket.getInetAddress();
        else
            return null;
    }

    @Override
    public boolean unify(List<TuVar> varsUnifiedArg1, List<TuVar> varsUnifiedArg2, Term t, boolean isOccursCheckEnabled) {
        t = t.getTerm();
        if (t instanceof TuVar) {
            return t.unify(varsUnifiedArg1, varsUnifiedArg2, this, isOccursCheckEnabled);
        } else if (t instanceof TuAbstractSocket && ((TuAbstractSocket) t).isDatagramSocket()) {
            InetAddress addr = ((TuAbstractSocket) t).getAddress();
            return socket.getInetAddress().toString().equals(addr.toString());
        } else {
            return false;
        }
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

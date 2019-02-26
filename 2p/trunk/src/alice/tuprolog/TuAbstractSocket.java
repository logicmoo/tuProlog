package alice.tuprolog;

import java.net.InetAddress;
import java.util.AbstractMap;
import java.util.ArrayList;

import nu.xom.xslt.XSLException;

public abstract class TuAbstractSocket extends TuTerm {
    private static final long serialVersionUID = 1L;

    public abstract boolean isClientSocket();

    public abstract boolean isServerSocket();

    public abstract boolean isDatagramSocket();

    public abstract Object getSocket();

    public abstract InetAddress getAddress();

    @Override
    public boolean isNumber() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isStruct() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isVar() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isEmptyList() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isAtomic() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isCompound() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isAtom() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isList() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isGround() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isGreater(Term t) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isGreaterRelink(Term t, ArrayList<String> vorder) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isEqual(Term t) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Term getTerm() {
        return this;
    }

    @Override
    public void free() {
        // TODO Auto-generated method stub

    }

    @Override
    public long resolveTerm(long count) {
        return count;
    }

    @Override
    public Term copy(int idExecCtx, AbstractMap<TuVar, TuVar> vMap)  {
        return this;
    }

    @Override
    public Term copy(AbstractMap<TuVar, TuVar> vMap, AbstractMap<Term, TuVar> substMap) {
        return this;
    }

    @Override
    public Term copyAndRetainFreeVar(AbstractMap<TuVar, TuVar> vMap, int idExecCtx) {
        // TODO Auto-generated method stub
        return this;
    }

    @Override
    public void accept(TuTermVisitor tv) {
        // TODO Auto-generated method stub

    }

}

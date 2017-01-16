package alice.tuprolog;

import java.util.ArrayList;
import java.util.List;


public class ChoicePointStore {
    
    
    private ChoicePointContext pointer;
    
    public ChoicePointStore() {
        pointer = null;
    }
    
    public void add(ChoicePointContext cpc) {
        if (pointer == null) {
            pointer = cpc;
            return;
        }
        ChoicePointContext oldCtx = pointer;
        cpc.prevChoicePointContext = oldCtx;
        pointer = cpc;
    }
    
    public void cut(ChoicePointContext pointerAfterCut) {
        pointer = pointerAfterCut;
    }
    
    
    public ChoicePointContext fetch() {
        return (existChoicePoint()) ? pointer : null;
    }
    
    
    public ChoicePointContext getPointer() {
        return pointer;
    }
    
    
    protected boolean existChoicePoint() {
        if (pointer == null) return false;
        ClauseStore clauses;
        do {
            clauses = pointer.compatibleGoals;
            if (clauses.existCompatibleClause()) return true;
            pointer = pointer.prevChoicePointContext;
        } while (pointer != null);            
        return false;
    }
    
    
    protected void removeUnusedChoicePoints() {
        // Note: it uses the side effect of this.existChoicePoint()!
        existChoicePoint();
    }
    
    public String toString(){
        return pointer + "\n";
    }
    
    public List<ChoicePointContext> getChoicePoints() {
        ArrayList<ChoicePointContext> l = new ArrayList<ChoicePointContext>();
        ChoicePointContext t = pointer;
        while (t != null) {
            l.add(t);
            t = t.prevChoicePointContext;
        }
        return l;
    }
    
}
package alice.tuprolog;

import java.util.*;

import alice.util.OneWayList;


public class ClauseStore {
    
    
    private OneWayList<ClauseInfo> clauses;
    private Term goal;
    private List<Var> vars;
    private boolean haveAlternatives;
    
    private ClauseStore(Term goal, List<Var> vars) {
        this.goal = goal;
        this.vars = vars;
        clauses = null;
    }
    
    
    
    public static ClauseStore build(Term goal, List<Var> vars, List<ClauseInfo> familyClauses) {
        ClauseStore clauseStore = new ClauseStore(goal, vars);
                clauseStore.clauses = OneWayList.transform2(familyClauses);
                if (clauseStore.clauses == null || !clauseStore.existCompatibleClause())
            return null;
        return clauseStore;
    }
    
    
   
    public ClauseInfo fetch() {
        if (clauses == null) return null;
        deunify(vars);
        if (!checkCompatibility(goal))
            return null;
        ClauseInfo clause = (ClauseInfo) clauses.getHead();
        clauses = clauses.getTail();
        haveAlternatives = checkCompatibility(goal);
        return clause;
    }
    
    
    public boolean haveAlternatives() {
        return haveAlternatives;
    }
    
    
   
    protected boolean existCompatibleClause() {
        List<Term> saveUnifications = deunify(vars);
        boolean found = checkCompatibility(goal);
        reunify(vars, saveUnifications);
        return found;
    }
    
    
    
    private List<Term> deunify(List<Var> varsToDeunify) {
        List<Term> saveUnifications = new ArrayList<Term>();
        //List saveUnifications = new LinkedList();
        //deunifico le variabili termporaneamente
        Iterator<Var> it = varsToDeunify.iterator();
        while (it.hasNext()) {
            Var v = ((Var) it.next());
            saveUnifications.add(v.getLink());
            v.free();
        }
        return saveUnifications;
    }
    
   
    private void reunify(List<Var> varsToReunify, List<Term> saveUnifications) {
        int size = varsToReunify.size();
        ListIterator<Var> it1 = varsToReunify.listIterator(size);
        ListIterator<Term> it2 = saveUnifications.listIterator(size);
        // Only the first occurrence of a variable gets its binding saved;
        // following occurrences get a null instead. So, to avoid clashes
        // between those values, and avoid random variable deunification,
        // the reunification is made starting from the end of the list.
        while (it1.hasPrevious()) {
            it1.previous().setLink(it2.previous());
        }
    }
    
   
    private boolean checkCompatibility(Term goal) {
        if (clauses == null) return false;
        ClauseInfo clause = null;
        do {
            clause = (ClauseInfo) clauses.getHead();
            if (goal.match(clause.getHead())) return true;
            clauses = clauses.getTail();
        } while (clauses != null);
        return false;
    }
    
    
    public String toString() {
        return "clauses: "+clauses+"\n"+
        "goal: "+goal+"\n"+
        "vars: "+vars+"\n";
    }
    
    public List<ClauseInfo> getClauses() {
        ArrayList<ClauseInfo> l = new ArrayList<ClauseInfo>();
        OneWayList<ClauseInfo> t = clauses;
        while (t != null) {
            l.add(t.getHead());
            t = t.getTail();
        }
        return l;
    }
    
    public Term getMatchGoal() {
        return goal;
    }
    
    public List<Var> getVarsForMatch() {
        return vars;
    }
    
    
}
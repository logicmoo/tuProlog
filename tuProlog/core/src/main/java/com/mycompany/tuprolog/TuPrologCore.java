package com.mycompany.tuprolog;
import alice.tuprolog.*;

/**
 * Created by Vito Colletta on 02/12/15.
 * Copyright (c) 2015 Vito Colletta. All rights reserved.
 */

public class TuPrologCore {
    private static TuPrologCore instance=null;
    private Prolog engine = null;
    private SolveInfo info = null;

    private boolean nextSolutionEnable=false;
    private String result="";

    private TuPrologCore(){
        if(engine==null) {
            engine = new Prolog();
        }
    }


    public static TuPrologCore getInstance(){
        if(instance==null){
            instance=new TuPrologCore();
        }
        return instance;
    }

    public Prolog getEngine()
    {
       return engine;
    }

    public void setTheory(String theory)
    {
        try {
            engine.setTheory(new Theory(theory));
        }catch(InvalidTheoryException ex){
            throw new IllegalArgumentException(""+ex.line);
        }

    }

    private String solveInfoToString(SolveInfo result) {
        String s = "";
        try {
            for (Var v: result.getBindingVars()) {
                if ( !v.isAnonymous() && v.isBound() && (!(v.getTerm() instanceof Var) || (!((Var) (v.getTerm())).getName().startsWith("_")))) {
                    s += v.getName() + " / " + v.getTerm() + "\n";
                }
            }
            if(s.length()>0)
                s.substring(0,s.length()-1);
        } catch (NoSolutionException e) {}
        return s;
    }

    public boolean getNextSolutionEnable()
    {
        return nextSolutionEnable;
    }

    public String solveGoal(String goal) {
        result="";
        try {
                info = engine.solve(goal);
                if (engine.isHalted())
                    System.exit(0);
                if (!info.isSuccess()) {
                    if (info.isHalted())
                        result += "halt.";
                    else
                        result += "no.";
                } else if (!engine.hasOpenAlternatives()) {
                    String binds = info.toString();
                    if (binds.equals("")) {
                        result += "yes.";
                    } else
                        result += solveInfoToString(info) + "\nyes.";
                } else {
                    result += solveInfoToString(info) + " ? ";
                    nextSolutionEnable = true;
                }
        }catch(MalformedGoalException ex){
            throw new IllegalArgumentException();
        }catch(NullPointerException ex){
            throw new IllegalArgumentException();
        }
        return result;
    }

    public String getNextSolution() {
        if (info.hasOpenAlternatives()) {
            try {
                info = engine.solveNext();
                if (!info.isSuccess()) {
                    result += "no.\n";
                    nextSolutionEnable = false;
                } else {
                    result += solveInfoToString(info) + " ? ";
                    if(!info.hasOpenAlternatives()){
                        nextSolutionEnable=false;
                    }
                }
            } catch (NoMoreSolutionException ex) {
                result += "no.";
                throw new IllegalArgumentException(result);
            }
        }
        else {
            nextSolutionEnable = false;
        }
        return result;
    }


}

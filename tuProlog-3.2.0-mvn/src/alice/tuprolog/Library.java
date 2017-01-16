/*
 * tuProlog - Copyright (C) 2001-2002  aliCE team at deis.unibo.it
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package alice.tuprolog;

import java.io.Serializable;
import java.util.*;



public abstract class Library implements Serializable, IPrimitives {
	private static final long serialVersionUID = 1L;
    
    protected Prolog engine;
    
    
    private String[][] opMappingCached;
    
    public Library(){
        opMappingCached = getSynonymMap();
    }
    
    
    public String getName() {
        return getClass().getName();
    }
    
    public String getTheory() {
        return "";
    }
    
    public String getTheory(int a) {
    	return "";
    }
    
    public String[][] getSynonymMap() {
        return null;
    }
    
    public Prolog getEngine() {
        return engine;
    }
    
    public void setEngine(Prolog en) {	
        engine = en;
    }
    
    protected boolean unify(Term a0,Term a1) {
        return engine.unify(a0,a1);
    }
    
    protected boolean match(Term a0,Term a1) {
        return engine.match(a0,a1);
    }
    
    protected Term evalExpression(Term term) throws Throwable {
        if (term == null)
            return null;
        Term val = term.getTerm();
        if (val instanceof Struct) {
            Struct t = (Struct) val;
            if (term != t)
                if (!t.isPrimitive())
                    engine.identifyFunctor(t);
            if (t.isPrimitive()) {
                PrimitiveInfo bt = t.getPrimitive();
                // check for library functors
                if (bt.isFunctor())
                    return bt.evalAsFunctor(t);
            }
        } else if (val instanceof Number) {
            return val;
        }
        return null;
    }
    
    public void dismiss() {}
    
    public void onSolveBegin(Term goal) {}
    
    public void onSolveHalt(){}
    
    public void onSolveEnd() {}
    
    public Map<Integer,List<PrimitiveInfo>> getPrimitives() {
        try {
            java.lang.reflect.Method[] mlist = this.getClass().getMethods();
            Map<Integer,List<PrimitiveInfo>> mapPrimitives = new HashMap<Integer, List<PrimitiveInfo>>();
            mapPrimitives.put(PrimitiveInfo.DIRECTIVE,new ArrayList<PrimitiveInfo>());
            mapPrimitives.put(PrimitiveInfo.FUNCTOR,new ArrayList<PrimitiveInfo>());
            mapPrimitives.put(PrimitiveInfo.PREDICATE,new ArrayList<PrimitiveInfo>());
            //{new ArrayList<PrimitiveInfo>(), new ArrayList<PrimitiveInfo>(), new ArrayList<PrimitiveInfo>()};
            
            for (int i = 0; i < mlist.length; i++) {
                String name = mlist[i].getName();
                
                Class<?>[] clist = mlist[i].getParameterTypes();
                Class<?> rclass = mlist[i].getReturnType();
                String returnTypeName = rclass.getName();
                
                int type;
                if (returnTypeName.equals("boolean")) type = PrimitiveInfo.PREDICATE;
                else if (returnTypeName.equals("alice.tuprolog.Term")) type = PrimitiveInfo.FUNCTOR;
                else if (returnTypeName.equals("void")) type = PrimitiveInfo.DIRECTIVE;
                else continue;
                
                int index=name.lastIndexOf('_');
                if (index!=-1) {
                    try {
                        int arity = Integer.parseInt(name.substring(index + 1, name.length()));
                        // check arg number
                        if (clist.length == arity) {
                            boolean valid = true;
                            for (int j=0; j<arity; j++) {
                                if (!(Term.class.isAssignableFrom(clist[j]))) {
                                    valid = false;
                                    break;
                                }
                            }
                            if (valid) {
                                String rawName = name.substring(0,index);
                                String key = rawName + "/" + arity;
                                PrimitiveInfo prim = new PrimitiveInfo(type, key, this, mlist[i], arity);
                                mapPrimitives.get(type).add(prim);
                                //
                                // adding also or synonims
                                //
                                String[] stringFormat = {"directive","predicate","functor"};
                                if (opMappingCached != null) {
                                    for (int j=0; j<opMappingCached.length; j++){
                                        String[] map = opMappingCached[j];
                                        if (map[2].equals(stringFormat[type]) && map[1].equals(rawName)){
                                            key = map[0] + "/" + arity;
                                            prim = new PrimitiveInfo(type, key, this, mlist[i], arity);
                                            mapPrimitives.get(type).add(prim);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception ex) {}
                }
                
            }
            return mapPrimitives;
        } catch (Exception ex) {
            return null;
        }
    }
}
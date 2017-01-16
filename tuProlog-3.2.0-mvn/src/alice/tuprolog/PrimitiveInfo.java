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

import java.lang.reflect.*;

public class PrimitiveInfo {
    
    public final static int DIRECTIVE  = 0;
    public final static int PREDICATE  = 1;
    public final static int FUNCTOR    = 2;
    
    private int type;
   
    private Method method;
    
    private IPrimitives source;
    
    private Object[] primitive_args;
    private String primitive_key;
    
    
    public PrimitiveInfo(int type, String key, Library lib, Method m, int arity) throws NoSuchMethodException {
        if (m==null) {
            throw new NoSuchMethodException();
        }
        this.type = type;
        primitive_key = key;
        source = lib;
        method = m;
        primitive_args=new Term[arity];
    }
    
    
   
    public synchronized String invalidate() {
        String key = primitive_key;
        primitive_key = null;
        return key;
    }
    
    
    public String getKey() {
        return primitive_key;
    }
    
    public boolean isDirective() {
        return (type == DIRECTIVE);
    }
    
    public boolean isFunctor() {
        return (type == FUNCTOR);
    }
    
    public boolean isPredicate() {
        return (type == PREDICATE);
    }
    
    
    public int getType() {
        return type;
    }
    
    public IPrimitives getSource() {
        return source;
    }
    
    public synchronized void evalAsDirective(Struct g) throws IllegalAccessException, InvocationTargetException {
        for (int i=0; i<primitive_args.length; i++) {
            primitive_args[i] = g.getTerm(i);
        }
        method.invoke(source,primitive_args);
    }
    
    public synchronized boolean evalAsPredicate(Struct g) throws Throwable {
        for (int i=0; i<primitive_args.length; i++) {
            primitive_args[i] = g.getArg(i);
        }
        try {
        	//System.out.println("PRIMITIVE INFO evalAsPredicate sto invocando metodo "+method.getName());
            return ((Boolean)method.invoke(source,primitive_args)).booleanValue();
        } catch (InvocationTargetException e) {
            // throw new Exception(e.getCause());
            throw e.getCause();
        }
    }
    
    public synchronized Term evalAsFunctor(Struct g) throws Throwable {
        try {
            for (int i=0; i<primitive_args.length; i++) {
                primitive_args[i] = g.getTerm(i);
            }
            return ((Term)method.invoke(source,primitive_args));
        } catch (Exception ex) {
            throw ex.getCause();
        }
    }
    
    public String toString() {
        return "[ primitive: method "+method.getName()+" - "+primitive_args+" - N args: "+primitive_args.length+" - "+source.getClass().getName()+" ]\n";
    }
    
}
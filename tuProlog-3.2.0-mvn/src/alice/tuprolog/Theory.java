/*
 * tuProlog - Copyright (C) 2001-2007  aliCE team at deis.unibo.it
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
import    java.io.*;
import java.util.Iterator;

import alice.tuprolog.json.JSONSerializerManager;


public class Theory implements Serializable {
	private static final long serialVersionUID = 1L;
    private String theory;
    private Struct clauseList;

   
    public Theory(InputStream is) throws IOException {
        byte[] info = new byte[is.available()];
        is.read(info);
        theory = new String(info);
    }


    public Theory(String theory) throws InvalidTheoryException {
        if (theory == null) {
            throw new InvalidTheoryException();
        }
        this.theory=theory;
    }
    
    Theory() {
        this.theory = "";
    }

    
    public Theory(Struct clauseList) throws InvalidTheoryException {
        if (clauseList==null || !clauseList.isList()) {
            throw new InvalidTheoryException();
        }
        this.clauseList = clauseList;
    }
    
    public Iterator<? extends Term> iterator(Prolog engine) {
        if (isTextual())
            return new Parser(engine.getOperatorManager(), theory).iterator();
        else
            return clauseList.listIterator();
    }

    
    public void append(Theory th) throws InvalidTheoryException {
        if (th.isTextual() && isTextual()) {
            theory += th.theory;
        } else if (!th.isTextual() && !isTextual()) {
            Struct otherClauseList = th.getClauseListRepresentation();
            if (clauseList.isEmptyList())
                clauseList = otherClauseList;
            else {
                Struct p = clauseList, q;
                while (!(q = (Struct) p.getArg(1)).isEmptyList())
                    p = q;
                p.setArg(1, otherClauseList);
            }
        } else if (!isTextual() && th.isTextual()) {
            theory = theory.toString() + "\n" + th;
            clauseList = null;
        } else if (isTextual() && !th.isTextual()) {
            theory += th.toString();
        }
        else {
            throw new InvalidTheoryException();
        }
    }

    
    boolean isTextual() {
        return theory != null;
    }

    Struct getClauseListRepresentation() {
        return clauseList;
    }

    public String toString() {
        return theory != null ? theory : clauseList.toString();
    }

    //Alberto
  	public String toJSON(){
  		return JSONSerializerManager.toJSON(this);
  	}
  	
  	//Alberto
  	public static Theory fromJSON(String jsonString){
  		return JSONSerializerManager.fromJSON(jsonString, Theory.class);	
  	}

}
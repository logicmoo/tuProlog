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

import java.util.*;

import alice.tuprolog.json.AbstractEngineState;
import alice.tuprolog.json.FullEngineState;
import alice.tuprolog.json.JSONSerializerManager;

/**
 * Administrator of flags declared
 * 
 * @author Alex Benini
 */
class FlagManager {
	
    /* flag list */
    private ArrayList<Flag> flags;

    /**
	 * mediator owner of the manager
	 */
    protected Prolog mediator;

    FlagManager() { //Alberto
        flags = new ArrayList<Flag>();
        
        //occursCheck flag -> a default è on!
        Struct s = new Struct();
        s.append(new Struct("on"));
        s.append(new Struct("off"));
        this.defineFlag("occursCheck", s, new Struct("on"), true, "BuiltIn");
    }

    /**
     * Config this Manager
     */
    public void initialize(Prolog vm) {
        mediator = vm;
    }

    /**
     * Defines a new flag
     */
    public synchronized boolean defineFlag(String name, Struct valueList, Term defValue,
            boolean modifiable, String libName) {
        flags.add(new Flag(name, valueList, defValue, modifiable, libName));
        return true;
    }

    public synchronized boolean setFlag(String name, Term value) {
        java.util.Iterator<Flag> it = flags.iterator();
        while (it.hasNext()) {
            Flag flag = it.next();
            if (flag.getName().equals(name)) {
                if (flag.isModifiable() && flag.isValidValue(value)) {
                    flag.setValue(value);
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public synchronized Struct getPrologFlagList() {
        Struct flist = new Struct();
        java.util.Iterator<Flag> it = flags.iterator();
        while (it.hasNext()) {
            Flag fl = it.next();
            flist = new Struct(new Struct("flag", new Struct(fl.getName()), fl
                    .getValue()), flist);
        }
        return flist;
    }

    public synchronized Term getFlag(String name) {
        java.util.Iterator<Flag> it = flags.iterator();
        while (it.hasNext()) {
            Flag fl = it.next();
            if (fl.getName().equals(name)) {
                return fl.getValue();
            }
        }
        return null;
    }

    // restituisce true se esiste un flag di nome name, e tale flag ?
    // modificabile
    public boolean isModifiable(String name) {
        java.util.Iterator<Flag> it = flags.iterator();
        while (it.hasNext()) {
            Flag flag = it.next();
            if (flag.getName().equals(name)) {
                return flag.isModifiable();
            }
        }
        return false;
    }

    // restituisce true se esiste un flag di nome name, e Value ? un valore
    // ammissibile per tale flag
    public boolean isValidValue(String name, Term value) {
        java.util.Iterator<Flag> it = flags.iterator();
        while (it.hasNext()) {
            Flag flag = it.next();
            if (flag.getName().equals(name)) {
                return flag.isValidValue(value);
            }
        }
        return false;
    }

    //Alberto
	public synchronized boolean isOccursCheckEnabled() {
		for(Flag f : flags){
			if(f.getName().equals("occursCheck")){
				if(f.getValue().toString().equals("on"))
					return true;
				else return false;
			}
		}
		return false;
	}

	//Alberto
	public void serializeFlags(AbstractEngineState brain) {
		if(brain instanceof FullEngineState){
			ArrayList<String> a = new ArrayList<String>();
			for(Flag f : flags){
				a.add(JSONSerializerManager.toJSON(f));
			}
			((FullEngineState) brain).setFlags(a);
		}
	}

	//Alberto
	public void reloadFlags(FullEngineState brain) {
		ArrayList<String> a = brain.getFlags();
		ArrayList<Flag> f = new ArrayList<Flag>();
		for(String s : a){
			Flag fl = JSONSerializerManager.fromJSON(s, Flag.class);
			f.add(fl);
		}
		flags = f;
	}
}

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
/*
 * Marco Prati
 * 15/04/11
 */
package alice.tuprologx.eclipse.util;

import java.util.EventObject;
import java.util.Vector;

import alice.tuprolog.Operator;

/**
 * This class defines an internal event relative to Dynamic Operators generated
 * by the prolog core
 * 
 */
public class OperatorEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4582320772037556853L;
	
	/** event description */
	private String msg;
	private Vector<Operator> opList;

	/**
	 * Standard constructor
	 * 
	 * @param source
	 * @param msg_
	 */
	public OperatorEvent(Object source, String msg_) {
		super(source);
		msg = msg_;
	}

	/**
	 * Create a new Opertaor Event with the given operator vector
	 * 
	 * @param source
	 * @param opList_
	 *            Operator Vector
	 */
	public OperatorEvent(Object source, Vector<Operator> opList_) {
		super(source);
		opList = opList_;
	}

	public String getMsg() {
		return msg;
	}

	public Vector<Operator> getOpList() {
		return opList;
	}

	public Vector<String> getOpListAsStringList() {
		Vector<String> toRet = new Vector<String>();
		for(Operator o:opList){
			toRet.add(o.name);
		}
		return toRet;
	}

	public String toString() {
		return msg;
	}

}
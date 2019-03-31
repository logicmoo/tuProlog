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
package alice.tuprolog.lib;

import alice.tuprolog.*;
import static alice.tuprolog.TuPrologError.*;
import static alice.tuprolog.TuFactory.*;


/**
 * This class represents a tuProlog library providing most of the built-ins
 * predicates and functors defined by ISO standard.
 * 
 * Library/Theory dependency: BasicLibrary
 * 
 * 
 * 
 */
public class ISOLibrary extends TuLibrary {
	private static final long serialVersionUID = 1L;
    public ISOLibrary() {
    }

    public boolean atom_length_2(Term arg0, Term len) throws TuPrologError {
        arg0 = arg0.dref();
        if (arg0 .isVar())
            throw instantiation_error(engine.getEngineManager(), 1);
        if (!arg0.isAtomSymbol())
            throw type_error(engine.getEngineManager(), 1, "atom",
                    arg0);
        TuStruct atom = (TuStruct) arg0;
        return unify(len, createTuInt(atom.fname().length()));
    }

    public boolean atom_chars_2(Term arg0, Term arg1) throws TuPrologError {
        arg0 = arg0.dref();
        arg1 = arg1.dref();
        if (arg0 .isVar()) {
            if (!arg1.isPlList()) {
                throw type_error(engine.getEngineManager(), 2,
                        "list", arg1);
            }
            TuStruct list = (TuStruct) arg1;
            if (list.isEmptyList()) {
                return unify(arg0, createTuAtom(""));
            }
            String st = "";
            while (!(list.isEmptyList())) {
                String st1 = list.getDerefArg(0).toString();
                try {
                    if (st1.startsWith("'") && st1.endsWith("'")) {
                        st1 = st1.substring(1, st1.length() - 1);
                    }
                    /*else
                    {
                    	byte[] b= st1.getBytes();
                    	st1=""+b[0];
                    }*/
                    
                } catch (Exception ex) {
                }
                ;
                st = st.concat(st1);
                list = (TuStruct) list.getDerefArg(1);
            }
            return unify(arg0, createTuAtom(st));
        } else {
            if (!arg0.isAtomSymbol()) {
                throw type_error(engine.getEngineManager(), 1,
                        "atom", arg0);
            }
            String st = ((TuStruct) arg0).fname();
            Term[] tlist = new Term[st.length()];
            for (int i = 0; i < st.length(); i++) {
                tlist[i] = createTuAtom(new String(new char[] { st.charAt(i) }));
            }
            TuStruct list = createTuListStruct(tlist);
            /*
             * for (int i=0; i<st.length(); i++){ Struct ch=new Struct(new
             * String(new char[]{ st.charAt(st.length()-i-1)} )); list=new
             * Struct( ch, list); }
             */

            return unify(arg1, list);
        }
    }

    public boolean char_code_2(Term arg0, Term arg1) throws TuPrologError {
        arg0 = arg0.dref();
        arg1 = arg1.dref();
        if (arg1 .isVar()) {
            if (arg0.isAtomSymbol()) {
                String st = ((TuStruct) arg0).fname();
                if (st.length() <= 1)
                    return unify(arg1, createTuInt(st.charAt(0)));
                else
                    throw type_error(engine.getEngineManager(), 1,
                            "character", arg0);
            } else
                throw type_error(engine.getEngineManager(), 1,
                        "character", arg0);
        } else if ((arg1 .isInt())
                || (arg1 instanceof alice.tuprolog.TuLong)) {
            char c = (char) ((TuNumber) arg1).intValue();
            return unify(arg0, createTuAtom("" + c));
        } else
            throw type_error(engine.getEngineManager(), 2,
                    "integer", arg1);
    }

    //

    // functors

    public Term sin_1(Term val) {
        Term val0 = null;
        try {
            val0 = evalExpression(val);
        } catch (Throwable e) {

        }
        if (val0 .isNumber())
            return createTuDouble(Math.sin(((TuNumber) val0)
                    .doubleValue()));
        return null;
    }

    public Term cos_1(Term val) {
        Term val0 = null;
        try {
            val0 = evalExpression(val);
        } catch (Throwable e) {

        }
        if (val0 .isNumber())
            return createTuDouble(Math.cos(((TuNumber) val0)
                    .doubleValue()));
        return null;
    }

    public Term exp_1(Term val) {
        Term val0 = null;
        try {
            val0 = evalExpression(val);
        } catch (Throwable e) {

        }
        if (val0 .isNumber())
            return createTuDouble(Math.exp(((TuNumber) val0)
                    .doubleValue()));
        return null;
    }

    public Term atan_1(Term val) {
        Term val0 = null;
        try {
            val0 = evalExpression(val);
        } catch (Throwable e) {

        }
        if (val0 .isNumber())
            return createTuDouble(Math.atan(((TuNumber) val0)
                    .doubleValue()));
        return null;
    }

    public Term log_1(Term val) {
        Term val0 = null;
        try {
            val0 = evalExpression(val);
        } catch (Throwable e) {

        }
        if (val0 .isNumber())
            return createTuDouble(Math.log(((TuNumber) val0)
                    .doubleValue()));
        return null;
    }

    public Term sqrt_1(Term val) {
        Term val0 = null;
        try {
            val0 = evalExpression(val);
        } catch (Throwable e) {

        }
        if (val0 .isNumber())
            return createTuDouble(Math.sqrt(((TuNumber) val0)
                    .doubleValue()));
        return null;
    }

    public Term abs_1(Term val) {
        Term val0 = null;
        try {
            val0 = evalExpression(val);
        } catch (Throwable e) {

        }
        if (val0 .isInt() || val0 instanceof alice.tuprolog.TuLong)
            return createTuInt(Math.abs(((TuNumber) val0).intValue()));
        if (val0 instanceof alice.tuprolog.TuDouble
                || val0 instanceof alice.tuprolog.TuFloat)
            return createTuDouble(Math.abs(((TuNumber) val0)
                    .doubleValue()));
        return null;
    }

    public Term sign_1(Term val) {
        Term val0 = null;
        try {
            val0 = evalExpression(val);
        } catch (Throwable e) {

        }
        if (val0 .isInt() || val0 instanceof alice.tuprolog.TuLong)
            return createTuDouble(((TuNumber) val0).intValue() > 0 ? 1.0 : -1.0);
        if (val0 instanceof alice.tuprolog.TuDouble
                || val0 instanceof alice.tuprolog.TuFloat)
            return createTuDouble(((TuNumber) val0).doubleValue() > 0 ? 1.0 : -1.0);
        return null;
    }

    public Term float_integer_part_1(Term val) {
        Term val0 = null;
        try {
            val0 = evalExpression(val);
        } catch (Throwable e) {

        }
        if (val0 .isNumber())
            return createTuDouble((long) Math.rint(((TuNumber) val0)
                    .doubleValue()));
        return null;
    }

    public Term float_fractional_part_1(Term val) {
        Term val0 = null;
        try {
            val0 = evalExpression(val);
        } catch (Throwable e) {

        }
        if (val0 .isNumber()) {
            double fl = ((TuNumber) val0).doubleValue();
            return createTuDouble(Math.abs(fl - Math.rint(fl)));
        }
        return null;
    }

    public Term float_1(Term val) {
        Term val0 = null;
        try {
            val0 = evalExpression(val);
        } catch (Throwable e) {

        }
        if (val0 .isNumber())
            return createTuDouble(((TuNumber) val0).doubleValue());
        return null;
    }

    public Term floor_1(Term val) {
        Term val0 = null;
        try {
            val0 = evalExpression(val);
        } catch (Throwable e) {

        }
        if (val0 .isNumber())
            return createTuInt((int) Math.floor(((TuNumber) val0).doubleValue()));
        return null;
    }

    public Term round_1(Term val) {
        Term val0 = null;
        try {
            val0 = evalExpression(val);
        } catch (Throwable e) {

        }
        if (val0 .isNumber())
            return createTuLong(Math.round(((TuNumber) val0)
                    .doubleValue()));
        return null;
    }

    public Term truncate_1(Term val) {
        Term val0 = null;
        try {
            val0 = evalExpression(val);
        } catch (Throwable e) {

        }
        if (val0 .isNumber())
            return createTuInt((int) Math.rint(((TuNumber) val0).doubleValue()));
        return null;
    }

    public Term ceiling_1(Term val) {
        Term val0 = null;
        try {
            val0 = evalExpression(val);
        } catch (Throwable e) {

        }
        if (val0 .isNumber())
            return createTuInt((int) Math.ceil(((TuNumber) val0).doubleValue()));
        return null;
    }

    public Term div_2(Term v0, Term v1) throws TuPrologError {
        Term val0 = null;
        Term val1 = null;
        try {
            val0 = evalExpression(v0);
            val1 = evalExpression(v1);
        } catch (Throwable e) {

        }
        if (val0 .isNumber() && val1 .isNumber())
            return createTuInt(((TuNumber) val0).intValue()
                    / ((TuNumber) val1).intValue());
        return null;
    }

    public Term mod_2(Term v0, Term v1) throws TuPrologError {
        Term val0 = null;
        Term val1 = null;
        try {
            val0 = evalExpression(v0);
            val1 = evalExpression(v1);
        } catch (Throwable e) {

        }
        if (val0 .isNumber() && val1 .isNumber()) {
            int x = ((TuNumber) val0).intValue();
            int y = ((TuNumber) val1).intValue();
            int f = new java.lang.Double(Math.floor((double) x / (double) y))
                    .intValue();
            return createTuInt(x - (f * y));
        }
        return null;
    }

    public Term rem_2(Term v0, Term v1) {
        Term val0 = null;
        Term val1 = null;
        try {
            val0 = evalExpression(v0);
            val1 = evalExpression(v1);
        } catch (Throwable e) {

        }
        if (val0 .isNumber() && val1 .isNumber()) {
            return createTuDouble(Math.IEEEremainder(((TuNumber) val0)
                    .doubleValue(), ((TuNumber) val1).doubleValue()));
        }
        return null;
    }

    /**
     * library theory
     */
    @Override
	public String getTheory() {
        return
        //
        // operators defined by the ISOLibrary theory
        //
        ":- op(  300, yfx,  'div'). \n"
                + ":- op(  400, yfx,  'mod'). \n"
                + ":- op(  400, yfx,  'rem'). \n"
                + ":- op(  200, fx,   'sin'). \n"
                + ":- op(  200, fx,   'cos'). \n"
                + ":- op(  200, fx,   'sqrt'). \n"
                + ":- op(  200, fx,   'atan'). \n"
                + ":- op(  200, fx,   'exp'). \n"
                + ":- op(  200, fx,   'log'). \n"
                +
                //
                // flags defined by the ISOLibrary theory
                //
                ":- flag(bounded, [true,false], true, false).\n"
                + ":- flag(max_integer, ["
                + new Integer(Integer.MAX_VALUE).toString()
                + "], "
                + new Integer(Integer.MAX_VALUE).toString()
                + ",false).\n"
                + ":- flag(min_integer, ["
                + new Integer(Integer.MIN_VALUE).toString()
                + "], "
                + new Integer(Integer.MIN_VALUE).toString()
                + ",false).\n"
                + ":- flag(integer_rounding_function, [up,down], down, false).\n"
                + ":- flag(char_conversion,[on,off],off,false).\n"
                + ":- flag(debug,[on,off],off,false).\n"
                + ":- flag(max_arity, ["
                + new Integer(Integer.MAX_VALUE).toString()
                + "], "
                + new Integer(Integer.MAX_VALUE).toString()
                + ",false).\n"
                + ":- flag(undefined_predicate, [error,fail,warning], fail, false).\n"
                + ":- flag(double_quotes, [atom,chars,codes], atom, false).\n"
                //
                //
                + "bound(X):-ground(X).\n                                                                                  "
                + "unbound(X):-not(ground(X)).\n                                                                          "
                
                //
                + "atom_concat(F,S,R) :- catch(atom_concat0(F,S,R), Error, false).\n"
                + "atom_concat0(F,S,R) :- var(R), !,(atom_chars(S,SL),append(FL,SL,RS),atom_chars(F,FL),atom_chars(R,RS)).  \n"
                + "atom_concat0(F,S,R) :-(atom_chars(R,RS), append(FL,SL,RS),atom_chars(F,FL),atom_chars(S,SL)).\n"
                
                + "atom_codes(A,L):- catch(atom_codes0(A,L), Error, false).\n"
                + "atom_codes0(A,L):-nonvar(A),atom_chars(A,L1),!,chars_codes(L1,L).\n"
                + "atom_codes0(A,L):-nonvar(L), list(L), !,chars_codes(L1,L),atom_chars(A,L1).\n"
                + "chars_codes([],[]).\n"
                + "chars_codes([X|L1],[Y|L2]):-char_code(X,Y),chars_codes(L1,L2).\n"
            
                + "sub_atom(Atom,B,L,A,Sub):- sub_atom_guard(Atom,B,L,A,Sub), sub_atom0(Atom,B,L,A,Sub).\n"
                + "sub_atom0(Atom,B,L,A,Sub):-atom_chars(Atom,L1),sub_list(L2,L1,B),atom_chars(Sub,L2),length(L2,L), length(L1,Len), A is Len-(B+L).\n"
                + "sub_list([],_,0).\n"
                + "sub_list([X|L1],[X|L2],0):- sub_list_seq(L1,L2).\n"
                + "sub_list(L1,[_|L2],N):- sub_list(L1,L2,M), N is M + 1.\n"
                + "sub_list_seq([],L).\n"
                + "sub_list_seq([X|L1],[X|L2]):-sub_list_seq(L1,L2).\n"
                
                + "number_chars(Number,List):-catch(number_chars0(Number,List), Error, false).\n"
                + "number_chars0(Number,List):-nonvar(Number),!,num_atom(Number,Struct),atom_chars(Struct,List).\n"
                + "number_chars0(Number,List):-atom_chars(Struct,List),num_atom(Number,Struct).\n"
                
                + "number_codes(Number,List):-catch(number_codes0(Number,List), Error, false).\n"
                + "number_codes0(Number,List):-nonvar(Number),!,num_atom(Number,Struct),atom_codes(Struct,List).\n"
                + "number_codes0(Number,List):-atom_codes(Struct,List),num_atom(Number,Struct).\n";
        //
        // ISO default
        // "current_prolog_flag(changeable_flags,[ char_conversion(on,off), debug(on,off), undefined_predicate(error,fail,warning),double_quotes(chars,codes,atom) ]).\n"+
        // "current_prolog_flag(changeable_flags,[]).\n                                                              "+

    }

    // Java guards for Prolog predicates

    public boolean sub_atom_guard_5(Term arg0, Term arg1, Term arg2, Term arg3, Term arg4)
            throws TuPrologError {
        arg0 = arg0.dref();
        if (!arg0.isAtomSymbol())
            throw type_error(engine.getEngineManager(), 1, "atom", arg0);
        return true;
    }

}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using fit;
using alice.tuprolog;
using alice.tuprolog.@event;
using java.util;

namespace Fixtures
{
    public class EngineFixture : Fixture
    {
        private Prolog engine = new Prolog();
        private String query;
        private SolveInfo result;
        private String variable;
        private String output;

        public static bool EXCEPTIONS_IMPLEMENTED = false;

        public EngineFixture()
        {
           
            OOLibrary.OOLibrary lib = new OOLibrary.OOLibrary();
            engine.unloadLibrary("alice.tuprolog.lib.JavaLibrary");
            engine.loadLibrary(lib);
        }

        /* Registers */

        public void Theory(String program)
        {
            engine.setTheory(new Theory(program));
        }

        public void Query(String problem)
        {
            this.query = problem;
        }

        public void Variable(String name)
        {
            this.variable = name;

        }

        /* Meters */

        public bool hasSolution()
        {
            result = engine.solve(this.query);
            return result.isSuccess();

        }

        public bool hasSolutionWithOutput()
        {
            output = "";
            engine.addOutputListener(new StringOutputListener(output));
            result = engine.solve(query);
            engine.removeAllOutputListeners();
            return result.isSuccess();
        }

        public bool hasAnotherSolution()
        {
            try
            {
                result = engine.solveNext();
                return result.isSuccess();
            }
            catch (PrologException e)
            {
                return false;
            }
        }

        public bool hasAnotherSolutionWithOutput()
        {
            output = "";
            engine.addOutputListener(new StringOutputListener(output));
            try
            {
                result = engine.solveNext();
                engine.removeAllOutputListeners();
                return result.isSuccess();
            }
            catch (PrologException e)
            {
                return false;
            }
        }

        public Term binding()
        {
            Term t = result.getVarValue(variable);
            // t.resolveVariables();
            return t;
        }

        public String Output()
        {
            return output;
        }

        /** Exceptions have not been implemented in tuProlog yet. */
        public Term exception()
        {
            return NullTerm.NULL_TERM;
        }

        public Theory getTheory()
        {
            return engine.getTheory();
        }

    }

    internal class NullTerm : Term 
    {

	public static NullTerm NULL_TERM = new NullTerm(); 

    private NullTerm() 
    {
    }

    // checking type and properties of the Term

    /** is this term a prolog numeric term? */
    public override bool isNumber(){
        return false;
    }

    /** is this term a struct  */
    public override bool isStruct()
    {
        return false;
    }

    /** is this term a variable  */
    public override bool isVar(){
        return false;
    }

    public override bool isEmptyList()
    {
        return false;
    }

    //

    public override bool isAtomic()
    {
        return true;
    }

    public override bool isCompound()
    {
        return false;
    }

    public override bool isAtom()
    {
        return false;
    }

    public override bool isList()
    {
        return false;
    }

    public override bool isGround()
    {
        return true;
    }

    public override bool isGreater(Term t)
    {
        return false;
    }

    public override bool isEqual(Term t)
    {
        return false;
    }

    public Term copy(int idExecCtx){
        return this;
    }

    public override Term getTerm(){
        return this;
    }


    int renameOuterVars(int count, int antialias){
        return antialias;
    }

    long resolveTerm(long count){
        return count;
    }

    public override void free()
    {
    }

    Term copy(AbstractMap vMap, int idExecCtx){
        return this;
    }

    Term copy(AbstractMap vMap, AbstractMap substMap){
        return this;
    }

    bool unify(List varsUnifiedArg1, List varsUnifiedArg2, Term t){
        return false;
    }


    public override void accept(TermVisitor tv)
    {
        throw new NotImplementedException();
    }
    }

    internal class StringOutputListener : OutputListener
    {
        private string _output = null;

        public StringOutputListener(String output)
        {
            _output = output;
        }
        public void onOutput(OutputEvent oe)
        {
            _output += oe.getMsg();
        }

    }
}

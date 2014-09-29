using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using fit;
using alice.tuprolog;

namespace Fixtures
{
    public class SimpleGoalFixture : ColumnFixture
    {
        public String goal;

        public bool Success()
        {
            Prolog engine = new Prolog();
            OOLibrary.OOLibrary lib = new OOLibrary.OOLibrary();
            engine.unloadLibrary("alice.tuprolog.lib.JavaLibrary");
            engine.loadLibrary(lib);
            SolveInfo info = engine.solve(goal);
            return info.isSuccess();
        }

    }
}

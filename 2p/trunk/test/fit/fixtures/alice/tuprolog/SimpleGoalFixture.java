package alice.tuprolog;

import fit.ColumnFixture;

public class SimpleGoalFixture extends ColumnFixture {

	public String goal;

	public boolean success() throws Exception {
		Prolog engine = new Prolog();
		SolveInfo info = engine.solve(goal);
		return info.isSuccess();
	}

}

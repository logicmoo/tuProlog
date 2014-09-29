package alice.tuprolog;

import fit.ActionFixture;
import fit.TypeAdapter;

public class PrologActionFixture extends ActionFixture {

	public void check() throws Exception {
        boolean exceptionsImplemented = true;
        if (actor instanceof EngineFixture)
            exceptionsImplemented = EngineFixture.EXCEPTIONS_IMPLEMENTED;
        else
            exceptionsImplemented = EvaluationFixture.EXCEPTIONS_IMPLEMENTED;
		if (!exceptionsImplemented && "exception".equals(method(0).getName()))
			ignore(cells.more.more);
		else {
			TypeAdapter adapter = TypeAdapter.on(actor, method(0));
        	check(cells.more.more, adapter);
		}
	}

}
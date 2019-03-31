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
import static alice.tuprolog.TuPrologError.*;
import static alice.tuprolog.TuFactory.*;

/**
 * @author Alex Benini
 */
public class StateGoalEvaluation extends TuState {

	public StateGoalEvaluation(EngineRunner c) {
		this.c = c;
		stateName = "Eval";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alice.tuprolog.AbstractRunState#doJob()
	 */
	@Override
	void doJob(TuEngine e) {
		if (e.currentContext.currentGoal.isPrimitive()) {
			// Recupero primitiva
			PrimitiveInfo primitive = e.currentContext.currentGoal
			.getPrimitive();
			try {
				e.nextState = (primitive
						.evalAsPredicate(e.currentContext.currentGoal)) ? c.GOAL_SELECTION
								: c.BACKTRACK;
			} catch (TuHaltException he) {
				e.nextState = c.END_HALT;
			} catch (Throwable t) {

				if (t  instanceof TuPrologError) {
					// cast da Throwable a PrologError
					TuPrologError error = (TuPrologError) t;
					// sostituisco il gol in cui si ? verificato l'errore con il
					// subgoal throw/1
					e.currentContext.currentGoal = S("throw", error.getError());
					/*Castagna 06/2011*/					
					e.manager.exception(error.toString());
					/**/
				} else if (t instanceof TuJavaException) {
					// cast da Throwable a JavaException
					TuJavaException exception = (TuJavaException) t;

					// sostituisco il gol in cui si ? verificato l'errore con il
					// subgoal java_throw/1
					e.currentContext.currentGoal = S("java_throw", exception.getException());
					/*Castagna 06/2011*/					
					e.manager.exception(exception.getException().toString());
					/**/
				}

				// mi sposto nello stato EXCEPTION
				e.nextState = c.EXCEPTION;
			}
			// Incremento il counter dei passi di dimostrazione
			e.nDemoSteps++;
		} else {
			e.nextState = c.RULE_SELECTION;
		}
	}
}
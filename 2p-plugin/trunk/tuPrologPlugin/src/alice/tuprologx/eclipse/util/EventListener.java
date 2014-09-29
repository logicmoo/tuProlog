package alice.tuprologx.eclipse.util;

import alice.tuprolog.event.ExceptionEvent;
import alice.tuprolog.event.ExceptionListener;
import alice.tuprolog.event.OutputEvent;
import alice.tuprolog.event.OutputListener;
import alice.tuprolog.event.SpyEvent;
import alice.tuprolog.event.SpyListener;

public class EventListener implements SpyListener, OutputListener/*Castagna06/2011*/, ExceptionListener {

	private String spy = "";
	private String output = "";
	/*Castagna 06/2011*/
	private String exception = "";
	/**/

	public void onSpy(SpyEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSnapshot() == null) {
			spy = arg0.getMsg();
		} else {
			spy = arg0.getSnapshot().toString();
		}

	}

	public void onOutput(OutputEvent arg0) {
		// TODO Auto-generated method stub
		output += arg0.getMsg(); /* Modified to fix nl bug*/
	}

	public String getSpy() {
		return spy;
	}

	public String getOutput() {
		return output;
	}

	public void setSpy(String spy) {
		this.spy = spy;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	/*Castagna 06/2011*/
	public void onException(ExceptionEvent arg0) {
		this.exception = arg0.getMsg();
	}
	
	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}
	/**/

}

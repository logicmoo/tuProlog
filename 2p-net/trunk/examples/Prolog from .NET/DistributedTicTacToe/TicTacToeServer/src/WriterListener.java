

import alice.tuprolog.event.ExceptionEvent;
import alice.tuprolog.event.ExceptionListener;
import alice.tuprolog.event.OutputEvent;
import alice.tuprolog.event.OutputListener;
//Permette di scrivere nella console gli output dell'inteprete
public class WriterListener implements OutputListener, ExceptionListener {

	private void write(String str)
	{
		System.out.print(str);
	}
	@Override
	public void onOutput(OutputEvent arg0) {
		write(arg0.getMsg());
	}

	@Override
	public void onException(ExceptionEvent arg0) {		
		write(arg0.getMsg());
	}

}

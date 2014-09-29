package alice.tuprologx.eclipse.views;

import java.io.ByteArrayInputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import alice.tuprolog.event.ReadEvent;
import alice.tuprolog.event.ReadListener;
import alice.tuprolog.lib.UserContextInputStream;

/***
 * This class has been created and inserted to ConsoleView
 * to conform the management of input of plugin as
 * that of JavaIDE 
 */

public class InputViewer extends Composite implements ReadListener {
	
	private UserContextInputStream stream;
	private Text input;
	
	InputViewer(SashForm sashIn) {
		super(sashIn, SWT.NONE);
		init();
	}
	
	public void setUserContextInputStream(UserContextInputStream str) {
		stream = str;
		stream.setReadListener(this);
	}
	
	public void init()
	{
		GridLayout tabLayout = new GridLayout();
		tabLayout.numColumns = 3;
		this.setLayout(tabLayout);
		
		Label inputLabel = new Label(this, SWT.NONE);
		inputLabel.setText("Input: ");
		
		input = new Text(this, SWT.MULTI | SWT.BORDER);
		input.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		input.setEnabled(false);
		input.addKeyListener(new org.eclipse.swt.events.KeyListener() {
			@Override
			public void keyPressed(org.eclipse.swt.events.KeyEvent arg0) {
				if(arg0.keyCode == 13)
				{ 
					stream.putInput(new ByteArrayInputStream(input.getText().toString().getBytes()));
					input.setEnabled(false);
					input.setText("");
				}
			}
			
			@Override
			public void keyReleased(org.eclipse.swt.events.KeyEvent arg0) {}			
		});		
	}
	
	public Text getInput() {
		return input;
	}
	
	@Override
	public void readCalled(ReadEvent arg0) {
		input.setEnabled(true);
		input.setFocus();
	}
}
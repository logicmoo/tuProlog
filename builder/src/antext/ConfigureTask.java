package antext;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.UIManager;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.CallTarget;

import antext.ui.ConfigureDialog;

public class ConfigureTask extends Task {
	
	protected Object lock;
	
	protected ConfigureDialog dialog;
	protected ArrayList<PropertyFile> propertyFiles;
	protected BuildOptions buildOptions;
	
	public ConfigureTask() {
		propertyFiles = new ArrayList<>();
		lock = new Object();
	}
	
	public List<PropertyFile> getPropertyFiles() {
		return this.propertyFiles;
	}
	
	
	@Override
	public void execute() {
		/*
		try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
		catch(Exception ex) {}*/
		
		
		dialog = new ConfigureDialog(this);
		/*
		dialog.addWindowListener(new WindowAdapter() {
			@Override
	        public void windowClosing(WindowEvent arg0) {
				System.out.println("Closed");
				synchronized (lock) {
					lock.notify();	
				}	
			}
		});*/
		
		dialog.setVisible(true);
		/*
		dialog.toFront();
		
		synchronized (lock) {
			try { lock.wait(); }
			catch(InterruptedException ex) {}
		}*/
		
		
	}
	
	public void startBuild() {
		try {
			dialog.updatePropertyFiles();
		}
		catch(IOException ex) {
			System.out.println("IOException: " + ex.getMessage());
			return;
		}		
		dialog.dispose();
		// dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
	}
	
	public void insertOption(Option option) {
		for(String target : option.getTargets()) {
			CallTarget callTarget = new CallTarget();
			
			callTarget.setProject(getProject());
			callTarget.setOwningTarget(getOwningTarget());
			callTarget.setTaskName(getTaskName());
			callTarget.setLocation(getLocation());
			callTarget.setTarget(target);
			
			getOwningTarget().addTask(callTarget);
		}			
	}
	
	public PropertyFile createPropertyFile() {
		PropertyFile element = new PropertyFile(this);
		propertyFiles.add(element);
		return element;
	}
	
	public BuildOptions createBuildOptions() {
		buildOptions = new BuildOptions(this);
		return buildOptions;
	}
	
	public BuildOptions getBuildOptions() {
		return this.buildOptions;
	}
	
}



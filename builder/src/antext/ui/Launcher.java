package antext.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.tools.ant.BuildLogger;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

public class Launcher {
	public static void main(String[] args) {
		
		BuildLogger logger = new DefaultLogger();
//		try {
//			PrintStream p = new PrintStream(new File ("Log.log"));
//			logger.setOutputPrintStream(p);
//			logger.setErrorPrintStream(p);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	    logger.setOutputPrintStream(System.out);
	    logger.setErrorPrintStream(System.out);
	    logger.setMessageOutputLevel(Project.MSG_INFO);
	 	Project metricsProject  = new Project();
	    metricsProject.addBuildListener(logger);

	    ProjectHelper helper = ProjectHelper.getProjectHelper();
	    metricsProject.addReference("ant.projectHelper", helper);

	    File buildFile = new File("build.xml");
	    
        helper.parse(metricsProject, buildFile);
        metricsProject.setProperty("ant.file", buildFile.getAbsolutePath());

        metricsProject.init();
        metricsProject.executeTarget("configure");
	}
}

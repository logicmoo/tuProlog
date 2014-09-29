package application;

import java.util.ArrayList;

import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.uikit.*;
import org.robovm.objc.Selector;
import org.robovm.objc.annotation.*;
import org.robovm.rt.bro.annotation.Callback;

import alice.tuprolog.*;

@SuppressWarnings("unused")
@CustomClass("ViewController")
public class ViewController extends UIViewController {
	
	private ApplicationDelegate app;
	private UITextView solutionTextView;
	private UITextView warningsTextView;
	private UITextField goalTextField;
	private UITextField theoryTextField;
	private UILabel label;
	private UILabel solutionLabel;
	private UIButton solveButton;
	private UIButton nextButton;
	private UIButton theoryButton;
	private UITextView theoryTextView;
		
	private Prolog engine = null;
	private SolveInfo info = null;
	private String result = "";
	private final String incipit = "tuProlog system - release " + Prolog.getVersion() + "\n";
    
	//Permette di scegliere se inserire la teoria in una textView o in un textField
	private boolean useTextField = false;
	
	//Constructor
    public ViewController(ApplicationDelegate app) {
        super("ViewController", null);
        this.app = app;
        init_prolog();
    }
    
    
    //Objective-C: handlers collegati al file .nib creato con Xcode
    @Callback
    @BindSelector("viewDidLoad")
    private static void viewDidLoad(ViewController self, Selector sel) {
    	self.theoryTextView.setHidden(self.useTextField);
    	self.theoryTextField.setHidden(!self.useTextField);
    	if (!self.useTextField) {
			self.theoryTextView.getLayer().setBorderColor(self.theoryTextView.getTextColor().getCGColor());
			self.theoryTextView.setTextColor(UIColor.colorLightGray());
			self.theoryTextView.getLayer().setCornerRadius(5.0);
			self.theoryTextView.getLayer().setBorderWidth(1.0);
			self.theoryTextView.setDelegate(new TextViewDelegate(self));
    	}
    }
    
    @Callback
    @BindSelector("setTheory:")
    private static void setTheory(ViewController self, Selector sel, UIButton button) {
    	self.hideKeyboard();
    	if (self.useTextField)
    		self.setTheory(self.theoryTextField.getText());
    	else
    		self.setTheory(self.theoryTextView.getText());
    }
    
    @Callback
    @BindSelector("theoryChanged:")
    private static void theoryChanged(ViewController self, Selector sel, UITextField textField) {
    	boolean choice = self.theoryTextField.getText().isEmpty();
    	self.enableTheoryButton(!choice);
    }
    
    @Callback
    @BindSelector("hideKeyboard:")
    private static void hideKeyboard(ViewController self, Selector sel, UIView view) {
    	self.hideKeyboard();
    } 
    
    @Callback
    @BindSelector("solve:")
    private static void solve(ViewController self, Selector sel, UIView view) {
    	self.hideKeyboard();
    	self.solve(self.goalTextField.getText());
    }
    
    @Callback
    @BindSelector("getNextSolution:")
    private static void getNextSolution(ViewController self, Selector sel, UIButton button) {
    	self.getNextSolution();
    }
    
    
    //Instance methods
    private void init_prolog() {
    	if (engine == null) {
	    	engine = new Prolog();
    	}
    }
    
    private void setTheory(String theory) {
    	if (theory != null && theory != "") {
			try {
				engine.setTheory(new Theory(theory));
		    	solutionTextView.setText("Theory set!");
				warningsTextView.setText("");
			} catch (InvalidTheoryException e) {
				warningsTextView.setText("Error setting theory: Syntax Error at/before line " + e.line);
			}
    	} else
    		warningsTextView.setText("WARNING: Theory is empty");
    }
    
    public void solve(String goal)
    {	
    	result = "";
    	warningsTextView.setText("");
        if (!goal.equals(""))
        {
            try
            {
                result += "Solving...";
                solveGoal(goal);
            } catch (Exception e) {
                warningsTextView.setText("Error: " + e);
            }   
        }
        else//if (goal.equals(""))
            /**
             * without this if getGoal is void still remains
             * status message of the precedent solve operation
             */ 
            result += "Ready.";
        
//        solutionTextView.setText(incipit + "\n" + result);
        solutionTextView.setText(result);
    }
    
    private void solveGoal(String goal){
    	result = "";
    	warningsTextView.setText("");
    	try {
        	info = engine.solve(goal);
         	
        	if (engine.isHalted())
        		System.exit(0);
            if (!info.isSuccess()) {      		
        		if(info.isHalted())
        			result += "halt.";
        		else
	                result += "no.";
            } else
                if (!engine.hasOpenAlternatives()) {
                    String binds = info.toString();
                    if (binds.equals("")) {
                        result += "yes.";
                    } else
                    	result += solveInfoToString(info) + "\nyes.";
                } else {
                	result += solveInfoToString(info) + " ? ";
                	nextButton.setEnabled(true);
                }
    	} catch (MalformedGoalException ex) {
    		warningsTextView.setText("Syntax Error: malformed goal.");
    	}
    }  
    
    private String solveInfoToString(SolveInfo result) {
        String s = "";
        try {
            for (Var v: result.getBindingVars()) {
                if ( !v.isAnonymous() && v.isBound() && (!(v.getTerm() instanceof Var) || (!((Var) (v.getTerm())).getName().startsWith("_")))) {
                    s += v.getName() + " / " + v.getTerm() + "\n";
                }
            }
            if(s.length()>0)
                s.substring(0,s.length()-1);   
        } catch (NoSolutionException e) {}
        return s;
    }
    
    public void getNextSolution(){
    	if (info.hasOpenAlternatives()) {
    		try {
		        info = engine.solveNext();
		        if (!info.isSuccess()) {
		            result += "no.\n";
		            nextButton.setEnabled(false);
		        } else
		        	result += solveInfoToString(info) + " ? ";
		    } catch (NoMoreSolutionException ex) {
		        result += "no.";
		    }
    	}
    			
//    	solutionTextView.setText(incipit + "\n" + result);
    	solutionTextView.setText(result);
    }

    public void enableTheoryButton(boolean choice) {
    	if ((theoryButton.isEnabled() && choice == false) || (!theoryButton.isEnabled() && choice == true))
    		theoryButton.setEnabled(choice);
    }
    
    public void hideKeyboard() {
    	if (theoryTextField.isFirstResponder())
    		theoryTextField.resignFirstResponder();
    	else if (theoryTextView.isFirstResponder())
    		theoryTextView.resignFirstResponder();
    	else if (goalTextField.isFirstResponder())
    		goalTextField.resignFirstResponder();
    }
    
    public void refreshView(double offset) {
    	for (UIView view : getViews()) {
    		CGRect rect = view.getFrame();
    		view.setFrame(new CGRect(rect.origin().x(), rect.origin().y()+offset, rect.getWidth(), rect.getHeight()));
    	}
    }
    
    private ArrayList<UIView> getViews() {
    	ArrayList<UIView> views = new ArrayList<UIView>();
    	views.add(theoryButton);
    	views.add(label);
    	views.add(goalTextField);
    	views.add(solveButton);
    	views.add(nextButton);
    	views.add(solutionLabel);
    	views.add(solutionTextView);
    	views.add(warningsTextView);
    	return views;
    }
    
    
  
    // View elements setters
    @Property
    @TypeEncoding("v@:@")
    public void setGoalTextField(UITextField textField) {
    	this.goalTextField = textField;
    }
    @Property
    @TypeEncoding("v@:@")
    public void setSolutionTextView(UITextView textView) {
    	this.solutionTextView = textView;
    }
    @Property
    @TypeEncoding("v@:@")
    public void setWarningsTextView(UITextView textView) {
    	this.warningsTextView = textView;
    }
    @Property
    @TypeEncoding("v@:@")
    public void setNextButton(UIButton button) {
    	this.nextButton = button;
    }
    @Property
    @TypeEncoding("v@:@")
    public void setTheoryButton(UIButton button) {
    	this.theoryButton = button;
    }
    @Property
    @TypeEncoding("v@:@")
    public void setSolveButton(UIButton button) {
    	this.solveButton = button;
    }
    @Property
    @TypeEncoding("v@:@")
    public void setTheoryTextField(UITextField textField) {
    	this.theoryTextField = textField;
    }
    @Property
    @TypeEncoding("v@:@")
    public void setTheoryTextView(UITextView textView) {
    	this.theoryTextView = textView;
    }
    @Property
    @TypeEncoding("v@:@")
    public void setLabel(UILabel label) {
    	this.label = label;
    }
    @Property
    @TypeEncoding("v@:@")
    public void setSolutionLabel(UILabel label) {
    	this.solutionLabel = label;
    }


}
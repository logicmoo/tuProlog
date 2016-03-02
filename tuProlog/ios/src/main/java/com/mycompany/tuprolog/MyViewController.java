package com.mycompany.tuprolog;

/**
 * Created by Vito Colletta on 02/12/15.
 * Copyright (c) 2015 Vito Colletta. All rights reserved.
 */

import org.robovm.apple.uikit.*;
import org.robovm.objc.annotation.*;


@CustomClass("MyViewController")
public class MyViewController extends UIViewController {
    private static TuPrologCore core=TuPrologCore.getInstance();
    private String result = "";
    private UITextView theoryTextView;



    @IBOutlet
    private UIButton solveButton;
    @IBOutlet
    private UIButton theoryButton;
    @IBOutlet
    private UIButton nextButton;
    @IBOutlet
    private UITextField goalTextField;
    @IBOutlet
    private UITextView warningsTextView;
    @IBOutlet
    private UITextView solutionTextView;

    @IBOutlet
    private void setTheoryTextView(UITextView textView){
        theoryTextView=textView;
        theoryTextView.setTextColor(UIColor.lightGray());
        theoryTextView.setText("Insert a theory");
        theoryTextView.getLayer().setCornerRadius(5.0);
        theoryTextView.getLayer().setBorderWidth(1.0);
        theoryTextView.setDelegate(new TextViewDelegate(this));
    }




    @IBAction
    private void solve() {
        hideKeyboard();
        result = "";
        warningsTextView.setText("");
        if (!goalTextField.getText().equals(""))
        {
            try
            {
                result += "Solving...";
                warningsTextView.setText("");
                try {
                    result=core.solveGoal(goalTextField.getText());
                    nextButton.setEnabled(core.getNextSolutionEnable());

                } catch (IllegalArgumentException ex) {
                    warningsTextView.setText("Syntax Error: malformed goal.");
                }

            } catch (Exception e) {
                warningsTextView.setText("Error: " + e);
            }
        }
        else
            result += "Ready.";
        solutionTextView.setText(result);
    }

    @IBAction
    private void hideKeyboard() {
       if (theoryTextView.isFirstResponder())
            theoryTextView.resignFirstResponder();
        else if (goalTextField.isFirstResponder())
            goalTextField.resignFirstResponder();
    }


    @IBAction
    private void getNextSolution() {
        try {
            result=core.getNextSolution();
            nextButton.setEnabled(core.getNextSolutionEnable());
        }catch (IllegalArgumentException ex) {
            result += ex.getMessage();
        }
        solutionTextView.setText(result);
    }


    @IBAction
    private void setTheory() {
        hideKeyboard();
        String theory=null;
        theory=theoryTextView.getText();
        try {
            if (theory != null && !theory.equals("")) {
                core.setTheory(theory);
                solutionTextView.setText("Theory set!");
                warningsTextView.setText("");
            } else
                warningsTextView.setText("WARNING: Theory is empty");
        }catch (IllegalArgumentException e) {
            warningsTextView.setText("Error setting theory: Syntax Error at/before line " + e.getMessage());
        }
    }


    public void enableTheoryButton(boolean choice) {
        if ((theoryButton.isEnabled() && choice == false) || (!theoryButton.isEnabled() && choice == true))
            theoryButton.setEnabled(choice);
    }



}

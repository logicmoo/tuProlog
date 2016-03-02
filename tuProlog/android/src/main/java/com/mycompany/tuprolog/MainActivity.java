package com.mycompany.tuprolog;

/**
 * Created by Vito Colletta on 07/12/15.
 * Copyright (c) 2015 Vito Colletta. All rights reserved.
 */

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends Activity {

    private String result="";
   private static TuPrologCore core=TuPrologCore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        final EditText theoryTextView = (EditText) findViewById(R.id.theoryTextView);
        final Button theoryButton = (Button) findViewById(R.id.theoryButton);
        final EditText goalTextView= (EditText) findViewById(R.id.goalTextView);
        final Button solveButton = (Button) findViewById(R.id.solveButton);
        final Button nextButton = (Button) findViewById(R.id.nextButton);
        final EditText solutionTextView= (EditText) findViewById(R.id.solutionTextView);
        final EditText warningsTextView= (EditText) findViewById(R.id.warningsTextView);


        theoryTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableTheoryButton(theoryButton,theoryTextView.getText().toString().equals("")? false:true);
            }



            @Override
            public void afterTextChanged(Editable s) {
                enableTheoryButton(theoryButton,theoryTextView.getText().toString().equals("")? false:true);
            }
        });

        solveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                solve(warningsTextView,goalTextView,solutionTextView,nextButton);
            }
        });


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNextSolution(nextButton,solutionTextView);
            }
        });

        theoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTheory(theoryTextView,solutionTextView,warningsTextView);
            }
        });



    }
    private void hideKeyboard(EditText edit){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
    }

    private void solve(EditText warningsTextView, EditText goalTextView,EditText solutionTextView,Button nextButton){
        result = "";
        warningsTextView.setText("");
        hideKeyboard(goalTextView);
        if (!goalTextView.getText().equals(""))
        {
            try
            {
                result += "Solving...";
                warningsTextView.setText("");
                try {
                    result=core.solveGoal(goalTextView.getText().toString());
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

    private void getNextSolution(Button nextButton,EditText solutionTextView){
        try {
            result=core.getNextSolution();
            nextButton.setEnabled(core.getNextSolutionEnable());
        }catch (IllegalArgumentException ex) {
            result += ex.getMessage();
        }
        solutionTextView.setText(result);
    }

    private void setTheory(EditText theoryTextView,EditText solutionTextView,EditText warningsTextView){
        String theory=theoryTextView.getText().toString();
        hideKeyboard(theoryTextView);
        try {
            if (theory != null && !theory.isEmpty()) {
                core.setTheory(theory);
                solutionTextView.setText("Theory set!");
                warningsTextView.setText("");
            } else
                warningsTextView.setText("WARNING: Theory is empty");
        }catch (IllegalArgumentException e) {
            warningsTextView.setText("Error setting theory: Syntax Error at/before line " + e.getMessage());
        }
    }

    public void enableTheoryButton(Button theoryButton,boolean choice) {
        if ((theoryButton.isEnabled() && choice == false) || (!theoryButton.isEnabled() && choice == true))
            theoryButton.setEnabled(choice);
    }



}

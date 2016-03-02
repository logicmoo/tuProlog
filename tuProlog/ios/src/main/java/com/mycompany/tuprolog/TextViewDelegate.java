package com.mycompany.tuprolog;

import org.robovm.apple.uikit.*;

/**
 * Created by Vito Colletta on 04/12/15.
 * Copyright (c) 2015 Vito Colletta. All rights reserved.
 */
public class TextViewDelegate extends UITextViewDelegateAdapter {
    private MyViewController view;
    public TextViewDelegate(MyViewController view){
        this.view=view;
    }

    @Override
    public boolean shouldBeginEditing(UITextView textView){
        if(textView.getTextColor()==UIColor.lightGray()){
            textView.setText("");
            textView.setTextColor(UIColor.black());
        }
        return true;
    }

    public void didChange(UITextView textView){
        view.enableTheoryButton((textView.getText().isEmpty()? false:true));
    }


}

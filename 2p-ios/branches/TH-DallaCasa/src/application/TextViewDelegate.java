package application;

import org.robovm.apple.coregraphics.*;
import org.robovm.apple.foundation.NSRange;
import org.robovm.apple.uikit.*;

public class TextViewDelegate extends UITextViewDelegateAdapter {

	private ViewController viewController = null;
	private CGRect originalFrame = null;
	private CGRect currentFrame = null;
	private boolean staticLayout = false;
	private boolean reduce = false;
	private boolean enlarge = false;
	
	public TextViewDelegate(ViewController viewController) {
		this.viewController = viewController;
	}
	
    @Override
    public boolean shouldBeginEditing(UITextView textView) {
    	if (textView.getTextColor() == UIColor.colorLightGray()) {
    		textView.setText("");
    		textView.setTextColor(UIColor.colorBlack());
    	}
    	
    	if (originalFrame == null) {
    		originalFrame = textView.getFrame();
    		currentFrame = originalFrame;
    	}
    	if (staticLayout) {
    		/**Static Layout**/
    		double offset = 40.0;
    		currentFrame = new CGRect(originalFrame.origin().x(), originalFrame.origin().y(), originalFrame.getWidth(), originalFrame.getHeight()+offset);
    		textView.setFrame(currentFrame);
			viewController.refreshView(offset);
    	} else if (!staticLayout){
    		/**Dynamic Layout**/
    		double offset = 0.0;
    		double originalHeight = originalFrame.getHeight();
    		double contentHeight = textView.getContentSize().height();
    		double maxHeight = originalFrame.getHeight()*3;
    		if (originalHeight < contentHeight) {
	    		if (contentHeight < maxHeight) {
					offset = contentHeight - originalHeight;
					currentFrame = new CGRect(textView.getFrame().origin(), textView.getContentSize());
	    		} else {
	    			offset = maxHeight - originalHeight;
	    			currentFrame = new CGRect(textView.getFrame().origin().x(), textView.getFrame().origin().y(), textView.getFrame().getWidth(), maxHeight);
	    		}
	    		textView.setFrame(currentFrame);
	    		viewController.refreshView(offset);
    		}
    	}
    	return true;
    }
    
    @Override
    public void didEndEditing(UITextView textView) {
    	textView.setFrame(originalFrame);
		viewController.refreshView(originalFrame.getHeight()-currentFrame.getHeight());
    }
    
    
    @Override
    public void didChange(UITextView textView) {
    	boolean choice = (textView.getText().isEmpty()) ? false : true;
    	viewController.enableTheoryButton(choice);
    	
    	double offset = 0.0;
    	if (!staticLayout) {
    		double currentHeight = currentFrame.getHeight();
    		double contentHeight = textView.getContentSize().height();
    		double maxHeight = originalFrame.getHeight()*3.5;
	    	if (reduce) {    		
	    		reduce = false;
				if (contentHeight < maxHeight && contentHeight < currentHeight) {
					offset = contentHeight - currentHeight;
					currentFrame = new CGRect(textView.getFrame().origin(), textView.getContentSize());
					textView.setFrame(currentFrame);
					viewController.refreshView(offset);
				}
	    	} else if (enlarge) {    		
	    		enlarge = false;
				if (currentHeight < contentHeight && contentHeight < maxHeight) {
					if (contentHeight < maxHeight) {
						offset = contentHeight - currentHeight;
						currentFrame = new CGRect(textView.getFrame().origin(), textView.getContentSize());
					} else {
						offset = maxHeight - currentHeight;
						currentFrame = new CGRect(textView.getFrame().origin().x(), textView.getFrame().origin().y(), textView.getFrame().getWidth(), maxHeight);
					}
					textView.setFrame(currentFrame);
					viewController.refreshView(offset);
				}
	    	}
    	}
    }
    
    
    
    @Override
    public boolean shouldChangeCharacters(UITextView textView, NSRange range, String text) {
    	if (!staticLayout) {
	    	if (range.length() > 0)
	    		reduce = true;
	    	else if (!text.isEmpty())
	    		enlarge = true;
    	}
    	return true;
    }
    
}

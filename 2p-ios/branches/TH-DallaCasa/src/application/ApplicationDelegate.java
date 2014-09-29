package application;

import org.robovm.apple.foundation.*;
import org.robovm.apple.uikit.*;

public class ApplicationDelegate extends UIApplicationDelegateAdapter {

    private UIWindow window = null;
    private ViewController viewController = null;
    
    @SuppressWarnings("rawtypes")
    @Override
    public boolean didFinishLaunching(UIApplication application,
            NSDictionary launchOptions) {

        window = new UIWindow(UIScreen.getMainScreen().getBounds());
        viewController = new ViewController(this);
        window.setRootViewController(viewController);
        window.makeKeyAndVisible();
        
        return true;
    }

    public static void main(String[] args) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(args, null, ApplicationDelegate.class);
        pool.close();
    }
}
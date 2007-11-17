/*
 * ClimbMIDlet.java
 *
 * Created on 29. Dezember 2002, 13:18
 */

package com.googlecode.climb;

import javax.microedition.lcdui.*;
import com.googlecode.climb.menu.MainMenu;
import com.googlecode.climb.utils.SplashScreen;


/**
 *
 * @author  Coskun
 * @version
 */
public class Climb2MIDlet extends javax.microedition.midlet.MIDlet
{
    private Display display;
    private boolean hasStarted;
    
    public void startApp()
    {
        if (!this.hasStarted) {
            this.display = Display.getDisplay(this);
            SplashScreen splash = new SplashScreen(this.display);
            MainMenu menu = new MainMenu(this.display,this);
            splash.show(new String[] {"Fat Joe M","presents","CLIMB 2"}, menu);
            this.hasStarted = true;
        }
    }
    
    public void pauseApp()
    {
        System.out.println("testpause");
    }
    
    public void destroyApp(boolean unconditional)
    {
        System.out.println("testkill");
    }
}

/*
 * CreditsScreen.java
 *
 * Created on 1. Januar 2003, 23:20
 */

package com.googlecode.climb.menu;

import javax.microedition.lcdui.*;

/**
 *
 * @author  Coskun
 */
public class CreditsScreen extends Form implements CommandListener
{
    private StringItem credits;
    private MainMenu menu;
    private Command toMain;
    
    /** Creates a new instance of CreditsScreen */
    public CreditsScreen(MainMenu menu)
    {
        super("Climb 2 v1.04");
        this.menu = menu;
        String credit = "Copyright 2005 Fatih Coskun\n\nProgramming and graphics:\nFatih Coskun\nTitlescreen:\nAlexander De Luca\nSpecial Thanks to: Hakan Durmaz\n\n" +
                "This game is under development. Changes in future versions are very likely\n\n" +
        "Homepage: http://www.cip.ifi.lmu.de/~coskun/project/climb2/\n\n" +
        "E-Mail: fatih.coskun@stud.ifi.lmu.de\n\nVisit www.tholex.de for more outstanding cool mobile stuff.";
        credits = new StringItem(null,credit);
        this.setCommandListener(this);
        this.toMain = new Command("Back", Command.SCREEN, 3);
        this.addCommand(toMain);
        this.append(credits);
    }
    
    public void commandAction(Command comm, Displayable disp)
    {
        if (comm == toMain)
            menu.show(menu);
    }
}
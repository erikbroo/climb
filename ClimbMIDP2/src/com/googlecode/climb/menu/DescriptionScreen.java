/*
 * DescriptionScreen.java
 *
 * Created on 1. Januar 2003, 23:08
 */

package com.googlecode.climb.menu;

import javax.microedition.lcdui.*;

/**
 *
 * @author  Coskun
 */
public class DescriptionScreen extends Form implements CommandListener
{
    private StringItem ablauf;
    private StringItem ziel;
    private StringItem punkte;
    private StringItem steuerung;
    private StringItem tips;
    private StringItem wasser;
    
    private MainMenu menu;
    private Command toMain;
    
    /** Creates a new instance of DescriptionScreen */
    public DescriptionScreen(MainMenu menu)
    {
        super("Climb 2 Help");
        this.menu = menu;
        this.initDescription();
        this.setCommandListener(this);
        this.toMain = new Command("Back", Command.SCREEN, 3);
        this.addCommand(toMain);
        this.append(ablauf);
        this.append(ziel);
        this.append(punkte);
        this.append(steuerung);
        this.append(wasser);
    }
    
    private void initDescription()
    {
        String ablaufString =
            "Spot got trapped in a deep well. Only brave and outstanding jump " +
            "acrobatics can help him escape. But be careful: the water is rising " +
            "and when it reaches spot, he will be pulled down to the bottom of " +
            "the well causing his death.\n";
        ablauf = new StringItem("General:", ablaufString);
        
        String zielString =
            "You could think of the goal to reach the upper end of the well (what " +
            "is at platform 1000), but you will hardly ever make it that far. " +
            "Learn how to make points, you can make many points without even get " +
            "to the high platforms.\n";
        ziel = new StringItem("Goal:", zielString);
        
        String punkteVerteilung =
            "You will get points for every platform you climb. Also every reached " +
            "100th platform will give you time-bonus points. The best way to make " +
            "points however is to make the cool acrobatic combo jumps. These jumps will " +
            "fill your combobar and add to your combo-multiplier, which in turn " +
            "earns you the very big bonus points.\nSee Climb 2 Homepage for more " +
            "details on the combo mechanism.";
        punkte = new StringItem("Points:", punkteVerteilung);
        
        String steuer =
            "You only need three buttons for left, right and jump to control " +
            "the movement of spot. You can change key settings in the options" +
            " screen.\n";
        steuerung = new StringItem("Controls:",steuer);
        
        
        String wass =
            "The water is your opponent. The longer you " +
            "need to reach an 100th platform the faster the water will rise. It will " +
            "also rise faster if your combo-multiplier is very high. This makes " +
            "the game a bit harder for very good players.";
        wasser = new StringItem("Water:",wass);
    }
    
    public void commandAction(Command comm, Displayable disp)
    {
        if (comm == toMain)
            menu.show(menu);
    }
}
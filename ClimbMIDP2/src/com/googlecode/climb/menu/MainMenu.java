/*
 * IntroScreen.java
 *
 * Created on 29. Dezember 2002, 13:36
 */

package com.googlecode.climb.menu;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import com.googlecode.climb.Climb2MIDlet;
import com.googlecode.climb.game.GameControl;
import java.io.IOException;

/**
 *
 * @author  Coskun
 */
public class MainMenu extends Form implements CommandListener
{
    private static Image titleImage;
    static {
        
        try {
            titleImage = Image.createImage("/title.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private Display display;

    private Command toDescription;
    private Command toCredits;
    private Command toOptions;
    private Command toHighscore;
    private Command startGame;

    private ImageItem imageItem;

    private DescriptionScreen descriptionsScreen;
    public ScoreScreen scoreScreen;
    public CreditsScreen credits;
    public OptionsScreen options;

    public MainMenu(Display display, Climb2MIDlet midlet)
    {
        super("Climb 2");
        this.display = display;
        this.initContents();
        
        this.setCommandListener(this);
        this.toDescription = new Command("Help", Command.SCREEN, 5);
        this.toCredits = new Command("Credits", Command.SCREEN, 6);
        this.toOptions = new Command("Settings", Command.SCREEN, 3);
        this.toHighscore = new Command("Highscore", Command.SCREEN, 4);
        this.startGame = new Command("New Game", Command.SCREEN, 1);
        this.addCommand(toDescription); 
        this.addCommand(toCredits);
        //this.addCommand(toOptions);
        this.addCommand(toHighscore);
        this.addCommand(startGame);
        this.addCommand(toOptions);
        
        this.credits = new CreditsScreen(this);
        this.scoreScreen = new ScoreScreen(this);
        this.options = new OptionsScreen(this);
    }
    
    public void show(Displayable disp)
    {
        display.setCurrent(disp);
    }
    
    public void show(Alert alert, Displayable disp)
    {
        display.setCurrent(alert, disp);
    }
    
    public void commandAction(Command comm, Displayable disp)
    {
//        else if (comm == toCredits)
//            display.setCurrent(this.credsScreen);
        if (comm == this.startGame) {
            System.gc();
            try { Thread.sleep(100); } catch (Exception e) { }
            System.gc();
            try { Thread.sleep(100); } catch (Exception e) { }
            
            GameControl game = new GameControl(this,this.display);
            game.setKeys(options.keyJump.getSelectedIndex(),options.keyLeft.getSelectedIndex(),options.keyRight.getSelectedIndex());
            game.startGame();
        }
        else if (comm == this.toDescription) {
            if (this.descriptionsScreen == null) {
                this.descriptionsScreen = new DescriptionScreen(this);
            }
            this.display.setCurrent(this.descriptionsScreen);
        }
        else if (comm == this.toHighscore) {
            this.scoreScreen.initScreen();
            this.display.setCurrent(this.scoreScreen);
        }
        else if (comm == this.toCredits) {
            this.display.setCurrent(this.credits);
        }
        else if (comm == toOptions)
            display.setCurrent(this.options);
    }
    
    public void initContents()
    {
        this.append(new ImageItem(null,titleImage,Item.LAYOUT_DEFAULT,null));
//        this.descScreen = new DescriptionScreen(this);
//        this.credsScreen = new CreditsScreen(this);
//        this.resumeScreen = new ResumeScreen(this, new ImageItem(null,image,ImageItem.LAYOUT_DEFAULT,null));
//        this.scoreScreen = new ScoreScreen(this);
        //this.optionsScreen = new OptionsScreen(this);
    }
}
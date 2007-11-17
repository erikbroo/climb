/*
 * GameControl.java
 * Created on 26.12.2004, &{time}
 */
package com.googlecode.climb.game;


import java.util.Random;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import com.googlecode.climb.menu.MainMenu;


/**
 * @author Fatih Coskun
 *
 */
public final class GameControl extends GameCanvas
{
    static final int NUM_0_PRESSED = 1;
    static final int NUM_1_PRESSED = 2;
    static final int NUM_2_PRESSED = 4;
    static final int NUM_3_PRESSED = 8;
    static final int NUM_4_PRESSED = 16;
    static final int NUM_5_PRESSED = 32;
    static final int NUM_6_PRESSED = 64;
    static final int NUM_7_PRESSED = 128;
    static final int NUM_8_PRESSED = 256;
    static final int NUM_9_PRESSED = 512;
    static final int FIRE_PRESSED = 1024;
    static final int UP_PRESSED = 2048;
    static final int LEFT_PRESSED = 2048*2;
    static final int RIGHT_PRESSED = 2048*4;
    static final int DOWN_PRESSED = 2048*8;
    static final int STAR_PRESSED = 2048*16;
    static final int POUND_PRESSED = 2048*32;
    
    int pressedKeys = 0;
    int keyJump = UP_PRESSED;
    int keyLeft = LEFT_PRESSED;
    int keyRight = RIGHT_PRESSED;
    
    
    private static final int MAINLOOP_SLEEPTIME = 50;
    static final Random random = new Random();
    
    
    private final MainMenu menu;
    private final Display display;
    
    final World world;
    final ScorePanel scorePanel;
    final ComboMeter comboMeter;
    final MessagePopup messagePopup;
    
    short highestTouchedPlatform;
    private short lastTouchedPlatform;
    private int currentCent;
    private boolean paused;
    private boolean gameover;
    private int totalScore;
    
    private boolean displayableSizeChanged;

    public GameControl(MainMenu menu, Display display)
    {
        super(false);
        super.setFullScreenMode(true);
        this.menu = menu;
        this.display = display;
        this.scorePanel = new ScorePanel(this);
        this.world = new World(this);
        this.messagePopup = new MessagePopup(this);
        this.comboMeter = new ComboMeter(this.messagePopup, this.world.water, this.scorePanel);
    }
    
    public final void keyPressed(int key) {
        switch (key) {
            case Canvas.KEY_STAR :
                pressedKeys |= GameControl.STAR_PRESSED;
                break;
            case Canvas.KEY_POUND :
                pressedKeys |= GameControl.POUND_PRESSED;
                break;
            case Canvas.KEY_NUM0 :
                pressedKeys |= GameControl.NUM_0_PRESSED;
                break;
            case Canvas.KEY_NUM1 :
                pressedKeys |= GameControl.NUM_1_PRESSED;
                break;
            case Canvas.KEY_NUM2 :
                pressedKeys |= GameControl.NUM_2_PRESSED;
                break;
            case Canvas.KEY_NUM3 :
                pressedKeys |= GameControl.NUM_3_PRESSED;
                break;
            case Canvas.KEY_NUM4 :
                pressedKeys |= GameControl.NUM_4_PRESSED;
                break;
            case Canvas.KEY_NUM5 :
                pressedKeys |= GameControl.NUM_5_PRESSED;
                break;
            case Canvas.KEY_NUM6 :
                pressedKeys |= GameControl.NUM_6_PRESSED;
                break;
            case Canvas.KEY_NUM7 :
                pressedKeys |= GameControl.NUM_7_PRESSED;
                break;
            case Canvas.KEY_NUM8 :
                pressedKeys |= GameControl.NUM_8_PRESSED;
                break;
            case Canvas.KEY_NUM9 :
                pressedKeys |= GameControl.NUM_9_PRESSED;
                break;
            default :
                switch (getGameAction(key)) {
                    case Canvas.DOWN :
                        System.out.println("down");
                        pressedKeys |= GameControl.DOWN_PRESSED;
                        break;
                    case Canvas.UP :
                        pressedKeys |= GameControl.UP_PRESSED;
                        break;
                    case Canvas.LEFT :
                        pressedKeys |= GameControl.LEFT_PRESSED;
                        break;
                    case Canvas.RIGHT :
                        pressedKeys |= GameControl.RIGHT_PRESSED;
                        break;
                    case Canvas.FIRE :
                        pressedKeys |= GameControl.FIRE_PRESSED;
                        break;
                }
        }
    }
    
    public final void keyReleased(int key) {
        switch (key) {
            case Canvas.KEY_STAR :
                pressedKeys &= ~GameControl.STAR_PRESSED;
                break;
            case Canvas.KEY_POUND :
                pressedKeys &= ~GameControl.POUND_PRESSED;
                break;
            case Canvas.KEY_NUM0 :
                pressedKeys &= ~GameControl.NUM_0_PRESSED;
                break;
            case Canvas.KEY_NUM1 :
                pressedKeys &= ~GameControl.NUM_1_PRESSED;
                break;
            case Canvas.KEY_NUM2 :
                pressedKeys &= ~GameControl.NUM_2_PRESSED;
                break;
            case Canvas.KEY_NUM3 :
                pressedKeys &= ~GameControl.NUM_3_PRESSED;
                break;
            case Canvas.KEY_NUM4 :
                pressedKeys &= ~GameControl.NUM_4_PRESSED;
                break;
            case Canvas.KEY_NUM5 :
                pressedKeys &= ~GameControl.NUM_5_PRESSED;
                break;
            case Canvas.KEY_NUM6 :
                pressedKeys &= ~GameControl.NUM_6_PRESSED;
                break;
            case Canvas.KEY_NUM7 :
                pressedKeys &= ~GameControl.NUM_7_PRESSED;
                break;
            case Canvas.KEY_NUM8 :
                pressedKeys &= ~GameControl.NUM_8_PRESSED;
                break;
            case Canvas.KEY_NUM9 :
                pressedKeys &= ~GameControl.NUM_9_PRESSED;
                break;
            default :
                switch (getGameAction(key)) {
                    case Canvas.DOWN :
                        pressedKeys &= ~GameControl.DOWN_PRESSED;
                        break;
                    case Canvas.UP :
                        pressedKeys &= ~GameControl.UP_PRESSED;
                        break;
                    case Canvas.LEFT :
                        pressedKeys &= ~GameControl.LEFT_PRESSED;
                        break;
                    case Canvas.RIGHT :
                        pressedKeys &= ~GameControl.RIGHT_PRESSED;
                        break;
                    case Canvas.FIRE :
                        pressedKeys &= ~GameControl.FIRE_PRESSED;
                        break;
                }
        }
    }

    public final void startGame()
    {
        this.display.setCurrent(this);
        this.world.renderTo(getGraphics());
        this.messagePopup.registerMSG("Go Go Go", 0x22FF33);
        this.scorePanel.start();
        this.comboMeter.initTick();
        mainLoop();
    }

    public final void resumeGame()
    {
    }
    
    public final void sizeChanged(int x, int y)
    {
        this.displayableSizeChanged = true;
    }

    private final void mainLoop()
    {
        new Thread() {

            public void run()
            {
                Graphics g = getGraphics();
                g.setColor(0);
                g.fillRect(0, 0, GameControl.this.getWidth(), GameControl.this.getHeight());
                if (    g.getClipWidth() > world.WORLD_VIEW_WIDTH && 
                        g.getClipHeight() > world.WORLD_VIEW_HEIGHT) {
                    g.translate(
                        (g.getClipWidth() - world.WORLD_VIEW_WIDTH) / 2, 
                        (g.getClipHeight() - world.WORLD_VIEW_HEIGHT) / 2);
                }
                g.setClip(0,0,world.WORLD_VIEW_WIDTH,world.WORLD_VIEW_HEIGHT);
                
                while (true) {
                    if (GameControl.this.displayableSizeChanged) {
                        g = getGraphics();
                        g.setColor(0);
                        g.fillRect(0, 0, GameControl.this.getWidth(), GameControl.this.getHeight());
                        if (    g.getClipWidth() > world.WORLD_VIEW_WIDTH && 
                                g.getClipHeight() > world.WORLD_VIEW_HEIGHT) {
                            g.translate(
                                (g.getClipWidth() - world.WORLD_VIEW_WIDTH) / 2, 
                                (g.getClipHeight() - world.WORLD_VIEW_HEIGHT) / 2);
                        }
                        g.setClip(0, 0, world.WORLD_VIEW_WIDTH, world.WORLD_VIEW_HEIGHT);
                        GameControl.this.displayableSizeChanged = false;
                    }
                    
                    long start = System.currentTimeMillis();
                    
                    if (GameControl.this.paused) {
                        if (GameControl.this.gameover) {
                            GameControl.this.messagePopup.registerMSG("Game Over", 0xFF5522);
                            GameControl.this.gameoverSequence();
                            GameControl.this.totalScore = GameControl.this.scorePanel.getTotalScore(); 
                            break;
                        }
                        try { 
                            Thread.sleep(MAINLOOP_SLEEPTIME);
                        } catch (InterruptedException e) { }
                        continue;
                    }
                    GameControl.this.world.renderTo(g);
                    GameControl.this.scorePanel.renderTo(g);
                    GameControl.this.comboMeter.renderTo(g);
                    GameControl.this.messagePopup.renderTo(g);
                    
                    flushGraphics();
                    
                    long tick = System.currentTimeMillis();
                    GameControl.this.world.tick(tick);
                    GameControl.this.scorePanel.tick(tick);
                    GameControl.this.comboMeter.tick(tick);
                    
                    int spotY = GameControl.this.world.calculateViewPosition(
                            GameControl.this.world.spot.getYPosition());
                    int waterY = GameControl.this.world.calculateViewPosition(
                            GameControl.this.world.water.getHeight());
                    if (spotY > world.WORLD_VIEW_HEIGHT + 20 ||
                        spotY > waterY + 40    ) {
                        GameControl.this.paused = true;
                        GameControl.this.gameover = true;
                    }
                    
                    
                    while( System.currentTimeMillis() - start < GameControl.MAINLOOP_SLEEPTIME ) {
                        Thread.yield();
                    }
                }
                
                if (!GameControl.this.menu.scoreScreen.check4newHighScore(GameControl.this.totalScore, GameControl.this.highestTouchedPlatform)) {
                    GameControl.this.menu.show(GameControl.this.menu);
                }
            }
        }.start();
    }
    
    final void gameoverSequence()
    {
        Graphics g = getGraphics();
        if (    g.getClipWidth() > world.WORLD_VIEW_WIDTH && 
                g.getClipHeight() > world.WORLD_VIEW_HEIGHT) {
            g.translate(
                (g.getClipWidth() - world.WORLD_VIEW_WIDTH) / 2, 
                (g.getClipHeight() - world.WORLD_VIEW_HEIGHT) / 2);
        }
        g.setClip(0, 0, world.WORLD_VIEW_WIDTH, world.WORLD_VIEW_HEIGHT);
        
        int i = 0;
        while (true) {
            long start = System.currentTimeMillis();
            
            int waterY = this.world.calculateViewPosition(this.world.water.getHeight());
            if (waterY < 0) {
                break;
            }
            this.world.water.rise(10);
            
                this.world.spot.setYPosition(this.world.spot.getYPosition() + 1);
            
            this.world.renderTo(g);
            this.scorePanel.renderTo(g);
            this.comboMeter.renderTo(g);
            this.messagePopup.renderTo(g);
            flushGraphics();
            i++;
            while( System.currentTimeMillis() - start < GameControl.MAINLOOP_SLEEPTIME ) {
                Thread.yield();
            }
        }
    }

    final void spotJumped()
    {
        this.comboMeter.jumpedFrom(this.lastTouchedPlatform);
    }

    final void spotLanded(short platform)
    {
        this.highestTouchedPlatform = (short)Math.max(highestTouchedPlatform, platform);
        this.lastTouchedPlatform = platform;
        
        this.comboMeter.landedOn(platform);
        
        if ((platform % 100) < 5) {
            if ((platform / 100) > this.currentCent) {
                this.world.water.setWaterSpeedLevel(0);
                this.world.water.setWaterSpeedMultiplikator(0);
                this.currentCent++;
                this.scorePanel.resetTimer();
            }
        }
    }
    
    final void spotCollidedWall()
    {
        this.comboMeter.collided();
    }
        
    public final void hideNotify()
    {
        this.paused = true;
        if (this.scorePanel != null)
            this.scorePanel.pause();
    }
    
    public final void showNotify()
    {
        if (this.paused) {
            this.paused = false;
            this.scorePanel.resume();
            this.comboMeter.resume();
        }
    }

    public final void setKeys(int keyJump, int keyLeft, int keyRight)
    {
        switch (keyJump) {
            case 0 :
                this.keyJump = NUM_0_PRESSED;
                break;
            case 1 :
                this.keyJump = NUM_1_PRESSED;
                break;
            case 2 :
                this.keyJump = NUM_2_PRESSED;
                break;
            case 3 :
                this.keyJump = NUM_3_PRESSED;
                break;
            case 4 :
                this.keyJump = NUM_4_PRESSED;
                break;
            case 5 :
                this.keyJump = NUM_5_PRESSED;
                break;
            case 6 :
                this.keyJump = NUM_6_PRESSED;
                break;
            case 7 :
                this.keyJump = NUM_7_PRESSED;
                break;
            case 8 :
                this.keyJump = NUM_8_PRESSED;
                break;
            case 9 :
                this.keyJump = NUM_9_PRESSED;
                break;
            case 10 :
                this.keyJump = STAR_PRESSED;
                break;
            case 11 :
                this.keyJump = POUND_PRESSED;
                break;
            case 12 :
                this.keyJump = UP_PRESSED;
                break;
            case 13 :
                this.keyJump = DOWN_PRESSED;
                break;
            case 14 :
                this.keyJump = LEFT_PRESSED;
                break;
            case 15 :
                this.keyJump = RIGHT_PRESSED;
                break;
            case 16 :
                this.keyJump = FIRE_PRESSED;
        }
        switch (keyLeft) {
            case 0 :
                this.keyLeft = NUM_0_PRESSED;
                break;
            case 1 :
                this.keyLeft = NUM_1_PRESSED;
                break;
            case 2 :
                this.keyLeft = NUM_2_PRESSED;
                break;
            case 3 :
                this.keyLeft = NUM_3_PRESSED;
                break;
            case 4 :
                this.keyLeft = NUM_4_PRESSED;
                break;
            case 5 :
                this.keyLeft = NUM_5_PRESSED;
                break;
            case 6 :
                this.keyLeft = NUM_6_PRESSED;
                break;
            case 7 :
                this.keyLeft = NUM_7_PRESSED;
                break;
            case 8 :
                this.keyLeft = NUM_8_PRESSED;
                break;
            case 9 :
                this.keyLeft = NUM_9_PRESSED;
                break;
            case 10 :
                this.keyLeft = STAR_PRESSED;
                break;
            case 11 :
                this.keyLeft = POUND_PRESSED;
                break;
            case 12 :
                this.keyLeft = UP_PRESSED;
                break;
            case 13 :
                this.keyLeft = DOWN_PRESSED;
                break;
            case 14 :
                this.keyLeft = LEFT_PRESSED;
                break;
            case 15 :
                this.keyLeft = RIGHT_PRESSED;
                break;
            case 16 :
                this.keyLeft = FIRE_PRESSED;
        }
        switch (keyRight) {
            case 0 :
                this.keyRight = NUM_0_PRESSED;
                break;
            case 1 :
                this.keyRight = NUM_1_PRESSED;
                break;
            case 2 :
                this.keyRight = NUM_2_PRESSED;
                break;
            case 3 :
                this.keyRight = NUM_3_PRESSED;
                break;
            case 4 :
                this.keyRight = NUM_4_PRESSED;
                break;
            case 5 :
                this.keyRight = NUM_5_PRESSED;
                break;
            case 6 :
                this.keyRight = NUM_6_PRESSED;
                break;
            case 7 :
                this.keyRight = NUM_7_PRESSED;
                break;
            case 8 :
                this.keyRight = NUM_8_PRESSED;
                break;
            case 9 :
                this.keyRight = NUM_9_PRESSED;
                break;
            case 10 :
                this.keyRight = STAR_PRESSED;
                break;
            case 11 :
                this.keyRight = POUND_PRESSED;
                break;
            case 12 :
                this.keyRight = UP_PRESSED;
                break;
            case 13 :
                this.keyRight = DOWN_PRESSED;
                break;
            case 14 :
                this.keyRight = LEFT_PRESSED;
                break;
            case 15 :
                this.keyRight = RIGHT_PRESSED;
                break;
            case 16 :
                this.keyRight = FIRE_PRESSED;
        }
    }
}
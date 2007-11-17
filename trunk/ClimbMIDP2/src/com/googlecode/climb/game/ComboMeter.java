/*
 * ComboMeter.java
 *
 * Created on 3. Dezember 2005, 04:17
 */
package com.googlecode.climb.game;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Font;

/**
 *
 * @author Fatih Coskun
 */
final class ComboMeter 
{
    //Combo Constants:
    private final static byte NO_EVENT = 0;
    private final static byte EAGLE = 1;
    private final static byte SHARKY = 2;
    private final static byte FOXY = 3;
    private final static byte WALLY = 4;
    private final static byte WOODY = 5;
    private final static byte DOGGY = 6;
    private final static byte CATTY = 7;
    
    //Event Constants:
    private final static byte NO_ENTRY = 0;
    private final static byte JUMPED = 1;
    private final static byte LANDED = 10;
    private final static byte COLLIDED = 100;
    
    /*
     * Some circular lists for events and platforms:
     */
    private final byte[] event;
    private final short[] onPlatform;
    private int cycleIndex;
    
    private byte sharkyComboCount;
    private byte eagleComboCount;
    private byte lastCombo;
    private short lastPlatform;
    
    private int comboMultiplicator;
    private int comboBarLength;
    private int multiBlinkNTimes = 0;
    private int multiBlinkWith = 0;
    
    private final MessagePopup popup;
    private final Water water;
    private final ScorePanel score;
    
    private long tick;
    
    ComboMeter(MessagePopup popup, Water water, ScorePanel score)
    {
        this.popup = popup;
        this.water = water;
        this.score = score;
        event = new byte[10];
        onPlatform = new short[event.length];
    }
    
    private final void addEvent(byte eventType, short platform)
    {
        event[cycleIndex] = eventType;
        onPlatform[cycleIndex] = platform;
        
        cycleIndex = (cycleIndex + 1) % event.length;
    }
    
    private final byte getEvent(int index)
    {
        final int i = (cycleIndex + index) % event.length;
        
        return event[i];
    }
    
    private final short getPlatform(int index)
    {
        final int i = (cycleIndex + index) % event.length;
        
        return onPlatform[i];
    }
    
    final void jumpedFrom(short platform)
    {
        this.fillComboBar();
        
        addEvent(JUMPED, platform);
    }

    final void landedOn(short platform)
    {
        addEvent(LANDED, platform);
        
        this.comboBarLength = Math.max(this.comboBarLength * 2 / 3,1);
        checkForCombo();
    }
    
    final void collided()
    {
        addEvent(COLLIDED, NO_ENTRY);
    }
    
    /**
     * Checks whether one of the following combos have been performed.
     *
     * Eagle:
     * Springen(x-2) [Abprallen] Landen(x)
     *
     * Sharky:
     * Springen(x-3) [Abprallen] Landen(x)
     *
     * Foxy:
     * Springen(x-2) Abprallen Landen(x-2) [Abprallen] Springen(x-2) [Abprallen] Landen(x) 
     *
     * Wally:
     * Springen(x-3) Abprallen Landen(x-3) [Abprallen] Springen(x-3) [Abprallen] Landen(x)
     *
     * Woody:
     * {Springen(x-1) | Landen(x-1)} [Abprallen] Landen(x-2) [Abprallen] Springen(x-2) [Abprallen] Landen(x)
     *
     * Doggy:
     * {Springen(x-1) | Landen(x-1)} [Abprallen] Landen(x-2) [Abprallen] Springen(x-3) [Abprallen] Landen(x)
     *
     * Catty:
     * {Springen(x+3) | Landen(x+3)} [Abprallen] Landen(x)
     */
    private void checkForCombo()
    {  
        if (this.comboBarLength <= 0) {
            return;
        }
        
        //Constants:
        final byte NO_COMBO = 0;
        final byte MAYBE_EAGLE = 1;
        final byte MAYBE_SHARKY = 2;    

        int L_1 = 0;
        int J_1 = 0;
        int L_2 = 0;
        int J_2 = 0;
        int LJ_3 = 0;
        int continueOn = 9;
        byte nextEvent = getEvent(continueOn);
        
        if (nextEvent != LANDED) {
            return;
        }
        
        L_1 = getPlatform(continueOn);
        //Events: ... Landed(L_1)
            
        continueOn -= 1;
        nextEvent = getEvent(continueOn);
        
        if (nextEvent == COLLIDED) {
            continueOn -= 1;
            nextEvent = getEvent(continueOn);
        }
        
        if (nextEvent == LANDED) {
            
            L_2 = getPlatform(continueOn);
            //Events: ... Landed(L_2) Landed(L_1)
            
            if (L_2 == L_1 + 3) {
                
                cattyComboPerformed();
                return;

            } else {
                return;
            }
            
        }
                
        if (nextEvent != JUMPED) {
            return;
        }
            
        J_1 = getPlatform(continueOn);
        //Events: ... Jumped(J_1) Landed(L_1)
            
        byte maybeCombo = NO_COMBO;
        if (J_1 == L_1 + 3) {
                
            cattyComboPerformed();
            return;
                
        } else if (J_1 == L_1 - 2) {
                
            maybeCombo = MAYBE_EAGLE;
            
        } else if (J_1 == L_1 - 3) {
            
            maybeCombo = MAYBE_SHARKY;
            
        } else {
            return;
        }
            
            
        continueOn -= 1;
        nextEvent = getEvent(continueOn);
            
        if (nextEvent == COLLIDED) {
            continueOn -= 1;
            nextEvent = getEvent(continueOn);
        }
            
        if (nextEvent != LANDED) {
            
            if (maybeCombo == MAYBE_EAGLE) {
                
                eagleComboPerformed();
                return;
                
            } else if (maybeCombo == MAYBE_SHARKY) {
             
                sharkyComboPerformed();
                return;
                
            }
            
            return;
        }
        
        L_2 = getPlatform(continueOn);
        //Events: ... Landed(L_2) Jumped(J_1) Landed(L_1)
            
        continueOn -= 1;
        nextEvent = getEvent(continueOn);
            
        if (nextEvent == COLLIDED) {
                
            //Events: ... COLLIDED Landed(L_2) Jumped(J_1) Landed(L_1)
                
            if (getEvent(continueOn - 1) == JUMPED) {
                    
                J_2 = getPlatform(continueOn - 1);
                //Events: JUMPED(J_2) COLLIDED Landed(L_2) Jumped(J_1) Landed(L_1)
                    
                if (J_2 == L_2 && L_2 == J_1) {
                    if (maybeCombo == MAYBE_EAGLE) {
                            
                        foxyComboPerformed();
                        return;
                            
                    } else if (maybeCombo == MAYBE_SHARKY) {
                        
                        wallyComboPerformed();
                        return;
                            
                    }
                }
            }
                
            continueOn -= 1;
            nextEvent = getEvent(continueOn);
        }
            
        //Events: ... Landed(L_2) Jumped(J_1) Landed(L_1)
        if (nextEvent == JUMPED || nextEvent == LANDED) {
            
            LJ_3 = getPlatform(continueOn);
            //Events: {Landed(LJ_3)|Jumped(LJ_3)} Landed(L_2) Jumped(J_1) Landed(L_1)
            if (LJ_3 == L_2 + 1 && L_2 == J_1) {
                
                if (maybeCombo == MAYBE_EAGLE) {
                
                   woodyComboPerformed();
                   return;
                
                } else if (maybeCombo == MAYBE_SHARKY) {
             
                    doggyComboPerformed();
                    return;
                
                }
                
            }
        }

        
        if (maybeCombo == MAYBE_EAGLE) {
                
            eagleComboPerformed();
            return;
                
        } else if (maybeCombo == MAYBE_SHARKY) {
             
            sharkyComboPerformed();
            return;
                
        }
    }
    
    
    private final void eagleComboPerformed()
    {
        this.comboBarLength = 42;
        
        this.eagleComboCount += 1;
        this.sharkyComboCount = 0;
        
        if (this.lastCombo == EAGLE && lastPlatform == getPlatform(9)) {
            addToMulti(4);
            popup.registerComboString("same eagle");
        }
        else if (eagleComboCount >= 3) {
            eagleComboCount = 0;
            addToMulti(3);
            popup.registerComboString("triple eagle");
        } else {
            addToMulti(1);
            popup.registerComboString("eagle");
        }
        
        this.lastPlatform = getPlatform(9);
        this.lastCombo = EAGLE;
    }
    
    private final void sharkyComboPerformed()
    {
        this.comboBarLength = 42;
        
        this.sharkyComboCount += 1;
        this.eagleComboCount = 0;
        
        if (this.lastCombo == SHARKY && lastPlatform == getPlatform(9)) {
            addToMulti(6);
            popup.registerComboString("same sharky");
        }
        else if (sharkyComboCount >= 2) {
            sharkyComboCount = 0;
            addToMulti(4);
            popup.registerComboString("double sharky");
        } else {
            addToMulti(2);
            popup.registerComboString("sharky");
        }
        
        this.lastPlatform = getPlatform(9);
        this.lastCombo = SHARKY;
    }
    
    private final void wallyComboPerformed()
    {
        this.comboBarLength = 42;
        
        this.sharkyComboCount = 0;
        this.eagleComboCount = 0;
        
        if (this.lastCombo == WALLY && lastPlatform == getPlatform(9)) {
            addToMulti(8);
            popup.registerComboString("same wally");
        } else {
            addToMulti(4);
            popup.registerComboString("wally");
        }
        
        this.lastPlatform = getPlatform(9);
        this.lastCombo = WALLY;
    }
    
    private final void foxyComboPerformed()
    {
        this.comboBarLength = 42;
        
        this.sharkyComboCount = 0;
        this.eagleComboCount = 0;
        
        if (this.lastCombo == FOXY && lastPlatform == getPlatform(9)) {
            addToMulti(6);
            popup.registerComboString("same foxy");
        } else {
            addToMulti(3);
            popup.registerComboString("foxy");
        }
        
        this.lastPlatform = getPlatform(9);
        this.lastCombo = FOXY;
    }
    
    private final void woodyComboPerformed()
    {
        this.comboBarLength = 42;
        
        this.sharkyComboCount = 0;
        this.eagleComboCount = 0;
        
        if (this.lastCombo == WOODY && lastPlatform == getPlatform(9)) {
            addToMulti(12);
            popup.registerComboString("same woody");
        } else {
            addToMulti(6);
            popup.registerComboString("woody");
        }
        
        this.lastPlatform = getPlatform(9);
        this.lastCombo = WOODY;
    }
    
    private final void cattyComboPerformed()
    {
        this.comboBarLength = 42;
        
        this.sharkyComboCount = 0;
        this.eagleComboCount = 0;
        
        if (this.lastCombo == CATTY && lastPlatform == getPlatform(9)) {
            addToMulti(10);
            popup.registerComboString("same catty");
        } else {
            addToMulti(4);
            popup.registerComboString("catty");
        }
        
        this.lastPlatform = getPlatform(9);
        this.lastCombo = CATTY;
    }
    
    private final void doggyComboPerformed()
    {
        this.comboBarLength = 42;
        
        this.sharkyComboCount = 0;
        this.eagleComboCount = 0;
        
        if (this.lastCombo == DOGGY && lastPlatform == getPlatform(9)) {
            addToMulti(16);
            popup.registerComboString("same doggy");
        } else {
            addToMulti(8);
            popup.registerComboString("doggy");
        }
        
        this.lastPlatform = getPlatform(9);
        this.lastCombo = DOGGY;
    }
    
    private final void addToMulti(int i)
    {
        this.comboMultiplicator += i;
        
        if (this.comboMultiplicator <= 20) {
            this.water.setWaterSpeedMultiplikator(0);
        }
        else if (this.comboMultiplicator > 20 && this.comboMultiplicator <= 40) {
            this.water.setWaterSpeedMultiplikator(1);
        }
        else if (this.comboMultiplicator > 40 && this.comboMultiplicator <= 60) {
            this.water.setWaterSpeedMultiplikator(2);
        }
        else if (this.comboMultiplicator > 60) {
            this.water.setWaterSpeedMultiplikator(3);
        }
    }
    
    final void renderTo(Graphics g)
    {
        //Bar:
        g.setColor(0x010101);
        g.fillRoundRect(75,5,40,5,10,10);
        g.setColor(0xD0D0D0);
        g.drawRoundRect(75,5,40,5,10,10);
        
        //Combo-Multi:
        g.setColor(250,180,40);
        int jumps = this.comboMultiplicator;
        g.setFont(Font.getFont(Font.FACE_MONOSPACE,Font.STYLE_PLAIN,Font.SIZE_SMALL));
        if (this.multiBlinkNTimes > 0) {
            if (this.multiBlinkNTimes % 3 == 0) {
                g.setColor(250,40,100);
            }
            jumps = this.multiBlinkWith;
            g.setFont(Font.getFont(Font.FACE_MONOSPACE,Font.STYLE_BOLD,Font.SIZE_SMALL));
        }
        g.drawString("x"+ jumps,89,13,Graphics.TOP|Graphics.LEFT);
        
        g.setColor(250,180,40);
        if (this.comboBarLength > 0) {
            g.fillRoundRect(76,5,this.comboBarLength-2,5,10,10);
            g.setColor(0xD0D0D0);
            g.drawRoundRect(75,5,40,5,10,10);
        }
    }
    
    final void fillComboBar()
    {
        if (this.comboBarLength == 0) {
            this.comboBarLength = 42;
        }
    }
    
    private final void resetComboMulti()
    {
        this.water.setWaterSpeedMultiplikator(0);
        this.comboBarLength = 0;
        int t = 1;

        if (this.comboMultiplicator >= 50) {
            this.popup.registerMSG("PHENOMENAL", 0x10EE99);
            t=50;
        }
        else if (this.comboMultiplicator >= 40) {
            this.popup.registerMSG("SENSATIONAL", 0x10EE99);
            t=40;
        }
        else if (this.comboMultiplicator >= 30) {
            this.popup.registerMSG("AMAZING", 0x10EE99);
            t=30;
        }
        else if (this.comboMultiplicator >= 20) {
            this.popup.registerMSG("Great", 0x10EE99);
            t=20;
        }
        else if (this.comboMultiplicator >= 10) {
            this.popup.registerMSG("NICE", 0x10EE99);
            t=10;
        }
        else if (this.comboMultiplicator >= 5) {
            this.popup.registerMSG("GOOD", 0x10EE99);
            t=5;
        }
        this.score.addToScore(t * this.comboMultiplicator);
        if (t*this.comboMultiplicator <= 1) {
            return;
        }
        this.popup.registerPointAdds(t*this.comboMultiplicator);

        this.multiBlinkNTimes = 16;
        this.multiBlinkWith = this.comboMultiplicator;
        
        this.comboMultiplicator = 0;
    }
    
    final void tick(long t)
    {
        if (t - this.tick > 200) {
            this.tick += 200;
            
            if (this.comboBarLength > 0) {
                this.comboBarLength = Math.max(this.comboBarLength - 2, 0);
                if (this.comboBarLength == 0) {
                    resetComboMulti();
                }
            }
            this.multiBlinkNTimes--;
            this.multiBlinkNTimes = Math.max(0, this.multiBlinkNTimes);
        }
    }
    
    final void initTick()
    {
        this.tick = System.currentTimeMillis();
    }
    
    final void resume()
    {
        this.tick = System.currentTimeMillis();
    }
}
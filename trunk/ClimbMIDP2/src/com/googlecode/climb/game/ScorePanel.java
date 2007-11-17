/*
 * ScorePanel.java
 *
 * Created on 16. Januar 2003, 03:25
 */

package com.googlecode.climb.game;

import java.util.Vector;

import javax.microedition.lcdui.*;

final class ScorePanel
{
    private final static int BOX_WIDTH = World.WORLD_VIEW_WIDTH;
    private final static int BOX_HEIGHT = 25;
    private final static int BOX_XPOS = 0;
    private final static int BOX_YPOS = 0;
    
    private final static int[] ALPHA_BOX;
    
    static {
        ALPHA_BOX= new int[BOX_WIDTH * 1];
        for (int i = 0; i < ALPHA_BOX.length; i++) {
            ALPHA_BOX[i] = 0x99222222;
        }
    }
    
    private final GameControl control;
    private int baseScore;
    private int baseTime = 150;
    
    ScorePanel(GameControl control)
    {
        this.control = control;
    }
    
    final void renderTo(Graphics g)
    {
        int lines = 0;
        while (lines <= BOX_HEIGHT) {
            g.drawRGB(ALPHA_BOX, 0, BOX_WIDTH, 0, lines, BOX_WIDTH, 1, true);
            lines++;
        }
        g.setColor(255,255,255);
        g.drawRect(0,0,175,25);
        
        //score
        g.setColor(0xFEFEFE);
        g.drawRect(19,4,50,16);
        g.setColor(0x404040);
        g.fillRect(19,4,50,16);
        g.setColor(20,140,250);
        g.setFont(Font.getFont(Font.FACE_MONOSPACE,Font.STYLE_BOLD,Font.SIZE_MEDIUM));
        g.drawString(scoreToString(),20,5,Graphics.TOP|Graphics.LEFT);
        
        //countdown
        g.setColor(0xFEFEFE);
        g.drawRect(125,4,30,16);
        g.setColor(0x404040);
        g.fillRect(125,4,30,16);
        g.setColor(250,10,100);
        g.drawString(timerToString(),126,5,Graphics.TOP|Graphics.LEFT);
        
        //buttons
        g.setColor(0x707070);
        g.fillArc(5,2,7,7,0,360);
        g.fillArc(164,2,7,7,0,360);
        g.fillArc(5,15,7,7,0,360);
        g.fillArc(164,15,7,7,0,360);
        g.setColor(0xD0D0D0);
        g.drawArc(5,2,7,7,0,360);
        g.drawArc(164,2,7,7,0,360);
        g.drawArc(5,15,7,7,0,360);
        g.drawArc(164,15,7,7,0,360);
        g.setColor(0xFEFEFE);
        g.fillArc(6,3,3,3,0,360);
        g.fillArc(165,3,3,3,0,360);
        g.fillArc(6,16,3,3,0,360);
        g.fillArc(165,16,3,3,0,360);
    }
    
    final void addToScore(int v)
    {
        this.baseScore += v;
    }
    
    private final String scoreToString()
    {
        int score_ = getTotalScore();
        String Score = "";
        score_ = score_ % 100000;
        
        if (score_ < 10)
            Score = "0000"+ score_;
        else if (score_ < 100)
            Score = "000"+ score_;
        else if (score_ < 1000)
            Score = "00"+ score_;
        else if (score_ < 10000)
            Score = "0"+ score_;
        else
            Score = ""+ score_;
        
        return Score;
    }
    
    final int getTotalScore()
    {
        return this.baseScore + this.control.highestTouchedPlatform * 1;
    }    

    private int timer;
    private long timerTick;

    final void start()
    {
        this.timerTick = System.currentTimeMillis();
        this.timer = this.baseTime;
    }
    
    final void pause()
    {
        //nothing to do
    }
    
    final void resume()
    {
        this.timerTick = System.currentTimeMillis();
    }

    final void tick(long tick)
    {
        if (tick - this.timerTick > 1000) {
            this.timer--;
            this.timerTick += 1000;
            
            if (this.timer == 100) {
                this.control.messagePopup.registerMSG("Hurry Up", 0xFF1111);
                this.control.world.water.setWaterSpeedLevel(1);
            }
            else if (this.timer == 70) {
                this.control.messagePopup.registerMSG("Hurry Up!", 0xFF1111);
                this.control.world.water.setWaterSpeedLevel(2);
            }
            else if (this.timer == 20) {
                this.control.messagePopup.registerMSG("Hurry Up!!", 0xFF1111);
                this.control.world.water.setWaterSpeedLevel(3);
            }
            else if (this.timer == 0) {
                this.control.messagePopup.registerMSG("Hurry Up!!!", 0xFF1111);
                this.control.world.water.setWaterSpeedLevel(4);
            }
        }
    }
    
    final String timerToString()
    {
        if (this.timer < 0) {
            return "000";
        }
        else if (this.timer < 10) {
            return "00"+ this.timer;
        }
        else if (this.timer < 100) {
            return "0"+ this.timer;
        }
        else {
            return ""+ this.timer;
        }
    }
    
    public void resetTimer()
    {
        this.baseTime -= 10;
        if (this.timer > 0) {
            this.baseScore += (this.timer*10);
            this.control.messagePopup.registerMSG("TIME BONUS", 0x202090);
        }
        else {
            this.control.messagePopup.registerMSG("NO TIME BONUS", 0x202090);
        }
        this.timer = this.baseTime;
    }
}
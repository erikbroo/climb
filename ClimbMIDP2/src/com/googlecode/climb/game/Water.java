/*
 * Water.java
 *
 * Created on 10. Januar 2003, 23:19
 */

package com.googlecode.climb.game;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.Sprite;

final class Water
{
    private final static int WATER_WIDTH = World.WORLD_VIEW_WIDTH;
    private final static int WATER_XPOS = 0;
    
    private static Image waterwaveImage;
    private final static int[] ALPHA_WATER;
    static {
        try {
            waterwaveImage = Image.createImage("/waterwaves.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ALPHA_WATER = new int[WATER_WIDTH * 1];
        for (int i = 0; i < ALPHA_WATER.length; i++) {
            ALPHA_WATER[i] = 0x551155FF;
        }
    }
    
    private int water_ypos;
    private final World world;
    private int waterSpeedLevel = 0;
    private int waterSpeedMultiplikator = 0;
    private boolean waterSpeedMultiToggle;
    private final Sprite waveSprite;
    private boolean waveToggle;
    
    Water(World world, int initialAbsolutePosition)
    {
        this.world = world;
        this.water_ypos = initialAbsolutePosition;
        this.waveSprite = new Sprite(waterwaveImage, 176, 5);
    }

    final void tick()
    {
        final int screenY = this.world.calculateViewPosition(this.water_ypos);
        if (screenY > World.WORLD_VIEW_HEIGHT + 15) {
            this.water_ypos = this.world.getViewYPosition() + World.WORLD_VIEW_HEIGHT + 10;
            return;
        }
        
        this.water_ypos -= 1;
        this.water_ypos -= this.waterSpeedLevel;
        this.waterSpeedMultiToggle = !this.waterSpeedMultiToggle;
        if (this.waterSpeedMultiToggle) {
            this.water_ypos -= this.waterSpeedMultiplikator;
        }
    }
    
    final void renderTo(Graphics g)
    {
        int screenY = this.world.calculateViewPosition(this.water_ypos);
        screenY = Math.max(0, screenY);
        
        if (screenY <= 208 ) {
            this.waveSprite.setPosition(0, screenY);
            this.waveSprite.paint(g);
            if (this.waveToggle) {
                this.waveSprite.nextFrame();
            }
            this.waveToggle = !this.waveToggle;
        }
        screenY += 5;
        while (screenY <= 208 ) {
            g.drawRGB(ALPHA_WATER, 0, WATER_WIDTH, 0, screenY, WATER_WIDTH, 1, true);
            screenY++;
        }
    }
    
    final int getHeight()
    {
        return this.water_ypos;
    }
    
    final void setWaterSpeedLevel(int a)
    {
        //System.out.println(" !!!!! alert: "+ a);
        this.waterSpeedLevel = a;
    }
    
    final void setWaterSpeedMultiplikator(int a)
    {
        this.waterSpeedMultiplikator = a;
    }
    
    final void rise(int i)
    {
        this.water_ypos -= i;
    }
}
/*
 * WorldView.java
 * Created on 26.12.2004, &{time}
 */
package com.googlecode.climb.game;

import java.util.Random;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.LayerManager;

/**
 * @author Fatih Coskun
 *
 */
final class World
{
    static final int WORLD_VIEW_HEIGHT = 208;
    static final int WORLD_VIEW_WIDTH = 176;
    
    
    private static final int ROW_HEIGHT = 14;
    private static final int ROW_COUNT = 600;
    
    private static final int PLATFORM_COUNT = 500;
    static final int PLATFORM_DISTANCE = 30;
    
    static final int WORLD_HEIGHT = PLATFORM_COUNT * PLATFORM_DISTANCE;
    
    private final LayerManager backgroundManager;
    private final LayerManager foregroundManagerLeft;
    private final LayerManager foregroundManagerRight;
    
    private int yPosition;
    
    final Water water;
    final Playground playGround;
    final Spot spot;
    
    World(GameControl control)
    {
        this.yPosition = WORLD_HEIGHT - WORLD_VIEW_HEIGHT;
        
        this.backgroundManager = new LayerManager();
        this.foregroundManagerLeft = new LayerManager();
        this.foregroundManagerRight = new LayerManager();
        
        this.water = new Water(this, this.yPosition + WORLD_VIEW_HEIGHT);
        
        this.playGround = new Playground(PLATFORM_COUNT,this);
        this.spot = new Spot(this, control, 50, this.playGround.getYPosition(0));        
        
        this.backgroundManager.append(new Background(ROW_COUNT,GameControl.random));
        this.foregroundManagerLeft.append(new Foreground(ROW_COUNT*2,GameControl.random));
        this.foregroundManagerRight.append(new Foreground(ROW_COUNT*2,GameControl.random));
        
    }
    
    final void renderTo(Graphics g)
    {
        this.backgroundManager.setViewWindow(0,this.yPosition/2 + 785,WORLD_VIEW_WIDTH,WORLD_VIEW_HEIGHT);
        System.out.println("ok");
        System.out.println(this.yPosition/2);
        this.foregroundManagerLeft.setViewWindow(0,this.yPosition*2 + 3700,176,208);
        this.foregroundManagerRight.setViewWindow(0,this.yPosition*2 + 3700,176,208);
        
        this.backgroundManager.paint(g, 0, 0);
        this.playGround.renderTo(g);
        this.spot.renderTo(g);
        this.water.renderTo(g);
        this.foregroundManagerLeft.paint(g, 0, 0);
        this.foregroundManagerRight.paint(g, 161, 0);
    }
    
    final void tick(long tick)
    {
        this.spot.tick();
        this.water.tick();
        
        final int spotScreenY = calculateViewPosition(this.spot.getYPosition());
        if (spotScreenY < 60) {
            viewup(6);
        }
        else if (spotScreenY < 80) {
            viewup(5);
        }
        else if (spotScreenY < 100) {
            viewup(4);
        }
        else if (spotScreenY < 120) {
            viewup(3);
        }
        else if (spotScreenY < 140) {
            viewup(2);
        }
        else if (spotScreenY < 180) {
            viewup(1);
        }
    }

    final void viewup(int i)
    {
        this.yPosition -= i;
    }
    
    final void down()
    {
        this.yPosition += 5;
    }
    
    final int calculateViewPosition(int absoluteY)
    {
        return absoluteY - this.yPosition;
    }
    
    final int getViewYPosition()
    {
        return this.yPosition;
    }
}
/*
 * Playgorund.java
 * Created on 27.12.2004, &{time}
 */
package com.googlecode.climb.game;

import java.io.IOException;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * @author Fatih Coskun
 *
 */
final class Playground
{
    private final static int PLATFORM_DISTANCE = World.PLATFORM_DISTANCE;
    private final static int PLATFORM_HEIGHT = 10;
    private final static int LOWEST_PLATFORM_YPOS = World.WORLD_HEIGHT - 20;
    private final static int VISIBLE_PLATFORM_COUNT = World.WORLD_VIEW_HEIGHT / PLATFORM_DISTANCE + 3;
    private final static byte POSITIONS = 15;
    
    private static Image woodShieldImageBig;
    private static Image woodShieldImageMedium;
    private static Image woodShieldImageSmall;
    
    private static Image platform70Image;
    private static Image platform50Image;
    private static Image platform30Image;
    private static Image platformFullImage;
    
    static {
        try {
            woodShieldImageBig = Image.createImage("/woodshieldBig.png");
            woodShieldImageMedium = Image.createImage("/woodshieldMed.png");
            woodShieldImageSmall = Image.createImage("/woodshieldSmall.png");
            platform70Image = Image.createImage("/platform70.png");
            platform50Image = Image.createImage("/platform50.png");
            platform30Image = Image.createImage("/platform30.png");
            platformFullImage = Image.createImage("/platformFull.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    private final byte[] platforms;
    private final World world;
    
    Playground(int platformCount, World world)
    {
        this.platforms = new byte[platformCount];
        this.world = world;
        
        for (int i = 0; i < this.platforms.length; i++) {
            
            if (i % 100 == 0) {
                
                continue;
            }
            
            int r = Math.abs(GameControl.random.nextInt() % 25);
            switch (r) {
                case 0 :
                    this.platforms[i] = 0;
                    this.platforms[i] = (byte)(this.platforms[i] | 16);
                    break;
                case 1 :
                    this.platforms[i] = 0;
                    this.platforms[i] = (byte)(this.platforms[i] | 32);
                    break;
                case 2 :
                    this.platforms[i] = 0;
                    this.platforms[i] = (byte)(this.platforms[i] | 64);
                    break;
                case 3 :
                    this.platforms[i] = 1;
                    this.platforms[i] = (byte)(this.platforms[i] | 16);
                    break;
                case 4 :
                    this.platforms[i] = 1;
                    this.platforms[i] = (byte)(this.platforms[i] | 32);
                    break;
                case 5 :
                    this.platforms[i] = 1;
                    this.platforms[i] = (byte)(this.platforms[i] | 64);
                    break;
                case 6 :
                    this.platforms[i] = 2;
                    this.platforms[i] = (byte)(this.platforms[i] | 16);
                    break;
                case 7 :
                    this.platforms[i] = 2;
                    this.platforms[i] = (byte)(this.platforms[i] | 32);
                    break;
                case 8 :
                    this.platforms[i] = 2;
                    this.platforms[i] = (byte)(this.platforms[i] | 64);
                    break;
                case 9 :
                    this.platforms[i] = 3;
                    this.platforms[i] = (byte)(this.platforms[i] | 16);
                    break;
                case 10 :
                    this.platforms[i] = 3;
                    this.platforms[i] = (byte)(this.platforms[i] | 32);
                    break;
                case 11 :
                    this.platforms[i] = 3;
                    this.platforms[i] = (byte)(this.platforms[i] | 64);
                    break;
                case 12 :
                    this.platforms[i] = 4;
                    this.platforms[i] = (byte)(this.platforms[i] | 16);
                    break;
                case 13 :
                    this.platforms[i] = 4;
                    this.platforms[i] = (byte)(this.platforms[i] | 32);
                    break;
                case 14 :
                    this.platforms[i] = 4;
                    this.platforms[i] = (byte)(this.platforms[i] | 64);
                    break;
                case 15 :
                    this.platforms[i] = 5;
                    this.platforms[i] = (byte)(this.platforms[i] | 16);
                    break;
                case 16 :
                    this.platforms[i] = 5;
                    this.platforms[i] = (byte)(this.platforms[i] | 32);
                    break;
                case 17 :
                    this.platforms[i] = 5;
                    this.platforms[i] = (byte)(this.platforms[i] | 64);
                    break;
                case 18 :
                    this.platforms[i] = 6;
                    this.platforms[i] = (byte)(this.platforms[i] | 16);
                    break;
                case 19 :
                    this.platforms[i] = 6;
                    this.platforms[i] = (byte)(this.platforms[i] | 32);
                    break;
                case 20 :
                    i--;
                    break;
                case 21 :
                    this.platforms[i] = 7;
                    this.platforms[i] = (byte)(this.platforms[i] | 16);
                    break;
                case 22 :
                    this.platforms[i] = 7;
                    this.platforms[i] = (byte)(this.platforms[i] | 32);
                    break;
                case 23 :
                    i--;
                    break;
                case 24 :
                    this.platforms[i] = 8;
                    this.platforms[i] = (byte)(this.platforms[i] | 16);
                    break;
                default :
                    throw new IllegalStateException("In Playground 3: " + r);
            }
        }
    }
    
    public int getXPosition(int platform)
    {
        if (platform % 100 == 0) {
            return 16;
        }
        
        int x = this.platforms[platform] & POSITIONS;
        return x * 14 + 17;
    }
    
    int getYPosition(int platform)
    {
        return LOWEST_PLATFORM_YPOS - (platform * PLATFORM_DISTANCE);
    }
    
    int getXLength(int platform)
    {
        if (platform % 100 == 0) {
            return 144;
        }
        
        int x = this.platforms[platform];
        if ((x & 16) != 0) {
            return 30;
        }
        else if ((x & 32) != 0) {
            return 50;
        }
        else if ((x & 64) != 0) {
            return 70;
        }
        else {
            throw new IllegalStateException("In Playground.java 2");
        }
    }
    
    void renderTo(Graphics g)
    {
        final int start = getLowestVisiblePlatform();
        for (int i = start; i < start + VISIBLE_PLATFORM_COUNT; i++) {
            final int xPos = getXPosition(i);
            final int xLength = getXLength(i);
            
            final int yPos = this.world.calculateViewPosition(getYPosition(i));
            
            if (xLength == 70) {
                g.drawImage(platform70Image, xPos, yPos, Graphics.LEFT | Graphics.TOP);
            }
            else if (xLength == 50) {
                g.drawImage(platform50Image, xPos, yPos, Graphics.LEFT | Graphics.TOP);
            }
            else if (xLength == 30) {
                g.drawImage(platform30Image, xPos, yPos, Graphics.LEFT | Graphics.TOP);
            }
            else if (xLength == 144){
                g.drawImage(platformFullImage, xPos, yPos, Graphics.LEFT | Graphics.TOP);
            }
            else {
                throw new IllegalStateException("In Playground 5");
            }
            
            
            if (i == 0) {
                g.drawImage(woodShieldImageSmall, xPos+xLength-20, yPos+1, Graphics.BOTTOM|Graphics.HCENTER);
                g.setColor(0x771111);
                g.setFont(Font.getFont(Font.FACE_MONOSPACE,Font.STYLE_PLAIN,Font.SIZE_SMALL));
                g.drawString("0", xPos+xLength-20, yPos-14, Graphics.HCENTER|Graphics.TOP);
            }
            else if (i % 10 == 0) {
                if (i < 100) {
                    g.drawImage(woodShieldImageMedium, xPos+xLength-17, yPos+1, Graphics.BOTTOM|Graphics.HCENTER);
                    g.setColor(0x771111);
                    g.setFont(Font.getFont(Font.FACE_MONOSPACE,Font.STYLE_PLAIN,Font.SIZE_SMALL));
                    g.drawString(""+i, xPos+xLength-17, yPos-14, Graphics.HCENTER|Graphics.TOP);
                }
                else {
                    g.drawImage(woodShieldImageBig, xPos+xLength-17, yPos+1, Graphics.BOTTOM|Graphics.HCENTER);
                    g.setColor(0x771111);
                    g.setFont(Font.getFont(Font.FACE_MONOSPACE,Font.STYLE_PLAIN,Font.SIZE_SMALL));
                    g.drawString(""+i, xPos+xLength-17, yPos-14, Graphics.HCENTER|Graphics.TOP);
                }
            }
        }
    }
    
    short getLowestVisiblePlatform()
    {
        final int y = LOWEST_PLATFORM_YPOS - this.world.getViewYPosition();
        final short result = (short) ((y / PLATFORM_DISTANCE) - VISIBLE_PLATFORM_COUNT + 1);
        //System.out.println("lowest visible: " + result);
        return (short)Math.max(result,0);
    }

    short checkCollision(Spot spot)
    {
        final int spotX = spot.getXPosition();
        final int spotY = spot.getYPosition();
        final int spotYSpeed = spot.getYSpeed();
        final short start = getLowestVisiblePlatform();
        for (short i = start; i < start + VISIBLE_PLATFORM_COUNT; i++) {
            final int platformY = getYPosition(i);
            if (spotY == platformY) {
                final int platformX = getXPosition(i);
                final int platformLength = getXLength(i);
                if (spotX > platformX-1 && spotX < platformX + platformLength+1) {
                    return i;
                }
            }
            if (spotY < platformY && spotY + spotYSpeed > platformY) {
                final int platformX = getXPosition(i);
                final int platformLength = getXLength(i);
                if (spotX > platformX-1 && spotX < platformX + platformLength+1) {
                    spot.setYSpeed(platformY - spotY);
                    return -1;
                }
            }
        }
        
        return -1;
    }
}

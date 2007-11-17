/*
 * Spot.java
 * Created on 28.12.2004, &{time}
 */
package com.googlecode.climb.game;

import java.io.IOException;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;

/**
 * @author Fatih Coskun
 *
 */
final class Spot
{
    private static Image spotImage;
    private final static int[] tailImage = new int[196];
    static {
        try {
            spotImage = Image.createImage("/spot.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        int i = 0;
        //row 1
        tailImage[i++] = 0x00000000;
        tailImage[i++] = 0x00000000;
        tailImage[i++] = 0x00000000;
        tailImage[i++] = 0x00000000;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x00000000;
        tailImage[i++] = 0x00000000;
        tailImage[i++] = 0x00000000;
        tailImage[i++] = 0x00000000;
        //row 2
        tailImage[i++] = 0x00000000;
        tailImage[i++] = 0x00000000;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x00000000;
        tailImage[i++] = 0x00000000;
        //row 3
        tailImage[i++] = 0x00000000;
        tailImage[i++] = 0x33000000;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x00000000;
        //row 4
        tailImage[i++] = 0x00000000;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x00000000;
        //row 6
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        //row 7
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        //row 8
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        //row 9
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        //row 10
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        //row 11
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        //row 12
        tailImage[i++] = 0x00000000;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x00000000;
        //row 13
        tailImage[i++] = 0x00000000;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x00000000;
        //row 15
        tailImage[i++] = 0x00000000;
        tailImage[i++] = 0x00000000;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x00000000;
        tailImage[i++] = 0x00000000;
        //row 16
        tailImage[i++] = 0x00000000;
        tailImage[i++] = 0x00000000;
        tailImage[i++] = 0x00000000;
        tailImage[i++] = 0x00000000;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x33FF1111;
        tailImage[i++] = 0x00000000;
        tailImage[i++] = 0x00000000;
        tailImage[i++] = 0x00000000;
        tailImage[i++] = 0x00000000;
    }
    
    private final static byte NOACC = 0;
    private final static byte LEFTACC = 1;
    private final static byte RIGHTACC = 2;
    
    private final static byte NORMAL = -1;
    
    private final static byte VH_LAND_0 = 0;
    private final static byte VH_LAND_1 = 1;
    private final static byte VH_LAND_2 = 2;
    private final static byte VH_LAND_3 = 3;
    private final static byte VH_LAND_4 = 4;
    private final static byte VH_LAND_5 = 5;
    private final static byte VH_LAND_6 = 6;
    
    private final static byte H_LAND_0 = 7;
    private final static byte H_LAND_1 = 8;
    private final static byte H_LAND_2 = 9;
    
    private final static byte H_JUMP0 = 11;
    private final static byte H_JUMP1 = 12;
    private final static byte H_JUMP2 = 13;
    
    private final static byte VH_JUMP0 = 14;
    private final static byte VH_JUMP1 = 15;
    private final static byte VH_JUMP2 = 16;
    private final static byte VH_JUMP3 = 17;
    private final static byte VH_JUMP4 = 18;
    private final static byte VH_JUMP5 = 19;
    private final static byte VH_JUMP6 = 20;

    
    private final static byte COLL_0 = 8;
    private final static byte COLL_1 = 9;
    private final static byte COLL_2 = 10;
    
    private final World world;
    private final GameControl control;
    
    private int accelerateDirection;
    private int xPosition;
    private int yPosition;
    private int previousXPosition;
    private int previousYPosition;
    private int xSpeed;
    private int ySpeed;
    private boolean isLanded;
    private boolean highFall;
    private boolean veryHighFall;
    private final Sprite spotSprite;
    private int frameState = NORMAL;
    
    
    Spot(World world, GameControl control, int xPosition, int yPosition)
    {
        this.world = world;
        this.control = control;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.previousXPosition = xPosition;
        this.previousYPosition = yPosition;
        this.spotSprite = new Sprite(spotImage,16,16);
        this.spotSprite.setRefPixelPosition(8,16);
    }
    
    final void renderTo(Graphics g)
    {
        int yPos = this.world.calculateViewPosition(this.yPosition);
        if (yPos > 228 || yPos < -16) {
            return;
        }
        
        
        int tailX1 = (this.xPosition + this.previousXPosition) / 2;
        int tailY1 = (this.yPosition + this.previousYPosition) / 2;
        int tailX2 = (this.xPosition + tailX1) / 2;
        int tailY2 = (this.yPosition + tailY1) / 2;
        tailY1 = this.world.calculateViewPosition(tailY1);
        tailY2 = this.world.calculateViewPosition(tailY2);
        g.drawRGB(tailImage, 0, 14, tailX2-7, tailY2 -14, 14, 14, true);
        g.drawRGB(tailImage, 0, 14, tailX1-7, tailY1 -14, 14, 14, true);
        
        switch (this.frameState) {
            case NORMAL :
                this.spotSprite.setFrame(0);
                break;
                
            case VH_LAND_0 :
                this.spotSprite.setFrame(1);
                this.frameState = VH_LAND_1;
                break;
            case VH_LAND_1 :
                this.spotSprite.setFrame(2);
                this.frameState = VH_LAND_2;
                break;
            case VH_LAND_2 :
                this.spotSprite.setFrame(1);
                this.frameState = VH_LAND_3;
                break;
            case VH_LAND_3 :
                this.spotSprite.setFrame(0);
                this.frameState = VH_LAND_4;
                break;
            case VH_LAND_4 :
                this.spotSprite.setFrame(3);
                this.frameState = VH_LAND_5;
                break;
            case VH_LAND_5 :
                this.spotSprite.setFrame(4);
                this.frameState = VH_LAND_6;
                break;
            case VH_LAND_6 :
                this.spotSprite.setFrame(3);
                this.frameState = NORMAL;
                break;
                
            case H_LAND_0 :
                this.spotSprite.setFrame(1);
                this.frameState = H_LAND_1;
                break;
            case H_LAND_1 :
                this.spotSprite.setFrame(2);
                this.frameState = H_LAND_2;
                break;
            case H_LAND_2 :
                this.spotSprite.setFrame(1);
                this.frameState = NORMAL;
                break;
                
            case H_JUMP0 :
                this.spotSprite.setFrame(3);
                this.frameState = H_JUMP1;
                break;
            case H_JUMP1 :
                this.spotSprite.setFrame(4);
                this.frameState = H_JUMP2;
                break;
            case H_JUMP2 :
                this.spotSprite.setFrame(3);
                this.frameState = NORMAL;
                break;
                
            case VH_JUMP0 :
                this.spotSprite.setFrame(3);
                this.frameState = VH_JUMP1;
                break;
            case VH_JUMP1 :
                this.spotSprite.setFrame(4);
                this.frameState = VH_JUMP2;
                break;
            case VH_JUMP2 :
                this.spotSprite.setFrame(3);
                this.frameState = VH_JUMP3;
                break;
            case VH_JUMP3 :
                this.spotSprite.setFrame(0);
                this.frameState = VH_JUMP4;
                break;
            case VH_JUMP4 :
                this.spotSprite.setFrame(1);
                this.frameState = VH_JUMP5;
                break;
            case VH_JUMP5 :
                this.spotSprite.setFrame(2);
                this.frameState = VH_JUMP6;
                yPos -= 2;
                break;
            case VH_JUMP6 :
                this.spotSprite.setFrame(1);
                this.frameState = NORMAL;
                break;
        }
        this.spotSprite.setPosition(this.xPosition - 8, yPos-16);
        this.spotSprite.paint(g);
    }
    
    final void tick()
    {
        checkKeys();
        applySpeed();
        applyGravity();
        platformCollisionDetection();
        applyAcceleration();
        sideCollisionDetection();
    }
    
    private final void checkKeys()
    {
        if ((this.control.pressedKeys & this.control.keyJump) != 0) {
            jump();
        }
        if ((this.control.pressedKeys & this.control.keyLeft) != 0) {
            this.accelerateDirection = LEFTACC;
        }
        else if ((this.control.pressedKeys & this.control.keyRight) != 0) {
            this.accelerateDirection = RIGHTACC;
        }
        else {
            this.accelerateDirection = NOACC;
        }
    }

    
        
    final void jump()
    {
        if (!this.isLanded) {
            return;
        }
        else {
            this.isLanded = false;
            
            int speed = Math.abs(this.xSpeed);
            if (speed >= 130) {
                this.ySpeed = -19;
                this.frameState = VH_JUMP0;
            }
            else if (speed >= 80) {
                this.ySpeed = -16;
                this.frameState = H_JUMP0;
            }
            else if (speed >= 40){
                this.ySpeed = -11;
                this.frameState = H_JUMP0;
            }
            else {
                this.ySpeed = -10;
                this.frameState = H_JUMP0;
            }
            
            this.control.spotJumped();
        }
    }
    
    private final void applySpeed()
    {
        this.previousYPosition = this.yPosition;
        this.yPosition += this.ySpeed;
        
        if (this.xSpeed == 0) {
            return;
        }
        else {
            int speed = (this.xSpeed / 10) + 1;
            
            if (this.xSpeed > 0) {
                if (speed > 10) {
                    speed = 10;
                }
                else {
                    speed = (this.xSpeed / 10) + 1;
                }
            }
            else {
                if (speed < -10) {
                    speed = -10;
                }
                else {
                    speed = (this.xSpeed / 10) - 1;
                }
            }
            this.previousXPosition = this.xPosition;
            this.xPosition += speed;
        }
    }
    
    final int getXPosition()
    {
        return this.xPosition;
    }
    
    final int getYPosition()
    {
        return this.yPosition;
    }
    
    final int getXSpeed()
    {
        return this.xSpeed;
    }
    
    final int getYSpeed()
    {
        return this.ySpeed;
    }
    
    final boolean isLanded()
    {
        return this.isLanded;
    }
    
    private final void landSpot()
    {
        if (this.veryHighFall) {
            this.highFall = false;
            this.veryHighFall = false;
            this.frameState = VH_LAND_0;
        }
        else if (this.highFall) {
            this.highFall = false;
            this.frameState = H_LAND_0;
        }
        this.isLanded = true;
        this.ySpeed = 0;
    }
    
    final void setYSpeed(int speed)
    {
        this.ySpeed = speed;
    }
    
    private final void applyAcceleration()
    {
        switch (this.accelerateDirection) {
            case NOACC :
                int speed = this.xSpeed;
                if (speed == 0) {
                    return;
                }
                if (speed > 0) {
                    if (speed > 100) {
                        speed = 100;
                    }
                    if (speed > 5) {
                        this.xSpeed -= 5;
                    }
                    else {
                        this.xSpeed = 0;
                    }
                }
                else if (speed < 0) {
                    if (speed < -100) {
                        speed = -100;
                    }
                    if (speed < 5) {
                        this.xSpeed += 5;
                    }
                    else {
                        this.xSpeed = 0;
                    }
                }
                break;
            case Spot.LEFTACC :
                if (this.xSpeed > 0) {
                    this.xSpeed = this.xSpeed / 2 - 1;
                }
                else {
                    if (this.isLanded) {
                        this.xSpeed -= 10;
                    }
                    else {
                        this.xSpeed -= 8;
                    }
                }
                if (this.xSpeed < -200) {
                    this.xSpeed = -200;
                } 
                break;
            case Spot.RIGHTACC :
                if (this.xSpeed < 0) {
                    this.xSpeed = this.xSpeed / 2 + 1;
                }
                else {
                    if (this.isLanded) {
                        this.xSpeed += 10;
                    }
                    else {
                        this.xSpeed += 8;
                    }
                }
                if (this.xSpeed > 200) {
                    this.xSpeed = 200;
                }
                break;
        }
    }
    
    private final void sideCollisionDetection()
    {
        int speed = this.xSpeed / 10; 
        int location = this.xPosition;
        if (speed > 10) {
            speed = 10;
        }
        else if (speed < -10) {
            speed = -10;
        }
        
        if (speed < 0) {
            if (speed + location - 8 < 17) {
                this.xPosition = 17 + 6;
                this.xSpeed = (-1) * this.xSpeed;
                if (speed <= -10) {
                    this.frameState = VH_JUMP0;
                }
                else {
                    this.frameState = H_JUMP0;
                }
                
                this.control.spotCollidedWall();
            }
        }
        else if (speed > 0) {
            if (speed + location + 8 > 159) {
                this.xPosition = 159 - 6;
                this.xSpeed = (-1) * this.xSpeed;
                if (speed >= 10) {
                    this.frameState = VH_JUMP0;
                }
                else {
                    this.frameState = H_JUMP0;
                }
                
                this.control.spotCollidedWall();
            }
        }
    }
    
    private final void applyGravity()
    {
        if (!this.isLanded) {
            this.ySpeed += 2;
            if (this.ySpeed >= 14) {
                this.veryHighFall = true;
            }
            else if (this.ySpeed >= 12) {
                this.highFall = true;
            }
        }
    }
    
    private final void platformCollisionDetection()
    {
        //System.out.println(location);
        short touchedPlatform = this.world.playGround.checkCollision(this);
        
        if (touchedPlatform == -1) {
            this.isLanded = false;
        }
        else {
            if (this.ySpeed >= 0 && !this.isLanded) {
                this.landSpot();
                this.control.spotLanded(touchedPlatform);
            }
        }
    }

    final void setYPosition(int i)
    {
        this.yPosition = i;
    }
}
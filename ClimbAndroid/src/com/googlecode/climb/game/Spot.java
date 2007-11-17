package com.googlecode.climb.game;

import com.googlecode.climb.R;
import com.googlecode.climb.game.utils.Sprite;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;


/**
 * 
 */
final class Spot
{

    private final static int NOACC = 0;

    private final static int LEFTACC = 1;

    private final static int RIGHTACC = 2;

    private final Game game;

    private int accelerateDirection;

    private final Vector2 position;

    private int xSpeed;

    private int ySpeed;

    private boolean isLanded;

    private boolean highFall;

    private boolean veryHighFall;

    private final Sprite spotSprite;

    private final SpotAnimation spotAnimation;

    Spot(Game game, World world, int xPosition, int yPosition)
    {
        this.game = game;
        this.position = new Vector2(xPosition, yPosition, world);
        final Bitmap spotBitmap = BitmapFactory.decodeResource(
                game.getResources(), R.drawable.spot);
        this.spotSprite = new Sprite(spotBitmap, 16, 16);
        this.spotSprite.setRefPixelPosition(8, 16);
        this.spotAnimation = new SpotAnimation(this.spotSprite);
    }

    final void doDraw(Canvas canvas)
    {
        this.spotAnimation.animate();

        final int xPos = this.position.getVirtualScreenX();
        final int yPos = this.position.getVirtualScreenY();
        if ((yPos > Game.VIRTUAL_CANVAS_HEIGHT + 20) || (yPos < -20)) {
            return;
        }

        this.spotSprite.setPosition(xPos - 8, yPos - 16);
        this.spotSprite.doDraw(canvas);
    }

    final void doUpdate()
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
        if (this.game.isJumpKeyPressed()) {
            jump();
        }
        if (this.game.isMoveLeftKeyPressed()) {
            this.accelerateDirection = Spot.LEFTACC;
        } else if (this.game.isMoveRightKeyPressed()) {
            this.accelerateDirection = Spot.RIGHTACC;
        } else {
            this.accelerateDirection = Spot.NOACC;
        }
    }

    final void jump()
    {
        if (!this.isLanded) {
            return;
        }

        this.isLanded = false;

        final int speed = Math.abs(this.xSpeed);
        if (speed >= 130) {
            this.ySpeed = -19;
            this.spotAnimation.startVeryHighJumpAnimation();
        } else if (speed >= 80) {
            this.ySpeed = -16;
            this.spotAnimation.startHighJumpAnimation();
        } else if (speed >= 40) {
            this.ySpeed = -11;
            this.spotAnimation.startHighJumpAnimation();
        } else {
            this.ySpeed = -10;
            this.spotAnimation.startHighJumpAnimation();
        }

        this.game.onSpotJumped();
    }

    private final void applySpeed()
    {

        int xSpeed_normalized = this.xSpeed / 10;

        if (this.xSpeed > 0) {
            if (xSpeed_normalized > 10) {
                xSpeed_normalized = 10;
            } else {
                xSpeed_normalized += 1;
            }
        } else {
            if (xSpeed_normalized < -10) {
                xSpeed_normalized = -10;
            } else {
                xSpeed_normalized -= 1;
            }
        }

        this.position.add(xSpeed_normalized, this.ySpeed);
    }

    final Vector2 getPosition()
    {
        return this.position;
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
            this.spotAnimation.startVeryHighLand();

        } else if (this.highFall) {
            this.highFall = false;
            this.spotAnimation.startHighLand();
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
            case NOACC:
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
                    } else {
                        this.xSpeed = 0;
                    }
                } else if (speed < 0) {
                    if (speed < -100) {
                        speed = -100;
                    }
                    if (speed < 5) {
                        this.xSpeed += 5;
                    } else {
                        this.xSpeed = 0;
                    }
                }
                break;
            case Spot.LEFTACC:
                if (this.xSpeed > 0) {
                    this.xSpeed = this.xSpeed / 2 - 1;
                } else {
                    if (this.isLanded) {
                        this.xSpeed -= 10;
                    } else {
                        this.xSpeed -= 8;
                    }
                }
                if (this.xSpeed < -200) {
                    this.xSpeed = -200;
                }
                break;
            case Spot.RIGHTACC:
                if (this.xSpeed < 0) {
                    this.xSpeed = this.xSpeed / 2 + 1;
                } else {
                    if (this.isLanded) {
                        this.xSpeed += 10;
                    } else {
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
        int xSpeed_normalized = this.xSpeed / 10;
        final int xPos = this.position.getWorldX();
        if (xSpeed_normalized > 10) {
            xSpeed_normalized = 10;
        } else if (xSpeed_normalized < -10) {
            xSpeed_normalized = -10;
        }

        if (xSpeed_normalized < 0) {
            if (xSpeed_normalized + xPos - 8 < 17) {
                this.position.setWorldX(17 + 6);
                this.xSpeed = (-1) * this.xSpeed;
                if (xSpeed_normalized <= -10) {
                    this.spotAnimation.startVeryHighJumpAnimation();
                } else {
                    this.spotAnimation.startHighJumpAnimation();
                }

                this.game.onSpotCollidedWall();
            }
        } else if (xSpeed_normalized > 0) {
            if (xSpeed_normalized + xPos + 8 > 159) {
                this.position.setWorldX(159 - 6);
                this.xSpeed = (-1) * this.xSpeed;
                if (xSpeed_normalized >= 10) {
                    this.spotAnimation.startVeryHighJumpAnimation();
                } else {
                    this.spotAnimation.startHighJumpAnimation();
                }

                this.game.onSpotCollidedWall();
            }
        }
    }

    private final void applyGravity()
    {
        if (!this.isLanded) {
            this.ySpeed += 2;
            if (this.ySpeed >= 14) {
                this.veryHighFall = true;
            } else if (this.ySpeed >= 12) {
                this.highFall = true;
            }
        }
    }

    private final void platformCollisionDetection()
    {
        final int touchedPlatform = this.game.world.playGround.checkCollision(this);

        if (touchedPlatform == -1) {
            this.isLanded = false;
        } else {
            if ((this.ySpeed >= 0) && !this.isLanded) {
                this.landSpot();
                this.game.onSpotLanded(touchedPlatform);
            }
        }
    }

}
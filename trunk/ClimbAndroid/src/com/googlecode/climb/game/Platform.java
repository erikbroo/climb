package com.googlecode.climb.game;

import com.googlecode.saga.Vector2;


/**
 * Represents a platform.
 */
class Platform
{
    private final static int BASE_PLATFORM_X = ForegroundLayer.CELL_WIDTH + 1;

    private final static int BASE_PLATFORM_WIDTH = Game.VIRTUAL_CANVAS_WIDTH
            - (2 * ForegroundLayer.CELL_WIDTH) - 1;

    /**
     * All platforms have the same height of 10.
     */
    final static int PLATFORM_HEIGHT = 10;

    private final Vector2 position;

    private final PlatformLayer platformLayer;

    private int width;

    private int absoluteIndex;

    Platform(PlatformLayer platformLayer)
    {
        this.platformLayer = platformLayer;
        this.position = this.platformLayer.newVector(0, 0);
    }

    final Vector2 getPosition()
    {
        return this.position;
    }

    final int getWidth()
    {
        return this.width;
    }

    final int getAbsoluteIndex()
    {
        return this.absoluteIndex;
    }

    /**
     * Mutates this platform and sets its attributes depending on the specified
     * platform index. If index % 100 = 0, the attributes will be the attributes
     * of a base platform. Otherwise, the attributes will be random generated.
     * 
     * @param platformIndex
     */
    final void setNewAttributes(int platformIndex)
    {
        this.absoluteIndex = platformIndex;

        if (platformIndex % 100 == 0) {
            newBaseAttributes(this.absoluteIndex);
        } else {
            newRandomAttributes(this.absoluteIndex);
        }
    }

    private final void newRandomAttributes(int platformIndex)
    {
        final int randomAttribute = Platform.randomPlatformAttribute();

        // computing x position out of the platform's random attribute
        final int NUMBER_OF_POSITIONS = 15;
        final int xPos = (randomAttribute & NUMBER_OF_POSITIONS) * 14 + 17;

        // computing y position
        final int yPos = PlatformSequence.LOWEST_PLATFORM_YPOS
                + (this.absoluteIndex * PlatformSequence.PLATFORM_DISTANCE);

        this.position.setLayerX(xPos);
        this.position.setLayerY(yPos);

        // computing width out of the platform's random attribute
        final int width;
        if ((randomAttribute & 16) != 0) {
            width = 30; // width of short platform
        } else if ((randomAttribute & 32) != 0) {
            width = 50; // width of medium platform
        } else if ((randomAttribute & 64) != 0) {
            width = 70; // width of long platform
        } else {
            throw new IllegalStateException("invalid random platform attribute: "
                    + randomAttribute);
        }
        this.width = width;
    }

    private final void newBaseAttributes(int platformIndex)
    {
        final int xPos = BASE_PLATFORM_X;
        final int yPos = PlatformSequence.LOWEST_PLATFORM_YPOS
                + (this.absoluteIndex * PlatformSequence.PLATFORM_DISTANCE);

        this.position.setLayerX(xPos);
        this.position.setLayerY(yPos);

        this.width = BASE_PLATFORM_WIDTH;
    }

    /**
     * Calculates and returns a random attribute for a new platform.
     * 
     * @return new random platform attribute
     */
    private static final int randomPlatformAttribute()
    {
        int result;

        final int r = Math.abs(Game.RANDOM.nextInt() % 25);
        switch (r) {
            case 0:
                result = 0;
                result = (result | 16);
                break;
            case 1:
                result = 0;
                result = (result | 32);
                break;
            case 2:
                result = 0;
                result = (result | 64);
                break;
            case 3:
                result = 1;
                result = (result | 16);
                break;
            case 4:
                result = 1;
                result = (result | 32);
                break;
            case 5:
                result = 1;
                result = (result | 64);
                break;
            case 6:
                result = 2;
                result = (result | 16);
                break;
            case 7:
                result = 2;
                result = (result | 32);
                break;
            case 8:
                result = 2;
                result = (result | 64);
                break;
            case 9:
                result = 3;
                result = (result | 16);
                break;
            case 10:
                result = 3;
                result = (result | 32);
                break;
            case 11:
                result = 3;
                result = (result | 64);
                break;
            case 12:
                result = 4;
                result = (result | 16);
                break;
            case 13:
                result = 4;
                result = (result | 32);
                break;
            case 14:
                result = 4;
                result = (result | 64);
                break;
            case 15:
                result = 5;
                result = (result | 16);
                break;
            case 16:
                result = 5;
                result = (result | 32);
                break;
            case 17:
                result = 5;
                result = (result | 64);
                break;
            case 18:
                result = 6;
                result = (result | 16);
                break;
            case 19:
                result = 6;
                result = (result | 32);
                break;
            case 20:
                return Platform.randomPlatformAttribute();
            case 21:
                result = 7;
                result = (result | 16);
                break;
            case 22:
                result = 7;
                result = (result | 32);
                break;
            case 23:
                return Platform.randomPlatformAttribute();
            case 24:
                result = 8;
                result = (result | 16);
                break;
            default:
                throw new IllegalStateException("In Platform 3: " + r);
        }

        return result;
    }
}

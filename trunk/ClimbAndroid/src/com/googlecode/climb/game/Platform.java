package com.googlecode.climb.game;

import com.googlecode.climb.game.utils.Vector2;


/**
 * Represents a platform.
 */
class Platform
{
    private final Vector2 position;

    private final int width;

    private final int height;

    private final int absoluteIndex;

    /**
     * Use static create methods.
     */
    private Platform(Vector2 position, int width, int height, int absoluteIndex)
    {
        this.position = position;
        this.width = width;
        this.height = height;
        this.absoluteIndex = absoluteIndex;
    }

    public static Platform createPlatform(int absoluteIndex,
            PlatformLayer platformLayer)
    {
        if (absoluteIndex % 100 == 0) {
            return Platform.createBasePlatform(absoluteIndex, platformLayer);
        }
        return Platform.createRandomPlatform(absoluteIndex, platformLayer);
    }

    private static Platform createRandomPlatform(int absoluteIndex,
            PlatformLayer playground)
    {
        final int randomAttribute = Platform.randomPlatformAttribute();

        // computing x position out of the platform's random attribute
        final int NUMBER_OF_POSITIONS = 15;
        final int xPos = (randomAttribute & NUMBER_OF_POSITIONS) * 14 + 17;

        // computing y position
        final int yPos = PlatformSequence.LOWEST_PLATFORM_YPOS
                + (absoluteIndex * PlatformSequence.PLATFORM_DISTANCE);

        final Vector2 position = new Vector2(xPos, yPos, Game.VIRTUAL_CANVAS_WIDTH, Game.VIRTUAL_CANVAS_HEIGHT, playground);

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

        // every platform has the same height
        final int height = 10;

        return new Platform(position, width, height, absoluteIndex);
    }

    private static Platform createBasePlatform(int absoluteIndex,
            PlatformLayer playground)
    {
        final int xPos = 16; // xPos of base platforms

        // computing y position
        final int yPos = PlatformSequence.LOWEST_PLATFORM_YPOS
                + (absoluteIndex * PlatformSequence.PLATFORM_DISTANCE);

        final Vector2 position = new Vector2(xPos, yPos, Game.VIRTUAL_CANVAS_WIDTH, Game.VIRTUAL_CANVAS_HEIGHT, playground);

        final int width = 144; // width of base platforms

        final int height = 10; // every platform has the same height

        return new Platform(position, width, height, absoluteIndex);
    }

    final Vector2 getPosition()
    {
        return this.position;
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

    final int getWidth()
    {
        return this.width;
    }

    final int getHeight()
    {
        return this.height;
    }

    final int getAbsoluteIndex()
    {
        return this.absoluteIndex;
    }
}

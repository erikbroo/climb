package com.googlecode.climb.game;

import com.googlecode.climb.game.utils.RingList;


/**
 * Represents an infinitely long sequence of random platforms. This class stores
 * only the interval [start,end] of currently visible platforms.
 */
final class PlatformSequence
{
    /**
     * Two adjacent platforms have a distance of 30.
     */
    final static int PLATFORM_DISTANCE = 30;

    /**
     * All platforms have the same height of 10.
     */
    final static int PLATFORM_HEIGHT = 10;

    /**
     * Visible platform count is view_height / platform_distance. We are adding
     * additional 3 platforms here: they will not be visible, but be below the
     * visible view (this way the player can try to save his life when falling
     * below the visible view).
     */
    final static int VISIBLE_PLATFORM_COUNT = Game.VIRTUAL_CANVAS_HEIGHT
            / (PlatformSequence.PLATFORM_DISTANCE) + 5;

    /**
     * World coordinate system origin is the lower left corner. The initial
     * lowest platform yPos is 30.
     */
    final static int LOWEST_PLATFORM_YPOS = 30;

    private final World world;

    private final RingList<Platform> platformList;

    private int topmostVisiblePlatform;;

    public PlatformSequence(World world)
    {
        this.world = world;
        this.platformList = new RingList<Platform>(PlatformSequence.VISIBLE_PLATFORM_COUNT);
        initializeList();
    }

    /**
     * Initializes the platform list with the first x visible platforms.
     */
    private final void initializeList()
    {
        // the first platform:
        this.platformList.add(Platform.createPlatform(0, this.world));
        // remaining initial platforms:
        setHighestVisiblePlatform(PlatformSequence.VISIBLE_PLATFORM_COUNT - 1);
    }

    final Platform getPlatform(int index)
    {
        if ((index < 0) || (index >= this.platformList.getSize())) {
            throw new IllegalArgumentException("Index must be in the interval [0,"
                    + (this.platformList.getSize() - 1) + "]: " + index);
        }

        return this.platformList.get(index);
    }

    /**
     * Sets the new highest visible platform. Because the game scrolls upwards
     * only, the new highest visible platform cannot be set to a platform that
     * is lower than the previous highest visible platform.
     * 
     * @param platform
     *            new highest visible platform
     * @throws IllegalArgumentException
     *             if platform is lower than the previous highest visible
     *             platform
     */
    private final void setHighestVisiblePlatform(int platform)
    {
        if (platform <= this.topmostVisiblePlatform) {
            return;
        }

        // if (platform < this.topmostVisiblePlatform) {
        // throw new IllegalArgumentException("new highest < previous highest: "
        // + platform + " < " + this.topmostVisiblePlatform);
        // }

        while (this.topmostVisiblePlatform < platform) {
            this.topmostVisiblePlatform += 1;
            final Platform newPlatform = Platform.createPlatform(
                    this.topmostVisiblePlatform, this.world);
            this.platformList.add(newPlatform);
        }
    }

    /**
     * Sets the highest visible platform according to the specified
     * worldViewYPosition. The lower the argument, the higher the highest
     * visible platform.
     * 
     * @param worldViewYPosition
     */
    final void updateWorldview(final int worldviewYPosition)
    {
        final int topmostPlatformIndex = (worldviewYPosition
                + Game.VIRTUAL_CANVAS_HEIGHT + PlatformSequence.LOWEST_PLATFORM_YPOS)
                / PlatformSequence.PLATFORM_DISTANCE;

        this.setHighestVisiblePlatform(topmostPlatformIndex);
    }

    final int visiblePlatformCount()
    {
        return this.platformList.getSize();
    }

    /**
     * @param spot
     * @return
     */
    final int getCollidingPlatform(Spot spot)
    {
        final int spotX = spot.getPosition().getWorldX();
        final int spotY = spot.getPosition().getWorldY();
        final int spotYSpeed = spot.getYSpeed();

        final Platform lowerPlatform = lowerPlatform(spotY);
        if (lowerPlatform == null) {
            return -1;
        }

        final int platformWidth = lowerPlatform.getWidth();
        final int platformHeight = lowerPlatform.getHeight();
        final int platformX = lowerPlatform.getPosition().getWorldX();
        final int platformY = lowerPlatform.getPosition().getWorldY();

        if (spotY == platformY) {

            if ((spotX >= platformX - 1)
                    && (spotX <= platformX + platformWidth + 1)) {
                return lowerPlatform.getAbsoluteIndex();
            }
        }
        if ((spotY > platformY) && (spotY + spotYSpeed < platformY)) {
            if ((spotX >= platformX) && (spotX <= platformX + platformWidth)) {
                spot.setYSpeed(platformY - spotY);
                return -1; // collision will happen next frame
            }
        }

        return -1;
    }

    /**
     * Returns the lower platform nearest to the specified y-position.
     * 
     * @param spotY
     * @return
     */
    private final Platform lowerPlatform(int y)
    {
        int topmostPlatformYpos = PlatformSequence.LOWEST_PLATFORM_YPOS
                + (this.topmostVisiblePlatform * PlatformSequence.PLATFORM_DISTANCE);

        int index = 0;
        while (topmostPlatformYpos > y) {
            topmostPlatformYpos -= PlatformSequence.PLATFORM_DISTANCE;
            index += 1;
        }

        // if (index >= PlatformSequence.VISIBLE_PLATFORM_COUNT) {
        // throw new IllegalStateException("Visible platform index >= visible
        // platform count: "
        // + index + ">=" + PlatformSequence.VISIBLE_PLATFORM_COUNT);
        // }
        if (index >= PlatformSequence.VISIBLE_PLATFORM_COUNT) {
            return null;
        }

        return this.platformList.get(index);
    }
}

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
     * Visible platform count is view_height / platform_distance.
     */
    final static int VISIBLE_PLATFORM_COUNT = Game.VIRTUAL_CANVAS_HEIGHT
            / (PlatformSequence.PLATFORM_DISTANCE) + 1;

    /**
     * World coordinate system origin is the lower left corner. The initial
     * lowest platform yPos is 10.
     */
    final static int LOWEST_PLATFORM_YPOS = 10;

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
        if ((index < 0) || (index >= this.topmostVisiblePlatform)) {
            throw new IllegalArgumentException("Index must be in the interval [0,"
                    + this.topmostVisiblePlatform + "]: " + index);
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
            throw new IllegalArgumentException("new highest < previous highest: "
                    + platform + " < " + this.topmostVisiblePlatform);
        }

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
}

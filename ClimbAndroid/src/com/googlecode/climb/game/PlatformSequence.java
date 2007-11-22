package com.googlecode.climb.game;

/**
 * Represents an infinitely long sequence of random platforms. This class stores
 * only the interval [start,end] of currently visible platforms. This class
 * reuses old instances of the Platform class.
 */
final class PlatformSequence
{
    /**
     * Two adjacent platforms have a distance of 30.
     */
    final static int PLATFORM_DISTANCE = 30;

    /**
     * Visible platform count is view_height / platform_distance. We are adding
     * additional 5 platforms here: they will not be visible, but be below the
     * visible view (this way the player can try to save his life when falling
     * below the visible view).
     */
    final static int VISIBLE_PLATFORM_COUNT = Game.VIRTUAL_CANVAS_HEIGHT
            / (PlatformSequence.PLATFORM_DISTANCE) + 5;

    final static int LOWEST_PLATFORM_YPOS = 30;

    /**
     * A circular list for the visible platforms.
     */
    private final Platform[] platforms = new Platform[VISIBLE_PLATFORM_COUNT];

    /**
     * An index for the current first element in the circular platforms list.
     */
    private int platformListIndex;

    private int topmostVisiblePlatform;

    private final PlatformLayer platformLayer;

    public PlatformSequence(PlatformLayer platformLayer)
    {
        this.platformLayer = platformLayer;
        initializeList();
    }

    /**
     * Initializes the platform list with the first x visible platforms.
     */
    private final void initializeList()
    {
        for (int i = 0; i < VISIBLE_PLATFORM_COUNT; i++) {
            this.platforms[i] = new Platform(this.platformLayer);
        }

        // the first platform:
        this.platforms[0].setNewAttributes(0);
        // remaining initial platforms:
        setHighestVisiblePlatform(PlatformSequence.VISIBLE_PLATFORM_COUNT - 1);

        // // the first platform:
        // this.platformList.add(Platform.createPlatform(0,
        // this.platformLayer));
        // // remaining initial platforms:
        // setHighestVisiblePlatform(PlatformSequence.VISIBLE_PLATFORM_COUNT -
        // 1);
    }

    /**
     * Returns one of the visible platforms. The index specifies how high the
     * returned platform is. Specifying an index of 0 will return the highest
     * currently visible platform. Higher indizes will return lower platforms.
     * 
     * @param index
     * @return
     */
    final Platform getPlatform(int index)
    {
        if ((index < 0) || (index >= this.platforms.length)) {
            throw new IllegalArgumentException("Index must be in the interval [0,"
                    + (this.platforms.length - 1) + "]: " + index);
        }

        int realIndex = this.platformListIndex - index;
        if (realIndex < 0) {
            realIndex = this.platforms.length + realIndex;
        }
        return this.platforms[realIndex];
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

        while (this.topmostVisiblePlatform < platform) {
            this.topmostVisiblePlatform += 1;
            this.platformListIndex += 1;
            this.platforms[this.platformListIndex].setNewAttributes(this.topmostVisiblePlatform);
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

    /**
     * @param spot
     * @return
     */
    final int getCollidingPlatform(Spot spot)
    {
        final int spotX = spot.getPosition().getLayerX();
        final int spotY = spot.getPosition().getLayerY();
        final int spotYSpeed = spot.getYSpeed();

        final Platform lowerPlatform = lowerPlatform(spotY);
        if (lowerPlatform == null) {
            return -1;
        }

        final int platformWidth = lowerPlatform.getWidth();
        final int platformHeight = Platform.PLATFORM_HEIGHT;
        final int platformX = lowerPlatform.getPosition().getLayerX();
        final int platformY = lowerPlatform.getPosition().getLayerY();

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

        if (index >= PlatformSequence.VISIBLE_PLATFORM_COUNT) {
            return null;
        }

        int realIndex = this.platformListIndex - index;
        if (realIndex < 0) {
            realIndex = this.platforms.length + realIndex;
        }
        return this.platforms[realIndex];
    }
}

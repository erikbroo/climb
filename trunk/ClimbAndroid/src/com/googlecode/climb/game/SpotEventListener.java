package com.googlecode.climb.game;

/**
 * An event listener for spot events. Spot events are jumping, landing and
 * collision with the side walls.
 */
public interface SpotEventListener
{
    /**
     * Called when spot lands on a platform.
     * 
     * @param onPlatform
     *            the platform Spot landed on
     * @param previousPlatform
     */
    public void onSpotLanded(int onPlatform, int previousPlatform);

    /**
     * Called when spot jumps from a platform.
     * 
     * @param fromPlatform
     *            the platform spot jumped from.
     */
    public void onSpotJumped(int fromPlatform);

    /**
     * Called when spot collides with one of the side walls.
     */
    public void onSpotCollidedWall();
}

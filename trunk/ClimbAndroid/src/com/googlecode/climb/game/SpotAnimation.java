package com.googlecode.climb.game;

import com.googlecode.saga.Sprite;


/**
 * 
 */
final class SpotAnimation
{

    private final static byte[] HIGH_LAND_ANIMATION =
    { 0, 1, 1, 2, 2, 1, 1, 0 };

    private final static byte[] HIGH_JUMP_ANIMATION =
    { 3, 3, 3, 4, 4, 4, 4, 3, 3, 0 };

    private final static byte[] VERYHIGH_LAND_ANIMATION =
    { 0, 0, 1, 1, 1, 2, 2, 2, 2, 1, 1, 0, 3, 0 };

    private final static byte[] BOUNCE_ANIMATION =
    { 3, 3, 3, 4, 4, 4, 4, 3, 3, 0, 0, 1, 1, 0 };

    private static final byte[] FALL_ANIMATION =
    { 3 };

    private static final byte[] HIGH_FALL_ANIMATION =
    { 4 };

    private final Sprite spotSprite;

    private byte[] currentAnimation;

    private int currentAnimationIndex;

    /**
     * 
     */
    public SpotAnimation(Sprite spotSprite)
    {
        this.spotSprite = spotSprite;
    }

    final void animate()
    {
        if (this.currentAnimation == null) {
            return;
        }
        if (this.currentAnimationIndex >= this.currentAnimation.length) {
            return;
        }

        this.spotSprite.setFrame(this.currentAnimation[this.currentAnimationIndex]);
        this.currentAnimationIndex++;
    }

    /**
     * 
     */
    final void startHighJump()
    {
        this.currentAnimation = SpotAnimation.HIGH_JUMP_ANIMATION;
        this.currentAnimationIndex = 0;
    }

    /**
     * 
     */
    final void startBounce()
    {
        this.currentAnimation = SpotAnimation.BOUNCE_ANIMATION;
        this.currentAnimationIndex = 0;
    }

    /**
     * 
     */
    final void startHighLand()
    {
        this.currentAnimation = SpotAnimation.HIGH_LAND_ANIMATION;
        this.currentAnimationIndex = 0;
    }

    /**
     * 
     */
    final void startVeryHighLand()
    {
        this.currentAnimation = SpotAnimation.VERYHIGH_LAND_ANIMATION;
        this.currentAnimationIndex = 0;
    }

    /**
     * 
     */
    final void startFall()
    {
        this.currentAnimation = SpotAnimation.FALL_ANIMATION;
        this.currentAnimationIndex = 0;
    }

    final void startHighFall()
    {
        this.currentAnimation = SpotAnimation.HIGH_FALL_ANIMATION;
        this.currentAnimationIndex = 0;
    }
}

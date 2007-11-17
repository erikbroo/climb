package de.coskunscastle.climb.game;

import de.coskunscastle.climb.game.utils.Sprite;


/**
 * 
 */
final class SpotAnimation
{
    private final static int NORMAL = -1;

    private final static int VERYHIGH_LAND_0 = 0;

    private final static int VERYHIGH_LAND_1 = 1;

    private final static int VERYHIGH_LAND_2 = 2;

    private final static int VERYHIGH_LAND_3 = 3;

    private final static int VERYHIGH_LAND_4 = 4;

    private final static int VERYHIGH_LAND_5 = 5;

    private final static int VERYHIGH_LAND_6 = 6;

    private final static int HIGH_LAND_0 = 7;

    private final static int HIGH_LAND_1 = 8;

    private final static int HIGH_LAND_2 = 9;

    private final static int HIGH_JUMP_0 = 11;

    private final static int HIGH_JUMP_1 = 12;

    private final static int HIGH_JUMP_2 = 13;

    private final static int VERYHIGH_JUMP_0 = 14;

    private final static int VERYHIGH_JUMP_1 = 15;

    private final static int VERYHIGH_JUMP_2 = 16;

    private final static int VERYHIGH_JUMP_3 = 17;

    private final static int VERYHIGH_JUMP_4 = 18;

    private final static int VERYHIGH_JUMP_5 = 19;

    private final static int VERYHIGH_JUMP_6 = 20;

    private int frameState = SpotAnimation.NORMAL;

    private final Sprite spotSprite;

    /**
     * 
     */
    public SpotAnimation(Sprite spotSprite)
    {
        this.spotSprite = spotSprite;
    }

    final void animate()
    {
        switch (this.frameState) {
            case NORMAL:
                this.spotSprite.setFrame(0);
                break;
            case VERYHIGH_LAND_0:
                this.spotSprite.setFrame(1);
                this.frameState = SpotAnimation.VERYHIGH_LAND_1;
                break;
            case VERYHIGH_LAND_1:
                this.spotSprite.setFrame(2);
                this.frameState = SpotAnimation.VERYHIGH_LAND_2;
                break;
            case VERYHIGH_LAND_2:
                this.spotSprite.setFrame(1);
                this.frameState = SpotAnimation.VERYHIGH_LAND_3;
                break;
            case VERYHIGH_LAND_3:
                this.spotSprite.setFrame(0);
                this.frameState = SpotAnimation.VERYHIGH_LAND_4;
                break;
            case VERYHIGH_LAND_4:
                this.spotSprite.setFrame(3);
                this.frameState = SpotAnimation.VERYHIGH_LAND_5;
                break;
            case VERYHIGH_LAND_5:
                this.spotSprite.setFrame(4);
                this.frameState = SpotAnimation.VERYHIGH_LAND_6;
                break;
            case VERYHIGH_LAND_6:
                this.spotSprite.setFrame(3);
                this.frameState = SpotAnimation.NORMAL;
                break;

            case HIGH_LAND_0:
                this.spotSprite.setFrame(1);
                this.frameState = SpotAnimation.HIGH_LAND_1;
                break;
            case HIGH_LAND_1:
                this.spotSprite.setFrame(2);
                this.frameState = SpotAnimation.HIGH_LAND_2;
                break;
            case HIGH_LAND_2:
                this.spotSprite.setFrame(1);
                this.frameState = SpotAnimation.NORMAL;
                break;

            case HIGH_JUMP_0:
                this.spotSprite.setFrame(3);
                this.frameState = SpotAnimation.HIGH_JUMP_1;
                break;
            case HIGH_JUMP_1:
                this.spotSprite.setFrame(4);
                this.frameState = SpotAnimation.HIGH_JUMP_2;
                break;
            case HIGH_JUMP_2:
                this.spotSprite.setFrame(3);
                this.frameState = SpotAnimation.NORMAL;
                break;

            case VERYHIGH_JUMP_0:
                this.spotSprite.setFrame(3);
                this.frameState = SpotAnimation.VERYHIGH_JUMP_1;
                break;
            case VERYHIGH_JUMP_1:
                this.spotSprite.setFrame(4);
                this.frameState = SpotAnimation.VERYHIGH_JUMP_2;
                break;
            case VERYHIGH_JUMP_2:
                this.spotSprite.setFrame(3);
                this.frameState = SpotAnimation.VERYHIGH_JUMP_3;
                break;
            case VERYHIGH_JUMP_3:
                this.spotSprite.setFrame(0);
                this.frameState = SpotAnimation.VERYHIGH_JUMP_4;
                break;
            case VERYHIGH_JUMP_4:
                this.spotSprite.setFrame(1);
                this.frameState = SpotAnimation.VERYHIGH_JUMP_5;
                break;
            case VERYHIGH_JUMP_5:
                this.spotSprite.setFrame(2);
                this.frameState = SpotAnimation.VERYHIGH_JUMP_6;
                // yPos -= 2;
                break;
            case VERYHIGH_JUMP_6:
                this.spotSprite.setFrame(1);
                this.frameState = SpotAnimation.NORMAL;
                break;
        }
    }

    /**
     * 
     */
    final void startHighJumpAnimation()
    {
        this.frameState = SpotAnimation.HIGH_JUMP_0;
    }

    /**
     * 
     */
    final void startVeryHighJumpAnimation()
    {
        this.frameState = SpotAnimation.VERYHIGH_JUMP_0;
    }

    /**
     * 
     */
    final void startHighLand()
    {
        this.frameState = SpotAnimation.HIGH_LAND_0;
    }

    /**
     * 
     */
    final void startVeryHighLand()
    {
        this.frameState = SpotAnimation.VERYHIGH_LAND_0;
    }
}

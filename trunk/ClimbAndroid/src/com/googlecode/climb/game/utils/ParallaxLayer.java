package com.googlecode.climb.game.utils;

/**
 * 
 */
public class ParallaxLayer
{
    private final float depth;

    private int viewX;

    private int viewY;

    /**
     * 
     */
    public ParallaxLayer(float depth)
    {
        this.depth = depth;
    }

    /**
     * @return
     */
    public final float getDepth()
    {
        return this.depth;
    }

    /**
     * @param x
     * @param y
     */
    final void setView(int x, int y)
    {
        setViewX(x);
        setViewY(y);
    }

    /**
     * @param y
     */
    final void setViewY(int y)
    {
        this.viewY = (int) (y * this.depth);
    }

    /**
     * @param x
     */
    final void setViewX(int x)
    {
        this.viewX = (int) (x * this.depth);
    }

    public final int getViewX()
    {
        return this.viewX;
    }

    public final int getViewY()
    {
        return this.viewY;
    }
}

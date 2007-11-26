package com.googlecode.saga;

/**
 * This class represents a vector in two separate coordinate systems: the layer
 * coordinate system and the virtual screen coordinate system. In the layer
 * coordinate system the origin is considered to be in the lower left corner. In
 * the screen coordinate system the origin is considered to be in the top left
 * corner. One unit in the layer coordinate system is considered to be equal to
 * one unit in the virtual screen coordinate system. The layer coordinate system
 * is much larger than can be visualized in the virtual screen coordinate
 * system. Hence a layer "view" is used to project layer coordinates to virtual
 * screen coordinates. This class is mutable, use with care.
 */
public class Vector2
{
    private int world_x;

    private int world_y;

    private final ParallaxLayer layer;

    private final int virtualScreenWidth;

    private final int virtualScreenHeight;

    public Vector2(int world_x, int world_y, int virtualScreenWidth,
            int virtualScreenHeight, ParallaxLayer layer)
    {
        this.world_x = world_x;
        this.world_y = world_y;
        this.virtualScreenWidth = virtualScreenWidth;
        this.virtualScreenHeight = virtualScreenHeight;

        if (layer == null) {
            throw new IllegalArgumentException("layer must not be null");
        }
        this.layer = layer;
    }

    public int getLayerX()
    {
        return this.world_x;
    }

    public int getLayerY()
    {
        return this.world_y;
    }

    public int getVirtualScreenX()
    {
        return this.world_x;
    }

    public int getVirtualScreenY()
    {
        return this.virtualScreenHeight
                - (this.world_y - this.layer.getViewY());
    }

    /**
     * Adds the specified x and y values to this vector's world x and y values.
     * 
     * @param world_x
     * @param world_y
     */
    public final void add(int layer_x, int layer_y)
    {
        this.world_x += layer_x;
        this.world_y += layer_y;
    }

    /**
     * @param x
     */
    public final void setLayerX(int x)
    {
        this.world_x = x;
    }

    /**
     * @param y
     */
    public final void setLayerY(int y)
    {
        this.world_y = y;
    }
}

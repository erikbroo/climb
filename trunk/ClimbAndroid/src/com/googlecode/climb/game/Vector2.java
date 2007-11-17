package com.googlecode.climb.game;

/**
 * This class represents a vector in two separate coordinate systems: the world
 * coordinate system and the virtual screen coordinate system. In the world
 * coordinate system the origin is considered to be in the lower left corner. In
 * the screen coordinate system the origin is considered to be in the top left
 * corner. One unit in the world coordinate system is considered to be equal to
 * one unit in the virtual screen coordinate system. The world coordinate system
 * is much larger than can be visualized in the virtual screen coordinate
 * system. Hence a world "view" is used to project world coordinates to virtual
 * screen coordinates. This class is mutable, use with care.
 */
public class Vector2
{
    private int world_x;

    private int world_y;

    private final World world;

    public Vector2(int world_x, int world_y, World world)
    {
        this.world_x = world_x;
        this.world_y = world_y;
        this.world = world;
    }

    public int getWorldX()
    {
        return this.world_x;
    }

    public int getWorldY()
    {
        return this.world_y;
    }

    public int getVirtualScreenX()
    {
        return this.world_x;
    }

    public int getVirtualScreenY()
    {
        return Game.VIRTUAL_CANVAS_HEIGHT
                - (this.world_y - this.world.getViewY());
    }

    /**
     * Adds the specified x and y values to this vector's world x and y values.
     * 
     * @param world_x
     * @param world_y
     */
    final void add(int world_x, int world_y)
    {
        this.world_x += world_x;
        this.world_y += world_y;
    }

    /**
     * @param x
     */
    final void setWorldX(int x)
    {
        this.world_x = x;
    }

    /**
     * @param y
     */
    public void setWorldY(int y)
    {
        this.world_y = y;
    }
}

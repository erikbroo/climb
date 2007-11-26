package com.googlecode.saga;

import java.util.Comparator;


/**
 * A class managing several layers capable of parallax-scrolling.
 */
public class ParallaxManager
{
    private final SortedArrayList<ParallaxLayer> layers;
    {
        this.layers = new SortedArrayList<ParallaxLayer>(new Comparator<ParallaxLayer>() {
            @Override
            public int compare(ParallaxLayer first, ParallaxLayer second)
            {
                if (first.getDepth() > second.getDepth()) {
                    return -1;
                } else if (first.getDepth() < second.getDepth()) {
                    return 1;
                }
                return 0;
            }
        });
    }

    private int numberOfLayers;

    /**
     * Adds a new parallax layer to this parallax manager.
     * 
     * @param layer
     */
    public final void addParallaxLayer(ParallaxLayer layer)
    {
        this.layers.add(layer);
        this.numberOfLayers = this.layers.size();
    }

    /**
     * Sets the view for this manager, which will automatically set the view of
     * all added layers with respect to their depth attributes.
     * 
     * @param x
     *            x-coordinate of the new view
     * @param y
     *            y-coordinate of the new view
     */
    public final void setView(int x, int y)
    {
        for (int i = 0; i < this.numberOfLayers; i++) {
            final ParallaxLayer layer = this.layers.get(i);
            layer.setView(x, y);
        }
    }

    public final void setViewX(int x)
    {
        setView(x, 0);
    }

    public final void setViewY(int y)
    {
        setView(0, y);
    }

    /**
     * Moves the view for this manager, which will automatically move the view
     * of all added layers with respect to their depth attributes.
     * 
     * @param x
     *            x-coordinate of the new view
     * @param y
     *            y-coordinate of the new view
     */
    public final void moveView(int x, int y)
    {
        for (int i = 0; i < this.numberOfLayers; i++) {
            final ParallaxLayer layer = this.layers.get(i);
            layer.moveView(x, y);
        }
    }

    public final void moveViewX(int x)
    {
        moveView(x, 0);
    }

    public final void moveViewY(int y)
    {
        moveView(0, y);
    }
}
package com.googlecode.climb.game;

import android.content.Resources;
import android.graphics.Canvas;
import com.googlecode.climb.game.utils.ParallaxManager;


final class World extends ParallaxManager
{
    private final PlatformLayer platformLayer;

    private final BackgroundLayer backgroundLayer;

    private final ForegroundLayer foregroundLayer;

    private final Spot spot;

    private final Water water;

    World(Resources resources, KeyEngine keyEngine)
    {
        this.platformLayer = new PlatformLayer(resources);
        this.backgroundLayer = new BackgroundLayer(resources);
        this.foregroundLayer = new ForegroundLayer(resources);

        this.water = new Water(resources, this.platformLayer);
        this.spot = new Spot(this.platformLayer, resources, keyEngine);

        addParallaxLayer(this.platformLayer);
        addParallaxLayer(this.backgroundLayer);
        addParallaxLayer(this.foregroundLayer);
    }

    final void doDraw(Canvas canvas)
    {
        this.backgroundLayer.doDraw(canvas);
        this.platformLayer.doDraw(canvas);
        this.spot.doDraw(canvas);
        this.water.doDraw(canvas);
        this.foregroundLayer.doDraw(canvas);
    }

    final void doUpdate(long thisUpdate)
    {
        this.spot.doUpdate();

        final int spotScreenY = this.spot.getPosition().getVirtualScreenY();
        if (spotScreenY < 40) {
            moveView(0, 6);
        } else if (spotScreenY < 60) {
            moveView(0, 5);
        } else if (spotScreenY < 80) {
            moveView(0, 4);
        } else if (spotScreenY < 100) {
            moveView(0, 3);
        } else if (spotScreenY < 120) {
            moveView(0, 2);
        } else if (spotScreenY < 140) {
            moveView(0, 1);
        }

        this.platformLayer.doUpdate();
        this.backgroundLayer.doUpdate();
        this.foregroundLayer.doUpdate();

        this.water.doUpdate();
    }

    final PlatformLayer getPlatformLayer()
    {
        return this.platformLayer;
    }

    final Spot getSpot()
    {
        return this.spot;
    }

    final Water getWater()
    {
        return this.water;
    }
}
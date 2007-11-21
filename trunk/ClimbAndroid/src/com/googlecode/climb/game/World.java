package com.googlecode.climb.game;

import android.graphics.Canvas;
import com.googlecode.climb.game.utils.ParallaxManager;


/**
 * 
 */
final class World
{
    private final ParallaxManager parallaxManager;

    final PlatformLayer platformLayer;

    private final BackgroundLayer backgroundLayer;

    private final ForegroundLayer foregroundLayer;

    private int yPosition;

    final Water water;

    final Spot spot;

    World(Game game)
    {
        this.parallaxManager = new ParallaxManager();
        this.platformLayer = new PlatformLayer(game);
        this.parallaxManager.addParallaxLayer(this.platformLayer);
        this.backgroundLayer = new BackgroundLayer(game);
        this.parallaxManager.addParallaxLayer(this.backgroundLayer);
        this.foregroundLayer = new ForegroundLayer(game);
        this.parallaxManager.addParallaxLayer(this.foregroundLayer);

        this.yPosition = 0;

        this.water = new Water(game, this.platformLayer);

        this.spot = new Spot(game, this.platformLayer, 50, PlatformSequence.LOWEST_PLATFORM_YPOS + 1);
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
            viewup(6);
        } else if (spotScreenY < 60) {
            viewup(5);
        } else if (spotScreenY < 80) {
            viewup(4);
        } else if (spotScreenY < 100) {
            viewup(3);
        } else if (spotScreenY < 120) {
            viewup(2);
        } else if (spotScreenY < 140) {
            viewup(1);
        }

        this.parallaxManager.setViewY(this.yPosition);
        this.platformLayer.doUpdate();
        this.backgroundLayer.doUpdate();
        this.foregroundLayer.doUpdate();

        this.water.doUpdate();
    }

    final void viewup(int i)
    {
        this.yPosition += i;
    }

    final int getViewY()
    {
        return this.yPosition;
    }

    final int checkPlaygroundCollision()
    {
        return this.platformLayer.checkCollision(this.spot);
    }
}
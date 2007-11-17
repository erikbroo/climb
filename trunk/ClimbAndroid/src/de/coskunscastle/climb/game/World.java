package de.coskunscastle.climb.game;

import android.graphics.Canvas;


/**
 * 
 */
final class World
{
    private static final int ROW_HEIGHT = 14;

    private static final int ROW_COUNT = 600;

    // private final LayerManager backgroundManager;
    //
    // private final LayerManager foregroundManagerLeft;
    //
    // private final LayerManager foregroundManagerRight;

    private int yPosition;

    final Water water;

    final Playground playGround;

    final Spot spot;

    World(Game game)
    {
        this.yPosition = 0;

        this.water = new Water(game, this.yPosition - 10);

        this.playGround = new Playground(game, this);
        this.spot = new Spot(game, this, 50, 50);

        // this.backgroundManager = new LayerManager();
        // this.foregroundManagerLeft = new LayerManager();
        // this.foregroundManagerRight = new LayerManager();
        // this.backgroundManager.append(new Background(World.ROW_COUNT,
        // GameControl.random));
        // this.foregroundManagerLeft.append(new Foreground(World.ROW_COUNT * 2,
        // GameControl.random));
        // this.foregroundManagerRight.append(new Foreground(World.ROW_COUNT *
        // 2, GameControl.random));

    }

    final void doDraw(Canvas canvas)
    {
        // this.backgroundManager.setViewWindow(0, this.yPosition / 2 + 785,
        // World.WORLD_VIEW_WIDTH, World.WORLD_VIEW_HEIGHT);
        // this.foregroundManagerLeft.setViewWindow(0, this.yPosition * 2 +
        // 3700,
        // 176, 208);
        // this.foregroundManagerRight.setViewWindow(0, this.yPosition * 2 +
        // 3700,
        // 176, 208);

        // this.backgroundManager.paint(canvas, 0, 0);
        this.playGround.doDraw(canvas);
        this.spot.doDraw(canvas);
        this.water.doDraw(canvas);
        // this.foregroundManagerLeft.paint(canvas, 0, 0);
        // this.foregroundManagerRight.paint(canvas, 161, 0);
    }

    final void doUpdate(long thisUpdate)
    {
        this.spot.doUpdate();
        this.water.doUpdate();

        final int spotScreenY = this.spot.getPosition().getVirtualScreenY();
        if (spotScreenY < 60) {
            viewup(6);
        } else if (spotScreenY < 80) {
            viewup(5);
        } else if (spotScreenY < 100) {
            viewup(4);
        } else if (spotScreenY < 120) {
            viewup(3);
        } else if (spotScreenY < 140) {
            viewup(2);
        } else if (spotScreenY < 180) {
            viewup(1);
        }
    }

    final void viewup(int i)
    {
        this.yPosition -= i;
    }

    final int getViewY()
    {
        return this.yPosition;
    }
}
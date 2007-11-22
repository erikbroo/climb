package com.googlecode.climb.game;

import android.content.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import com.googlecode.climb.R;
import com.googlecode.climb.game.utils.Sprite;
import com.googlecode.climb.game.utils.Vector2;


final class Water
{
    private final static int WATER_WIDTH = Game.VIRTUAL_CANVAS_WIDTH;

    private final Vector2 position;

    private final PlatformLayer platformLayer;

    private int waterSpeedLevel = 0;

    private final int waterSpeedMultiplikator = 0;

    private boolean waterSpeedMultiToggle;

    private final Sprite waveSprite;

    private long lastUpdateTime;

    private final Paint waterPaint = new Paint();
    {
        this.waterPaint.setStyle(Style.FILL);
        this.waterPaint.setColor(Color.CYAN);
        this.waterPaint.setAlpha(125);
    }

    private int waveToggle;

    private boolean waterRiseToggle;

    Water(Resources resources, PlatformLayer platformLayer)
    {
        this.platformLayer = platformLayer;
        this.position = new Vector2(0, 10, Game.VIRTUAL_CANVAS_WIDTH, Game.VIRTUAL_CANVAS_HEIGHT, this.platformLayer);
        final Bitmap waveBitmap = BitmapFactory.decodeResource(resources,
                R.drawable.waterwaves);
        this.waveSprite = new Sprite(waveBitmap, 176, 5);
    }

    final void doUpdate()
    {
        final int screenY = this.position.getVirtualScreenY();
        if (this.position.getLayerY() < this.platformLayer.getViewY() - 20) {
            // the water will never be lower than 30 units below zero
            this.position.setLayerY(this.platformLayer.getViewY() - 20);
            return;
        }

        // water is rising depending on some factors:
        this.waterRiseToggle = !this.waterRiseToggle;
        if (this.waterRiseToggle) {
            this.position.add(0, this.waterSpeedLevel);
        }
    }

    final void doDraw(Canvas canvas)
    {
        int screenY = this.position.getVirtualScreenY();
        screenY = Math.max(0, screenY);

        if (screenY <= 208) {
            this.waveSprite.setPosition(0, screenY);
            this.waveSprite.doDraw(canvas);
            this.waveToggle += 1;
            this.waveToggle %= 5;
            if (this.waveToggle == 0) {
                this.waveSprite.nextFrame();
            }
        }
        screenY += 5;
        canvas.drawRect(0, screenY, Game.VIRTUAL_CANVAS_WIDTH,
                Game.VIRTUAL_CANVAS_HEIGHT, this.waterPaint);
    }

    final void setWaterSpeedLevel(int a)
    {
        this.waterSpeedLevel = a;
    }

    final void rise(int i)
    {
        this.position.add(0, i);
    }

    /**
     * @return
     */
    public Vector2 getPosition()
    {
        return this.position;
    }
}
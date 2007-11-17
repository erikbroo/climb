package de.coskunscastle.climb.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import de.coskunscastle.climb.R;
import de.coskunscastle.climb.game.utils.Sprite;


final class Water
{
    private final static int WATER_WIDTH = Game.VIRTUAL_CANVAS_WIDTH;

    private final Vector2 position;

    private final World world;

    private int waterSpeedLevel = 0;

    private int waterSpeedMultiplikator = 0;

    private boolean waterSpeedMultiToggle;

    private final Sprite waveSprite;

    private boolean waveToggle;

    private final Paint waterPaint = new Paint();
    {
        this.waterPaint.setStyle(Style.FILL);
        this.waterPaint.setColor(Color.CYAN);
        this.waterPaint.setAlpha(125);
    }

    Water(Game game, int initialAbsoluteYPosition)
    {
        this.world = game.world;
        this.position = new Vector2(0, initialAbsoluteYPosition, game.world);
        final Bitmap waveBitmap = BitmapFactory.decodeResource(
                game.getResources(), R.drawable.waterwaves);
        this.waveSprite = new Sprite(waveBitmap, 176, 5);
    }

    final void doUpdate()
    {
        final int screenY = this.position.getVirtualScreenY();
        if (this.position.getWorldY() > this.world.getViewY() + 15) {
            this.position.setWorldY(this.world.getViewY() - 10);
            return;
        }

        // water is rising depending on some factors:
        this.position.add(0, 1);
        this.position.add(0, this.waterSpeedLevel);
        this.waterSpeedMultiToggle = !this.waterSpeedMultiToggle;
        if (this.waterSpeedMultiToggle) {
            this.position.add(0, this.waterSpeedMultiplikator);
        }
    }

    final void doDraw(Canvas canvas)
    {
        int screenY = this.position.getVirtualScreenY();
        screenY = Math.max(0, screenY);

        if (screenY <= 208) {
            this.waveSprite.setPosition(0, screenY);
            this.waveSprite.doDraw(canvas);
            this.waveToggle = !this.waveToggle;
            if (this.waveToggle) {
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

    final void setWaterSpeedMultiplikator(int a)
    {
        this.waterSpeedMultiplikator = a;
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
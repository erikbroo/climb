package com.googlecode.climb.game;

import android.content.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import com.googlecode.climb.R;
import com.googlecode.climb.game.utils.ParallaxLayer;


/**
 *
 */
final class PlatformLayer extends ParallaxLayer
{
    private final static float PlatformLayer_DEPTH = 1.0f;

    private final static int WOODSIGN_BITMAP_HEIGHT = 17;

    private final Paint text_paint = new Paint();
    {
        this.text_paint.setColor(Color.rgb(65, 65, 0));
        this.text_paint.setTypeface(Typeface.create(Typeface.MONOSPACE,
                Typeface.BOLD));
        this.text_paint.setTextSize(12);
    }

    private final Bitmap platform_30_image;

    private final Bitmap platform_50_image;

    private final Bitmap platform_70_image;

    private final Bitmap platform_full_image;

    private final Bitmap woodsign_small_image;

    private final Bitmap woodsign_medium_image;

    private final Bitmap woodsign_large_image;

    private final PlatformSequence platformSequence;

    PlatformLayer(Resources resources)
    {
        super(PlatformLayer.PlatformLayer_DEPTH, Game.VIRTUAL_CANVAS_WIDTH, Game.VIRTUAL_CANVAS_HEIGHT);

        this.platformSequence = new PlatformSequence(this);

        this.platform_30_image = BitmapFactory.decodeResource(resources,
                R.drawable.platform_30);
        this.platform_50_image = BitmapFactory.decodeResource(resources,
                R.drawable.platform_50);
        this.platform_70_image = BitmapFactory.decodeResource(resources,
                R.drawable.platform_70);
        this.platform_full_image = BitmapFactory.decodeResource(resources,
                R.drawable.platform_full);
        this.woodsign_small_image = BitmapFactory.decodeResource(resources,
                R.drawable.woodshield_small);
        this.woodsign_medium_image = BitmapFactory.decodeResource(resources,
                R.drawable.woodshield_med);
        this.woodsign_large_image = BitmapFactory.decodeResource(resources,
                R.drawable.woodshield_big);
    }

    final void doDraw(Canvas canvas)
    {
        for (int i = 0; i < PlatformSequence.VISIBLE_PLATFORM_COUNT; i++) {

            final Platform platform = this.platformSequence.getPlatform(i);

            final int width = platform.getWidth();
            final int height = Platform.PLATFORM_HEIGHT;
            final int xPos = platform.getPosition().getVirtualScreenX();
            final int yPos = platform.getPosition().getVirtualScreenY();

            // platform:
            if (width == 70) { // longest platforms
                canvas.drawBitmap(this.platform_70_image, xPos, yPos, null);
            } else if (width == 50) {
                canvas.drawBitmap(this.platform_50_image, xPos, yPos, null);
            } else if (width == 30) {
                canvas.drawBitmap(this.platform_30_image, xPos, yPos, null);
            } else if (width == 145) {
                canvas.drawBitmap(this.platform_full_image, xPos, yPos, null);
            } else {
                throw new IllegalStateException("invalid platform width: "
                        + width);
            }

            // wood signs:
            final int absolutePlatformIndex = platform.getAbsoluteIndex();

            if (absolutePlatformIndex == 0) { // base platform
                canvas.drawBitmap(this.woodsign_small_image, xPos + width - 30,
                        yPos - WOODSIGN_BITMAP_HEIGHT, null);
                canvas.drawText("1", xPos + width - 25, yPos - 5,
                        this.text_paint);
            } else if (absolutePlatformIndex % 10 == 0) {
                if (absolutePlatformIndex < 100) {
                    canvas.drawBitmap(this.woodsign_medium_image, xPos + width
                            - 30, yPos - WOODSIGN_BITMAP_HEIGHT, null);
                    canvas.drawText(Integer.toString(absolutePlatformIndex),
                            xPos + width - 22, yPos - 5, this.text_paint);
                } else {
                    canvas.drawBitmap(this.woodsign_large_image, xPos + width
                            - 30, yPos - WOODSIGN_BITMAP_HEIGHT, null);
                    canvas.drawText(Integer.toString(absolutePlatformIndex),
                            xPos + width - 25, yPos - 7, this.text_paint);
                }
            }
        }
    }

    final void doUpdate()
    {
        this.platformSequence.updateWorldview(getViewY());
    }

    /**
     * Checks whether spot collides with a platform in this frame. Returns the
     * platform's absolute index, if a collision is detected.
     * 
     * @param spot
     * @return
     */
    int checkCollision(Spot spot)
    {
        return this.platformSequence.getCollidingPlatform(spot);
    }

    /**
     * @param comboPlatform
     * @return
     */
    final int getScreenY(int platformIndex)
    {
        final int worldY = PlatformSequence.LOWEST_PLATFORM_YPOS
                + platformIndex * PlatformSequence.PLATFORM_DISTANCE;

        return Game.VIRTUAL_CANVAS_HEIGHT - (worldY - getViewY());
    }
}

package com.googlecode.climb.game;

import android.content.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import com.googlecode.climb.R;


/**
 *
 */
final class Playground
{
    private final Paint text_paint = new Paint();
    {
        this.text_paint.setColor(Color.BLACK);
        this.text_paint.setTypeface(Typeface.MONOSPACE);
        this.text_paint.setTextSize(10);
    }

    private final Bitmap platform_30_image;

    private final Bitmap platform_50_image;

    private final Bitmap platform_70_image;

    private final Bitmap platform_full_image;

    private final Bitmap woodshield_small_image;

    private final Bitmap woodshield_medium_image;

    private final Bitmap woodshield_large_image;

    private final World world;

    private final PlatformSequence platformSequence;

    Playground(Game game, World world)
    {
        this.world = world;
        this.platformSequence = new PlatformSequence(this.world);

        final Resources resources = game.getResources();
        this.platform_30_image = BitmapFactory.decodeResource(resources,
                R.drawable.platform_30);
        this.platform_50_image = BitmapFactory.decodeResource(resources,
                R.drawable.platform_50);
        this.platform_70_image = BitmapFactory.decodeResource(resources,
                R.drawable.platform_70);
        this.platform_full_image = BitmapFactory.decodeResource(resources,
                R.drawable.platform_full);
        this.woodshield_small_image = BitmapFactory.decodeResource(resources,
                R.drawable.woodshield_small);
        this.woodshield_medium_image = BitmapFactory.decodeResource(resources,
                R.drawable.woodshield_med);
        this.woodshield_large_image = BitmapFactory.decodeResource(resources,
                R.drawable.woodshield_big);
    }

    final void doDraw(Canvas canvas)
    {
        for (int i = 0; i < this.platformSequence.visiblePlatformCount(); i++) {

            final Platform platform = this.platformSequence.getPlatform(i);

            final int width = platform.getWidth();
            final int height = platform.getHeight();
            final int xPos = platform.getPosition().getVirtualScreenX();
            final int yPos = platform.getPosition().getVirtualScreenY();

            if (width == 70) { // longest platforms
                canvas.drawBitmap(this.platform_70_image, xPos, yPos, null);
            } else if (width == 50) {
                canvas.drawBitmap(this.platform_50_image, xPos, yPos, null);
            } else if (width == 30) {
                canvas.drawBitmap(this.platform_30_image, xPos, yPos, null);
            } else if (width == 144) {
                canvas.drawBitmap(this.platform_full_image, xPos, yPos, null);
            } else {
                throw new IllegalStateException("invalid platform width: "
                        + width);
            }

            final int absolutePlatformIndex = platform.getAbsoluteIndex();

            if (absolutePlatformIndex == 0) { // base platform
                canvas.drawBitmap(this.woodshield_small_image, xPos + width
                        - 20, yPos + 1, null);
                canvas.drawText("0", xPos + width - 20, yPos - 14,
                        this.text_paint);
            } else if (absolutePlatformIndex % 10 == 0) {
                if (absolutePlatformIndex < 100) {
                    canvas.drawBitmap(this.woodshield_medium_image, xPos
                            + width - 17, yPos + 1, null);
                    canvas.drawText(Integer.toString(i), xPos + width - 17,
                            yPos - 14, this.text_paint);
                } else {
                    canvas.drawBitmap(this.woodshield_large_image, xPos + width
                            - 17, yPos + 1, null);
                    canvas.drawText(Integer.toString(i), xPos + width - 17,
                            yPos - 14, this.text_paint);
                }
            }
        }
    }

    final void doUpdate()
    {
        this.platformSequence.updateWorldview(this.world.getViewY());
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

}

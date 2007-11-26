package com.googlecode.saga;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;


/**
 * A partial reimplementation of the JavaME Sprite class.
 */
public class Sprite
{
    private final Bitmap bitmap;

    private final int numFrameColumns;

    private final int numFrameRows;

    private final int numFrames;

    private final int frameWidth;

    private final int frameHeight;

    private int currentFrame;

    private int xPosition;

    private int yPosition;

    private final Rect tempSrcRect = new Rect();

    private final Rect tempDstRect = new Rect();

    /**
     * @param resourceID
     * @param frameWidth
     * @param frameHeight
     * @param view
     */
    public Sprite(Bitmap bitmap, int frameWidth, int frameHeight)
    {
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;

        this.bitmap = bitmap;

        final int bitmapWidth = this.bitmap.width();
        final int bitmapHeight = this.bitmap.height();

        if (bitmapWidth % frameWidth != 0) {
            throw new IllegalArgumentException("frameWidth: must be factor of bitmap width ("
                    + frameWidth + "," + bitmapWidth + ")");
        }
        if (bitmapHeight % frameHeight != 0) {
            throw new IllegalArgumentException("frameHeight: must be factor of bitmap height ("
                    + frameHeight + "," + bitmapHeight + ")");
        }

        this.numFrameColumns = bitmapWidth / frameWidth;
        this.numFrameRows = bitmapHeight / frameHeight;
        this.numFrames = this.numFrameColumns * this.numFrameRows;
    }

    /**
     * @param i
     */
    public void setFrame(int index)
    {
        if ((index < 0) || (index >= this.numFrames)) {
            throw new IllegalArgumentException("index(" + index + "): "
                    + "must be greater than 0 and less than "
                    + "number of frames(" + this.numFrames + ").");
        }

        this.currentFrame = index;
    }

    /**
     * @param i
     * @param j
     */
    public void setPosition(int x, int y)
    {
        this.xPosition = x;
        this.yPosition = y;
    }

    /**
     * @param canvas
     */
    public void doDraw(Canvas canvas)
    {
        final int height = this.frameHeight;
        final int width = this.frameWidth;
        final int dstX = this.xPosition;
        final int dstY = this.yPosition;
        final int srcX = calculateBitmapX(this.currentFrame);
        final int srcY = calculateBitmapY(this.currentFrame);

        this.tempSrcRect.set(srcX, srcY, srcX + width, srcY + height);
        this.tempDstRect.set(dstX, dstY, dstX + width, dstY + height);

        canvas.drawBitmap(this.bitmap, this.tempSrcRect, this.tempDstRect, null);
    }

    /**
     * Calculates and returns the frame's x coordinate in this sprites
     * underlying bitmap.
     * 
     * @return
     */
    private final int calculateBitmapX(int frame)
    {
        return this.frameWidth * (frame % this.numFrameColumns);
    }

    /**
     * Calculates and returns the frame's y coordinate in this sprites
     * underlying bitmap.
     * 
     * @return
     */
    private int calculateBitmapY(int frame)
    {
        return this.frameHeight * (frame / this.numFrameColumns);
    }

    /**
     * 
     */
    public void nextFrame()
    {
        this.currentFrame += 1;
        this.currentFrame %= this.numFrames;
    }

}

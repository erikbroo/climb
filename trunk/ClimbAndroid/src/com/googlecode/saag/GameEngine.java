package com.googlecode.saga;

import android.content.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;


/**
 * 
 */
public abstract class GameEngine
{
    private final int ups;

    private final int desiredResolutionWidth;

    private final int desiredResolutionHeight;

    private final int desiredResolutionRatio;

    private int realResolutionWidth;

    private int realResolutionHeight;

    private int realResolutionRatio;

    private long lastUpdateNanos;

    private Resources resources;

    /**
     * Creates a new game engine with specified view resolution and logic update
     * rate. Note that the specified resolution is not really reflected on the
     * screen device, but is used to transform the view canvas such as to
     * simulate the specified resolution. The update rate is specified in
     * updates per second.
     * 
     * @param width
     *            desired resolution width
     * @param height
     *            desired resolution height
     * @param ups
     *            the logic update rate
     */
    protected GameEngine(int width, int height, int ups,
            GameEngineActivity activity)
    {
        this.ups = ups;
        this.desiredResolutionWidth = width;
        this.desiredResolutionHeight = height;
        this.desiredResolutionRatio = width / height;
        this.resources = activity.getResources();
        activity.registerEngine(this);
        this.lastUpdateNanos = System.nanoTime();
    }

    /**
     * Package private method called periodically by the view that is created by
     * this engine's game activity. The elapsed time since the last call to this
     * method is compared to the logic update rate of this game engine. If more
     * time has elapsed than is specified in this engine's logic update rate,
     * than the call is delegated to onUpdate() and onDraw(canvas).
     * 
     * @param canvas
     */
    final void delegateDraw(Canvas canvas)
    {
        final long thisNanos = System.nanoTime();
        final long elapsedNanos = thisNanos - this.lastUpdateNanos;
        final int nanosPerSec = 1000000000;
        final int nanosPerUpdate = nanosPerSec / 20;

        if (elapsedNanos > nanosPerUpdate) {
            this.lastUpdateNanos = thisNanos;

            // final float interpolation = elapsedMillis / millisPerUpdate;

            onUpdate();
        }

        canvas.drawRGB(0, 0, 0);
        transformCanvas(canvas);
        onDraw(canvas);
    }

    /**
     * Transforms the canvas such that this engine's desired resolution is
     * simulated. This is achieved by scaling the canvas to the appropriate
     * size. The aspect ratio of the desired resolution is preserved. If the
     * aspect ratio of the desired resolution is different to the aspect ratio
     * of the canvas, than the canvas will be translated to the center of the
     * screen.
     * 
     * @param canvas
     */
    private final void transformCanvas(Canvas canvas)
    {
        final float scale;
        final float translateX;
        final float translateY;
        if (this.realResolutionRatio > this.desiredResolutionRatio) {
            scale = (float) this.realResolutionHeight
                    / (float) this.desiredResolutionHeight;
            translateY = 0;
            translateX = ((this.realResolutionWidth - (scale * this.desiredResolutionWidth)) / 2);
        } else {
            scale = (float) this.realResolutionWidth
                    / (float) this.desiredResolutionWidth;
            translateX = 0;
            translateY = ((this.realResolutionHeight - (scale * this.desiredResolutionHeight)) / 2);
        }

        canvas.translate(translateX, translateY);
        canvas.scale(scale, scale);

        canvas.clipRect(0, 0, this.desiredResolutionWidth,
                this.desiredResolutionHeight);
    }

    /**
     * Package private method called when this engine's view size changes.
     * 
     * @param width
     * @param height
     */
    final void onSizeChanged(int width, int height)
    {
        if ((width < this.desiredResolutionWidth)
                || (height < this.desiredResolutionHeight)) {
            throw new IllegalStateException("This game cannot be played on a screen resolution less than "
                    + this.desiredResolutionWidth
                    + "*"
                    + this.desiredResolutionHeight);
        }

        this.realResolutionWidth = width;
        this.realResolutionHeight = height;
        this.realResolutionRatio = width / height;
    }

    /**
     * Package private method called by a game activity, when this engine is
     * registered to it.
     * 
     * @param resources
     */
    final void setResources(Resources resources)
    {
        this.resources = resources;
    }

    protected abstract void onUpdate();

    protected abstract void onDraw(Canvas canvas);

    protected void onPause()
    {

    }

    protected void onResume()
    {

    }

    protected void onSaveState(Bundle state)
    {

    }

    protected void onLoadState(Bundle state)
    {

    }

    /**
     * Called whenever the user releases a key. The argument specifies the key
     * id for the released key. You may override this method but should make a
     * call to super for this engine instance to remain functioning.
     * 
     * @param keyId
     *            the key id of the released key
     */
    protected void onKeyUp(int keyId)
    {

    }

    /**
     * Called whenever the user presses a key. The argument specifies the key id
     * for the pressed key. You may override this method but should make a call
     * to super for this engine instance to remain functioning.
     * 
     * @param keyId
     *            the key id of the pressed key
     */
    protected void onKeyDown(int keyId)
    {

    }

    /**
     * Returns whether the key with the given id is currently pressed.
     * 
     * @param keyId
     *            one of the key ids defined in android.view.KeyEvent
     * @return whether the key is pressed
     */
    // public final boolean isPressed(int keyId)
    // {
    // return this.pressedKeys.get(keyId);
    // }
    public final Bitmap getBitmap(int resourceId)
    {
        return BitmapFactory.decodeResource(this.resources, resourceId);
    }
}

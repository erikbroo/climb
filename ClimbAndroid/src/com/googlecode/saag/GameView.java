package com.googlecode.saga;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.FrameLayout;


/**
 * Package private class used by GameActivity. Most of the callback methods of
 * this class are delegated to the game engine registered to the game activity.
 */
final class GameView extends FrameLayout
{

    private final GameEngine gameEngine;

    GameView(Context context, GameEngine engine)
    {
        super(context);
        this.gameEngine = engine;
    }

    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);

        this.gameEngine.delegateDraw(canvas);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        this.gameEngine.onSizeChanged(w, h);
        invalidate();
    }
}

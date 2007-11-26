package com.googlecode.saga;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;


/**
 * Represents an android activity capable of registering a game engine. Use the
 * registerEngine(GameEngine) method for registering the engine.
 */
public class GameEngineActivity extends Activity
{

    private GameView view;

    private GameEngine engine;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * Registers the specified game engine to this game activity. The content
     * view of this activity will be set to a new view. Drawing and update
     * methods of the specified engine will be called periodically according to
     * the logic update rate of the specified engine.
     * 
     * @param engine
     *            the game engine to register to this game activity
     */
    final void registerEngine(GameEngine engine)
    {
        this.engine = engine;
        this.view = new GameView(this, engine);
        setContentView(this.view);
        this.engine.setResources(getResources());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        final boolean result = super.onKeyDown(keyCode, event);

        this.engine.onKeyDown(keyCode);

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        final boolean result = super.onKeyUp(keyCode, event);

        this.engine.onKeyUp(keyCode);

        return result;
    }
}

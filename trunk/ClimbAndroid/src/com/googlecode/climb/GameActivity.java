package com.googlecode.climb;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import com.googlecode.climb.game.Game;
import com.googlecode.saga.GameEngineActivity;


/**
 * 
 */
public class GameActivity extends GameEngineActivity
{
    public static final String RESULT_SCORE = "Score";

    public static final String RESULT_PLATFORM = "Platform";

    // private final OnClickListener resumeButtonListener = new
    // OnClickListener() {
    // @Override
    // public void onClick(DialogInterface dialog, int arg)
    // {
    // if ((GameActivity.this.game != null)
    // && GameActivity.this.game.isPaused()) {
    // GameActivity.this.game.resumeGameLogic();
    // }
    // }
    // };
    //
    // private final Runnable resumeHandler = new Runnable() {
    // @Override
    // public void run()
    // {
    // AlertDialog.show(GameActivity.this, "", "Game Paused", "Resume",
    // GameActivity.this.resumeButtonListener, false, null);
    // }
    // };

    private Game game;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(final Bundle icicle)
    {
        super.onCreate(icicle);

        final SharedPreferences prefs = getSharedPreferences(
                SettingsActivity.KEY_SETTINGS, Context.MODE_PRIVATE);
        final int keyJump = prefs.getInt(SettingsActivity.JUMP_KEY_SETTING,
                KeyEvent.KEYCODE_1);
        final int keyLeft = prefs.getInt(SettingsActivity.LEFT_KEY_SETTING,
                KeyEvent.KEYCODE_DPAD_LEFT);
        final int keyRight = prefs.getInt(SettingsActivity.RIGHT_KEY_SETTING,
                KeyEvent.KEYCODE_DPAD_RIGHT);

        this.game = new Game(this);
        this.game.setKeys(keyJump, keyLeft, keyRight);
        this.game.startGameLogic(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPause()
    {
        super.onPause();

        if (this.game != null) {
            this.game.pauseGameLogic();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onResume()
    {
        super.onResume();

        if ((this.game == null) || !this.game.isPaused()) {
            return;
        }

        // new Handler().post(this.resumeHandler);
        this.game.resumeGameLogic();
    }

    /**
     * Callback method. Called by the game, when it is finished.
     */
    public void onGameFinished()
    {
        final int score = this.game.getTotalScore();
        final int platform = this.game.getHighestTouchedPlatform();

        final Bundle result = new Bundle();
        result.putInteger(GameActivity.RESULT_SCORE, score);
        result.putInteger(GameActivity.RESULT_PLATFORM, platform);
        setResult(Activity.RESULT_OK, null, result);
        finish();
    }

    /**
     * Callback Method. {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        final boolean result = super.onCreateOptionsMenu(menu);
        final int resourceID;

        // resourceID = R.string.menu_label_pause;
        // menu.add(0, 0, resourceID, new MenuActionListener(resourceID));

        return result;
    }

    private class MenuActionListener implements Runnable
    {
        private final int id;

        MenuActionListener(int menuResourceID)
        {
            this.id = menuResourceID;
        }

        @Override
        public void run()
        {
            switch (this.id) {
                case R.string.menu_label_pause:
                    // if (GameActivity.this.game != null) {
                    // game.pauseGameLogic();
                    // }
                    break;
            }
        }
    }
}

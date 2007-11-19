package com.googlecode.climb;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.Window;
import com.googlecode.climb.game.Game;


/**
 * 
 */
public class GameActivity extends Activity
{
    private static final String RESULT_SCORE = "Score";

    private static final String RESULT_PLATFORM = "Platform";

    private Game game;

    /**
     * Callback method. {@inheritDoc}
     */
    @Override
    public void onCreate(final Bundle icicle)
    {
        super.onCreate(icicle);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.game_layout);

        final SharedPreferences prefs = getSharedPreferences(
                SettingsActivity.KEY_SETTINGS, Context.MODE_PRIVATE);
        final int keyJump = prefs.getInt(SettingsActivity.JUMP_KEY_SETTING,
                KeyEvent.KEYCODE_1);
        final int keyLeft = prefs.getInt(SettingsActivity.LEFT_KEY_SETTING,
                KeyEvent.KEYCODE_DPAD_LEFT);
        final int keyRight = prefs.getInt(SettingsActivity.RIGHT_KEY_SETTING,
                KeyEvent.KEYCODE_DPAD_RIGHT);

        this.game = (Game) findViewById(R.id.game_view);
        this.game.setKeys(keyJump, keyLeft, keyRight);
        this.game.startGameLogic(this);
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
        this.game = null;
        finish();
    }

    /**
     * Callback Method. {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        final boolean result = super.onCreateOptionsMenu(menu);
        int resourceID;

        resourceID = R.string.menu_label_close;
        menu.add(0, 0, resourceID, new MenuActionListener(resourceID));

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
                case R.string.menu_label_close:
                    GameActivity.this.game.forceQuit();
                    GameActivity.this.game = null;
                    GameActivity.this.setResult(Activity.RESULT_CANCELED);
                    GameActivity.this.finish();
                    break;
            }
        }
    }
}

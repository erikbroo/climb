package de.coskunscastle.climb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import de.coskunscastle.climb.game.Game;


public class MainActivity extends Activity
{
    /**
     * Subactivity request code for the splash screen.
     */
    private final static int SUBACT_SPLASH = 0;

    /**
     * Subactivity request code for the game.
     */
    private final static int SUBACT_GAME = 1;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(final Bundle icicle)
    {
        super.onCreate(icicle);

        // requestWindowFeature(Window.FEATURE_OPTIONS_PANEL);
        // requestWindowFeature(Window.FEATURE_LEFT_ICON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_layout);

        startSplash();
    }

    /**
     * Callback Method. {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        final boolean result = super.onCreateOptionsMenu(menu);
        int resourceID;

        resourceID = R.string.menu_label_newgame;
        menu.add(0, 0, resourceID, new MenuActionListener(resourceID));

        resourceID = R.string.menu_label_settings;
        menu.add(0, 0, resourceID, new MenuActionListener(resourceID));

        resourceID = R.string.menu_label_help;
        menu.add(0, 0, resourceID, new MenuActionListener(resourceID));

        resourceID = R.string.menu_label_highscore;
        menu.add(0, 0, resourceID, new MenuActionListener(resourceID));

        resourceID = R.string.menu_label_credits;
        menu.add(0, 0, resourceID, new MenuActionListener(resourceID));

        resourceID = R.string.menu_label_close;
        menu.add(0, 0, resourceID, new MenuActionListener(resourceID));

        return result;
    }

    /**
     * Starts the splash screen as a subactivity.
     */
    private void startSplash()
    {
        final Intent splashScreenIntent = new Intent();
        splashScreenIntent.setClass(this, SplashScreenActivity.class);
        startSubActivity(splashScreenIntent, MainActivity.SUBACT_SPLASH);
    }

    /**
     * Starts the game as a subactivity.
     */
    private void startGame()
    {
        final Intent gameIntent = new Intent();
        gameIntent.setClass(this, Game.class);
        startSubActivity(gameIntent, MainActivity.SUBACT_GAME);
    }

    /**
     * Starts the highscore as a subactivity.
     */
    private void startHighscore()
    {
    }

    /**
     * Called when subactivities finish.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, String data,
            Bundle extras)
    {
        if (requestCode == MainActivity.SUBACT_GAME) {
            startHighscore();
        }
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
                case R.string.menu_label_newgame:
                    startGame();
                    break;
                case R.string.menu_label_close:
                    MainActivity.this.finish();
                    break;
            }
        }
    }
}

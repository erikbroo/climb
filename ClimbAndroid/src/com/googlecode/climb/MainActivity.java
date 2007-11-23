package com.googlecode.climb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;


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
     * Subactivity request code for settings.
     */
    private static final int SUBACT_SETTINGS = 2;

    /**
     * Subactivity request code for highscore.
     */
    private static final int SUBACT_HIGHSCORE = 3;

    /**
     * Subactivity request code for credits.
     */
    private static final int SUBACT_CREDITS = 4;

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
        gameIntent.setClass(this, GameActivity.class);
        startSubActivity(gameIntent, MainActivity.SUBACT_GAME);
    }

    /**
     * Starts the settings subactivity.
     */
    private void startSettings()
    {
        final Intent settingsIntent = new Intent();
        settingsIntent.setClass(this, SettingsActivity.class);
        startSubActivity(settingsIntent, MainActivity.SUBACT_SETTINGS);
    }

    /**
     * Starts the credits as a subactivity.
     */
    private void startCredits()
    {
        final Intent intent = new Intent();
        intent.setClass(this, CreditsActivity.class);
        startSubActivity(intent, MainActivity.SUBACT_CREDITS);
    }

    /**
     * Starts the highscore as a subactivity.
     */
    private void startHighscore()
    {
        final Intent intent = new Intent();
        intent.setClass(this, HighscoreActivity.class);
        startSubActivity(intent, MainActivity.SUBACT_HIGHSCORE);
    }

    /**
     * Called when subactivities finish.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, String data,
            Bundle extras)
    {
        switch (requestCode) {
            case SUBACT_GAME:
                // startHighscore();
                break;
            default:
                break;
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
                case R.string.menu_label_settings:
                    startSettings();
                    break;
                case R.string.menu_label_credits:
                    startCredits();
                    break;
                case R.string.menu_label_highscore:
                    startHighscore();
                    break;
                case R.string.menu_label_close:
                    MainActivity.this.finish();
                    break;
            }
        }
    }
}

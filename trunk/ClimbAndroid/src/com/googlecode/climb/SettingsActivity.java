package com.googlecode.climb;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;


public class SettingsActivity extends Activity implements OnClickListener
{
    public static final String KEY_SETTINGS = "Climb.Settings.Keys";

    public static final String JUMP_KEY_SETTING = "Climb.Setting.Key.Jump";

    public static final String LEFT_KEY_SETTING = "Climb.Setting.Key.Left";

    public static final String RIGHT_KEY_SETTING = "Climb.Setting.Key.Right";

    private static final String LOG_TAG = "SettingsActivity";

    private Button jumpButton;

    private Button leftButton;

    private Button rightButton;

    private Button currentSelectedButton = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(final Bundle icicle)
    {
        super.onCreate(icicle);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.settings_layout);

        this.jumpButton = (Button) findViewById(R.id.button_jump);
        this.leftButton = (Button) findViewById(R.id.button_left);
        this.rightButton = (Button) findViewById(R.id.button_right);

        this.jumpButton.setOnClickListener(this);
        this.leftButton.setOnClickListener(this);
        this.rightButton.setOnClickListener(this);
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

    /**
     * Called when a button is clicked.
     */
    @Override
    public void onClick(View clickedView)
    {
        final Button clickedButton = (Button) clickedView;

        clickedButton.setText(R.string.settings_button_label_press);
        this.currentSelectedButton = clickedButton;

        this.jumpButton.setFocusable(false);
        this.leftButton.setFocusable(false);
        this.rightButton.setFocusable(false);
    }

    /**
     * Called when a key is pressed.
     */
    @Override
    public boolean onKeyDown(int id, KeyEvent event)
    {
        final SharedPreferences prefs = getSharedPreferences(
                SettingsActivity.KEY_SETTINGS, Context.MODE_PRIVATE);

        if (this.currentSelectedButton == this.jumpButton) {
            Log.i(SettingsActivity.LOG_TAG, "Setting jump key to id: " + id);
            prefs.edit().putInt(SettingsActivity.JUMP_KEY_SETTING, id).commit();
            this.jumpButton.setText(R.string.settings_button_label_jump);
        } else if (this.currentSelectedButton == this.leftButton) {
            Log.i(SettingsActivity.LOG_TAG, "Setting left key to id: " + id);
            prefs.edit().putInt(SettingsActivity.LEFT_KEY_SETTING, id).commit();
            this.leftButton.setText(R.string.settings_button_label_left);
        } else if (this.currentSelectedButton == this.rightButton) {
            Log.i(SettingsActivity.LOG_TAG, "Setting right key to id: " + id);
            prefs.edit().putInt(SettingsActivity.RIGHT_KEY_SETTING, id).commit();
            this.rightButton.setText(R.string.settings_button_label_right);
        }

        this.currentSelectedButton = null;

        this.jumpButton.setFocusable(true);
        this.leftButton.setFocusable(true);
        this.rightButton.setFocusable(true);

        return false;
    }

    private final class MenuActionListener implements Runnable
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
                    SettingsActivity.this.finish();
                    break;
            }
        }
    }
}

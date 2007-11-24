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
import android.widget.TextView;


public class SettingsActivity extends Activity implements OnClickListener
{
    public static final String KEY_SETTINGS = "Climb.Settings.Keys";

    public static final String JUMP_KEY_SETTING = "Climb.Setting.Key.Jump";

    public static final String LEFT_KEY_SETTING = "Climb.Setting.Key.Left";

    public static final String RIGHT_KEY_SETTING = "Climb.Setting.Key.Right";

    private static final String LOG_TAG = "SettingsActivity";

    private SharedPreferences prefs;

    private Button jumpButton;

    private Button leftButton;

    private Button rightButton;

    private String currentSelectedKey = null;

    private TextView jumpAssignment;

    private TextView leftAssignment;

    private TextView rightAssignment;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(final Bundle icicle)
    {
        super.onCreate(icicle);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.settings_layout);

        this.prefs = getSharedPreferences(SettingsActivity.KEY_SETTINGS,
                Context.MODE_PRIVATE);

        this.jumpButton = (Button) findViewById(R.id.button_jump);
        this.leftButton = (Button) findViewById(R.id.button_left);
        this.rightButton = (Button) findViewById(R.id.button_right);

        this.jumpButton.setOnClickListener(this);
        this.leftButton.setOnClickListener(this);
        this.rightButton.setOnClickListener(this);

        final String jumpKeyString = KeyId2String.map(this.prefs.getInt(
                JUMP_KEY_SETTING, KeyEvent.KEYCODE_1));
        final String leftKeyString = KeyId2String.map(this.prefs.getInt(
                LEFT_KEY_SETTING, KeyEvent.KEYCODE_DPAD_LEFT));
        final String rightKeyString = KeyId2String.map(this.prefs.getInt(
                RIGHT_KEY_SETTING, KeyEvent.KEYCODE_DPAD_RIGHT));

        this.jumpAssignment = (TextView) findViewById(R.id.text_assignment_jump);
        this.leftAssignment = (TextView) findViewById(R.id.text_assignment_left);
        this.rightAssignment = (TextView) findViewById(R.id.text_assignment_right);

        this.jumpAssignment.setText(jumpKeyString);
        this.leftAssignment.setText(leftKeyString);
        this.rightAssignment.setText(rightKeyString);
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

        if (clickedButton == this.jumpButton) {
            this.currentSelectedKey = JUMP_KEY_SETTING;
            this.jumpAssignment.setText(R.string.settings_button_label_press);
        }
        if (clickedButton == this.leftButton) {
            this.currentSelectedKey = LEFT_KEY_SETTING;
            this.leftAssignment.setText(R.string.settings_button_label_press);
        }
        if (clickedButton == this.rightButton) {
            this.currentSelectedKey = RIGHT_KEY_SETTING;
            this.rightAssignment.setText(R.string.settings_button_label_press);
        }

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
        super.onKeyDown(id, event);

        if (this.currentSelectedKey == JUMP_KEY_SETTING) {
            Log.i(SettingsActivity.LOG_TAG, "Setting jump key to id: " + id);
            this.prefs.edit().putInt(SettingsActivity.JUMP_KEY_SETTING, id).commit();
            this.jumpAssignment.setText(KeyId2String.map(id));
        } else if (this.currentSelectedKey == LEFT_KEY_SETTING) {
            Log.i(SettingsActivity.LOG_TAG, "Setting left key to id: " + id);
            this.prefs.edit().putInt(SettingsActivity.LEFT_KEY_SETTING, id).commit();
            this.leftAssignment.setText(KeyId2String.map(id));
        } else if (this.currentSelectedKey == RIGHT_KEY_SETTING) {
            Log.i(SettingsActivity.LOG_TAG, "Setting right key to id: " + id);
            this.prefs.edit().putInt(SettingsActivity.RIGHT_KEY_SETTING, id).commit();
            this.rightAssignment.setText(KeyId2String.map(id));
        }

        this.currentSelectedKey = null;

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

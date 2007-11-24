package com.googlecode.climb;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class HighscoreActivity extends Activity
{
    private static final String LOG_TAG = "HighscoreActivity";

    /**
     * Default intent action for this activity. If no intent action is specified
     * by the activator, this one is assumed. This action lets the highscore
     * activity show the current highscore table.
     */
    public static final String VIEW_INTENT_ACTION = "com.googlecode.climb.highscore.view";

    /**
     * This intent action lets the highscore activity check for a new highscore.
     * If this action is specified, the intent extras SCORE_INTENT_EXTRA and
     * PLATFORM_INTENT_EXTRA must have been set by the activator of this
     * activity.
     */
    public static final String CHECK_INTENT_ACTION = "com.googlecode.climb.highscore.check";

    /**
     * Intent extra for the CHECK_INTENT_ACTION.
     */
    public static final String SCORE_INTENT_EXTRA = "com.googlecode.climb.highscore.extra.score";

    /**
     * Intent extra for the CHECK_INTENT_ACTION.
     */
    public static final String PLATFORM_INTENT_EXTRA = "com.googlecode.climb.highscore.extra.platform";

    /**
     * A constant key for the shared prefs, which stores highscore entries. You
     * must append a number to this constant to receive the real key. The number
     * will denote the row of the highscore table.
     */
    private static final String SHAREDPREF_NAME = "climb.highscore.sharedpref.name";

    /**
     * A constant key for the shared prefs, which stores highscore entries. You
     * must append a number to this constant to receive the real key. The number
     * will denote the row of the highscore table.
     */
    private static final String SHAREDPREF_SCORE = "climb.highscore.sharedpref.score";

    /**
     * A constant key for the shared prefs, which stores highscore entries. You
     * must append a number to this constant to receive the real key. The number
     * will denote the row of the highscore table.
     */
    private static final String SHAREDPREF_PLATFORM = "climb.highscore.sharedpref.platform";

    /**
     * Number of highscore entries
     */
    private static final int HIGHSCORE_ENTRIES = 5;

    private SharedPreferences prefs;

    private SharedPreferences.Editor prefsEditor;

    private int newScoreAt;

    private EditText newScoreInput;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(final Bundle icicle)
    {
        super.onCreate(icicle);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.prefs = getSharedPreferences(SettingsActivity.KEY_SETTINGS,
                Context.MODE_PRIVATE);
        this.prefsEditor = this.prefs.edit();

        if (CHECK_INTENT_ACTION.equals(getIntent().getAction())) {
            final Integer score = (Integer) getIntent().getExtra(
                    SCORE_INTENT_EXTRA);
            final Integer platform = (Integer) getIntent().getExtra(
                    PLATFORM_INTENT_EXTRA);
            if ((score == null) || (platform == null)) {
                throw new RuntimeException("Missing arguments for HighscoreActivity CHECK Action");
            }

            final boolean newScore = checkNewScore(score.intValue(),
                    platform.intValue());

            if (!newScore) {
                setResult(RESULT_CANCELED);
                finish();
                return;
            }
        }

        setContentView(R.layout.highscore_layout);
        fillHighscoreTable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPause()
    {
        super.onPause();

        if (this.newScoreInput == null) {
            return;
        }
        String newName = this.newScoreInput.getText().toString();
        if (newName.length() > 15) {
            newName = newName.substring(0, 13) + "...";
        }

        this.prefsEditor.putString(SHAREDPREF_NAME + this.newScoreAt, newName);
        this.prefsEditor.commit();
    }

    /**
     * Fills the highscore table view with the stored values.
     */
    private void fillHighscoreTable()
    {
        final TableLayout highscoreTable = (TableLayout) findViewById(R.id.highscore_table);

        final int numberOfRows = highscoreTable.getChildCount();
        // the first row is the header
        for (int entry = 1; entry < numberOfRows; entry++) {
            final String name = this.prefs.getString(SHAREDPREF_NAME + entry,
                    defaultName(entry));
            final int score = this.prefs.getInt(SHAREDPREF_SCORE + entry,
                    defaultScore(entry));
            final int platform = this.prefs.getInt(SHAREDPREF_PLATFORM + entry,
                    0);

            final TableRow row = (TableRow) highscoreTable.getChildAt(entry);
            final EditText nameView = (EditText) row.getChildAt(1);
            final TextView scoreView = (TextView) row.getChildAt(2);
            final TextView platformView = (TextView) row.getChildAt(3);

            nameView.setText(name);
            scoreView.setText(Integer.toString(score));
            platformView.setText(Integer.toString(platform));

            nameView.setMaxEms(15);
            if (this.newScoreAt == entry) {
                nameView.setText("enter name");
                nameView.setSingleLine();
                nameView.requestFocus();
                nameView.setSelection(0, 10);
                nameView.setBackgroundColor(Color.rgb(200, 255, 255));
                this.newScoreInput = nameView;
            } else {
                nameView.setBackgroundColor(Color.rgb(255, 255, 255));
                nameView.setInputMethod(null);
            }
        }
    }

    /**
     * Returns default names for highscore table rows.
     */
    private String defaultName(int i)
    {
        switch (i) {
            case 1:
                return "John";
            case 2:
                return "James";
            case 3:
                return "Sayid";
            case 4:
                return "Desmond";
            case 5:
                return "Jack";
            default:
                return "unknown";
        }
    }

    /**
     * Returns default scores for highscore table rows.
     */
    private int defaultScore(int i)
    {
        return 6 * 250 - i * 250;
    }

    /**
     * Checks whether the specified score is a new highscore. If so the score
     * will be added to the appropriate entry. The old score in this entry and
     * lower scores will be pushed down. Returns true, if the specified score is
     * a new highscore.
     * 
     * @param score
     *            the new score
     * @param platform
     *            the new platform
     */
    private boolean checkNewScore(int newScore, int newPlatform)
    {
        Log.i(LOG_TAG, "checking new score (" + newScore + "," + newPlatform
                + ")");
        final int numberOfEntries = HIGHSCORE_ENTRIES;

        for (int entry = numberOfEntries; entry >= 1; entry--) {
            final String name = this.prefs.getString(SHAREDPREF_NAME + entry,
                    defaultName(entry));
            final int score = this.prefs.getInt(SHAREDPREF_SCORE + entry,
                    defaultScore(entry));
            final int platform = this.prefs.getInt(SHAREDPREF_PLATFORM + entry,
                    0);

            Log.d(LOG_TAG, "entry " + entry + " stored score: " + score);
            if (newScore > score) {
                if (entry < 5) {
                    this.prefsEditor.putString(SHAREDPREF_NAME + (entry + 1),
                            name);
                    this.prefsEditor.putInt(SHAREDPREF_SCORE + (entry + 1),
                            score);
                    this.prefsEditor.putInt(SHAREDPREF_PLATFORM + (entry + 1),
                            platform);
                }
                this.newScoreAt = entry;
                this.prefsEditor.putString(SHAREDPREF_NAME + entry, "unknown");
                this.prefsEditor.putInt(SHAREDPREF_SCORE + entry, newScore);
                this.prefsEditor.putInt(SHAREDPREF_PLATFORM + entry,
                        newPlatform);
            } else if (entry == numberOfEntries) { // no new score
                Log.i(LOG_TAG, "no new score");
                return false;
            }
        }
        Log.i(LOG_TAG, "new score");
        this.prefsEditor.commit();
        return true;
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
                    HighscoreActivity.this.finish();
                    break;
            }
        }
    }
}

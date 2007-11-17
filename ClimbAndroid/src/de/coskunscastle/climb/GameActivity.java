package de.coskunscastle.climb;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import de.coskunscastle.climb.game.Game;


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
        this.game = (Game) findViewById(R.id.game_view);
        this.game.startGameLogic(this);
    }

    /**
     * Callback method. Called by the game, when it is finished.
     * 
     * @param totalScore
     *            the score of the player
     * @param highestTouchedPlatform
     *            the highest platform reached
     */
    public void onGameFinished(int totalScore, int highestTouchedPlatform)
    {
        final Bundle result = new Bundle();
        result.putInteger(GameActivity.RESULT_SCORE, totalScore);
        result.putInteger(GameActivity.RESULT_PLATFORM, highestTouchedPlatform);
        setResult(Activity.RESULT_OK, null, result);
        this.game = null;
        finish();
    }

}

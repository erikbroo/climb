package de.coskunscastle.climb.game;

import java.util.Map;
import java.util.Random;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import de.coskunscastle.climb.GameActivity;


/**
 * 
 */
public final class Game extends View
{
    private static final int STATE_WAITING = 0;

    private static final int STATE_RUNNING = 1;

    private static final int STATE_PAUSED = 2;

    private static final int STATE_GAMEOVER_SEQUENCE = 3;

    private static final int STATE_FINISHED = 4;

    private static final long LOGIC_UPDATE_RATE = 50000000; // 50 millisec

    static final Random RANDOM = new Random();

    /**
     * Climb 2 assumes a rendering canvas height of 208 pixels. All logic and
     * rendering operations use this value. At the end the rendering is scaled
     * to fit the real screen dimensions.
     */
    static final int VIRTUAL_CANVAS_HEIGHT = 208;

    /**
     * Climb 2 assumes a rendering canvas width of 176 pixels. All logic and
     * rendering operations use this value. At the end the rendering is scaled
     * to fit the real screen dimensions.
     */
    static final int VIRTUAL_CANVAS_WIDTH = 176;

    private int realScreenWidth;

    private int realScreenHeight;

    private boolean realScreenSizeChanged = false;

    private int keyJump = KeyEvent.KEYCODE_1;

    private int keyMoveLeft = KeyEvent.KEYCODE_DPAD_LEFT;

    private int keyMoveRight = KeyEvent.KEYCODE_DPAD_RIGHT;

    private boolean pressedJumpKey = false;

    private boolean pressedMoveLeftKey = false;

    private boolean pressedMoveRightKey = false;

    final World world;

    final ScorePanel scorePanel;

    final ComboMeter comboMeter;

    final MessagePopup messagePopup;

    private int highestTouchedPlatform;

    private int lastTouchedPlatform;

    private int currentCent;

    private int gameState = Game.STATE_WAITING;

    private long lastUpdate;

    private GameActivity activity;

    /**
     * I don't know what the arguments do, I need them for the super
     * constructor. Fortunately an instance of this class is created for me
     * automatically because of the game_layout.xml and the android manifest
     * file.
     * 
     * @param context
     * @param attrs
     * @param inflateParams
     * @param defStyle
     */
    public Game(Context context, AttributeSet attrs, Map inflateParams)
    {
        super(context, attrs, inflateParams);

        this.scorePanel = new ScorePanel(this);
        this.world = new World(this);
        this.messagePopup = new MessagePopup(this);
        this.comboMeter = new ComboMeter(this.messagePopup, this.world.water, this.scorePanel);
    }

    /**
     * {@inheritDoc}
     * 
     * @param key
     */
    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH)
    {
        this.realScreenWidth = w;
        this.realScreenHeight = h;

        this.realScreenSizeChanged = true;
        invalidate();
    }

    /**
     * {@inheritDoc}
     * 
     * @param key
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        super.onKeyDown(keyCode, event);

        this.pressedJumpKey = (keyCode == this.keyJump);
        this.pressedMoveLeftKey = (keyCode == this.keyMoveLeft);
        this.pressedMoveRightKey = (keyCode == this.keyMoveRight);

        return true;
    }

    public final void startGameLogic(GameActivity activity)
    {
        if (this.gameState != Game.STATE_WAITING) {
            throw new IllegalStateException();
        }

        this.activity = activity;

        this.messagePopup.registerMSG("Go Go Go", 0x22FF33);
        this.scorePanel.init();
        this.comboMeter.init();
    }

    public final void resumeGameLogic()
    {
    }

    public final void pauseGameLogic()
    {
    }

    public final void hideNotify()
    {
        this.paused = true;
        if (this.scorePanel != null) {
            this.scorePanel.pause();
        }
    }

    public final void showNotify()
    {
        if (this.paused) {
            this.paused = false;
            this.scorePanel.resume();
            this.comboMeter.resume();
        }
    }

    /**
     * Callback method. {@inheritDoc}
     */
    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        doUpdate();

        doDraw(canvas);

        if (this.gameState == Game.STATE_RUNNING) {
            invalidate();
        }
    }

    private final void doUpdate()
    {
        final long thisUpdate = System.nanoTime();
        if (thisUpdate - this.lastUpdate < Game.LOGIC_UPDATE_RATE) {
            return;
        }
        this.lastUpdate = thisUpdate;

        switch (this.gameState) {
            case STATE_WAITING:
                return;
            case STATE_RUNNING:

                this.world.doUpdate(thisUpdate);
                this.scorePanel.doUpdate(thisUpdate);
                this.comboMeter.doUpdate(thisUpdate);

                final int spotY = this.world.spot.getPosition().getWorldY();
                final int waterY = this.world.water.getPosition().getWorldY();
                if ((spotY < 0) || (spotY < waterY - 40)) {
                    this.gameState = Game.STATE_GAMEOVER_SEQUENCE;
                    this.messagePopup.registerMSG("Game Over", 0xFF5522);
                }
                break;
            case STATE_PAUSED:
                return;
            case STATE_GAMEOVER_SEQUENCE:
                final int waterScreenY = this.world.water.getPosition().getVirtualScreenX();
                if (waterScreenY < 0) {
                    this.gameState = Game.STATE_FINISHED;
                }
                this.world.water.rise(10);
                this.world.spot.getPosition().add(0, -1);
                break;
            case STATE_FINISHED:
                this.activity.onGameFinished(this.scorePanel.getTotalScore(),
                        this.highestTouchedPlatform);
                return;
            default:
                throw new IllegalStateException("BUG");
        }
    }

    private final void doDraw(Canvas canvas)
    {
        if (this.realScreenSizeChanged) {
            this.realScreenSizeChanged = false;

            final Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setStyle(Style.FILL);
            canvas.drawRect(0, 0, this.realScreenWidth, this.realScreenHeight,
                    paint);
            if ((this.realScreenWidth > Game.VIRTUAL_CANVAS_WIDTH)
                    && (this.realScreenHeight > Game.VIRTUAL_CANVAS_HEIGHT)) {
                canvas.translate(
                        (this.realScreenWidth - Game.VIRTUAL_CANVAS_WIDTH) / 2,
                        (this.realScreenHeight - Game.VIRTUAL_CANVAS_HEIGHT) / 2);

            }
            canvas.clipRect(0, 0, Game.VIRTUAL_CANVAS_WIDTH,
                    Game.VIRTUAL_CANVAS_HEIGHT);
        }

        this.world.doDraw(canvas);
        this.scorePanel.doDraw(canvas);
        this.comboMeter.doDraw(canvas);
        this.messagePopup.doDraw(canvas);
    }

    /**
     * Called when the ball jumps.
     */
    final void onSpotJumped()
    {
        this.comboMeter.onSpotJumped(this.lastTouchedPlatform);
    }

    /**
     * Called when the ball lands on the specified platform.
     * 
     * @param platform
     */
    final void onSpotLanded(int platform)
    {
        this.highestTouchedPlatform = (short) Math.max(
                this.highestTouchedPlatform, platform);
        this.lastTouchedPlatform = platform;

        this.comboMeter.onSpotLanded(platform);

        if ((platform % 100) < 5) {
            if ((platform / 100) > this.currentCent) {
                this.world.water.setWaterSpeedLevel(0);
                this.world.water.setWaterSpeedMultiplikator(0);
                this.currentCent++;
                this.scorePanel.resetTimer();
            }
        }
    }

    final void onSpotCollidedWall()
    {
        this.comboMeter.onSpotCollided();
    }

    public final void setKeys(int keyJump, int keyMoveLeft, int keyMoveRight)
    {
        this.keyJump = keyJump;
        this.keyMoveLeft = keyMoveLeft;
        this.keyMoveRight = keyMoveRight;
    }

    /**
     * Returns whether the jump key has been pressed by the user during the last
     * frame.
     * 
     * @return
     */
    public final boolean isJumpKeyPressed()
    {
        return this.pressedJumpKey;
    }

    /**
     * Returns whether the move-left key has been pressed by the user during the
     * last frame.
     * 
     * @return
     */
    public final boolean isMoveLeftKeyPressed()
    {
        return this.pressedMoveLeftKey;
    }

    /**
     * Returns whether the move-right key has been pressed by the user during
     * the last frame.
     * 
     * @return
     */
    public final boolean isMoveRightKeyPressed()
    {
        return this.pressedMoveRightKey;
    }
}
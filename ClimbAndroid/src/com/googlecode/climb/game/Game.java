package com.googlecode.climb.game;

import java.util.Map;
import java.util.Random;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import com.googlecode.climb.GameActivity;


/**
 * 
 */
public final class Game extends View
{
    private static final String LOG_TAG = "Game";

    private static final int STATE_WAITING = 0;

    private static final int STATE_RUNNING = 1;

    private static final int STATE_PAUSED = 2;

    private static final int STATE_GAMEOVER_SEQUENCE = 3;

    private static final int STATE_FINISHING = 4;

    private static final int STATE_FINISHED = 5;

    private static final long LOGIC_UPDATE_RATE = 50; // 50 millisec

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

    /**
     * Climb 2 assumes a rendering canvas ratio of 176 / 208.
     */
    static final float VIRTUAL_CANVAS_RATIO = Game.VIRTUAL_CANVAS_WIDTH
            / Game.VIRTUAL_CANVAS_HEIGHT;

    private int realScreenWidth;

    private int realScreenHeight;

    private int realScreenRatio;

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

        setFocusable(true);

        this.world = new World(this);
        this.messagePopup = new MessagePopup(this);
        this.comboMeter = new ComboMeter(this.messagePopup);
        this.scorePanel = new ScorePanel(this.messagePopup, this.comboMeter, this.world.water);
    }

    /**
     * {@inheritDoc}
     * 
     * @param key
     */
    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH)
    {
        if ((w < Game.VIRTUAL_CANVAS_WIDTH) || (h < Game.VIRTUAL_CANVAS_HEIGHT)) {
            throw new IllegalStateException("Climb cannot be played on a screen resolution < 176*208");
        }

        this.realScreenWidth = w;
        this.realScreenHeight = h;
        this.realScreenRatio = w / h;

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

        if (this.gameState == Game.STATE_WAITING) {
            this.gameState = Game.STATE_RUNNING;
        }

        if (keyCode == this.keyJump) {
            this.pressedJumpKey = true;
        }
        if (keyCode == this.keyMoveLeft) {
            this.pressedMoveLeftKey = true;
        }
        if (keyCode == this.keyMoveRight) {
            this.pressedMoveRightKey = true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @param key
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        super.onKeyUp(keyCode, event);

        if (keyCode == this.keyJump) {
            this.pressedJumpKey = false;
        }
        if (keyCode == this.keyMoveLeft) {
            this.pressedMoveLeftKey = false;
        }
        if (keyCode == this.keyMoveRight) {
            this.pressedMoveRightKey = false;
        }
        return false;
    }

    public final void startGameLogic(GameActivity activity)
    {
        if (this.gameState != Game.STATE_WAITING) {
            throw new IllegalStateException();
        }

        this.activity = activity;

        this.messagePopup.registerMSG("Go Go Go", Color.rgb(30, 255, 50));
        this.scorePanel.init();
        this.comboMeter.init();
    }

    public final void resumeGameLogic()
    {
        if (this.gameState == Game.STATE_PAUSED) {
            this.gameState = Game.STATE_RUNNING;
            this.comboMeter.resume();
            this.scorePanel.resume();
            invalidate();
        }
    }

    public final void pauseGameLogic()
    {
        if (this.gameState == Game.STATE_RUNNING) {
            this.gameState = Game.STATE_PAUSED;
            this.comboMeter.pause();
            this.scorePanel.pause();
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

        invalidate();
    }

    private final void doUpdate()
    {
        final long thisUpdate = System.currentTimeMillis();
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

                final int spotY = this.world.spot.getPosition().getVirtualScreenY();
                final int waterY = this.world.water.getPosition().getVirtualScreenY();
                if (spotY > Game.VIRTUAL_CANVAS_HEIGHT + 25) {
                    this.gameState = Game.STATE_GAMEOVER_SEQUENCE;
                    this.messagePopup.registerMSG("Game Over", Color.rgb(255,
                            100, 10));
                }
                if (spotY > waterY + 20) {
                    this.gameState = Game.STATE_GAMEOVER_SEQUENCE;
                    this.messagePopup.registerMSG("Game Over", Color.rgb(255,
                            100, 10));
                }
                break;
            case STATE_PAUSED:
                return;
            case STATE_GAMEOVER_SEQUENCE:
                final int waterScreenY = this.world.water.getPosition().getVirtualScreenY();
                if (waterScreenY < 0) {
                    this.gameState = Game.STATE_FINISHING;
                }
                this.world.water.rise(10);
                this.world.spot.getPosition().add(0, -1);
                break;
            case STATE_FINISHING:
                this.activity.onGameFinished();
                this.gameState = Game.STATE_FINISHED;
                return;
            case STATE_FINISHED:
                return;
            default:
                throw new IllegalStateException("illegal gamestate: "
                        + this.gameState);
        }
    }

    private final void doDraw(Canvas canvas)
    {
        canvas.drawARGB(255, 0, 0, 0);

        transformCanvas(canvas);
        canvas.clipRect(0, 0, Game.VIRTUAL_CANVAS_WIDTH,
                Game.VIRTUAL_CANVAS_HEIGHT);

        this.world.doDraw(canvas);
        this.scorePanel.doDraw(canvas);
        this.comboMeter.doDraw(canvas);
        this.messagePopup.doDraw(canvas);
    }

    /**
     * Translates the virtual canvas to the center of the real screen canvas.
     * Scales the virtual canvas to fit the size of the real screen canvas
     * (preserving aspect ratio).
     * 
     * @param canvas
     *            real screen canvas
     */
    private final void transformCanvas(Canvas canvas)
    {
        final float scale;
        final float translateX;
        final float translateY;
        if (this.realScreenRatio > Game.VIRTUAL_CANVAS_RATIO) {
            scale = (float) this.realScreenHeight
                    / (float) Game.VIRTUAL_CANVAS_HEIGHT;
            translateY = 0;
            translateX = ((this.realScreenWidth - (scale * Game.VIRTUAL_CANVAS_WIDTH)) / 2);
        } else {
            scale = (float) this.realScreenWidth
                    / (float) Game.VIRTUAL_CANVAS_WIDTH;
            translateX = 0;
            translateY = ((this.realScreenHeight - (scale * Game.VIRTUAL_CANVAS_HEIGHT)) / 2);
        }

        canvas.translate(translateX, translateY);
        canvas.scale(scale, scale);
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
        this.scorePanel.onSpotLanded(platform);

        if ((platform % 100) < 3) { // remember we can jump 3 platforms at once!
            if ((platform / 100) > this.currentCent) {
                this.world.water.setWaterSpeedLevel(0);
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
        Log.d(Game.LOG_TAG, "Received key settings (jump,left,right): ("
                + keyJump + "," + keyMoveLeft + "," + keyMoveRight + ")");

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
    final boolean isJumpKeyPressed()
    {
        return this.pressedJumpKey;
    }

    /**
     * Returns whether the move-left key has been pressed by the user during the
     * last frame.
     * 
     * @return
     */
    final boolean isMoveLeftKeyPressed()
    {
        return this.pressedMoveLeftKey;
    }

    /**
     * Returns whether the move-right key has been pressed by the user during
     * the last frame.
     * 
     * @return
     */
    final boolean isMoveRightKeyPressed()
    {
        return this.pressedMoveRightKey;
    }

    public int getTotalScore()
    {
        return this.scorePanel.getScore();
    }

    public int getHighestTouchedPlatform()
    {
        return this.highestTouchedPlatform;
    }

    /**
     * 
     */
    public void forceQuit()
    {
    }

}
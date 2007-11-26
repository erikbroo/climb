package com.googlecode.climb.game;

import java.util.Random;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import com.googlecode.climb.GameActivity;
import com.googlecode.saga.GameEngine;


/**
 * 
 */
public final class Game extends GameEngine implements SpotEventListener,
        ClimbKeyEngine
{
    private static final String LOG_TAG = "Game";

    public final static Random RANDOM = new Random();

    private static final int GAMESTATE_WAITING = 0;

    private static final int GAMESTATE_RUNNING = 1;

    private static final int GAMESTATE_PAUSED = 2;

    private static final int GAMESTATE_GAMEOVER_SEQUENCE = 3;

    private static final int GAMESTATE_FINISHING = 4;

    private static final int GAMESTATE_FINISHED = 5;

    private static final int LOGIC_UPDATE_RATE = 5; // 20 ups = 50 millisec

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

    private final World world;

    private final Spot spot;

    private final Water water;

    private final ScorePanel scorePanel;

    private final ComboMeter comboMeter;

    private final MessagePopup messagePopup;

    private int currentLevel;

    private int gameState = Game.GAMESTATE_WAITING;

    private long lastLogicUpdate;

    private final GameActivity activity;

    private boolean jumpKeyPressed;

    private boolean moveLeftKeyPressed;

    private boolean moveRightKeyPressed;

    public Game(GameActivity activity)
    {
        super(VIRTUAL_CANVAS_WIDTH, VIRTUAL_CANVAS_HEIGHT, LOGIC_UPDATE_RATE, activity);

        this.activity = activity;
        this.world = new World(this);

        final PlatformLayer platformLayer = this.world.getPlatformLayer();
        this.spot = this.world.getSpot();
        this.water = this.world.getWater();

        this.messagePopup = new MessagePopup(this, this.spot, platformLayer);
        this.comboMeter = new ComboMeter(this.messagePopup);
        this.scorePanel = new ScorePanel(this.messagePopup, this.comboMeter, this.water);

        this.spot.addSpotEventListener(this);
        this.spot.addSpotEventListener(this.comboMeter);
        this.spot.addSpotEventListener(this.scorePanel);
    }

    /**
     * {@inheritDoc}
     * 
     * @param key
     */
    @Override
    public void onKeyDown(int keyCode)
    {
        super.onKeyDown(keyCode);

        if (this.gameState == Game.GAMESTATE_WAITING) {
            this.gameState = Game.GAMESTATE_RUNNING;
        }

        if (keyCode == this.keyJump) {
            this.jumpKeyPressed = true;
        } else if (keyCode == this.keyMoveLeft) {
            this.moveLeftKeyPressed = true;
        } else if (keyCode == this.keyMoveRight) {
            this.moveRightKeyPressed = true;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @param key
     */
    @Override
    public void onKeyUp(int keyCode)
    {
        super.onKeyUp(keyCode);

        if (keyCode == this.keyJump) {
            this.jumpKeyPressed = false;
        } else if (keyCode == this.keyMoveLeft) {
            this.moveLeftKeyPressed = false;
        } else if (keyCode == this.keyMoveRight) {
            this.moveRightKeyPressed = false;
        }
    }

    public final void startGameLogic(GameActivity activity)
    {
        if (this.gameState != Game.GAMESTATE_WAITING) {
            throw new IllegalStateException();
        }

        this.messagePopup.registerMSG("Go Go Go", Color.rgb(30, 255, 50));
        this.scorePanel.init();
        this.comboMeter.init();
    }

    public final void resumeGameLogic()
    {
        if (this.gameState == Game.GAMESTATE_PAUSED) {
            this.gameState = Game.GAMESTATE_RUNNING;
            this.comboMeter.resume();
            this.scorePanel.resume();
            // invalidate();
        }
    }

    public final void pauseGameLogic()
    {
        if (this.gameState == Game.GAMESTATE_RUNNING) {
            this.gameState = Game.GAMESTATE_PAUSED;
            this.comboMeter.pause();
            this.scorePanel.pause();
        }
    }

    public final boolean isPaused()
    {
        return this.gameState == GAMESTATE_PAUSED;
    }

    @Override
    protected void onUpdate()
    {
        switch (this.gameState) {
            case GAMESTATE_WAITING:
                return;
            case GAMESTATE_RUNNING:

                this.world.doUpdate();
                this.scorePanel.doUpdate();
                this.comboMeter.doUpdate();

                final int spotY = this.spot.getPosition().getVirtualScreenY();
                final int waterY = this.water.getPosition().getVirtualScreenY();
                if (spotY > Game.VIRTUAL_CANVAS_HEIGHT + 25) {
                    this.gameState = Game.GAMESTATE_GAMEOVER_SEQUENCE;
                    this.messagePopup.registerMSG("Game Over", Color.rgb(255,
                            100, 10));
                }
                if (spotY > waterY + 20) {
                    this.gameState = Game.GAMESTATE_GAMEOVER_SEQUENCE;
                    this.messagePopup.registerMSG("Game Over", Color.rgb(255,
                            100, 10));
                }
                break;
            case GAMESTATE_PAUSED:
                return;
            case GAMESTATE_GAMEOVER_SEQUENCE:
                final int waterScreenY = this.water.getPosition().getVirtualScreenY();
                if (waterScreenY < 0) {
                    this.gameState = Game.GAMESTATE_FINISHING;
                }
                this.water.rise(10);
                this.spot.getPosition().add(0, -1);
                break;
            case GAMESTATE_FINISHING:
                this.gameState = Game.GAMESTATE_FINISHED;
                this.activity.onGameFinished();
                return;
            case GAMESTATE_FINISHED:
                return;
            default:
                throw new IllegalStateException("illegal gamestate: "
                        + this.gameState);
        }
    }

    @Override
    protected final void onDraw(Canvas canvas)
    {
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

    public final void setKeys(int keyJump, int keyMoveLeft, int keyMoveRight)
    {
        Log.d(Game.LOG_TAG, "Received key settings (jump,left,right): ("
                + keyJump + "," + keyMoveLeft + "," + keyMoveRight + ")");

        this.keyJump = keyJump;
        this.keyMoveLeft = keyMoveLeft;
        this.keyMoveRight = keyMoveRight;
    }

    public int getTotalScore()
    {
        return this.scorePanel.getScore();
    }

    public int getHighestTouchedPlatform()
    {
        return this.spot.getHighestTouchedPlatform();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void onSpotJumped(int platform)
    {
        this.comboMeter.onSpotJumped(platform);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void onSpotLanded(int platform, int previousPlatform)
    {
        if ((platform % 100) < 3) { // remember we can jump 3 platforms at once!
            if ((platform / 100) > this.currentLevel) {
                this.currentLevel += 1;
                this.scorePanel.onNewLevel();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void onSpotCollidedWall()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isJumpPressed()
    {
        return this.jumpKeyPressed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isMoveLeftPressed()
    {
        return this.moveLeftKeyPressed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isMoveRightPressed()
    {
        return this.moveRightKeyPressed;
    }
}
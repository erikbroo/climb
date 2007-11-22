package com.googlecode.climb.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Style;


final class ScorePanel implements SpotEventListener
{
    private final static String LOG_TAG = "ScorePanel";

    private final static int PANEL_LEFT = 0;

    private final static int PANEL_TOP = 0;

    private final static int PANEL_RIGHT = Game.VIRTUAL_CANVAS_WIDTH;

    final static int PANEL_BOTTOM = 30;

    private final static int SCORE_LEFT = PANEL_LEFT + 15;

    final static int SCORE_TOP = PANEL_TOP + 7;

    final static int SCORE_RIGHT = SCORE_LEFT + 50;

    private final static int SCORE_BOTTOM = SCORE_TOP + 16;

    final static int TIMER_LEFT = PANEL_LEFT + 130;

    private final static int TIMER_TOP = PANEL_TOP + 7;

    private final static int TIMER_RIGHT = TIMER_LEFT + 31;

    private final static int TIMER_BOTTOM = TIMER_TOP + 16;

    private final Paint panel_paint = new Paint();
    {
        this.panel_paint.setStyle(Style.FILL);
        this.panel_paint.setColor(Color.BLACK);
        this.panel_paint.setAlpha(150);
    }

    private final Paint score_paint = new Paint();
    {
        this.score_paint.setStyle(Style.FILL);
        this.score_paint.setColor(Color.rgb(70, 70, 70));
    }

    private final Paint score_text_paint = new Paint();
    {
        this.score_text_paint.setTypeface(Typeface.create(Typeface.MONOSPACE,
                Typeface.BOLD));
        this.score_text_paint.setTextSize(16);
        this.score_text_paint.setColor(Color.rgb(20, 140, 250));
    }

    private final Paint timer_text_paint = new Paint();
    {
        this.timer_text_paint.setTypeface(Typeface.create(Typeface.MONOSPACE,
                Typeface.BOLD));
        this.timer_text_paint.setTextSize(16);
        this.timer_text_paint.setColor(Color.rgb(250, 10, 100));
    }

    private final Paint border_paint = new Paint();
    {
        this.border_paint.setStyle(Style.STROKE);
        this.border_paint.setColor(Color.GRAY);
    }

    private final Paint button_paint = new Paint();
    {
        this.button_paint.setStyle(Style.FILL);
        this.button_paint.setColor(Color.DKGRAY);
    }

    private int score;

    /**
     * Using an explicite string builder for efficiency reasons.
     */
    private final StringBuilder stringBuilder = new StringBuilder();

    private int baseTime = 150;

    private int timer;

    private long lastUpdate;

    // private int lastPlatform;

    private final ComboMeter comboMeter;

    private final MessagePopup messagePopup;

    private final Water water;

    ScorePanel(MessagePopup messagePopup, ComboMeter comboMeter, Water water)
    {
        this.messagePopup = messagePopup;
        this.comboMeter = comboMeter;
        this.water = water;
    }

    final void doDraw(Canvas canvas)
    {
        // panel:
        canvas.drawRect(PANEL_LEFT, PANEL_TOP, PANEL_RIGHT, PANEL_BOTTOM,
                this.panel_paint);
        canvas.drawRect(PANEL_LEFT, PANEL_TOP, PANEL_RIGHT, PANEL_BOTTOM,
                this.border_paint);

        // score:
        canvas.drawRect(SCORE_LEFT, SCORE_TOP, SCORE_RIGHT, SCORE_BOTTOM,
                this.score_paint);
        canvas.drawRect(SCORE_LEFT, SCORE_TOP, SCORE_RIGHT, SCORE_BOTTOM,
                this.border_paint);
        canvas.drawText(scoreToString(), SCORE_LEFT + 1, SCORE_BOTTOM - 2,
                this.score_text_paint);

        // timer:
        canvas.drawRect(TIMER_LEFT, TIMER_TOP, TIMER_RIGHT, TIMER_BOTTOM,
                this.score_paint);
        canvas.drawRect(TIMER_LEFT, TIMER_TOP, TIMER_RIGHT, TIMER_BOTTOM,
                this.border_paint);
        canvas.drawText(timerToString(), TIMER_LEFT + 1, TIMER_BOTTOM - 2,
                this.timer_text_paint);

        // buttons
        canvas.drawCircle(PANEL_LEFT + 4, PANEL_TOP + 4, 2, this.button_paint);
        canvas.drawCircle(PANEL_LEFT + 4, PANEL_TOP + 4, 2, this.border_paint);

        canvas.drawCircle(PANEL_LEFT + 4, PANEL_BOTTOM - 4, 2,
                this.button_paint);
        canvas.drawCircle(PANEL_LEFT + 4, PANEL_BOTTOM - 4, 2,
                this.border_paint);

        canvas.drawCircle(PANEL_RIGHT - 4, PANEL_TOP + 4, 2, this.button_paint);
        canvas.drawCircle(PANEL_RIGHT - 4, PANEL_TOP + 4, 2, this.border_paint);

        canvas.drawCircle(PANEL_RIGHT - 4, PANEL_BOTTOM - 4, 2,
                this.button_paint);
        canvas.drawCircle(PANEL_RIGHT - 4, PANEL_BOTTOM - 4, 2,
                this.border_paint);
    }

    private final String scoreToString()
    {
        final int score_ = this.score % 100000;

        this.stringBuilder.delete(0, this.stringBuilder.length());
        if (score_ < 10) {
            this.stringBuilder.append("0000");
        } else if (score_ < 100) {
            this.stringBuilder.append("000");
        } else if (score_ < 1000) {
            this.stringBuilder.append("00");
        } else if (score_ < 10000) {
            this.stringBuilder.append("0");
        }
        this.stringBuilder.append(score_);

        return this.stringBuilder.toString();
    }

    final int getScore()
    {
        return this.score;
    }

    final void init()
    {
        this.lastUpdate = System.currentTimeMillis();
        this.timer = this.baseTime;
    }

    final void pause()
    {
        // nothing to do
    }

    final void resume()
    {
        this.lastUpdate = System.currentTimeMillis();
    }

    final void doUpdate(long thisUpdate)
    {
        // Log.v(LOG_TAG, "doUpdate() last update: " + this.lastUpdate);
        // Log.v(LOG_TAG, "doUpdate() this update: " + thisUpdate);
        // Log.v(LOG_TAG, "doUpdate() elapsed time: "
        // + (thisUpdate - this.lastUpdate));

        if (thisUpdate - this.lastUpdate > 1000) { // 1 sec
            this.timer--;
            this.lastUpdate += 1000;

            if (this.timer == 100) {
                this.messagePopup.registerMSG("Hurry Up!", Color.RED);
            } else if (this.timer == 50) {
                this.messagePopup.registerMSG("Hurry Up!", Color.RED);
            } else if (this.timer == 10) {
                this.messagePopup.registerMSG("Hurry Up!", Color.RED);
            } else if (this.timer == 0) {
                this.messagePopup.registerMSG("Hurry Up", Color.RED);
            }

            if (this.timer <= 0) {
                this.water.setWaterSpeedLevel(6);
            } else if (this.timer <= 10) {
                this.water.setWaterSpeedLevel(5);
            } else if (this.timer <= 25) {
                this.water.setWaterSpeedLevel(4);
            } else if (this.timer <= 75) {
                this.water.setWaterSpeedLevel(3);
            } else if (this.timer <= 125) {
                this.water.setWaterSpeedLevel(2);
            } else {
                this.water.setWaterSpeedLevel(1);
            }
        }
    }

    private final String timerToString()
    {
        this.stringBuilder.delete(0, this.stringBuilder.length());

        if (this.timer < 0) {
            return "000";
        } else if (this.timer < 10) {
            this.stringBuilder.append("00");
            this.stringBuilder.append(this.timer);
            return this.stringBuilder.toString();
        } else if (this.timer < 100) {
            this.stringBuilder.append("0");
            this.stringBuilder.append(this.timer);
            return this.stringBuilder.toString();
        } else {
            return Integer.toString(this.timer);
        }
    }

    private void resetTimer()
    {
        this.baseTime -= 5;
        if (this.timer > 0) {
            this.score += (this.timer * 10);
            this.messagePopup.registerMSG("TIME BONUS", Color.CYAN);
        } else {
            this.messagePopup.registerMSG("NO TIME BONUS", Color.CYAN);
        }
        this.timer = this.baseTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSpotCollidedWall()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSpotJumped(int fromPlatform)
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSpotLanded(int onPlatform, int previousPlatform)
    {
        if (onPlatform - previousPlatform > 0) {
            this.score += (onPlatform - previousPlatform)
                    * this.comboMeter.getMultiplicator();
        }
    }

    final void onNewLevel()
    {
        resetTimer();
        this.water.setWaterSpeedLevel(0);
    }
}
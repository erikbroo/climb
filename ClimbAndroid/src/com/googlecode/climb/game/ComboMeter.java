package com.googlecode.climb.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;


/**
 *
 */
final class ComboMeter implements SpotEventListener
{
    private final static int BAR_TOP = ScorePanel.SCORE_TOP + 3;

    private final static int BAR_BOTTOM = BAR_TOP + 5;

    private final static int BAR_WIDTH = (ScorePanel.TIMER_LEFT - ScorePanel.SCORE_RIGHT) - 20;

    private final static int BAR_LEFT = ScorePanel.SCORE_RIGHT + 10;

    private final static int BAR_RIGHT = BAR_LEFT + BAR_WIDTH;

    private final static int BAR_RADIUS = 5; // for the round rect attribute

    private final static int MULTI_TEXT_X = BAR_LEFT + BAR_WIDTH / 2;

    private final static int MULTI_TEXT_Y = ScorePanel.PANEL_BOTTOM - 5;

    // Combo Constants:
    private final static byte NO_EVENT = 0;

    private final static byte EAGLE = 1;

    private final static byte SHARKY = 2;

    private final static byte FOXY = 3;

    private final static byte WALLY = 4;

    private final static byte WOODY = 5;

    private final static byte DOGGY = 6;

    private final static byte CATTY = 7;

    // Event Constants:
    private final static byte NO_ENTRY = 0;

    private final static byte JUMPED = 1;

    private final static byte LANDED = 10;

    private final static byte COLLIDED = 100;

    private final Paint text_paint = new Paint();
    {
        this.text_paint.setColor(Color.GREEN);
        this.text_paint.setTypeface(Typeface.create(Typeface.MONOSPACE,
                Typeface.BOLD));
        this.text_paint.setTextSize(10);
        this.text_paint.setTextAlign(Align.CENTER);
    }

    private final Paint bar_border_paint = new Paint();
    {
        this.bar_border_paint.setColor(Color.WHITE);
        this.bar_border_paint.setStyle(Style.STROKE);
    }

    private final Paint bar_empty_paint = new Paint();
    {
        this.bar_empty_paint.setColor(Color.BLACK);
        this.bar_empty_paint.setStyle(Style.FILL);
    }

    private final Paint bar_filled_paint = new Paint();
    {
        this.bar_filled_paint.setColor(Color.GREEN);
        this.bar_filled_paint.setStyle(Style.FILL);
    }

    private final RectF bar_empty_rect = new RectF(BAR_LEFT, BAR_TOP, BAR_RIGHT, BAR_BOTTOM);

    private final RectF bar_filled_rect = new RectF(BAR_LEFT, BAR_TOP, BAR_LEFT, BAR_BOTTOM);

    /*
     * Some circular lists for events and platforms:
     */
    private final byte[] event;

    private final int[] onPlatform;

    private int cycleIndex;

    private byte sharkyComboCount;

    private byte eagleComboCount;

    private byte lastCombo;

    private int lastPlatform;

    private int multiplicator = 0;

    private float barSize = 0.0f;

    private final MessagePopup popup;

    private long lastUpdate;

    ComboMeter(MessagePopup popup)
    {
        this.popup = popup;
        this.event = new byte[10];
        this.onPlatform = new int[this.event.length];
    }

    private final void addEvent(byte eventType, int platform)
    {
        this.event[this.cycleIndex] = eventType;
        this.onPlatform[this.cycleIndex] = platform;

        this.cycleIndex = (this.cycleIndex + 1) % this.event.length;
    }

    private final byte getEvent(int index)
    {
        final int i = (this.cycleIndex + index) % this.event.length;

        return this.event[i];
    }

    private final int getPlatform(int index)
    {
        final int i = (this.cycleIndex + index) % this.event.length;

        return this.onPlatform[i];
    }

    /**
     * Checks whether one of the following combos have been performed. Eagle:
     * Springen(x-2) [Abprallen] Landen(x) Sharky: Springen(x-3) [Abprallen]
     * Landen(x) Foxy: Springen(x-2) Abprallen Landen(x-2) [Abprallen]
     * Springen(x-2) [Abprallen] Landen(x) Wally: Springen(x-3) Abprallen
     * Landen(x-3) [Abprallen] Springen(x-3) [Abprallen] Landen(x) Woody:
     * {Springen(x-1) | Landen(x-1)} [Abprallen] Landen(x-2) [Abprallen]
     * Springen(x-2) [Abprallen] Landen(x) Doggy: {Springen(x-1) | Landen(x-1)}
     * [Abprallen] Landen(x-2) [Abprallen] Springen(x-3) [Abprallen] Landen(x)
     * Catty: {Springen(x+3) | Landen(x+3)} [Abprallen] Landen(x)
     */
    private void checkForCombo()
    {
        // if (this.comboBarLength <= 0) {
        // return;
        // }

        // Constants:
        final byte NO_COMBO = 0;
        final byte MAYBE_EAGLE = 1;
        final byte MAYBE_SHARKY = 2;

        int L_1 = 0;
        int J_1 = 0;
        int L_2 = 0;
        int J_2 = 0;
        int LJ_3 = 0;
        int continueOn = 9;
        byte nextEvent = getEvent(continueOn);

        if (nextEvent != ComboMeter.LANDED) {
            return;
        }

        L_1 = getPlatform(continueOn);
        // Events: ... Landed(L_1)

        continueOn -= 1;
        nextEvent = getEvent(continueOn);

        if (nextEvent == ComboMeter.COLLIDED) {
            continueOn -= 1;
            nextEvent = getEvent(continueOn);
        }

        if (nextEvent == ComboMeter.LANDED) {

            L_2 = getPlatform(continueOn);
            // Events: ... Landed(L_2) Landed(L_1)

            if (L_2 == L_1 + 3) {
                cattyComboPerformed();
            }
            return;

        }

        if (nextEvent != ComboMeter.JUMPED) {
            return;
        }

        J_1 = getPlatform(continueOn);
        // Events: ... Jumped(J_1) Landed(L_1)

        byte maybeCombo = NO_COMBO;
        if (J_1 == L_1 + 3) {

            cattyComboPerformed();
            return;

        } else if (J_1 == L_1 - 2) {

            maybeCombo = MAYBE_EAGLE;

        } else if (J_1 == L_1 - 3) {

            maybeCombo = MAYBE_SHARKY;

        } else {
            return;
        }

        continueOn -= 1;
        nextEvent = getEvent(continueOn);

        if (nextEvent == ComboMeter.COLLIDED) {
            continueOn -= 1;
            nextEvent = getEvent(continueOn);
        }

        if (nextEvent != ComboMeter.LANDED) {

            if (maybeCombo == MAYBE_EAGLE) {

                eagleComboPerformed();
                return;

            } else if (maybeCombo == MAYBE_SHARKY) {

                sharkyComboPerformed();
                return;

            }

            return;
        }

        L_2 = getPlatform(continueOn);
        // Events: ... Landed(L_2) Jumped(J_1) Landed(L_1)

        continueOn -= 1;
        nextEvent = getEvent(continueOn);

        if (nextEvent == ComboMeter.COLLIDED) {

            // Events: ... COLLIDED Landed(L_2) Jumped(J_1) Landed(L_1)

            if (getEvent(continueOn - 1) == ComboMeter.JUMPED) {

                J_2 = getPlatform(continueOn - 1);
                // Events: JUMPED(J_2) COLLIDED Landed(L_2) Jumped(J_1)
                // Landed(L_1)

                if ((J_2 == L_2) && (L_2 == J_1)) {
                    if (maybeCombo == MAYBE_EAGLE) {

                        foxyComboPerformed();
                        return;

                    } else if (maybeCombo == MAYBE_SHARKY) {

                        wallyComboPerformed();
                        return;

                    }
                }
            }

            continueOn -= 1;
            nextEvent = getEvent(continueOn);
        }

        // Events: ... Landed(L_2) Jumped(J_1) Landed(L_1)
        if ((nextEvent == ComboMeter.JUMPED)
                || (nextEvent == ComboMeter.LANDED)) {

            LJ_3 = getPlatform(continueOn);
            // Events: {Landed(LJ_3)|Jumped(LJ_3)} Landed(L_2) Jumped(J_1)
            // Landed(L_1)
            if ((LJ_3 == L_2 + 1) && (L_2 == J_1)) {

                if (maybeCombo == MAYBE_EAGLE) {

                    woodyComboPerformed();
                    return;

                } else if (maybeCombo == MAYBE_SHARKY) {

                    doggyComboPerformed();
                    return;

                }

            }
        }

        if (maybeCombo == MAYBE_EAGLE) {

            eagleComboPerformed();
            return;

        } else if (maybeCombo == MAYBE_SHARKY) {

            sharkyComboPerformed();
            return;

        }
    }

    private final void eagleComboPerformed()
    {
        fillBar();

        this.eagleComboCount += 1;
        this.sharkyComboCount = 0;

        if ((this.lastCombo == ComboMeter.EAGLE)
                && (this.lastPlatform == getPlatform(9))) {
            this.multiplicator += 4;
            this.popup.registerComboString("same eagle (+4)", this.lastPlatform);
        } else if (this.eagleComboCount >= 3) {
            this.eagleComboCount = 0;
            this.multiplicator += 3;
            this.popup.registerComboString("triple eagle  (+3)",
                    this.lastPlatform);
        } else {
            this.multiplicator += 1;
            this.popup.registerComboString("eagle (+1)", this.lastPlatform);
        }

        this.lastPlatform = getPlatform(9);
        this.lastCombo = ComboMeter.EAGLE;
    }

    private final void sharkyComboPerformed()
    {
        fillBar();

        this.sharkyComboCount += 1;
        this.eagleComboCount = 0;

        if ((this.lastCombo == ComboMeter.SHARKY)
                && (this.lastPlatform == getPlatform(9))) {
            this.multiplicator += 6;
            this.popup.registerComboString("same sharky (+6)",
                    this.lastPlatform);
        } else if (this.sharkyComboCount >= 2) {
            this.sharkyComboCount = 0;
            this.multiplicator += 4;
            this.popup.registerComboString("double sharky (+4)",
                    this.lastPlatform);
        } else {
            this.multiplicator += 2;
            this.popup.registerComboString("sharky (+2)", this.lastPlatform);
        }

        this.lastPlatform = getPlatform(9);
        this.lastCombo = ComboMeter.SHARKY;
    }

    private final void wallyComboPerformed()
    {
        fillBar();

        this.sharkyComboCount = 0;
        this.eagleComboCount = 0;

        if ((this.lastCombo == ComboMeter.WALLY)
                && (this.lastPlatform == getPlatform(9))) {
            this.multiplicator += 8;
            this.popup.registerComboString("same wally (+8)", this.lastPlatform);
        } else {
            this.multiplicator += 4;
            this.popup.registerComboString("wally (+4)", this.lastPlatform);
        }

        this.lastPlatform = getPlatform(9);
        this.lastCombo = ComboMeter.WALLY;
    }

    private final void foxyComboPerformed()
    {
        fillBar();

        this.sharkyComboCount = 0;
        this.eagleComboCount = 0;

        if ((this.lastCombo == ComboMeter.FOXY)
                && (this.lastPlatform == getPlatform(9))) {
            this.multiplicator += 6;
            this.popup.registerComboString("same foxy (+6)", this.lastPlatform);
        } else {
            this.multiplicator += 3;
            this.popup.registerComboString("foxy (+3)", this.lastPlatform);
        }

        this.lastPlatform = getPlatform(9);
        this.lastCombo = ComboMeter.FOXY;
    }

    private final void woodyComboPerformed()
    {
        fillBar();

        this.sharkyComboCount = 0;
        this.eagleComboCount = 0;

        if ((this.lastCombo == ComboMeter.WOODY)
                && (this.lastPlatform == getPlatform(9))) {
            this.multiplicator += 12;
            this.popup.registerComboString("same woody (+12)",
                    this.lastPlatform);
        } else {
            this.multiplicator += 6;
            this.popup.registerComboString("woody (+6)", this.lastPlatform);
        }

        this.lastPlatform = getPlatform(9);
        this.lastCombo = ComboMeter.WOODY;
    }

    private final void cattyComboPerformed()
    {
        fillBar();

        this.sharkyComboCount = 0;
        this.eagleComboCount = 0;

        if ((this.lastCombo == ComboMeter.CATTY)
                && (this.lastPlatform == getPlatform(9))) {
            this.multiplicator += 15;
            this.popup.registerComboString("same catty (+15)",
                    this.lastPlatform);
        } else {
            this.multiplicator += 6;
            this.popup.registerComboString("catty (+6)", this.lastPlatform);
        }

        this.lastPlatform = getPlatform(9);
        this.lastCombo = ComboMeter.CATTY;
    }

    private final void doggyComboPerformed()
    {
        fillBar();

        this.sharkyComboCount = 0;
        this.eagleComboCount = 0;

        if ((this.lastCombo == ComboMeter.DOGGY)
                && (this.lastPlatform == getPlatform(9))) {
            this.multiplicator += 16;
            this.popup.registerComboString("same doggy (+16)",
                    this.lastPlatform);
        } else {
            this.multiplicator += 8;
            this.popup.registerComboString("doggy (+8)", this.lastPlatform);
        }

        this.lastPlatform = getPlatform(9);
        this.lastCombo = ComboMeter.DOGGY;
    }

    final void doDraw(Canvas canvas)
    {
        // BAR:
        canvas.drawRoundRect(this.bar_empty_rect, BAR_RADIUS, BAR_RADIUS,
                this.bar_empty_paint);
        if (this.barSize > 0) {
            this.bar_filled_rect.right = BAR_LEFT + this.barSize * BAR_WIDTH;
            canvas.drawRoundRect(this.bar_filled_rect, BAR_RADIUS, BAR_RADIUS,
                    this.bar_filled_paint);
        }
        canvas.drawRoundRect(this.bar_empty_rect, BAR_RADIUS, BAR_RADIUS,
                this.bar_border_paint);

        // Combo-Multiplicator:
        canvas.drawText(Integer.toString(this.multiplicator), MULTI_TEXT_X,
                MULTI_TEXT_Y, this.text_paint);
    }

    // if (this.multiplicator >= 50) {
    // this.popup.registerMSG("PHENOMENAL", Color.rgb(5, 240, 190));
    // t = 50;
    // } else if (this.multiplicator >= 40) {
    // this.popup.registerMSG("SENSATIONAL", Color.rgb(5, 240, 190));
    // t = 40;
    // } else if (this.multiplicator >= 30) {
    // this.popup.registerMSG("AMAZING", Color.rgb(5, 240, 190));
    // t = 30;
    // } else if (this.multiplicator >= 20) {
    // this.popup.registerMSG("Great", Color.rgb(5, 240, 190));
    // t = 20;
    // } else if (this.multiplicator >= 10) {
    // this.popup.registerMSG("NICE", Color.rgb(5, 240, 190));
    // t = 10;
    // } else if (this.multiplicator >= 5) {
    // this.popup.registerMSG("GOOD", Color.rgb(5, 240, 190));
    // t = 5;
    // }

    final void doUpdate()
    {
        final long thisUpdate = System.currentTimeMillis();
        if (thisUpdate - this.lastUpdate > 100) { // 100 msec
            this.lastUpdate += 100;

            if (this.barSize > 0) {
                this.barSize = Math.max(this.barSize - 0.02f, 0);
                if (this.barSize == 0) {
                    this.multiplicator = 0;
                }
            }
        }
    }

    final void init()
    {
        this.lastUpdate = System.currentTimeMillis();
    }

    final void resume()
    {
        this.lastUpdate = System.currentTimeMillis();
    }

    final void pause()
    {

    }

    private void fillBar()
    {
        this.barSize = 1.0f;
    }

    final int getMultiplicator()
    {
        return this.multiplicator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void onSpotJumped(int platform)
    {
        addEvent(ComboMeter.JUMPED, platform);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void onSpotLanded(int platform, int previousPlatform)
    {
        addEvent(ComboMeter.LANDED, platform);

        checkForCombo();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void onSpotCollidedWall()
    {
        addEvent(ComboMeter.COLLIDED, ComboMeter.NO_ENTRY);
    }
}
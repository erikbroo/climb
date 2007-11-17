package com.googlecode.climb.game;

import android.graphics.Canvas;


/**
 *
 */
final class ComboMeter
{
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

    private int comboMultiplicator;

    private int comboBarLength;

    private int multiBlinkNTimes = 0;

    private int multiBlinkWith = 0;

    private final MessagePopup popup;

    private final Water water;

    private final ScorePanel score;

    private long lastUpdate;

    ComboMeter(MessagePopup popup, Water water, ScorePanel score)
    {
        this.popup = popup;
        this.water = water;
        this.score = score;
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
     * Called when the ball jumps.
     * 
     * @param fromPlatform
     *            ball jumped from this platform
     */
    final void onSpotJumped(int fromPlatform)
    {
        this.fillComboBar();

        addEvent(ComboMeter.JUMPED, fromPlatform);
    }

    /**
     * Called when the ball lands on the specified platform.
     * 
     * @param platform
     */
    final void onSpotLanded(int platform)
    {
        addEvent(ComboMeter.LANDED, platform);

        this.comboBarLength = Math.max(this.comboBarLength * 2 / 3, 1);
        checkForCombo();
    }

    final void onSpotCollided()
    {
        addEvent(ComboMeter.COLLIDED, ComboMeter.NO_ENTRY);
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
        if (this.comboBarLength <= 0) {
            return;
        }

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
                return;

            } else {
                return;
            }

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
        this.comboBarLength = 42;

        this.eagleComboCount += 1;
        this.sharkyComboCount = 0;

        if ((this.lastCombo == ComboMeter.EAGLE)
                && (this.lastPlatform == getPlatform(9))) {
            addToMulti(4);
            this.popup.registerComboString("same eagle");
        } else if (this.eagleComboCount >= 3) {
            this.eagleComboCount = 0;
            addToMulti(3);
            this.popup.registerComboString("triple eagle");
        } else {
            addToMulti(1);
            this.popup.registerComboString("eagle");
        }

        this.lastPlatform = getPlatform(9);
        this.lastCombo = ComboMeter.EAGLE;
    }

    private final void sharkyComboPerformed()
    {
        this.comboBarLength = 42;

        this.sharkyComboCount += 1;
        this.eagleComboCount = 0;

        if ((this.lastCombo == ComboMeter.SHARKY)
                && (this.lastPlatform == getPlatform(9))) {
            addToMulti(6);
            this.popup.registerComboString("same sharky");
        } else if (this.sharkyComboCount >= 2) {
            this.sharkyComboCount = 0;
            addToMulti(4);
            this.popup.registerComboString("double sharky");
        } else {
            addToMulti(2);
            this.popup.registerComboString("sharky");
        }

        this.lastPlatform = getPlatform(9);
        this.lastCombo = ComboMeter.SHARKY;
    }

    private final void wallyComboPerformed()
    {
        this.comboBarLength = 42;

        this.sharkyComboCount = 0;
        this.eagleComboCount = 0;

        if ((this.lastCombo == ComboMeter.WALLY)
                && (this.lastPlatform == getPlatform(9))) {
            addToMulti(8);
            this.popup.registerComboString("same wally");
        } else {
            addToMulti(4);
            this.popup.registerComboString("wally");
        }

        this.lastPlatform = getPlatform(9);
        this.lastCombo = ComboMeter.WALLY;
    }

    private final void foxyComboPerformed()
    {
        this.comboBarLength = 42;

        this.sharkyComboCount = 0;
        this.eagleComboCount = 0;

        if ((this.lastCombo == ComboMeter.FOXY)
                && (this.lastPlatform == getPlatform(9))) {
            addToMulti(6);
            this.popup.registerComboString("same foxy");
        } else {
            addToMulti(3);
            this.popup.registerComboString("foxy");
        }

        this.lastPlatform = getPlatform(9);
        this.lastCombo = ComboMeter.FOXY;
    }

    private final void woodyComboPerformed()
    {
        this.comboBarLength = 42;

        this.sharkyComboCount = 0;
        this.eagleComboCount = 0;

        if ((this.lastCombo == ComboMeter.WOODY)
                && (this.lastPlatform == getPlatform(9))) {
            addToMulti(12);
            this.popup.registerComboString("same woody");
        } else {
            addToMulti(6);
            this.popup.registerComboString("woody");
        }

        this.lastPlatform = getPlatform(9);
        this.lastCombo = ComboMeter.WOODY;
    }

    private final void cattyComboPerformed()
    {
        this.comboBarLength = 42;

        this.sharkyComboCount = 0;
        this.eagleComboCount = 0;

        if ((this.lastCombo == ComboMeter.CATTY)
                && (this.lastPlatform == getPlatform(9))) {
            addToMulti(10);
            this.popup.registerComboString("same catty");
        } else {
            addToMulti(4);
            this.popup.registerComboString("catty");
        }

        this.lastPlatform = getPlatform(9);
        this.lastCombo = ComboMeter.CATTY;
    }

    private final void doggyComboPerformed()
    {
        this.comboBarLength = 42;

        this.sharkyComboCount = 0;
        this.eagleComboCount = 0;

        if ((this.lastCombo == ComboMeter.DOGGY)
                && (this.lastPlatform == getPlatform(9))) {
            addToMulti(16);
            this.popup.registerComboString("same doggy");
        } else {
            addToMulti(8);
            this.popup.registerComboString("doggy");
        }

        this.lastPlatform = getPlatform(9);
        this.lastCombo = ComboMeter.DOGGY;
    }

    private final void addToMulti(int i)
    {
        this.comboMultiplicator += i;

        if (this.comboMultiplicator <= 20) {
            this.water.setWaterSpeedMultiplikator(0);
        } else if ((this.comboMultiplicator > 20)
                && (this.comboMultiplicator <= 40)) {
            this.water.setWaterSpeedMultiplikator(1);
        } else if ((this.comboMultiplicator > 40)
                && (this.comboMultiplicator <= 60)) {
            this.water.setWaterSpeedMultiplikator(2);
        } else if (this.comboMultiplicator > 60) {
            this.water.setWaterSpeedMultiplikator(3);
        }
    }

    final void doDraw(Canvas canvas)
    {
        // Bar:
        g.setColor(0x010101);
        g.fillRoundRect(75, 5, 40, 5, 10, 10);
        g.setColor(0xD0D0D0);
        g.drawRoundRect(75, 5, 40, 5, 10, 10);

        // Combo-Multi:
        g.setColor(250, 180, 40);
        int jumps = this.comboMultiplicator;
        g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN,
                Font.SIZE_SMALL));
        if (this.multiBlinkNTimes > 0) {
            if (this.multiBlinkNTimes % 3 == 0) {
                g.setColor(250, 40, 100);
            }
            jumps = this.multiBlinkWith;
            g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD,
                    Font.SIZE_SMALL));
        }
        g.drawString("x" + jumps, 89, 13, Graphics.TOP | Graphics.LEFT);

        g.setColor(250, 180, 40);
        if (this.comboBarLength > 0) {
            g.fillRoundRect(76, 5, this.comboBarLength - 2, 5, 10, 10);
            g.setColor(0xD0D0D0);
            g.drawRoundRect(75, 5, 40, 5, 10, 10);
        }
    }

    final void fillComboBar()
    {
        if (this.comboBarLength == 0) {
            this.comboBarLength = 42;
        }
    }

    private final void resetComboMulti()
    {
        this.water.setWaterSpeedMultiplikator(0);
        this.comboBarLength = 0;
        int t = 1;

        if (this.comboMultiplicator >= 50) {
            this.popup.registerMSG("PHENOMENAL", 0x10EE99);
            t = 50;
        } else if (this.comboMultiplicator >= 40) {
            this.popup.registerMSG("SENSATIONAL", 0x10EE99);
            t = 40;
        } else if (this.comboMultiplicator >= 30) {
            this.popup.registerMSG("AMAZING", 0x10EE99);
            t = 30;
        } else if (this.comboMultiplicator >= 20) {
            this.popup.registerMSG("Great", 0x10EE99);
            t = 20;
        } else if (this.comboMultiplicator >= 10) {
            this.popup.registerMSG("NICE", 0x10EE99);
            t = 10;
        } else if (this.comboMultiplicator >= 5) {
            this.popup.registerMSG("GOOD", 0x10EE99);
            t = 5;
        }
        this.score.addToScore(t * this.comboMultiplicator);
        if (t * this.comboMultiplicator <= 1) {
            return;
        }
        this.popup.registerPointAdds(t * this.comboMultiplicator);

        this.multiBlinkNTimes = 16;
        this.multiBlinkWith = this.comboMultiplicator;

        this.comboMultiplicator = 0;
    }

    final void doUpdate(final long thisUpdate)
    {
        thisUpdate = thisUpdate / 1000; // todo

        if (thisUpdate - this.lastUpdate > 200) { // 200 msec
            this.lastUpdate += 200;

            if (this.comboBarLength > 0) {
                this.comboBarLength = Math.max(this.comboBarLength - 2, 0);
                if (this.comboBarLength == 0) {
                    resetComboMulti();
                }
            }
            this.multiBlinkNTimes--;
            this.multiBlinkNTimes = Math.max(0, this.multiBlinkNTimes);
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
}
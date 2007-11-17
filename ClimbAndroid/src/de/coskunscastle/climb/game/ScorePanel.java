package de.coskunscastle.climb.game;

import android.graphics.Canvas;


final class ScorePanel
{
    private final static int BOX_WIDTH = World.WORLD_VIEW_WIDTH;

    private final static int BOX_HEIGHT = 25;

    private final static int BOX_XPOS = 0;

    private final static int BOX_YPOS = 0;

    private final static int[] ALPHA_BOX;

    static {
        ALPHA_BOX = new int[ScorePanel.BOX_WIDTH * 1];
        for (int i = 0; i < ScorePanel.ALPHA_BOX.length; i++) {
            ScorePanel.ALPHA_BOX[i] = 0x99222222;
        }
    }

    private final Game game;

    private int baseScore;

    private int baseTime = 150;

    ScorePanel(Game game)
    {
        this.game = game;
    }

    final void doDraw(Canvas canvas)
    {
        int lines = 0;
        while (lines <= ScorePanel.BOX_HEIGHT) {
            canvas.drawRGB(ScorePanel.ALPHA_BOX, 0, ScorePanel.BOX_WIDTH, 0,
                    lines, ScorePanel.BOX_WIDTH, 1, true);
            lines++;
        }
        canvas.setColor(255, 255, 255);
        canvas.drawRect(0, 0, 175, 25);

        // score
        canvas.setColor(0xFEFEFE);
        canvas.drawRect(19, 4, 50, 16);
        canvas.setColor(0x404040);
        canvas.fillRect(19, 4, 50, 16);
        canvas.setColor(20, 140, 250);
        canvas.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD,
                Font.SIZE_MEDIUM));
        canvas.drawString(scoreToString(), 20, 5, Graphics.TOP | Graphics.LEFT);

        // countdown
        canvas.setColor(0xFEFEFE);
        canvas.drawRect(125, 4, 30, 16);
        canvas.setColor(0x404040);
        canvas.fillRect(125, 4, 30, 16);
        canvas.setColor(250, 10, 100);
        canvas.drawString(timerToString(), 126, 5, Graphics.TOP | Graphics.LEFT);

        // buttons
        canvas.setColor(0x707070);
        canvas.fillArc(5, 2, 7, 7, 0, 360);
        canvas.fillArc(164, 2, 7, 7, 0, 360);
        canvas.fillArc(5, 15, 7, 7, 0, 360);
        canvas.fillArc(164, 15, 7, 7, 0, 360);
        canvas.setColor(0xD0D0D0);
        canvas.drawArc(5, 2, 7, 7, 0, 360);
        canvas.drawArc(164, 2, 7, 7, 0, 360);
        canvas.drawArc(5, 15, 7, 7, 0, 360);
        canvas.drawArc(164, 15, 7, 7, 0, 360);
        canvas.setColor(0xFEFEFE);
        canvas.fillArc(6, 3, 3, 3, 0, 360);
        canvas.fillArc(165, 3, 3, 3, 0, 360);
        canvas.fillArc(6, 16, 3, 3, 0, 360);
        canvas.fillArc(165, 16, 3, 3, 0, 360);
    }

    final void addToScore(int v)
    {
        this.baseScore += v;
    }

    private final String scoreToString()
    {
        int score_ = getTotalScore();
        String Score = "";
        score_ = score_ % 100000;

        if (score_ < 10) {
            Score = "0000" + score_;
        } else if (score_ < 100) {
            Score = "000" + score_;
        } else if (score_ < 1000) {
            Score = "00" + score_;
        } else if (score_ < 10000) {
            Score = "0" + score_;
        } else {
            Score = "" + score_;
        }

        return Score;
    }

    final int getTotalScore()
    {
        return this.baseScore + this.game.highestTouchedPlatform * 1;
    }

    private int timer;

    private long timerTick;

    final void init()
    {
        this.timerTick = System.currentTimeMillis();
        this.timer = this.baseTime;
    }

    final void pause()
    {
        // nothing to do
    }

    final void resume()
    {
        this.timerTick = System.currentTimeMillis();
    }

    final void doUpdate(long thisUpdate)
    {
        if (tick - this.timerTick > 1000) {
            this.timer--;
            this.timerTick += 1000;

            if (this.timer == 100) {
                this.game.messagePopup.registerMSG("Hurry Up", 0xFF1111);
                this.game.world.water.setWaterSpeedLevel(1);
            } else if (this.timer == 70) {
                this.game.messagePopup.registerMSG("Hurry Up!", 0xFF1111);
                this.game.world.water.setWaterSpeedLevel(2);
            } else if (this.timer == 20) {
                this.game.messagePopup.registerMSG("Hurry Up!!", 0xFF1111);
                this.game.world.water.setWaterSpeedLevel(3);
            } else if (this.timer == 0) {
                this.game.messagePopup.registerMSG("Hurry Up!!!", 0xFF1111);
                this.game.world.water.setWaterSpeedLevel(4);
            }
        }
    }

    final String timerToString()
    {
        if (this.timer < 0) {
            return "000";
        } else if (this.timer < 10) {
            return "00" + this.timer;
        } else if (this.timer < 100) {
            return "0" + this.timer;
        } else {
            return "" + this.timer;
        }
    }

    public void resetTimer()
    {
        this.baseTime -= 10;
        if (this.timer > 0) {
            this.baseScore += (this.timer * 10);
            this.game.messagePopup.registerMSG("TIME BONUS", 0x202090);
        } else {
            this.game.messagePopup.registerMSG("NO TIME BONUS", 0x202090);
        }
        this.timer = this.baseTime;
    }
}
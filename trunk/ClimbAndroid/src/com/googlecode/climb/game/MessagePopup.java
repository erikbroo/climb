package com.googlecode.climb.game;

import java.util.LinkedList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.Log;


final class MessagePopup
{
    private static final String LOG_TAG = "MessagePopup";

    private final static int BOX_LEFT = 30;

    private final static int BOX_RIGHT = Game.VIRTUAL_CANVAS_WIDTH - 30;

    private final static int BOX_TOP = 50;

    private final static int BOX_BOTTOM = BOX_TOP + 20;

    private final static int TEXT_X = (BOX_LEFT + BOX_RIGHT) / 2;

    private final static int TEXT_Y = BOX_BOTTOM - 4;

    private final Paint box_border_paint = new Paint();
    {
        this.box_border_paint.setColor(Color.WHITE);
        this.box_border_paint.setStyle(Style.STROKE);
    }

    private final Paint box_fill_paint = new Paint();
    {
        this.box_fill_paint.setColor(Color.rgb(50, 50, 50));
        this.box_fill_paint.setAlpha(200);
        this.box_fill_paint.setStyle(Style.FILL);
    }

    private final Paint text_paint = new Paint();
    {
        this.text_paint.setTextSize(18);
        this.text_paint.setTypeface(Typeface.create(Typeface.MONOSPACE,
                Typeface.BOLD));
        this.text_paint.setTextAlign(Align.CENTER);
    }

    private final LinkedList<Message> messageQueue = new LinkedList<Message>();

    private long startTime;

    private long elapsedTime = -1;

    private int pointAdds = -1;

    private int pointAddsAt = 0;

    private String comboString;

    private long comboShowedTime;

    private int comboX;

    private int comboY;

    private final Game game;

    private final Spot spot;

    MessagePopup(Game game)
    {
        this.game = game;
        this.spot = this.game.world.spot;
    }

    final void registerPointAdds(int adds)
    {
        this.pointAdds = adds;
        this.pointAddsAt = 0;
    }

    final void registerComboString(String msg)
    {
        this.comboString = msg;
        this.comboY = this.spot.getPosition().getVirtualScreenY();
        this.comboX = this.spot.getPosition().getVirtualScreenX();
        this.comboShowedTime = System.currentTimeMillis();
    }

    final void registerMSG(String msg, int color)
    {
        Log.i(LOG_TAG, "registerMSG() registering message: " + msg);
        this.messageQueue.add(new Message(color, msg));
    }

    final void doDraw(Canvas canvas)
    {
        // if (this.pointAdds != -1) {
        // g.setColor(20, 140, 250);
        // g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD,
        // Font.SIZE_MEDIUM));
        // g.drawString(this.pointAdds + "", 66, 27 - this.pointAddsAt,
        // Graphics.TOP | Graphics.RIGHT);
        // this.pointAddsAt += 2;
        // if (27 - this.pointAddsAt <= 7) {
        // this.pointAdds = -1;
        // }
        // }
        //
        // if (this.comboString != null) {
        // final int y = this.world.calculateViewPosition(this.comboY);
        //
        // g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD,
        // Font.SIZE_SMALL));
        // g.setColor(30, 80, 1);
        // g.drawString(this.comboString, this.comboX + 2, y + 12,
        // Graphics.TOP | Graphics.HCENTER);
        // g.drawString(this.comboString, this.comboX - 2, y + 12,
        // Graphics.TOP | Graphics.HCENTER);
        // g.drawString(this.comboString, this.comboX + 2, y + 8, Graphics.TOP
        // | Graphics.HCENTER);
        // g.drawString(this.comboString, this.comboX - 2, y + 8, Graphics.TOP
        // | Graphics.HCENTER);
        // g.setColor(250, 180, 40);
        // g.drawString(this.comboString, this.comboX, y + 10, Graphics.TOP
        // | Graphics.HCENTER);
        // if (System.currentTimeMillis() - this.comboShowedTime >= 500) {
        // this.comboString = null;
        // }
        // }

        if (this.messageQueue.isEmpty()) {
            return;
        }
        // Log.d(LOG_TAG, "doDraw() message queue not empty");
        if (this.elapsedTime >= 1500) { // 2.5 secs
            this.messageQueue.remove(0);
            this.elapsedTime = -1;
            return;
        }
        // Log.d(LOG_TAG, "doDraw() drawing message");

        if (this.elapsedTime == -1) {
            this.startTime = System.currentTimeMillis();
        }
        this.elapsedTime = System.currentTimeMillis() - this.startTime;

        canvas.drawRect(BOX_LEFT, BOX_TOP, BOX_RIGHT, BOX_BOTTOM,
                this.box_fill_paint);
        canvas.drawRect(BOX_LEFT, BOX_TOP, BOX_RIGHT, BOX_BOTTOM,
                this.box_border_paint);

        final String s = this.messageQueue.get(0).message;
        final int c = this.messageQueue.get(0).color;
        this.text_paint.setColor(Color.rgb(100, 1, 1));
        canvas.drawText(s, TEXT_X - 1, TEXT_Y - 1, this.text_paint);
        this.text_paint.setColor(c);
        canvas.drawText(s, TEXT_X, TEXT_Y, this.text_paint);
    }

    private static class Message
    {
        int color;

        String message;

        Message(int color, String s)
        {
            this.color = color;
            this.message = s;
        }
    }
}
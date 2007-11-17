package de.coskunscastle.climb;

import java.util.Map;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;


/**
 * 
 */
public class SplashScreenView extends View
{
    private final static String LOG_TAG = "SplashScreenView";

    private final Rect viewSize = new Rect();

    private final Paint backgroundPaint = new Paint();
    {
        this.backgroundPaint.setStyle(Style.FILL);
        this.backgroundPaint.setColor(Color.BLACK);
    }

    final Paint textPaint = new Paint();
    {
        this.textPaint.setARGB(255, 0, 0, 0);
        this.textPaint.setTextAlign(Align.CENTER);
        this.textPaint.setTextSize(18);
    }

    boolean first = true;

    private boolean interruptAnimation = false;

    /**
     * I don't know what the arguments do, I need them for the super
     * constructor. Fortunately an instance of this class is created for me
     * automatically because of the game_layout.xml file.
     * 
     * @param context
     * @param attrs
     * @param inflateParams
     * @param defStyle
     */
    public SplashScreenView(Context context, AttributeSet attrs,
            Map inflateParams)
    {
        super(context, attrs, inflateParams);

        setFocusable(true);
    }

    /**
     * Callback method. This method is called when the user presses any keys on
     * the device.
     */
    @Override
    public boolean onKeyDown(int code, KeyEvent event)
    {
        super.onKeyDown(code, event);

        this.interruptAnimation = true;

        return false;
    }

    /**
     * Callback method. This method is called when this view is supposed to draw
     * itself.
     * 
     * @param canvas
     */
    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        canvas.drawRect(this.viewSize, this.backgroundPaint);

        if (this.first) {
            final String appDeveloper = getResources().getString(
                    R.string.app_developer);
            canvas.drawText(appDeveloper, this.viewSize.width() / 2,
                    this.viewSize.height() / 2 - 10, this.textPaint);
            canvas.drawText("presents", this.viewSize.width() / 2,
                    this.viewSize.height() / 2 + 10, this.textPaint);
        } else {
            final String appName = getResources().getString(R.string.app_name);
            canvas.drawText(appName, this.viewSize.width() / 2, this.viewSize
                    .height() / 2, this.textPaint);
        }
    }

    /**
     * Callback method. This method is called, when this view has changed its
     * size.
     * 
     * @param width
     * @param height
     * @param oldW
     * @param oldH
     */
    @Override
    public void onSizeChanged(int width, int height, int oldW, int oldH)
    {
        super.onSizeChanged(width, height, oldW, oldH);
        this.viewSize.set(0, 0, width, height);
        invalidate();
    }

    /**
     * Performs the splash screen animation. This method returns, when the
     * animation is finished or interrupted. The animation is interrupted by any
     * key press of the user.
     */
    void doSplash()
    {
        Log.v(SplashScreenView.LOG_TAG, "begin doSplash()");
        int textColor = 0;
        int animationStep = 5;

        try {
            Thread.sleep(1000);
        } catch (final InterruptedException e1) {
            e1.printStackTrace();
        }

        while (true) {
            if (this.interruptAnimation) {
                break;
            }
            textColor += animationStep;
            if (textColor > 180) {
                textColor = 180;
                animationStep = -5;
                try {
                    Thread.sleep(1000);
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (textColor < 0) {
                if (SplashScreenView.this.first) {
                    SplashScreenView.this.first = false;
                    textColor = 0;
                    animationStep = 5;
                    try {
                        Thread.sleep(1000);
                    } catch (final InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    break;
                }
            }
            SplashScreenView.this.textPaint.setARGB(255, textColor, textColor,
                    textColor);
            postInvalidate();
            try {
                Thread.sleep(50);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

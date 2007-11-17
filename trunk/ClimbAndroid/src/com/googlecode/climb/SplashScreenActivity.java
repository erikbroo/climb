package com.googlecode.climb;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;


public class SplashScreenActivity extends Activity
{
    /**
     * Callback method. {@inheritDoc}
     */
    @Override
    public void onCreate(final Bundle icicle)
    {
        super.onCreate(icicle);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.splash_layout);
        final SplashScreenView splash = (SplashScreenView) findViewById(R.id.splash_view);
        new Thread(new Runnable() {
            public void run()
            {
                splash.doSplash();
                SplashScreenActivity.this.setResult(Activity.RESULT_OK);
                SplashScreenActivity.this.finish();
            }
        }).start();
    }
}

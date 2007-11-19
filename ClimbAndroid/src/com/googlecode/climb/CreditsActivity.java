package com.googlecode.climb;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;


public class CreditsActivity extends Activity
{
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(final Bundle icicle)
    {
        super.onCreate(icicle);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.credits_layout);
    }

    /**
     * Callback Method. {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        final boolean result = super.onCreateOptionsMenu(menu);
        int resourceID;

        resourceID = R.string.menu_label_close;
        menu.add(0, 0, resourceID, new MenuActionListener(resourceID));

        return result;
    }

    private final class MenuActionListener implements Runnable
    {
        private final int id;

        MenuActionListener(int menuResourceID)
        {
            this.id = menuResourceID;
        }

        @Override
        public void run()
        {
            switch (this.id) {
                case R.string.menu_label_close:
                    CreditsActivity.this.finish();
                    break;
            }
        }
    }
}

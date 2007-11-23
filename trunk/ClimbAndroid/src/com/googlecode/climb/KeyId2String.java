package com.googlecode.climb;

import android.view.KeyEvent;


public class KeyId2String
{
    public static String map(int id)
    {
        switch (id) {
            case KeyEvent.ACTION_DOWN:
                return "Action Down";
            case KeyEvent.ACTION_UP:
                return "Action Up";
            case KeyEvent.KEYCODE_0:
                return "0";
            case KeyEvent.KEYCODE_1:
                return "1";
            case KeyEvent.KEYCODE_2:
                return "2";
            case KeyEvent.KEYCODE_3:
                return "3";
            case KeyEvent.KEYCODE_4:
                return "4";
            case KeyEvent.KEYCODE_5:
                return "5";
            case KeyEvent.KEYCODE_6:
                return "6";
            case KeyEvent.KEYCODE_7:
                return "7";
            case KeyEvent.KEYCODE_8:
                return "8";
            case KeyEvent.KEYCODE_9:
                return "9";
            case KeyEvent.KEYCODE_DPAD_CENTER:
                return "DPAD Center";
            case KeyEvent.KEYCODE_DPAD_LEFT:
                return "DPAD Left";
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                return "DPAD Right";
            case KeyEvent.KEYCODE_DPAD_DOWN:
                return "DPAD Down";
            case KeyEvent.KEYCODE_DPAD_UP:
                return "DPAD Up";
            case KeyEvent.KEYCODE_NEWLINE:
                return "Newline";
            case KeyEvent.KEYCODE_POUND:
                return "Pound";
                // case KeyEvent.KEYCODE_SOFT_LEFT:
                // return "Soft Left";
            case KeyEvent.KEYCODE_SOFT_RIGHT:
                return "Soft Right";
            case KeyEvent.KEYCODE_SPACE:
                return "Space";
            case KeyEvent.KEYCODE_STAR:
                return "Star";
            case KeyEvent.KEYCODE_TAB:
                return "TAB";
            default:
                return "unkown";
        }
    }
}

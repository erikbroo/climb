/*
 * Background.java
 * Created on 26.12.2004, &{time}
 */
package com.googlecode.climb.game;

import java.io.IOException;
import java.util.Random;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.TiledLayer;

/**
 * @author Fatih Coskun
 *
 */
final class Background extends TiledLayer
{
    private static Image image;
    static {
        try {
            image = Image.createImage("/background.png");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    private final int rows;
    private final Random random;
    
    Background(int rows, Random random)
    {
        super(1, rows, image, 176, 14);
        this.rows = rows;
        this.random = random;
        
        setCells();
    }

    private final void setCells()
    {
        for (int i=0; i < this.rows; i++) {
            int r = Math.abs(this.random.nextInt() % 14) + 1;
            if (Math.abs(this.random.nextInt() % 5) == 0) {
                if (i % 2 == 0) {
                    r = 14;
                }
                else {
                    r = 13;
                }
            }
            else {
                if (i % 2 == 0 && (r % 2 != 0)) {
                    i--;
                    continue;
                }
                else if (i % 2 != 0 && (r % 2 == 0)) {
                    i--;
                    continue;
                }
            }
            setCell(0, i, r);
        }
    }
    
}

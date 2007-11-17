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
final class Foreground extends TiledLayer
{
    private static Image image;
    static {
        
        try {
            image = Image.createImage("/foreground.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private final int rows;
    private final Random random;
    
    Foreground(int rows, Random random)
    {
        super(1, rows, image, 15, 28);
        this.rows = rows;
        this.random = random;
        
        setCells();
    }

    private final void setCells()
    {
        for (int i=0; i < this.rows; i++) {
            int r = Math.abs(this.random.nextInt() % 12) + 1;
            setCell(0, i, r);
        }
    }
    
}

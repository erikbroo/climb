package com.googlecode.climb.game;

import java.util.Random;
import android.content.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import com.googlecode.climb.R;
import com.googlecode.climb.game.utils.ParallaxLayer;
import com.googlecode.climb.game.utils.RingListI;
import com.googlecode.climb.game.utils.Sprite;


/**
 *
 */
final class BackgroundLayer extends ParallaxLayer
{
    private final static int ROW_HEIGHT = 14;

    private final static int ROW_WIDTH = Game.VIRTUAL_CANVAS_WIDTH;

    private final static int BITMAP_HEIGHT = 196;

    private final static int BITMAP_WIDTH = ROW_WIDTH;

    private final static int NUMBER_OF_ROWS_IN_BITMAP = BITMAP_HEIGHT
            / ROW_HEIGHT;

    private static final int BLANK_ROW_1_INDEX = NUMBER_OF_ROWS_IN_BITMAP - 1;

    private static final int BLANK_ROW_2_INDEX = NUMBER_OF_ROWS_IN_BITMAP - 2;

    private final static int NUMBER_OF_ROWS = Game.VIRTUAL_CANVAS_HEIGHT
            / ROW_HEIGHT + 2;

    private static final float BACKGROUND_DEPTH = 0.75f;

    private final Random random = new Random();

    private final RingListI rowList;

    private int topmostVisibleRow;

    private final Sprite backgroundSprite;

    BackgroundLayer(Resources resources)
    {
        super(BACKGROUND_DEPTH, Game.VIRTUAL_CANVAS_WIDTH, Game.VIRTUAL_CANVAS_HEIGHT);

        final Bitmap bitmap = BitmapFactory.decodeResource(resources,
                R.drawable.background);
        this.backgroundSprite = new Sprite(bitmap, ROW_WIDTH, ROW_HEIGHT);

        this.rowList = new RingListI(NUMBER_OF_ROWS);
        initializeList();
    }

    private final void initializeList()
    {
        // the first row:
        int firstRowFrame = this.random.nextInt(NUMBER_OF_ROWS_IN_BITMAP);
        if (firstRowFrame % 2 != 0) {
            firstRowFrame += 1;
            firstRowFrame %= NUMBER_OF_ROWS_IN_BITMAP;
        }
        this.rowList.add(firstRowFrame);
        // remaining initial rows:
        doUpdate();
    }

    final void doUpdate()
    {
        final int topmostRowIndex = 1
                + (getViewY() + Game.VIRTUAL_CANVAS_HEIGHT) / ROW_HEIGHT;

        if (topmostRowIndex <= this.topmostVisibleRow) {
            return;
        }

        while (this.topmostVisibleRow < topmostRowIndex) {
            this.topmostVisibleRow += 1;
            int randomFrame;
            if (this.topmostVisibleRow % 2 == 0) {
                if (this.random.nextBoolean()) {
                    randomFrame = BLANK_ROW_2_INDEX;
                } else {
                    randomFrame = this.random.nextInt(NUMBER_OF_ROWS_IN_BITMAP);
                    if (randomFrame % 2 != 0) {
                        randomFrame += 1;
                        randomFrame %= NUMBER_OF_ROWS_IN_BITMAP;
                    }
                }
            } else {
                if (this.random.nextBoolean()) {
                    randomFrame = BLANK_ROW_1_INDEX;
                } else {
                    randomFrame = this.random.nextInt(NUMBER_OF_ROWS_IN_BITMAP);
                    if (randomFrame % 2 == 0) {
                        randomFrame += 1;
                        randomFrame %= NUMBER_OF_ROWS_IN_BITMAP;
                    }
                }
            }
            this.rowList.add(randomFrame);
        }
    }

    final void doDraw(Canvas canvas)
    {
        int y = (getViewY() + Game.VIRTUAL_CANVAS_HEIGHT)
                - (this.topmostVisibleRow * ROW_HEIGHT);

        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            this.backgroundSprite.setPosition(0, y);
            this.backgroundSprite.setFrame(this.rowList.get(i));
            this.backgroundSprite.doDraw(canvas);
            y += ROW_HEIGHT;
        }
    }
}

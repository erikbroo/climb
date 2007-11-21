package com.googlecode.climb.game;

import java.util.Random;
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
final class ForegroundLayer extends ParallaxLayer
{
    private final static int CELL_HEIGHT = 28;

    private final static int CELL_WIDTH = 15;

    private final static int FRAMES_IN_BITMAP = 12;

    private final static int NUMBER_OF_CELLS = Game.VIRTUAL_CANVAS_HEIGHT
            / CELL_HEIGHT + 2;

    private static final float FOREGROUND_DEPTH = 1.25f;

    private final Random random = new Random();

    private final RingListI cellListLeft;

    private final RingListI cellListRight;

    private int topmostVisibleCell;

    private final Sprite backgroundSprite;

    ForegroundLayer(Game view)
    {
        super(FOREGROUND_DEPTH);

        final Bitmap bitmap = BitmapFactory.decodeResource(view.getResources(),
                R.drawable.foreground);
        this.backgroundSprite = new Sprite(bitmap, CELL_WIDTH, CELL_HEIGHT);

        this.cellListLeft = new RingListI(NUMBER_OF_CELLS);
        this.cellListRight = new RingListI(NUMBER_OF_CELLS);
        initializeList();
    }

    private final void initializeList()
    {
        // the first cells:
        int firstCellLeftFrame = this.random.nextInt(FRAMES_IN_BITMAP);
        int firstCellRightFrame = this.random.nextInt(FRAMES_IN_BITMAP);
        if (firstCellLeftFrame % 2 != 0) {
            firstCellLeftFrame += 1;
            firstCellLeftFrame %= FRAMES_IN_BITMAP;
        }
        if (firstCellRightFrame % 2 != 0) {
            firstCellRightFrame += 1;
            firstCellRightFrame %= FRAMES_IN_BITMAP;
        }
        this.cellListLeft.add(firstCellLeftFrame);
        this.cellListRight.add(firstCellRightFrame);
        // remaining initial rows:
        doUpdate();
    }

    final void doUpdate()
    {
        final int topmostCellIndex = 1
                + (getViewY() + Game.VIRTUAL_CANVAS_HEIGHT) / CELL_HEIGHT;

        if (topmostCellIndex <= this.topmostVisibleCell) {
            return;
        }

        while (this.topmostVisibleCell < topmostCellIndex) {
            this.topmostVisibleCell += 1;
            int randomFrameLeft = this.random.nextInt(FRAMES_IN_BITMAP);
            int randomFrameRight = this.random.nextInt(FRAMES_IN_BITMAP);
            if (((this.topmostVisibleCell % 2 == 0) && (randomFrameLeft % 2 != 0))
                    || ((this.topmostVisibleCell % 2 != 0) && (randomFrameLeft % 2 == 0))) {
                randomFrameLeft += 1;
                randomFrameLeft %= FRAMES_IN_BITMAP;
            }
            if (((this.topmostVisibleCell % 2 == 0) && (randomFrameRight % 2 != 0))
                    || ((this.topmostVisibleCell % 2 != 0) && (randomFrameRight % 2 == 0))) {
                randomFrameRight += 1;
                randomFrameRight %= FRAMES_IN_BITMAP;
            }
            this.cellListLeft.add(randomFrameLeft);
            this.cellListRight.add(randomFrameRight);
        }
    }

    final void doDraw(Canvas canvas)
    {
        int y = (getViewY() + Game.VIRTUAL_CANVAS_HEIGHT)
                - (this.topmostVisibleCell * CELL_HEIGHT);

        for (int i = 0; i < NUMBER_OF_CELLS; i++) {
            this.backgroundSprite.setPosition(0, y);
            this.backgroundSprite.doDraw(canvas, this.cellListLeft.get(i));
            this.backgroundSprite.setPosition(Game.VIRTUAL_CANVAS_WIDTH
                    - CELL_WIDTH, y);
            this.backgroundSprite.doDraw(canvas, this.cellListRight.get(i));
            y += CELL_HEIGHT;
        }
    }
}

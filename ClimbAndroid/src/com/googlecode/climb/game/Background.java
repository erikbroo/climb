package com.googlecode.climb.game;
// package de.coskunscastle.climb.game;
//
// import java.util.Random;
// import android.graphics.Bitmap;
// import android.graphics.BitmapFactory;
//
//
// /**
// *
// */
// final class Background
// {
// private final int rows;
//
// private final Random random;
//
// private final Bitmap bitmap;
//
// Background(int resourceID, int rows, Random random, Game view)
// {
// // super(1, rows, image, 176, 14);
// this.rows = rows;
// this.random = random;
//
// this.bitmap = BitmapFactory.decodeResource(view.getResources(),
// resourceID);
//
// setCells();
// }
//
// private final void setCells()
// {
// for (int i = 0; i < this.rows; i++) {
// int r = Math.abs(this.random.nextInt() % 14) + 1;
// if (Math.abs(this.random.nextInt() % 5) == 0) {
// if (i % 2 == 0) {
// r = 14;
// } else {
// r = 13;
// }
// } else {
// if ((i % 2 == 0) && (r % 2 != 0)) {
// i--;
// continue;
// } else if ((i % 2 != 0) && (r % 2 == 0)) {
// i--;
// continue;
// }
// }
// setCell(0, i, r);
// }
// }
//
// }

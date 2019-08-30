package com.example.myapplication.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.shapes.RoundRectShape;

class ImageUtils {
          static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int topLeftX, int topLeftY, int topRightX, int topRightY, int bottomRightX, int bottomRightY, int bottomLeftX, int bottomLeftY) {
              final Paint paint = new Paint();
              final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
              Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
              Canvas canvas = new Canvas(output);
              // the float array passed to this function defines the x/y values of the corners
              // it starts top-left and works clockwise
              // so top-left-x, top-left-y, top-right-x etc
              RoundRectShape rrs = new RoundRectShape(new float[]{topLeftX, topLeftY, topRightX, topRightY, bottomRightX, bottomRightY, bottomLeftX, bottomLeftY}, null, null);
              canvas.drawARGB(0, 0, 0, 0);
              paint.setAntiAlias(true);
              paint.setColor(0xFF000000);
              rrs.resize(bitmap.getWidth(), bitmap.getHeight());
              rrs.draw(canvas, paint);
              paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
              canvas.drawBitmap(bitmap, rect, rect, paint);
              return output;
         }
    }
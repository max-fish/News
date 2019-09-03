package com.example.myapplication.ui.newListFragment;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.shapes.RoundRectShape;

public class CircleTransform implements com.squareup.picasso.Transformation {
    private float topLeftX;
    private float topLeftY;
    private float topRightX;
    private float topRightY;
    private float bottomRightX;
    private float bottomRightY;
    private float bottomLeftX;
    private float bottomLeftY;


    // radius is corner radii in dp
    // margin is the board in dp
    public CircleTransform(final float topLeftX, float topLeftY, float topRightX, float topRightY, float bottomRightX, float bottomRightY, float bottomLeftX, float bottomLeftY) {
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        this.topRightX = topRightX;
        this.topRightY = topRightY;
        this.bottomRightX = bottomRightX;
        this.bottomRightY = bottomRightY;
        this.bottomLeftX = bottomLeftX;
        this.bottomLeftY = bottomLeftY;
    }

    @Override
    public Bitmap transform(final Bitmap source) {
        final Paint paint = new Paint();

        final Rect rect = new Rect(0, 0, source.getWidth(), source.getHeight());

        Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        // the float array passed to this function defines the x/y values of the corners
        // it starts top-left and works clockwise
        // so top-left-x, top-left-y, top-right-x etc
        RoundRectShape rrs = new RoundRectShape(new float[]{topLeftX, topLeftY, topRightX, topRightY, bottomRightX, bottomRightY, bottomLeftX, bottomLeftY}, null, null);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setAntiAlias(true);
        paint.setColor(0xFF000000);
        rrs.resize(source.getWidth(), source.getHeight());
        rrs.draw(canvas, paint);
        paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, rect, rect, paint);

        if (source != output) {
            source.recycle();
        }
        return output;
    }

    @Override
    public String key() {
        return "rounded";
    }
}
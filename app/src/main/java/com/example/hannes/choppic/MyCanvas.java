package com.example.hannes.choppic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.Console;
import java.util.HashSet;

/**
 * Created by Hannes on 12.05.2019.
 */
public class MyCanvas extends View {

    int[][] pixelsPhoto;
    TriangleList triangles;

    public MyCanvas(Context context, int[][] pixelsPhoto) {
        super(context);
        this.pixelsPhoto = pixelsPhoto;

        triangles = new TriangleList(pixelsPhoto);

        double splitPoint = Math.random() / 2 + 0.25;
        int w = pixelsPhoto[0].length;
        int h = pixelsPhoto.length;
        Log.d("log", "" + (getHeight() * splitPoint));
        triangles.add(new Triangle(0, 0, w-1, (int) (h * splitPoint), w-1, 0));
        triangles.add(new Triangle(0, 0, w-1, (int) (h * splitPoint), 0, h-1));
        triangles.add(new Triangle(w-1, h-1, w-1, (int) (h * splitPoint), 0, h-1));


        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    HashSet<Triangle> trianglesAtPoint = triangles.getTrianglesFromPoint((int) event.getX(), (int) event.getY());
                    for (Triangle splitTriangle: trianglesAtPoint){
                        try {
                            triangles.split(splitTriangle);
                        } catch (NullPointerException e) {
                            Log.e("exc", "dreieick teilen failed");
                        }
                    }
                    invalidate();
                }
                return false;
            }
        });

        int count = Integer.parseInt(MainActivity.editText.getText().toString());
        w:while (true){
            HashSet<Triangle> triangles2 = new HashSet<Triangle>();
            triangles2.addAll(triangles);
            for (Triangle triangle: triangles2){
                if (count<1)
                    break w;
                triangles.split(triangle);
                count--;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint wallpaint = new Paint();
        Path wallpath = new Path();

        for (Triangle triangle : triangles) {

            wallpath.reset();
            wallpath.moveTo(triangle.x[0], triangle.y[0]);
            wallpath.lineTo(triangle.x[1], triangle.y[1]);
            wallpath.lineTo(triangle.x[2], triangle.y[2]);
            wallpath.lineTo(triangle.x[0], triangle.y[0]);

            wallpaint.setColor(triangle.getColor());
            wallpaint.setStyle(Paint.Style.FILL);
            canvas.drawPath(wallpath, wallpaint);

            /*wallpaint.setColor(Color.WHITE);
            wallpaint.setStyle(Paint.Style.STROKE);
            wallpaint.setStrokeWidth(4f);
            canvas.drawPath(wallpath, wallpaint);*/

            //Log.d("log", "dreieck angezeigt, koordinaten: (" + triangle.x[0] + "|" + triangle.y[0] + "), (" + triangle.x[1] + "|" + triangle.y[1] + "), (" + triangle.x[2] + "|" + triangle.y[2] + ")");
        }
        Log.d("log", triangles.size() + " dreiecke angezeigt");
    }

}
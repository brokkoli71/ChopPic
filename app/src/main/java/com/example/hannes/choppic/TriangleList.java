package com.example.hannes.choppic;

import android.graphics.Color;
import android.util.Log;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * Created by Hannes on 12.05.2019.
 */

public class TriangleList extends HashSet<Triangle> {
    private int[][] pixelsPhoto;

    public TriangleList(int[][] pixelsPhoto) {
        this.pixelsPhoto = pixelsPhoto;
    }

    public void split(Triangle triangle) throws NullPointerException{
        remove(triangle);

        TriangleChildren triangleChildren = triangle.getSubTriangles();
        add(triangleChildren.getT1());
        add(triangleChildren.getT2());

    }

    public HashSet<Triangle> getTrianglesFromPoint(int xP, int yP) {
        HashSet<Triangle> result = new HashSet<Triangle>();
        for (Triangle triangle : this) {
            for (int i = 0; i < 3; i++) {
                if (Math.abs(triangle.x[i] - xP) < 100 && Math.abs(triangle.y[i] - yP) < 100)
                    result.add(triangle);
            }
            if (Math.abs(triangle.getMitteX() - xP) < 100 && Math.abs(triangle.getMitteX() - yP) < 100)
                result.add(triangle);
        }
        return result;
    }

    @Override
    public boolean add(Triangle triangle) {
        Random rnd = new Random();
        //triangle.setColor(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
        triangle.setColor(getAvgColor(triangle));
        return super.add(triangle);
    }



    private int getAvgColor(Triangle triangle){
        //get top, middle, bottom, point at height of middle on border
        int p1x, p1y, p2x, p2y, p3x, p3y, p4x, p4y;

        int top, mid, bot;

        if (triangle.y[0] < triangle.y[1]) {
            if (triangle.y[0] < triangle.y[2]) {
                top = 0;
                mid = triangle.y[1] < triangle.y[2]?1:2;
            }else {
                top = 2;
                mid = 0;
            }
        }else {
            if (triangle.y[1] < triangle.y[2]) {
                top = 1;
                mid = triangle.y[0] < triangle.y[2]?0:2;
            }else {
                top = 2;
                mid = 1;
            }
        }
        bot = 3 - mid - top;

        p1x = triangle.x[top];
        p1y = triangle.y[top];
        p2x = triangle.x[mid];
        p2y = triangle.y[mid];
        p3x = triangle.x[bot];
        p3y = triangle.y[bot];

        p4y = p2y;
        try{
            p4x = p3x + (p4y-p3y)*(p1x-p3x)/(p1y-p3y);
        }catch (ArithmeticException exc){
            p4x = p3x;
        }

        //Log.d("log", "dreieck berechnen, koordinaten: (" + p1x + "|" + p1y + "), (" + p2x + "|" + p2y + "), (" + p3x + "|" + p3y + "), (" + p4x + "|" + p4y + ")");

                // Standard Algorithm
        //top triangle : p1, p2, p4
        float invslope1 = (float)(p2x - p1x) / (p2y - p1y);
        float invslope2 = (float)(p4x - p1x) / (p4y - p1y);

        float curx1 = p1x;
        float curx2 = p1x;

        int pixelCount = 0;
        long a = 0;
        long r = 0;
        long g = 0;
        long b = 0;

        for (int i = p1y; i <= p2y; i++){

            for (int j = (int) curx1; j <= (int) curx2; j++) {
                //Log.d("var", "i:"+i + "j:"+j);
                try {
                    a += Color.alpha(pixelsPhoto[i][j]);
                    r += Color.red(pixelsPhoto[i][j]);
                    g += Color.green(pixelsPhoto[i][j]);
                    b += Color.blue(pixelsPhoto[i][j]);
                    pixelCount++;
                }catch (ArrayIndexOutOfBoundsException exc){
                    Log.e("err", "ArrayIndexOutOfBoundsException at i = "+i+" j = "+j);
                }
            }
            curx1 += invslope1;
            curx2 += invslope2;
        }


        //bot triangle : p2, p3, p4

        invslope1 = (float)(p3x - p2x) / (p3y - p2y);
        invslope2 = (float)(p3x - p4x) / (p3y - p4y);

        curx1 = p3x;
        curx2 = p3x;


        for (int i = p3y; i > p2y; i--) {
            for (int j = (int) curx1; j <= (int) curx2; j++) {
                try {
                    //Log.d("var", "i:"+i + "j:"+j);
                    a += Color.alpha(pixelsPhoto[i][j]);
                    r += Color.red(pixelsPhoto[i][j]);
                    g += Color.green(pixelsPhoto[i][j]);
                    b += Color.blue(pixelsPhoto[i][j]);
                    pixelCount++;
                }catch (ArrayIndexOutOfBoundsException exc){
                    Log.e("err", "ArrayIndexOutOfBoundsException at i = "+i+" j = "+j);
                }
            }
            curx1 -= invslope1;
            curx2 -= invslope2;
            }

        Log.d("var", "abs: a= "+a+" r= "+r+" g= "+g+" b= "+b+" counter= " + pixelCount);
        try {
            a /= pixelCount;
            r /= pixelCount;
            g /= pixelCount;
            b /= pixelCount;
        }catch (ArithmeticException e){
            a=r=g=b=0;
        }

        Log.d("var", "avg: a= "+a+" r= "+r+" g= "+g+" b= "+b + " color: "+Color.valueOf(Color.rgb(r, g, b)));

        return Color.argb((int)a, (int)r, (int)g, (int)b);//Color.pack(255f/r, 255f/g, 255f/b);
    }
}



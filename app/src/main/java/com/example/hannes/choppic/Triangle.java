package com.example.hannes.choppic;

import android.graphics.Color;
import android.util.Log;

import java.util.Random;

/**
 * Created by Hannes on 12.05.2019.
 */

public class Triangle {

    int[] x, y;
    private int color;
    private int mitteX, mitteY;



    public Triangle(int x1, int y1, int x2, int y2, int x3, int y3){
        x = new int[]{x1, x2, x3};
        y = new int[]{y1, y2, y3};
        mitteX = (x1 + x2 + x3)/3;
        mitteY = (y1 + y2 + y3)/3;
    }

    public Triangle(int x1, int y1, int x2, int y2, int x3, int y3, int color){
        this(x1, y1, x2, y2, x3, y3);
        setColor(color);
    }

    public void setColor(int c){
        color = c;
    }

    public int getColor() {
        return color;
    }

    public int getMitteY() {
        return mitteY;
    }

    public int getMitteX() {
        return mitteX;
    }

    public TriangleChildren getSubTriangles(){

        // index an welcher ecke gespalten werden soll (gegenüber der längsten Kante)
        int ecke1;
        if(distance(0, 1) > distance(1, 2))
            if (distance(0, 1) > distance(0, 2))
                ecke1 = 2;
            else
                ecke1 = 1;
        else
            if(distance(1, 2) > distance(0, 2))
                ecke1 = 0;
            else
                ecke1 = 1;


        // index restlicher Ecken
        int ecke2 = (ecke1+1) % 3;
        int ecke3 = (ecke1+2) % 3;

        // Anteile ersten Dreiecks an Strecke gegenüber der gespaltenen Ecke
        double a = Math.random()/2 + 0.25;
        //Log.d("log", "split at "+ecke1 +ecke2 +ecke3);

        int xNeueEcke = (int) (a*x[ecke2] + (1-a)*x[ecke3]);
        int yNeueEcke = (int) (a*y[ecke2] + (1-a)*y[ecke3]);

        Triangle t1 = new Triangle(x[ecke1], y[ecke1], x[ecke2], y[ecke2], xNeueEcke, yNeueEcke);
        Triangle t2 = new Triangle(x[ecke1], y[ecke1], x[ecke3], y[ecke3], xNeueEcke, yNeueEcke);

        return new TriangleChildren(t1, t2);
    }

    private double distance(int i1, int i2){
        return Math.sqrt(Math.pow(x[i1]-x[i2], 2) + Math.pow(y[i1]-y[i2], 2));
    }


    //zu ineffizient -> ersetzt, nicht mehr benoetigt
    public boolean pointInside(int pX, int pY){
        float d1, d2, d3;
        boolean hasNeg, hasPos;

        d1 = sign(pX, pY, x[0], y[0], x[1], y[1]);
        d2 = sign(pX, pY, x[1], y[1], x[2], y[2]);
        d3 = sign(pX, pY, x[2], y[2], x[0], y[0]);

        hasNeg = (d1 < 0) || (d2 < 0) || (d3 < 0);
        hasPos = (d1 > 0) || (d2 > 0) || (d3 > 0);

        return !(hasNeg && hasPos);
    }

    private float sign (int p1x, int p1y, int p2x, int p2y, int p3x, int p3y){
        return (p1x - p3x) * (p2y - p3y) - (p2x - p3x) * (p1y - p3y);
    }


}

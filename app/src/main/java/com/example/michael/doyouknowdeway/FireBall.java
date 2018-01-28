package com.example.michael.doyouknowdeway;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.MediaPlayer;

/**
 * Created by michael on 1/28/18.
 */


public class FireBall {
    int Xval;
    int Yval;
    int screenWidth;
    int screenHeight;
    boolean isShooting = false;
    public Bitmap fire;
    public Bitmap fireResized;

    private Rect hitBox;
    private MediaPlayer fireNoise;

    public FireBall(Context context, int screenX, int screenY) {
        screenWidth = screenX;
        screenHeight = screenY;
        fire = BitmapFactory.decodeResource(context.getResources(), R.drawable.fire_projectile_1);
        fireResized = Bitmap.createScaledBitmap(fire, 100, 100, false);
        fireNoise = MediaPlayer.create(context, R.raw.fire_ball_sound);
    }

    public void update() {
        if(isShooting){
            Xval += 50;
            if(Xval > screenWidth) {
                isShooting = false;
            }
        }
    }

    public void setOnScreen(boolean check){
        //sets start location of the ball
        Xval = 200;
        Yval = screenHeight - 300;
        fireNoise.start();
        isShooting = check;

    }

    public Bitmap getImage(){
        return fireResized;
    }

    public Rect getHitBox() {
        return hitBox;
    }

    public int getXVal() {
        return Xval;
    }

    public int getYVal() {
        return Yval;
    }
}

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
    Player player;

    private Rect hitBox;
    private MediaPlayer fireNoise;

    public FireBall(Context context, int screenX, int screenY) {
        screenWidth = screenX;
        screenHeight = screenY;
        fire = BitmapFactory.decodeResource(context.getResources(), R.drawable.fire_projectile_1);
        fireResized = Bitmap.createScaledBitmap(fire, 100, 100, false);
        fireNoise = MediaPlayer.create(context, R.raw.fire_ball_sound);
        player = new Player(context, screenX, screenY);
    }

    public void update(Player player) {
        if(isShooting){
            Xval += 50;
            Yval = player.getYVal();
            if(Xval > screenWidth) {
                isShooting = false;
                Xval = 250;

            }
        }
    }

    public void setOnScreen(boolean check){
        //sets start location of the ball
        Yval = player.getYVal();
        Xval = player.getXVal();
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

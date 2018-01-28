package com.example.michael.doyouknowdeway;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.MediaPlayer;

/**
 * Created by michael on 1/27/18.
 */

public class Player {


  int Xval;
  int Yval;
  int screenWidth;
  int screenHeight, jumpCount = 0;
  boolean isJumping = false;
  boolean isFalling = false;
  Bitmap playerImage;
  Bitmap playerImageResized;
  private Rect hitBox;
  private MediaPlayer jumpNoise;

  public Player(Context context, int screenX, int screenY){
    screenWidth = screenX;
    screenHeight = screenY;
    Xval = 250;
    Yval = 720;
    playerImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.knuckles_run);
    playerImageResized = Bitmap.createScaledBitmap(playerImage, 200, 200, false);
    hitBox = new Rect(Xval, Yval, playerImageResized.getWidth(), playerImageResized.getHeight());
  }

  public void update(){
    int currentY = playerImageResized.getHeight();
    if(isJumping){
      Yval -= 30;
      if(Yval <= currentY + 10) {
        isJumping = false;
        isFalling = true;
      }
    }

    else if(isFalling) {
      isJumping = false;
      Yval += 30;
      //change to when ground is hit
      if(GameView.isColliding){
        isFalling = false;
        jumpCount = 0;
      }
    }
    //update the hitbox location with the ball as it moves
    hitBox.top = Yval;
    hitBox.bottom = Yval + playerImageResized.getHeight();
    hitBox.left = Xval;
    hitBox.right = Xval + playerImageResized.getWidth();
  }

  public Bitmap getBitmap()
  {
    return playerImageResized;
  }

  public Rect getHitBox(){
    return hitBox;
  }

  public int getXVal(){
    return Xval;
  }

  public int getYVal(){
    return Yval;
  }

  public int getJumpCount()
  {
    return jumpCount;
  }

  public void incrJump()
  {
    jumpCount++;
  }

}
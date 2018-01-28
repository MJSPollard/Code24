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

  private Bitmap playerImage;
  private Bitmap playerJumpImage;
  private Bitmap playerImageResized;
  int Xval;
  int Yval;
  int screenWidth;
  int screenHeight;
  boolean isJumping = false;
  boolean isFalling = false;
  private Rect hitBox;
  private MediaPlayer jumpNoise;

  public Player(Context context, int screenX, int screenY){
    screenWidth = screenY;
    screenHeight = screenX;


    playerJumpImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.knucklesjump);
    playerImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.ugandan_knuckle);
    playerImageResized = Bitmap.createScaledBitmap(playerImage, 200, 200, false);

    Xval = 200;
    Yval = screenHeight - 1200;
  }

  public void update(){
    if(isJumping){

      playerImageResized = Bitmap.createScaledBitmap(playerJumpImage, 200, 200, false);
      Yval -= 30;
      if(Yval <= 400) {
        isJumping = false;
        isFalling = true;
      }
    }

    if(isFalling) {
      Yval += 30;
      if(Yval >= screenHeight - 1200){
        isFalling = false;
        playerImageResized = Bitmap.createScaledBitmap(playerImage, 200, 200, false);
      }
    }


  }


  public Bitmap getPlayerImage(){
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



}


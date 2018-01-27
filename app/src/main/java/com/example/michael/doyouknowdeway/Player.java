package com.example.michael.doyouknowdeway;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

/**
 * Created by michael on 1/27/18.
 */

public class Player {

  private Bitmap playerImage;
  private Bitmap playerImageResized;
  int Xval;
  int Yval;
  int screenWidth;
  int screenHeight;
  boolean isJumping = false;
  boolean isFalling = false;
  private Rect hitBox;

  public Player(Context context, int screenX, int screenY){
    screenWidth = screenY;
    screenHeight = screenX;

    playerImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.grass_block);
    playerImageResized = Bitmap.createScaledBitmap(playerImage, 60, 300, false);

    Xval = 200;
    Yval = screenHeight - 1200;


  }
  public void update(){
    if(isJumping){
      Yval -= 20;
      if(Yval <= 100) {
        isJumping = false;
        isFalling = true;
      }
    }

    if(isFalling) {
      Yval += 20;
      if(Yval >= screenHeight - 1200){
        isFalling = false;
      }
    }

  }

//  public void update() {
//
//    if (isJumping) {
//      Yval -= 200;
//
//    }
//    if (jumpTime % 20 == 0) {
//      isJumping = false;
//      isFalling = true;
//    }
//    if (isFalling) {
//      Yval += 30;
//      jumpTime--;
//
//      //if it lands
//      if (jumpTime == 1) {
//        isFalling = false;
//      }
//
//    }
//  }


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


package com.example.michael.doyouknowdeway;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.MediaPlayer;

/**
 * Created by michael on 1/27/18.
 * Player class for storing data on player location in image pane
 */

public class Player {

  //all initialized values for tracking location of the character in the app
  int Xval;
  int Yval;
  int screenWidth;
  int screenHeight;
  boolean isJumping = false;
  boolean isFalling = false;
  Bitmap playerImage;
  Bitmap playerImageResized;
  private Rect hitBox;
  private MediaPlayer jumpNoise;

  //player constructor, creates the player in the image pane based on the size of the screen
  //the app is run on
  public Player(Context context, int screenX, int screenY){
    screenWidth = screenX;
    screenHeight = screenY;
    Xval = 250;
    Yval = 720;
    playerImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.knuckles_run);
    playerImageResized = Bitmap.createScaledBitmap(playerImage, 200, 200, false);
    hitBox = new Rect(Xval, Yval, playerImageResized.getWidth(), playerImageResized.getHeight());
  }

  //the main update method for moving the character up and down for jumps
  public void update(){
    if(isJumping){
      Yval -= 30;
      if(Yval <= 400) {
        isJumping = false;
        isFalling = true;
      }
    }

    //the compliment to update, handles falling of the character
    else if(isFalling) {
      isJumping = false;
      Yval += 30;
      //change to when ground is hit
      if(GameView.isColliding){
        isFalling = false;
      }
    }
    //update the hitbox location with the ball as it moves
    hitBox.top = Yval;
    hitBox.bottom = Yval + playerImageResized.getHeight();
    hitBox.left = Xval;
    hitBox.right = Xval + playerImageResized.getWidth();
  }

  //loads in the image for the ugandan knuckles
  public Bitmap getBitmap()
  {
    return playerImageResized;
  }

  //hitbox for taking damage and collecting tide pods
  public Rect getHitBox(){
    return hitBox;
  }

  //getter methods
  public int getXVal(){
    return Xval;
  }

  public int getYVal(){
    return Yval;
  }

}
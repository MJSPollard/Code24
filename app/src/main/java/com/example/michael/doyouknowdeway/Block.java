package com.example.michael.doyouknowdeway;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Hughman on 1/27/2018.
 * the Block class creates the filler image for the tiles in the class
 */

public class Block {

    //Bitmaps for the images initialized
    private int x, y;
    private Bitmap blockImage;
    private Bitmap blockImageResized;
    private Boolean harmful, pod;


    //block constuctor, to be called in the tile class
    Block(Block copy, int x, int y)
    {
        this.blockImage = copy.blockImage;
        this.blockImageResized = copy.blockImageResized;
        this.harmful = copy.harmful;
        this.pod = copy.pod;
        this.x = x;
        this.y = y;
    }

    //to be used again by the tile class, sets the image and properties on a certain block based
    //on randomization algorithms
    Block(Context context, int selection)
    {
        pod = false;
        harmful = false;
        if(selection == 0)
        {
            blockImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.grass_block);
            blockImageResized = Bitmap.createScaledBitmap(blockImage, 100, 200, false);
        }
        else if(selection == 1)
        {
            blockImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.lava_block);
            blockImageResized = Bitmap.createScaledBitmap(blockImage, 100, 200, false);
            harmful = true;
        }
        else if(selection == 2){
            blockImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.detergent_pod);
            blockImageResized = Bitmap.createScaledBitmap(blockImage, 200, 200, false);
            pod = true;
        }
    }

    //getter methods for tile properties
    public Bitmap getImage()
    {
        return blockImageResized;
    }

    public Boolean getHarmful()
    {
        return harmful;
    }

    Boolean isPod()
    {
        return pod;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

//    public int[] getPosition()
//    {
//        int[] position = new int[2];
//        position[0] = x;
//        position[1] = y;
//        return position;
//    }
}

package com.example.michael.doyouknowdeway;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Hughman on 1/27/2018.
 */

public class Block {

    private int x, y;
    private Bitmap blockImage;
    private Bitmap blockImageResized;
    private Boolean harmful, pod;


    Block(Block copy, int x, int y)
    {
        this.blockImage = copy.blockImage;
        this.blockImageResized = copy.blockImageResized;
        this.harmful = copy.harmful;
        this.pod = copy.pod;
        this.x = x;
        this.y = y;
    }
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

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
    private Boolean harmful;

    public Block(Context context, int selection)
    {
        if(selection == 0)
        {
            blockImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.grass_block);
            blockImageResized = Bitmap.createScaledBitmap(blockImage, 60, 700, false);
            harmful = false;
        }
        else if(selection == 1)
        {
            blockImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.lava_block);
            blockImageResized = Bitmap.createScaledBitmap(blockImage, 60, 700, false);
            harmful = true;
        }
    }

    public Boolean getHarmful()
    {
        return harmful;
    }

    public void setPosition(int x,int y)
    {
        self.x = x;
        self.y = y;
    }
}

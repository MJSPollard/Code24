package com.example.michael.doyouknowdeway;

import android.content.Context;

/**
 * Created by Hughman on 1/27/2018.
 */

public class Tile {
    int length, height; //might make height constant variable
    Block[] blocks = new Block[5];
    Block[][] tileMap;


    //Creates the initial starting tile
    public Tile(Context context, int number_block_types)
    {
        //create block classes?
        for(int i = 0; i < number_block_types; i++)
        {
           // blocks[i] = new Block("grass")
            blocks[i] = new Block(context, i);
            length = 50;
            height = 50;
            tileMap = new Block[length][height];
        }
    }
    public Tile(int length, int height)
    {
        this.length = length;
        this.height = height;
    }

    private void fillTile()
    {
        //add block to Tile
        for(int i = 0; i < length; i++)
        {
            for(int j = 0; j < height; j++)
            {
                if(i == 25 && j == 25)
                {
                    tileMap[i][j] = blocks[0];
                }
            }
        }
    }

}

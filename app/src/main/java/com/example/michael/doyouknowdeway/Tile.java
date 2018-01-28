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
    public Tile(Context context, int number_block_types, int length, int height)
    {
        System.out.println("Height = " + height);
        //create block classes?
        for(int i = 0; i < number_block_types; i++)
        {
           // blocks[i] = new Block("grass")
            blocks[i] = new Block(context, i);
            tileMap = new Block[length][height];
        }
    }
    public Tile(int length, int height)
    {
        this.length = length;
        this.height = height;
    }

    public void fillTile()
    {
        //add block to Tile
        for(int i = 0; i < tileMap.length; i++)
        {
            for(int j = 0; j < tileMap[i].length; j++)
            {
             //   System.out.println("Height = " + height);
                if(i == 1)
                {
                    tileMap[i][j] = new Block(blocks[0], i, j);
                }
            }
        }
    }

    public int getHeight()
    {
        return height;
    }

    public int getLength()
    {
        return length;
    }

    public int[] getBlockPosition(int x, int y)
    {
        return tileMap[x][y].getPosition();
    }

    public Block getBlock(int x, int y)
    {
        return tileMap[x][y];
    }

}

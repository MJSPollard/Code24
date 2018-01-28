package com.example.michael.doyouknowdeway;

import android.content.Context;

/**
 * Created by Hughman on 1/27/2018.
 */

public class Tile {
    private int length, height; //might make height constant variable
    private Block[] blocks = new Block[5];
    private Block[][] tileMap;
    //private int[] possible_next_tile;


    //Creates the initial starting tile
    Tile(Context context, int number_block_types, int length, int height)
    {
        //create block classes?
        for(int i = 0; i < number_block_types; i++) {
            // blocks[i] = new Block("grass")
            blocks[i] = new Block(context, i);
        }
            tileMap = new Block[(length + 100)/100][height/100];
            this.height = height/100;
            this.length = (length + 100)/100;
    }
    public Tile(int length, int height)
    {
        tileMap = new Block[(length + 100)/100][height/100];
        this.height = height/100;
        this.length = (length + 100)/100;
    }

    public void fillTile()
    {
        //add block to Tile
        for(int i = 0; i < tileMap.length; i++)
        {
            for(int j = 0; j < tileMap[i].length; j++)
            {
                if(j == tileMap[i].length - 1)
                {
                    if(i == 14)
                    {
                        tileMap[i][j] = new Block(blocks[1], i, j);
                    }
                    else {
                        tileMap[i][j] = new Block(blocks[0], i, j);
                    }
                }
            }
        }
    }

    int getHeight()
    {
        return height;
    }

    int getLength()
    {
        return length;
    }

    public int[] getBlockPosition(int x, int y)
    {
        return tileMap[x][y].getPosition();
    }

    Block getBlock(int x, int y)
    {
        return tileMap[x][y];
    }


    Tile getNextTile(int length, int height)
    {
        Tile nextTile = new Tile(length, height);
        return nextTile;
    }
}

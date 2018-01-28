package com.example.michael.doyouknowdeway;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Hughman on 1/27/2018.
 */

public class Tile {
    private int length, height;
    private Block[] blocks = new Block[5];
    private Block[][] tileMap;
    //private int[] possible_next_tile;
    private ArrayList<Double> tidePods;

    Tile(Tile copy)
    {
        this.length = copy.length;
        this.height = copy.height;
        this.blocks = copy.blocks;
        this.tileMap = copy.tileMap;
        this.tidePods = copy.tidePods;
    }

    //Creates the initial starting tile
    Tile(Context context, int number_block_types, int length, int height)
    {
        //create block classes
        for(int i = 0; i < number_block_types; i++) {
            blocks[i] = new Block(context, i);
        }
            tileMap = new Block[(length + 100)/100][height/100];
            this.height = height/100;
            this.length = (length + 100)/100;
    }
    Tile(int length, int height, Block[] blocks)
    {
        this.blocks = blocks;
        tileMap = new Block[length][height];
        this.height = height;
        this.length = length;
    }

    public void fillTile()
    {
        tidePods = new ArrayList<>();

        //add block to Tile
        for(int i = 0; i < tileMap.length; i++)
        {
            for(int j = 0; j < tileMap[i].length; j++)
            {
                if(j == tileMap[i].length - 1)
                {
                    if(i == 3) {
                        tileMap[i][j] = new Block(blocks[1], i, j);
                    }
                    else if(i == 9)
                    {
                        continue;
                    }
                    else {
                        tileMap[i][j] = new Block(blocks[0], i, j);
                    }
                }
                else if(j == tileMap[i].length - 3){
                     if(i == 13){
                        tileMap[i][j] = new Block(blocks[2], i, j);
                        double k = (double) j;
                        tidePods.add((double) i + (k/10.00));
                        tidePods.add((double) i + (j/10.00));
                    }
                }

                if(i == 8 && j >= tileMap[i].length - 3)
                {
                    tileMap[i][j] = new Block(blocks[0], i, j);
                }
                else if(i == 7 && j >= tileMap[i].length - 2)
                {
                    tileMap[i][j] = new Block(blocks[0], i, j);
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

    public void setNullBlock(int x, int y)
    {
        tileMap[x][y] = null;
        double compare = (double) x + (y/10.00);

        for(int i = 0; i < tidePods.size(); i++)
        {
            double comparitor = Math.abs(compare - tidePods.get(i));
            if(0 <= comparitor && comparitor < 1)
            {
                tidePods.remove(i);
                i--;
                break;
            }
        }
    }

//    public int[] getBlockPosition(int x, int y)
//    {
//        return tileMap[x][y].getPosition();
//    }

    Block getBlock(int x, int y)
    {
        return tileMap[x][y];
    }

    Tile getNextTile()
    {
        Tile nextTile = new Tile(length, height, blocks);
        nextTile.fillTile();
        return nextTile;
    }

    ArrayList<Double> getTidePods()
    {
        return tidePods;
    }
}

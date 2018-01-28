package com.example.michael.doyouknowdeway;

import android.content.Context;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Hughman on 1/27/2018.
 */

public class Tile {
    private int length, height;
    private Block[] blocks = new Block[5];
    private Block[][] tileMap;
    private ArrayList<Double> tidePods;
    private Random rand = new Random();

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
        int random_height = rand.nextInt(height - 4);
        int random_gap = rand.nextInt(length);
        int random_plume = rand.nextInt(length);

        if(random_height == 0)
        {
            random_height = 1;
        }
        if(random_gap == 0)
        {
            random_gap = 1;
        }
        if(random_gap - random_plume == 0)
        {
            random_gap += 1;
        }

        while(random_plume == random_gap)
        {
            random_plume = rand.nextInt(length);
        }
        int scale = Math.abs(random_gap - random_plume);


        //add block to Tile
        for(int i = 0; i < tileMap.length; i++)
        {
            int random_adjust = rand.nextInt(Math.abs(random_height));
            int random_adjust2 = rand.nextInt(random_gap);

            for(int j = 0; j < tileMap[i].length; j++)
            {
                int block_type = rand.nextInt(1);

                if(j == tileMap[i].length - 1)
                {
                    if(rand.nextBoolean()) {
                        makeBlock(i, j, 0);
                    }
                    else
                    {
                        makeBlock(1, j, 1);
                    }
                }
                if(i == random_gap && j < tileMap[i].length)
                {
                    tileMap[i][j] = null;
                }
                if(i == scale + random_adjust2 && j == tileMap[i].length - (random_adjust - 2))
                {
                    makeBlock(i, j, 2);
                }
                if(i == random_gap + random_adjust2 && j == tileMap[i].length - (random_adjust - 2))
                {
                    makeBlock(i, j, 2);
                }
                if(i == scale - 4 && j >= tileMap[i].length - random_height)
                {
                    makeBlock(i, j, 0);
                }
                if(i == scale - 3 && j >= tileMap[i].length - random_height - random_adjust)
                {
                    makeBlock(i, j, 0);
                }
                if(i == scale - 2 && j >= tileMap[i].length - random_height - random_adjust)
                {
                    makeBlock(i, j, 0);
                }
                if(i == scale - 1 && j >= tileMap[i].length - 2)
                {
                    makeBlock(i, j, 0);
                }
                if(i == random_gap - random_adjust2 && j == tileMap[i].length - random_adjust)
                {
                    makeBlock(i, j, block_type);
                }
                if(i <= 10 && j == tileMap[i].length - 1)
                {
                    makeBlock(i, j, 0);
                }
            }
        }
    }

    private void makeBlock(int i, int j, int k) {
        tileMap[i][j] = new Block(blocks[k], i, j);
        if(k == 3)
        {
            tidePods.add((double) i + (j/10.00));
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
        Tile nextTile = new Tile(this);
        int randomizer = rand.nextInt(length/10);
        if(rand.nextBoolean())
        {
            nextTile = new Tile(length + randomizer, height, blocks);
        }
        else
        {
           if(length - randomizer <= 20)
           {
               randomizer = rand.nextInt(randomizer);
               nextTile = new Tile(length + randomizer, height, blocks);
           }
           else
           {
               nextTile = new Tile(length - randomizer, height, blocks);
           }
        }
        nextTile.fillTile();
        return nextTile;
    }

    ArrayList<Double> getTidePods()
    {
        return tidePods;
    }
}

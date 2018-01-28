package com.example.michael.doyouknowdeway;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by michael on 1/27/18.
 */

public class GameView extends SurfaceView implements Runnable {

    volatile boolean isPlaying = true, init = true;
    private Thread gameThread = null;
    private SurfaceHolder surfaceHolder;
    private Canvas canvas;
    private Context context;
    private Activity activity;
    private int screenWidth = 0, screenHeight = 0, move_const;
    private Player player;
    Tile initTile;
    Paint paint = new Paint();


    public GameView(Context context, int screenX, int screenY) {
        super(context);


        screenWidth = screenX;
        screenHeight = screenY;
        activity = (Activity) context;
        this.context = context;
        player = new Player(context, screenX, screenY);
        surfaceHolder = getHolder();


    }

    public void run() {
        initTile = new Tile(context, 2, screenWidth, screenHeight);
        initTile.fillTile();
        while (isPlaying) {
            update();
            draw();
            setFPS();
        }
    }

    /**
     * Redraws the screen in the new positions
     */
    public void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.WHITE);


            if(init) {
                init = false;
                for (int i = 0; i < initTile.getLength(); i++) {
                    for (int j = 0; j < initTile.getHeight(); j++) {
                        if (initTile.getBlock(i, j) != null) {
                            canvas.drawBitmap(initTile.getBlock(i, j).getImage(), (i * 100), (j * 100) + 10, paint);
                        }
                    }
                }
            }
            else
            {
                if(player.isJumping)
                {
                    move_const += 10;
                }

                for (int i = 0; i < initTile.getLength(); i++) {
                    for (int j = 0; j < initTile.getHeight(); j++) {
                        if (initTile.getBlock(i, j) != null) {
                            canvas.drawBitmap(initTile.getBlock(i, j).getImage(), (i * 100) + move_const, (j * 100) + 10, paint);
                        }
                    }
                }
            }

            canvas.drawBitmap(player.getPlayerImage(),player.getXVal(), player.getYVal(), paint);

            //releases the canvas to be redrawn again
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    /**
     * updates the positions of everything on the screen if needed
     */
    public void update() {
        player.update();
    }

    /**
     * ignore for now
     *
     * @param s
     */
    public void setLevel(String s) {
    }

    /**
     * Sets the FPS to roughly 60 fps
     */
    public void setFPS() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void gameOver() {

    }

    public boolean onTouchEvent(MotionEvent event){

        int touchAction = event.getAction();

        if(touchAction == MotionEvent.ACTION_DOWN){
            player.isJumping = true;
        }

        return true;
    }

    /**
     * Pause game
     */
    public void pause() {
        isPlaying = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void resume() {
        isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

}

package com.example.michael.doyouknowdeway;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    private int screenWidth = 0, screenHeight = 0, move_const = 1;
    private Player player;
    private MediaPlayer jumpNoise;
    private Bitmap backgroundImage;
    private MediaPlayer backgroundMusic;
    private Bitmap backgroundImageResized;
    Tile currentTile, nextTile;
    private ScheduledExecutorService executorService;
    Paint paint = new Paint();
    FireBall fireball;
    private int scoreCount = 0;
    private Bitmap run1, podCount;
    private Bitmap run1Resized, podCountResized;
    private Bitmap run2;
    private Bitmap playerJumpImage;
    boolean isRun1 = false;


    public GameView(Context context, int screenX, int screenY) {
        super(context);
        backgroundImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.background_sky);
        backgroundImageResized = Bitmap.createScaledBitmap(backgroundImage, screenX, screenY, false);

        podCount = BitmapFactory.decodeResource(context.getResources(), R.drawable.detergent_pod);
        run1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.knuckles_run);
        run1Resized = Bitmap.createScaledBitmap(run1, 200, 200, false);
        podCountResized = Bitmap.createScaledBitmap(podCount, 20, 20, false);
       run2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.ugandan_knuckle);
       playerJumpImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.knucklesjump);
//        run2Resized = Bitmap.createScaledBitmap(run2, screenX, screenY, false);
        jumpNoise = MediaPlayer.create(context, R.raw.jump_takeoff);
        backgroundMusic = MediaPlayer.create(context, R.raw.music_baby);
        screenWidth = screenX;
        screenHeight = screenY;
        activity = (Activity) context;
        backgroundMusic.start();
        this.context = context;
        player = new Player(context, screenX, screenY);
        fireball = new FireBall(context, screenX, screenY);
        currentTile = new Tile(context, 2, screenWidth + 200, screenHeight);
        currentTile.fillTile();

        surfaceHolder = getHolder();



        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if(!player.isJumping) {
                    if (!isRun1) {
                        run1Resized = Bitmap.createScaledBitmap(run1, 200, 200, false);
                        isRun1 = true;
                    } else {
                        run1Resized = Bitmap.createScaledBitmap(run2, 200, 200, false);
                        isRun1 = false;
                    }
                }
            }
        }, 0, 200, TimeUnit.MILLISECONDS);


    }

    public void run() {
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
            canvas.drawBitmap(backgroundImageResized, 0, 0, paint);
            //System.out.println("ghghghg " + (currentTile.getBlock(currentTile.getLength() - 1, currentTile.getHeight() - 1).getX() *100 - (10 * move_const)));
            if(-100 >= (currentTile.getBlock(currentTile.getLength() - 1, currentTile.getHeight() - 1).getX() *100) - move_const)
            canvas.drawBitmap(podCountResized, 0, 0, paint);
            canvas.drawText(Integer.toString(scoreCount), 15, 15, paint);
            System.out.println("ghghghg " + (currentTile.getBlock(currentTile.getLength() - 1, currentTile.getHeight() - 1).getX() *100 - ((100 * move_const) + 25)));
            if(50 >= currentTile.getBlock(currentTile.getLength() - 1, currentTile.getHeight() - 1).getX() *100 - ((10 * move_const) + 25))
            {
                System.out.println("hello");
                currentTile = new Tile(nextTile);
                nextTile = null;
                move_const = 0;
            }
            if(init) {
                init = false;
                for (int i = 0; i < currentTile.getLength(); i++) {
                    for (int j = 0; j < currentTile.getHeight(); j++) {
                        if (currentTile.getBlock(i, j) != null) {
                            canvas.drawBitmap(currentTile.getBlock(i, j).getImage(), (i * 100), (j * 100) + 10, paint);
                        }
                    }
                }
            }
            else
            {
                for (int i = 0; i < currentTile.getLength(); i++) {
                    for (int j = 0; j < currentTile.getHeight(); j++) {
                        if (currentTile.getBlock(i, j) != null) {
                            canvas.drawBitmap(currentTile.getBlock(i, j).getImage(), (i * 100) - move_const, (j * 100) + 10, paint);
                            if(nextTile != null) {
                                if (i < nextTile.getLength() && j < nextTile.getHeight()) {
                                    System.out.println(nextTile.getID() + " || " + currentTile.getID());
                                    if (!(0 == nextTile.isEqualTo(currentTile))) {
                                        canvas.drawBitmap(nextTile.getBlock(i, j).getImage(), ((i + currentTile.getLength()) * 100) - move_const, (j * 100) + 10, paint);
                                    }
                                }
                            }
                        }
                    }
                }
                move_const += 10;
            }
            if(fireball.isShooting) {
                canvas.drawBitmap(fireball.getImage(), fireball.getXVal(), fireball.getYVal(), paint);
            }
            canvas.drawBitmap(run1Resized,player.getXVal(), player.getYVal(), paint);

            //releases the canvas to be redrawn again
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    /**
     * updates the positions of everything on the screen if needed
     */
    public void update() {
        player.update();

        if(fireball.isShooting) {
            fireball.update();
        }

        if((currentTile.getBlock(currentTile.getLength()-(screenWidth/100), currentTile.getHeight() -1).getX() * 100) - move_const <= 200)
        {
            nextTile = currentTile.getNextTile();
            System.out.println("PPPPPPPPPPPPP " + nextTile.getLength() + " || " + nextTile.getHeight());
        }
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
            if(event.getX() < (screenWidth / 2)) {
                jumpNoise.start();
                player.isJumping = true;
                run1Resized = Bitmap.createScaledBitmap(playerJumpImage, 200, 200, false);
            } else {
                fireball.setOnScreen(true);
            }


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


    /**
     * Resume the game
     */
    public void resume() {
        isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

}

package com.example.michael.doyouknowdeway;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.view.View;
import android.widget.ImageButton;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeUnit;

/**
 * Created by michael on 1/27/18.
 */

public class GameView extends SurfaceView implements Runnable {


    volatile boolean isPlaying = true, init = true, isPassOver = true;
    private Thread gameThread = null;
    private SurfaceHolder surfaceHolder;
    private Canvas canvas;
    private Context context;
    private Activity activity;
    private int screenWidth = 0, screenHeight = 0, move_const = 1;
    private Player player;
    private MediaPlayer jumpNoise, eatNoise;
    private Bitmap backgroundImage;
    private MediaPlayer backgroundMusic;
    private MediaPlayer endGameSound;
    private Bitmap backgroundImageResized;
    Tile currentTile, nextTile;
    private ScheduledExecutorService executorService;
    Paint paint = new Paint();
    Paint textPaint = new Paint();
    FireBall fireball;
    private int scoreCount = 0;
    private Bitmap endImage;
    private Bitmap endImageResized;
    private Bitmap run1, podCount;
    private Bitmap run1Resized, podCountResized;
    private Bitmap run2;
    private Bitmap playerJumpImage;
    boolean isRun1 = false;
    private ImageButton redoButton;


    public GameView(Context context, int screenX, int screenY) {
        super(context);

        //load images into game
        backgroundImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.background_sky);
        backgroundImageResized = Bitmap.createScaledBitmap(backgroundImage, screenX, screenY, false);
        podCount = BitmapFactory.decodeResource(context.getResources(), R.drawable.detergent_pod);
        run1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.knuckles_run);
        run1Resized = Bitmap.createScaledBitmap(run1, 200, 200, false);
        podCountResized = Bitmap.createScaledBitmap(podCount, 100, 100, false);
        run2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.ugandan_knuckle);
        playerJumpImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.knucklesjump);

        //load sounds into game
        jumpNoise = MediaPlayer.create(context, R.raw.jump_takeoff);
        eatNoise = MediaPlayer.create(context, R.raw.eat_1);
        backgroundMusic = MediaPlayer.create(context, R.raw.music_baby);
        endGameSound = MediaPlayer.create(context, R.raw.end_game);

        //initialize other important stuff
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(72);
        textPaint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        screenWidth = screenX;
        screenHeight = screenY;
        activity = (Activity) context;
        backgroundMusic.start();
        this.context = context;
        player = new Player(context, screenX, screenY);
        fireball = new FireBall(context, screenX, screenY);
        currentTile = new Tile(context, 3, screenWidth + 200, screenHeight);
        currentTile.fillTile();
        surfaceHolder = getHolder();

        //controls "running" animation
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

    /**
     * Main game loop
     */
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
            canvas.drawBitmap(podCountResized, 0, 0, paint);
            String mystr = Integer.toString(scoreCount);


            if(-100 >= (currentTile.getBlock(currentTile.getLength() - 1, currentTile.getHeight() - 1).getX() *100) - move_const)
            {
                currentTile = new Tile(nextTile);
                isPassOver = true;
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
                            if (nextTile != null) {
                                if (i < nextTile.getLength() && j < nextTile.getHeight()) {
                                    canvas.drawBitmap(nextTile.getBlock(i, j).getImage(), ((i + currentTile.getLength()) * 100) - move_const, (j * 100) + 10, paint);
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
            canvas.drawText(mystr, 120, 80, textPaint);

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

        if(((currentTile.getBlock(currentTile.getLength()-(screenWidth/100), currentTile.getHeight() -1).getX() * 100) - move_const <= 200) && nextTile == null){
            nextTile = currentTile.getNextTile();
        }

        if(player.getYVal() >= screenHeight || player.getXVal() <= 0){
            gameOver();
        }
        detectCollisions();
    }

    static boolean isColliding = false;

    public void detectCollisions(){
        int highestY = 9, passOver;
        int currentX = (300 + move_const)/100;

        if(currentX >= currentTile.getLength() && isPassOver)
        {
            passOver = -10;
            isPassOver = false;
        }
        else
        {
            passOver= -25;
        }

        for(int i = 0; i < currentTile.getHeight(); i++)
        {
            if(currentX >= currentTile.getLength())
            {
                passOver += 10;
                if(nextTile.getBlock(passOver/100, i) != null)
                {
                    if(!nextTile.getBlock(passOver/100, i).isPod())
                    {
                        highestY = i;
                        break;
                    }
                }
            }
            else if(currentTile.getBlock(currentX, i) != null)
            {
                if(!currentTile.getBlock(currentX, i).isPod())
                {
                    highestY = i;
                    break;
                }
            }
            else
            {
                highestY = -1;
            }
        }

        checkGroundCollision(highestY);

        checkTidePodCollision(currentTile, nextTile);

        checkForwardCollision(nextTile, currentX, highestY, passOver);
    }

    public void checkForwardCollision(Tile next, int x, int y, int passOver)
    {
        boolean collision = false;
        Rect rect = new Rect();

        if(next != null && passOver >= 0)
        {
            rect.top = y * 100;
            rect.bottom = screenHeight;
            rect.left = passOver + (player.getBitmap().getWidth() / 2);
            rect.right = passOver + (player.getBitmap().getWidth() / 2) + 100;

             collision = Rect.intersects(player.getHitBox(), rect);
        }
        else
        {
            rect.top = y * 100;
            rect.bottom = screenHeight;
            rect.left = (x+1)* 100;
            rect.right = (x+2) * 100;

            collision = Rect.intersects(player.getHitBox(), rect);
        }

        //if collision is true, half player movement until its not true
    }

    public void checkTidePodCollision(Tile current, Tile next)
    {
        if(next != null && !isPassOver)
        {
            for(double iter: next.getTidePods())
            {
                int x = (int) iter;
                int y = (int) (iter - x)*10;

                boolean hit = podCollision(x, y);

                if(hit)
                {
                    eatNoise.start();
                    scoreCount++;
                    nextTile.setNullBlock(x, y);
                }
            }
        }
        else
        {
            for(double iter: current.getTidePods())
            {
                int x = (int) iter;
                double temp = x;
                int y = (int) ((iter - temp)*10.00);

                boolean hit = podCollision(x, y);

                if(hit)
                {
                    eatNoise.start();
                    scoreCount++;
                    //currentTile.setNullBlock(x, y);
                }
            }
        }

    }

    private boolean podCollision(int x, int y) {
        Rect tideRect = new Rect();
        tideRect.top = y * 100;
        tideRect.left = x* 100 - move_const;
        tideRect.right = (x+1) * 100 - move_const;
        tideRect.bottom = (y+1) * 100;

        return Rect.intersects(player.getHitBox(), tideRect);
    }

    private void checkGroundCollision(int highestY) {
        Rect blockRect = new Rect();
        boolean GroundCollision;

        if(highestY >= 0) {
            blockRect.top = (highestY) * 100;
            blockRect.left = 200;
            blockRect.right = 300;
            blockRect.bottom = screenHeight;


            GroundCollision = Rect.intersects(player.getHitBox(), blockRect);
        }
        else
        {
            GroundCollision = false;
        }

        if(GroundCollision){
            isColliding = true;
        } else {
            player.isFalling = true;
            isColliding = false;
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
        endImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.end_game);
        endImageResized = Bitmap.createScaledBitmap(endImage, 100, 200, false);
        canvas.drawBitmap(endImageResized, screenWidth/2, screenHeight/2, paint);
        backgroundMusic.stop();
        endGameSound.start();
        canvas.drawText("Your Score: " + mystr, screenWidth/2, screenHeight/2, textPaint);
            try
            {
                Thread.sleep(1000);
            }
            catch(InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }
        context.startActivity(new Intent(context,MainActivity.class));
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

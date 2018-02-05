package com.example.michael.doyouknowdeway;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageButton;
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
 * The big class that does most of the work for the code, sets the overall view of the game, getting
 * the character, tiles, and player motions all together for a working game. Took us the most time
 * to work on with many bugs occuring along de wae.
 */

public class GameView extends SurfaceView implements Runnable{

    //
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
    private Paint paint = new Paint();
    private Paint textPaint = new Paint();
    private FireBall fireball;
    private int scoreCount = 0, passOver;
    private Bitmap endImage;
    private Bitmap endImageResized;
    private Bitmap run1, podCount;
    private Bitmap run1Resized, podCountResized;
    private Bitmap run2;
    private Bitmap playerJumpImage;
    private boolean isRun1 = false;
    private ImageButton redoButton;

    //starts of the program, creating the background for the game based on the dimensions of the
    //phone being used
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
        currentTile = new Tile(context, 3, screenWidth + 400, screenHeight);
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
        }, 0, 200, TimeUnit.MILLISECONDS); //can change "speed" of run by altering the second param
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
     * Redraws the screen in the new positions, creating continuous movement for the game
     */
    public void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(backgroundImageResized, 0, 0, paint);
            canvas.drawBitmap(podCountResized, 0, 0, paint);
            String scoreCountStr = Integer.toString(scoreCount);


            if(0 >= (currentTile.getLength() * 100) - move_const)
            {
                currentTile = new Tile(nextTile);
                isPassOver = true;
                nextTile = null;
                move_const = 0;
            }
            if(init) {
                init = false;
                for (int i = 0; i < currentTile.getLength(); i++) {
                    for (int j = currentTile.getHeight() - 1; j >= 0; j--) {
                        if (currentTile.getBlock(i, j) != null) {
                            canvas.drawBitmap(currentTile.getBlock(i, j).getImage(), (i * 100), (j * 100) + 10, paint);
                        }
                    }
                }
            }
            else
            {
                for (int i = 0; i < currentTile.getLength(); i++) {
                    for (int j = currentTile.getHeight() - 1; j >= 0; j--) {
                        if (currentTile.getBlock(i, j) != null) {
                            canvas.drawBitmap(currentTile.getBlock(i, j).getImage(), (i * 100) - move_const, (j * 100) + 10, paint);
                        }
                        if (nextTile != null) {
                            if (i < nextTile.getLength() && j < nextTile.getHeight()) {
                                if (nextTile.getBlock(i, j) != null) {
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
            canvas.drawText(scoreCountStr, 120, 80, textPaint);

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
            fireball.update(player);
        }

        if((((currentTile.getLength() - (screenWidth/100) )* 100) - move_const <= 200) && nextTile == null){
            nextTile = currentTile.getNextTile();
        }

        if(player.getYVal() >= screenHeight || player.getXVal() <= 0){
            gameOver();
        }
        detectCollisions();
    }

    //initially sets player to not be colliding - changed almost instantly
    static boolean isColliding = false;

    //

    /**
     * Detects collisions so the player can collect tide pods and jump off of surfaces.
     */
    public void detectCollisions(){
        int highestY = 9;
        int currentX = (250 + move_const)/100;

        if(currentX >= currentTile.getLength() && isPassOver)
        {
            passOver = -10;
            isPassOver = false;
        }
        else
        {
            passOver= -25; //arbitrary
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

    /**
     * Method used to check if the player has hit a wall in front of them.
     * @param next - the next tile
     * @param x - player x position
     * @param y - player y position
     * @param passOver - the location being passed over.
     */
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

    /**
     * Method that checks if a player has hit a tidepod, if so, adds to score count
     * @param current - the current tile
     * @param next - the next tile
     */
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
                    scoreCount += 10;
                    System.out.println("Cur Next: " + x + " || " + y);
                    nextTile.setNullBlock(x, y);
                    return;
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
                    scoreCount += 10;
                    System.out.println("Current: " + x + " || " + y);
                    currentTile.setNullBlock(x, y);
                    return;
                }
            }
        }

    }

    //compliment to the previous method
    private boolean podCollision(int x, int y) {
        Rect tideRect = new Rect();
        if(isPassOver && x < 3)
        {
            System.out.println("TEST: " + x + " || " + y);
            tideRect.top = (y * 100);
            tideRect.left = x * 100 + passOver + 10;
            tideRect.right = (x + 2) * 100 + passOver;
            tideRect.bottom = (y + 2) * 100 + 10;
        }
        else {
            tideRect.top = y * 100;
            tideRect.left = x * 100 - move_const + 10;
            tideRect.right = (x + 2) * 100 - move_const;
            tideRect.bottom = (y + 2) * 100 + 10;
        }

        return Rect.intersects(player.getHitBox(), tideRect);
    }


    /**
     * Method used for jumping off of the ground.
     * @param highestY
     */
    private void checkGroundCollision(int highestY) {
        Rect blockRect = new Rect();
        boolean GroundCollision;

        if(highestY >= 0) {
            blockRect.top = (highestY) * 100 - 25;
            blockRect.left = 200;
            blockRect.right = 300;
            //changed this valued -- this is to remind myself
            blockRect.bottom = highestY * 100 + 25; //still needs work //make player hitbox just his feet


            GroundCollision = Rect.intersects(player.getFeetBox(), blockRect);
            System.out.println("WWWWWW : " + GroundCollision);
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
     * Sets the FPS to roughly 60 fps
     */
    public void setFPS() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that
     */
    public void gameOver() {
        //end image is not currently working
        endImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.end_game);
        endImageResized = Bitmap.createScaledBitmap(endImage, 100, 200, false);
        canvas.drawBitmap(endImageResized, screenWidth/2, screenHeight/2, paint);

        //free up memory from bitmaps
        backgroundImage.recycle();
        backgroundImage = null;

        backgroundImageResized.recycle();
        backgroundImageResized = null;

        podCount.recycle();
        podCount = null;

        podCountResized.recycle();
        podCountResized = null;

        backgroundImage.recycle();
        backgroundImage = null;

        run1.recycle();
        run1 = null;

        run1Resized.recycle();
        run1Resized = null;

        run2.recycle();
        run2 = null;
        playerJumpImage.recycle();
        playerJumpImage = null;

        backgroundMusic.stop();

        Runtime.getRuntime().gc(); //manually run garbage collector

        endGameSound.start();
        context.startActivity(new Intent(context,MainActivity.class));
    }

    /**
     * Method used to dictate what to do when the android screen is touched.
     * @param event - the type of touch on the screen
     * @return - true when screen is touched
     */
    public boolean onTouchEvent(MotionEvent event){
        int touchAction = event.getAction();

        if(touchAction == MotionEvent.ACTION_DOWN){
            if(event.getX() < (screenWidth / 2)) {
                jumpNoise.start();
                if(!player.isJumping && !player.isFalling) {
                    player.setYval(player.getYVal());
                    player.isJumping = true;
                    run1Resized = Bitmap.createScaledBitmap(playerJumpImage, 200, 200, false);
                }
                else if(player.getJumpCount() < 1)
                {
                    player.setYval(player.getYVal());
                    player.incrJump();
                    player.isJumping = true;
                    run1Resized = Bitmap.createScaledBitmap(playerJumpImage, 200, 200, false);
                }
            } else {
                fireball.setOnScreen(true);
            }
        }
        return true;
    }

    /**
     * Method that pauses the game when necessary (i.e when home button is pressed)
     */
    public void pause() {
        isPlaying = false;
        backgroundMusic.pause();
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * Resumes the game after a pause
     */
    public void resume() {
        isPlaying = true;
        backgroundMusic.start();
        gameThread = new Thread(this);
        gameThread.start();
    }



}

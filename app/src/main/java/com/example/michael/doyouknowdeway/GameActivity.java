package com.example.michael.doyouknowdeway;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by michael on 1/27/18.
 */

public class GameActivity extends AppCompatActivity {

    private GameView gameView;
    private String level;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //sets full screen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //gets the screen size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        //intialize gameview class
        gameView = new GameView(this, size.x, size.y);

        Bundle bundle = getIntent().getExtras();
        level = bundle.getString("levelValue");
        gameView.setLevel(level);

        setContentView(R.layout.gameactivity_view);
        setContentView(gameView);


    }



    //pauses the game
    @Override
    public void onPause() {
        super.onPause();
        gameView.pause();
    }

    //resumes the game
    @Override
    public void onResume(){
        super.onResume();
        gameView.resume();
    }

}


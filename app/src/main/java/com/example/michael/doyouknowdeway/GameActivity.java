package com.example.michael.doyouknowdeway;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

/**
 * Created by michael on 1/27/18.
 */

public class GameActivity extends AppCompatActivity {

    private GameView gameView;
    private String level;
    private ImageButton settingsButton;
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
        settingsButton = findViewById(R.id.imageButton4);
//        settingsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PopupMenu popupMenu = new PopupMenu(GameActivity.this, settingsButton);
//                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem menuItem) {
//                        Toast.makeText(GameActivity.this, "" + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
////                        if (menuItem.equals("one")) {
////                            onPause();
////                        } else if (menuItem.equals("two")) {
////                            onResume();
////                        }
//                        return true;
//                    }
//                });
//                popupMenu.show();
//            }
//        });
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


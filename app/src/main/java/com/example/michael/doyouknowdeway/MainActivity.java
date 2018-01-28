/**
 * Authors: Hugh Jackovivh, Austin Johnson, Cory Petersen, Mike Pollard, Matt Sagen
 * Description: Main class for Da Wae app
 * Code24 - 1/27/2018
 */


package com.example.michael.doyouknowdeway;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton startButton;
    private MediaPlayer deway;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //puts the app in full screen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        //initialize buttons
        startButton = findViewById(R.id.imageButton2);
        startButton.setOnClickListener(this);

        deway = MediaPlayer.create(this, R.raw.do_u_kno_de_way);
    }

    //method for when user selects play game
    public void onClick(View v) {
        if (v == startButton) {
            //starts de wae
            deway.start();
            Intent myIntent = new Intent(this, GameActivity.class);
            myIntent.putExtra("levelValue", "1");
            startActivity(myIntent);
        }
    }
}

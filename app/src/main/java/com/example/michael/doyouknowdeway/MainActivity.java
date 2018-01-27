package com.example.michael.doyouknowdeway;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button startButton;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //puts the app in full screen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        //initialize buttons
        startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(this);


    }

    public void onClick(View v) {
        if (v == startButton) {
            Intent myIntent = new Intent(this, GameActivity.class);
            myIntent.putExtra("levelValue", "1");
            startActivity(myIntent);
        }
    }
}

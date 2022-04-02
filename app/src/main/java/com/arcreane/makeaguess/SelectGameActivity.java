package com.arcreane.makeaguess;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectGameActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_game);

        Button makeAGuessButton = findViewById(R.id.makeAguessButton);
        makeAGuessButton.setOnClickListener(this);
        Button wordScrambleButton = findViewById(R.id.wordScrambleButton);
        wordScrambleButton.setOnClickListener(this);


        ActionBar applicationActionBar = getSupportActionBar();
        applicationActionBar.setTitle("Game Selection");
    }

    @Override
    public void onClick(View view) {
        Intent startIntent = null;
        if(view.getId() == R.id.makeAguessButton){
            startIntent = new Intent(this, MakeAGuessActivity.class);
        }
        else if(view.getId() == R.id.wordScrambleButton){
            startIntent = new Intent(this, WordScrambleActivity.class);
        }
        startActivity(startIntent);
    }
}
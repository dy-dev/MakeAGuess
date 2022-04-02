package com.arcreane.makeaguess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WordScrambleActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.guessMI:
                Intent guessIntent = new Intent(this, MakeAGuessActivity.class);
                startActivity(guessIntent);
                break;
            case R.id.wordScrambleMI:
                Toast.makeText(this,"Let reset the game", Toast.LENGTH_LONG).show();
        }


        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_scramble);

        ActionBar applicationActionBar = getSupportActionBar();
        applicationActionBar.setTitle("Word Scrambler");
    }
}
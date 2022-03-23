package com.arcreane.makeaguess;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int m_iNumberToGuess;
    private EditText guessET;
    private ProgressBar progressBar;
    private TextView triesLeftTV;
    private TextView hintTV;
    private ListView hintLV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Random rand = new Random();
        m_iNumberToGuess = rand.nextInt(10);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button validateButton = findViewById(R.id.validateButton);
        guessET = findViewById(R.id.guessET);
        progressBar = findViewById(R.id.progressBar);
        triesLeftTV = findViewById(R.id.triesLeftTV);
        triesLeftTV.setText(String.valueOf(progressBar.getMax()));
        hintTV = findViewById(R.id.hintTV);
        hintLV = findViewById(R.id.hintLV);

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUserValue();
            }
        });



    }

    private void checkUserValue(){
        progressBar.setProgress(progressBar.getProgress() + 1 );
        triesLeftTV.setText(String.valueOf(progressBar.getMax() - progressBar.getProgress()));
        String tmp = guessET.getText().toString();
 
        int userInput = -1;
        userInput = Integer.parseInt(tmp);

        if(userInput > m_iNumberToGuess)
            hintTV.setText("TOO BIG");
        else if(userInput < m_iNumberToGuess)
            hintTV.setText("TOO SMALL");
        else
            hintTV.setText("CONGRATS");

    }

}
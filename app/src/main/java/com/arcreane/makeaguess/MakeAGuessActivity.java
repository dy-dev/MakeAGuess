package com.arcreane.makeaguess;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

enum Difficulty {
    EASY(10, 5),
    MEDIUM(50, 10),
    HARD(100, 15);

    int m_iDifficulty;
    int m_iNbTries;

    Difficulty(int p_iDifficylty, int p_iNbTries) {
        m_iDifficulty = p_iDifficylty;
        m_iNbTries = p_iNbTries;
    }

    public int getValue() {
        return m_iDifficulty;
    }

    public int getNbTries() {
        return m_iNbTries;
    }
}

public class MakeAGuessActivity extends AppCompatActivity {

    private Difficulty m_eDifficulty;
    private int m_iNumberToGuess;
    private EditText guessET;
    private EditText nameET;
    private ProgressBar progressBar;
    private TextView triesLeftTV;
    private TextView hintTV;
    boolean m_bGameOver;

    //    List<Pair<String, String>> userPreviousTries;
    List<String> userPreviousTries;
    // ArrayAdapter<Pair<String, String>> triesAdapter;
    ArrayAdapter<String> triesAdapter;
    private ListView hintLV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_a_guess);

        userPreviousTries = new ArrayList<>();
        triesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userPreviousTries);
        nameET = findViewById(R.id.nameEdit);
    }

    private void Initialisation() {
        Random rand = new Random();
        m_iNumberToGuess = rand.nextInt(m_eDifficulty.getValue());

        Toast.makeText(this, "" + m_iNumberToGuess, Toast.LENGTH_LONG).show();

        Button validateButton = findViewById(R.id.validateButton);
        guessET = findViewById(R.id.guessET);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(m_eDifficulty.getNbTries());
        triesLeftTV = findViewById(R.id.triesLeftTV);
        triesLeftTV.setText(String.valueOf(progressBar.getMax()));
        hintTV = findViewById(R.id.hintTV);
        hintLV = findViewById(R.id.hintLV);
        m_bGameOver = false;


//        triesAdapter = new ArrayAdapter<Pair<String, String>>(this, android.R.layout.simple_list_item_2, userPreviousTries){
//            @NonNull
//            @Override
//            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//                View adaptedView = super.getView(position, convertView, parent);
//                TextView tv1 = adaptedView.findViewById(android.R.id.text1);
//                TextView tv2 = adaptedView.findViewById(android.R.id.text2);
//                Pair<String, String> infos = userPreviousTries.get(position);
//                tv1.setText(infos.first);
//                tv2.setText(infos.second);
//                return adaptedView;
//            }
//        };
        hintLV.setAdapter(triesAdapter);

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUserValue();
            }
        });
    }

    private void checkUserValue() {
        progressBar.setProgress(progressBar.getProgress() + 1);
        triesLeftTV.setText(String.valueOf(progressBar.getMax() - progressBar.getProgress()));
        String tmp = guessET.getText().toString();

        int userInput = -1;
        userInput = Integer.parseInt(tmp);

        if (userInput > m_iNumberToGuess)
            hintTV.setText("TOO BIG");
        else if (userInput < m_iNumberToGuess)
            hintTV.setText("TOO SMALL");
        else
            endGame();

//        userPreviousTries.add(0,Pair.create(tmp, hintTV.getText().toString()));
        if (!m_bGameOver) {
            userPreviousTries.add(0, tmp + " is " + hintTV.getText().toString());
            triesAdapter.notifyDataSetChanged();
        }
    }

    public void selectDifficulty(View view) {
        RadioGroup difficultyR = findViewById(R.id.difficultyRG);

        switch (difficultyR.getCheckedRadioButtonId()) {
            case R.id.easyRB:
                m_eDifficulty = Difficulty.EASY;
                break;
            case R.id.mediumRB:
                m_eDifficulty = Difficulty.MEDIUM;
                break;
            case R.id.hardRB:
                m_eDifficulty = Difficulty.HARD;
                break;
        }

        Initialisation();
        LinearLayout difficultyLayout = findViewById(R.id.difficultyLayout);
        difficultyLayout.setVisibility(View.INVISIBLE);
        LinearLayout gameLayout = findViewById(R.id.gameLayout);
        gameLayout.setVisibility(View.VISIBLE);
    }

    private void endGame() {

        m_bGameOver = true;
        userPreviousTries.clear();
        triesAdapter.notifyDataSetChanged();
        progressBar.setProgress(0);
        hintTV.setText("");


        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Congrats, you won");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Play again", (dialogInterface, i) -> {
            Initialisation();
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Change  Difficulty", (dialogInterface, i) -> {
            LinearLayout difficultyLayout = findViewById(R.id.difficultyLayout);
            LinearLayout gameLayout = findViewById(R.id.gameLayout);

            difficultyLayout.setVisibility(View.VISIBLE);
            gameLayout.setVisibility(View.INVISIBLE);
            RadioGroup difficultyR = findViewById(R.id.difficultyRG);
            RadioButton difficultyRB = findViewById(difficultyR.getCheckedRadioButtonId());
            difficultyRB.setChecked(false);
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Quit", (dialogInterface, i) -> {
            finish();
        });

        alertDialog.show();
    }


}
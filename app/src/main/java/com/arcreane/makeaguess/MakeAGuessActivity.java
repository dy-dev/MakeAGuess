package com.arcreane.makeaguess;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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

    /****
     * Solution for simple_list_item1
     */
    //    List<String> userPreviousTries;
    //    ArrayAdapter<String> triesAdapter;

    /****
     * Solution for simple_list_item2
     */
    ArrayList<Pair<String, String>> userPreviousTries;
    ArrayAdapter<Pair<String, String>> triesAdapter;

    /****
     * Solution for simple_list_item2 using a SimpleAdapter
     */
    ArrayList<Map<String, Object>> listForSimpleAdapter;
    SimpleAdapter simpleAdapter;

    private ListView hintLV;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.guessMI:
                Toast.makeText(this,"Let reset the game", Toast.LENGTH_LONG).show();
                break;
            case R.id.wordScrambleMI:
                Intent wordIntent = new Intent(this, WordScrambleActivity.class);
                startActivity(wordIntent);
                break;
        }


        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_a_guess);

        userPreviousTries = new ArrayList<>();
        //When using simple_list_item_1
        //triesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userPreviousTries);

        listForSimpleAdapter = new ArrayList<>();
        nameET = findViewById(R.id.nameEdit);


        ActionBar applicationActionBar = getSupportActionBar();
        applicationActionBar.setTitle("Make A Guess");
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

        //When using simple_list_item_2
        triesAdapter = new ArrayAdapter<Pair<String, String>>(this, android.R.layout.simple_list_item_2, android.R.id.text1, userPreviousTries) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View adaptedView = super.getView(position, convertView, parent);
                TextView tv1 = adaptedView.findViewById(android.R.id.text1);
                TextView tv2 = adaptedView.findViewById(android.R.id.text2);
                Pair<String, String> infos = userPreviousTries.get(position);
                tv1.setText(infos.first);
                tv2.setText(infos.second);
                tv2.setTextSize(8);
                return adaptedView;
            }
        };


        simpleAdapter = new SimpleAdapter(this, listForSimpleAdapter, android.R.layout.simple_list_item_2,
                new String[]{"Value", "hint"}, new int[]{android.R.id.text1, android.R.id.text2});

        //When using a SimpleAdapter
        //hintLV.setAdapter(simpleAdapter);

        //When using an Adapter
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

        if (!m_bGameOver) {
            //When using an Adapter
            userPreviousTries.add(0, Pair.create(tmp, hintTV.getText().toString()));
            triesAdapter.notifyDataSetChanged();

            //When using a SimpleAdapter
            Map<String, Object> listItemMap = new HashMap<String, Object>();
            listItemMap.put("Value", tmp);
            listItemMap.put("hint", hintTV.getText().toString());
            listForSimpleAdapter.add(0, listItemMap);
            //simpleAdapter.notifyDataSetChanged();
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
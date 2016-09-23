package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Dictionary;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    TextView ghostText;
    TextView gameStatus;
    Button mRestart, mChallenge;
    public static boolean user_started_first;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);

        ghostText = (TextView) findViewById(R.id.ghostText);
        gameStatus = (TextView) findViewById(R.id.gameStatus);
        mRestart = (Button) findViewById(R.id.restart);
        mChallenge = (Button) findViewById(R.id.challenge);

       /* ghostText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(InputMethodManager.RESULT_SHOWN,0);

            }
        });*/


        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new FastDictionary(inputStream);
        } catch (IOException e) {
            Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG).show();
        }
        onStart(null);

        if(savedInstanceState!=null)
        {
            ghostText.setText(savedInstanceState.getString("Current Word"));
            gameStatus.setText(savedInstanceState.getString("Current Status"));
        }

        mRestart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onStart(v);
            }
        });

        mChallenge.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String curr_fragment = ghostText.getText().toString();
                if (curr_fragment.length() >= 4 && dictionary.isWord(curr_fragment)) {
                    gameStatus.setText("User Wins as it is a word");
                } else {
                    String word = dictionary.getGoodWordStartingWith(curr_fragment);
                    //Log.e("Here","Word is "+word);
                    if (word == null) {
                        gameStatus.setText("User Wins as no words is possible");
                    } else {
                        gameStatus.setText("Computer Wins! As the word possible is -> " + word);
                        mChallenge.setEnabled(Boolean.FALSE);
                    }
                }
            }
        });
        onStart(null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     *
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        mChallenge.setEnabled(Boolean.TRUE);
        userTurn = random.nextBoolean();
        //TextView text = (TextView) findViewById(R.id.ghostText);
        ghostText.setText("");
        //TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            user_started_first = true;
            gameStatus.setText(USER_TURN);
        } else {
            user_started_first = false;
            gameStatus.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }


    private void computerTurn() {
        //TextView label = (TextView) findViewById(R.id.gameStatus);
        // Do computer turn stuff then make it the user's turn again
        if (ghostText.getText().toString().length() >= 4 && dictionary.isWord(ghostText.getText().toString())) {
            gameStatus.setText("Computer Wins");
            mChallenge.setEnabled(Boolean.FALSE);
            return;
        }

        String word = dictionary.getGoodWordStartingWith(ghostText.getText().toString());
        if (word == null) {
            gameStatus.setText("No Words Start with this prefix. Computer Wins!");
            mChallenge.setEnabled(Boolean.FALSE);
            return;
        } else {
                String new_word = ghostText.getText().toString() + word.charAt(ghostText.getText().toString().length());
            ghostText.setText(new_word);
        }

        userTurn = true;
        gameStatus.setText(USER_TURN);
    }

    //Basically User's turn

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {   //On Key Pressed from A-Z this will open
        if (keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z) {
            char character = (char) event.getUnicodeChar();
            ghostText.append(String.valueOf(character));
            boolean isValid = dictionary.isWord(ghostText.getText().toString());
            computerTurn();
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Current Word", ghostText.getText().toString());
        outState.putString("Current Status", gameStatus.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ghostText.setText(savedInstanceState.getString("Current Word"));
        gameStatus.setText(savedInstanceState.getString("Current Status"));
    }
}

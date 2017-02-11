/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    Button challenge;
    Button restart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        challenge = (Button)findViewById(R.id.challenge);
        restart = (Button)findViewById(R.id.restart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView status = (TextView) findViewById(R.id.gameStatus);
                status.setText(USER_TURN);
                TextView word=(TextView)findViewById(R.id.wordFragment);
                word.setText("");
                userTurn = true;
            }
        });
        challenge.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                TextView word = (TextView)findViewById(R.id.wordFragment);

                String s = word.getText().toString();
                if(s.length()>=4 && dictionary.isWord(s))
                {
                    TextView status = (TextView) findViewById(R.id.gameStatus);
                    status.setText("User Victory!");
                }
                else if(dictionary.getAnyWordStartingWith(s)==null)
                {
                    TextView status = (TextView) findViewById(R.id.gameStatus);
                    status.setText("Computer Victory!");
                }
                else
                {
                    TextView status = (TextView) findViewById(R.id.gameStatus);
                    status.setText("User Victory!");
                }
            }
        });

        InputStream i = null;
        try {
            i = assetManager.open("words.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            //SimpleDictionary simpleDictionary= new SimpleDictionary(i);
            dictionary= new SimpleDictionary(i);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        TextView status=(TextView)findViewById(R.id.gameStatus);
        TextView label = (TextView) findViewById(R.id.wordFragment);
        if(dictionary.isWord(label.getText().toString()) && label.getText().length()>=4)
        {

            status.setText("User Victory!");
        }
        else
        {
            String p = dictionary.getAnyWordStartingWith(label.getText().toString());
            if(p==null)
            {

                status.setText("Computer Victory!");
            }
            else
            {
                String c=p.substring(label.getText().toString().length());
                label.setText(label.getText().toString()+c.substring(0,1));
            }
        }
        userTurn = true;
        status.setText(USER_TURN);
    }

    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        char pressedKey = (char) event.getUnicodeChar();
        if((pressedKey>='A' && pressedKey<='Z') || (pressedKey>='a' && pressedKey<='z'))
        {
            TextView word = (TextView)findViewById(R.id.wordFragment);
            String s = word.getText().toString();
            word.setText(s+pressedKey);
            s = word.getText().toString();


            computerTurn();
        }
        else
            return super.onKeyUp(keyCode,event);



        return super.onKeyUp(keyCode, event);
    }
}

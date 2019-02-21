package com.jahn.periodictablememory;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

  JSONArray periodicTable = null;

  int elementindex = 0;
  Button[] buttons = null;
  int score = 0;
  int scoreStreak = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    PeriodicTableJSON periodicTableJSON = new PeriodicTableJSON(MainActivity.this);
    periodicTable = periodicTableJSON.getPeriodicTable(0, 91);
    elementindex = new Random().nextInt(periodicTable.length());
    buttons = new Button[]{
      findViewById(R.id.answear1),
      findViewById(R.id.answear2),
      findViewById(R.id.answear3),
      findViewById(R.id.answear4)
    };

    //ImageButton overlay = findViewById(R.id.imageButton);
    setElementText();


    for (final Button button : buttons)
      button.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          // Code here executes on main thread after user presses button
          highLightCurrectButton();
          setScore((String) button.getText());
          for (Button b : buttons)
            b.setEnabled(false);
          button.postDelayed(new Runnable() {
            @Override
            public void run() {
              setElementText();
              for (Button b : buttons)
                b.setEnabled(true);
            }
          }, 1000);
        }
      });
  }


  public void setElementText() {

    TextView elementTextView = findViewById(R.id.ElementName);
    int periodictableLength = periodicTable.length();
    elementindex = new Random().nextInt(periodictableLength);

    setAnswerButtons();
    resetButtonColor();
    try {
      elementTextView.setText((CharSequence) periodicTable.getJSONObject(elementindex).get("name"));
    } catch (JSONException e) {
      e.printStackTrace();
    }

  }

  public void setAnswerButtons() {

    ArrayList<Integer> answerArray = getAnswerIndexs();

    for (int i = 0; i < buttons.length; i++) {
      try {
        buttons[i].setText("" + periodicTable.getJSONObject(answerArray.get(i)).get("atomic_mass"));
      } catch (JSONException e) {
        e.printStackTrace();
      }

    }

  }


  public void highLightCurrectButton() {
    String currectAnswer = "";
    try {
      currectAnswer = "" + periodicTable.getJSONObject(elementindex).get("atomic_mass");
    } catch (JSONException e) {
      e.printStackTrace();
    }

    for (int i = 0; i < buttons.length; i++) {
      if (currectAnswer.equals(buttons[i].getText()))
        buttons[i].getBackground().setColorFilter(Color.rgb(66, 244, 92), PorterDuff.Mode.MULTIPLY);
    }
  }

  public void resetButtonColor() {
    for (Button b : buttons) {
      b.getBackground().clearColorFilter();
    }
  }


  public ArrayList<Integer> getAnswerIndexs() {
    ArrayList<Integer> array = new ArrayList<>();
    int space = 7;
    int max = periodicTable.length() - elementindex > space ? space : periodicTable.length() - elementindex - 1;
    int min = elementindex > space ? space : elementindex;

    for (int i = elementindex - min; i < elementindex + max; i++) {
      if (i == elementindex)
        continue;
      array.add(i);
    }
    Collections.shuffle(array);
    ArrayList<Integer> returnArray = new ArrayList<>();

    for (int i = 0; i < 3; i++) {
      returnArray.add(array.get(i));
    }
    returnArray.add(elementindex);

    Collections.shuffle(returnArray);

    return returnArray;

  }

  public void setScore(String answer) {
    String correctAnswer = "";
    try {
      correctAnswer = periodicTable.getJSONObject(elementindex).get("atomic_mass").toString();
    } catch (JSONException e) {
      e.printStackTrace();
    }

    if (answer.equals("" + correctAnswer)) {
      score += 100 * scoreStreak;
      scoreStreak++;
    } else {
      scoreStreak = 1;
    }
    TextView scoreView = findViewById(R.id.score);
    TextView steakView = findViewById(R.id.streak);
    scoreView.setText(String.format("%d Points", score));
    steakView.setText(String.format("%d X multiplier", scoreStreak));
  }
}

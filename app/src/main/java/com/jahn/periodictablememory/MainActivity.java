package com.jahn.periodictablememory;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import java.util.Random;

public class MainActivity extends AppCompatActivity {

  JSONArray periodicTable = null;
  Button nextButton = null;
  Button prevButton = null;
  int elementindex = 0;
  Button[] buttons = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    PeriodicTableJSON periodicTableJSON = new PeriodicTableJSON(MainActivity.this);
    periodicTable = periodicTableJSON.getPeriodicTable();
    elementindex = new Random().nextInt(119);
    buttons = new Button[]{
      findViewById(R.id.answear1),
      findViewById(R.id.answear2),
      findViewById(R.id.answear3),
      findViewById(R.id.answear4)
    };

    setElementText("");

    nextButton = findViewById(R.id.next);
    nextButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        // Code here executes on main thread after user presses button
        setElementText("next");
      }
    });

    prevButton = findViewById(R.id.prev);
    prevButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        // Code here executes on main thread after user presses button
        setElementText("prev");
      }
    });

    // setAnswerButtons();


    for (Button button : buttons)
      button.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          // Code here executes on main thread after user presses button
          highLightCurrectButton();
        }
      });

  }


  public void setElementText(String next) {

    TextView textView = findViewById(R.id.ElementName);
    int periodictableLength = periodicTable.length();

    if (next.equals("next") && elementindex < periodictableLength - 1)
      elementindex++;
    if (next.equals("prev") && elementindex > 0)
      elementindex--;

    setAnswerButtons();
    resetButtonColor();
    try {
      textView.setText((CharSequence) periodicTable.getJSONObject(elementindex).get("name"));
    } catch (JSONException e) {
      e.printStackTrace();
    }

  }

  public void setAnswerButtons() {

    Random random = new Random();
    int index = random.nextInt(buttons.length);
    JSONObject currectElement = null;
    try {
      currectElement = periodicTable.getJSONObject(elementindex);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    int[] answerArray = getAnswerIndexs();
    Collections.shuffle(Arrays.asList(answerArray));

    for (int i = 0; i < buttons.length; i++) {
      try {
        buttons[i].setText("" + periodicTable.getJSONObject(answerArray[i]).get("atomic_mass"));
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

  public int[] getAnswerIndexs() {
    int[] array = new int[4];


    int space = 7;
    int index = 0;
    int max = periodicTable.length() - elementindex > space ? space : periodicTable.length() - elementindex - 1;
    int min = elementindex > space ? space : elementindex;
    process: do {

      int randomElementIndex = new Random().nextInt((max - min) + min)+1;
      System.out.println("\nelementindex: " + elementindex +"\nrandomElementIndex: "  + randomElementIndex);

      boolean plusOrMinus = new Random().nextBoolean();
      int returnindex;
      if (max < 3)
        returnindex = elementindex - randomElementIndex;
      else if (min < 3)
        returnindex = elementindex + randomElementIndex;
      else
        returnindex = plusOrMinus ? elementindex + randomElementIndex : elementindex - randomElementIndex;


      for (int i = 0; i < index; i++) {
        System.out.println(Arrays.toString(array));
        System.out.println("\ncurrect element: " + (array[i] == elementindex) + "\nreturn element: " + (returnindex == array[i]));

        if (returnindex == array[i]){
          System.out.println("array element: " + array[i] );
          System.out.println("index: " + index);
          continue process;

        }
      }
      array[index] = returnindex;
      index++;

    } while (index < array.length - 1);

    array[3] = elementindex;
    System.out.println("plus: " + max);
    System.out.println("minus: " + min);
    System.out.println(Arrays.toString(array));
    return array;

  }
}

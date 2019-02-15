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
import java.util.Random;

public class MainActivity extends AppCompatActivity {

  JSONObject periodicTable = null;
  Button nextButton = null;
  Button prevButton = null;
  int elementindex = 0;
  Button[] buttons = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    System.out.println("JSON DATA :");
    try {
      periodicTable = new JSONObject(loadJSONFromAsset(MainActivity.this));
      System.out.println(periodicTable);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    buttons = new Button[]{
            findViewById(R.id.answear1),
            findViewById(R.id.answear2),
            findViewById(R.id.answear3),
            findViewById(R.id.answear4)
    };


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

    setAnswerButtons();
    for(Button button : buttons)
      button.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          // Code here executes on main thread after user presses button
          highLightCurrectButton();
        }
      });

  }

  public String loadJSONFromAsset(Context context) {
    String json = null;
    try {
      InputStream is = context.getAssets().open("PeriodicTableJSON.json");

      int size = is.available();

      byte[] buffer = new byte[size];

      is.read(buffer);

      is.close();

      json = new String(buffer, "UTF-16LE");


    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
    return json;

  }

  public void setElementText(String next) {

    TextView textView = findViewById(R.id.ElementName);
    int periodictableLength = 0;
    try {
      periodictableLength = periodicTable.getJSONArray("elements").length();
    } catch (JSONException e) {
      e.printStackTrace();
    }
    if (next.equals("next") && elementindex < periodictableLength - 1)
      elementindex++;
    if (next.equals("prev") && elementindex > 0)
      elementindex--;

    System.out.println("element index: " + elementindex);
    setAnswerButtons();
    resetButtonColor();
    try {
      textView.setText((CharSequence) getElement(elementindex).get("name"));
    } catch (JSONException e) {
      e.printStackTrace();
    }

  }

  public void setAnswerButtons() {

    Random random = new Random();
    int index = random.nextInt(buttons.length);
    JSONObject currectElement = getElement(elementindex);

    for (int i = 0; i < buttons.length; i++) {
      int randomElement = random.nextInt(periodicTable.length());
      try {
        if (i == index) {
          buttons[i].setText("" + currectElement.get("atomic_mass"));
        } else {
          buttons[i].setText("" + getElement(randomElement).get("atomic_mass"));
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }

  }

  public JSONObject getElement(int index) {
    JSONArray array = null;
    JSONObject element = null;
    try {
      array = periodicTable.getJSONArray("elements");
      element = array.getJSONObject(index);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return element;
  }



  public void highLightCurrectButton(){
    String currectAnswer = "";
    try {
      currectAnswer = ""+ getElement(elementindex).get("atomic_mass");
    } catch (JSONException e) {
      e.printStackTrace();
    }

    for (int i = 0; i < buttons.length; i++) {
      if(currectAnswer.equals(buttons[i].getText()))
        buttons[i].getBackground().setColorFilter(Color.rgb(66, 244, 92), PorterDuff.Mode.MULTIPLY);
    }
  }

  public void resetButtonColor(){
    for (Button b : buttons) {
      b.getBackground().clearColorFilter();
    }
  }
}

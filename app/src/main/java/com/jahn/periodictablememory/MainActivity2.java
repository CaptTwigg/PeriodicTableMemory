package com.jahn.periodictablememory;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public
class MainActivity2 extends AppCompatActivity {

  JSONArray    periodicTable  = null;
  JSONObject   currentElement = null;
  int          elementindex   = 0;
  Button[]     buttons        = null;
  int          score          = 0;
  int          scoreStreak    = 1;
  String       topic          = "atomic_mass";
  String[]     topics         = {
    "atomic_mass",
    "symbol",
    "number",
    "xpos",
    "period"
  };
  TextView[]   elementViewArray;
  int          topicIndex     = 0;
  List<String> answers        = new ArrayList<>();

  TextView Symbol      = null;
  TextView Mass        = null;
  TextView ElementName = null;
  TextView Period      = null;
  TextView Number      = null;
  TextView Group       = null;
  TextView next        = null;

  ColorStateList defaultTextColor;

  @Override
  protected
  void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main2);
    getSupportActionBar().hide(); //hide the title bar
    System.out.println(Debug.isDebuggerConnected());
    if (Debug.isDebuggerConnected()) {
      getWindow().setFlags(
        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
        WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
                          );
    }


    Symbol = findViewById(R.id.Symbol);
    Mass = findViewById(R.id.Mass);
    ElementName = findViewById(R.id.ElementName);
    Period = findViewById(R.id.Period);
    Number = findViewById(R.id.Number);
    Group = findViewById(R.id.Group);
    elementViewArray = new TextView[]{
      Mass,
      Symbol,
      Number,
      Period,
      Group,
      ElementName
    };
    defaultTextColor = Symbol.getTextColors(); //save original colors

    PeriodicTableJSON periodicTableJSON = new PeriodicTableJSON(MainActivity2.this);
    periodicTable = periodicTableJSON.getPeriodicTable(0, 91);


    buttons = new Button[]{
      findViewById(R.id.answear1),
      findViewById(R.id.answear2),
      findViewById(R.id.answear3),
      findViewById(R.id.answear4)
    };


    setStartElement();
    next = findViewById(R.id.next);
    next.setOnClickListener(new View.OnClickListener() {
      @Override
      public
      void onClick(View view) {
        next.setVisibility(View.GONE);
        setNextElement();
        for (Button b : buttons)
          b.setVisibility(View.VISIBLE);
      }
    });


    for (final Button button : buttons)
      button.setOnClickListener(new View.OnClickListener() {
        public
        void onClick(View v) {
          //highLightCurrectButton();

          String answer = (String) button.getText();
          // setScore(answer);
          answers.add(answer);
          updateElementView();

          for (Button b : buttons)
            b.setEnabled(false);

          button.postDelayed(new Runnable() {
            @Override
            public
            void run() {
              setNextTopic();
              setAnswerButtons();
              setWhatToGuess();
              for (Button b : buttons) {
                resetButtonColor();
                b.setEnabled(true);
              }
            }
          }, 100);
        }
      });
  }

  public
  void setStartElement() {
    try {
      clearElementView();
      elementindex = new Random().nextInt(periodicTable.length());
      currentElement = periodicTable.getJSONObject(elementindex);
      ElementName.setText((CharSequence) currentElement.get("name"));
      setWhatToGuess();
      setAnswerButtons();
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public
  void setNextElement() {
    clearElementView();
    answers.clear();

    elementindex = new Random().nextInt(periodicTable.length());

    try {
      ElementName.setText((CharSequence) currentElement.get("name"));
      currentElement = periodicTable.getJSONObject(elementindex);
    } catch (JSONException e) {
      e.printStackTrace();
    }

  }

  public
  void setAnswerButtons() {
    ArrayList<Integer> answerArray   = getAnswerIndexs();
    List<Integer>      periodAnswers = getPeriodNumbers();
    System.out.println(answers);
    for (int i = 0; i < buttons.length; i++) {
      try {
        if (topics[topicIndex].equals("period"))
          buttons[i].setText("" + periodAnswers.get(i));
        else
          buttons[i].setText(
            String.format("%s", periodicTable.getJSONObject(answerArray.get(i)).get(topic)));
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
  }

  public
  void setNextTopic() {
    if (topicIndex == topics.length - 1) {
      next.setVisibility(View.VISIBLE);
      for (TextView tv : elementViewArray)
        System.out.println(tv.getText());
      highLightCorrectAnswers();
      for (Button b : buttons)
        b.setVisibility(View.GONE);
      topicIndex = 0;
    } else {
      topicIndex++;
    }
    topic = topics[topicIndex];


  }

  public
  void highLightCurrectButton() {
    String currectAnswer = "";
    try {
      currectAnswer = "" + currentElement.get(topic);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    for (int i = 0; i < buttons.length; i++) {
      if (currectAnswer.equals(buttons[i].getText()))
        buttons[i].getBackground().setColorFilter(Color.rgb(66, 244, 92), PorterDuff.Mode.MULTIPLY);
    }
  }

  public
  void highLightCorrectAnswers() {
    for (int i = 0; i < answers.size(); i++) {
      try {
        if (answers.get(i).equals(currentElement.get(topics[i])))
          elementViewArray[i].setTextColor(Color.rgb(66, 244, 92));
        else
          elementViewArray[i].setTextColor(Color.rgb(255, 0, 255));
      } catch (JSONException e) {
        e.printStackTrace();
      }

    }
  }


  public
  void resetButtonColor() {
    for (Button b : buttons) {
      b.getBackground().clearColorFilter();
    }
  }


  public
  ArrayList<Integer> getAnswerIndexs() {
    ArrayList<Integer> array = new ArrayList<>();
    int                space = 7;
    int max = periodicTable.length() - elementindex > space ? space : periodicTable
      .length() - elementindex - 1;
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

  public
  List<Integer> getPeriodNumbers() {
    int currentElementPeriod = 0;
    try {
      currentElementPeriod = (int) currentElement.get("period");
    } catch (JSONException e) {
      e.printStackTrace();
    }
    List<Integer> periods = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
    periods.remove(currentElementPeriod - 1);

    Collections.shuffle(periods);
    List<Integer> returnPeriods = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      returnPeriods.add(periods.get(i));
    }
    returnPeriods.add(currentElementPeriod);
    Collections.shuffle(returnPeriods);

    return returnPeriods;
  }

  public
  void setScore(String answer) {
    String correctAnswer = "";
    try {
      correctAnswer = currentElement.get(topic).toString();
    } catch (JSONException e) {
      e.printStackTrace();
    }

    if (answer.equals("" + correctAnswer)) {
      score += 100;
    }
    TextView scoreView = findViewById(R.id.score);
    scoreView.setText(String.format("%d Points", score));
  }

  public
  void setWhatToGuess() {
    TextView whatToGuessText = findViewById(R.id.whatToGuess);
    String[] text = {
      "atomic mass",
      "symbol",
      "atomic number",
      "group",
      "period",
      };
    String htmlText = String.format("Guess the <b> %s <b>", text[topicIndex]);
    whatToGuessText.setText(Html.fromHtml(htmlText));
  }

  public
  void updateElementView() {
    TextView[] elementViewArray = {
      Mass,
      Symbol,
      Number,
      Group,
      Period,
      ElementName
    };
    float[] textSize2 = {
      24,
      70,
      30,
      20,
      20,
      50
    };
    String[] textDescription = {
      " g/mol",
      "",
      "",
      "Group ",
      "Period ",
      ""
    };
    for (int i = 0; i < answers.size(); i++) {
      String text;
      if (i == 0)
        text = String.format("%s %s", answers.get(i), textDescription[i]);
      else
        text = String.format("%s %s", textDescription[i], answers.get(i));
      elementViewArray[i].setText(text);
      elementViewArray[i].setTextSize(textSize2[i]);
    }
  }

  public
  void clearElementView() {
    TextView[] elementViewArray = {
      Mass,
      Symbol,
      Number,
      Period,
      Group,
      ElementName
    };

    for (TextView tv : elementViewArray) {
      tv.setText("");
      tv.setTextColor(defaultTextColor);

    }
  }
}

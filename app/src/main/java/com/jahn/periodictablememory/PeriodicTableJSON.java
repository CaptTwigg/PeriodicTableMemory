package com.jahn.periodictablememory;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class PeriodicTableJSON {
  Context context;

  public PeriodicTableJSON(Context context) {
    this.context = context;
  }

  public String loadJSONFromAsset() {
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

  public JSONArray getPeriodicTable(int from, int to){
    JSONObject periodicTable = null;
    JSONArray periodicTableElements = null;
    JSONArray periodicTableElementsRanged = new JSONArray();

    try {
      periodicTable = new JSONObject(loadJSONFromAsset());
      System.out.println(periodicTable);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    try {
      periodicTableElements=  periodicTable.getJSONArray("elements");
    } catch (JSONException e) {
      e.printStackTrace();
    }
    for (int i = from; i < to; i++) {
      try {
        periodicTableElementsRanged.put(periodicTableElements.get(i));
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return periodicTableElementsRanged;
  }
}

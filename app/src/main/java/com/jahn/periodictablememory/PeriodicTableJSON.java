package com.jahn.periodictablememory;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class PeriodicTableJSON {

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

  public JSONArray getPeriodicTable(){
    JSONObject periodicTable = null;
    JSONArray periodicTableElements = null;

    try {
      periodicTable = new JSONObject(loadJSONFromAsset(MainActivity.this));
      System.out.println(periodicTable);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    try {
      periodicTableElements=  periodicTable.getJSONArray("elements");
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return periodicTableElements;
  }
}

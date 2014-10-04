package com.littleredgroup.jokennerd.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class CastMessages {

	public static String messageToChoice(int valueChoice) {
		JSONObject object = new JSONObject();
		try {
			object.put(Constants.KEY_ACTION, Constants.ACTION_CHOICE);
			object.put(Constants.KEY_VALUE, valueChoice);
			Log.d("messageToChoice: ", String.valueOf(valueChoice));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	public static String messageSetupUser(String name) {
		JSONObject object = new JSONObject();
		try {
			object.put(Constants.KEY_ACTION, Constants.ACTION_CONNECT);
			object.put(Constants.KEY_NAME, name);
			Log.d("messageSetupUser: ", name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	public static String messageUpdateUser(String name) {
		JSONObject object = new JSONObject();
		try {
			object.put(Constants.KEY_ACTION, Constants.ACTION_UPDATE);
			object.put(Constants.KEY_NAME, name);
			Log.d("messageUpdateUser: ", name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

}

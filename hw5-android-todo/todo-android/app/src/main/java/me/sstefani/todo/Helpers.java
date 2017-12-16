package me.sstefani.todo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


public class Helpers {

    public static void saveJWT(Activity activity, String jwt) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("jwt", jwt);
        editor.commit();
    }

    public static String loadJWT(Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString("jwt", "/");
    }
}

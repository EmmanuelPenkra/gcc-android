package com.penkra.gcc.Helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.penkra.gcc.GCC;

public class Actions {

    Context context;
    public SharedPreferences storage;
    public static  Actions instance = new Actions();

    public Actions (){
        context = GCC.getAppContext();
        storage = context.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void saveInfo(String key, Boolean value){
        SharedPreferences.Editor editor = storage.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void saveInfo(String key, String value){
        SharedPreferences.Editor editor = storage.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void saveInfo(String key, int value){
        SharedPreferences.Editor editor = storage.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void removeInfo(String key){
        storage.edit().remove(key).apply();
    }

    public void removeAll(){
        storage.edit().clear().apply();
    }

    public static void showAlert(Context c, String title, String message){
        showAlert(c, title, new String[]{message, "• Okay"}, ((dialog, which) -> {
            if (which == 1) dialog.dismiss();
        }));
    }

    public static void showAlert(Context c, String title, String[] actions, DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(c)
                .setTitle(title);
//                .setMessage(message);
        builder.setItems(actions, listener);
        builder.create().show();
    }

    public static void messagePhone(Context c, final String phoneNumber){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("sms:" + phoneNumber));
        c.startActivity(intent);
    }

    public static void callPhone(Context c, final String phoneNumber){
        c.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("sms:", phoneNumber, null)));
    }

    public static void hideKeyboard(Activity activity, View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

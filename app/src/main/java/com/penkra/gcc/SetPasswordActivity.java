package com.penkra.gcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.penkra.gcc.Helpers.Actions;
import com.penkra.gcc.Helpers.KeyValuePair;
import com.penkra.gcc.Helpers.Response;
import com.penkra.gcc.Helpers.VolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.Key;

import static com.penkra.gcc.Helpers.Constants.DOMAIN;

public class SetPasswordActivity extends AppCompatActivity implements View.OnFocusChangeListener, Response {

    EditText pass_input, cpass_input;
    Button continue_btn;
    VolleyRequest internet = new VolleyRequest(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("");

        pass_input = findViewById(R.id.pass_input);
        cpass_input = findViewById(R.id.cpass_input);

        pass_input.setOnFocusChangeListener(this);
        cpass_input.setOnFocusChangeListener(this);

        internet.response = this;

        continue_btn = findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener( view -> {
            Actions.hideKeyboard(this, view);
            internet.request(DOMAIN + "finish-up", Request.Method.POST, new KeyValuePair[]{
                    new KeyValuePair("fname", Actions.instance.storage.getString("fname", "")),
                    new KeyValuePair("lname", Actions.instance.storage.getString("lname", "")),
                    new KeyValuePair("email", Actions.instance.storage.getString("email", "")),
                    new KeyValuePair("password", pass_input.getText().toString()),
                    new KeyValuePair("cpassword", cpass_input.getText().toString())
            }, 0, true, continue_btn);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus) Actions.hideKeyboard(this, v);
    }

    @Override
    public void getResponse(String response, int identifier) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        if(jsonObject.getInt("_status") == 0){
            getError(jsonObject.getString("_error"), jsonObject.getString("_message"), identifier);
            return;
        }

        switch (identifier){
            case 0:
                Actions.instance.saveInfo("isLoggedIn", true);
                Actions.instance.saveInfo("user_id", jsonObject.getInt("user_id"));
                Actions.showAlert(this, "Account Created!", new String[]{"We have created your account successfully.", "• Awesome"}, (dialog, which) -> {
                    if (which == 1){
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
                break;
        }
    }

    @Override
    public void getError(String error, String message, int identifier) {
        Actions.showAlert(this, error, message);
    }
}
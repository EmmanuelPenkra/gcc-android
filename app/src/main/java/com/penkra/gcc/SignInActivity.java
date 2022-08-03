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

import static com.penkra.gcc.Helpers.Constants.DOMAIN;

public class SignInActivity extends AppCompatActivity implements View.OnFocusChangeListener, Response {

    EditText email_input, password_input;
    Button sign_in_btn, sign_up_btn;
    VolleyRequest internet = new VolleyRequest(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("");

        email_input = findViewById(R.id.email_input);
        password_input = findViewById(R.id.password_input);
        sign_in_btn = findViewById(R.id.sign_in_btn);
        sign_up_btn = findViewById(R.id.sign_up_btn);

        email_input.setOnFocusChangeListener(this);
        password_input.setOnFocusChangeListener(this);

        internet.response = this;

        sign_in_btn.setOnClickListener(view -> {
            Actions.hideKeyboard(this, view);
            internet.request(DOMAIN + "sign-in", Request.Method.POST, new KeyValuePair[]{
                    new KeyValuePair("email", email_input.getText().toString()),
                    new KeyValuePair("password", password_input.getText().toString())
            }, 0, true, sign_in_btn);
        });

        sign_up_btn.setOnClickListener(view -> {
            startActivity(new Intent(this, SignUpActivity.class));
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
                Actions.instance.saveInfo("user_id", jsonObject.getInt("user_id"));
                Actions.instance.saveInfo("icon", jsonObject.getString("icon"));
                Actions.instance.saveInfo("fname", jsonObject.getString("fname"));
                Actions.instance.saveInfo("lname", jsonObject.getString("lname"));
                Actions.instance.saveInfo("email", jsonObject.getString("email"));
                Actions.instance.saveInfo("isLoggedIn", true);

                Actions.instance.saveInfo("hasBusiness", jsonObject.getBoolean("hasBusiness"));
                if(jsonObject.getBoolean("hasBusiness")){
                    Actions.instance.saveInfo("bis_id", jsonObject.getInt("bis_id"));
                    Actions.instance.saveInfo("bis_icon", jsonObject.getString("bis_icon"));
                    Actions.instance.saveInfo("bis_name", jsonObject.getString("bis_name"));
                    Actions.instance.saveInfo("bis_location", jsonObject.getString("bis_location"));
                    Actions.instance.saveInfo("bis_category", jsonObject.getString("bis_category"));
                    Actions.instance.saveInfo("bis_email", jsonObject.getString("bis_email"));
                    Actions.instance.saveInfo("bis_phone", jsonObject.getString("bis_phone"));
                    Actions.instance.saveInfo("bis_description", jsonObject.getString("bis_description"));
                }

                Actions.showAlert(this, "Welcome back " + jsonObject.getString("fname"), new String[]{"• Awesome"}, (dialog, which) -> {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                });
                break;
        }
    }

    @Override
    public void getError(String error, String message, int identifier) {
        Actions.showAlert(this, error, message);
    }
}

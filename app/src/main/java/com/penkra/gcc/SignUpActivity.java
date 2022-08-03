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
import com.android.volley.RequestQueue;
import com.penkra.gcc.Helpers.Actions;
import com.penkra.gcc.Helpers.KeyValuePair;
import com.penkra.gcc.Helpers.Response;
import com.penkra.gcc.Helpers.VolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;

import static com.penkra.gcc.Helpers.Constants.DOMAIN;

public class SignUpActivity extends AppCompatActivity implements View.OnFocusChangeListener, Response {

    EditText fname_input, lname_input, email_input;
    Button continue_btn;
    VolleyRequest internet = new VolleyRequest(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("");

        fname_input = findViewById(R.id.fname_input);
        lname_input = findViewById(R.id.lname_input);
        email_input = findViewById(R.id.email_input);

        fname_input.setText(Actions.instance.storage.getString("fname", ""));
        lname_input.setText(Actions.instance.storage.getString("lname", ""));
        email_input.setText(Actions.instance.storage.getString("email", ""));

        fname_input.setOnFocusChangeListener(this);
        lname_input.setOnFocusChangeListener(this);
        email_input.setOnFocusChangeListener(this);

        internet.response = this;

        continue_btn = findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(view -> {
            Actions.hideKeyboard(this, view);
            KeyValuePair[] pairs = {
                    new KeyValuePair("fname", fname_input.getText().toString()),
                    new KeyValuePair("lname", lname_input.getText().toString()),
                    new KeyValuePair("email", email_input.getText().toString())
            };
            internet.request(DOMAIN + "sign-up", Request.Method.POST, pairs, 0, true, continue_btn);
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
                Actions.instance.saveInfo("icon", "");
                Actions.instance.saveInfo("fname", fname_input.getText().toString().trim());
                Actions.instance.saveInfo("lname", lname_input.getText().toString().trim());
                Actions.instance.saveInfo("email", email_input.getText().toString().trim());
                startActivity(new Intent(this, SetPasswordActivity.class));
                break;
        }
    }

    @Override
    public void getError(String error, String message, int identifier) {
        Actions.showAlert(this, error, message);
    }
}

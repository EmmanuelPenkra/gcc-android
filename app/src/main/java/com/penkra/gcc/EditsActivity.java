package com.penkra.gcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.penkra.gcc.Helpers.Actions;
import com.penkra.gcc.Helpers.KeyValuePair;
import com.penkra.gcc.Helpers.Response;
import com.penkra.gcc.Helpers.VolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;

import static com.penkra.gcc.Helpers.Constants.DOMAIN;

public class EditsActivity extends AppCompatActivity implements View.OnFocusChangeListener, Response {

    EditText description_input;
    Button update_btn;
    VolleyRequest internet = new VolleyRequest(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edits);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("");

        description_input = findViewById(R.id.description_input);
        update_btn = findViewById(R.id.update_btn);

        String description = Actions.instance.storage.getString("bis_description", "");
        description_input.setText(description);

        description_input.setOnFocusChangeListener(this);

        internet.response = this;

        update_btn.setOnClickListener(v -> {
            Actions.hideKeyboard(this, v);
            internet.request(DOMAIN + "update-description", Request.Method.POST, new KeyValuePair[]{
                    new KeyValuePair("user_id", Actions.instance.storage.getInt("user_id", 0) + ""),
                    new KeyValuePair("description", description_input.getText().toString())
            }, 0, true, update_btn);
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
    public void getResponse(String response, int identifier) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        if(jsonObject.getInt("_status") == 0){
            getError(jsonObject.getString("_error"), jsonObject.getString("_message"), identifier);
            return;
        }

        switch (identifier){
            case 0:
                Actions.instance.saveInfo("bis_description", description_input.getText().toString());
                Toast.makeText(this, "Description has been updated", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void getError(String error, String message, int identifier) {
        Actions.showAlert(this, error, message);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus) Actions.hideKeyboard(this, v);
    }
}

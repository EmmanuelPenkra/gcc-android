package com.penkra.gcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.penkra.gcc.Helpers.Actions;
import com.penkra.gcc.Helpers.KeyValuePair;
import com.penkra.gcc.Helpers.Response;
import com.penkra.gcc.Helpers.VolleyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.penkra.gcc.Helpers.Constants.DOMAIN;

public class NewBusinessActivity extends AppCompatActivity implements View.OnFocusChangeListener, Response {

    EditText name_input, location_input, email_input, phone_input;
    Spinner category_input;
    Button add_business_btn;
    VolleyRequest internet = new VolleyRequest(this);

    List<String> items = new ArrayList<>();
    List<Integer> item_ids = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_business);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("");

        name_input = findViewById(R.id.name_input);
        location_input = findViewById(R.id.location_input);
        email_input = findViewById(R.id.email_input);
        phone_input = findViewById(R.id.phone_input);
        category_input = findViewById(R.id.category_input);
        add_business_btn = findViewById(R.id.add_business_btn);

        name_input.setOnFocusChangeListener(this);
        location_input.setOnFocusChangeListener(this);
        email_input.setOnFocusChangeListener(this);
        phone_input.setOnFocusChangeListener(this);

        internet.response = this;
        internet.request(DOMAIN + "fetch-categories", Request.Method.POST, new KeyValuePair[]{}, 0, true, null);

        add_business_btn.setOnClickListener(view -> {
            Actions.hideKeyboard(this, view);
            internet.request(DOMAIN + "new-business", Request.Method.POST, new KeyValuePair[]{
                    new KeyValuePair("user_id", Actions.instance.storage.getInt("user_id", 0) + ""),
                    new KeyValuePair("name", name_input.getText().toString()),
                    new KeyValuePair("category", item_ids.get(category_input.getSelectedItemPosition()) + ""),
                    new KeyValuePair("location", location_input.getText().toString()),
                    new KeyValuePair("email", email_input.getText().toString()),
                    new KeyValuePair("phone", phone_input.getText().toString())
            }, 1, true, add_business_btn);
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
                JSONArray array = jsonObject.getJSONArray("categories");
                for (int i = 0; i < array.length(); i++){
                    final JSONObject object = array.getJSONObject(i);
                    item_ids.add(object.getInt("id"));
                    items.add(object.getString("name"));
                }
                category_input.setAdapter(new ArrayAdapter<> ( this, android.R.layout.simple_list_item_1, items ));
                break;
            case 1:
                Actions.instance.saveInfo("hasBusiness", true);
                Actions.instance.saveInfo("bis_id", jsonObject.getInt("business_id"));
                Actions.instance.saveInfo("bis_icon", "");
                Actions.instance.saveInfo("bis_name", name_input.getText().toString().trim());
                Actions.instance.saveInfo("bis_category", category_input.getSelectedItem().toString());
                Actions.instance.saveInfo("bis_location", location_input.getText().toString());
                Actions.instance.saveInfo("bis_email", email_input.getText().toString());
                Actions.instance.saveInfo("bis_phone", phone_input.getText().toString());
                Actions.instance.saveInfo("bis_description", "");

                Actions.showAlert(this, "", new String[]{"Your business has been added", "• Awesome"}, (dialog, which) -> {
                    if (which == 1) {
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

package com.penkra.gcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.penkra.gcc.Helpers.Actions;

public class VerifyEmailActivity extends AppCompatActivity implements View.OnFocusChangeListener {

    EditText code_input;
    Button continue_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("");

        code_input = findViewById(R.id.code_input);
        code_input.setOnFocusChangeListener(this);

        continue_btn = findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener( view -> {
            startActivity(new Intent(this, SetPasswordActivity.class));
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
}
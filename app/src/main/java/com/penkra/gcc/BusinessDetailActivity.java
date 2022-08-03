package com.penkra.gcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.makeramen.roundedimageview.Corner;
import com.makeramen.roundedimageview.RoundedImageView;
import com.penkra.gcc.Helpers.Actions;
import com.penkra.gcc.Helpers.KeyValuePair;
import com.penkra.gcc.Helpers.Objects;
import com.penkra.gcc.Helpers.Response;
import com.penkra.gcc.Helpers.VolleyRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.penkra.gcc.Helpers.Constants.DOMAIN;

public class BusinessDetailActivity extends AppCompatActivity implements Response {

    ImageView logo;
    RoundedImageView contact_image;
    TextView description, location, phone, email, contact_fname, contact_lname;
    NestedScrollView scrollView;
    AppBarLayout app_bar;
    CoordinatorLayout coordinatorLayout;
    FloatingActionButton heart;
    Objects.Business business;
    Boolean is_favorite;
    VolleyRequest internet = new VolleyRequest(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle prev = getIntent().getExtras();
        business = new Objects.Business(prev.getInt("id"), prev.getString("name"),
                prev.getString("logo"), prev.getString("category"), prev.getString("description"));

        logo = findViewById(R.id.logo);
        contact_image = findViewById(R.id.contact_image);
        description = findViewById(R.id.description);
        location = findViewById(R.id.location);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        contact_fname = findViewById(R.id.contact_fname);
        contact_lname = findViewById(R.id.contact_lname);
        scrollView = findViewById(R.id.scrollView);
        app_bar = findViewById(R.id.app_bar);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        heart = findViewById(R.id.heart);

        if (!business.logo.equals("")) Picasso.get()
                .load(DOMAIN + business.logo)
                .placeholder(R.drawable.ic_image)
                .into(logo);
        description.setText(business.description);
        toolbar.setTitle(business.name);

        heart.setOnClickListener(view -> {
            internet.request(DOMAIN + (is_favorite ? "remove-favorite" : "add-favorite"), Request.Method.POST, new KeyValuePair[]{
                    new KeyValuePair("user_id", Actions.instance.storage.getInt("user_id", 0) + ""),
                    new KeyValuePair("business_id", business.id + "")
            }, is_favorite ? 2 : 1, true, null);
        });
        internet.response = this;

        internet.request(DOMAIN + "fetch-business", Request.Method.POST, new KeyValuePair[]{
                new KeyValuePair("business_id", business.id + "")
        }, 0, false, null);
    }

    void toggleFavorite() {
        heart.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, is_favorite ? R.color.red : R.color.white)));
        heart.setImageDrawable(getDrawable(is_favorite ? R.drawable.ic_heart_white : R.drawable.ic_heart_red));
    }

    @Override
    public void getResponse(String response, int identifier) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.getInt("_status") == 0) {
            getError(jsonObject.getString("_error"), jsonObject.getString("_message"), identifier);
            return;
        }

        switch (identifier) {
            case 0:
                String icon = jsonObject.getString("icon");
                if (!icon.equals("")) Picasso.get()
                        .load(DOMAIN + icon)
                        .placeholder(R.drawable.ic_user)
                        .into(contact_image);
                contact_fname.setText(jsonObject.getString("fname"));
                contact_lname.setText(jsonObject.getString("lname"));
                phone.setText(jsonObject.getString("phone"));
                email.setText(jsonObject.getString("email"));
                location.setText(jsonObject.getString("location"));

                contact_image.setOnClickListener(view -> {
                    if (!icon.equals("")) {
                        Intent intent = new Intent(this, ImageViewActivity.class);
                        intent.putExtra("IDENTIFIER", 2);
                        intent.putExtra("icon", icon);
                        startActivity(intent);
                    }
                });

                is_favorite = jsonObject.getBoolean("is_favorite");
                break;
            case 1:
                is_favorite = true;
                Toast.makeText(this, "Added to Favorites", Toast.LENGTH_LONG).show();
                break;
            case 2:
                is_favorite = false;
                Toast.makeText(this, "Removed from Favorites", Toast.LENGTH_LONG).show();
                break;
        }
        ;
        toggleFavorite();
    }

    @Override
    public void getError(String error, String message, int identifier) {
        Actions.showAlert(this, error, message);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}

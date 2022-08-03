package com.penkra.gcc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.penkra.gcc.Helpers.Actions;
import com.penkra.gcc.Helpers.KeyValuePair;
import com.penkra.gcc.Helpers.VolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.Key;

import com.penkra.gcc.Helpers.Response;
import com.squareup.picasso.Picasso;

import static com.penkra.gcc.Helpers.Constants.DOMAIN;

public class ImageViewActivity extends AppCompatActivity implements Response {

    int identifier;
    ImageView imageView;
    Bitmap photo;
    private final int PICK_IMAGE = 0;
    VolleyRequest internet = new VolleyRequest(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("");

        identifier = getIntent().getIntExtra("IDENTIFIER", 0);
        imageView = findViewById(R.id.imageView);

        if (identifier < 2) {
            internet.response = this;

            String store = "";
            switch (identifier) {
                case 0:
                    store = "icon";
                    break;
                case 1:
                    store = "bis_icon";
                    break;
            }
            String image = Actions.instance.storage.getString(store, "");
            if (!image.equals("")) Picasso.get()
                    .load(DOMAIN + image)
                    .placeholder(R.drawable.ic_image)
                    .into(imageView);
            else {
                switch (identifier) {
                    case 0:
                        imageView.setImageResource(R.drawable.ic_user);
                    case 1:
                        imageView.setImageResource(R.drawable.ic_customer);
                }
            }
        }else Picasso.get()
                .load(DOMAIN + getIntent().getStringExtra("icon"))
                .placeholder(R.drawable.ic_user)
                .into(imageView);
    }

    void uploadImage(){
        internet.request(DOMAIN + "upload-image", Request.Method.POST, new KeyValuePair[]{
                new KeyValuePair("identifier", identifier + ""),
                new KeyValuePair("user_id", Actions.instance.storage.getInt("user_id", 0) + ""),
                new KeyValuePair("image", getStringImage(photo))
        }, 0, false, null);
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
                switch (this.identifier){
                    case 0: Actions.instance.saveInfo("icon", jsonObject.getString("image")); break;
                    case 1: Actions.instance.saveInfo("bis_icon", jsonObject.getString("image")); break;
                }
                Toast.makeText(this, "Image successfully uploaded", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void getError(String error, String message, int identifier) {
        Actions.showAlert(this, error, message);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.change_icon:
                if (identifier < 2) {
                    Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    getIntent.setType("image/*");

                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("image/*");

                    Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                    startActivityForResult(chooserIntent, PICK_IMAGE);
                }
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && data != null) {
            Bundle bundle = data.getExtras();
//            if(bundle != null) {
//                photo = (Bitmap) bundle.get("data");
//                imageView.setImageBitmap(photo);
//            }else Toast.makeText(this, "Couldn't receive image", Toast.LENGTH_LONG).show();

            Uri selectedImageUri = data.getData();
            // Get the path from the Uri
            final String path = getPathFromURI(selectedImageUri);
            if (path != null) {
                File f = new File(path);
                selectedImageUri = Uri.fromFile(f);
            }
            try {
                photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                imageView.setImageBitmap(photo);
                Toast.makeText(this, "Uploading Image... Please wait", Toast.LENGTH_LONG).show();
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Set the image in ImageView
        }
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageByte = stream.toByteArray();
        return Base64.encodeToString(imageByte, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.change_menu, menu);
        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return true;
    }
}

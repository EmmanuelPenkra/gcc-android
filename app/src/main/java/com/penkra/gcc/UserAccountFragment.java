package com.penkra.gcc;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.penkra.gcc.Helpers.Actions;
import com.squareup.picasso.Picasso;

import static com.penkra.gcc.Helpers.Constants.DOMAIN;

public class UserAccountFragment extends Fragment {

    RoundedImageView user_image;
    TextView user_name, user_email;
//    Button btn_name, btn_email;

    public UserAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_account, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Account");

        user_image = view.findViewById(R.id.user_image);
        user_name = view.findViewById(R.id.user_name);
        user_email = view.findViewById(R.id.user_email);

//        btn_name = view.findViewById(R.id.btn_name);
//        btn_email = view.findViewById(R.id.btn_email);

        user_name.setText(Actions.instance.storage.getString("fname", "") + " " +
                Actions.instance.storage.getString("lname", ""));
        user_email.setText(Actions.instance.storage.getString("email", ""));

        user_image.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ImageViewActivity.class);
            intent.putExtra("IDENTIFIER", 0);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String image = Actions.instance.storage.getString("icon", "");
        if(!image.equals("")) Picasso.get()
                .load(DOMAIN + image)
                .placeholder(R.drawable.ic_image)
                .resize(100, 100)
                .centerCrop()
                .into(user_image);
    }
}

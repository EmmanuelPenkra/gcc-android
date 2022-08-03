package com.penkra.gcc;

import android.content.Intent;
import android.icu.text.Edits;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessProfileFragment extends Fragment {

    RoundedImageView bis_image;
    TextView bis_name, bis_category, bis_location, bis_email, bis_phone, bis_description;
//    Button btn_name, btn_category, btn_location, btn_email, btn_phone;
    Button btn_description;

    public BusinessProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_business_profile, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Business");

        bis_image = view.findViewById(R.id.bis_image);
        bis_name = view.findViewById(R.id.bis_name);
        bis_category = view.findViewById(R.id.bis_category);
        bis_location = view.findViewById(R.id.bis_location);
        bis_email = view.findViewById(R.id.bis_email);
        bis_phone = view.findViewById(R.id.bis_phone);
        bis_description = view.findViewById(R.id.bis_description);

//        btn_name = view.findViewById(R.id.btn_name);
//        btn_category = view.findViewById(R.id.btn_category);
//        btn_location = view.findViewById(R.id.btn_location);
//        btn_email = view.findViewById(R.id.btn_email);
//        btn_phone = view.findViewById(R.id.btn_phone);
        btn_description = view.findViewById(R.id.btn_description);

        bis_name.setText(Actions.instance.storage.getString("bis_name", ""));
        bis_category.setText(Actions.instance.storage.getString("bis_category", ""));
        bis_location.setText(Actions.instance.storage.getString("bis_location", ""));
        bis_email.setText(Actions.instance.storage.getString("bis_email", ""));
        bis_phone.setText(Actions.instance.storage.getString("bis_phone", ""));

        bis_image.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ImageViewActivity.class);
            intent.putExtra("IDENTIFIER", 1);
            startActivity(intent);
        });

        btn_description.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), EditsActivity.class));
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String image = Actions.instance.storage.getString("bis_icon", "");
        if(!image.equals("")) Picasso.get()
                .load(DOMAIN + image)
                .placeholder(R.drawable.ic_image)
                .resize(100, 100)
                .centerCrop()
                .into(bis_image);

        String description = Actions.instance.storage.getString("bis_description", "");
        if (description.equals("")) description = "(No description added)";
        bis_description.setText(description);
    }
}

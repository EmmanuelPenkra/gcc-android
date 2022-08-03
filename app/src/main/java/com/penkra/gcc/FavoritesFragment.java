package com.penkra.gcc;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.penkra.gcc.Helpers.Actions;
import com.penkra.gcc.Helpers.Adapters;
import com.penkra.gcc.Helpers.KeyValuePair;
import com.penkra.gcc.Helpers.Objects;
import com.penkra.gcc.Helpers.Response;
import com.penkra.gcc.Helpers.VolleyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.penkra.gcc.Helpers.Constants.DOMAIN;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends Fragment implements Response {

    ListView business_list;
    List<Objects.Business> businesses = new ArrayList<>();
    Adapters.BusinessAdapter adapter;
    TextView empty_text;
    VolleyRequest internet;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Favorite Businesses");

        internet = new VolleyRequest(getActivity());
        business_list = view.findViewById(R.id.business_list);
        empty_text = view.findViewById(R.id.empty_text);

        adapter = new Adapters.BusinessAdapter(getActivity(), businesses);
        business_list.setAdapter(adapter);

        business_list.setOnItemClickListener((parent, v, position, id) -> {
            Intent intent = new Intent(getActivity(), BusinessDetailActivity.class);
            intent.putExtra("id", businesses.get(position).id);
            intent.putExtra("name", businesses.get(position).name);
            intent.putExtra("logo", businesses.get(position).logo);
            intent.putExtra("category", businesses.get(position).category);
            intent.putExtra("description", businesses.get(position).description);
            startActivity(intent);
        });

        internet.response = this;

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        internet.request(DOMAIN + "fetch-favorites", Request.Method.POST, new KeyValuePair[]{
                new KeyValuePair("user_id", Actions.instance.storage.getInt("user_id", 0) + "")
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
                businesses.clear();
                JSONArray businesses = jsonObject.getJSONArray("businesses");
                for (int i = 0; i < businesses.length(); i++){
                    final JSONObject business = businesses.getJSONObject(i);
                    this.businesses.add(new Objects.Business(
                            business.getInt("id"),
                            business.getString("name"),
                            business.getString("logo"),
                            business.getString("category"),
                            business.getString("description")
                    ));
                }
                adapter.notifyDataSetChanged();
                empty_text.setVisibility(this.businesses.size() == 0 ? View.VISIBLE : View.GONE);
                break;
        }
    }

    @Override
    public void getError(String error, String message, int identifier) {
        Actions.showAlert(getActivity(), error, message);
    }
}

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
import android.widget.Toast;

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
public class PaymentHistoryFragment extends Fragment implements Response {

    ListView payments_list;
    List<Objects.PaymentHistory> payments = new ArrayList<>();
    Adapters.PaymentHistoryAdapter adapter;
    TextView empty_text;
    VolleyRequest internet;

    public PaymentHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_history, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Payment History");

        internet = new VolleyRequest(getActivity());
        payments_list = view.findViewById(R.id.payments_list);
        empty_text = view.findViewById(R.id.empty_text);

        adapter = new Adapters.PaymentHistoryAdapter(getActivity(), payments);
        payments_list.setAdapter(adapter);

        internet.response = this;
        internet.request(DOMAIN + "fetch-payment-history", Request.Method.POST, new KeyValuePair[]{
                new KeyValuePair("business_id", Actions.instance.storage.getInt("bis_id", 0) + "")
        }, 0, false, null);

        return view;
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
                JSONArray payments = jsonObject.getJSONArray("history");
                for (int i = 0; i < payments.length(); i++){
                    final JSONObject payment = payments.getJSONObject(i);
                    this.payments.add(new Objects.PaymentHistory(
                            payment.getString("amount"),
                            payment.getString("payment_method"),
                            payment.getString("reason"),
                            payment.getString("date")
                    ));
                }
                adapter.notifyDataSetChanged();
                empty_text.setVisibility(this.payments.size() == 0 ? View.VISIBLE : View.GONE);
                break;
        }
    }

    @Override
    public void getError(String error, String message, int identifier) {
        Actions.showAlert(getActivity(), error, message);
    }
}

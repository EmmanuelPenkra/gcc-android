package com.penkra.gcc;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.penkra.gcc.Helpers.Adapters;
import com.penkra.gcc.Helpers.Objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment {

    ExpandableListView listView;
    Adapters.HelpAdapter adapter;
    Objects.Help[] helps = {
            new Objects.Help(
                    "How do I add my business?",
                    new String[]{
                            "Create your account by going to User Account on the menu. Afterwards, go to Business " +
                                    "Profile and create your Business Profile. Add a description and a logo of your business " +
                                    "to your Business Profile.",
                            "For the upkeep of this technology, you will have to subscribe to a monthly payment of GHC 5 " +
                                    "to make and keep your Business public to everyone. You will have more publicity and " +
                                    "more clients!"
                    }
            ),
            new Objects.Help(
                    "When do I make each month's payment?",
                    new String[]{
                            "You will pay for the the next month at the end of its previous month",
                            "If you fail to make a payment, your profile will not be public until you make that payment."
                    }
            ),
            new Objects.Help(
                    "How do I make the monthly payments?",
                    new String[]{
                            "At the end of every month, you can pay for the next month by MTN Mobile Money " +
                                    "by sending GHC 5 to +233 26 429 1705 with your name and business name as the " +
                                    "description."
                    }
            )
    };

    public HelpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_help, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Help");

        listView = view.findViewById(R.id.listView);

        adapter = new Adapters.HelpAdapter(getActivity(), helps);
        listView.setAdapter(adapter);

        return view;
    }
}

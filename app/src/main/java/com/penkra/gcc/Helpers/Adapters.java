package com.penkra.gcc.Helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.makeramen.roundedimageview.RoundedImageView;
import com.penkra.gcc.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.penkra.gcc.Helpers.Constants.DOMAIN;

public class Adapters {
    public static class BusinessAdapter  extends ArrayAdapter<Objects.Business> {

        Context context;
        List<Objects.Business> businesses;

        public BusinessAdapter(Context context, List<Objects.Business> businesses){
            super(context, R.layout.business_row, businesses);
            this.context = context;
            this.businesses = businesses;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.business_row, parent, false);
            }

            RoundedImageView logo = convertView.findViewById(R.id.logo);
            TextView name = convertView.findViewById(R.id.name);
            TextView category = convertView.findViewById(R.id.category);
            TextView description = convertView.findViewById(R.id.description);

            Objects.Business business_row = businesses.get(position);
            if(!business_row.logo.equals("")) Picasso.get()
                    .load(DOMAIN + business_row.logo)
                    .placeholder(R.drawable.ic_image)
                    .resize(100, 100)
                    .centerCrop()
                    .into(logo);

            name.setText(business_row.name);
            category.setText(business_row.category);
            description.setText(business_row.description);

            return convertView;
        }
    }
    public static class PaymentHistoryAdapter  extends ArrayAdapter<Objects.PaymentHistory> {

        Context context;
        List<Objects.PaymentHistory> payments;

        public PaymentHistoryAdapter(Context context, List<Objects.PaymentHistory> payments){
            super(context, R.layout.history_row, payments);
            this.context = context;
            this.payments = payments;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.history_row, parent, false);
            }

            TextView date = convertView.findViewById(R.id.date);
            TextView amount = convertView.findViewById(R.id.amount);
            TextView text = convertView.findViewById(R.id.text);

            Objects.PaymentHistory payment = payments.get(position);

            date.setText(payment.date);
            amount.setText("GH₵" + payment.amount);
            text.setText(payment.reason + " via " + payment.payment_method);

            return convertView;
        }
    }

    public static class HelpAdapter extends BaseExpandableListAdapter {

        Context context;
        Objects.Help[] helps;

        public HelpAdapter(Context context, Objects.Help[] helps){
            this.context = context;
            this.helps = helps;
        }

        @Override
        public int getGroupCount() {
            return helps.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return helps[groupPosition].details.length;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return helps[groupPosition];
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return helps[groupPosition].details[childPosition];
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.help_parent_row, null);
            }

            TextView textView = convertView.findViewById(R.id.list_parent);
            textView.setText(helps[groupPosition].title);

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.help_child_row, null);
            }

            TextView textView = convertView.findViewById(R.id.list_parent);
            convertView.setBackgroundColor(context.getResources().getColor(R.color.background));
            textView.setText(helps[groupPosition].details[childPosition]);

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
}

package com.penkra.gcc.Helpers;

import android.content.DialogInterface;

public class Objects {
    public static class Business {
        public int id;
        public String name, logo, category, description;

        public Business(int id, String name, String logo, String category, String description){
            this.id = id;
            this.name = name;
            this.logo = logo;
            this.category = category;
            this.description = description;
        }
    }

    public static class PaymentHistory {
        public String amount, payment_method, reason, date;

        public PaymentHistory(String amount, String payment_method, String reason, String date){
            this.amount = amount;
            this.payment_method = payment_method;
            this.reason = reason;
            this.date = date;
        }
    }

    public static class Help {
        public String title;
        public String[] details;

        public Help(String title, String[] details){
            this.title = title;
            this.details = details;
        }
    }
}

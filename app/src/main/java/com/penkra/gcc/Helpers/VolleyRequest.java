package com.penkra.gcc.Helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.penkra.gcc.R;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class VolleyRequest {

    RequestQueue requestQueue;
    Context context;
    public Response response;
    Boolean requesting = false;

    public VolleyRequest(Context context){
        this.context = context;
    }

    public void request(String url, int method, KeyValuePair[] pairs, int identifier, Boolean await, Button button){
        if(await){
            if(requesting) return;
            requesting = true;
        }
        String btn_text = "";
        if(button != null){
            btn_text = button.getText().toString();
            button.setText("Please Wait");
        }
        requestQueue = Volley.newRequestQueue(context);
        String finalBtn_text = btn_text;
        StringRequest request = new StringRequest(method, url,
                response -> {
                    if (await) requesting = false;
                    if (button != null) button.setText(finalBtn_text);
                    try {
                        this.response.getResponse(response, identifier);
                    }catch (JSONException e){
                        Log.d("INFO", "Something happened to identifier " + identifier);
                    }
                },
                error -> {
                    if (await) requesting = false;
                    if (button != null) button.setText(finalBtn_text);
                    response.getError("", error.toString(), identifier);
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                for (KeyValuePair pair : pairs) params.put(pair.key, pair.value);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        requestQueue.add(request);
    }

//    private SSLSocketFactory getSocketFactory() {
//
//        CertificateFactory cf = null;
//        try {
//
//            cf = CertificateFactory.getInstance("X.509");
//            InputStream caInput = context.getResources().openRawResource(R.raw.cert_name);
//            Certificate ca;
//            try {
//                ca = cf.generateCertificate(caInput);
//                Log.e("CERT", "ca=" + ((X509Certificate) ca).getSubjectDN());
//            } finally {
//                caInput.close();
//            }
//
//
//            String keyStoreType = KeyStore.getDefaultType();
//            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
//            keyStore.load(null, null);
//            keyStore.setCertificateEntry("ca", ca);
//
//
//            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//            tmf.init(keyStore);
//
//
//            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
//                @Override
//                public boolean verify(String hostname, SSLSession session) {
//
//                    Log.e("CipherUsed", session.getCipherSuite());
//                    return hostname.compareTo("10.199.89.68")==0; //The Hostname of your server.
//
//                }
//            };
//
//
//            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
//            SSLContext context = null;
//            context = SSLContext.getInstance("TLS");
//
//            context.init(null, tmf.getTrustManagers(), null);
//            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
//
//            SSLSocketFactory sf = context.getSocketFactory();
//
//
//            return sf;
//
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
//
//        return  null;
//    }
}

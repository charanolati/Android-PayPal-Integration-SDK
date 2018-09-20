package com.gurucharan.paypalandroid;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.pddstudio.highlightjs.HighlightJsView;
import com.pddstudio.highlightjs.models.Language;
import com.pddstudio.highlightjs.models.Theme;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by GuruCharan on 19/11/2018.
 */

public class Final_Activity extends AppCompatActivity {

//Actvity will execute payments api is completed

    String status,Token,PayerId;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        List<String> params = getIntent().getData().getPathSegments();
        TextView textView = findViewById(R.id.status);

        status = params.get(0);
        Token = params.get(1);
        PayerId = params.get(2);
        if(status.equals("success")){
            textView.setText("Transaction Sucesss");


            //todo Do Your Own Implementation

            get_payment_details();//Optional get details from paypal Server

        }else{
            textView.setText("Transaction Failed/Pending");

        }
    }

    public void get_payment_details(){
        try{
            final TextView textLoader = findViewById(R.id.statusLoading);
            final HighlightJsView highlightJsView = findViewById(R.id.highlight_view);

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(60000);

            String getPaymentDetails_url = "https://node-paypal-express-sever.herokuapp.com/get-payment-details";
            getPaymentDetails_url = getPaymentDetails_url+"?token="+Token+"&payerID="+PayerId;

            client.post( getPaymentDetails_url,  new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

                    highlightJsView.setTheme(Theme.ANDROID_STUDIO);
                    highlightJsView.setHighlightLanguage(Language.AUTO_DETECT);
                    try{
                        highlightJsView.setSource(String.valueOf(String.valueOf(response.toString(4))));
                        textLoader.setVisibility(View.INVISIBLE);
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
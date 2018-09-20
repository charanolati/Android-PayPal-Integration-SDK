package com.gurucharan.paypalandroid;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cz.msebera.android.httpclient.entity.StringEntity;

public class MainActivity extends AppCompatActivity {

    int TAB_REQUEST_CODE = 465;
    String Calling_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button proceedbtn = findViewById(R.id.proceedbtn);
        proceedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo change Accordingly
                //Calling_URL;//Change with your Own Server URl
                //open_cct();//Paypal Running on Own Server
                paypal_demo();// Paypal Demo
            }
        });
    }

    public boolean isPackageInstalled(String packageName) {
        try{
            getApplicationContext().getPackageManager().getPackageInfo(packageName, 0);
            return true;
        }catch(PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void paypal_demo(){
        try{
            String price = ((EditText) findViewById(R.id.priceEt)).getText().toString();

            JSONObject productJson = new JSONObject();
            productJson.put("description","Paid to me");
            productJson.put("shipping","0");
            productJson.put("tax","0");
            productJson.put("shipping_discount","0");
            productJson.put("total",price);
            productJson.put("currency","USD");
            productJson.put("intent","sale");
            productJson.put("subtotal",price);
            productJson.put("name","name");
            productJson.put("price",price);
            productJson.put("quantity","1");
            productJson.put("handling_fee","0");
            productJson.put("insurance","0");
            productJson.put("customFlag","false");

            StringEntity requestData = new StringEntity(productJson.toString());

            String createPayments_url = "https://node-paypal-express-sever.herokuapp.com/create-payments";

            new AsyncHttpClient().post(getApplicationContext(), createPayments_url, requestData, "application/json", new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                    // code to open URL in Custom Chrome Tab
                    try{

                        Log.i("response", String.valueOf(response));
                        JSONArray links = response.getJSONArray("links");

                        for (int i = 0; i < links.length(); i++) {
                            JSONObject elements = links.getJSONObject(i);
                            Iterator<?> keys = elements.keys();
                            while (keys.hasNext()) {
                                String key = (String) keys.next();
                                if(elements.get(key).toString().equals("REDIRECT")) {
                                    Calling_URL =  elements.get("href").toString();
                                }
                            }

                        }

                        open_cct();

                    } catch(JSONException e) {
                        e.printStackTrace();
                    }
                    catch(ActivityNotFoundException a) {
                        Toast.makeText(getApplicationContext(),"Chrome Browser Not Installed",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void open_cct(){
        String packageName = "com.android.chrome";
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();

        // check if chrome is installed if installed always open in chrome so we can have OneTouch Feature !
        if(isPackageInstalled(packageName)) {
            customTabsIntent.intent.setPackage(packageName);
        }

        customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        customTabsIntent.intent.setData(Uri.parse(Calling_URL));
        startActivityForResult(customTabsIntent.intent, TAB_REQUEST_CODE);
    }
}

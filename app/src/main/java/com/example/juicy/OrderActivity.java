package com.example.juicy;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Vibrator;
import android.provider.Settings.Secure;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class OrderActivity extends AppCompatActivity{
    Button apple, banana, grape, cherry;
    Animation shake;
    ProgressDialog PD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        final CheckBox apple = (CheckBox) findViewById(R.id.checkBoxApple);
        final CheckBox banana = (CheckBox) findViewById(R.id.checkBoxBanana);
        final CheckBox lemon = (CheckBox) findViewById(R.id.checkBoxLemon);
        final CheckBox orange = (CheckBox) findViewById(R.id.checkBoxOrange);
        final CheckBox carrot = (CheckBox) findViewById(R.id.checkBoxCarrot);
        final CheckBox pineapple = (CheckBox) findViewById(R.id.checkBoxPineapple);
        final CheckBox cherry = (CheckBox) findViewById(R.id.checkBoxCherry);
        shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        final ImageButton order = (ImageButton) findViewById(R.id.imageButton);

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.startAnimation(shake);
                Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                vib.vibrate(500);
                PD = new ProgressDialog(OrderActivity.this);
                PD.setMessage("Loading.....");
                PD.show();
                ArrayList<String> result = new ArrayList<String>();
                result.add("apple: "+apple.isChecked());
                result.add("lemon: "+lemon.isChecked());
                result.add("orange: "+orange.isChecked());
                result.add("carrot: "+carrot.isChecked());
                result.add("pineapple: "+pineapple.isChecked());
                result.add("banana: "+banana.isChecked());
                result.add("cherry: "+cherry.isChecked());

                String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()).toString();
                String customerID = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
                String description = result.toString();


                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success){
                                Intent intent = new Intent(OrderActivity.this, SuccessActivity.class);
                                OrderActivity.this.startActivity(intent);
                                PD.dismiss();
                            }else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
                                builder.setMessage("Register Failed").setNegativeButton("Retry", null).create().show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                OrderRequest orderRequest = new OrderRequest(date, customerID, description, responseListener);
                RequestQueue queue = Volley.newRequestQueue(OrderActivity.this);
                queue.add(orderRequest);

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(OrderActivity.this, LoginActivity.class);
            OrderActivity.this.startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.example.juicy;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReadData extends ListActivity {

    String url = "http://dailyday.xyz/read_allorder.php";
    ArrayList<HashMap<String, String>> Item_List;
    ProgressDialog PD;
    ListAdapter adapter;

    ListView listview = null;

    // JSON Node names
    public static final String ORDER_ID = "id";
    public static final String ORDER_DATE = "date";
    public static final String ORDER_CUSTOMERID = "customerid";
    public static final String ORDER_DESC = "description";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.read);

        listview = getListView();
        Item_List = new ArrayList<HashMap<String, String>>();

        ReadDataFromDB();
    }

    private void ReadDataFromDB() {
        PD = new ProgressDialog(this);
        PD.setMessage("Loading.....");
        PD.show();

        JsonObjectRequest jreq = new JsonObjectRequest(Method.GET, url,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int success = response.getInt("success");

                            if (success == 1) {
                                JSONArray ja = response.getJSONArray("orders");

                                for (int i = 0; i < ja.length(); i++) {
                                    Log.e("if success:", "success");
                                    JSONObject jobj = ja.getJSONObject(i);
                                    HashMap<String, String> item = new HashMap<String, String>();
                                    item.put(ORDER_ID, jobj.getString(ORDER_ID));
                                    item.put(ORDER_DATE,jobj.getString(ORDER_DATE));
                                    item.put(ORDER_CUSTOMERID,jobj.getString(ORDER_CUSTOMERID));
                                    item.put(ORDER_DESC,jobj.getString(ORDER_DESC));


                                    Item_List.add(item);

                                } // for loop ends

                                String[] from = {ORDER_ID, ORDER_DESC, ORDER_DATE};
                                int[] to = {R.id.item_id, R.id.item_name, R.id.item_date};

                                adapter = new SimpleAdapter(
                                        getApplicationContext(), Item_List,
                                        R.layout.list_items, from, to);

                                listview.setAdapter(adapter);

                                listview.setOnItemClickListener(new ListitemClickListener());

                                PD.dismiss();

                            } // if ends

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
            }
        });

        // Adding request to request queue
        MyApplication.getInstance().addToReqQueue(jreq);

    }


    //On List Item Click move to UpdateDelete Activity
    class ListitemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

            Intent modify_intent = new Intent(ReadData.this,
                    UserActivity.class);

            modify_intent.putExtra("item", Item_List.get(position));

            startActivity(modify_intent);

        }
    }
}
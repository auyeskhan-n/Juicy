package com.example.juicy;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 77 on 02.04.2016.
 */
public class OrderRequest extends StringRequest {
    private  static final String ORDER_REQUEST_URL = "http://dailyday.xyz/Order.php";
    private Map<String, String> params;

    public OrderRequest(String date, String customerID, String description, Response.Listener<String> listener){
        super(Method.POST, ORDER_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put("date",date);
        params.put("customerID",customerID);
        params.put("description", description);
    }
//

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

package com.example.workoutapp;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class WiseRequest extends StringRequest {

    final static private String URL = "http://wlgur0914.dothome.co.kr/Wise.php";
    private Map<String, String> map;


    public WiseRequest(String wiseNo, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("wishNo",wiseNo);


    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}


package com.example.workoutapp;


import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    // 서버 URL 설정 ( PHP 파일 연동 )
    final static private String URL = "http://wlgur0914.dothome.co.kr/Register.php";;
    private Map<String, String> map;


    public RegisterRequest(String userID, String userName, String userGender,  String userBirthday, double userWeight,
                           double userHeight, String userLocation, String userClass,
                           double userBmi, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID",userID);
        map.put("userName", userName);
        map.put("userGender", userGender);
        map.put("userBirthday", userBirthday);
        map.put("userWeight", userWeight + "");
        map.put("userHeight", userHeight + "");
        map.put("userLocation", userLocation);
        map.put("userClass", userClass);
        map.put("userBmi", userBmi + "");

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
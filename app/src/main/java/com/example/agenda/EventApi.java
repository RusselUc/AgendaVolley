package com.example.agenda;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class EventApi {
    private RequestQueue requestQueue;
    //String url = "http://192.168.0.104:4000/api/users";
    private String url = "https://api-agenda-isc.herokuapp.com/api/events";
    private Context context;

    public void store(String title, String activity, String date, String hour, String id, String status){
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        System.out.println(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        System.out.println(error);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("title", title);
                params.put("activity", activity);
                params.put("date", date);
                params.put("time", hour);
                params.put("idUser", id);
                params.put("status", status);
                System.out.println(params);
                return params;
            }
        };
        requestQueue.add(postRequest);
    }
}

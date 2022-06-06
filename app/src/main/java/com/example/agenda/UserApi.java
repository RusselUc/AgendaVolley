package com.example.agenda;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserApi {
    private RequestQueue requestQueue;
    String url = "http://192.168.0.104:4000/api/users";
    //private String url = "https://api-agenda-isc.herokuapp.com/api/users";
    private Context context;
    public static String nameUser = "";

    public UserApi(Context context){
        requestQueue = Volley.newRequestQueue(context);
        this.context = context;
    }
    public void store(String name, String pass){
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
                params.put("name", name);
                params.put("password", pass);
                System.out.println(params);
                return params;
            }
        };
        requestQueue.add(postRequest);
    }

    public void login(String name, String pass){
        StringRequest postRequest = new StringRequest(Request.Method.POST, url+"/sign",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject json = new JSONObject(response);
                            nameUser = json.getString("name");
                            irEventos(json.getString("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                params.put("name", name);
                params.put("password", pass);

                return params;
            }
        };
        requestQueue.add(postRequest);
    }

    public void irEventos(String dato){
        Intent i = new Intent(context.getApplicationContext(), EventActivity.class);
        i.putExtra("usuario", dato);
        context.getApplicationContext().startActivity(i);
    }
}

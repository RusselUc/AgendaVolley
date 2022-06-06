package com.example.agenda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.agenda.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Button iniciar;
    EditText etUser, etPass;
    TextView titulo, aqui, txt;
    ImageView imagen;

    private RequestQueue requestQueue;
    String url = "http://api-agenda-isc.herokuapp.com/api/users";
    User userSingle = null;
    String dato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        UserApi userApi = new UserApi(this);
        //userApi.singleUser("UT7CX2M3ykIGfW6PE5Lf");

        titulo = binding.textView;
        aqui = binding.aqui;
        txt = binding.textView4;

        imagen = binding.imageView;

        etUser = binding.etUser;
        etPass = binding.etPassword;

        iniciar = binding.button3;
        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(iniciar.getText().toString().equals("Iniciar")) {
                    login(etUser.getText().toString(), etPass.getText().toString());
                    //single(etUser.getText().toString(), etPass.getText().toString());
                } else {
                    //if(checkuser(etUser.getText().toString())) {
                        if(!etUser.getText().toString().isEmpty() && !etPass.getText().toString().isEmpty()) {
                            store(etUser.getText().toString(), etPass.getText().toString());
                            //userApi.create(etUser.getText().toString(), etPass.getText().toString());
                            //createUser();
                            titulo.setText("Agenda");
                            txt.setText("¿Usuario nuevo?");
                            aqui.setText("Registrate aquí");
                            iniciar.setText("Iniciar");
                            imagen.setImageResource(R.drawable.ic_undraw_events_re_98ue);
                            Toast.makeText(getApplicationContext(), "Usuario registrado", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Favor de llenar todos los campos", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            //}
        });

        aqui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(aqui.getText().toString().equals("Registrate aquí")){
                    titulo.setText("Registro");
                    txt.setText("");
                    aqui.setText("Iniciar sesión");
                    iniciar.setText("Registar");
                    imagen.setImageResource(R.drawable.ic_undraw_terms_re_6ak4);

                } else {
                    titulo.setText("Agenda");
                    txt.setText("¿Usuario nuevo?");
                    aqui.setText("Registrate aquí");
                    iniciar.setText("Iniciar");
                    imagen.setImageResource(R.drawable.ic_undraw_events_re_98ue);
                }
            }
        });
        //createUser();
    }

    public void irEventos(String data){
        Intent i = new Intent(this, EventActivity.class);
        i.putExtra("usuario", data);
        startActivity(i);
        finish();
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
                            //nameUser = json.getString("name");
                            dato = json.getString("name");
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


        try {
            Thread thread =  new Thread(new Runnable() {
                public void run(){
                    requestQueue.add(postRequest);
                }
            });
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        irEventos(dato);
    }

    public void toastCorrecto(String message){
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toas_ok, (ViewGroup) findViewById(R.id.llcustom_ok));

        TextView textView = view.findViewById(R.id.txtMessage);
        textView.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    public void toastIncorrecto(String message){
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_fail, (ViewGroup) findViewById(R.id.llcustom_fail));

        TextView textView = view.findViewById(R.id.txtMessageFail);
        textView.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }
}
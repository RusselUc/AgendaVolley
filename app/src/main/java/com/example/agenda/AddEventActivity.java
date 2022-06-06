package com.example.agenda;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.agenda.databinding.ActivityAddEventBinding;
import com.example.agenda.databinding.ActivityEventBinding;

import java.util.Locale;

public class AddEventActivity extends AppCompatActivity {

    ActivityAddEventBinding binding;
    EditText ettitulo;
    TextView tvHour, tvFecha, showHora, showFecha;
    ImageView imgHora, imgFecha;
    Spinner spinner;
    Button crear;

    User user = null;

    String [] opciones =  {"Selecciona actividad...", "Física", "Trabajo", "Compras", "Recreativo", "Otros"};

    int selectId = 0;
    int hour;
    int minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEventBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        user = (User) getIntent().getSerializableExtra("usuario");

        ettitulo = binding.etTitulo;
        tvHour = binding.hora;
        tvFecha = binding.fecha;
        showFecha = binding.showfecha;
        showHora = binding.showhora;
        imgHora = binding.imgHora;
        imgFecha = binding.imgFecha;
        spinner = binding.spinner;
        crear = binding.button;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, opciones);
        spinner.setAdapter(adapter);

        imgFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.imgFecha:
                        tvFecha.setText("");
                        showDatePickerDialog();
                        break;
                }
            }
        });

        imgHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popTimePicker();
                tvHour.setText("");
            }
        });

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ettitulo.getText().toString().isEmpty() && spinner.getSelectedItemPosition() != 0 &&
                        !showFecha.getText().toString().trim().isEmpty() &&
                        !showHora.getText().toString().trim().isEmpty()) {
                    createUser();
                    onBackPressed();
                }else {
                    Toast.makeText(getApplicationContext(), "Favor de llenar todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private  void popTimePicker(){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                hour = i;
                minute = i1;
                showHora.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, false);
        timePickerDialog.setTitle("Selecciona una hora");
        timePickerDialog.show();
    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + " / " + (month+1) + " / " + year;
                showFecha.setText(selectedDate);
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void createUser(){
        Sqlite sqLite = new Sqlite(this);
        SQLiteDatabase sqLiteDatabase = sqLite.getWritableDatabase();

        String titulo = ettitulo.getText().toString();
        String activity = spinner.getSelectedItem().toString();
        String date = showFecha.getText().toString();
        String hour = showHora.getText().toString();
        int idUser = user.getId();


        ContentValues values = new ContentValues();
        values.put("titulo", titulo);
        values.put("activity", activity);
        values.put("today", date);
        values.put("hourmin", hour);
        values.put("id_user", idUser);

        Long result = sqLiteDatabase.insert("eventos", null, values);

        toastCorrecto("Evento creado");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
}
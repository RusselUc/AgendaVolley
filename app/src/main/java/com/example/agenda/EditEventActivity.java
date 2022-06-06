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

public class EditEventActivity extends AddEventActivity {

    ActivityAddEventBinding binding;
    EditText ettitulo;
    TextView tvHour, tvFecha, showHora, showFecha, tvEdit;
    ImageView imgHora, imgFecha, imgAddEvent;
    Spinner spinner;
    Button crear;

    Events event;

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

        ettitulo = binding.etTitulo;
        tvHour = binding.hora;
        tvEdit = binding.AddEdit;
        tvFecha = binding.fecha;
        showFecha = binding.showfecha;
        showHora = binding.showhora;
        imgHora = binding.imgHora;
        imgFecha = binding.imgFecha;
        spinner = binding.spinner;
        crear = binding.button;
        imgAddEvent = binding.imgAddEvent;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, opciones);
        spinner.setAdapter(adapter);

        tvEdit.setText("Editar evento");
        imgAddEvent.setImageResource(R.drawable.documento);

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

        crear.setText("Actualizar");
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
                onBackPressed();
            }
        });

        event = (Events) getIntent().getSerializableExtra("evento");
        llenarDatos();
        System.out.println(event.getActividad());
    }

    public void llenarDatos(){
        selectId = event.getId();
        ettitulo.setText(event.getTitulo());
        for(int i = 0; i < opciones.length; i++){
            if(opciones[i].equals(event.getActividad())){
                spinner.setSelection(i);
            }
        }
        showFecha.setText(event.getFecha());
        showHora.setText(event.getHora());
        tvFecha.setText("");
        tvHour.setText("");
    }

    public void update() {
        Sqlite sqLite = new Sqlite(this);
        SQLiteDatabase sqLiteDatabase = sqLite.getWritableDatabase();

        String titulo = ettitulo.getText().toString();
        String activity = spinner.getSelectedItem().toString();
        String date = showFecha.getText().toString();
        String hour = showHora.getText().toString();

        ContentValues values = new ContentValues();
        values.put("titulo", titulo);
        values.put("activity", activity);
        values.put("today", date);
        values.put("hourmin", hour);

        sqLiteDatabase.update("eventos", values, "id="+selectId, null);
        sqLiteDatabase.close();
        toastCorrecto("Se actualizó correctamente");
    }

    private void editEvent(View view){

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
package com.example.agenda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agenda.databinding.ActivityEventBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class EventActivity extends AppCompatActivity  implements InterfaceAux, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    ActivityEventBinding binding;
    FloatingActionButton btn;
    RecyclerView recyclerView;
    TextView bienvenido;

    ArrayList<Events> eventsArrayList;
    private EventAdapter eventAdapter;
    Events event;

    User user = null;
    public static String idUserg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        user = (User) getIntent().getSerializableExtra("usuario");
        btn = binding.btn;
        bienvenido = binding.Bienvenido;
        recyclerView = binding.recycler;

        idUserg = String.valueOf(UserApi.nameUser);
        bienvenido.setText("Hola, "+UserApi.nameUser+" 😃");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irCrear();
            }
        });

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT, EventActivity.this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

        updateList(list());
    }

    public void updateList(ArrayList<Events> events){
        eventAdapter = new EventAdapter(this,this,events);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(eventAdapter);
        eventsArrayList = events;
    }

    public ArrayList<Events> list(){
        Sqlite sqLite = new Sqlite(this);
        SQLiteDatabase sqLiteDatabase = sqLite.getWritableDatabase();
        Events events = null;
        ArrayList<Events>list = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM eventos", null);
        while(cursor.moveToNext()){
            events = new Events(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            list.add(events);
        }

        return list;
    }

    public void irCrear(){
        Intent i = new Intent(this, AddEventActivity.class);
        i.putExtra("usuario", user);
        startActivity(i);
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof EventAdapter.eventView){
            eventAdapter.removeItem(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public void OptionEdit(Events events) {
        Intent i = new Intent(this, EditEventActivity.class);
        i.putExtra("evento", events);
        startActivity(i);
    }

    public void Favorito(Events events) {

    }

    protected void onResume() {
        super.onResume();
        updateList(list());
    }
}
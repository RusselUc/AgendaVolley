package com.example.agenda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.agenda.databinding.ItemCardBinding;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.eventView>  implements Filterable {
    private ArrayList<Events> eventsArrayList = new ArrayList<>();
    private List<Events> eventsList = new ArrayList<>();
    private Context context;

   private InterfaceAux iAuxUser;

    public EventAdapter(Context context,InterfaceAux iAuxUser, ArrayList<Events> eventsArrayList) {
        this.eventsList = eventsArrayList;
        this.eventsArrayList = eventsArrayList;
        this.iAuxUser = iAuxUser;
        this.context = context;
    }

   /*public void deleteUser(User user){
        userArrayList.remove(user);
        this.notifyDataSetChanged();
    }*/

    @NonNull
    @Override
    public eventView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new eventView(ItemCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(eventView holder, int position) {

        Events events = eventsArrayList.get(position);

        holder.titulo.setText(events.getTitulo());
        holder.borrar.setOnClickListener(new eventEdit(events));
        holder.event.setText(String.valueOf(events.getActividad()));
        if(events.getActividad().equals("Física")){
            holder.imgEvent.setImageResource(R.drawable.fisica);
        }else if(events.getActividad().equals("Trabajo")){
            holder.imgEvent.setImageResource(R.drawable.maleta);
        }else if(events.getActividad().equals("Compras")){
            holder.imgEvent.setImageResource(R.drawable.compras);
        }else if(events.getActividad().equals("Recreativo")){
            holder.imgEvent.setImageResource(R.drawable.rubik);
        }else if(events.getActividad().equals("Otros")){
            holder.imgEvent.setImageResource(R.drawable.otro);
        }

        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!holder.flag){
                    holder.favorite.setImageResource(R.drawable.ic_baseline_favorite_24);
                    holder.flag = true;
                    Sqlite sqLite = new Sqlite(context.getApplicationContext());
                    SQLiteDatabase sqLiteDatabase = sqLite.getWritableDatabase();

                    int idEvent = events.getId();
                    int idUser = Integer.parseInt(EventActivity.idUserg);

                    ContentValues values = new ContentValues();
                    values.put("id_user", idUser);
                    values.put("id_eventos", idEvent);

                    Long result = sqLiteDatabase.insert("favoritos", null, values);
                    Toast.makeText(context.getApplicationContext(), "Añadido a favoritos", Toast.LENGTH_SHORT).show();
                }else{
                    holder.favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    holder.flag = false;

                    Sqlite sqLite = new Sqlite(context.getApplicationContext());
                    SQLiteDatabase sqLiteDatabase = sqLite.getWritableDatabase();

                    String id = EventActivity.idUserg;
                    if(!id.isEmpty()){
                        sqLiteDatabase.delete("favoritos", "id_user="+id, null);
                        Toast.makeText(context.getApplicationContext(), "Se eliminó de favoritos", Toast.LENGTH_SHORT).show();
                        //EventAdapter.deleteEvents(events);
                        sqLiteDatabase.close();
                    } else {
                        //Toast.makeText(context.getApplicationContext(), "¡Ups!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Sqlite sqLite = new Sqlite(context.getApplicationContext());
        SQLiteDatabase sqLiteDatabase = sqLite.getWritableDatabase();

        Cursor cursor = null;
        String id_event = String.valueOf(events.getId());
        System.out.println("idUsuario "+EventActivity.idUserg);
        System.out.println("idEvento "+id_event);
        String [] args = new String[]{EventActivity.idUserg, id_event};

        cursor = sqLiteDatabase.rawQuery("SELECT * from favoritos WHERE id_user = ? AND id_eventos = ?" , new String[]{args[0], args[1]});

        if(cursor.moveToFirst()){
            do {
                holder.favorite.setImageResource(R.drawable.ic_baseline_favorite_24);
                holder.flag = true;
                //Toast.makeText(context.getApplicationContext(), "favoritos", Toast.LENGTH_SHORT).show();
                System.out.println(cursor.getInt(2));
            }while(cursor.moveToNext());
        } else {
            holder.favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            holder.flag = false;
            //Toast.makeText(context.getApplicationContext(), "no hay favoritos", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        //holder.imgEvent.setImageResource(R.drawable.ic_baseline_favorite_24);
    }

    @Override
    public int getItemCount() {
        return eventsArrayList.size();
    }

    public void addUser(Events events){
        eventsArrayList.add(events);
        this.notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                return null;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            }
        };
    }

    /*class eventEdit implements View.OnClickListener {

        private User user;
        public eventEdit(User user){
            this.user = user;
        }

        @Override
        public void onClick(View view) {
            iAuxUser.OptionEdit(user);
        }
    }*/

    /*class eventDelete implements  View.OnClickListener {
        private User user;
        public eventDelete(User user){
            this.user = user;
        }

        @Override
        public void onClick(View view) {
            iAuxUser.OptionDelete(user);
        }
    } */

    class eventEdit implements View.OnClickListener {

        private Events events;
        public eventEdit(Events events){
            this.events = events;
        }

        @Override
        public void onClick(View view) {
            iAuxUser.OptionEdit(events);
        }
    }

    public void removeItem(int position){
        delete(eventsArrayList.get(position).getId());
        eventsArrayList.remove(position);
        notifyItemRemoved(position);
    }

    public void delete(int idEvent){
        Sqlite sqLite = new Sqlite(this.context);
        SQLiteDatabase sqLiteDatabase = sqLite.getWritableDatabase();

        String id = String.valueOf(idEvent);
        if(!id.isEmpty()){
            sqLiteDatabase.delete("eventos", "id="+id, null);
            Toast.makeText(this.context, "Se eliminó correctamente", Toast.LENGTH_SHORT).show();
            //EventAdapter.deleteEvents(events);
            sqLiteDatabase.close();
        } else {
            Toast.makeText(this.context, "¡Ups!", Toast.LENGTH_SHORT).show();
        }
    }


    public class eventView extends RecyclerView.ViewHolder {

        ItemCardBinding binding;

        public RelativeLayout borrar;

        TextView event, titulo;
        ImageView favorite;
        ImageView imgEvent;

        boolean flag = false;

        public eventView(ItemCardBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            borrar = binding.layoutBorrar;
            event = binding.event;
            titulo = binding.titulo;
            favorite = binding.favorite;
            imgEvent = binding.imgEvent;

        }
    }
}

package com.example.dotbit.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dotbit.Models.Notes;
import com.example.dotbit.NotesClickListner;
import com.example.dotbit.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotesListAdapters extends RecyclerView.Adapter<NotesViewHolder>{


    Context context;
    List<Notes> list;

    NotesClickListner listener;

    public NotesListAdapters(Context context, List<Notes> list, NotesClickListner listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_list,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.textview_title.setText(list.get(position).getTitle());
        holder.textview_title.setSelected(true);

        holder.textview_note.setText(list.get(position).getNotes());

        holder.textview_date.setText((list.get(position).getDate()));
        holder.textview_date.setSelected(true);

        if(list.get(position).isPinned()){
            holder.imageview_pin.setImageResource(R.drawable.ic_pin);
        }
        else {
            if(holder.imageview_pin != null) {
                // Now you can safely set the image resource
                holder.imageview_pin.setImageResource(0);
            }
        }

        int color_code = getRandomColor();
        holder.notes_container.setCardBackgroundColor(holder.itemView.getResources().getColor(color_code,null));


        holder.notes_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(list.get(holder.getAdapterPosition()));
            }
        });

        holder.notes_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onLongCLick(list.get(holder.getAdapterPosition()),holder.notes_container);
                return true;
            }
        });
    }

    private int getRandomColor(){
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.aqua);
        colorCode.add(R.color.alice_blue);
        colorCode.add(R.color.bisque);
        colorCode.add(R.color.cornsilk);
        colorCode.add(R.color.dark_cyan);
        colorCode.add(R.color.medium_aquamarine);
        colorCode.add(R.color.forest_green);

        Random random = new Random();
        int random_color = random.nextInt(colorCode.size());

        return colorCode.get(random_color);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(List<Notes> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }
}



class NotesViewHolder extends RecyclerView.ViewHolder{


    CardView notes_container;
    TextView textview_title,textview_note,textview_date;
    ImageView imageview_pin;
    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);

        notes_container = itemView.findViewById(R.id.notes_container);
        textview_title = itemView.findViewById(R.id.textview_title);
        textview_date = itemView.findViewById(R.id.textview_date);
        textview_note = itemView.findViewById(R.id.textview_note);
    }
}


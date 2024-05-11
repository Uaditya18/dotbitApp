package com.example.dotbit;

import androidx.cardview.widget.CardView;

import com.example.dotbit.Models.Notes;

public interface NotesClickListner {
    void onClick(Notes note);

    void onLongCLick(Notes notes, CardView cardView);
}

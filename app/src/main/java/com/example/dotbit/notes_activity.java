package com.example.dotbit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.dotbit.Adapters.NotesListAdapters;
import com.example.dotbit.Database.RoomDb;
import com.example.dotbit.Models.Notes;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class notes_activity extends AppCompatActivity {
    RecyclerView recyclerView;
    NotesListAdapters notesListAdapters;
    List<Notes> notes = new ArrayList<>();
    RoomDb database;
    FloatingActionButton fab_add;
    BottomNavigationView bottomNavigationView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.note);
        bottomNavigationView.setOnItemSelectedListener( menuItem -> {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.main) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            } else if (itemId == R.id.content) {
                startActivity(new Intent(getApplicationContext(), content_activity.class));
                finish();

            } else if (itemId == R.id.note) {
                return true;
            } else if (itemId == R.id.chatbot) {
                startActivity(new Intent(getApplicationContext(), chat_acitivity.class));
                finish();
            } else if (itemId == R.id.profile) {
                startActivity(new Intent(getApplicationContext(), profile_activity.class));
                finish();
            }
            else {
                // Handle other menu items if needed
                return false;
            }
            return true;

        });


        recyclerView = findViewById(R.id.recycler_home);
        fab_add = findViewById(R.id.fab_add);

        database = RoomDb.getInstance(this);

        notes = database.mainDAO().getAll();
        updateRecycler(notes);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(notes_activity.this,NotesTakerActivity.class);
                startActivityForResult(intent,101);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101){
            if(requestCode == Activity.RESULT_OK) {
                Notes new_notes = (Notes) data.getSerializableExtra("note");
                database.mainDAO().insert(new_notes);
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                notesListAdapters.notifyDataSetChanged();
            }
        }
    }

    private void updateRecycler(List<Notes> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        notesListAdapters = new NotesListAdapters(notes_activity.this,notes,notesClickListner);
        recyclerView.setAdapter(notesListAdapters);
    }
    private final NotesClickListner notesClickListner = new NotesClickListner() {
        @Override
        public void onClick(Notes note) {

        }

        @Override
        public void onLongCLick(Notes notes, CardView cardView) {

        }
    };
}
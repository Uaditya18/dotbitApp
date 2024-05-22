package com.example.dotbit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
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

public class notes_activity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    RecyclerView recyclerView;
    NotesListAdapters notesListAdapters;
    List<Notes> notes = new ArrayList<>();
    RoomDb database;
    FloatingActionButton fab_add;
    BottomNavigationView bottomNavigationView;

    SearchView searchview_home;

    Notes selectedNote;
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



        searchview_home = findViewById(R.id.searchview_home);
        recyclerView = findViewById(R.id.recycler_home);
        fab_add = findViewById(R.id.fab_add);

        database = RoomDb.getInstance(this);

        notes = database.mainDAO().getAll();
        updateRecycler(notes);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(notes_activity.this,NotesTakerActivity.class);
                startActivityForResult(intent, 101);
            }


        });

        searchview_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String newText) {
        List<Notes> filteredList = new ArrayList<>();
        for (Notes singleNote: notes){
            if(singleNote.getTitle().toLowerCase().contains(newText.toLowerCase())
            || singleNote.getNotes().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(singleNote);
            }

        }
        notesListAdapters.filterList(filteredList);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==101){
            if(resultCode== Activity.RESULT_OK){

                Notes new_notes = (Notes) data.getSerializableExtra("notes");
                database.mainDAO().insert(new_notes);
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                notesListAdapters.notifyDataSetChanged();
            }
        } else if (requestCode==102) {
            if (resultCode==Activity.RESULT_OK){
                Notes new_notes = (Notes) data.getSerializableExtra("notes");
                database.mainDAO().update(new_notes.getId(),new_notes.getTitle(), new_notes.getNotes());
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
            Intent intent = new Intent(notes_activity.this,NotesTakerActivity.class);
            intent.putExtra("old_note",note);
            startActivityForResult(intent,102);

        }

        @Override
        public void onLongCLick(Notes notes, CardView cardView) {
        selectedNote = new Notes();
        selectedNote = notes;
        showPopup(cardView);
        }
    };

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this,cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.pin) {
            if (selectedNote.isPinned()) {
                database.mainDAO().pin(selectedNote.getId(), false);
                Toast.makeText(this, "unpinned", Toast.LENGTH_SHORT).show();
            } else {
                database.mainDAO().pin(selectedNote.getId(), true);
                Toast.makeText(this, "pinned", Toast.LENGTH_SHORT).show();
            }

            notes.clear();
            notes.addAll(database.mainDAO().getAll());
            notesListAdapters.notifyDataSetChanged();
            return true;
        }

        if(itemId == R.id.delete){
            database.mainDAO().delete(selectedNote);
            notes.remove(selectedNote);
            notesListAdapters.notifyDataSetChanged();
            Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }
}
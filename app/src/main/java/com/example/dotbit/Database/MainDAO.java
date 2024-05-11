package com.example.dotbit.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;
import com.example.dotbit.Models.Notes;

import java.util.List;

@Dao
public interface MainDAO {

    @Insert(onConflict = REPLACE)
    void insert(Notes notes);
    @Query("select * from notes order by ID desc")
    List<Notes> getAll();

    @Query("UPDATE notes SET title = :title, notes= :notes WHERE ID =:id")
    void update(int id, String title,String notes);

    @Delete
    void delete(Notes notes);
}

package com.example.dotbit.Database;
import static androidx.room.OnConflictStrategy.REPLACE;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.dotbit.Models.Notes;








@Dao
public interface MainDAO {

    @Insert(onConflict = REPLACE)
    void insert(Notes notes);
    @Query("SELECT * FROM notes ORDER BY ID DESC")
    List<Notes> getAll();

    @Query("update notes set title = :title, notes= :notes WHERE id =:id")
    void update(int id,String title,String notes);

    @Delete
    void delete(Notes notes);
    @Query("UPDATE notes SET pinned = :pin WHERE ID=:id")
    void pin(int id,boolean pin);
}

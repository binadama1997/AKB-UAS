package com.rex1997.akb_uas.ui.notes;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface NoteDAO {
    @Update
    void update(Note note);
    @Query("delete from note where noteId=:id")
    void deleteNote(int id);
    @Delete
    void delete(Note note);
    @Insert
    void add(Note note);
    @Query("Select * from Note")
    List<Note> getAllNotes();
    @Query("Select * from Note where noteId=:id")
    Note getNote(int id);
}

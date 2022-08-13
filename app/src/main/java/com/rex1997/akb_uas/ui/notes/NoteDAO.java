package com.rex1997.akb_uas.ui.notes;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/*
Created at 12/08/2022
Created by Bina Damareksa (NIM: 10121702; Class: AKB-7)
*/

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

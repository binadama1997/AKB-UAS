package com.rex1997.akb_uas.ui.notes;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/*
Created at 12/08/2022
Created by Bina Damareksa (NIM: 10121702; Class: AKB-7)
*/

@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class NoteAppDatabase extends RoomDatabase
{
    public abstract NoteDAO noteDao();
    private static NoteAppDatabase INSTANCE;
    public static NoteAppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    NoteAppDatabase.class, "notes_db").build();
        }
        return INSTANCE;
    }
}

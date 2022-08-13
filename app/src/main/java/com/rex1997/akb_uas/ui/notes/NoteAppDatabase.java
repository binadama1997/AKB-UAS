package com.rex1997.akb_uas.ui.notes;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

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

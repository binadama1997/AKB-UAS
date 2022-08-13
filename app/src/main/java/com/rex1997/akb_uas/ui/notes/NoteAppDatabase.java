package com.rex1997.akb_uas.ui.notes;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Note.class}, version = 1)
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
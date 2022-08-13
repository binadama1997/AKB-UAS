package com.rex1997.akb_uas.ui.notes;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rex1997.akb_uas.R;

import java.util.List;

/*
Created at 12/08/2022
Created by Bina Damareksa (NIM: 10121702; Class: AKB-7)
*/

public class NotesFragment extends Fragment {

    private ImageButton addBtn;
    private RecyclerView notes;
    private NoteAdapter noteAdapter;
    private List<Note> noteList;
    public static NoteAppDatabase noteAppDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_notes, container, false);
        addBtn = rootView.findViewById(R.id.addNotesBtn);
        notes= rootView.findViewById(R.id.rcvNotes);

        addControls();
        addEvent();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        class GetAllNotes extends AsyncTask<Void, Void, Void>
        {
            @Override
            protected Void doInBackground(Void... voids) {
                noteList = noteAppDatabase.noteDao().getAllNotes();
                return null;
            }

            protected void onPostExecute(Void aVoid) {
                noteAdapter = new NoteAdapter(getContext(), noteList);
                notes.setAdapter(noteAdapter);
            }
        }
        GetAllNotes getAllNotes = new GetAllNotes();
        getAllNotes.execute();
    }

    private void addControls() {
        noteAppDatabase = NoteAppDatabase.getAppDatabase(getContext());
        notes.setLayoutManager(new GridLayoutManager(getContext(), 1));

        class GetAllNotes extends AsyncTask<Void, Void, Void>
        {

            @Override
            protected Void doInBackground(Void... voids) {
                noteList=noteAppDatabase.noteDao().getAllNotes();
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                noteAdapter = new NoteAdapter(getContext(), noteList);
                notes.setAdapter(noteAdapter);
            }
        }

        GetAllNotes getAllNotes = new GetAllNotes();
        getAllNotes.execute();

    }

    private void addEvent()
    {
        addBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), NewNoteFragment.class);
            intent.putExtra("action","create");
            startActivity(intent);
        });

    }
}
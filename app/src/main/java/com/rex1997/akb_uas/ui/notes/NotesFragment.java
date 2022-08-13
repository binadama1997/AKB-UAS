package com.rex1997.akb_uas.ui.notes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rex1997.akb_uas.R;

import java.util.List;

public class NotesFragment extends Fragment {

    private ImageButton addBtn;
    private RecyclerView notes;
    private NoteAdapter noteAdapter;
    private List<Note> noteList;
    public static NoteAppDatabase noteAppDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        addBtn = rootView.findViewById(R.id.addNotesBtn);
        notes= rootView.findViewById(R.id.rcvNotes);

        checkPermission();
        addControls();
        addEvent();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onResume();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    100);
        }
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
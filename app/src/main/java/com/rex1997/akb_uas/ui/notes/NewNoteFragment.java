package com.rex1997.akb_uas.ui.notes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rex1997.akb_uas.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewNoteFragment extends Fragment {

    private ImageButton saveBtn;
    private EditText titleText, contentText;
    private Note note;
    private String action;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_newnotes, container, false);

        saveBtn = rootView.findViewById(R.id.saveBtn);
        titleText = rootView.findViewById(R.id.titleText);
        contentText = rootView.findViewById(R.id.contentText);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        blindSaveButton();

        Intent intent = requireActivity().getIntent();
        action=intent.getStringExtra("action");
        if(!action.equalsIgnoreCase("create"))
        {
            addControls();
        }
        addEvents();
    }

    private void addControls() {
        Intent intent = requireActivity().getIntent();
        int id = intent.getIntExtra("id", -1);
        class GetNote extends AsyncTask<Void, Void, Void>
        {

            @Override
            protected Void doInBackground(Void... voids) {
                note = NotesFragment.noteAppDatabase.noteDao().getNote(id);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                titleText.setText(note.getNoteTitle());
                contentText.setText(note.getNoteContent());
            }
        }
        GetNote getNote = new GetNote();
        getNote.execute();
    }

    private void addEvents() {

        saveBtn.setOnClickListener(v -> {
            if(action.equalsIgnoreCase("create"))
            {
                processSaveNote(v);
            }
            else
            {
                processUpdateNote(v);
            }
        });

        titleText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showSaveButton();
            } else {
                blindSaveButton();
            }
        });

        contentText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showSaveButton();
            } else {
                blindSaveButton();
            }
        });
    }

    private void blindSaveButton() {
        saveBtn.setVisibility(View.INVISIBLE);
    }

    private void showSaveButton() {
        saveBtn.setVisibility(View.VISIBLE);
    }

    private void processUpdateNote(View v) {
        blindSaveButton();
        hideKeyboard(v);

        if (contentText.getText().toString().length() <= 0) {
            return;
        }

        String title = titleText.getText().toString();
        String content = contentText.getText().toString();
        note.setNoteTitle(title);
        note.setNoteContent(content);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        note.setModificationDate(formatter.format(date));

        class UpdateNote extends AsyncTask<Void, Void, Void>
        {

            @Override
            protected Void doInBackground(Void... voids) {
                NoteAppDatabase.getAppDatabase(requireActivity().getApplicationContext()).noteDao().update(note);
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(requireActivity().getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
                requireActivity().finish();
            }
        }
        UpdateNote updateNote = new UpdateNote();
        updateNote.execute();
    }


    private void processSaveNote(View v) {
        blindSaveButton();
        hideKeyboard(v);

        if (contentText.getText().toString().length() <= 0) {
            return;
        }

        String title = titleText.getText().toString();
        String content = contentText.getText().toString();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        Note note = new Note(0,title, content, formatter.format(date),"");
        class SaveNote extends AsyncTask<Void, Void, Void>
        {

            @Override
            protected Void doInBackground(Void... voids) {
                NoteAppDatabase.getAppDatabase(requireActivity().getApplicationContext()).noteDao().add(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(requireActivity().getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
                requireActivity().finish();
            }
        }
        SaveNote saveNote = new SaveNote();
        saveNote.execute();
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

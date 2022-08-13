package com.rex1997.akb_uas.ui.notes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rex1997.akb_uas.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
Created at 12/08/2022
Created by Bina Damareksa (NIM: 10121702; Class: AKB-7)
*/

public class NewNoteFragment extends AppCompatActivity {

    private ImageButton saveBtn;
    private EditText titleText, contentText;
    private Note note;
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_newnotes);

        saveBtn = findViewById(R.id.saveBtn);

        titleText = findViewById(R.id.titleText);
        contentText = findViewById(R.id.contentText);

        blindSaveButton();

        Intent intent=getIntent();
        action=intent.getStringExtra("action");
        if("create".equalsIgnoreCase(action))
        {
            addControls();
        }
        addEvents();
    }

    private void addControls() {
        Intent intent = getIntent();
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
            if(!"create".equalsIgnoreCase(action))
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
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        note.setModificationDate(formatter.format(date));

        class UpdateNote extends AsyncTask<Void, Void, Void>
        {

            @Override
            protected Void doInBackground(Void... voids) {
                NoteAppDatabase.getAppDatabase(getApplicationContext()).noteDao().update(note);
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
                finish();
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
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        Note note = new Note(0,title, content, formatter.format(date),"");
        class SaveNote extends AsyncTask<Void, Void, Void>
        {

            @Override
            protected Void doInBackground(Void... voids) {
                NoteAppDatabase.getAppDatabase(getApplicationContext()).noteDao().add(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
                finish();
            }
        }
        SaveNote saveNote = new SaveNote();
        saveNote.execute();
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

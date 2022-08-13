package com.rex1997.akb_uas.ui.notes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.rex1997.akb_uas.R;

import java.util.List;
import java.util.Random;

/*
Created at 12/08/2022
Created by Bina Damareksa (NIM: 10121702; Class: AKB-7)
*/

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private final List<Note> noteList;
    private final Context context;
    public static String[] colorList = {"#DC3545", "#28A745", "#007BFF", "#17A2B8", "#FD7E14", "#6F42C1"};

    public NoteAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.note_item, parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = noteList.get(position);

        holder.titleText.setText(note.getNoteTitle());
        holder.contentText.setText(note.getNoteContent());
        if(!note.getModificationDate().equalsIgnoreCase(""))
            holder.dateText.setText(note.getModificationDate());
        else
            holder.dateText.setText(note.getCreationDate());
        holder.color.setBackgroundColor(Color.parseColor(colorList[new Random().nextInt(colorList.length)]));

        holder.setItemClickListener((view, position1, isLongClick) -> {
            if (isLongClick) {
                processDeleteItem(position1);
            } else {
                processClickItem(position1);
            }
        });

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    private void processClickItem(int position) {
        Note note = noteList.get(position);

        Intent intent = new Intent(context, NewNoteFragment.class);
        intent.putExtra("id", note.getNoteId());
        intent.putExtra("action","Edit");

        context.startActivity(intent);
    }

    private void processDeleteItem(int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Deleting Note");
        alert.setMessage("Do you want to delete this note?");
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                class DeleteNote extends AsyncTask<Void, Void, Void>
                {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        NotesFragment.noteAppDatabase.noteDao().deleteNote(noteList.get(position).getNoteId());
                        return null;
                    }
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    protected void onPostExecute(Void unused) {
                        super.onPostExecute(unused);
                        noteList.remove(position);
                        notifyDataSetChanged();
                    }
                }
                DeleteNote deleteNote = new DeleteNote();
                deleteNote.execute();
            }
        });
        alert.setNegativeButton(android.R.string.no, (dialog, which) -> dialog.cancel());
        alert.show();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private final TextView titleText;
        private final TextView contentText;
        private final TextView dateText;
        private final View color;
        private ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleText = itemView.findViewById(R.id.titleText);
            contentText = itemView.findViewById(R.id.contentText);
            dateText = itemView.findViewById(R.id.dateText);
            color = itemView.findViewById(R.id.color);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(v, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onItemClick(v, getAdapterPosition(), true);
            return true;
        }

    }

}

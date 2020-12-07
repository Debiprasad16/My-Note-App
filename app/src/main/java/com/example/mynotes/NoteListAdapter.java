package com.example.mynotes;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import model.Items;
import model.Note;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteListHolder> {

    private Context context;
    private ArrayList<Note> notes;

    private NoteListClickListener listener;

    public NoteListAdapter(Context context, ArrayList<Note> notes){
        this.notes = notes;
        this.context = context;
    }

    public void setListener(NoteListClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteListHolder(LayoutInflater.from(context).inflate(R.layout.cell_note, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteListHolder holder, int position) {
        final Note item = notes.get(position);
        holder.dTvNoteTitle.setText(item.noteTitle);
        ArrayList<Items> itemsList = Items.covertJSONArrayStringToArrayList(item.noteItems);

        holder.dIvEditOldNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onEditNoteClicked(item);
                }
            }
        });

        holder.dIvDeleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null) {
                    listener.onDeleteNoteClicked(item);
                }
            }
        });

        holder.dLlNoteItems.removeAllViews();

        for(final Items value : itemsList){
            View view = LayoutInflater.from(context).inflate(R.layout.cell_items_view, null);
            TextView dTvItemName = view.findViewById(R.id.tv_view_items);
            CheckBox dChItem = view.findViewById(R.id.ch_view_items);

            dTvItemName.setText(value.itemName);
            dChItem.setChecked(value.isChecked);

            if(value.isChecked){
                dTvItemName.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }

            dChItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(listener != null){
                        listener.onItemUpdateClicked(value, item, b);
                    }
                }
            });

            holder.dLlNoteItems.addView(view);
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NoteListHolder extends RecyclerView.ViewHolder{

        private TextView dTvNoteTitle;
        private LinearLayout dLlNoteItems;
        private ImageView dIvEditOldNote;
        private ImageView dIvDeleteNote;

        public NoteListHolder(@NonNull View itemView) {
            super(itemView);

            dTvNoteTitle = itemView.findViewById(R.id.tv_note_title);
            dLlNoteItems = itemView.findViewById(R.id.ll_view_items);
            dIvEditOldNote = itemView.findViewById(R.id.iv_edit_old_note);
            dIvDeleteNote = itemView.findViewById(R.id.iv_delete_note);
        }
    }

    public interface NoteListClickListener{
        void onItemUpdateClicked(Items updatedItem, Note note, boolean checkedValue);
        void onEditNoteClicked(Note note);
        void onDeleteNoteClicked(Note note);
    }
}

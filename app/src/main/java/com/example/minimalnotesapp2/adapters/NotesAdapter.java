package com.example.minimalnotesapp2.adapters;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.minimalnotesapp2.R;
import com.example.minimalnotesapp2.entities.Note;
import com.example.minimalnotesapp2.listeners.NotesListener;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder>{

    private List<Note> notes;
    private NotesListener notesListener;
    private Timer timer;
    private List<Note> noteSource;

    public NotesAdapter(List<Note> notes, NotesListener notesListener){
        this.notes = notes;
        this.notesListener = notesListener;
        noteSource = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_note,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, final int position) {
        holder.setNote(notes.get(position));

        holder.textTitle.setTransitionName("noteTitle");
        holder.textSubtitle.setTransitionName("noteSubtitle");
        holder.textDateTime.setTransitionName("noteDateTime");

        /*Pair<View, String> pair1 = Pair.create((View) holder.textTitle, holder.textTitle.getTransitionName());
        Pair<View, String> pair2 = Pair.create((View) holder.textSubtitle, holder.textSubtitle.getTransitionName());
        Pair<View, String> pair3 = Pair.create((View) holder.textDateTime, holder.textDateTime.getTransitionName());*/

        final Pair[] pairs= new Pair[3];
        pairs[0] = new Pair<View,String> (holder.textTitle,holder.textTitle.getTransitionName());
        pairs[1] = new Pair<View,String> (holder.textSubtitle,holder.textSubtitle.getTransitionName());
        pairs[2] = new Pair<View,String> (holder.textDateTime,holder.textDateTime.getTransitionName());

        holder.layoutNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesListener.onNoteClicked(notes.get(position),position,v,pairs);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView textTitle,textSubtitle,textDateTime;
        LinearLayout layoutNote;
        RoundedImageView imageNote;

        NoteViewHolder(@NonNull View itemView){
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textSubtitle = itemView.findViewById(R.id.textSubtitle);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            layoutNote = itemView.findViewById(R.id.layoutNote);
            imageNote = itemView.findViewById(R.id.imageNote);
        }

        void setNote(Note note){
            textTitle.setText(note.getTitle());
            if(note.getSubtitle().trim().isEmpty()){
                textSubtitle.setVisibility(View.GONE);
            }else {
                textSubtitle.setText(note.getSubtitle());
            }
            textDateTime.setText(note.getDateTime());

            GradientDrawable gradientDrawable = (GradientDrawable) layoutNote.getBackground();
            if (note.getColor() != null ){
                gradientDrawable.setColor(Color.parseColor(note.getColor()));
            }else{
                gradientDrawable.setColor(Color.parseColor("#333333"));
            }

            if (note.getImagePath() != null){
                imageNote.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));
                imageNote.setVisibility(View.VISIBLE);
            }else{
                imageNote.setVisibility(View.GONE);
            }
        }
    }

    public void searchNotes(final String searchKeyword){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(searchKeyword.trim().isEmpty()){
                    notes = noteSource;
                }else{
                    ArrayList<Note> temp = new ArrayList<>();
                    for (Note note : noteSource){
                        if(note.getTitle().toLowerCase().contains(searchKeyword.toLowerCase())
                        || note.getSubtitle().toLowerCase().contains(searchKeyword.toLowerCase())
                        || note.getNoteText().toLowerCase().contains(searchKeyword.toLowerCase())){
                            temp.add(note);
                        }
                    }
                    notes = temp;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        },500);
    }

    public void cancelTimer(){
        if (timer != null){
            timer.cancel();
        }
    }
}

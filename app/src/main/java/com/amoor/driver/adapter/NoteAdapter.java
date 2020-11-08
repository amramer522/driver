package com.amoor.driver.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amoor.driver.R;
import com.amoor.driver.data.note.Note;


import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.Hold> {
    private Context context;
    private List<Note> noteList= new ArrayList<>();

    public NoteAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Hold onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new Hold(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull Hold hold, int i)
    {

        hold.student_name.setText(hold.student_name.getText()+"  "+noteList.get(i).getName());
        hold.station_name.setText(hold.station_name.getText()+"  "+noteList.get(i).getLine());
        hold.student_note.setText(noteList.get(i).getNote());


    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public void setData(List<Note> noteList)
    {
        this.noteList = noteList;
    }

    class Hold extends RecyclerView.ViewHolder
     {
         TextView student_name,station_name,student_note;
         Hold(@NonNull View itemView)
         {
            super(itemView);
            station_name = itemView.findViewById(R.id.item_note_tv_station_name);
            student_name = itemView.findViewById(R.id.item_note_tv_student_name);
            student_note = itemView.findViewById(R.id.item_driver_bus_request);
        }
    }
}

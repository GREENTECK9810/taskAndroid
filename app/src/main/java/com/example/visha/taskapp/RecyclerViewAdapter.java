package com.example.visha.taskapp;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.visha.taskapp.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecylerViewAdapter";

    ArrayList<Tasks> tasks = new ArrayList<>();
    Context context;
    onItemClickListener mListener;

    public RecyclerViewAdapter(ArrayList<Tasks> tasks, Context context) {
        this.tasks = tasks;
        this.context = context;
    }

    public interface onItemClickListener {
        void onGarbageClick(int position);
        void onClickCheckbox(int position, Boolean completed);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tasklistitem, parent, false);
        ViewHolder holder = new ViewHolder(view, mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(!tasks.get(position).getCompleted()){
            holder.task.setText(tasks.get(position).getDescription());

        }else{
            holder.task.setText(tasks.get(position).getDescription());
            holder.task.setPaintFlags(holder.task.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.checkBox.setChecked(true);
        }


    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;
        TextView task, time;
        ImageView garbage;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView, onItemClickListener listener) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.checkbox);
            task = itemView.findViewById(R.id.task);
            time = itemView.findViewById(R.id.time);
            garbage = itemView.findViewById(R.id.garbage);
            parentLayout = itemView.findViewById(R.id.parent_layout);

            garbage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onGarbageClick(position);
                        }
                    }
                }
            });

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!checkBox.isChecked()){
                        task.setPaintFlags(task.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

                        if(listener != null){
                            int position = getAdapterPosition();
                            if(position != RecyclerView.NO_POSITION){
                                listener.onClickCheckbox(position, false);
                            }
                        }
                    }else {
                        task.setPaintFlags(task.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                        if(listener != null){
                            int position = getAdapterPosition();
                            if(position != RecyclerView.NO_POSITION){
                                listener.onClickCheckbox(position, true);
                            }
                        }
                    }

                }
            });

        }
    }
}

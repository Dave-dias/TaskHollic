package com.example.taskhollic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    TaskHandler taskHandler;
    ArrayList<TaskClass> taskList;

    MyAdapter (TaskHandler taskHandler, ArrayList<TaskClass> taskList){
        this.taskHandler = taskHandler;
        this.taskList = taskHandler.getTaskList();
    }

    public void refreshTaskList(){
        this.taskList = taskHandler.getTaskList();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvName, tvPriority;
        private final ImageView ivTaskDot;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPriority = itemView.findViewById(R.id.tvPriority);
            tvName = itemView.findViewById(R.id.tvName);
            ivTaskDot = itemView.findViewById(R.id.ivTaskDot);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taskHandler.displayTask(getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    taskHandler.deleteTask(taskList.get(getAdapterPosition()).getId());
                    return true;
                }
            });
        }

        public TextView getTvName() {
            return tvName;
        }

        public TextView getTvPriority() {
            return tvPriority;
        }

        public ImageView getIvTaskDot() {
            return ivTaskDot;
        }
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
        if(taskList.get(position).getImportant()){
            holder.getTvPriority().setText("Important task");
            holder.getIvTaskDot().setImageResource(R.drawable.important_task_dot);
        } else {
            holder.getTvPriority().setText("Commum task");
            holder.getIvTaskDot().setImageResource(R.drawable.commum_task_dot);
        }
        holder.getTvName().setText(taskList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return taskHandler.getRowCount();
    }
}

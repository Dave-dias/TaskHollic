package com.example.taskhollic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements TouchHelperInterface {
    TaskHandler taskHandler;
    ArrayList<TaskClass> taskList;

    MyAdapter (TaskHandler taskHandler, ArrayList<TaskClass> taskList){
        this.taskHandler = taskHandler;
        this.taskList = taskList;
    }

    public void refreshTaskList(){
        MainActivity.taskList = taskHandler.getTaskList();
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

            itemView.setOnClickListener(v -> taskHandler.displayTask(getAdapterPosition()));
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card_layout, parent, false);
        return new ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
        if(taskList.get(position).getImportant()){
            holder.getTvPriority().setText(R.string.important_task);
            holder.getIvTaskDot().setImageResource(R.drawable.important_task_dot);
        } else {
            holder.getTvPriority().setText(R.string.common_task);
            holder.getIvTaskDot().setImageResource(R.drawable.commum_task_dot);
        }
        holder.getTvName().setText(taskList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return taskHandler.getRowCount();
    }

    @Override
    public void movedTask(int oldPosition, int newPosition) {
        taskHandler.swapTasks(taskList.get(newPosition), taskList.get(oldPosition));

        Collections.swap(MainActivity.taskList, oldPosition, newPosition);
        Collections.swap(taskList, oldPosition, newPosition);

        notifyItemMoved(oldPosition, newPosition);
    }

    @Override
    public void swipedTask(int Position) {
        taskHandler.deleteTask(taskList.get(Position).getId());
    }

}

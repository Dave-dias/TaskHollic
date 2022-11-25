package com.example.taskhollic;

import java.util.ArrayList;

public interface TaskHandler {
    ArrayList<TaskClass> getTaskList();
    void swapTasks(TaskClass target, TaskClass moved);
    void updateTask(TaskClass task);
    void displayTask(int index);
    void deleteTask(int id);
    int getRowCount();
}

package com.example.taskhollic;

import java.util.ArrayList;

public interface TaskHandler {
    public ArrayList<TaskClass> getTaskList();
    public void updateTask (TaskClass task);
    void displayTask(int index);
    void deleteTask(TaskClass task);

}

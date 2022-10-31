package com.example.taskhollic;

import android.app.Application;

import java.io.File;
import java.util.ArrayList;

// Classe para guardar e persistir as tarefas no aplicativo
public class ApplicationClass extends Application {
    static ArrayList<TaskClass> TaskList;
    static int lastIndex;

    @Override
    public void onCreate() {
        super.onCreate();

        TaskList = new ArrayList<com.example.taskhollic.TaskClass>();

        fillData();
    }

    void fillData (){
        TaskList.add(new TaskClass("Do the laundry",true));
        TaskList.add(new TaskClass("Do my homework", "Page 32 to 37 of the history book", false));
        TaskList.add(new TaskClass("Go to the gym",true));
        TaskList.add(new TaskClass("Walk the dog",false));

    }
}

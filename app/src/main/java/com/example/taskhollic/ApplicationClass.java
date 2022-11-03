package com.example.taskhollic;

import android.app.Application;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

// Classe para guardar e persistir as tarefas no aplicativo
public class ApplicationClass extends Application {
    static ArrayList<TaskClass> taskList;
    static int lastIndex;

    @Override
    public void onCreate() {
        super.onCreate();

        taskList = new ArrayList<TaskClass>();
        taskList = retrieveData();
    }

    // Resgata os dados do arquivo e atribui eles para o array principal
    ArrayList<TaskClass> retrieveData (){
        ArrayList<TaskClass> retrievedArray = new ArrayList<>();
        File file = getApplicationContext().getFileStreamPath("Data.txt");
        String lineFromFile;

        if (file.exists()){
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput("Data.txt")));

                while ((lineFromFile = reader.readLine()) != null){
                    StringTokenizer tokens = new StringTokenizer(lineFromFile, ",");
                    retrievedArray.add(new TaskClass(tokens.nextToken(), tokens.nextToken(), Boolean.parseBoolean(tokens.nextToken())));
                }
                reader.close();
            } catch (IOException e){
                return null;
            }
        }
        return retrievedArray;
    }
}

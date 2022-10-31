package com.example.taskhollic;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

// Fragmento para editar e adicionar novas tarefas para a lista
public class AddTaskFragment extends Fragment {
    TaskHandler taskHandler;

    public AddTaskFragment(){

    }

    public AddTaskFragment(TaskHandler taskHandler) {
        this.taskHandler = taskHandler;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_task, container, false);
    }
}
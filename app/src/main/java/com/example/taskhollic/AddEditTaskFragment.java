package com.example.taskhollic;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

// Fragmento para editar e adicionar novas tarefas para a lista
public class AddEditTaskFragment extends Fragment {
    ButtonInterface buttonInterface;
    FloatingActionButton fbtnSave;
    View view;

    public AddEditTaskFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_edit_task, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Seta o listener do botao
        buttonInterface = (ButtonInterface) this.getActivity();
        fbtnSave = view.findViewById(R.id.fbtnSave);

        fbtnSave.setOnClickListener(v -> buttonInterface.onSaveClick());
    }
}
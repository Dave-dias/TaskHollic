package com.example.taskhollic;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

//Fragmento para exibir das tarefas que são clicadas no RecycleView
public class DisplayTaskFragment extends Fragment {
    ButtonInterface buttonInterface;
    FloatingActionButton fbtnEdit;
    View view;

    public DisplayTaskFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_display_task, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Seta o listener do botao
        buttonInterface = (ButtonInterface) this.getActivity();
        fbtnEdit = view.findViewById(R.id.fbtnEdit);

        fbtnEdit.setOnClickListener(v -> buttonInterface.onEditClick());
    }
}
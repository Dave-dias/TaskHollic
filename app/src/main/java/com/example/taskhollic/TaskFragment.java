package com.example.taskhollic;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TaskFragment extends Fragment {
    ButtonInterface buttonInterface;
    FloatingActionButton fbtnNew;
    static MyAdapter myAdapter;
    RecyclerView recyclerView;
    View view;

    public TaskFragment (){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_task, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.rvTasks);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        // Passando MainActivity para ser usada como interface no adaptador
        myAdapter = new MyAdapter(this.getActivity(), ApplicationClass.taskList);
        recyclerView.setAdapter(myAdapter);

        //Seta o listener do botao
        buttonInterface = (ButtonInterface) this.getActivity();
        fbtnNew = view.findViewById(R.id.fbtnNew);

        fbtnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonInterface.onNewClick();
            }
        });
    }

    //Atualiza o item modificado da possiçãorecebida
    public static void reloadList (int index){
        myAdapter.setArray(ApplicationClass.taskList);
        myAdapter.notifyItemChanged(index);
    }

    //Adiciona umanova tarefa a lista
    public static void addTask (int index){
        myAdapter.setArray(ApplicationClass.taskList);
        myAdapter.notifyItemInserted(index);
    }

    //Deleta tarefa da lista
    public static void DeleteTasks(int index){
        myAdapter.setArray(ApplicationClass.taskList);
        //.notifyItemRemoved() seria mais especifico mas não funcionou como esperado
        myAdapter.notifyDataSetChanged();
    }

}
package com.example.taskhollic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.database.SQLException;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TaskHandler, ButtonInterface{
    FragmentManager fragmentManager;
    Fragment taskFragment, addTaskFragment, displayTaskFragment;
    TextView tvNameInfo, tvDescriptionInfo, tvDisplayPriority, tvIndex;
    EditText emtTaskDescription, etTaskName;
    Switch sImportant;

    ArrayList<TaskClass> taskList;
    static int lastIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Puxa os dados do banco de dados
        taskList = getTaskList();

        fragmentManager = getSupportFragmentManager();
        taskFragment = new TaskFragment();
        addTaskFragment = new AddTaskFragment();
        displayTaskFragment = new DisplayTaskFragment();

        fragmentSwitch("List");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Retorna a um estado de fragmento especifico dependendo fragmento
        Fragment fragment = fragmentManager.getFragments().get(0);
        if(fragment == displayTaskFragment){
            fragmentManager.popBackStack("Display", 0);
            setViews("Display");
            retrieveTask(lastIndex);
        } else if (fragment == taskFragment){
            fragmentManager.popBackStack("List",0);
        }
    }

    public void setViews(String state){
        switch (state){
            case "Add/Edit":
                etTaskName = findViewById(R.id.etTaskName);
                emtTaskDescription = findViewById(R.id.emtTaskDescription);
                sImportant = findViewById(R.id.sImportant);
                tvIndex = findViewById(R.id.tvIndex);
                break;
            case "Display":
                tvNameInfo = findViewById(R.id.tvNameInfo);
                tvDescriptionInfo = findViewById(R.id.tvDescriptionInfo);
                tvDisplayPriority = findViewById(R.id.tvDisplayPriority);
        }
    }

    @Override
    public void onNewClick() {
        fragmentSwitch("Add/Edit");
        cleanAddEditFragment();
    }

    @Override
    public void onSaveClick() {
        if (etTaskName.getText().length() > 0) {
            //tvIndex exibe o index do objeto ou a palavra "new" caso este seja novo
            if (tvIndex.getText().equals("New")) {
                TaskFragment.addTask(saveChanges(true));
            } else {
                saveChanges(false, Integer.parseInt(tvIndex.getText().toString()));
                TaskFragment.reloadList(Integer.parseInt(tvIndex.getText().toString()));
            }
            fragmentManager.popBackStack("List", 0);
        } else {
            Toast.makeText(getApplicationContext(),"You need to give your task a name!" , Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onEditClick() {
        editTask();
    }

    // Sava as alterações feitas no objeto no array principal
    public void saveChanges(boolean isNew, int index) {
        taskList.get(index).setName(etTaskName.getText().toString().trim());
        taskList.get(index).setDescription(emtTaskDescription.getText().toString().trim());
        taskList.get(index).setImportant(sImportant.isChecked());
        try {
            DatabaseContract db = new DatabaseContract(this);
            db.open();
            updateTask(taskList.get(index));
            db.close();
        }catch (SQLException e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        taskList = getTaskList();
    }

    // Sava o novo objeto no array principal caso
    public int saveChanges(boolean isNew) {
        try {
            DatabaseContract db = new DatabaseContract(this);
            db.open();
            TaskClass task = new TaskClass(db.getRowCount()-1, etTaskName.getText().toString().trim(),
                    emtTaskDescription.getText().toString().trim(), sImportant.isChecked());
            db.addNewTask(task);
            db.close();
        }catch (SQLException e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        taskList = getTaskList();
        return (taskList.size()-1);
    }

    // Chama o metodo de troca de fragmentos, resgata os dados para a tela
    // e seta o index para uso posterior
    @Override
    public void displayTask(int index) {
        lastIndex = index;
        fragmentSwitch("Display");
        retrieveTask(index);
    }

    // Resgata os dados para a tela de display
    private void retrieveTask(int index){
        tvNameInfo.setText(taskList.get(index).getName());

        if (taskList.get(index).getDescription().equals("")) {
            tvDescriptionInfo.setText("(No description found)");
        } else {
            tvDescriptionInfo.setText(taskList.get(index).getDescription());
        }

        if (taskList.get(index).getImportant()) {
            tvDisplayPriority.setText("Important");
            tvDisplayPriority.setTextColor(getResources().getColor(R.color.red_important));
        } else {
            tvDisplayPriority.setText("Commum");
            tvDisplayPriority.setTextColor(getResources().getColor(R.color.purple_500));
        }
    }

    @Override
    public void deleteTask(int id) {
        try {
            DatabaseContract db = new DatabaseContract(this);
            db.open();
            db.deleteTask(id);
            TaskFragment.DeleteTasks();
            db.close();
        }catch (SQLException e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Retorna o numero de entradas no banco
    @Override
    public int getRowCount() {
        int rowCount = 0;
        try {
            DatabaseContract db = new DatabaseContract(this);
            db.open();
            rowCount = db.getRowCount();
            db.close();
        }catch (SQLException e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return rowCount;
    }

    //"Limpa" a tela de edição para a criação de uma nova tarefa
    private void cleanAddEditFragment() {
        etTaskName.setText("");
        emtTaskDescription.setText("");
        tvIndex.setText("New");
        sImportant.setChecked(false);
    }

    // Chama a tela de edição e resgata os dados do objeto a ser editado
    public void editTask() {
        int index = lastIndex;
        fragmentSwitch("Add/Edit");

        etTaskName.setText(taskList.get(index).getName());
        tvIndex.setText(Integer.toString(index));

        if (!taskList.get(index).getDescription().equals("")) {
            emtTaskDescription.setText(taskList.get(index).getDescription());
        }

        if(taskList.get(index).getImportant()){
            sImportant.setChecked(true);
        } else {
            sImportant.setChecked(false);
        }
    }

    //Realiza transações e adiciona elas ao BackStack
    public void fragmentSwitch(String state){
        switch (state){
            case "Add/Edit":
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, addTaskFragment)
                        .setReorderingAllowed(true)
                        .addToBackStack("Add/Edit")
                        .commit();
                fragmentManager.executePendingTransactions();
                setViews("Add/Edit");
                break;

            case "List":
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container,taskFragment)
                        .setReorderingAllowed(true)
                        .addToBackStack("List")
                        .commit();
                fragmentManager.executePendingTransactions();
                break;

            case "Display":
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, displayTaskFragment)
                        .setReorderingAllowed(true)
                        .addToBackStack("Display")
                        .commit();
                fragmentManager.executePendingTransactions();
                setViews("Display");
                break;
        }
    }

    // Puxa as tarefas do banco de dados
    @Override
    public ArrayList<TaskClass> getTaskList(){
        try {
            DatabaseContract db = new DatabaseContract(this);
            db.open();
            ArrayList<TaskClass> taskList = db.getTaskList();
            db.close();
            return taskList;
        } catch (SQLException e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return taskList = new ArrayList<>();
    }

    // Da update na tarefa no ID passada pelo parametro
    @Override
    public void updateTask (TaskClass task) {
        try {
            DatabaseContract db = new DatabaseContract(this);
            db.open();
            db.updateTask(task);
            db.close();
        } catch (SQLException e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
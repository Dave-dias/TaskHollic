package com.example.taskhollic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements TaskHandler{
    FragmentManager fragmentManager;
    Fragment taskFragment, addTaskFragment, displayTaskFragment;
    TextView tvNameInfo, tvDescriptionInfo, tvDisplayPriority, tvIndex;
    EditText emtTaskDescription, etTaskName;
    Button btnAdd, btnSave, btnEdit;
    Switch sImportant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        taskFragment = new TaskFragment();
        addTaskFragment = new AddTaskFragment();
        displayTaskFragment = new DisplayTaskFragment();

        btnAdd = findViewById(R.id.btnAdd);
        btnEdit = findViewById(R.id.btnEdit);
        btnSave =findViewById(R.id.btnSave);

        setButtons();
        fragmentSwitch("List");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //Verifica, seta os botoes para fragmento que vai ser exibido
        Fragment fragment = fragmentManager.getFragments().get(0);
        if(fragment == displayTaskFragment){
            fragmentManager.popBackStack("Display", 0);
            setViews("Display");
            retrieveTask(ApplicationClass.lastIndex);
            buttonPattern("Display");
        } else if (fragment == taskFragment){
            fragmentManager.popBackStack("List",0);
            buttonPattern("List");
        } else if(fragment == addTaskFragment){
            buttonPattern("Add/Edit");
        }
    }

    private void setViews(String state){
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

    // Sava as alterações feitas no objeto no array principal
    private void saveChanges(boolean isNew, int index) {
        ApplicationClass.TaskList.get(index).setName(etTaskName.getText().toString().trim());
        ApplicationClass.TaskList.get(index).setDescription(emtTaskDescription.getText().toString().trim());
        ApplicationClass.TaskList.get(index).setImportant(sImportant.isChecked());
    }

    // Sava o novo objeto no array principal caso
    private int saveChanges(boolean isNew) {
        ApplicationClass.TaskList.add(new TaskClass(etTaskName.getText().toString().trim(),
                emtTaskDescription.getText().toString().trim(), sImportant.isChecked()));

        return (ApplicationClass.TaskList.size()-1);
    }

    //Chama o metodo de troca de fragmentos e resgata os dados para a tela
    @Override
    public void displayTask(int index) {
        ApplicationClass.lastIndex = index;
        fragmentSwitch("Display");
        retrieveTask(index);
    }

    // Resgata os dados para a tela de display
    private void retrieveTask(int index){
        tvNameInfo.setText(ApplicationClass.TaskList.get(index).getName());

        if (ApplicationClass.TaskList.get(index).getDescription().equals("")) {
            tvDescriptionInfo.setText("(No description found)");
        } else {
            tvDescriptionInfo.setText(ApplicationClass.TaskList.get(index).getDescription());
        }

        if (ApplicationClass.TaskList.get(index).getImportant()) {
            tvDisplayPriority.setText("Important");
            tvDisplayPriority.setTextColor(getResources().getColor(R.color.red_important));
        } else {
            tvDisplayPriority.setText("Commum");
            tvDisplayPriority.setTextColor(getResources().getColor(R.color.purple_500));
        }
    }

    @Override
    public void deleteTask(int index) {
        ApplicationClass.TaskList.remove(index);
        TaskFragment.DeleteTasks(index);
    }

    // Chama a tela de edição e resgata os dados do objeto a ser editado
    private void editTask() {
        int index = ApplicationClass.lastIndex;
        fragmentSwitch("Add/Edit");

        etTaskName.setText(ApplicationClass.TaskList.get(index).getName());
        tvIndex.setText(Integer.toString(index));

        if (!ApplicationClass.TaskList.get(index).getDescription().equals("")) {
            emtTaskDescription.setText(ApplicationClass.TaskList.get(index).getDescription());
        }

        if(ApplicationClass.TaskList.get(index).getImportant()){
            sImportant.setChecked(true);
        } else {
            sImportant.setChecked(false);
        }
    }

    //Seta todos os onClickListeners dos botões
    private void setButtons() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etTaskName.getText().length() > 0) {

                    //tvIndex exibe o index do objeto ou a palavra "new" caso este seja novo
                    if (tvIndex.getText().equals("New")) {
                        TaskFragment.addTask(saveChanges(true));
                    } else {
                        saveChanges(false, Integer.parseInt(tvIndex.getText().toString()));
                        TaskFragment.reloadList(Integer.parseInt(tvIndex.getText().toString()));
                    }
                    fragmentManager.popBackStack("List", 0);
                    buttonPattern("List");

                } else {
                    Toast.makeText(getApplicationContext(),"You need to give yout task a name!" , Toast.LENGTH_LONG).show();
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentSwitch("Add/Edit");
                cleanAddEditFragment();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editTask();
                }
        });
    }

    //Realiza transações e adiciona elas ao BackStack, seta botões e views
    private void fragmentSwitch(String state){
        switch (state){
            case "Add/Edit":
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, addTaskFragment)
                        .setReorderingAllowed(true)
                        .addToBackStack("Add/Edit")
                        .commit();
                fragmentManager.executePendingTransactions();

                buttonPattern("Add/Edit");
                setViews("Add/Edit");
                break;

            case "List":
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container,taskFragment)
                        .setReorderingAllowed(true)
                        .addToBackStack("List")
                        .commit();
                fragmentManager.executePendingTransactions();

                buttonPattern("List");
                break;

            case "Display":
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, displayTaskFragment)
                        .setReorderingAllowed(true)
                        .addToBackStack("Display")
                        .commit();
                fragmentManager.executePendingTransactions();

                buttonPattern("Display");
                setViews("Display");
                break;
        }
    }

    //"Limpa" a tela de edição para a criação de uma nova tarefa
    private void cleanAddEditFragment() {
        etTaskName.setText("");
        emtTaskDescription.setText("");
        tvIndex.setText("New");
        sImportant.setChecked(false);
    }

    //Metodo para ativar e desativar botões em certos padrões
    private void buttonPattern(String state){
        switch (state){
            case "Add/Edit":
                btnAdd.setEnabled(false);
                btnEdit.setEnabled(false);
                btnSave.setEnabled(true);
                break;

            case "List":
                btnAdd.setEnabled(true);
                btnEdit.setEnabled(false);
                btnSave.setEnabled(false);
                break;

            case "Display":
                btnAdd.setEnabled(false);
                btnEdit.setEnabled(true);
                btnSave.setEnabled(false);
                break;
        }
    }
}
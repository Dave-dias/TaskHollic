package com.example.taskhollic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

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
    protected void onStop() {
        // Salva os dados da lista num arquivo interno ao pausar atividade
        try {
            FileOutputStream file = openFileOutput("Data.txt", MODE_PRIVATE);
            OutputStreamWriter outputFile = new OutputStreamWriter(file);

            for (TaskClass task: ApplicationClass.taskList){
                outputFile.write(task.getName() + "," + task.getDescription() + "," + task.getImportant() + "\n");
            }

            outputFile.flush();
            outputFile.close();
        } catch (IOException e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        super.onStop();
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

    // Sava as alterações feitas no objeto no array principal
    public void saveChanges(boolean isNew, int index) {
        ApplicationClass.taskList.get(index).setName(etTaskName.getText().toString().trim());
        ApplicationClass.taskList.get(index).setDescription(emtTaskDescription.getText().toString().trim());
        ApplicationClass.taskList.get(index).setImportant(sImportant.isChecked());
    }

    // Sava o novo objeto no array principal caso
    public int saveChanges(boolean isNew) {
        ApplicationClass.taskList.add(new TaskClass(etTaskName.getText().toString().trim(),
                emtTaskDescription.getText().toString().trim(), sImportant.isChecked()));

        return (ApplicationClass.taskList.size()-1);
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
        tvNameInfo.setText(ApplicationClass.taskList.get(index).getName());

        if (ApplicationClass.taskList.get(index).getDescription().equals("")) {
            tvDescriptionInfo.setText("(No description found)");
        } else {
            tvDescriptionInfo.setText(ApplicationClass.taskList.get(index).getDescription());
        }

        if (ApplicationClass.taskList.get(index).getImportant()) {
            tvDisplayPriority.setText("Important");
            tvDisplayPriority.setTextColor(getResources().getColor(R.color.red_important));
        } else {
            tvDisplayPriority.setText("Commum");
            tvDisplayPriority.setTextColor(getResources().getColor(R.color.purple_500));
        }
    }

    @Override
    public void deleteTask(int index) {
        ApplicationClass.taskList.remove(index);
        TaskFragment.DeleteTasks(index);
    }

    // Chama a tela de edição e resgata os dados do objeto a ser editado
    public void editTask() {
        int index = ApplicationClass.lastIndex;
        fragmentSwitch("Add/Edit");

        etTaskName.setText(ApplicationClass.taskList.get(index).getName());
        tvIndex.setText(Integer.toString(index));

        if (!ApplicationClass.taskList.get(index).getDescription().equals("")) {
            emtTaskDescription.setText(ApplicationClass.taskList.get(index).getDescription());
        }

        if(ApplicationClass.taskList.get(index).getImportant()){
            sImportant.setChecked(true);
        } else {
            sImportant.setChecked(false);
        }
    }

    //Seta todos os onClickListeners dos botões
    public void setButtons() {
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
    public void fragmentSwitch(String state){
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
package com.example.taskhollic;

public class TaskClass {
    int id;
    String Name, Description;
    Boolean isImportant;

    public TaskClass(int id, String name, String description, Boolean isImportant) {
        this.id = id;
        Name = name;
        Description = description;
        this.isImportant = isImportant;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        if (Description.isEmpty()){
            this.Description = "(No description)";
        }
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Boolean getImportant() {
        return isImportant;
    }

    public void setImportant(Boolean important) {
        isImportant = important;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

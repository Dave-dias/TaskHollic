package com.example.taskhollic;

public class TaskClass {
    String Name, Description;
    Boolean isImportant;

    public TaskClass(String name, Boolean isImportant) {
        Name = name;
        Description = "";
        this.isImportant = isImportant;
    }

    public TaskClass(String name, String description, Boolean isImportant) {
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

}

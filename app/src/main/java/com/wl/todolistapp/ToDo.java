package com.wl.todolistapp;

import java.util.ArrayList;
import java.util.List;

public class ToDo {
    private String name;
    private String description;
    private boolean isCompleted;

    public static final List<ToDo> toDos = new ArrayList<>();

//    static {
//        toDos.add(new ToDo("My Office Worklist", "Complete the project report, attend the team meeting at 2 PM, and review the client feedback.", true));
//        toDos.add(new ToDo("My Shopping List", "Buy milk, bread, and apples from the supermarket, and don't forget to check the pharmacy for vitamins.", false));
//        toDos.add(new ToDo("My Studying List", "Read Chapter 4 of the history textbook, practice math problems, and prepare the science project outline.", false));
//        toDos.add(new ToDo("test1", "test 1 content", false));
//    }

    public ToDo(String name, String description, boolean isCompleted){
        this.name = name;
        this.description = description;
        this.isCompleted = isCompleted;
    }

    public static List<ToDo> getToDos() {
        return toDos;
    }

    public static void addToDo(String name, String description) {
        toDos.add(new ToDo(name, description, false));
    }

    public static void removeToDo(String name) {
        toDos.removeIf(todo -> todo.getName().equals(name));
    }

    public static void setCompleted(String name) {
        for (ToDo todo : toDos) {
            if (todo.getName().equals(name)) {
                todo.isCompleted = !todo.isCompleted;
                break;
            }
        }
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public boolean getIsComplete(){
        return isCompleted;
    }

    @Override
    public String toString(){
        return this.name + " - " + (isCompleted ? "Completed" : "Pending");
    }
}

package model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a to-do list consisting of a collection of tasks.
 * Able to add and remove tasks to/from the list.
 * Duplicaiton of task is allowed as long as they don't occupy the same time slot
 */

public class ListToDo {
    private ArrayList<Task> list;
    private HashMap<String, String> availability;

    public ListToDo() {
        this.list = new ArrayList<Task>();
        this.availability = new HashMap<>();
    }


    // REQUIRES: task.done != true
    // MODIFIES: this
    // EFFECTS: add a task to the list to do, if duplicated add anyway
    public void addTask(Task task) {
        this.list.add(task);
    }

    // REQUIRES: task.done != true
    // MODIFIES: this
    // EFFECTS: remove a task to the list to do
    public void removeTask(Task task) {
        this.list.remove(task);
    }

    public ArrayList<Task> getList() {
        return this.list;
    }

    public HashMap<String, String> getAvailability(){
        return this.availability;
    }
}

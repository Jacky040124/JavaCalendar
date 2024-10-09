package model;
import java.util.ArrayList;

public class ListToDo {
    private ArrayList<Task> list;

    public ListToDo() {
        this.list = new ArrayList<Task>();
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
}

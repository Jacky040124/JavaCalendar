package model;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Represents a to-do list consisting of a collection of tasks.
 * Able to add and remove tasks to/from the list.
 * Duplicaiton of task is allowed as long as they don't occupy the same time slot
 */

public class ListToDo {
    private ArrayList<Task> list;
    private HashMap<String, String> availability;

    // EFFECTS: creates a new empty list of tasks and initializes availability hashmap
    public ListToDo() {
        this.list = new ArrayList<Task>();
        this.availability = new HashMap<>();
        
    }


    // REQUIRES: task.done != true
    // MODIFIES: this
    // EFFECTS: add a task to the list to do, if duplicated add anyway
    public void addTask(Task task) {
        this.list.add(task);

        EventLog events = EventLog.getInstance();
        events.logEvent(new Event("Task " + task.getName() + " added from calendar."));
     
    }

    // REQUIRES: task.done != true
    // MODIFIES: this
    // EFFECTS: remove a task to the list to do
    public void removeTask(Task task) {
        this.list.remove(task);

        for (int i = 0; i < task.getLength(); i++) {
            String strDay = Integer.toString(task.getDay());
            String strTime = Integer.toString(task.getTime() + i);
            availability.remove(strDay + ":" + strTime);
        }
        EventLog events = EventLog.getInstance();
        events.logEvent(new Event("Task " + task.getName() + " removed from calendar."));

    }


    // EFFECTS: writes file content to a JSONArray and reuturn it
    public JSONArray toJson() {
        JSONArray jsonLst = new JSONArray();

        for (int i = 0; i < list.size(); i++) {
            Task curretnTask = list.get(i);
            JSONObject jsonTask = new JSONObject();
            jsonTask.put("name",curretnTask.getName());
            jsonTask.put("length",curretnTask.getLength());
            jsonTask.put("day",curretnTask.getDay());
            jsonTask.put("time",curretnTask.getTime());
            jsonTask.put("done",curretnTask.getDone());
            jsonLst.put(jsonTask);
        }

        return jsonLst;
    }

    // EFFECTS: returns the list of tasks
    public ArrayList<Task> getList() {
        return this.list;
    }



    // EFFECTS: returns the availability hashmap of time slots
    public HashMap<String, String> getAvailability() {
        return this.availability;
    }


}

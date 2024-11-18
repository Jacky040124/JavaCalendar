package ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import model.ListToDo;
import model.Task;
import persistence.JsonReader;
import persistence.JsonWriter;

public class CalendarView {
    private static final String JSON_STORE = "./data/myList.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private ListToDo lst;
    private Scanner input;

    // EFFECTS: Initializes the CalendarView object and runs the user interface.
    public CalendarView() {
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
        input = new Scanner(System.in);
        lst = new ListToDo();
        // runCalendar();
        
    }

    // MODIFIES: this
    // EFFECTS: Adds a new task to the list and marks its time slots as unavailable if they are free; 
    //          else, notifies the user time slot is not available.
    public void addingTask() {
        System.out.println("whole number time (16 for 16:00)");
        int time = input.nextInt();
        System.out.println("task name");
        String name = input.next().toLowerCase();
        System.out.println("task length");
        int length = input.nextInt();
        System.out.println("Input weekday in integer form, eg Monday is 0");
        int date = input.nextInt();
        Task taskToAdd = new Task(name,length,date,time);

        if (checkAvailability(taskToAdd)) {
            addAvailability(taskToAdd);
            lst.addTask(taskToAdd);
        } else {
            System.out.println("busy");
        }
    }

    // REQUIRES: lst is not empty.
    // MODIFIES: this
    // EFFECTS: Removes the specified task from the list based on user input.
    public void removeTask() { 
        if (lst.getList().size() == 0) {
            System.out.println("Empty");
        } else {
            int selected = input.nextInt();
            Task taskSelected = lst.getList().get(selected);
            lst.removeTask(taskSelected);
        }


    }

    // REQUIRES: lst is not empty.
    // MODIFIES: this
    // EFFECTS: Marks the specified task as completed based on user input.
    public void markTaskDone() { 
        if (lst.getList().size() == 0) { 
            System.out.println("Empty");
        } else {
            int selected = input.nextInt();
            Task taskSelected = lst.getList().get(selected);
            taskSelected.setDone(true);
        }

    }

    // EFFECTS: Returns true if the specified task's time slots are available; else false.
    public boolean checkAvailability(Task task) {
        String strDay = Integer.toString(task.getDay());
        for (int i = 0; i < task.getLength(); i++) {
            String strTime = Integer.toString(task.getTime() + i);
            if (lst.getAvailability().containsKey(strDay + ":" + strTime)) {
                return false;
            }

        }
        return true;
    }

    // MODIFIES: availability
    // EFFECTS: Adds the task's time slots to availability, marking them as unavailable.
    public void addAvailability(Task task) {
        for (int i = 0; i < task.getLength(); i++) {
            String strDay = Integer.toString(task.getDay());
            String strTime = Integer.toString(task.getTime() + i);
            lst.getAvailability().put(strDay + ":" + strTime,task.getName());
        }
    }


    // EFFECTS: saves the list to file
    public void saveListToDo() {
        try {
            jsonWriter.open();
            jsonWriter.write(lst);
            jsonWriter.close();
            System.out.println("Saved list to" + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: regenerate the list to do and load the saved availability from file
    public void loadListToDo() {
        try {
            lst = jsonReader.read();

            for (Task task : lst.getList()) {
                addAvailability(task);
            }
            System.out.println("Loaded list from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }



}

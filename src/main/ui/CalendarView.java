package ui;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import model.ListToDo;
import model.Task;
// import persistence.JsonReader;
import persistence.JsonWriter;

public class CalendarView {
    private static final String JSON_STORE = "./data/myList.json";
    private JsonWriter jsonWriter;
    // private JsonReader jsonReader;
    private ListToDo lst;
    private Scanner input;

    // EFFECTS: Initializes the CalendarView object and runs the user interface.
    public CalendarView() {
        jsonWriter = new JsonWriter(JSON_STORE);
        input = new Scanner(System.in);
        lst = new ListToDo();
        runCalendar();
        
    }

    // MODIFIES: this
    // EFFECTS: Continuously accepts user commands until the user chooses to quit.
    private void runCalendar() {
        boolean continueRunning = true;
        String command = null;
        while (continueRunning) {
            displayCalendar();
            System.out.println("a : add Task | m : mark task done | r : remove task | s : save task | l : load task");
            command = input.next().toLowerCase();
            if (command.equals("q")) {
                continueRunning = false;
            } else {
                run(command);
            }
        }
        System.out.println("Thank you");
    } 

    // EFFECTS: Displays the list of tasks.
    public void displayList() {
        for (int i = 0; i < lst.getList().size();i++ ) {
            System.out.println(i + "|" + lst.getList().get(i).getName());
        }
    }

    // EFFECTS: Prints the calendar view with time slots and tasks for each day of the week.
    public void displayCalendar() {
        System.out.println("Time |Monday    |Tuesday   |Wednesday |Thuresday |Friday    |Saturday  |Sunday    ");
        System.out.println("----------------------------------------------------------------------------------");

        for (int i = 0; i <= 24; i ++) {
            String strToPrint = String.format("%02d:00", i);
            for (int j = 0; j < 7; j++) {
                String key = Integer.toString(j) +":"+ Integer.toString(i);
                if (lst.getAvailability().containsKey(key)) {
                    strToPrint += String.format("|%-10s", lst.getAvailability().get(key)); 
                } else {
                    strToPrint += "|          ";
                }
            }
            System.out.println(strToPrint);

        }
    }

    // MODIFIES: this
    // EFFECTS: Executes the user's command, 'a' to add a task, 'm' to mark a task as done, 'r' to remove a task
    public void run(String command) {
        switch (command) {
            case "a":
                addingTask();
                break;
            case "m":
                markTaskDone();
                break;
            case "r":
                removeTask();
                break;
            case "s":
                saveListToDo();
                break;
            case "l":
                // loadListToDo();
                break;
            default:
                System.out.println("Invalid Input");
                break;
        }
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
        if (lst.getList().size() == 0){
            System.out.println("Empty");
        } else {
            displayList();
            System.out.println("Select by index");
            int selected = input.nextInt();
            Task taskSelected = lst.getList().get(selected);
            lst.removeTask(taskSelected);
        }


    }

    // REQUIRES: lst is not empty.
    // MODIFIES: this
    // EFFECTS: Marks the specified task as completed based on user input.
    public void markTaskDone() {
        if (lst.getList().size() == 0){
            System.out.println("Empty");
        } else {
            displayList();
            System.out.println("Select by index");
            int selected = input.nextInt();
            Task taskSelected = lst.getList().get(selected);
            taskSelected.setDone(true);
        }

    }

    // EFFECTS: Returns true if the specified task's time slots are available; else false.
    public boolean checkAvailability(Task task) {
        String strDay = Integer.toString(task.getDay());
        for (int i = 0; i < task.getLength(); i++) {
            String strTime = Integer.toString(task.getTime()+i);
            if (lst.getAvailability().containsKey(strDay+":"+ strTime)) {
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
                String strTime = Integer.toString(task.getTime()+i);
                lst.getAvailability().put(strDay+":"+ strTime,task.getName());
            System.out.println("availability added" + strDay+":"+ strTime);
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
    // EFFECTS: load the saved availability from file, and regenerate the list to do
    // public void loadListToDo() {
    //     try {
    //         workRoom = jsonReader.read();
    //         System.out.println("Loaded " + workRoom.getName() + " from " + JSON_STORE);
    //     } catch (IOException e) {
    //         System.out.println("Unable to read from file: " + JSON_STORE);
    //     }
    // }


}

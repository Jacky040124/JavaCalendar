package ui;
import java.util.Scanner;
import model.ListToDo;

public class CalendarView {
    private ListToDo lst;
    private Scanner input;

    // EFFECTS: runs the user interface 
    public CalendarView() {
        lst = new ListToDo();
        runCalendar();
    }

    // MODIFIES: this
    // EFFECTS: response to the user's input, implementing a user interface
    private void runCalendar() {
        boolean continueRunning = true;
        String command = null;

        while (continueRunning) {
            updateCalendar(lst);
            command = input.next().toLowerCase();
            if (command.equals("q")) {
                continueRunning = false;
            } else {
                run(command);
            }
        }
        System.out.println("Thank you");
    } 


    // EFFECTS: update and prints out calendar to match events in the list 
    public void updateCalendar(ListToDo lst) {
        System.out.println("Monday | Tuesday | Wednesday | Thuresday | Friday | Saturday | Sunday");
        System.out.println("---------------------------------------------------------------------");
        System.out.println("       |         |           |           |        |          |       ");
        System.out.println("       |         |           |           |        |          |       ");
        System.out.println("       |         |           |           |        |          |       ");
        System.out.println("       |         |           |           |        |          |       ");
        System.out.println("       |         |           |           |        |          |       ");
        System.out.println("       |         |           |           |        |          |       ");
        System.out.println("       |         |           |           |        |          |       ");

    }

    // MODIFIES: this
    // EFFECTS: response to user's input
    public void run(String command) {
        switch (command) {
            case "a":
                addingTask();
                break;
            case "c":
                changeTask();
                break;
            case "d":
                markTaskDone();
                break;

            default:
                System.out.println("Invalid Input");
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: add a task to list
    public void addingTask() {
        // TODO
    }

    // REQUIRES: lst.size() > 0
    // MODIFIES: this
    // EFFECTS: change attributes of an existing task
    public void changeTask() {
        // TODO
    }

    // REQUIRES: lst.size() > 0
    // MODIFIES: this
    // EFFECTS: mark a task in the list as done
    public void markTaskDone() {
        // TODO
    }
}

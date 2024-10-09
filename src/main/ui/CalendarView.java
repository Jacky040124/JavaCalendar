package ui;
import java.util.Scanner;
import model.ListToDo;
import model.Task;

public class CalendarView {
    private ListToDo lst;
    private Scanner input;

    // EFFECTS: runs the user interface 
    public CalendarView() {
        input = new Scanner(System.in);
        lst = new ListToDo();
        runCalendar();
    }

    // MODIFIES: this
    // EFFECTS: response to the user's input, implementing a user interface
    private void runCalendar() {
        boolean continueRunning = true;
        String command = null;
        while (continueRunning) {
            displayCalendar(lst);
            System.out.println("a : add Task | m : mark task done | r : remove task");
            command = input.next().toLowerCase();
            if (command.equals("q")) {
                continueRunning = false;
            } else {
                run(command);
            }
        }
        System.out.println("Thank you");
    } 

    // EFFECTS: prints out list to do
    public void displayList() {
        for (int i = 0; i < lst.getList().size();i++ ) {
            System.out.println(i + "|" + lst.getList().get(i).getName());
        }
    }

    // EFFECTS: prints out list to do in a calendar view 
    public void displayCalendar(ListToDo lst) {
        // TODO: currently hard coded, update to refelct lst content
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
            case "m":
                markTaskDone();
                break;
            case "r":
                removeTask();
                break;
            default:
                System.out.println("Invalid Input");
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: add a task to list
    public void addingTask() {
        System.out.println("Input task name");
        String name = input.next().toLowerCase();
        System.out.println("Input task length");
        int length = input.nextInt();
        System.out.println("Input date in the format 25/10/2024");
        String date = input.next().toLowerCase();
        System.out.println("Input time in the format 16:00, only whole hours");
        String time = input.next().toLowerCase();
        Task taskToAdd = new Task(name,length,date,time);
        lst.addTask(taskToAdd);
    }

    // REQUIRES: lst.size() > 0
    // MODIFIES: this
    // EFFECTS: remove a task from the list to do
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

    // REQUIRES: lst.size() > 0
    // MODIFIES: this
    // EFFECTS: mark a task in the list as done
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


}

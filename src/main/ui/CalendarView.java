package ui;

import java.io.FileNotFoundException;
import java.io.IOException;

import model.ListToDo;
import model.Task;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;

public class CalendarView extends JFrame {
    private static final String JSON_STORE = "./data/myList.json";
	public static final int WIDTH = 1000;
	public static final int HEIGHT = 700;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private ListToDo lst;
    private JPanel calendarPanel;

    // EFFECTS: Initializes the CalendarView object and runs the user interface.
    public CalendarView() {
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
        lst = new ListToDo();
        initializeWindow();
        // runCalendar();
        
    }

    // MODIFIES: this
    // EFFECTS:  draws the JFrame window where the Calendar will operate
    private void initializeWindow() {

        // LAYOUT : NORTH, SOUTH, EAST, WEST, and CENTER
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(WIDTH, HEIGHT));

        // Add button panel to the bottom of the frame
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        // Set window properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    

    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // buttons
        JButton addButton = new JButton("Add Task");
        JButton markButton = new JButton("Mark Done");
        JButton removeButton = new JButton("Remove Task");
        JButton saveButton = new JButton("Save");
        JButton loadButton = new JButton("Load");

        // listeners
        addButton.addActionListener(e -> addingTask());
        markButton.addActionListener(e -> markTaskDone());
        removeButton.addActionListener(e -> removeTask());
        saveButton.addActionListener(e -> saveListToDo());
        loadButton.addActionListener(e -> loadListToDo());

        // Add buttons to panel
        buttonPanel.add(addButton);
        buttonPanel.add(markButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

        return buttonPanel;
    }


    // MODIFIES: this
    // EFFECTS: Adds a new task to the list and marks its time slots as unavailable if they are free; 
    //          else, notifies the user time slot is not available.
    public void addingTask() {
    }

    // REQUIRES: lst is not empty.
    // MODIFIES: this
    // EFFECTS: Removes the specified task from the list based on user input.
    public void removeTask() { 
    }

    // REQUIRES: lst is not empty.
    // MODIFIES: this
    // EFFECTS: Marks the specified task as completed based on user input.
    public void markTaskDone() { 
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

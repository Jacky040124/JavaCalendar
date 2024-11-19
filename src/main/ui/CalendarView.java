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
        createButtonPanel();

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

        calendarPanel = new JPanel();
        calendarPanel.setLayout(new BorderLayout());
        add(calendarPanel, BorderLayout.CENTER);
        updateCalendarView();
    }

    // MODIFIES: this
    // EFFECTS: Creates and returns a panel with all buttons
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
    // EFFECTS: Opens dialog boxes to add task
    private void addingTask() {
        try {
            String name = JOptionPane.showInputDialog(this, "Enter task name:");
            if (name == null) return;
            name = name.toLowerCase();
            
            String string = JOptionPane.showInputDialog(this, "Enter task length (hours):");
            if (string == null) return;
            int length = Integer.parseInt(string);

            int date = getDaySelection();
            if (date == -1) return;

            String timeStr = JOptionPane.showInputDialog(this, "Enter time (24-hour format, e.g., 16 for 4:00 PM):");
            if (timeStr == null) return;
            int time = Integer.parseInt(timeStr);

            Task taskToAdd = new Task(name, length, date, time);

            if (checkAvailability(taskToAdd)) {
                addAvailability(taskToAdd);
                lst.addTask(taskToAdd);
                JOptionPane.showMessageDialog(this, "Task added");
            } else {
                JOptionPane.showMessageDialog(this, "Time slot is not available!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers!", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
        updateCalendarView();
    }

    // REQUIRES: lst is not empty
    // MODIFIES: this
    // EFFECTS: Shows tasks and allows user to mark one as done
    private void markTaskDone() {
        if (lst.getList().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tasks", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] options = createTaskArray();
        int choice = JOptionPane.showOptionDialog(this,
                "Select task:",
                "Mark Task Done",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice >= 0) {
            lst.getList().get(choice).setDone(true);
            JOptionPane.showMessageDialog(this, "Task marked as done!");
        }
        updateCalendarView();
    }

    // REQUIRES: lst is not empty
    // MODIFIES: this
    // EFFECTS: Shows list of tasks and allows user to remove one
    private void removeTask() {
        if (lst.getList().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tasks to remove!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] options = createTaskArray();
        int choice = JOptionPane.showOptionDialog(this,
                "Select task:",
                "Remove Task",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice >= 0) {
            Task taskToRemove = lst.getList().get(choice);
            lst.removeTask(taskToRemove);
            JOptionPane.showMessageDialog(this, "Task removed successfully");
        }
        updateCalendarView();
    }

    // EFFECTS: Creates an array of task descriptions for display in dialog boxes
    private String[] createTaskArray() {
        String[] options = new String[lst.getList().size()];
        for (int i = 0; i < lst.getList().size(); i++) {
            Task task = lst.getList().get(i);
            options[i] = String.format("%s (Day: %d, Time: %d, Length: %d)", 
                task.getName(), task.getDay(), task.getTime(), task.getLength());
        }
        return options;
    }

    // EFFECTS: saves the list to file with visual feedback
    private void saveListToDo() {
        try {
            jsonWriter.open();
            jsonWriter.write(lst);
            jsonWriter.close();
            JOptionPane.showMessageDialog(this, "Saved list to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Unable to write to file: " + JSON_STORE,
                "Error", JOptionPane.ERROR_MESSAGE);
        }
        updateCalendarView();
    }

    // MODIFIES: this
    // EFFECTS: loads the list from file with visual feedback
    private void loadListToDo() {
        try {
            lst = jsonReader.read();
            for (Task task : lst.getList()) {
                addAvailability(task);
            }
            JOptionPane.showMessageDialog(this, "Loaded list from " + JSON_STORE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to read from file: " + JSON_STORE,
                "Error", JOptionPane.ERROR_MESSAGE);
        }
        updateCalendarView();
    }

    // EFFECTS: Returns true if time slots available; else false.
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
    // EFFECTS: Add time slots to availability
    public void addAvailability(Task task) {
        for (int i = 0; i < task.getLength(); i++) {
            String strDay = Integer.toString(task.getDay());
            String strTime = Integer.toString(task.getTime() + i);
            lst.getAvailability().put(strDay + ":" + strTime,task.getName());
        }
        updateCalendarView();
    }


    // MODIFIES: this
    // EFFECTS: Updates calendar view
    private void updateCalendarView() {
        calendarPanel.removeAll(); // Clear existing components

        String[] columnNames = {"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        String[][] data = new String[24][8];

        for (int i = 0; i < 24; i++) {
            data[i][0] = String.format("%02d:00", i);
            for (int j = 1; j < 8; j++) {
                String key = (j - 1) + ":" + i;
                data[i][j] = lst.getAvailability().getOrDefault(key, "");
            }
        }

        // Create table 
        JTable calendarTable = new JTable(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Set table appearance
        calendarTable.setRowHeight(30);

        // Add scroll pane
        JScrollPane scrollPane = new JScrollPane(calendarTable);
        calendarPanel.add(scrollPane, BorderLayout.CENTER);
        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    // EFFECTS: Shows a dialog with selection buttons and returns the selected day
    private int getDaySelection() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        final int[] selectedDay = {-1};

        for (int i = 0; i < days.length; i++) {
            final int day = i;
            JButton dayButton = new JButton(days[i]);
            dayButton.addActionListener(e -> {
                selectedDay[0] = day;
                Window dialog = SwingUtilities.getWindowAncestor((JButton) e.getSource());
                dialog.dispose();
            });
            panel.add(dayButton);
        }

        JDialog dialog = new JDialog(this, "Select Day", true);
        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        return selectedDay[0];
    }

}

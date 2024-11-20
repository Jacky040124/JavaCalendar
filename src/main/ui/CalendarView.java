package ui;

import java.io.FileNotFoundException;
import java.io.IOException;

import model.ListToDo;
import model.Task;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

// EFFECTS: Create a calendarView and provides GUI
public class CalendarView extends JFrame {
    private static final String JSON_STORE = "./data/myList.json";
	public static final int WIDTH = 1000;
	public static final int HEIGHT = 700;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private ListToDo lst;
    private JPanel calendarPanel;
    private JPanel mainPanel;
    

    // EFFECTS: Initializes the CalendarView object and runs the user interface.
    public CalendarView() {
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
        lst = new ListToDo();
        initializeWindow();
        
        // Add calendar panel
        calendarPanel = new JPanel(new BorderLayout());
        mainPanel.add(calendarPanel, BorderLayout.CENTER);
        
        // Add button panel at the bottom
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        updateCalendarView();
    }

    // MODIFIES: this
    // EFFECTS:  draws the JFrame window where the Calendar will operate
    private void initializeWindow() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Add icon to the title bar
        setIconImage(getIcon().getImage());
        
        // Create a panel for the icon in the top-right corner
        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel iconLabel = new JLabel(getIcon());
        iconPanel.add(iconLabel);
        
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(iconPanel, BorderLayout.NORTH);
        add(mainPanel);
        
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: Creates and returns a panel containing all control buttons
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



    // REQUIRES: task name not null, length > 0, time between 0-23
    // MODIFIES: this
    // EFFECTS: Adds task to calendar if time slot available
    private void addingTask() {
        try {
            String name = JOptionPane.showInputDialog(this, "Enter task name:");
            if (name == null) return; // User clicked cancel
            name = name.toLowerCase();

            String lengthStr = JOptionPane.showInputDialog(this, "Enter task length (hours):");
            if (lengthStr == null) return;
            int length = Integer.parseInt(lengthStr);

            int date = getDaySelection();
            if (date == -1) return; // User closed the dialog without selecting

            String timeStr = JOptionPane.showInputDialog(this, "Enter time (24-hour format, e.g., 16 for 4:00 PM):");
            if (timeStr == null) return;
            int time = Integer.parseInt(timeStr);

            Task taskToAdd = new Task(name, length, date, time);

            if (checkAvailability(taskToAdd)) {
                addAvailability(taskToAdd);
                lst.addTask(taskToAdd);
                JOptionPane.showMessageDialog(this, "Task added successfully!");
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

    // REQUIRES: lst not empty
    // MODIFIES: this
    // EFFECTS: Marks selected task as done
    private void markTaskDone() {
        if (lst.getList().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tasks to mark!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] options = createTaskArray();
        int choice = JOptionPane.showOptionDialog(this,
                "Select task to mark as done:",
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

    // REQUIRES: lst not empty
    // MODIFIES: this
    // EFFECTS: Removes selected task from calendar
    private void removeTask() {
        if (lst.getList().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tasks to remove!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] options = createTaskArray();
        int choice = JOptionPane.showOptionDialog(this,
                "Select task to remove:",
                "Remove Task",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice >= 0) {
            Task taskToRemove = lst.getList().get(choice);
            lst.removeTask(taskToRemove);
            JOptionPane.showMessageDialog(this, "Task removed successfully!");
        }
        updateCalendarView();
    }

    // EFFECTS: Returns array of task descriptions
    private String[] createTaskArray() {
        List<String> options = new ArrayList<>();
        for (Task task : lst.getList()) {
            options.add(String.format("%s (Day: %d, Time: %d, Length: %d)", 
                task.getName(), task.getDay(), task.getTime(), task.getLength()));
        }
        return options.toArray(new String[0]);
    }

    // MODIFIES: JSON_STORE
    // EFFECTS: Saves current list to file
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
    // EFFECTS: Loads list from file and updates calendar
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

    // REQUIRES: task not null
    // EFFECTS: Returns true if task's time slots are available
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

    // REQUIRES: task not null
    // MODIFIES: this
    // EFFECTS: Marks task's time slots as unavailable
    public void addAvailability(Task task) {
        for (int i = 0; i < task.getLength(); i++) {
            String strDay = Integer.toString(task.getDay());
            String strTime = Integer.toString(task.getTime() + i);
            lst.getAvailability().put(strDay + ":" + strTime,task.getName());
        }
        updateCalendarView();
    }

    // MODIFIES: this
    // EFFECTS: Refreshes calendar display with current tasks
    private void updateCalendarView() {
        calendarPanel.removeAll();

        // Create table model
        List<String> columnNamesList = Arrays.asList("Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
        // just to make JTable work
        String[] columnNames = columnNamesList.toArray(new String[0]);
        String[][] data = new String[24][8];

        // Fill time column
        for (int i = 0; i < 24; i++) {
            data[i][0] = String.format("%02d:00", i);
            for (int j = 1; j < 8; j++) {
                String key = (j - 1) + ":" + i;
                if (lst.getAvailability().containsKey(key)) {
                    String taskName = lst.getAvailability().get(key);
                    boolean isDone = false;
                    for (Task task : lst.getList()) {
                        if (task.getName().equals(taskName) && task.getDone()) {
                            isDone = true;
                            break;
                        }
                    }
                    data[i][j] = isDone ? "✓ " + taskName : taskName;
                } else {
                    data[i][j] = "";
                }
            }
        }

        // Create table
        JTable calendarTable = new JTable(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // appearance
        calendarTable.setRowHeight(30);
        calendarTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        for (int i = 1; i < 8; i++) {
            calendarTable.getColumnModel().getColumn(i).setPreferredWidth(120);
        }

        // Add table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(calendarTable);
        calendarPanel.add(scrollPane, BorderLayout.CENTER);
        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    // EFFECTS: returns selected day (0-6)
    private int getDaySelection() {
        String input = JOptionPane.showInputDialog(this, 
            "Enter day number:\n0: Monday\n1: Tuesday\n2: Wednesday\n3: Thursday\n4: Friday\n5: Saturday\n6: Sunday");
        
        try {
            if (input == null) return -1;
            int day = Integer.parseInt(input);
            return (day >= 0 && day <= 6) ? day : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private ImageIcon getIcon() {
        ImageIcon icon = new ImageIcon("CalendarLogo.png");
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        icon = new ImageIcon(resizedImage);
        return icon;
    }

}
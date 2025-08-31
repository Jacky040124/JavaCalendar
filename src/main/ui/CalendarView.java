package ui;

import java.io.FileNotFoundException;
import java.io.IOException;

import model.ListToDo;
import model.Task;
import model.TimeConverter;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import model.Event;
import model.EventLog;

// Iterator<Event> iterator = events.iterator();
// Event latest = new Event("");
// while (iterator.hasNext()) {
//     System.out.println(latest.toString());
// }

// EFFECTS: Create a calendarView and provides GUI
public class CalendarView extends JFrame {
    private static final String JSON_STORE = "./data/myList.json";
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 700;
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
        setupPanels();
        updateCalendarView();
    }

    // MODIFIES: this
    // EFFECTS: sets up main panels
    private void setupPanels() {
        calendarPanel = new JPanel(new BorderLayout());
        mainPanel.add(calendarPanel, BorderLayout.CENTER);
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    // MODIFIES: this
    // EFFECTS: draws the JFrame window where the Calendar will operate
    private void initializeWindow() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                printLog();
                System.exit(0);
            }
        });
        setLocationRelativeTo(null);
        setIconImage(getIcon().getImage());
        setupIconPanel();
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: sets up icon panel
    private void setupIconPanel() {
        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel iconLabel = new JLabel(getIcon());
        iconPanel.add(iconLabel);
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(iconPanel, BorderLayout.NORTH);
        add(mainPanel);
    }

    // MODIFIES: this
    // EFFECTS: Creates and returns a panel containing all control buttons
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        addButtons(buttonPanel);
        return buttonPanel;
    }

    // MODIFIES: buttonPanel
    // EFFECTS: adds buttons to panel
    private void addButtons(JPanel buttonPanel) {
        JButton addButton = new JButton("Add Task");
        JButton markButton = new JButton("Mark Done");
        JButton removeButton = new JButton("Remove Task");
        JButton timeCalcButton = new JButton("Time Calculator");
        JButton saveButton = new JButton("Save");
        JButton loadButton = new JButton("Load");

        addButton.addActionListener(e -> addingTask());
        markButton.addActionListener(e -> markTaskDone());
        removeButton.addActionListener(e -> removeTask());
        timeCalcButton.addActionListener(e -> showTimeCalculator());
        saveButton.addActionListener(e -> saveListToDo());
        loadButton.addActionListener(e -> loadListToDo());

        addButtonsToPanel(buttonPanel, addButton, markButton, removeButton, timeCalcButton, saveButton, loadButton);
    }

    // MODIFIES: buttonPanel
    // EFFECTS: adds buttons to panel
    private void addButtonsToPanel(JPanel buttonPanel, JButton... buttons) {
        for (JButton button : buttons) {
            buttonPanel.add(button);
        }
    }

    // REQUIRES: task name not null, length > 0, time between 0-23
    // MODIFIES: this
    // EFFECTS: Adds task to calendar if time slot available
    private void addingTask() {
        try {
            processNewTask();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        updateCalendarView();
    }

    // MODIFIES: this
    // EFFECTS: processes new task input
    private void processNewTask() {
        String name = JOptionPane.showInputDialog(this, "Enter task name:");
        if (name == null) {
            return;
        }
        name = name.toLowerCase();

        String lengthStr = JOptionPane.showInputDialog(this, "Enter task length (hours):");
        if (lengthStr == null) {
            return;
        }
        int length = Integer.parseInt(lengthStr);

        processTaskDetails(name, length);
    }

    // MODIFIES: this
    // EFFECTS: processes task details
    private void processTaskDetails(String name, int length) {
        int date = getDaySelection();
        if (date == -1) {
            return;
        }

        String timePrompt = "Enter time (you can use):\n" +
                           "• 24h format: 16 (for 4PM)\n" +
                           "• 12h format: 4 PM, 2:00 PM\n" +
                           "• HH:MM format: 14:00";
        String timeStr = JOptionPane.showInputDialog(this, timePrompt);
        if (timeStr == null) {
            return;
        }
        
        int time;
        try {
            time = TimeConverter.parseTimeInput(timeStr);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Invalid time format: " + e.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        addTaskIfAvailable(new Task(name, length, date, time));
    }

    // MODIFIES: this
    // EFFECTS: adds task if time slot is available
    private void addTaskIfAvailable(Task taskToAdd) {
        if (checkAvailability(taskToAdd)) {
            addAvailability(taskToAdd);
            lst.addTask(taskToAdd);
            JOptionPane.showMessageDialog(this, "Task added successfully!");
        } else {
            String msg = "Time slot is not available!";
            JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
        }
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
            String dayName = task.getDayName();
            String timeRange = task.getTimeRange(true); // Use 12-hour format for display
            String status = task.getDone() ? "✓ " : "";
            String taskInfo = String.format("%s%s - %s (%s, %s)", 
                    status, task.getName(), timeRange, dayName, task.getLength() + "h");
            options.add(taskInfo);
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
            JOptionPane.showMessageDialog(this, "Saved to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            String msg = "Unable to write to: " + JSON_STORE;
            JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "Loaded from " + JSON_STORE);
        } catch (IOException e) {
            String msg = "Unable to read from: " + JSON_STORE;
            JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
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
        String[][] data = createTableData();
        JTable calendarTable = createCalendarTable(data);
        setupTableAppearance(calendarTable);
        addTableToPanel(calendarTable);
    }

    // EFFECTS: Creates and returns table data structure for calendar display
    private String[][] createTableData() {
        List<String> cols = Arrays.asList("Time", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");
        String[] columnNames = cols.toArray(new String[0]);
        String[][] data = new String[24][8];
        fillTimeData(data);
        return data;
    }

    // MODIFIES: data
    // EFFECTS: fills time column (0-23) in data array
    private void fillTimeData(String[][] data) {
        for (int i = 0; i < 24; i++) {
            // Show both 24-hour and 12-hour formats for better readability
            String time24 = String.format("%02d:00", i);
            String time12 = TimeConverter.to12HourFormat(i);
            data[i][0] = time24 + " (" + time12 + ")";
            fillRowData(data, i);
        }
    }

    // MODIFIES: data
    // EFFECTS: fills task data for each day in the given row
    private void fillRowData(String[][] data, int row) {
        for (int j = 1; j < 8; j++) {
            String key = (j - 1) + ":" + row;
            data[row][j] = getTaskDisplay(key);
        }
    }

    // EFFECTS: returns task display string for given time slot, with checkmark if task is done
    private String getTaskDisplay(String key) {
        if (lst.getAvailability().containsKey(key)) {
            String taskName = lst.getAvailability().get(key);
            return isTaskDone(taskName) ? "✓ " + taskName : taskName;
        }
        return "";
    }

    // EFFECTS: returns true if task with given name is marked as done
    private boolean isTaskDone(String taskName) {
        for (Task task : lst.getList()) {
            if (task.getName().equals(taskName) && task.getDone()) {
                return true;
            }
        }
        return false;
    }

    // EFFECTS: creates and returns non-editable calendar table with given data
    private JTable createCalendarTable(String[][] data) {
        List<String> cols = Arrays.asList("Time", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");
        return new JTable(data, cols.toArray(new String[0])) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    // MODIFIES: table
    // EFFECTS: sets up table appearance including row heights and column widths
    private void setupTableAppearance(JTable table) {
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        for (int i = 1; i < 8; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(120);
        }
    }

    // MODIFIES: calendarPanel
    // EFFECTS: adds table to panel with scroll functionality
    private void addTableToPanel(JTable table) {
        JScrollPane scrollPane = new JScrollPane(table);
        calendarPanel.add(scrollPane, BorderLayout.CENTER);
        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    // EFFECTS: returns selected day (0-6)
    private int getDaySelection() {
        String[] dayOptions = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        int choice = JOptionPane.showOptionDialog(this,
                "Select day for the task:",
                "Day Selection",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                dayOptions,
                dayOptions[0]);
        
        return choice;
    }
    
    private ImageIcon getIcon() {
        ImageIcon icon = new ImageIcon("CalendarLogo.png");
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        icon = new ImageIcon(resizedImage);
        return icon;
    }

    private void printLog() {
        EventLog eventLog = EventLog.getInstance();
        for (Event event : eventLog) {
            System.out.println(event.toString() + "\n");
        }
        eventLog.clear();
    }

    // EFFECTS: Shows time calculator interface
    private void showTimeCalculator() {
        String[] options = {"Convert 12h to 24h", "Convert 24h to 12h", "Calculate End Time", "Check Time Overlap"};
        int choice = JOptionPane.showOptionDialog(this,
                "Select time calculation:",
                "Time Calculator",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        
        switch (choice) {
            case 0:
                convert12To24();
                break;
            case 1:
                convert24To12();
                break;
            case 2:
                calculateEndTime();
                break;
            case 3:
                checkTimeOverlap();
                break;
        }
    }
    
    // EFFECTS: Converts 12-hour time to 24-hour format
    private void convert12To24() {
        String input = JOptionPane.showInputDialog(this, 
            "Enter time in 12-hour format (e.g., '2:00 PM', '10 AM'):");
        if (input == null) return;
        
        try {
            int hour24 = TimeConverter.to24HourFormat(input);
            String result = String.format("%s = %02d:00 (%d)", input, hour24, hour24);
            JOptionPane.showMessageDialog(this, result, "12h to 24h Conversion", 
                                        JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                                        "Conversion Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // EFFECTS: Converts 24-hour time to 12-hour format
    private void convert24To12() {
        String input = JOptionPane.showInputDialog(this, 
            "Enter hour (0-23):");
        if (input == null) return;
        
        try {
            int hour = Integer.parseInt(input);
            if (hour < 0 || hour > 23) {
                throw new IllegalArgumentException("Hour must be between 0 and 23");
            }
            String result = String.format("%02d:00 = %s", hour, TimeConverter.to12HourFormat(hour));
            JOptionPane.showMessageDialog(this, result, "24h to 12h Conversion", 
                                        JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number", 
                                        "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                                        "Conversion Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // EFFECTS: Calculates end time based on start time and duration
    private void calculateEndTime() {
        String startInput = JOptionPane.showInputDialog(this, 
            "Enter start time (e.g., '14', '2 PM', '14:00'):");
        if (startInput == null) return;
        
        String durationInput = JOptionPane.showInputDialog(this, 
            "Enter duration in hours:");
        if (durationInput == null) return;
        
        try {
            int startHour = TimeConverter.parseTimeInput(startInput);
            int duration = Integer.parseInt(durationInput);
            if (duration <= 0) {
                throw new IllegalArgumentException("Duration must be positive");
            }
            
            int endHour = TimeConverter.calculateEndTime(startHour, duration);
            String start12 = TimeConverter.to12HourFormat(startHour);
            String end12 = TimeConverter.to12HourFormat(endHour);
            String start24 = TimeConverter.to24HourString(startHour);
            String end24 = TimeConverter.to24HourString(endHour);
            
            String result = String.format("Start: %s (%s)\nDuration: %d hours\nEnd: %s (%s)", 
                                        start12, start24, duration, end12, end24);
            JOptionPane.showMessageDialog(this, result, "End Time Calculation", 
                                        JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                                        "Calculation Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers", 
                                        "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // EFFECTS: Checks if two time ranges overlap
    private void checkTimeOverlap() {
        String start1Input = JOptionPane.showInputDialog(this, 
            "Enter first start time (e.g., '9 AM'):");
        if (start1Input == null) return;
        
        String duration1Input = JOptionPane.showInputDialog(this, 
            "Enter first duration (hours):");
        if (duration1Input == null) return;
        
        String start2Input = JOptionPane.showInputDialog(this, 
            "Enter second start time (e.g., '10 AM'):");
        if (start2Input == null) return;
        
        String duration2Input = JOptionPane.showInputDialog(this, 
            "Enter second duration (hours):");
        if (duration2Input == null) return;
        
        try {
            int start1 = TimeConverter.parseTimeInput(start1Input);
            int duration1 = Integer.parseInt(duration1Input);
            int start2 = TimeConverter.parseTimeInput(start2Input);
            int duration2 = Integer.parseInt(duration2Input);
            
            if (duration1 <= 0 || duration2 <= 0) {
                throw new IllegalArgumentException("Durations must be positive");
            }
            
            boolean overlap = TimeConverter.hasTimeOverlap(start1, duration1, start2, duration2);
            String start1Str = TimeConverter.to12HourFormat(start1);
            String start2Str = TimeConverter.to12HourFormat(start2);
            String end1Str = TimeConverter.to12HourFormat(TimeConverter.calculateEndTime(start1, duration1));
            String end2Str = TimeConverter.to12HourFormat(TimeConverter.calculateEndTime(start2, duration2));
            
            String result = String.format("Time Range 1: %s - %s (%d hours)\nTime Range 2: %s - %s (%d hours)\n\nOverlap: %s", 
                                        start1Str, end1Str, duration1, start2Str, end2Str, duration2, 
                                        overlap ? "YES" : "NO");
            JOptionPane.showMessageDialog(this, result, "Time Overlap Check", 
                                        JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                                        "Calculation Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers", 
                                        "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
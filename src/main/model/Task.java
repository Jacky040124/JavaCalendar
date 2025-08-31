package model;

/**
 * Represents a task in the list to do or calendar
 * Each task has a name, length (duration), day of the week, and time to start
 * The task can also be marked as done or not done. 
 */

public class Task {
    private int day;
    private String name;
    private int length;
    private int time;
    private boolean done;

    public Task(String name, int length, int date, int time) {
        this.name = name;
        this.length = length;
        this.day = date;
        this.time = time;
        this.done = false;
    }
    
    // MODIFIES: this
    // EFFECTS: change the state of Done, either to true or false
    public void setDone(boolean state) {
        this.done = state;
    }

    public String getName(){
        return this.name;
    }

    public int getLength(){
        return this.length;
    }

    public int getDay(){
        return this.day;
    }

    public int getTime(){
        return this.time;
    }

    public Boolean getDone(){
        return this.done;
    }
    
    /**
     * Gets the end time of this task
     * @return End hour in 24-hour format (0-23)
     */
    public int getEndTime() {
        return TimeConverter.calculateEndTime(this.time, this.length);
    }
    
    /**
     * Gets the start time in 12-hour format
     * @return Start time in 12-hour format (e.g., "2:00 PM")
     */
    public String getStartTime12Hour() {
        return TimeConverter.to12HourFormat(this.time);
    }
    
    /**
     * Gets the end time in 12-hour format
     * @return End time in 12-hour format (e.g., "4:00 PM")
     */
    public String getEndTime12Hour() {
        return TimeConverter.to12HourFormat(getEndTime());
    }
    
    /**
     * Gets the start time in 24-hour format
     * @return Start time in 24-hour format (e.g., "14:00")
     */
    public String getStartTime24Hour() {
        return TimeConverter.to24HourString(this.time);
    }
    
    /**
     * Gets the end time in 24-hour format
     * @return End time in 24-hour format (e.g., "16:00")
     */
    public String getEndTime24Hour() {
        return TimeConverter.to24HourString(getEndTime());
    }
    
    /**
     * Gets a formatted time range string
     * @param use12Hour Whether to use 12-hour format
     * @return Formatted time range (e.g., "2:00 PM - 4:00 PM" or "14:00 - 16:00")
     */
    public String getTimeRange(boolean use12Hour) {
        if (use12Hour) {
            return getStartTime12Hour() + " - " + getEndTime12Hour();
        } else {
            return getStartTime24Hour() + " - " + getEndTime24Hour();
        }
    }
    
    /**
     * Gets the day name as a string
     * @return Day name (e.g., "Monday", "Tuesday")
     */
    public String getDayName() {
        String[] dayNames = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        if (day >= 0 && day < dayNames.length) {
            return dayNames[day];
        }
        return "Unknown";
    }
}

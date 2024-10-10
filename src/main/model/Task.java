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
    // EFFECTS: chnage the state of Done, either to true or false
    public void setDone(boolean state){
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



}

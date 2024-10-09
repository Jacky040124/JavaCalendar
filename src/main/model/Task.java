package model;


public class Task {
    private String date;
    private String name;
    private int length;
    private String time;
    private boolean done;

    public Task(String name, int length, String date, String time) {
        this.name = name;
        this.length = length;
        this.date = date;
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

    public String getDate(){
        return this.date;
    }

    public String getTime(){
        return this.time;
    }

    public Boolean getDone(){
        return this.done;
    }



}

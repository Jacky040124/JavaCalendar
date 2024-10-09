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
        // TODO
    }

    public String getName(){
        // TODO
        return "";
    }

    public int getLength(){
        // TODO
        return 0;
    }

    public String getDate(){
        // TODO
        return "";
    }

    public String getTime(){
        // TODO
        return "";
    }

    public Boolean getDone(){
        // TODO
        return false;
    }



}

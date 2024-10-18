package persistence;

import model.ListToDo;

import org.json.JSONArray;
import java.io.*;

// Represents a writer that writes JSON representation of ListToDo to file
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cant be opened for writing
    public void open() throws FileNotFoundException {
        File file = new File(destination);
        
        // Check if the directory exists, if not, throw an exception
        if (!file.getParentFile().exists()) {
            throw new FileNotFoundException("Directory does not exist: ");
        }
        
        writer = new PrintWriter(file);
    }
    

    // MODIFIES: this
    // EFFECTS: writes JSON representation of ListToDo to file
    public void write(ListToDo lst) {
        JSONArray json = lst.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}

package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

import model.ListToDo;
import model.Task;

// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads list from file and returns it;
    // throws IOException if an error occurs reading data from file
    public ListToDo read() throws IOException {
        String jsonData = readFile(source);
        JSONArray jsonArray = new JSONArray(jsonData);
        return parseWorkRoom(jsonArray);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses list from JSON object and returns it
    private ListToDo parseWorkRoom(JSONArray jsonArray) {
        ListToDo lst = new ListToDo();
        regenerateLst(lst, jsonArray);
        return lst;
    }


    // MODIFIES: lst
    // EFFECTS: parses tasks from JSON object and adds them to list
    private void regenerateLst(ListToDo lst, JSONArray jsonArray) {
        for (Object json : jsonArray) {
            JSONObject nextObject = (JSONObject) json;

            String name = nextObject.getString("name");
            int length = nextObject.getInt("length");
            int day = nextObject.getInt("day");
            int time = nextObject.getInt("time");
            boolean done = nextObject.getBoolean("done");
            Task nexTask = new Task(name,length,day,time);
            nexTask.setDone(done);

            lst.addTask(nexTask);
        }
    }

}

// package persistence;

// import java.io.IOException;
// import java.nio.charset.StandardCharsets;
// import java.nio.file.Files;
// import java.nio.file.Paths;
// import java.util.stream.Stream;

// import org.json.*;

// import model.ListToDo;
// import model.Task;

// // Represents a reader that reads workroom from JSON data stored in file
// public class JsonReader {
//     private String source;

//     // EFFECTS: constructs reader to read from source file
//     public JsonReader(String source) {
//         this.source = source;
//     }

//     // EFFECTS: reads list from file and returns it;
//     // throws IOException if an error occurs reading data from file
//     public ListToDo read() throws IOException {
//         String jsonData = readFile(source);
//         JSONObject jsonObject = new JSONObject(jsonData);
//         return parseWorkRoom(jsonObject);
//     }

//     // EFFECTS: reads source file as string and returns it
//     private String readFile(String source) throws IOException {
//         StringBuilder contentBuilder = new StringBuilder();

//         try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
//             stream.forEach(s -> contentBuilder.append(s));
//         }

//         return contentBuilder.toString();
//     }

//     // EFFECTS: parses list from JSON object and returns it
//     private ListToDo parseWorkRoom(JSONObject jsonObject) {
//         ListToDo lst = new ListToDo();
//         regenerateLst(lst, jsonObject);
//         return lst;
//     }

//     // TODO stuck here 

//     // MODIFIES: lst
//     // EFFECTS: parses tasks from JSON object and adds them to list
//     private void regenerateLst(ListToDo lst, JSONObject jsonObject) {
//         JSONArray jsonArray = jsonObject.getJSONArray("thingies");
//         for (Object json : jsonArray) {
//             JSONObject nextObject = (JSONObject) json;
//             lst.addTask(nextObject);
//         }
//     }

//     private void regenerateLst(ListToDo lst, JSONObject jsonObject) {
//         for (String key : jsonObject.keySet()) {
//             String value = jsonObject.getString(key);

//             Task task = new Task(key, value);
//             lst.addTask(task);
//         }
//     }

// }

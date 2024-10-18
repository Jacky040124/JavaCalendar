package persistence;

import model.ListToDo;
import model.Task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest {
    private Task testTask1;
    private Task testTask2;
    private ListToDo testList;
    private HashMap<String, String> testMap;

    @BeforeEach
    void runBefore() {
        testTask1 = new Task("do homework",2,1,12);
        testTask2 = new Task("eat dinner",1,2,14);
        testList = new ListToDo();
        testMap = new HashMap<>();
    }
    

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/myIllegalJson");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmpty() {
        try {
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyWorkroom.json");
            writer.open();
            writer.write(testList);
            writer.close();
            JsonReader reader = new JsonReader("./data/testWriterEmptyWorkroom.json");
            testList = reader.read();
            assertEquals(testMap,testList.getAvailability());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
    
    @Test
    void testWriterGeneralListToDo() {
        try {
            testList.addTask(testTask1);
            testList.addTask(testTask2);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralListToDo.json");
            writer.open();
            writer.write(testList);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralListToDo.json");
            testMap = reader.read();
            assertEquals(testMap,testList.getAvailability());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
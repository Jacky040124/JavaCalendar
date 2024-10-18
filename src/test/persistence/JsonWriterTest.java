package persistence;

import model.ListToDo;
import model.Task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest {
    private Task testTask1;
    private Task testTask2;
    private ListToDo testList;

    @BeforeEach
    void runBefore() {
        testTask1 = new Task("do homework",2,1,12);
        testTask2 = new Task("eat dinner",1,2,14);
        testList = new ListToDo();
    }
    

    @Test
    void testWriterNonexistentDirectory() {
        try {
            // Specify a directory that does not exist
            JsonWriter writer = new JsonWriter("./nonexistent_dir/test.json");
            writer.open();
            fail("FileNotFoundException was expected due to nonexistent parent directory");
        } catch (FileNotFoundException e) {
            // Test passes as exception is expected
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
            assertTrue(testList.getList().isEmpty());
            assertTrue(testList.getAvailability().isEmpty());
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
            testList = reader.read();

            Task loadedTask1 = testList.getList().get(0);
            assertEquals(testTask1.getName(), loadedTask1.getName());
            assertEquals(testTask1.getLength(), loadedTask1.getLength());
            assertEquals(testTask1.getDay(), loadedTask1.getDay());
            assertEquals(testTask1.getTime(), loadedTask1.getTime());
            assertEquals(testTask1.getDone(), loadedTask1.getDone());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
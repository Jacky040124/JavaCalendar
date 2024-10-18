package persistence;

import model.ListToDo;
import model.Task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
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
    
            Task loadedTask2 = testList.getList().get(1);
            assertEquals(testTask2.getName(), loadedTask2.getName());
            assertEquals(testTask2.getLength(), loadedTask2.getLength());
            assertEquals(testTask2.getDay(), loadedTask2.getDay());
            assertEquals(testTask2.getTime(), loadedTask2.getTime());
            assertEquals(testTask2.getDone(), loadedTask2.getDone());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
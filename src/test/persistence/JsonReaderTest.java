package persistence;

import model.ListToDo;
import model.Task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest {
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
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            ListToDo lst = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyListToDo() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyListToDo.json");
        try {
            ListToDo testList = reader.read();
            assertTrue(testList.getList().isEmpty());
            assertTrue(testList.getAvailability().isEmpty());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralListToDo() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralListToDo.json");
        try {
            ListToDo testList = reader.read();
    
            List<Task> tasks = testList.getList();
            assertEquals(2, tasks.size());
    
            Task task1 = tasks.get(0);
            assertEquals("do homework", task1.getName());
            assertEquals(2, task1.getLength());
            assertEquals(1, task1.getDay());
            assertEquals(12, task1.getTime());
            assertFalse(task1.getDone());
    
            Task task2 = tasks.get(1);
            assertEquals("eat dinner", task2.getName());
            assertEquals(1, task2.getLength());
            assertEquals(2, task2.getDay());
            assertEquals(14, task2.getTime());
            assertFalse(task2.getDone());
    
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
    
}
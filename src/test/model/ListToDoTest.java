package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;

public class ListToDoTest {
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
    void testConstructor() {
        assertEquals(0,testList.getList().size());
    }

    @Test
    void testAddTask() {
        testList.addTask(testTask1);
        assertEquals(1,testList.getList().size());
        testList.addTask(testTask2);
        testList.addTask(testTask2);
        assertEquals(3,testList.getList().size());
    }

    @Test
    void testRemoveTask() {
        testList.addTask(testTask1);
        testList.removeTask(testTask1);
        assertEquals(0,testList.getList().size());
        testList.addTask(testTask1);
        testList.addTask(testTask2);
        testList.removeTask(testTask2);
        assertEquals(1,testList.getList().size());
        testList.removeTask(testTask1);
        assertEquals(0,testList.getList().size());
    }

    @Test
    void testGetAvailability() {
        HashMap<String, String> availability = new HashMap<>();
        assertEquals(availability, testList.getAvailability());
    }
    
}

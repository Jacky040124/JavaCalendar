package model;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.model.ListToDo;

public class ListToDoTest {
    private Task testTask1;
    private Task testTask2;
    private ListToDo testList;
    
    @BeforeEach
    void runBefore() {
        testTask1 = new Task("do homework",2,"10/10/2024","12:00");
        testTask2 = new Task("eat dinner",1,"10/10/2024","14:00");
        testList = new ListToDo();
    }

    @Test
    void testConstructor() {
        assertEqual(0,testList.size());
    }

    @Test
    void testAddTask() {
        testList.addTask(testTask1)
        assertEqual(1,testList.size())
        testList.addTask(testTask2)
        testList.addTask(testTask2)
        assertEqual(3,testList.size())
    }

    @Test
    void testRemoveTask() {
        testList.addTask(testTask1)
        testList.removeTask(testTask1)
        assertEqual(0,testList.size())
        testList.addTask(testTask1)
        testList.addTask(testTask2)
        testList.removeTask(testTask2)
        assertEqual(1,testList.size())
        testList.removeTask(testTask1)
        assertEqual(0,testList.size())
    }
}

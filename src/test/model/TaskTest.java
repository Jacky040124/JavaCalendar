package model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TaskTest {
    private Task testTask1;
    private Task testTask2;
    
    @BeforeEach
    void runBefore() {
        testTask1 = new Task("do homework",2,"10/10/2024","12:00");
        testTask2 = new Task("eat dinner",1,"10/10/2024","14:00");
    }

    @Test
    void testConstructor() {
        assertFalse(testTask1.getDone);
        assertEqual("do homework",testTask1.getName())
        assertEqual(2,testTask1.getLength())
        assertEqual("10/10/2024",testTask1.getDate())
        assertEqual("12:00",testTask1.getTime())

        assertFalse(testTask1.getDone);
        assertEqual("eat dinner",testTask1.getName())
        assertEqual(1,testTask1.getLength())
        assertEqual("10/10/2024",testTask1.getDate())
        assertEqual("14:00",testTask1.getTime())
    }

    @Test
    void testSetDone() {
        testTask1.setDone(true)
        assertTrue(testTask1.getDone);
        testTask1.setDone(false)
        assertFalse(testTask1.getDone);
        testTask1.setDone(false)
        assertFalse(testTask1.getDone);
    }


}

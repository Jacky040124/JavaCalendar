package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertFalse(testTask1.getDone());
        assertEquals("do homework",testTask1.getName());
        assertEquals(2,testTask1.getLength());
        assertEquals("10/10/2024",testTask1.getDate());
        assertEquals("12:00",testTask1.getTime());

        assertFalse(testTask2.getDone());
        assertEquals("eat dinner",testTask2.getName());
        assertEquals(1,testTask2.getLength());
        assertEquals("10/10/2024",testTask2.getDate());
        assertEquals("14:00",testTask2.getTime());
    }

    @Test
    void testSetDone() {
        testTask1.setDone(true);
        assertTrue(testTask1.getDone());
        testTask1.setDone(false);
        assertFalse(testTask1.getDone());
        testTask1.setDone(false);
        assertFalse(testTask1.getDone());
    }


}

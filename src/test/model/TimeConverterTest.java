package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for TimeConverter utility class
 */
public class TimeConverterTest {
    
    @Test
    public void testTo12HourFormat() {
        assertEquals("12:00 AM", TimeConverter.to12HourFormat(0));
        assertEquals("1:00 AM", TimeConverter.to12HourFormat(1));
        assertEquals("12:00 PM", TimeConverter.to12HourFormat(12));
        assertEquals("1:00 PM", TimeConverter.to12HourFormat(13));
        assertEquals("11:00 PM", TimeConverter.to12HourFormat(23));
    }
    
    @Test
    public void testTo24HourFormat() {
        assertEquals(0, TimeConverter.to24HourFormat("12 AM"));
        assertEquals(1, TimeConverter.to24HourFormat("1 AM"));
        assertEquals(12, TimeConverter.to24HourFormat("12 PM"));
        assertEquals(13, TimeConverter.to24HourFormat("1 PM"));
        assertEquals(23, TimeConverter.to24HourFormat("11 PM"));
        assertEquals(14, TimeConverter.to24HourFormat("2:00 PM"));
        assertEquals(9, TimeConverter.to24HourFormat("9:00 AM"));
    }
    
    @Test
    public void testTo24HourString() {
        assertEquals("00:00", TimeConverter.to24HourString(0));
        assertEquals("12:00", TimeConverter.to24HourString(12));
        assertEquals("23:00", TimeConverter.to24HourString(23));
    }
    
    @Test
    public void testParseTimeInput() {
        assertEquals(14, TimeConverter.parseTimeInput("14"));
        assertEquals(14, TimeConverter.parseTimeInput("2 PM"));
        assertEquals(14, TimeConverter.parseTimeInput("2:00 PM"));
        assertEquals(9, TimeConverter.parseTimeInput("9 AM"));
        assertEquals(0, TimeConverter.parseTimeInput("12 AM"));
        assertEquals(12, TimeConverter.parseTimeInput("12 PM"));
    }
    
    @Test
    public void testCalculateEndTime() {
        assertEquals(16, TimeConverter.calculateEndTime(14, 2));
        assertEquals(0, TimeConverter.calculateEndTime(23, 1));
        assertEquals(1, TimeConverter.calculateEndTime(23, 2));
        assertEquals(12, TimeConverter.calculateEndTime(10, 2));
    }
    
    @Test
    public void testHasTimeOverlap() {
        // No overlap
        assertFalse(TimeConverter.hasTimeOverlap(9, 2, 12, 2));
        assertFalse(TimeConverter.hasTimeOverlap(12, 2, 9, 2));
        
        // Overlap
        assertTrue(TimeConverter.hasTimeOverlap(9, 3, 10, 2));
        assertTrue(TimeConverter.hasTimeOverlap(10, 2, 9, 3));
        
        // Adjacent (no overlap)
        assertFalse(TimeConverter.hasTimeOverlap(9, 2, 11, 2));
        
        // Spanning midnight
        assertTrue(TimeConverter.hasTimeOverlap(23, 2, 0, 1));
        assertTrue(TimeConverter.hasTimeOverlap(0, 1, 23, 2));
    }
    
    @Test
    public void testInvalidInputs() {
        // Invalid hours
        assertThrows(IllegalArgumentException.class, () -> TimeConverter.to12HourFormat(-1));
        assertThrows(IllegalArgumentException.class, () -> TimeConverter.to12HourFormat(24));
        
        // Invalid time strings
        assertThrows(IllegalArgumentException.class, () -> TimeConverter.to24HourFormat(""));
        assertThrows(IllegalArgumentException.class, () -> TimeConverter.to24HourFormat("25 AM"));
        assertThrows(IllegalArgumentException.class, () -> TimeConverter.to24HourFormat("13 PM"));
        
        // Invalid durations
        assertThrows(IllegalArgumentException.class, () -> TimeConverter.calculateEndTime(10, 0));
        assertThrows(IllegalArgumentException.class, () -> TimeConverter.calculateEndTime(10, -1));
    }
}
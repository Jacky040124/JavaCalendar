package model;

/**
 * Utility class for time format conversions and time-related calculations.
 * Provides methods to convert between 12-hour and 24-hour formats,
 * parse time strings, and format time for display.
 */
public class TimeConverter {
    
    /**
     * Converts 24-hour time (0-23) to 12-hour format with AM/PM
     * @param hour24 The hour in 24-hour format (0-23)
     * @return Formatted string in 12-hour format (e.g., "2:00 PM")
     */
    public static String to12HourFormat(int hour24) {
        if (hour24 < 0 || hour24 > 23) {
            throw new IllegalArgumentException("Hour must be between 0 and 23");
        }
        
        if (hour24 == 0) {
            return "12:00 AM";
        } else if (hour24 == 12) {
            return "12:00 PM";
        } else if (hour24 < 12) {
            return hour24 + ":00 AM";
        } else {
            return (hour24 - 12) + ":00 PM";
        }
    }
    
    /**
     * Converts 12-hour time string to 24-hour format
     * @param time12Hour Time string in 12-hour format (e.g., "2:00 PM", "10:30 AM")
     * @return Hour in 24-hour format (0-23)
     */
    public static int to24HourFormat(String time12Hour) {
        if (time12Hour == null || time12Hour.trim().isEmpty()) {
            throw new IllegalArgumentException("Time string cannot be null or empty");
        }
        
        String time = time12Hour.trim().toUpperCase();
        
        // Handle formats like "2:00 PM", "10:30 AM", "2 PM", "10 AM"
        boolean isPM = time.contains("PM");
        boolean isAM = time.contains("AM");
        
        if (!isAM && !isPM) {
            throw new IllegalArgumentException("Time must include AM or PM");
        }
        
        // Extract the hour part
        String hourStr = time.replaceAll("[^0-9]", "");
        if (hourStr.isEmpty()) {
            throw new IllegalArgumentException("Invalid time format: no hour found");
        }
        
        int hour = Integer.parseInt(hourStr);
        
        if (hour < 1 || hour > 12) {
            throw new IllegalArgumentException("Hour must be between 1 and 12");
        }
        
        // Convert to 24-hour format
        if (isPM && hour != 12) {
            hour += 12;
        } else if (isAM && hour == 12) {
            hour = 0;
        }
        
        // Validate the result
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("Invalid time: " + time12Hour + " results in hour " + hour);
        }
        
        return hour;
    }
    
    /**
     * Converts 24-hour time to a more readable format
     * @param hour24 The hour in 24-hour format (0-23)
     * @return Formatted string (e.g., "14:00" for 2 PM)
     */
    public static String to24HourString(int hour24) {
        if (hour24 < 0 || hour24 > 23) {
            throw new IllegalArgumentException("Hour must be between 0 and 23");
        }
        
        return String.format("%02d:00", hour24);
    }
    
    /**
     * Converts 24-hour time to a user-friendly display format
     * @param hour24 The hour in 24-hour format (0-23)
     * @return User-friendly string (e.g., "2:00 PM" or "14:00")
     */
    public static String toDisplayFormat(int hour24, boolean use12Hour) {
        if (use12Hour) {
            return to12HourFormat(hour24);
        } else {
            return to24HourString(hour24);
        }
    }
    
    /**
     * Parses various time input formats and returns 24-hour hour
     * @param timeInput Time input string (can be "14", "2 PM", "14:00", etc.)
     * @return Hour in 24-hour format (0-23)
     */
    public static int parseTimeInput(String timeInput) {
        if (timeInput == null || timeInput.trim().isEmpty()) {
            throw new IllegalArgumentException("Time input cannot be null or empty");
        }
        
        String time = timeInput.trim();
        
        // If it's already a number (0-23), return it
        try {
            int hour = Integer.parseInt(time);
            if (hour >= 0 && hour <= 23) {
                return hour;
            }
        } catch (NumberFormatException e) {
            // Not a number, continue to other parsing methods
        }
        
        // Try to parse as 12-hour format
        try {
            return to24HourFormat(time);
        } catch (IllegalArgumentException e) {
            // Try to parse as "HH:MM" format
            if (time.contains(":")) {
                String[] parts = time.split(":");
                if (parts.length == 2) {
                    try {
                        int hour = Integer.parseInt(parts[0]);
                        if (hour >= 0 && hour <= 23) {
                            return hour;
                        }
                    } catch (NumberFormatException ex) {
                        // Invalid format
                    }
                }
            }
        }
        
        throw new IllegalArgumentException("Unable to parse time input: " + timeInput + 
                                        ". Please use format like '14', '2 PM', or '14:00'");
    }
    
    /**
     * Calculates the end time of a task based on start time and duration
     * @param startHour Start hour in 24-hour format (0-23)
     * @param duration Duration in hours
     * @return End hour in 24-hour format (0-23)
     */
    public static int calculateEndTime(int startHour, int duration) {
        if (startHour < 0 || startHour > 23) {
            throw new IllegalArgumentException("Start hour must be between 0 and 23");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("Duration must be positive");
        }
        
        int endHour = startHour + duration;
        
        // Handle overflow to next day (wrap around to 0)
        if (endHour >= 24) {
            endHour = endHour % 24;
        }
        
        return endHour;
    }
    
    /**
     * Checks if a time range overlaps with another time range
     * @param start1 Start time of first range
     * @param duration1 Duration of first range
     * @param start2 Start time of second range
     * @param duration2 Duration of second range
     * @return true if ranges overlap, false otherwise
     */
    public static boolean hasTimeOverlap(int start1, int duration1, int start2, int duration2) {
        int end1 = calculateEndTime(start1, duration1);
        int end2 = calculateEndTime(start2, duration2);
        
        // Handle cases where tasks span midnight
        if (end1 < start1) { // First task spans midnight
            return start2 < end1 || start2 < start1;
        } else if (end2 < start2) { // Second task spans midnight
            return start1 < end2 || start1 < start2;
        } else { // No tasks span midnight
            return start1 < end2 && start2 < end1;
        }
    }
}
# *Project Proposal*

## *Project Idea*

I am going to build something that combines a calendar and a task manager. 

## What will the application do?

The application has two main features. First, the sidebar allows users to input tasks they need to complete. Unlike a common to-do list, when users input tasks in the sidebar, they must include a date, ensuring that daily tasks are presented on the sidebar each day. The second feature is a calendar view, similar to Google Calendar. What I really want to emphasize is the interaction between these two components, where users can drag and drop tasks into the calendar, making time-blocking much more intuitive.

## Who will use it?

When it comes to time management/productivity apps, there are two types of people: those who want to keep it simple and just use Google Calendar for everything, and those who geek out on Notion, spending more time managing their time than actually doing what needs to be done. I aim to offer something in the middle—providing a bit more functionality while keeping it simple.

## Why is this project of interest to you?

Well, I am really interested in time management software myself, and I've been using Notion daily for the past five years. While it offers a lot of functionality for an all-in-one experience, realistically, I don't need much of it. Based on my observation, many of my friends don't seem to use those features either. So, I'm aiming to build something that hits the sweet spot between simplicity and functionality—something I would actually use myself.

## *User Story*

- As a user, I want to add tasks to my to-do-list

- As a user, I watn to remove taks from my to-do-list

- As a user, I want to assign a date and time to my tasks

- As a user, I want to be able to see my the tasks in need to do in a calendar view

- As a user, I want to have my task sync to my calendar without me having to manually enter it

- As a user, I want to be able to save my list to do. (if I choose to do so)

- As a user, I want to be able to access the list to do I saved previous everytime I login to the app. (if I choose to do so)

## User Instructions

### Managing Tasks
- To add a task:
  1. Click "Add Task"
  2. Enter name
  3. Enter length in hours 
  4. Select day from dropdown (Monday-Sunday)
  5. Enter time (multiple formats supported):
     - 24-hour format: 16 (for 4PM)
     - 12-hour format: 4 PM, 2:00 PM
     - HH:MM format: 14:00

- To mark a task as done:
  1. Click "Mark Done" button
  2. Select task
  3. Task will appear with a ✓ if successful

- To remove a task:
  1. Click "Remove Task" button
  2. Select task to remove

### Time Calculator Features
- **Convert 12h to 24h**: Convert times like "2:00 PM" to "14:00"
- **Convert 24h to 12h**: Convert times like "14" to "2:00 PM"
- **Calculate End Time**: Find when a task ends based on start time and duration
- **Check Time Overlap**: Determine if two time ranges conflict

### Visual Component
The calendar logo appears in the top-right corner.

### Enhanced Display Features
- **Dual Time Format**: Calendar shows both 24-hour and 12-hour formats (e.g., "14:00 (2:00 PM)")
- **Better Task Information**: Task list shows day names, time ranges, and completion status
- **Improved Day Selection**: Dropdown menu for selecting days instead of entering numbers
- **Time Range Display**: Tasks show start and end times in readable format

### Saving and Loading
- To save your current tasks:
  1. Click "Save" button
  2. Your tasks will be saved

- To load previously saved tasks:
  1. Click "Load" button
  2. Your previously saved tasks will be restored

### Possbile Refactoring to improve
Have a seperate class call ErrorHandle to centralise error handling thus makes the program more robust and maintainable. 

## Acknowledgments
Part of this project utilizes codes based on  [JsonSerializationDemo](https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo) provided by CS210.
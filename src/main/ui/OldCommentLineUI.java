// package ui;

// import model.Task;

// public class OldCommentLineUI {

//     // MODIFIES: this
//     // EFFECTS: Continuously accepts user commands until the user chooses to quit.
//     private void runCalendar() {
//         boolean continueRunning = true;
//         String command = null;
//         while (continueRunning) {
//             displayCalendar();
//             System.out.println("a : add Task | m : mark task done | r : remove task | s : save task | l : load task");
//             command = input.next().toLowerCase();
//             if (command.equals("q")) {
//                 continueRunning = false;
//             } else {
//                 run(command);
//             }
//         }
//         System.out.println("Thank you");
//     } 

//     // EFFECTS: Prints the calendar view with time slots and tasks for each day of the week.
//     public void displayCalendar() {
//         System.out.println("Time |Monday    |Tuesday   |Wednesday |Thuresday |Friday    |Saturday  |Sunday    ");
//         System.out.println("----------------------------------------------------------------------------------");

//         for (int i = 0; i <= 24; i++) { 
//             String strToPrint = String.format("%02d:00", i);
//             for (int j = 0; j < 7; j++) {
//                 String key = Integer.toString(j) + ":" + Integer.toString(i);
//                 if (lst.getAvailability().containsKey(key)) {
//                     String taskName = lst.getAvailability().get(key);

//                     boolean isDone = false;
//                     for (Task task : lst.getList()) {
//                         if (task.getName().equals(taskName) && task.getDone()) {
//                             isDone = true;
//                             break;
//                         }
//                     }

//                     if (isDone) {
//                         String crossedOut = taskName.chars()
//                                 .mapToObj(ch -> (char) ch + "\u0336")
//                                 .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
//                                 .toString();
//                         strToPrint += String.format("|%-10s", crossedOut);
//                     } else {
//                         strToPrint += String.format("|%-10s", taskName);
//                     }
                    
//                 } else {
//                     strToPrint += "|          ";
//                 }
//             }
//             System.out.println(strToPrint);
//         }
//     }

//     // MODIFIES: this
//     // EFFECTS: Executes the user's command, 'a' to add a task, 'm' to mark a task as done, 'r' to remove a task
//     public void run(String command) {
//         switch (command) {
//             case "a":
//                 addingTask();
//                 break;
//             case "m":
//                 markTaskDone();
//                 break;
//             case "r":
//                 removeTask();
//                 break;
//             case "s":
//                 saveListToDo();
//                 break;
//             case "l":
//                 loadListToDo();
//                 break;
//             default:
//                 System.out.println("Invalid Input");
//                 break;
//         }
//     }

//     // EFFECTS: Displays the list of tasks.
//     public void displayList() {
//         for (int i = 0; i < lst.getList().size();i++) { 
//             System.out.println(i + "|" + lst.getList().get(i).getName());
//         }
//     }
// }

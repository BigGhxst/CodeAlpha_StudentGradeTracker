import java.util.ArrayList;
import java.util.Scanner;

public class StudentGradeTracker {
    // Instance variables
    private ArrayList<Student> students;
    private Scanner scanner;

    // Constructor - initializes the lists
    public StudentGradeTracker() {
        students = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    // ===== MAIN METHOD - PROGRAM ENTRY POINT =====
    public static void main(String[] args) {
        // Create an instance of the tracker
        StudentGradeTracker tracker = new StudentGradeTracker();
        // Start the program
        tracker.run();
    }
    // ============================================

    // Main program loop
    public void run() {
        while (true) {
            System.out.println("\n========== STUDENT GRADE TRACKER ==========");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. View Summary Report");
            System.out.println("4. Update Grade");
            System.out.println("5. Remove Student");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    viewAllStudents();
                    break;
                case 3:
                    viewSummaryReport();
                    break;
                case 4:
                    updateGrade();
                    break;
                case 5:
                    removeStudent();
                    break;
                case 6:
                    System.out.println("Exiting... Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // ===== ALL THE METHODS BELOW =====

    // Add a new student
    private void addStudent() {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();

        System.out.print("Enter grade (0-100): ");
        double grade = scanner.nextDouble();
        scanner.nextLine();

        students.add(new Student(name, grade));
        System.out.println(" Student added successfully!");
    }

    // View all students
    private void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students recorded.");
            return;
        }

        System.out.println("\n========== STUDENT LIST ==========");
        for (int i = 0; i < students.size(); i++) {
            System.out.println((i + 1) + ". " + students.get(i));
        }
    }

    // View summary report with average, highest, lowest
    private void viewSummaryReport() {
        if (students.isEmpty()) {
            System.out.println("No students to generate report.");
            return;
        }

        double total = 0;
        double highest = Double.MIN_VALUE;
        double lowest = Double.MAX_VALUE;
        String highestStudent = "";
        String lowestStudent = "";

        for (Student s : students) {
            double grade = s.getGrade();
            total += grade;

            if (grade > highest) {
                highest = grade;
                highestStudent = s.getName();
            }
            if (grade < lowest) {
                lowest = grade;
                lowestStudent = s.getName();
            }
        }

        double average = total / students.size();

        System.out.println("\n========== SUMMARY REPORT ==========");
        System.out.println("Total Students: " + students.size());
        System.out.printf("Average Score: %.2f\n", average);
        System.out.println("Highest Score: " + highest + " (by " + highestStudent + ")");
        System.out.println(" Lowest Score: " + lowest + " (by " + lowestStudent + ")");

        // Grade distribution
        System.out.println("\n📋 Grade Distribution:");
        int a = 0, b = 0, c = 0, d = 0, f = 0;
        for (Student s : students) {
            double g = s.getGrade();
            if (g >= 90) a++;
            else if (g >= 80) b++;
            else if (g >= 70) c++;
            else if (g >= 60) d++;
            else f++;
        }
        System.out.println("  A (90-100): " + a);
        System.out.println("  B (80-89):  " + b);
        System.out.println("  C (70-79):  " + c);
        System.out.println("  D (60-69):  " + d);
        System.out.println("  F (Below 60): " + f);
    }

    // Update a student's grade
    private void updateGrade() {
        if (students.isEmpty()) {
            System.out.println("No students to update.");
            return;
        }

        viewAllStudents();
        System.out.print("Enter student number to update: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine();

        if (index >= 0 && index < students.size()) {
            System.out.print("Enter new grade (0-100): ");
            double newGrade = scanner.nextDouble();
            scanner.nextLine();
            students.get(index).setGrade(newGrade);
            System.out.println(" Grade updated successfully!");
        } else {
            System.out.println("Invalid student number.");
        }
    }

    // Remove a student
    private void removeStudent() {
        if (students.isEmpty()) {
            System.out.println("No students to remove.");
            return;
        }

        viewAllStudents();
        System.out.print("Enter student number to remove: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine();

        if (index >= 0 && index < students.size()) {
            Student removed = students.remove(index);
            System.out.println(" Removed: " + removed.getName());
        } else {
            System.out.println("Invalid student number.");
        }
    }
}
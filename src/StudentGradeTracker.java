import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Collections;
import java.util.Comparator;

public class StudentGradeTracker {
    // Instance variables
    private ArrayList<Student> students;
    private Scanner scanner;
    private static final String DATA_FILE = "student_data.txt";
    private static final String REPORT_FILE = "grade_report.txt";

    // Constructor - initializes the lists and loads saved data
    public StudentGradeTracker() {
        students = new ArrayList<>();
        scanner = new Scanner(System.in);
        loadFromFile(); // Auto-load saved data on startup
    }

    // ===== MAIN METHOD - PROGRAM ENTRY POINT =====
    public static void main(String[] args) {
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║     📚 WELCOME TO STUDENT GRADE TRACKER SYSTEM     ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        StudentGradeTracker tracker = new StudentGradeTracker();
        tracker.run();
    }

    // ===== MAIN PROGRAM LOOP =====
    public void run() {
        while (true) {
            displayMenu();
            int choice = getValidatedIntInput("Choose an option: ", 1, 9);

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
                    searchStudent();
                    break;
                case 7:
                    sortStudents();
                    break;
                case 8:
                    exportReport();
                    break;
                case 9:
                    if (confirmAction("Are you sure you want to exit?")) {
                        saveToFile();
                        System.out.println("\n✅ Data saved successfully!");
                        System.out.println("👋 Thank you for using Student Grade Tracker!");
                        System.out.println("   Goodbye!");
                        scanner.close();
                        System.exit(0);
                    }
                    break;
                default:
                    System.out.println("❌ Invalid choice. Please try again.");
            }
        }
    }

    // ===== DISPLAY MENU =====
    private void displayMenu() {
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║              STUDENT GRADE TRACKER                  ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.println("║  📊 Total Students: " + String.format("%-3d", students.size()) + "                         ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.println("║  1. ➕ Add Student                                  ║");
        System.out.println("║  2. 📋 View All Students                           ║");
        System.out.println("║  3. 📊 View Summary Report                         ║");
        System.out.println("║  4. ✏️  Update Grade                                ║");
        System.out.println("║  5. 🗑️  Remove Student                             ║");
        System.out.println("║  6. 🔍 Search Student                             ║");
        System.out.println("║  7. 🔀 Sort Students                              ║");
        System.out.println("║  8. 💾 Export Report                              ║");
        System.out.println("║  9. 🚪 Exit                                       ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println("\n📌 Grading System:");
        System.out.println("   🟢 75-100 = Distinction (PD)");
        System.out.println("   🟡 50-74  = Pass (P)");
        System.out.println("   🔴 0-49   = Fail (F)");
    }

    // ===== 1. ADD STUDENT WITH VALIDATION =====
    private void addStudent() {
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║              ➕ ADD NEW STUDENT                     ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        // Validate name
        String name;
        while (true) {
            System.out.print("👤 Enter student name: ");
            name = scanner.nextLine().trim();

            if (name.isEmpty()) {
                System.out.println("❌ Name cannot be empty! Please try again.");
                continue;
            }

            // Check for duplicate names
            if (isDuplicateName(name)) {
                System.out.println("⚠️  A student with this name already exists!");
                System.out.println("   Use a different name or update the existing one.");
                continue;
            }

            break;
        }

        // Validate grade
        double grade;
        while (true) {
            System.out.print("📝 Enter grade (0-100): ");
            String input = scanner.nextLine().trim();

            try {
                grade = Double.parseDouble(input);
                if (grade < 0 || grade > 100) {
                    System.out.println("❌ Grade must be between 0 and 100!");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input! Please enter a valid number.");
            }
        }

        // Add student
        Student newStudent = new Student(name, grade);
        students.add(newStudent);
        saveToFile(); // Auto-save

        System.out.println("\n✅ Student added successfully!");
        System.out.println("   ┌─────────────────────────────────────");
        System.out.println("   │ Name : " + name);
        System.out.println("   │ Grade: " + grade + " (" + newStudent.getLetterGrade() + ")");
        System.out.println("   │ Status: " + newStudent.getGradeDescription());
        System.out.println("   └─────────────────────────────────────");
        System.out.println("   📊 Total Students: " + students.size());
    }

    // ===== 2. VIEW ALL STUDENTS =====
    private void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("\n📭 No students recorded yet.");
            System.out.println("   Use option 1 to add students.");
            return;
        }

        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║              📋 STUDENT LIST                        ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");

        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            String gradeDisplay = String.format("%.1f", s.getGrade());
            String letter = s.getLetterGrade();
            String statusIcon = letter.equals("PD") ? "🟢" : (letter.equals("P") ? "🟡" : "🔴");
            System.out.printf("║ %2d. %-20s  %5s  %s%-2s  ║%n",
                    (i + 1), s.getName(), gradeDisplay, statusIcon, letter);
        }

        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.println("║  📊 Total: " + String.format("%-3d", students.size()) + " students                          ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
    }

    // ===== 3. VIEW SUMMARY REPORT =====
    private void viewSummaryReport() {
        if (students.isEmpty()) {
            System.out.println("\n📭 No students to generate report.");
            System.out.println("   Use option 1 to add students first.");
            return;
        }

        // Calculate statistics
        double total = 0;
        double highest = Double.MIN_VALUE;
        double lowest = Double.MAX_VALUE;
        String highestStudent = "";
        String lowestStudent = "";
        int pd = 0, p = 0, f = 0;

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

            // Grade distribution based on new system
            if (grade >= 75) pd++;
            else if (grade >= 50) p++;
            else f++;
        }

        double average = total / students.size();
        double median = calculateMedian();

        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║              📊 SUMMARY REPORT                      ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.println("║  📌 Total Students   : " + String.format("%-3d", students.size()) + "                        ║");
        System.out.printf(  "║  📈 Average Score    : %.2f                          ║%n", average);
        System.out.printf(  "║  📊 Median Score     : %.2f                          ║%n", median);
        System.out.println("║  🥇 Highest Score    : " + String.format("%.1f", highest) + " (by " + highestStudent + ")" + " ".repeat(Math.max(0, 25 - highestStudent.length())) + "║");
        System.out.println("║  🥉 Lowest Score     : " + String.format("%.1f", lowest) + " (by " + lowestStudent + ")" + " ".repeat(Math.max(0, 25 - lowestStudent.length())) + "║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.println("║  📋 GRADE DISTRIBUTION                             ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.println("║  🟢 Distinction (PD): " + String.format("%-3d", pd) + " students  " + generateBar(pd, getMaxCount(pd, p, f)) + " ║");
        System.out.println("║  🟡 Pass (P)       : " + String.format("%-3d", p) + " students  " + generateBar(p, getMaxCount(pd, p, f)) + " ║");
        System.out.println("║  🔴 Fail (F)        : " + String.format("%-3d", f) + " students  " + generateBar(f, getMaxCount(pd, p, f)) + " ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        // Show grade statistics summary
        System.out.println("\n📊 Grade Statistics Summary:");
        System.out.println("   ┌─────────────────────────────────────────────────────");
        System.out.printf("   │ 🟢 %-12s: %d students (%.1f%%)\n", "Distinction", pd, (pd * 100.0 / students.size()));
        System.out.printf("   │ 🟡 %-12s: %d students (%.1f%%)\n", "Pass", p, (p * 100.0 / students.size()));
        System.out.printf("   │ 🔴 %-12s: %d students (%.1f%%)\n", "Fail", f, (f * 100.0 / students.size()));
        System.out.println("   └─────────────────────────────────────────────────────");
    }

    // ===== 4. UPDATE GRADE =====
    private void updateGrade() {
        if (students.isEmpty()) {
            System.out.println("\n📭 No students to update.");
            return;
        }

        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║              ✏️  UPDATE GRADE                        ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        viewAllStudents();
        System.out.println("\n📝 Enter the number of the student to update (0 to cancel):");

        int index = getValidatedIntInput("Student number: ", 0, students.size());
        if (index == 0) {
            System.out.println("❌ Update cancelled.");
            return;
        }

        int studentIndex = index - 1;
        Student student = students.get(studentIndex);

        System.out.println("\n📊 Current details:");
        System.out.println("   Name : " + student.getName());
        System.out.println("   Grade: " + student.getGrade() + " (" + student.getLetterGrade() + ")");
        System.out.println("   Status: " + student.getGradeDescription());

        double newGrade;
        while (true) {
            System.out.print("\n📝 Enter new grade (0-100): ");
            String input = scanner.nextLine().trim();

            try {
                newGrade = Double.parseDouble(input);
                if (newGrade < 0 || newGrade > 100) {
                    System.out.println("❌ Grade must be between 0 and 100!");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input! Please enter a valid number.");
            }
        }

        student.setGrade(newGrade);
        saveToFile();

        System.out.println("\n✅ Grade updated successfully!");
        System.out.println("   Name : " + student.getName());
        System.out.println("   New Grade: " + newGrade + " (" + student.getLetterGrade() + ")");
        System.out.println("   New Status: " + student.getGradeDescription());
    }

    // ===== 5. REMOVE STUDENT =====
    private void removeStudent() {
        if (students.isEmpty()) {
            System.out.println("\n📭 No students to remove.");
            return;
        }

        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║              🗑️  REMOVE STUDENT                     ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        viewAllStudents();
        System.out.println("\n📝 Enter the number of the student to remove (0 to cancel):");

        int index = getValidatedIntInput("Student number: ", 0, students.size());
        if (index == 0) {
            System.out.println("❌ Removal cancelled.");
            return;
        }

        int studentIndex = index - 1;
        Student student = students.get(studentIndex);

        System.out.println("\n⚠️  You are about to remove:");
        System.out.println("   Name : " + student.getName());
        System.out.println("   Grade: " + student.getGrade() + " (" + student.getLetterGrade() + ")");

        if (confirmAction("Are you sure you want to remove this student?")) {
            students.remove(studentIndex);
            saveToFile();
            System.out.println("\n✅ Student removed successfully!");
            System.out.println("   Removed: " + student.getName());
            System.out.println("   📊 Total Students: " + students.size());
        } else {
            System.out.println("❌ Removal cancelled.");
        }
    }

    // ===== 6. SEARCH STUDENT =====
    private void searchStudent() {
        if (students.isEmpty()) {
            System.out.println("\n📭 No students to search.");
            return;
        }

        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║              🔍 SEARCH STUDENT                      ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        System.out.print("🔍 Enter name to search (or part of name): ");
        String searchTerm = scanner.nextLine().trim().toLowerCase();

        if (searchTerm.isEmpty()) {
            System.out.println("❌ Search term cannot be empty!");
            return;
        }

        ArrayList<Student> results = new ArrayList<>();
        for (Student s : students) {
            if (s.getName().toLowerCase().contains(searchTerm)) {
                results.add(s);
            }
        }

        if (results.isEmpty()) {
            System.out.println("\n❌ No students found matching: " + searchTerm);
            return;
        }

        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║              🔍 SEARCH RESULTS                      ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.println("║  Found " + results.size() + " student(s) matching: " + searchTerm + " ".repeat(Math.max(0, 25 - searchTerm.length())) + "║");
        System.out.println("╠══════════════════════════════════════════════════════╣");

        for (int i = 0; i < results.size(); i++) {
            Student s = results.get(i);
            String statusIcon = s.getLetterGrade().equals("PD") ? "🟢" : (s.getLetterGrade().equals("P") ? "🟡" : "🔴");
            System.out.printf("║ %2d. %-20s  %5.1f  %s%-2s  ║%n",
                    (i + 1), s.getName(), s.getGrade(), statusIcon, s.getLetterGrade());
        }
        System.out.println("╚══════════════════════════════════════════════════════╝");
    }

    // ===== 7. SORT STUDENTS =====
    private void sortStudents() {
        if (students.isEmpty()) {
            System.out.println("\n📭 No students to sort.");
            return;
        }

        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║              🔀 SORT STUDENTS                       ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println("  1. Sort by Name (A → Z)");
        System.out.println("  2. Sort by Name (Z → A)");
        System.out.println("  3. Sort by Grade (High → Low)");
        System.out.println("  4. Sort by Grade (Low → High)");
        System.out.println("  5. Sort by Status (PD → P → F)");
        System.out.println("  0. Cancel");

        int choice = getValidatedIntInput("Choose option: ", 0, 5);

        switch (choice) {
            case 0:
                System.out.println("❌ Sort cancelled.");
                return;
            case 1:
                students.sort(Comparator.comparing(Student::getName));
                System.out.println("\n✅ Students sorted by Name (A → Z)");
                break;
            case 2:
                students.sort(Comparator.comparing(Student::getName).reversed());
                System.out.println("\n✅ Students sorted by Name (Z → A)");
                break;
            case 3:
                students.sort(Comparator.comparing(Student::getGrade).reversed());
                System.out.println("\n✅ Students sorted by Grade (High → Low)");
                break;
            case 4:
                students.sort(Comparator.comparing(Student::getGrade));
                System.out.println("\n✅ Students sorted by Grade (Low → High)");
                break;
            case 5:
                students.sort((s1, s2) ->
                        s1.getLetterGrade().compareTo(s2.getLetterGrade()));
                System.out.println("\n✅ Students sorted by Status (PD → P → F)");
                break;
        }

        saveToFile();
        viewAllStudents();
    }

    // ===== 8. EXPORT REPORT =====
    private void exportReport() {
        if (students.isEmpty()) {
            System.out.println("\n📭 No students to export.");
            return;
        }

        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║              💾 EXPORT REPORT                       ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println("  1. Export to Text File");
        System.out.println("  2. Export to CSV (Excel)");
        System.out.println("  0. Cancel");

        int choice = getValidatedIntInput("Choose option: ", 0, 2);

        try {
            switch (choice) {
                case 0:
                    System.out.println("❌ Export cancelled.");
                    return;
                case 1:
                    exportTextFile();
                    break;
                case 2:
                    exportCSVFile();
                    break;
            }
        } catch (IOException e) {
            System.out.println("❌ Error exporting file: " + e.getMessage());
        }
    }

    private void exportTextFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "student_report_" + timestamp + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("═══════════════════════════════════════════════════════");
            writer.println("           STUDENT GRADE REPORT");
            writer.println("═══════════════════════════════════════════════════════");
            writer.println("Generated: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            writer.println("Total Students: " + students.size());
            writer.println("═══════════════════════════════════════════════════════");
            writer.println();
            writer.println("GRADING SYSTEM:");
            writer.println("  75-100 = Distinction (PD)");
            writer.println("  50-74  = Pass (P)");
            writer.println("  0-49   = Fail (F)");
            writer.println();
            writer.println("STUDENT LIST:");
            writer.println("─────────────────────────────────────────────────────");
            writer.println(String.format("%-4s %-20s %8s %10s %12s", "#", "Name", "Grade", "Status", "Description"));
            writer.println("─────────────────────────────────────────────────────");

            for (int i = 0; i < students.size(); i++) {
                Student s = students.get(i);
                writer.println(String.format("%-4d %-20s %8.1f %10s %12s",
                        (i+1), s.getName(), s.getGrade(), s.getLetterGrade(), s.getGradeDescription()));
            }

            writer.println("─────────────────────────────────────────────────────");
            writer.println();

            // Add statistics
            int pd = 0, p = 0, f = 0;
            for (Student s : students) {
                if (s.getGrade() >= 75) pd++;
                else if (s.getGrade() >= 50) p++;
                else f++;
            }

            writer.println("STATISTICS SUMMARY:");
            writer.println("  Distinction (PD): " + pd + " students");
            writer.println("  Pass (P):        " + p + " students");
            writer.println("  Fail (F):        " + f + " students");
            writer.println();
            writer.println("End of Report");
        }

        System.out.println("\n✅ Report exported successfully!");
        System.out.println("   📄 File: " + fileName);
        System.out.println("   📁 Location: " + new File(".").getAbsolutePath());
    }

    private void exportCSVFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "student_report_" + timestamp + ".csv";

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("Name,Grade,Status,Description");
            for (Student s : students) {
                writer.println(s.getName() + "," + s.getGrade() + "," +
                        s.getLetterGrade() + "," + s.getGradeDescription());
            }
        }

        System.out.println("\n✅ Report exported successfully!");
        System.out.println("   📊 File: " + fileName);
        System.out.println("   📁 Location: " + new File(".").getAbsolutePath());
        System.out.println("   💡 Open with Excel or any spreadsheet software");
    }

    // ===== HELPER METHODS =====

    // Calculate median
    private double calculateMedian() {
        ArrayList<Double> grades = new ArrayList<>();
        for (Student s : students) {
            grades.add(s.getGrade());
        }
        Collections.sort(grades);

        int size = grades.size();
        if (size % 2 == 0) {
            return (grades.get(size/2 - 1) + grades.get(size/2)) / 2.0;
        } else {
            return grades.get(size/2);
        }
    }

    // Generate bar for visualization
    private String generateBar(int count, int maxCount) {
        if (maxCount == 0) return "";
        int barLength = (count * 20) / maxCount;
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < barLength; i++) {
            bar.append("█");
        }
        return bar.toString();
    }

    private int getMaxCount(int a, int b, int c) {
        return Math.max(Math.max(a, b), c);
    }

    // Check for duplicate names
    private boolean isDuplicateName(String name) {
        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    // ===== VALIDATED INPUT METHODS =====

    // Get validated integer input within range
    private int getValidatedIntInput(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("❌ Please enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input! Please enter a valid number.");
            }
        }
    }

    // Confirm action with Y/N
    private boolean confirmAction(String message) {
        System.out.print("\n⚠️  " + message + " (y/n): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("y") || response.equals("yes");
    }

    // ===== FILE I/O - SAVE/LOAD =====

    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Student s : students) {
                writer.println(s.getName() + "|" + s.getGrade());
            }
        } catch (IOException e) {
            // Silent fail - not critical
        }
    }

    private void loadFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("ℹ️  No previous data found. Starting fresh!");
            return;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            int loadedCount = 0;
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    try {
                        String name = parts[0].trim();
                        double grade = Double.parseDouble(parts[1].trim());
                        students.add(new Student(name, grade));
                        loadedCount++;
                    } catch (NumberFormatException e) {
                        // Skip invalid entries
                    }
                }
            }
            if (loadedCount > 0) {
                System.out.println("📂 Loaded " + loadedCount + " student(s) from previous session.");
            }
        } catch (IOException e) {
            System.out.println("⚠️  Could not load previous data.");
        }
    }
}
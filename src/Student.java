public class Student {
    private String name;
    private double grade;

    // Constructor
    public Student(String name, double grade) {
        this.name = name;
        this.grade = grade;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for grade
    public double getGrade() {
        return grade;
    }

    // Setter for grade (allows updating)
    public void setGrade(double grade) {
        this.grade = grade;
    }

    // Get letter grade based on new grading system
    public String getLetterGrade() {
        if (grade >= 75) {
            return "PD"; // Distinction
        } else if (grade >= 50) {
            return "P";  // Pass
        } else {
            return "F";  // Fail
        }
    }

    // Get full grade description
    public String getGradeDescription() {
        if (grade >= 75) {
            return "Distinction (PD)";
        } else if (grade >= 50) {
            return "Pass (P)";
        } else {
            return "Fail (F)";
        }
    }

    // Display student info
    @Override
    public String toString() {
        return name + " - Grade: " + grade + " (" + getLetterGrade() + ")";
    }
}
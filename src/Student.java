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

    // Display student info
    @Override
    public String toString() {
        return name + " - Grade: " + grade;
    }
}
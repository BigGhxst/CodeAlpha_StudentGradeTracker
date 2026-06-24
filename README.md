# Student Grade Tracker

## Project Overview
A comprehensive Student Grade Tracker system built in Java with a professional console interface. This application allows educators to efficiently manage student grades, generate statistical reports, and track academic performance.

## Features

### Core Functionality
- **Add Students**: Register students with their names and grades
- **View All Students**: Display complete student list with grades
- **Summary Report**: Generate comprehensive statistics including:
  - Average score
  - Median score
  - Highest and lowest scores with student names
  - Grade distribution (PD, P, F)
- **Update Grades**: Modify student grades easily
- **Remove Students**: Delete student records with confirmation
- **Search Students**: Find students by name (partial matching)
- **Sort Students**: Sort by name (A-Z/Z-A) or grade (High-Low/Low-High)
- **Export Reports**: Save data as .txt or .csv (Excel-ready)

### Custom Grading System
| Grade Range | Status | Code |
|-------------|--------|------|
| 75 - 100    | Distinction | PD |
| 50 - 74     | Pass | P |
| 0 - 49      | Fail | F |

### Additional Features
- **Auto-Save**: Data automatically saved to file
- **Auto-Load**: Previous data loaded on startup
- **Input Validation**: Prevents invalid entries
- **Duplicate Detection**: Warns about duplicate student names
- **Visual Bar Charts**: Grade distribution displayed as bars

## Technologies Used
- Java 17
- Collections Framework (ArrayList)
- File I/O (Serialization)
- Scanner for Console Input


## How to Run

### Using IntelliJ IDEA
1. Open IntelliJ IDEA
2. File → Open → Select project folder
3. Run `StudentGradeTracker.java`
4. Follow the console menu

### Using Command Line
```bash
# Compile
javac src/*.java

# Run
java src/StudentGradeTracker

## GitHub Link
https://github.com/BigGhxst/CodeAlpha_StudentGradeTracker.git

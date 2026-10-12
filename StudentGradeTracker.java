
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class StudentGradeTracker {

    static class Student {
        String name;
        double[] marks;

        Student(String name, double[] marks) {
            this.name = name;
            this.marks = marks;
        }

        double getAverage() {
            double total = 0;
            for (double mark : marks) {
                total += mark;
            }
            return total / marks.length;
        }
    }

    static ArrayList<Student> students = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static final String FILE_NAME = "students.txt";

    // Save student records to a file
    static void saveStudents() {
        try (PrintWriter writer = new PrintWriter(
                new FileWriter(FILE_NAME))) {
            for (Student s : students) {
                writer.println(s.name + "," +
                        s.marks[0] + "," +
                        s.marks[1] + "," +
                        s.marks[2]);
            }
            System.out.println("Records saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving records.");
        }
    }

    // Load saved records when the program starts
    static void loadStudents() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader =
                new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", -1);

                if (data.length != 4) {
                    continue;
                }

                try {
                    String name = data[0];
                    double[] marks = {
                        Double.parseDouble(data[1]),
                        Double.parseDouble(data[2]),
                        Double.parseDouble(data[3])
                    };

                    if (name.trim().isEmpty()
                            || marks[0] < 0 || marks[0] > 100
                            || marks[1] < 0 || marks[1] > 100
                            || marks[2] < 0 || marks[2] > 100) {
                        continue;
                    }

                    students.add(new Student(name, marks));
                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid saved record.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading records.");
        }
    }

    static double getValidMark(String subject) {
        while (true) {
            System.out.print("Enter marks in " + subject + " (0-100): ");

            if (sc.hasNextDouble()) {
                double mark = sc.nextDouble();
                sc.nextLine();

                if (Double.isFinite(mark) && mark >= 0 && mark <= 100) {
                    return mark;
                }
            } else {
                sc.nextLine();
            }

            System.out.println("Invalid marks! Enter a number from 0 to 100.");
        }
    }

    static void addStudent() {
        System.out.print("Enter student name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty() || name.contains(",")) {
            System.out.println("Enter a valid name without commas.");
            return;
        }

        for (Student s : students) {
            if (s.name.equalsIgnoreCase(name)) {
                System.out.println("Student already exists!");
                return;
            }
        }

        double[] marks = new double[3];
        String[] subjects = {"Java", "Database", "Networks"};

        for (int i = 0; i < marks.length; i++) {
         marks[i] = getValidMark(subjects[i]);
        }

        students.add(new Student(name, marks));
        saveStudents();
        System.out.println("Student added successfully!");
    }

    static void searchStudent() {
        System.out.print("Enter student name to search: ");
        String name = sc.nextLine().trim();

        for (Student s : students) {
            if (s.name.equalsIgnoreCase(name)) {
                System.out.println("\nStudent: " + s.name);
                System.out.printf("Java: %.1f%n", s.marks[0]);
                System.out.printf("Database: %.1f%n", s.marks[1]);
                System.out.printf("Networks: %.1f%n", s.marks[2]);
                System.out.printf("Average: %.2f%n", s.getAverage());
                return;
            }
        }

        System.out.println("Student not found.");
    }

    static void displayReport() {
        if (students.isEmpty()) {
            System.out.println("No student records available.");
            return;
        }

        double highest = -1;
        double lowest = 101;
        String highestStudent = "";
        String lowestStudent = "";
        double totalAverage = 0;

        System.out.println("\n========== STUDENT GRADE REPORT ==========");
        System.out.printf("%-18s %8s %8s %8s %10s%n",
                "Name", "Java", "Database", "Networks", "Average");
        System.out.println("--------------------------------------------------------------");

        for (Student s : students) {
            double average = s.getAverage();
            totalAverage += average;

            System.out.printf("%-18s %8.1f %8.1f %8.1f %10.2f%n",
                    s.name, s.marks[0], s.marks[1],
                    s.marks[2], average);

            for (double mark : s.marks) {
                if (mark > highest) {
                    highest = mark;
                    highestStudent = s.name;
                }

                if (mark < lowest) {
                    lowest = mark;
                    lowestStudent = s.name;
                }
            }
        }

        System.out.println("--------------------------------------------------------------");
        System.out.printf("Class Average: %.2f%n",
                totalAverage / students.size());
        System.out.printf("Highest Mark: %.1f (Student: %s)%n",
                highest, highestStudent);
        System.out.printf("Lowest Mark: %.1f (Student: %s)%n",
                lowest, lowestStudent);
        System.out.println("Total Students: " + students.size());
        System.out.println("==============================================");
    }

    public static void main(String[] args) {
        loadStudents();

        System.out.println("Loaded " + students.size() + " saved student(s).");

        while (true) {
            System.out.println("\n===== STUDENT GRADE TRACKER =====");
            System.out.println("1. Add Student");
            System.out.println("2. View Grade Report");
            System.out.println("3. Search Student");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("Please enter a number from 1 to 4.");
                sc.nextLine();
                continue;
            }

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    displayReport();
                    break;
                case 3:
                    searchStudent();
                    break;
                case 4:
                    saveStudents();
                    System.out.println("Thank you for using Student Grade Tracker!");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;  // Changed data type to int
    private String name;
    private List<Double> grades;
    private List<Integer> units;

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
        this.grades = new ArrayList<>();
        this.units = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addGrade(double grade, int units) {
        grades.add(grade);
        this.units.add(units);
    }

    public List<Double> getGrades() {
        return grades;
    }

    public List<Integer> getUnits() {
        return units;
    }

    public double getGWA() {
        if (grades.isEmpty()) {
            return 0;
        }
        double weightedSum = 0;
        double totalUnits = 0;
        for (int i = 0; i < grades.size(); i++) {
            double grade = grades.get(i);
            double units = this.units.get(i);
            // Calculate grade points based on the 5-point grading system
            double gradePoints;
            if (grade == 5.0) {
                gradePoints = 4.0;
            } else if (grade == 4.0) {
                gradePoints = 3.0;
            } else if (grade == 3.0) {
                gradePoints = 2.0;
            } else if (grade == 2.0) {
                gradePoints = 1.0;
            } else {
                gradePoints = 0.0;
            }
            weightedSum += gradePoints * units;
            totalUnits += units;
        }
        return weightedSum / totalUnits;
    }
}

class StudentGradeTracker {
    private List<Student> students;
    private String dataFilePath;

    public StudentGradeTracker(String dataFilePath) {
        this.students = new ArrayList<>();
        this.dataFilePath = dataFilePath;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public List<Student> getStudents() {
        return students;
    }

    public void saveDataToFile() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(dataFilePath))) {
            outputStream.writeObject(students);
            System.out.println("Data saved to " + dataFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadDataFromFile() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(dataFilePath))) {
            students = (List<Student>) inputStream.readObject();
            System.out.println("Data loaded from " + dataFilePath);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class GradeTrackerGUI {
    private JFrame frame;
    private JTextField idField, nameField, subject1Field, subject2Field, subject3Field;
    private JTextField subject1UnitField, subject2UnitField, subject3UnitField;
    private JTable studentTable;
    private JButton addButton, editButton, deleteButton;

    private StudentGradeTracker gradeTracker;
    private DefaultTableModel tableModel;

    /**
     * 
     */
    public GradeTrackerGUI() {
        gradeTracker = new StudentGradeTracker("data.txt");
        gradeTracker.loadDataFromFile();

        frame = new JFrame("Student Grade Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);

        JLabel idLabel = new JLabel("Student ID:");
        idField = new JTextField(10);
        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(10);
        JLabel subject1Label = new JLabel("Subject 1 Grade:");
        subject1Field = new JTextField(10);
        JLabel subject1UnitLabel = new JLabel("Units:");
        subject1UnitField = new JTextField(10);
        JLabel subject2Label = new JLabel("Subject 2 Grade:");
        subject2Field = new JTextField(10);
        JLabel subject2UnitLabel = new JLabel("Units:");
        subject2UnitField = new JTextField(10);
        JLabel subject3Label = new JLabel("Subject 3 Grade:");
        subject3Field = new JTextField(10);
        JLabel subject3UnitLabel = new JLabel("Units:");
        subject3UnitField = new JTextField(10);

        constraints.gridx = 0;
        constraints.gridy = 0;
        inputPanel.add(idLabel, constraints);
        constraints.gridx = 1;
        inputPanel.add(idField, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        inputPanel.add(nameLabel, constraints);
        constraints.gridx = 1;
        inputPanel.add(nameField, constraints);
        constraints.gridx = 0;
        constraints.gridy = 2;
        inputPanel.add(subject1Label, constraints);
        constraints.gridx = 1;
        inputPanel.add(subject1Field, constraints);
        constraints.gridx = 0;
        constraints.gridy = 3;
        inputPanel.add(subject1UnitLabel, constraints);
        constraints.gridx = 1;
        inputPanel.add(subject1UnitField, constraints);
        constraints.gridx = 0;
        constraints.gridy = 4;
        inputPanel.add(subject2Label, constraints);
        constraints.gridx = 1;
        inputPanel.add(subject2Field, constraints);
        constraints.gridx = 0;
        constraints.gridy = 5;
        inputPanel.add(subject2UnitLabel, constraints);
        constraints.gridx = 1;
        inputPanel.add(subject2UnitField, constraints);
        constraints.gridx = 0;
        constraints.gridy = 6;
        inputPanel.add(subject3Label, constraints);
        constraints.gridx = 1;
        inputPanel.add(subject3Field, constraints);
        constraints.gridx = 0;
        constraints.gridy = 7;
        inputPanel.add(subject3UnitLabel, constraints);
        constraints.gridx = 1;
        inputPanel.add(subject3UnitField, constraints);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        addButton = new JButton("Add");
        editButton = new JButton("Update/Edit");
        deleteButton = new JButton("Delete");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("GWA");

        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane studentTableScrollPane = new JScrollPane(studentTable);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(studentTable, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        
        
                addButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String idText = idField.getText();
                        String name = nameField.getText();
                        String subject1GradeText = subject1Field.getText();
                        String subject1UnitText = subject1UnitField.getText();
                        String subject2GradeText = subject2Field.getText();
                        String subject2UnitText = subject2UnitField.getText();
                        String subject3GradeText = subject3Field.getText();
                        String subject3UnitText = subject3UnitField.getText();
        
                        try {
                            int id = Integer.parseInt(idText);  // Parse the ID as an integer
                            double subject1Grade = Double.parseDouble(subject1GradeText);
                            int subject1Units = Integer.parseInt(subject1UnitText);
                            double subject2Grade = Double.parseDouble(subject2GradeText);
                            int subject2Units = Integer.parseInt(subject2UnitText);
                            double subject3Grade = Double.parseDouble(subject3GradeText);
                            int subject3Units = Integer.parseInt(subject3UnitText);
        
                            // Validate the name to ensure it contains no digits
                            if (name.matches(".*\\d.*")) {
                                JOptionPane.showMessageDialog(frame, "Invalid input. Name cannot contain digits.");
                            } else {
                                Student student = new Student(id, name);
                                student.addGrade(subject1Grade, subject1Units);
                                student.addGrade(subject2Grade, subject2Units);
                                student.addGrade(subject3Grade, subject3Units);
                                gradeTracker.addStudent(student);
                                updateStudentTable();
                                clearInputFields();
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(frame, "Invalid input. Please enter valid numeric values.");
                        }
                    }
                });
        
           

                editButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int selectedRow = studentTable.getSelectedRow();
                        if (selectedRow != -1) {
                            String idText = idField.getText();
                            String name = nameField.getText();
                            String subject1GradeText = subject1Field.getText();
                            String subject1UnitText = subject1UnitField.getText();
                            String subject2GradeText = subject2Field.getText();
                            String subject2UnitText = subject2UnitField.getText();
                            String subject3GradeText = subject3Field.getText();
                            String subject3UnitText = subject3UnitField.getText();
                
                            try {
                                int id = Integer.parseInt(idText);  // Parse the ID as an integer
                
                                // Check if the parsed ID is equal to the original string value
                                if (!idText.equals(String.valueOf(id))) {
                                    JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid integer for Student ID.");
                                    return;
                                }
                
                                double subject1Grade = Double.parseDouble(subject1GradeText);
                                int subject1Units = Integer.parseInt(subject1UnitText);
                                double subject2Grade = Double.parseDouble(subject2GradeText);
                                int subject2Units = Integer.parseInt(subject2UnitText);
                                double subject3Grade = Double.parseDouble(subject3GradeText);
                                int subject3Units = Integer.parseInt(subject3UnitText);
                
                                // Validate the name to ensure it contains no digits
                                if (name.matches(".*\\d.*")) {
                                    JOptionPane.showMessageDialog(frame, "Invalid input. Name cannot contain digits.");
                                    nameField.setText(gradeTracker.getStudents().get(selectedRow).getName());  // Revert the name to its original value
                                } else {
                                    Student student = gradeTracker.getStudents().get(selectedRow);
                                    student.setId(id);
                                    student.setName(name);
                                    student.getGrades().clear();
                                    student.getUnits().clear();
                                    student.addGrade(subject1Grade, subject1Units);
                                    student.addGrade(subject2Grade, subject2Units);
                                    student.addGrade(subject3Grade, subject3Units);
                                    updateStudentTable();
                                    clearInputFields();
                                }
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter valid numeric values.");
                            }
                        }
                    }
                });
               
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow != -1) {
                    gradeTracker.getStudents().remove(selectedRow);
                    updateStudentTable();
                    clearInputFields();
                }
            }
        });

        studentTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow != -1) {
                    Student student = gradeTracker.getStudents().get(selectedRow);
                    idField.setText(String.valueOf(student.getId()));
                    nameField.setText(student.getName());
                    List<Double> grades = student.getGrades();
                    List<Integer> units = student.getUnits();
                    if (grades.size() >= 3 && units.size() >= 3) {
                        subject1Field.setText(String.valueOf(grades.get(0)));
                        subject1UnitField.setText(String.valueOf(units.get(0)));
                        subject2Field.setText(String.valueOf(grades.get(1)));
                        subject2UnitField.setText(String.valueOf(units.get(1)));
                        subject3Field.setText(String.valueOf(grades.get(2)));
                        subject3UnitField.setText(String.valueOf(units.get(2)));
                    }
                }
            }
        });

        frame.setVisible(true);
        updateStudentTable();
    }

    private void updateStudentTable() {
        tableModel.setRowCount(0);
        List<Student> students = gradeTracker.getStudents();
        for (Student student : students) {
            List<Double> grades = student.getGrades();
            List<Integer> units = student.getUnits();
            double gwa = student.getGWA();

            Object[] rowData = {student.getId(), student.getName(),
                    grades.get(0), units.get(0),
                    grades.get(1), units.get(1),
                    grades.get(2), units.get(2),
                    gwa};
            tableModel.addRow(rowData);
        }
        gradeTracker.saveDataToFile();
    }

    private void clearInputFields() {
        idField.setText("");
        nameField.setText("");
        subject1Field.setText("");
        subject1UnitField.setText("");
        subject2Field.setText("");
        subject2UnitField.setText("");
        subject3Field.setText("");
        subject3UnitField.setText("");
    }
}

public class StudentTracker {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GradeTrackerGUI();
            }
        });
    }
}

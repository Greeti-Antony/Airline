package Airlinedemo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Employee {
    private static Scanner scanner = new Scanner(System.in);
    public void employeeMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("Employee Portal:");
            System.out.println("1. Add Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. View Employee by ID");
            System.out.println("4. Edit Employee");
            System.out.println("5. Fire Employee");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    addEmployee();
                    break;
                case 2:
                    viewAllEmployees();
                    break;
                case 3:
                	viewEmployee();
                	break;
                case 4:
                    editEmployee();
                    break;
                case 5:
                    fireEmployee();
                    break;
                case 6:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void addEmployee() {
    	try (Connection conn = Database.getconnection()) {
        	System.out.println("Enter Employee username:");
        	String username=scanner.nextLine();
        	System.out.println("Enter Employee password:");
        	String password=scanner.nextLine();
            System.out.println("Enter employee name:");
            String emp_name = scanner.nextLine();
            System.out.println("Enter employee email:");
            String email = scanner.nextLine();
            System.out.println("Enter employee phone Number:");
            String phoneNumber = scanner.nextLine();
            System.out.println("Enter employee salary:");
            double salary = scanner.nextDouble();
            scanner.nextLine(); // Consume newline
            System.out.println("Enter employee position:");
            String position = scanner.nextLine();

            String query = "INSERT INTO employees (username,password,emp_name, email, phoneNumber, salary, position) VALUES ( ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
            	statement.setString(1,username);
            	statement.setString(2,password);
                statement.setString(3, emp_name);
                statement.setString(4, email);
                statement.setString(5, phoneNumber);
                statement.setDouble(6, salary);
                statement.setString(7, position);
                statement.executeUpdate();
                System.out.println("Employee added successfully.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    private void viewAllEmployees() {
    	try (Connection conn = Database.getconnection()) {
            String query = "SELECT * FROM employees";
            try (PreparedStatement statement = conn.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                	String username = resultSet.getString("username");
                    String password= resultSet.getString("password");
                    String empName = resultSet.getString("emp_name");
                    String email = resultSet.getString("email");
                    String phoneNumber = resultSet.getString("phoneNumber");
                    double salary = resultSet.getDouble("salary");
                    String position = resultSet.getString("position");
                    System.out.println("--------------------------");
                    System.out.println("Employee Username: " + username);
                    System.out.println("Employee Password: " + password); 
                    System.out.println("Employee Name: " + empName);
                    System.out.println("Email: " + email);
                    System.out.println("Phone Number: " + phoneNumber);
                    System.out.println("Salary: " + salary);
                    System.out.println("Position: " + position);
                    System.out.println("--------------------------");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
    
    private void viewEmployee()
    {
    	    System.out.println("View Employees:");
    	    System.out.print("Enter Employee ID: ");
    	    int empId = scanner.nextInt();
    	    scanner.nextLine(); // Consume newline
    	    System.out.print("Enter Employee Name: ");
    	    String empName = scanner.nextLine();

    	    try (Connection conn = Database.getconnection()) {
    	        String query = "SELECT * FROM employees WHERE emp_id = ? AND emp_name LIKE ?";
    	        try (PreparedStatement statement = conn.prepareStatement(query)) {
    	            statement.setInt(1, empId);
    	            statement.setString(2, "%" + empName + "%");

    	            ResultSet resultSet = statement.executeQuery();
    	            boolean found = false;
    	            while (resultSet.next()) {
    	                found = true;
    	                String username = resultSet.getString("username");
    	                String password = resultSet.getString("password");
    	                String name = resultSet.getString("emp_name");
    	                String email = resultSet.getString("email");
    	                String phoneNumber = resultSet.getString("phoneNumber");
    	                double salary = resultSet.getDouble("salary");
    	                String position = resultSet.getString("position");
    	                System.out.println("--------------------------");
    	                System.out.println("Employee Username: " + username);
    	                System.out.println("Employee Password: " + password);
    	                System.out.println("Employee Name: " + name);
    	                System.out.println("Email: " + email);
    	                System.out.println("Phone Number: " + phoneNumber);
    	                System.out.println("Salary: " + salary);
    	                System.out.println("Position: " + position);
    	                System.out.println("--------------------------");
    	            }
    	            if (!found) {
    	                System.out.println("Employee with ID " + empId + " and name '" + empName + "' not found.");
    	            }
    	        }
    	    } catch (SQLException ex) {
    	        ex.printStackTrace();
    	    }
    	}

    
    

    private void editEmployee() {
    	try (Connection conn = Database.getconnection()) {
            System.out.print("Enter employee ID to update: ");
            int empId = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Enter new salary:");
            double newSalary = scanner.nextDouble();
            scanner.nextLine(); 

            String query = "UPDATE employees SET salary = ? WHERE emp_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setDouble(1, newSalary);
                statement.setInt(2, empId);
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Employee with ID " + empId + " updated successfully.");
                } else {
                    System.out.println("Employee with ID " + empId + " not found.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    private void fireEmployee() {
    	try (Connection conn = Database.getconnection()) {
            System.out.print("Enter employee ID to fire: ");
            int empId = scanner.nextInt();
            scanner.nextLine(); 

            String query = "DELETE FROM employees WHERE emp_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, empId);
                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Employee with ID " + empId + " fired successfully.");
                } else {
                    System.out.println("Employee with ID " + empId + " not found.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}


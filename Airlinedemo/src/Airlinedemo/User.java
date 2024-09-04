package Airlinedemo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
public class User {
    private static Scanner scanner = new Scanner(System.in);
    public void userMenu() {
    	Flight flight = new Flight("FlightName", "Origin", "Destination", "ArrivalTime",
                "DepartureTime", 100, 200.0);

        boolean exit = false;
        while (!exit) {
            System.out.println("User Portal:");
            System.out.println("1. View All Users");
            System.out.println("2. View User by ID");
            System.out.println("3. Edit User");
            System.out.println("4. Remove User");
            System.out.println("5. View All Flights");
            System.out.println("6. View Flight By ID");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 
            switch (choice) {
                case 1:
                    viewAllUsers();
                    break;
                case 2:
                    viewUserById();
                    break; 
                case 3:
                    editUser();
                    break;
                case 4:
                    deleteUser();
                    break;
                case 5:
                	flight.viewAllFlights();
                	break;
                case 6:
                	flight.viewAllFlights();
                	break;
                case 7:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
public void viewAllUsers() {
        try (Connection conn = Database.getconnection()) {
            String query = "SELECT user_id, username, password, customerName, phoneNumber, email FROM users";
            try (PreparedStatement statement = conn.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String userId = resultSet.getString("user_id");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String customerName = resultSet.getString("customerName");
                    String email = resultSet.getString("email");
                    String phoneNumber = resultSet.getString("phoneNumber");
                    
                    System.out.println("--------------------------");
                    System.out.println("User ID: " + userId);
                    System.out.println("Username: " + username);
                    System.out.println("Password: " + password);
                    System.out.println("Customer Name: " + customerName);
                    System.out.println("Email: " + email);
                    System.out.println("Phone Number: " + phoneNumber);
                    System.out.println("--------------------------");
                    
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void viewUserById() {
        System.out.print("Enter User ID to view: ");
        String userId = scanner.nextLine();

        try (Connection conn = Database.getconnection()) {
            String query = "SELECT username, password, customerName, email, phoneNumber FROM users WHERE user_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String username = resultSet.getString("username");
                        String password = resultSet.getString("password");
                        String customerName = resultSet.getString("customerName");
                        String email = resultSet.getString("email");
                        String phoneNumber = resultSet.getString("phoneNumber");

                        System.out.println("--------------------------");
                        System.out.println("User ID: " + userId);
                        System.out.println("Username: " + username);
                        System.out.println("Password: " + password);
                        System.out.println("Customer Name: " + customerName);
                        System.out.println("Email: " + email);
                        System.out.println("Phone Number: " + phoneNumber);
                        System.out.println("--------------------------");
                    } else {
                        System.out.println("User with ID " + userId + " not found.");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void editUser() {
    	
    	    System.out.print("Enter User ID to edit: ");
    	    String userId = scanner.nextLine();

    	    try (Connection conn = Database.getconnection()) {
    	        
    	        String query = "SELECT username, password, customerName, email, phoneNumber FROM users WHERE user_id = ?";
    	        try (PreparedStatement selectStatement = conn.prepareStatement(query)) {
    	            selectStatement.setString(1, userId);
    	            try (ResultSet resultSet = selectStatement.executeQuery()) {
    	                if (resultSet.next()) {
    	                    String currentUsername = resultSet.getString("username");
    	                    String currentPassword = resultSet.getString("password");
    	                    String currentCustomerName = resultSet.getString("customerName");
    	                    String currentEmail = resultSet.getString("email");
    	                    String currentPhoneNumber = resultSet.getString("phoneNumber");

    	                    System.out.println("Enter new user details. Press Enter to keep old values.");

    	                    System.out.print("Enter new username (current: " + currentUsername + "): ");
    	                    String newUsername = scanner.nextLine();
    	                    newUsername = newUsername.isEmpty() ? currentUsername : newUsername;

    	                    System.out.print("Enter new password (current: " + currentPassword + "): ");
    	                    String newPassword = scanner.nextLine();
    	                    newPassword = newPassword.isEmpty() ? currentPassword : newPassword;

    	                    System.out.print("Enter new customer name (current: " + currentCustomerName + "): ");
    	                    String newCustomerName = scanner.nextLine();
    	                    newCustomerName = newCustomerName.isEmpty() ? currentCustomerName : newCustomerName;

    	                    System.out.print("Enter new email (current: " + currentEmail + "): ");
    	                    String newEmail = scanner.nextLine();
    	                    newEmail = newEmail.isEmpty() ? currentEmail : newEmail;

    	                    System.out.print("Enter new phone number (current: " + currentPhoneNumber + "): ");
    	                    String newPhoneNumber = scanner.nextLine();
    	                    newPhoneNumber = newPhoneNumber.isEmpty() ? currentPhoneNumber : newPhoneNumber;

    	                    String updateQuery = "UPDATE users SET username = ?, password = ?, customerName = ?, email = ?, phoneNumber = ? WHERE user_id = ?";
    	                    try (PreparedStatement updateStatement = conn.prepareStatement(updateQuery)) {
    	                        
    	                        updateStatement.setString(1, newUsername);
    	                        updateStatement.setString(2, newPassword);
    	                        updateStatement.setString(3, newCustomerName);
    	                        updateStatement.setString(4, newEmail);
    	                        updateStatement.setString(5, newPhoneNumber);
    	                        updateStatement.setString(6, userId);

    	                        int rowsUpdated = updateStatement.executeUpdate();

    	                        if (rowsUpdated > 0) {
    	                            System.out.println("User details updated successfully.");
    	                        } else {
    	                            System.out.println("Failed to update user details.");
    	                        }
    	                    }
    	                } else {
    	                    System.out.println("User with ID " + userId + " not found.");
    	                }
    	            }
    	        }
    	    } catch (SQLException ex) {
    	        ex.printStackTrace();
    	    }
    	}

private void deleteUser() {
        System.out.print("Enter User ID to delete: ");
        String userId = scanner.nextLine();

        try (Connection conn = Database.getconnection()) {
            String checkQuery = "SELECT * FROM users WHERE user_id = ?";
            try (PreparedStatement checkStatement = conn.prepareStatement(checkQuery)) {
                checkStatement.setString(1, userId);
                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    if (resultSet.next()) {
                       String deleteQuery = "DELETE FROM users WHERE user_id = ?";
                        try (PreparedStatement deleteStatement = conn.prepareStatement(deleteQuery)) {
                            deleteStatement.setString(1, userId);
                            int rowsDeleted = deleteStatement.executeUpdate();
                            if (rowsDeleted > 0) {
                                System.out.println("User with ID " + userId + " deleted successfully.");
                            } else {
                                System.out.println("Failed to delete user with ID " + userId + ".");
                            }
                        }
                    } else {
                        System.out.println("User with ID " + userId + " not found.");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }



}

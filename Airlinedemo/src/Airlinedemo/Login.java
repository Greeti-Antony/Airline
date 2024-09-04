package Airlinedemo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Login {
    private static Scanner scanner = new Scanner(System.in);

    public static void login() {
        System.out.println("Login");

        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        String role = authenticate(username, password);

        switch (role) {
            case "users":
               UserBooking userBooking = new UserBooking();
               userBooking.userBookingMenu( username,password);
                break;
            case "admin":
                Admin admin = new Admin();
                admin.adminMenu();
                break;
            case "employees":
                User user = new User();
                user.userMenu();
                break;
            default:
                System.out.println("Invalid username or password.");
        }
    }

    public static void signup() {
        System.out.println("Sign Up");

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print("Enter Your name: ");
        String customerName = scanner.nextLine();

        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        try (Connection conn = Database.getconnection()) {
            String query = "INSERT INTO users (username, password, customerName, phoneNumber, email) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, password);
                statement.setString(3, customerName);
                statement.setString(4, phoneNumber);
                statement.setString(5, email);
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Registration successful!");

                    UserBooking userBooking = new UserBooking();
                    userBooking.userBookingMenu(username,password);
                } else {
                    System.out.println("Registration failed. Please try again.");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Registration failed. Please try again later.");
            ex.printStackTrace();
        }
    }

    private static String authenticate(String username, String password) {
        try (Connection conn = Database.getconnection()) {
            if (checkUserExists(conn, "admin", username, password)) {
                return "admin";
            } else if (checkUserExists(conn, "employees", username, password)) {
                return "employees";
            } else if (checkUserExists(conn, "users", username, password)) {
                return "users";
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "invalid";
    }

    private static boolean checkUserExists(Connection conn, String tableName, String username, String password) throws SQLException {
        String query = "SELECT * FROM " + tableName + " WHERE username = ? AND password = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
}

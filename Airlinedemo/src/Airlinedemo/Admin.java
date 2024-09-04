package Airlinedemo;
import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Scanner;

public class Admin {
    private static Scanner scanner = new Scanner(System.in);

    public void adminMenu() {
        boolean exit = false;
        try {
            while (!exit) {
                System.out.println("Admin Menu:");
                System.out.println("1. Employee Portal");
                System.out.println("2. User Portal");
                System.out.println("3. Flight Portal");
                System.out.println("4. Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); 

                switch (choice) {
                    case 1:
                        employeePortal();
                        break;
                    case 2:
                        userPortal();
                        break;
                    case 3:
                        flightPortal();
                        break;
                    case 4:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            }
        } finally {
            //scanner.close();
        }
    }

    private void employeePortal() {
        Employee employee = new Employee();
        employee.employeeMenu();
    }

    private void userPortal() {
        User user = new User();

        boolean exit = false;
        while (!exit) {
            System.out.println("User Portal:");
            System.out.println("1. View All Users");
            System.out.println("2. View User by ID");
            System.out.println("3. View User Booking");
            System.out.println("4. View All User Bookings");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    user.viewAllUsers();
                    break;
                case 2:
                    user.viewUserById();
                    break;
                case 3:
                	UserBooking userBooking = new UserBooking();
                	userBooking.viewMyBookings();
 
                    break;
                case 4:
                	viewAllBookings();
                    break;
                case 5:
                    exit = true;
                    System.out.println("Exiting User Portal.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void flightPortal() {
        Flight flight = new Flight("FlightName", "Origin", "Destination", "ArrivalTime", "DepartureTime", 100, 150.0);
        flight.flightMenu();
    }
    
    private void viewAllBookings() {
        try (Connection conn = Database.getconnection()) {
            String query = "SELECT * FROM booking";
            try (PreparedStatement statement = conn.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    System.out.println("--------------------------");
                    System.out.println("Username: " + resultSet.getString("Username"));
                    System.out.println("Flight Name: " + resultSet.getString("FlightName"));
                    System.out.println("Passenger Name: " + resultSet.getString("PassengerName"));
                    System.out.println("No. of Seats Booked: " + resultSet.getInt("NoSeatsBooked"));
                    System.out.println("Ticket Price: " + resultSet.getDouble("TicketPrice"));
                    System.out.println("Arrival Time: " + resultSet.getString("ArrivalTime"));
                    System.out.println("Departure Time: " + resultSet.getString("DepartureTime"));
                    System.out.println("Arrival Date: " + resultSet.getString("ArrivalDate"));
                    System.out.println("Origin: " + resultSet.getString("Origin"));
                    System.out.println("Destination: " + resultSet.getString("Destination"));
                    System.out.println("--------------------------");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}


















































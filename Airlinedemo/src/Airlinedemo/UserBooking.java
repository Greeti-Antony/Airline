package Airlinedemo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class UserBooking {
    private static Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void userBookingMenu(String username, String password) {
        boolean exit = false;
        while (!exit) {
            System.out.println("User Booking Portal:");
            System.out.println("1. View All Flights");
            System.out.println("2. Book Tickets");
            System.out.println("3. View My Bookings");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    viewAllFlights();
                    break;
                case 2:
                    bookTickets(username, password);
                    break;
                case 3:
                    viewMyBookings();
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void viewAllFlights() {
        try (Connection conn = Database.getconnection()) {
            String query = "SELECT * FROM flight";
            try (PreparedStatement statement = conn.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    System.out.println("--------------------------");
                    System.out.println("Flight ID: " + resultSet.getInt("FlightID"));
                    System.out.println("Flight Name: " + resultSet.getString("FlightName"));
                    System.out.println("Origin: " + resultSet.getString("Origin"));
                    System.out.println("Destination: " + resultSet.getString("Destination"));
                    System.out.println("Arrival Time: " + resultSet.getString("ArrivalTime"));
                    System.out.println("Departure Time: " + resultSet.getString("DepartureTime"));
                    System.out.println("Seat Capacity: " + resultSet.getInt("SeatCapacity"));
                    System.out.println("Ticket Price: " + resultSet.getDouble("TicketPrice"));
                    System.out.println("--------------------------");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    private void bookTickets(String username, String password) {
        try (Connection conn = Database.getconnection()) {
            System.out.print("Enter Flight Name: ");
            String flightName = scanner.nextLine();

            System.out.print("Enter Number of Seats to Book: ");
            int noSeatsBooked = scanner.nextInt();
            scanner.nextLine(); 

            LocalDate today = LocalDate.now();
            System.out.print("Enter Arrival Date (YYYY-MM-DD): ");
            LocalDate arrivalDate = LocalDate.parse(scanner.nextLine(), dateFormatter);
            if (arrivalDate.isBefore(today)) {
                System.out.println("Invalid arrival date. You cannot book a flight for a past date.");
                return;
            }

            Flight flightDetails = getFlight(conn, flightName);

            if (flightDetails != null) {
            	int availableSeats = flightDetails.getAvailableSeats(conn, flightName);

                if (noSeatsBooked > availableSeats) {
                    System.out.println("Seats not available. Requested seats exceed the available seat capacity.");
                    return;
                }

                double ticketPricePerSeat = flightDetails.getTicketPrice(flightName);
                double totalTicketPrice = noSeatsBooked * ticketPricePerSeat;

                System.out.println("Total Ticket Price for " + noSeatsBooked + " seats: " + totalTicketPrice);

                System.out.print("Confirm booking (yes/no): ");
                String confirmation = scanner.nextLine();
                if (confirmation.equalsIgnoreCase("yes")) {
                    for (int i = 0; i < noSeatsBooked; i++) {
                        System.out.println("Passenger " + (i + 1) + " Details:");
                        System.out.print("Enter Username: ");
                        String passengerUsername = scanner.nextLine();
                        System.out.print("Enter Password: ");
                        String passengerPassword = scanner.nextLine();

                        String passengerName = "";
                        if (checkUserExists(conn, "users", passengerUsername, passengerPassword)) {
                            passengerName = getUserCustomerName(conn, passengerUsername);
                            if (passengerName == null) {
                                System.out.println("Failed to retrieve user details. Please try again.");
                                return;
                            }
                        } else {
                            System.out.print("Enter Passenger Name: ");
                            passengerName = scanner.nextLine();
                            System.out.print("Enter Phone Number: ");
                            String phoneNumber = scanner.nextLine();
                            System.out.print("Enter Email: ");
                            String email = scanner.nextLine();
                            insertUser(conn, passengerUsername, passengerPassword, passengerName, phoneNumber, email);
                        }

                        insertBooking(conn, passengerUsername, passengerPassword, flightName, passengerName, 1, ticketPricePerSeat, flightDetails.getArrivalTime(conn, flightName), flightDetails.getDepartureTime(conn, flightName), arrivalDate, flightDetails.getOrigin(conn, flightName), flightDetails.getDestination(conn, flightName));
                    }

                    updateSeatCapacity(conn, flightName, noSeatsBooked);
                    System.out.println("Booking(s) confirmed successfully.");
                    System.out.println("Total Ticket Price for " + noSeatsBooked + " seats: " + totalTicketPrice);

                } else {
                    System.out.println("Booking cancelled.");
                }
            } else {
                System.out.println("Failed to retrieve flight details. Please try again.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
        public  void viewMyBookings() {
        try (Connection conn = Database.getconnection()) {
            // Prompt user for username and password
            System.out.print("Enter Username: ");
            String username = scanner.nextLine();
            System.out.print("Enter Password: ");
            String password = scanner.nextLine();

            // Check if the user exists
            if (!checkUserExists(conn, "users", username, password)) {
                System.out.println("Invalid username or password.");
                return;
            }

            String query = "SELECT * FROM booking WHERE Username = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, username);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.isBeforeFirst()) {
                        // If the result set is empty, user hasn't made any bookings
                        System.out.println("You have not made any bookings yet.");
                    } else {
                        while (resultSet.next()) {
                            System.out.println("--------------------------");
                            System.out.println("Username: " + resultSet.getString("Username"));
                            System.out.println("Flight Name: " + resultSet.getString("FlightName"));
                            System.out.println("Passenger Name: " + resultSet.getString("PassengerName"));
                            System.out.println("Number of Seats Booked: " + resultSet.getInt("NoSeatsBooked"));
                            System.out.println("Ticket Price: " + resultSet.getDouble("TicketPrice"));
                            System.out.println("Arrival Time: " + resultSet.getString("ArrivalTime"));
                            System.out.println("Departure Time: " + resultSet.getString("DepartureTime"));
                            System.out.println("Arrival Date: " + resultSet.getString("ArrivalDate"));
                            System.out.println("Origin: " + resultSet.getString("Origin"));
                            System.out.println("Destination: " + resultSet.getString("Destination"));
                            System.out.println("--------------------------");
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    private Flight getFlight(Connection conn, String flightName) throws SQLException {
        String query = "SELECT * FROM flight WHERE FlightName = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, flightName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String origin = resultSet.getString("Origin");
                    String destination = resultSet.getString("Destination");
                    String arrivalTime = resultSet.getString("ArrivalTime");
                    String departureTime = resultSet.getString("DepartureTime");
                    int seatCapacity = resultSet.getInt("SeatCapacity");
                    double ticketPrice = resultSet.getDouble("TicketPrice");
                    
                    return new Flight(flightName, origin, destination, arrivalTime, departureTime, seatCapacity, ticketPrice);
                } 
                else {
                    System.out.println("Flight with name " + flightName + " not found.");
                    return null;
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error while fetching flight details: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

    private String getUserCustomerName(Connection conn, String username) throws SQLException {
        String query = "SELECT customerName FROM users WHERE username = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("customerName");
                } else {
                    // User not found
                    System.out.println("User with username " + username + " not found.");
                    return null;
                }
            }
        } catch (SQLException ex) {
            // Handle SQL exception
            System.out.println("Error retrieving customer name for username " + username + ": " + ex.getMessage());
            throw ex; // Rethrow the exception for upper levels to handle
        }
    }


    private boolean checkUserExists(Connection conn, String tableName, String username, String password) throws SQLException {
        String query = "SELECT * FROM " + tableName + " WHERE Username = ? AND Password = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private void insertUser(Connection conn, String username, String password, String customerName, String phoneNumber, String email) throws SQLException {
        String query = "INSERT INTO users (Username, Password, customerName, phoneNumber, email) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, customerName);
            statement.setString(4, phoneNumber);
            statement.setString(5, email);
            statement.executeUpdate();
        }
    }

    
    public void insertBooking(Connection conn, String username, String password, String flightName, String passengerName, int noSeatsBooked, double totalTicketPrice, String arrivalTime, String departureTime, LocalDate arrivalDate, String origin, String destination) {
        String query = "INSERT INTO booking (Username, Password, FlightName, PassengerName, NoSeatsBooked, TicketPrice, ArrivalTime, DepartureTime, ArrivalDate, Origin, Destination) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, flightName);
            statement.setString(4, passengerName);
            statement.setInt(5, noSeatsBooked);
            statement.setDouble(6, totalTicketPrice);
            statement.setString(7, arrivalTime);
            statement.setString(8, departureTime);
            statement.setDate(9, java.sql.Date.valueOf(arrivalDate));
            statement.setString(10, origin);
            statement.setString(11, destination);
            
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Booking inserted successfully.");
            } else {
                System.out.println("Failed to insert booking.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }



    private void updateSeatCapacity(Connection conn, String flightName, int noSeatsBooked) throws SQLException {
        String query = "UPDATE flight SET SeatCapacity = SeatCapacity - ? WHERE FlightName = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, noSeatsBooked);
            statement.setString(2, flightName);
            statement.executeUpdate();
        }
    }
}

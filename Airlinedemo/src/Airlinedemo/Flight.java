package Airlinedemo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Flight {
	private static Scanner scanner = new Scanner(System.in);
	 private String flightName;
	    private String origin;
	    private String destination;
	    private String arrivalTime;
	    private String departureTime;
	    private int seatCapacity;
	    private double ticketPrice;
	 
	    public Flight(String flightName, String origin, String destination, String arrivalTime,
	                  String departureTime, int seatCapacity, double ticketPrice) {
	        this.flightName = flightName;
	        this.origin = origin;
	        this.destination = destination;
	        this.arrivalTime = arrivalTime;
	        this.departureTime = departureTime;
	        this.seatCapacity = seatCapacity;
	        this.ticketPrice = ticketPrice;
	    }

	    // Getters and setters
	    public String getFlightName() {
	        return flightName;
	    }

	    public void setFlightName(String flightName) {
	        this.flightName = flightName;
	    }

	    public String getOrigin() {
	        return origin;
	    }
         
	    public String getDestination() {
	        return destination;
	    }

	    public void setDestination(String destination) {
	        this.destination = destination;
	    }

	    public String getArrivalTime() {
	        return arrivalTime;
	    }

	    public void setArrivalTime(String arrivalTime) {
	        this.arrivalTime = arrivalTime;
	    }
	    
	    public String getDepartureTime() {
	        return departureTime;
	    }

	    public void setDepartureTime(String departureTime) {
	        this.departureTime = departureTime;
	    }

	    public int getSeatCapacity() {
	        return seatCapacity;
	    }

	    public void setSeatCapacity(int seatCapacity) {
	        this.seatCapacity = seatCapacity;
	    }

	    public double getTicketPrice() {
	        return ticketPrice;
	    }

	    public void setTicketPrice(double ticketPrice) {
	        this.ticketPrice = ticketPrice;
	    }

    //private static Scanner scanner = new Scanner(System.in);

    public void flightMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("Flight Portal:");
            System.out.println("1. Add Flight");
            System.out.println("2. View All Flights");
            System.out.println("3. View Flight By ID");
            System.out.println("4. Edit Flight");
            System.out.println("5. Cancel Flight");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    addFlight();
                    break;
                case 2:
                    viewAllFlights();
                    break;
                case 3:
                	viewFlightById();
                	break;
                case 4:
                    editFlight();
                    break;
                case 5:
                    deleteFlight();
                    break;
                case 6:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
    public String getArrivalTime(Connection conn, String flightName) throws SQLException {
        String arrivalTime = null;
        String query = "SELECT ArrivalTime FROM flight WHERE FlightName = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, flightName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    arrivalTime = resultSet.getString("ArrivalTime");
                }
            }
        }
        return arrivalTime;
    }
    
    public String getDepartureTime(Connection conn, String flightName) throws SQLException {
        String departureTime = null;
        String query = "SELECT DepartureTime FROM flight WHERE FlightName = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, flightName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    departureTime = resultSet.getString("DepartureTime");
                }
            }
        }
        return departureTime;
    }

    public String getOrigin(Connection conn, String flightName) throws SQLException {
        String origin = null;
        String query = "SELECT Origin FROM flight WHERE FlightName = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, flightName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    origin = resultSet.getString("Origin");
                }
            }
        }
        return origin;
    }
    public String getDestination(Connection conn, String flightName) throws SQLException {
        String destination = null;
        String query = "SELECT Destination FROM flight WHERE FlightName = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, flightName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    destination = resultSet.getString("Destination");
                }
            }
        }
        return destination;
    }
    
    
    
    public double getTicketPrice(String flightName) {
        double ticketPrice = 0.0;
        try (Connection conn = Database.getconnection()) {
            String query = "SELECT TicketPrice FROM flight WHERE FlightName = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, flightName);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        ticketPrice = resultSet.getDouble("TicketPrice");
                    } else {
                        System.out.println("Flight not found.");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ticketPrice;
    }

    public int getAvailableSeats(Connection conn, String flightName) throws SQLException {
        int bookedSeats = getBookedSeats(conn, flightName);
        int availableSeats = getSeatCapacity() - bookedSeats;
        return availableSeats;
    }
    
    public int getBookedSeats(Connection conn, String flightName) throws SQLException {
        int bookedSeats = 0;
        String query = "SELECT COUNT(*) AS bookedSeats FROM booking WHERE FlightName = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, flightName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    bookedSeats = resultSet.getInt("bookedSeats");
                }
            }
        }
        return bookedSeats;
    }

    
    
    
    
    public void addFlight() {
        try (Connection conn = Database.getconnection()) {
            System.out.print("Enter Flight Name: ");
            String flightName = scanner.nextLine();

            System.out.print("Enter Origin: ");
            String origin = scanner.nextLine();

            System.out.print("Enter Destination: ");
            String destination = scanner.nextLine();

            System.out.print("Enter Arrival Time (HH:MM:SS): ");
            String arrivalTime = scanner.nextLine();

            System.out.print("Enter Departure Time (HH:MM:SS): ");
            String departureTime = scanner.nextLine();

            System.out.print("Enter Seat Capacity: ");
            int seatCapacity = scanner.nextInt();
            
            System.out.print("Enter Ticket Price: ");
            double ticketPrice = scanner.nextDouble();
            scanner.nextLine(); // Consume newline

            String query = "INSERT INTO flight (FlightName, Origin, Destination, ArrivalTime, DepartureTime, seatCapacity, TicketPrice) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, flightName);
                statement.setString(2, origin);
                statement.setString(3, destination);
                statement.setString(4, arrivalTime);
                statement.setString(5, departureTime);
                statement.setInt(6, seatCapacity);
                statement.setDouble(7,ticketPrice);
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Flight added successfully.");
                } else {
                    System.out.println("Failed to add flight.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void viewAllFlights() {
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
    
    public void viewFlightById() {
        try (Connection conn = Database.getconnection()) {
            System.out.print("Enter Flight ID to view: ");
            int flightId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            String query = "SELECT * FROM flight WHERE FlightID = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, flightId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
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
                    } else {
                        System.out.println("Flight with ID " + flightId + " not found.");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void editFlight() {
        try (Connection conn = Database.getconnection()) {
            System.out.print("Enter Flight ID to edit: ");
            int flightID = scanner.nextInt();
            scanner.nextLine(); 

            
            String checkQuery = "SELECT * FROM flight WHERE FlightID = ?";
            try (PreparedStatement checkStatement = conn.prepareStatement(checkQuery)) {
                checkStatement.setInt(1, flightID);
                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    if (resultSet.next()) {
                        
                        System.out.println("Enter new details for the flight (Press Enter to keep old values):");

                        System.out.print("Enter new Flight Name: ");
                        String newFlightName = scanner.nextLine();

                        System.out.print("Enter new Origin: ");
                        String newOrigin = scanner.nextLine();

                        System.out.print("Enter new Destination: ");
                        String newDestination = scanner.nextLine();

                        System.out.print("Enter new Arrival Time (HH:MM:SS): ");
                        String newArrivalTime = scanner.nextLine();

                        System.out.print("Enter new Departure Time (HH:MM:SS): ");
                        String newDepartureTime = scanner.nextLine();

                        System.out.print("Enter new Seat Capacity (Press Enter to keep old value): ");
                        String newSeatCapacityInput = scanner.nextLine();
                        int newSeatCapacity = 0; 
                        if (!newSeatCapacityInput.isEmpty()) {
                            newSeatCapacity = Integer.parseInt(newSeatCapacityInput);
                        }

                        System.out.print("Enter new Ticket Price (Press Enter to keep old value): ");
                        String newTicketPriceInput = scanner.nextLine();
                        double newTicketPrice = 0.0; 
                        if (!newTicketPriceInput.isEmpty()) {
                            newTicketPrice = Double.parseDouble(newTicketPriceInput);
                        }

                        
                        String updateQuery = "UPDATE flight SET FlightName = ?, Origin = ?, Destination = ?, ArrivalTime = ?, DepartureTime = ?, SeatCapacity = ?, TicketPrice = ? WHERE FlightID = ?";
                        try (PreparedStatement updateStatement = conn.prepareStatement(updateQuery)) {
                            updateStatement.setString(1, newFlightName.isEmpty() ? resultSet.getString("FlightName") : newFlightName);
                            updateStatement.setString(2, newOrigin.isEmpty() ? resultSet.getString("Origin") : newOrigin);
                            updateStatement.setString(3, newDestination.isEmpty() ? resultSet.getString("Destination") : newDestination);
                            updateStatement.setString(4, newArrivalTime.isEmpty() ? resultSet.getString("ArrivalTime") : newArrivalTime);
                            updateStatement.setString(5, newDepartureTime.isEmpty() ? resultSet.getString("DepartureTime") : newDepartureTime);
                            updateStatement.setInt(6, newSeatCapacity == 0 ? resultSet.getInt("SeatCapacity") : newSeatCapacity);
                            updateStatement.setDouble(7, newTicketPrice == 0.0 ? resultSet.getDouble("TicketPrice") : newTicketPrice);
                            updateStatement.setInt(8, flightID);

                            
                            int rowsUpdated = updateStatement.executeUpdate();

                            if (rowsUpdated > 0) {
                                System.out.println("Flight details updated successfully.");
                            } else {
                                System.out.println("Failed to update flight details.");
                            }
                        }
                    } else {
                        System.out.println("Flight with ID " + flightID + " not found.");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public void deleteFlight() {
        try (Connection conn = Database.getconnection()) {
            System.out.print("Enter Flight ID to delete: ");
            int flightID = scanner.nextInt();
            scanner.nextLine(); 

            String checkQuery = "SELECT * FROM flight WHERE FlightID = ?";
            try (PreparedStatement checkStatement = conn.prepareStatement(checkQuery)) {
                checkStatement.setInt(1, flightID);
                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    if (resultSet.next()) {
                       String deleteQuery = "DELETE FROM flight WHERE FlightID = ?";
                        try (PreparedStatement deleteStatement = conn.prepareStatement(deleteQuery)) {
                            deleteStatement.setInt(1, flightID);
                            int rowsDeleted = deleteStatement.executeUpdate();
                            if (rowsDeleted > 0) {
                                System.out.println("Flight with ID " + flightID + " cancelled successfully.");
                            } else {
                                System.out.println("Failed to cancel flight with ID " + flightID + ".");
                            }
                        }
                    } else {
                        System.out.println("Flight with ID " + flightID + " not found.");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}

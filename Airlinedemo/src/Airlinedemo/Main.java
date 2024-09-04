package Airlinedemo;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        System.out.println("Welcome to Airline Management System");

        try {
            while (!exit) {
                System.out.println("Select your option:");
                System.out.println("1. Login");
                System.out.println("2. Signup");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");

                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        System.out.println("Logging in...");
                        Login.login();
                        break;
                    case "2":
                        System.out.println("Signing up...");
                        Login.signup();
                        break;
                    case "3":
                        System.out.println("Thank you for using Airline Management System!");
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}

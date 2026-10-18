import java.util.Random;
import java.util.Scanner;

public class CabBookingChatbot {

    // Reads a line and converts it to int; returns -1 if input is not a number
    static int readChoice(Scanner sc) {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random random = new Random();

        // Predefined cab types and rates (Rs per km)
        String[] cabNames = {"Auto", "Sedan", "SUV"};
        int[] cabRates = {12, 18, 25};

        // Predefined places
        String[] sources = {"Pune Station", "Swargate", "Hinjawadi"};
        String[] destinations = {"Shivajinagar", "Kothrud", "Wakad"};

        // Distance table in km: distances[source][destination]
        // The 3 PDF routes are on the diagonal (5, 8, 10).
        // The other values are approximate - change them if your teacher gives exact ones.
        //                     Shivajinagar Kothrud Wakad
        int[][] distances = {
                {5,  9,  15},   // from Pune Station
                {6,  8,  18},   // from Swargate
                {16, 12, 10}    // from Hinjawadi
        };

        System.out.println("===== Welcome to Cab Booking Chatbot =====");

        while (true) {
            System.out.println("\n----- MAIN MENU -----");
            System.out.println("1. Book Ride");
            System.out.println("2. Exit");
            System.out.print("Enter your choice: ");
            int mainChoice = readChoice(sc);

            if (mainChoice == 1) {

                // Step 2: choose cab type
                System.out.println("\nChoose Cab Type:");
                System.out.println("1. Auto  (Rs.12/km)");
                System.out.println("2. Sedan (Rs.18/km)");
                System.out.println("3. SUV   (Rs.25/km)");
                System.out.print("Enter cab choice: ");
                int cabChoice = readChoice(sc);

                if (cabChoice < 1 || cabChoice > 3) {
                    System.out.println("Invalid cab choice! Returning to main menu.");
                    continue;
                }

                // Step 3a: choose SOURCE first
                System.out.println("\nSelect Source:");
                for (int i = 0; i < sources.length; i++) {
                    System.out.println((i + 1) + ". " + sources[i]);
                }
                System.out.print("Enter source number: ");
                int srcChoice = readChoice(sc);

                if (srcChoice < 1 || srcChoice > 3) {
                    System.out.println("Invalid source choice! Returning to main menu.");
                    continue;
                }

                // Step 3b: then choose DESTINATION
                System.out.println("\nSelect Destination:");
                for (int i = 0; i < destinations.length; i++) {
                    System.out.println((i + 1) + ". " + destinations[i]);
                }
                System.out.print("Enter destination number: ");
                int destChoice = readChoice(sc);

                if (destChoice < 1 || destChoice > 3) {
                    System.out.println("Invalid destination choice! Returning to main menu.");
                    continue;
                }

                String cab = cabNames[cabChoice - 1];
                int rate = cabRates[cabChoice - 1];
                String source = sources[srcChoice - 1];
                String destination = destinations[destChoice - 1];
                int km = distances[srcChoice - 1][destChoice - 1];

                // Step 4: calculation (after source and destination are selected)
                int fare = rate * km;

                System.out.println("\n----- RIDE DETAILS -----");
                System.out.println("Cab Type    : " + cab);
                System.out.println("Source      : " + source);
                System.out.println("Destination : " + destination);
                System.out.println("Distance    : " + km + " km");
                System.out.println("Rate        : Rs." + rate + "/km");
                System.out.println("Total Fare  : Rs." + fare + "  (" + km + " km x Rs." + rate + ")");

                // Step 5: confirm before OTP (cancellation allowed only here)
                boolean decided = false;
                while (!decided) {
                    System.out.println("\n1. Confirm & Share OTP");
                    System.out.println("2. Cancel Ride");
                    System.out.print("Enter your choice: ");
                    int confirm = readChoice(sc);

                    if (confirm == 1) {
                        // Step 6: generate 4-digit OTP (1000 - 9999)
                        int otp = 1000 + random.nextInt(9000);
                        System.out.println("\nYour OTP has been shared with you: " + otp);
                        System.out.println("NOTE: Once the OTP is shared, the ride CANNOT be cancelled.");
                        System.out.println("Booking Confirmed! Your " + cab + " from " + source
                                + " to " + destination + " is on the way.");
                        decided = true;
                    } else if (confirm == 2) {
                        System.out.println("\nRide cancelled. No OTP was shared.");
                        decided = true;
                    } else {
                        System.out.println("Invalid choice! Please enter 1 or 2.");
                    }
                }

            } else if (mainChoice == 2) {
                // Step 7: exit
                System.out.println("\nThank you for using Cab Booking Chatbot. Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice! Please enter 1 or 2.");
            }
        }

        sc.close();
    }
}
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/* ------------------------------------------------------------
 * ABSTRACTION: common contract for anything that gives a discount
 * ------------------------------------------------------------ */
interface Discountable {
    double getDiscountPercent();
}

/* ------------------------------------------------------------
 * INHERITANCE: base class Passenger + two passenger types
 * ------------------------------------------------------------ */
abstract class Passenger implements Discountable {
    private String name;
    private int age;

    public Passenger(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() { return name; }
    public int getAge() { return age; }

    public abstract String getType();

    // Same method, different behaviour in each subclass (polymorphism)
    @Override
    public abstract double getDiscountPercent();
}

class RegularPassenger extends Passenger {
    public RegularPassenger(String name, int age) {
        super(name, age);
    }

    @Override
    public String getType() { return "Regular"; }

    @Override
    public double getDiscountPercent() { return 0; }
}

class StudentPassenger extends Passenger {
    public StudentPassenger(String name, int age) {
        super(name, age);
    }

    @Override
    public String getType() { return "Student"; }

    @Override
    public double getDiscountPercent() { return 10; }
}

/* ------------------------------------------------------------
 * ENCAPSULATION: Flight controls its own seat count
 * ------------------------------------------------------------ */
class Flight {
    private final String code;
    private final String airline;
    private final String source;
    private final String destination;
    private int availableSeats;
    private final double baseFare;

    public Flight(String code, String airline, String source,
                  String destination, int availableSeats, double baseFare) {
        this.code = code;
        this.airline = airline;
        this.source = source;
        this.destination = destination;
        this.availableSeats = availableSeats;
        this.baseFare = baseFare;
    }

    public String getCode() { return code; }
    public String getAirline() { return airline; }
    public String getSource() { return source; }
    public String getDestination() { return destination; }
    public int getAvailableSeats() { return availableSeats; }
    public double getBaseFare() { return baseFare; }

    public boolean hasSeats(int tickets) {
        return tickets <= availableSeats;
    }

    // Only way to reduce seats; menu code never touches the count directly
    public boolean reserveSeats(int tickets) {
        if (tickets <= 0 || tickets > availableSeats) {
            return false;
        }
        availableSeats -= tickets;
        return true;
    }

    public String getRoute() {
        return source + " to " + destination;
    }
}

/* ------------------------------------------------------------
 * Booking: holds one confirmed booking
 * ------------------------------------------------------------ */
class Booking {
    private final String bookingId;
    private final Flight flight;
    private final Passenger passenger;
    private final int tickets;
    private final double subtotal;
    private final double discountAmount;
    private final double finalAmount;

    public Booking(String bookingId, Flight flight, Passenger passenger, int tickets,
                   double subtotal, double discountAmount, double finalAmount) {
        this.bookingId = bookingId;
        this.flight = flight;
        this.passenger = passenger;
        this.tickets = tickets;
        this.subtotal = subtotal;
        this.discountAmount = discountAmount;
        this.finalAmount = finalAmount;
    }

    public String getBookingId() { return bookingId; }
    public Flight getFlight() { return flight; }
    public Passenger getPassenger() { return passenger; }
    public int getTickets() { return tickets; }
    public double getSubtotal() { return subtotal; }
    public double getDiscountAmount() { return discountAmount; }
    public double getFinalAmount() { return finalAmount; }
}

/* ------------------------------------------------------------
 * FlightBookingSystem: all the business logic
 * ------------------------------------------------------------ */
class FlightBookingSystem {
    private final List<Flight> flights = new ArrayList<>();
    private final List<Booking> bookings = new ArrayList<>();
    private int bookingCounter = 1000;

    public FlightBookingSystem() {
        flights.add(new Flight("PN101", "Kiran Air", "Pune", "Delhi", 20, 4000));
        flights.add(new Flight("MB202", "Sky Jet", "Mumbai", "Bengaluru", 15, 3500));
        flights.add(new Flight("PH303", "Indo Wings", "Pune", "Hyderabad", 10, 2800));
        flights.add(new Flight("DC404", "Kiran Air", "Delhi", "Chennai", 5, 5200));
    }

    // ---------- Display flights ----------
    public void displayFlights() {
        System.out.println("\nAvailable Flights");
        System.out.println("--------------------------------------------------------------------------");
        System.out.printf("%-8s %-12s %-12s %-12s %-8s %-10s%n",
                "Code", "Airline", "Source", "Destination", "Seats", "Base Fare");
        System.out.println("--------------------------------------------------------------------------");
        for (Flight f : flights) {
            System.out.printf("%-8s %-12s %-12s %-12s %-8d Rs.%.0f%n",
                    f.getCode(), f.getAirline(), f.getSource(),
                    f.getDestination(), f.getAvailableSeats(), f.getBaseFare());
        }
    }

    // ---------- Find flight by code ----------
    public Flight findFlightByCode(String code) {
        for (Flight f : flights) {
            if (f.getCode().equalsIgnoreCase(code.trim())) {
                return f;
            }
        }
        return null;
    }

    // ---------- Validate booking details ----------
    // Returns null if everything is valid, otherwise an error message
    public String validateBookingDetails(Flight flight, String name, int age,
                                         String type, int tickets) {
        if (flight == null) {
            return "Flight code does not exist.";
        }
        if (name == null || name.trim().isEmpty()) {
            return "Passenger name cannot be blank.";
        }
        if (age <= 0) {
            return "Age must be greater than 0.";
        }
        if (!type.equalsIgnoreCase("Regular") && !type.equalsIgnoreCase("Student")) {
            return "Passenger type must be Regular or Student.";
        }
        if (tickets <= 0) {
            return "Number of tickets must be greater than 0.";
        }
        if (!flight.hasSeats(tickets)) {
            return "Not enough seats available.";
        }
        return null;
    }

    // ---------- Create passenger object based on type ----------
    private Passenger createPassenger(String name, int age, String type) {
        if (type.equalsIgnoreCase("Student")) {
            return new StudentPassenger(name.trim(), age);
        }
        return new RegularPassenger(name.trim(), age);
    }

    // ---------- Calculate fare ----------
    public double calculateSubtotal(Flight flight, int tickets) {
        return flight.getBaseFare() * tickets;
    }

    public double calculateDiscount(double subtotal, Passenger passenger) {
        // Polymorphism: correct discount depends on actual passenger type
        return subtotal * passenger.getDiscountPercent() / 100.0;
    }

    // ---------- Confirm booking ----------
    // Returns the Booking, or null if booking failed (no seats/bookings changed)
    public Booking confirmBooking(String flightCode, String name, int age,
                                  String type, int tickets) {
        Flight flight = findFlightByCode(flightCode);

        String error = validateBookingDetails(flight, name, age, type, tickets);
        if (error != null) {
            System.out.println("\nBooking failed: " + error);
            System.out.println("No booking was created. Available seats remain unchanged.");
            return null;
        }

        Passenger passenger = createPassenger(name, age, type);
        double subtotal = calculateSubtotal(flight, tickets);
        double discount = calculateDiscount(subtotal, passenger);
        double finalAmount = subtotal - discount;

        if (!flight.reserveSeats(tickets)) {
            System.out.println("\nBooking failed: Not enough seats available.");
            System.out.println("No booking was created. Available seats remain unchanged.");
            return null;
        }

        bookingCounter++;
        Booking booking = new Booking("BK" + bookingCounter, flight, passenger,
                tickets, subtotal, discount, finalAmount);
        bookings.add(booking);
        return booking;
    }

    // ---------- Display booking summary ----------
    public void displaySummary(Booking b) {
        Passenger p = b.getPassenger();
        Flight f = b.getFlight();
        System.out.println("Booking ID: " + b.getBookingId());
        System.out.println("Flight: " + f.getCode() + " | " + f.getRoute());
        System.out.println("Passenger: " + p.getName() + " (Age " + p.getAge() + ")");
        System.out.println("Passenger type: " + p.getType());
        System.out.println("Tickets: " + b.getTickets());
        System.out.printf("Base fare per ticket: Rs.%.2f%n", f.getBaseFare());
        System.out.printf("Subtotal: Rs.%.2f%n", b.getSubtotal());
        System.out.printf("%s discount (%.0f%%): Rs.%.2f%n",
                p.getType(), p.getDiscountPercent(), b.getDiscountAmount());
        System.out.printf("Final amount: Rs.%.2f%n", b.getFinalAmount());
    }

    // ---------- View most recent booking ----------
    public void viewLatestBooking() {
        if (bookings.isEmpty()) {
            System.out.println("\nNo bookings found. Please book a flight first (option 2).");
            return;
        }
        System.out.println("\nMost Recent Booking");
        System.out.println("-------------------");
        displaySummary(bookings.get(bookings.size() - 1));
    }
}

/* ------------------------------------------------------------
 * Main class: only handles the menu and user input
 * ------------------------------------------------------------ */
public class FlightBookingApp {

    private static final Scanner scanner = new Scanner(System.in);

    // Reads an integer safely; returns -1 if the input is not a valid number
    private static int readInt(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static String readText(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static void bookFlight(FlightBookingSystem system) {
        String code = readText("Enter flight code: ");
        String name = readText("Enter passenger name: ");
        int age = readInt("Enter age: ");
        String type = readText("Passenger type (Regular/Student): ").trim();
        int tickets = readInt("Enter number of tickets: ");

        Booking booking = system.confirmBooking(code, name, age, type, tickets);
        if (booking != null) {
            System.out.println("\nBooking confirmed!");
            system.displaySummary(booking);
        }
    }

    public static void main(String[] args) {
        FlightBookingSystem system = new FlightBookingSystem();

        while (true) {
            System.out.println("\nWelcome to Kiran Air Booking");
            System.out.println("1. View Flights");
            System.out.println("2. Book Flight");
            System.out.println("3. View Booking");
            System.out.println("4. Exit");
            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1:
                    system.displayFlights();
                    break;
                case 2:
                    bookFlight(system);
                    break;
                case 3:
                    system.viewLatestBooking();
                    break;
                case 4:
                    System.out.println("\nThank you for using Kiran Air Booking!");
                    return;
                default:
                    System.out.println("\nInvalid choice. Please enter a number from 1 to 4.");
            }
        }
    }
}
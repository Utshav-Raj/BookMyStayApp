/**
 * Book My Stay App
 * Use Case 2: Basic Room Types & Static Availability
 *
 * This program demonstrates object-oriented modeling using
 * abstraction, inheritance, polymorphism, and encapsulation.
 * Room availability is maintained using simple variables.
 *
 * @author Soham
 * @version 2.1
 */

// Abstract Class
abstract class Room {

    protected String roomType;
    protected int numberOfBeds;
    protected double size;
    protected double price;

    // Constructor
    public Room(String roomType, int numberOfBeds, double size, double price) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.size = size;
        this.price = price;
    }

    // Abstract Method
    public abstract void displayDetails();
}


// Single Room
class SingleRoom extends Room {

    public SingleRoom() {
        super("Single Room", 1, 200.0, 1500.0);
    }

    @Override
    public void displayDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Size: " + size + " sq ft");
        System.out.println("Price: ₹" + price);
    }
}


// Double Room
class DoubleRoom extends Room {

    public DoubleRoom() {
        super("Double Room", 2, 350.0, 2500.0);
    }

    @Override
    public void displayDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Size: " + size + " sq ft");
        System.out.println("Price: ₹" + price);
    }
}


// Suite Room
class SuiteRoom extends Room {

    public SuiteRoom() {
        super("Suite Room", 3, 600.0, 5000.0);
    }

    @Override
    public void displayDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Size: " + size + " sq ft");
        System.out.println("Price: ₹" + price);
    }
}


// Main Class
public class UseCase2RoomInitialization {

    public static void main(String[] args) {

        // Polymorphism (same reference type)
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        // Static Availability (No data structures)
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        System.out.println("=========== BOOK MY STAY APP ===========\n");

        // Display Single Room
        singleRoom.displayDetails();
        System.out.println("Available Rooms: " + singleAvailable + "\n");

        // Display Double Room
        doubleRoom.displayDetails();
        System.out.println("Available Rooms: " + doubleAvailable + "\n");

        // Display Suite Room
        suiteRoom.displayDetails();
        System.out.println("Available Rooms: " + suiteAvailable + "\n");

        System.out.println("=========== END ===========");
    }
}
/**
 * Book My Stay App
 * Use Case 4: Room Search & Availability Check
 *
 * This program demonstrates how guests can search for available rooms
 * using read-only access to inventory without modifying system state.
 *
 * @author Soham
 * @version 4.0
 */

import java.util.HashMap;
import java.util.Map;

// ---------------- ROOM DOMAIN ----------------
abstract class Room {

    protected String roomType;
    protected int beds;
    protected double price;

    public Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.price = price;
    }

    public String getRoomType() {
        return roomType;
    }

    public abstract void displayDetails();
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 1500);
    }

    public void displayDetails() {
        System.out.println(roomType + " | Beds: " + beds + " | Price: ₹" + price);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 2500);
    }

    public void displayDetails() {
        System.out.println(roomType + " | Beds: " + beds + " | Price: ₹" + price);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 5000);
    }

    public void displayDetails() {
        System.out.println(roomType + " | Beds: " + beds + " | Price: ₹" + price);
    }
}

// ---------------- INVENTORY (READ ONLY USE) ----------------
class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 0); // unavailable (test filtering)
        inventory.put("Suite Room", 2);
    }

    // Read-only access
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public Map<String, Integer> getAllInventory() {
        return inventory;
    }
}

// ---------------- SEARCH SERVICE ----------------
class RoomSearchService {

    private RoomInventory inventory;

    public RoomSearchService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void searchAvailableRooms(Room[] rooms) {

        System.out.println("===== AVAILABLE ROOMS =====");

        for (Room room : rooms) {

            int available = inventory.getAvailability(room.getRoomType());

            // Validation (Defensive Programming)
            if (available > 0) {
                room.displayDetails();
                System.out.println("Available: " + available + "\n");
            }
        }

        System.out.println("===========================");
    }
}

// ---------------- MAIN CLASS ----------------
public class BookMyStayApp {

    public static void main(String[] args) {

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Create room objects
        Room[] rooms = {
                new SingleRoom(),
                new DoubleRoom(),
                new SuiteRoom()
        };

        // Initialize search service
        RoomSearchService searchService = new RoomSearchService(inventory);

        // Perform search (READ ONLY)
        searchService.searchAvailableRooms(rooms);
    }
}
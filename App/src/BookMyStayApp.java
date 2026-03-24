/**
 * Book My Stay App
 * Use Case 3: Centralized Room Inventory Management
 *
 * This program demonstrates how a centralized inventory system
 * can be implemented using HashMap to manage room availability.
 *
 * @author Soham
 * @version 3.1
 */

import java.util.HashMap;
import java.util.Map;

// Inventory Class
class RoomInventory {

    private Map<String, Integer> inventory;

    // Constructor - Initialize inventory
    public RoomInventory() {
        inventory = new HashMap<>();

        // Initial room availability
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Get availability
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability
    public void updateAvailability(String roomType, int count) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, count);
        } else {
            System.out.println("Room type not found!");
        }
    }

    // Display inventory
    public void displayInventory() {
        System.out.println("===== CURRENT ROOM INVENTORY =====");

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> Available: " + entry.getValue());
        }

        System.out.println("==================================");
    }
}


// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        // Initialize Inventory
        RoomInventory inventory = new RoomInventory();

        // Display Initial Inventory
        inventory.displayInventory();

        // Example: Update availability
        System.out.println("\nUpdating availability...\n");
        inventory.updateAvailability("Single Room", 4);

        // Display Updated Inventory
        inventory.displayInventory();

        // Example: Get availability
        System.out.println("\nAvailable Double Rooms: "
                + inventory.getAvailability("Double Room"));
    }
}
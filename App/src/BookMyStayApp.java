import java.io.*;
import java.util.*;

// Booking class (Serializable)
class Booking implements Serializable {
    private static final long serialVersionUID = 1L;
    private String guestName;

    public Booking(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestName() {
        return guestName;
    }
}

// Inventory class (Serializable)
class HotelInventory implements Serializable {
    private static final long serialVersionUID = 1L;
    private int availableRooms;

    public HotelInventory(int rooms) {
        this.availableRooms = rooms;
    }

    public int getAvailableRooms() {
        return availableRooms;
    }

    public void bookRoom() {
        if (availableRooms > 0) {
            availableRooms--;
        }
    }
}

// Wrapper class to persist full system state
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    List<Booking> bookings;
    HotelInventory inventory;

    public SystemState(List<Booking> bookings, HotelInventory inventory) {
        this.bookings = bookings;
        this.inventory = inventory;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    // Save state to file
    public static void save(SystemState state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(state);
            System.out.println("System state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    // Load state from file
    public static SystemState load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            SystemState state = (SystemState) ois.readObject();
            System.out.println("System state loaded successfully.");
            return state;
        } catch (FileNotFoundException e) {
            System.out.println("No previous state found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Corrupted data. Starting with a clean state.");
        }
        return null;
    }
}

// Main Class
public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        List<Booking> bookings;
        HotelInventory inventory;

        // STEP 1: Load previous state (Recovery)
        SystemState state = PersistenceService.load();

        if (state != null) {
            bookings = state.bookings;
            inventory = state.inventory;
        } else {
            // Initialize fresh state
            bookings = new ArrayList<>();
            inventory = new HotelInventory(5);
        }

        // STEP 2: Simulate booking operations
        System.out.println("\n--- Booking Simulation ---");

        for (int i = 1; i <= 3; i++) {
            if (inventory.getAvailableRooms() > 0) {
                String guestName = "Guest-" + i;
                bookings.add(new Booking(guestName));
                inventory.bookRoom();
                System.out.println("Booked room for " + guestName);
            } else {
                System.out.println("No rooms available");
            }
        }

        // Display current state
        System.out.println("\nCurrent Available Rooms: " + inventory.getAvailableRooms());
        System.out.println("Total Bookings: " + bookings.size());

        // STEP 3: Save state before shutdown
        PersistenceService.save(new SystemState(bookings, inventory));

        System.out.println("\nSystem shutting down... Restart to verify recovery.");
    }
}
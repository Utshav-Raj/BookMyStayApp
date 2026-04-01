import java.util.*;
import java.util.concurrent.*;

// Booking Request Class
class BookingRequest {
    private String guestName;

    public BookingRequest(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestName() {
        return guestName;
    }
}

// Shared Hotel Inventory
class HotelInventory {
    private int availableRooms;

    public HotelInventory(int rooms) {
        this.availableRooms = rooms;
    }

    // Critical Section (Thread-safe)
    public synchronized boolean bookRoom(String guestName) {
        if (availableRooms > 0) {
            System.out.println(guestName + " is booking a room...");
            try {
                Thread.sleep(100); // simulate delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            availableRooms--;
            System.out.println("Booking confirmed for " + guestName +
                    ". Rooms left: " + availableRooms);
            return true;
        } else {
            System.out.println("No rooms available for " + guestName);
            return false;
        }
    }
}

// Booking Processor (Consumer Threads)
class BookingProcessor implements Runnable {
    private Queue<BookingRequest> bookingQueue;
    private HotelInventory inventory;

    public BookingProcessor(Queue<BookingRequest> queue, HotelInventory inventory) {
        this.bookingQueue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {
            BookingRequest request;

            // Synchronize queue access
            synchronized (bookingQueue) {
                if (bookingQueue.isEmpty()) {
                    break;
                }
                request = bookingQueue.poll();
            }

            if (request != null) {
                inventory.bookRoom(request.getGuestName());
            }
        }
    }
}

// Main Class
/**
 * Book My Stay App
 * Use Case 10: Booking Cancellation & Inventory Rollback
 *
 * This program demonstrates safe cancellation of bookings using
 * rollback logic with Stack (LIFO), ensuring system consistency.
 *
 * @author Soham
 * @version 10.0
 */

import java.util.*;

// ---------------- RESERVATION ----------------
class Reservation {

    private String reservationId;
    private String roomType;

    public Reservation(String reservationId, String roomType) {
        this.reservationId = reservationId;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }
}

// ---------------- INVENTORY ----------------
class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 1);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void increment(String roomType) {
        inventory.put(roomType, inventory.get(roomType) + 1);
    }

    public void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void display() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.println(e.getKey() + " -> " + e.getValue());
        }
    }
}

// ---------------- BOOKING HISTORY ----------------
class BookingHistory {

    private Map<String, Reservation> confirmedBookings = new HashMap<>();

    public void addReservation(Reservation r) {
        confirmedBookings.put(r.getReservationId(), r);
    }

    public Reservation getReservation(String id) {
        return confirmedBookings.get(id);
    }

    public void removeReservation(String id) {
        confirmedBookings.remove(id);
    }

    public boolean exists(String id) {
        return confirmedBookings.containsKey(id);
    }
}

// ---------------- CANCELLATION SERVICE ----------------
class CancellationService {

    private RoomInventory inventory;
    private BookingHistory history;

    // Stack for rollback (LIFO)
    private Stack<String> rollbackStack = new Stack<>();

    public CancellationService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void cancelBooking(String reservationId) {

        System.out.println("\nProcessing cancellation for: " + reservationId);

        // Validate existence
        if (!history.exists(reservationId)) {
            System.out.println("ERROR: Reservation does not exist or already cancelled.");
            return;
        }

        Reservation r = history.getReservation(reservationId);

        // Push to rollback stack
        rollbackStack.push(reservationId);

        // Restore inventory
        inventory.increment(r.getRoomType());

        // Remove from history
        history.removeReservation(reservationId);

        System.out.println("Cancellation successful for " + reservationId);
    }

    // View rollback stack
    public void displayRollbackStack() {
        System.out.println("\nRollback Stack (Recent cancellations): " + rollbackStack);
    }
}

// ---------------- MAIN CLASS ----------------
public class BookMyStayApp {

    public static void main(String[] args) {

feature/UC11-ConcurrentBookingSimulation
        // Shared Booking Queue
        Queue<BookingRequest> bookingQueue = new LinkedList<>();

        // Add booking requests
        for (int i = 1; i <= 10; i++) {
            bookingQueue.add(new BookingRequest("Guest-" + i));
        }

        // Shared Inventory (only 5 rooms)
        HotelInventory inventory = new HotelInventory(5);

        // Create multiple threads
        Thread t1 = new Thread(new BookingProcessor(bookingQueue, inventory));
        Thread t2 = new Thread(new BookingProcessor(bookingQueue, inventory));
        Thread t3 = new Thread(new BookingProcessor(bookingQueue, inventory));

        // Start threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nAll booking requests processed safely.");

        // Initialize system
        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings
        Reservation r1 = new Reservation("SI-101", "Single Room");
        Reservation r2 = new Reservation("SU-202", "Suite Room");

        history.addReservation(r1);
        history.addReservation(r2);

        // Inventory after booking (decremented)
        inventory.decrement("Single Room");
        inventory.decrement("Suite Room");

        // Initialize cancellation service
        CancellationService service = new CancellationService(inventory, history);

        // Perform cancellation
        service.cancelBooking("SI-101");

        // Try invalid cancellation
        service.cancelBooking("SI-101");

        // Show rollback stack
        service.displayRollbackStack();

        // Show inventory after rollback
        inventory.display();
 dev
    }
}
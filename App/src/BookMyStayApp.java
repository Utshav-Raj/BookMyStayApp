/**
 * Book My Stay App
 * Use Case 6: Reservation Confirmation & Room Allocation
 *
 * This program processes booking requests from a queue,
 * assigns unique room IDs, updates inventory, and prevents
 * double-booking using Set and HashMap.
 *
 * @author Soham
 * @version 6.0
 */

import java.util.*;

// ---------------- RESERVATION ----------------
class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// ---------------- BOOKING QUEUE ----------------
class BookingRequestQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // dequeue (FIFO)
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// ---------------- INVENTORY SERVICE ----------------
class RoomInventory {

    private HashMap<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }
}

// ---------------- BOOKING SERVICE ----------------
class BookingService {

    private RoomInventory inventory;

    // Map: RoomType -> Set of Room IDs
    private HashMap<String, Set<String>> allocatedRooms = new HashMap<>();

    // Global Set for uniqueness
    private Set<String> allAllocatedRoomIds = new HashSet<>();

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void processRequests(BookingRequestQueue queue) {

        System.out.println("===== PROCESSING BOOKINGS =====\n");

        while (!queue.isEmpty()) {

            Reservation r = queue.getNextRequest();
            String roomType = r.getRoomType();

            int available = inventory.getAvailability(roomType);

            // Check availability
            if (available > 0) {

                // Generate unique room ID
                String roomId = generateRoomId(roomType);

                // Ensure uniqueness (extra safety)
                if (allAllocatedRoomIds.contains(roomId)) {
                    System.out.println("Duplicate Room ID detected! Skipping...");
                    continue;
                }

                // Store in global set
                allAllocatedRoomIds.add(roomId);

                // Store per room type
                allocatedRooms.putIfAbsent(roomType, new HashSet<>());
                allocatedRooms.get(roomType).add(roomId);

                // Decrement inventory (atomic step)
                inventory.decrement(roomType);

                // Confirm booking
                System.out.println("Booking Confirmed!");
                System.out.println("Guest: " + r.getGuestName());
                System.out.println("Room Type: " + roomType);
                System.out.println("Assigned Room ID: " + roomId + "\n");

            } else {
                System.out.println("Booking Failed for " + r.getGuestName() +
                        " (No " + roomType + " available)\n");
            }
        }

        System.out.println("===== BOOKING COMPLETE =====");
    }

    // Simple unique ID generator
    private String generateRoomId(String roomType) {
        return roomType.substring(0, 2).toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 5);
    }
}

// ---------------- MAIN CLASS ----------------
public class BookMyStayApp {

    public static void main(String[] args) {

        // Initialize components
        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue queue = new BookingRequestQueue();
        BookingService bookingService = new BookingService(inventory);

        // Add booking requests
        queue.addRequest(new Reservation("Soham", "Single Room"));
        queue.addRequest(new Reservation("Nandana", "Single Room"));
        queue.addRequest(new Reservation("Arjun", "Single Room")); // should fail (only 2 available)
        queue.addRequest(new Reservation("Riya", "Suite Room"));

        // Process queue
        bookingService.processRequests(queue);
    }
}
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
public class BookMyStayApp {

    public static void main(String[] args) {

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
    }
}
/**
 * Book My Stay App
 * Use Case 5: Booking Request (First-Come-First-Served)
 *
 * This program demonstrates how booking requests are collected
 * and stored in a queue to ensure fair processing using FIFO.
 *
 * No inventory updates or allocation happens at this stage.
 *
 * @author Soham
 * @version 5.0
 */

import java.util.LinkedList;
import java.util.Queue;

// ---------------- RESERVATION CLASS ----------------
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

    public void display() {
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
    }
}

// ---------------- BOOKING QUEUE ----------------
class BookingRequestQueue {

    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add request (enqueue)
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added for " + reservation.getGuestName());
    }

    // View all requests (without removing)
    public void displayQueue() {
        System.out.println("\n===== BOOKING REQUEST QUEUE =====");

        if (queue.isEmpty()) {
            System.out.println("No pending requests.");
        } else {
            for (Reservation r : queue) {
                r.display();
            }
        }

        System.out.println("=================================");
    }

    // Peek next request (FIFO head)
    public Reservation peekNextRequest() {
        return queue.peek();
    }
}

// ---------------- MAIN CLASS ----------------
public class BookMyStayApp {

    public static void main(String[] args) {

        // Initialize queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Create booking requests
        Reservation r1 = new Reservation("Soham", "Single Room");
        Reservation r2 = new Reservation("Nandana", "Suite Room");
        Reservation r3 = new Reservation("Arjun", "Double Room");

        // Add requests (arrival order matters)
        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);

        // Display queue
        bookingQueue.displayQueue();

        // Show next request to be processed
        System.out.println("\nNext Request to Process:");
        Reservation next = bookingQueue.peekNextRequest();
        if (next != null) {
            next.display();
        }
    }
}
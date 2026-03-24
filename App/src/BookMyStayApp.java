/**
 * Book My Stay App
 * Use Case 8: Booking History & Reporting
 *
 * This program stores confirmed bookings in a history list
 * and generates reports without modifying stored data.
 *
 * @author Soham
 * @version 8.0
 */

import java.util.*;

// ---------------- RESERVATION ----------------
class Reservation {

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room: " + roomType);
    }
}

// ---------------- BOOKING HISTORY ----------------
class BookingHistory {

    private List<Reservation> history = new ArrayList<>();

    // Add confirmed booking
    public void addReservation(Reservation reservation) {
        history.add(reservation);
    }

    // Retrieve all bookings (read-only usage)
    public List<Reservation> getAllReservations() {
        return history;
    }

    // Display all bookings
    public void displayHistory() {
        System.out.println("\n===== BOOKING HISTORY =====");

        if (history.isEmpty()) {
            System.out.println("No bookings found.");
        } else {
            for (Reservation r : history) {
                r.display();
            }
        }

        System.out.println("============================");
    }
}

// ---------------- REPORT SERVICE ----------------
class BookingReportService {

    // Generate summary report
    public void generateReport(List<Reservation> reservations) {

        System.out.println("\n===== BOOKING REPORT =====");

        if (reservations.isEmpty()) {
            System.out.println("No data available.");
            return;
        }

        // Count bookings per room type
        Map<String, Integer> report = new HashMap<>();

        for (Reservation r : reservations) {
            String type = r.getRoomType();
            report.put(type, report.getOrDefault(type, 0) + 1);
        }

        // Display report
        for (Map.Entry<String, Integer> entry : report.entrySet()) {
            System.out.println(entry.getKey() + " Bookings: " + entry.getValue());
        }

        System.out.println("===========================");
    }
}

// ---------------- MAIN CLASS ----------------
public class BookMyStayApp {

    public static void main(String[] args) {

        // Initialize history
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings (from Use Case 6)
        history.addReservation(new Reservation("SI-101", "Soham", "Single Room"));
        history.addReservation(new Reservation("SU-202", "Nandana", "Suite Room"));
        history.addReservation(new Reservation("SI-103", "Arjun", "Single Room"));
        history.addReservation(new Reservation("DB-301", "Riya", "Double Room"));

        // Display booking history
        history.displayHistory();

        // Generate report
        BookingReportService reportService = new BookingReportService();
        reportService.generateReport(history.getAllReservations());
    }
}
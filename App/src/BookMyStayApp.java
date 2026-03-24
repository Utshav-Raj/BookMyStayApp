/**
 * Book My Stay App
 * Use Case 9: Error Handling & Validation
 *
 * This program demonstrates input validation, custom exceptions,
 * and fail-fast design to ensure system reliability.
 *
 * @author Soham
 * @version 9.0
 */

import java.util.*;

// ---------------- CUSTOM EXCEPTIONS ----------------
class InvalidRoomTypeException extends Exception {
    public InvalidRoomTypeException(String message) {
        super(message);
    }
}

class InsufficientAvailabilityException extends Exception {
    public InsufficientAvailabilityException(String message) {
        super(message);
    }
}

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

// ---------------- INVENTORY ----------------
class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }
}

// ---------------- VALIDATOR ----------------
class InvalidBookingValidator {

    public static void validate(Reservation r, RoomInventory inventory)
            throws InvalidRoomTypeException, InsufficientAvailabilityException {

        // Validate room type
        if (!inventory.isValidRoomType(r.getRoomType())) {
            throw new InvalidRoomTypeException(
                    "Invalid Room Type: " + r.getRoomType());
        }

        // Validate availability
        if (inventory.getAvailability(r.getRoomType()) <= 0) {
            throw new InsufficientAvailabilityException(
                    "No availability for: " + r.getRoomType());
        }
    }
}

// ---------------- BOOKING SERVICE ----------------
class BookingService {

    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void confirmBooking(Reservation r) {

        try {
            // Fail-fast validation
            InvalidBookingValidator.validate(r, inventory);

            // Safe to proceed
            inventory.decrement(r.getRoomType());

            System.out.println("Booking Confirmed for " + r.getGuestName() +
                    " (" + r.getRoomType() + ")");

        } catch (InvalidRoomTypeException e) {
            System.out.println("ERROR: " + e.getMessage());

        } catch (InsufficientAvailabilityException e) {
            System.out.println("ERROR: " + e.getMessage());

        } catch (Exception e) {
            System.out.println("Unexpected Error: " + e.getMessage());
        }
    }
}

// ---------------- MAIN CLASS ----------------
public class BookMyStayApp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService(inventory);

        // Valid booking
        Reservation r1 = new Reservation("Soham", "Single Room");

        // Invalid room type
        Reservation r2 = new Reservation("Nandana", "Luxury Room");

        // Exceed availability
        Reservation r3 = new Reservation("Arjun", "Suite Room");
        Reservation r4 = new Reservation("Riya", "Suite Room"); // should fail

        // Process bookings
        bookingService.confirmBooking(r1);
        bookingService.confirmBooking(r2);
        bookingService.confirmBooking(r3);
        bookingService.confirmBooking(r4);
    }
}
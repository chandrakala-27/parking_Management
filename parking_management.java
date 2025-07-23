import java.util.*;

// Vehicle class
class Vehicle {
    String regNumber;
    String color;

    public Vehicle(String regNumber, String color) {
        this.regNumber = regNumber;
        this.color = color;
    }

    @Override
    public String toString() {
        return regNumber + " (" + color + ")";
    }
}

// ParkingLot class
class ParkingLot {
    private final PriorityQueue<Integer> availableSlots;
    private final Map<Integer, Vehicle> slotToVehicle;
    private final Map<String, Integer> regToSlot;
    private final int capacity;

    public ParkingLot(int capacity) {
        this.capacity = capacity;
        this.availableSlots = new PriorityQueue<>();
        this.slotToVehicle = new HashMap<>();
        this.regToSlot = new HashMap<>();

        for (int i = 1; i <= capacity; i++) {
            availableSlots.offer(i);
        }
    }

    public void parkVehicle(String regNumber, String color) {
        if (availableSlots.isEmpty()) {
            System.out.println("Sorry, parking lot is full.");
            return;
        }

        if (regToSlot.containsKey(regNumber)) {
            System.out.println("Vehicle is already parked in slot " + regToSlot.get(regNumber));
            return;
        }

        int slot = availableSlots.poll();
        Vehicle vehicle = new Vehicle(regNumber, color);
        slotToVehicle.put(slot, vehicle);
        regToSlot.put(regNumber, slot);

        System.out.println("Allocated slot number: " + slot);
    }

    public void leaveSlot(int slotNumber) {
        // ✅ Added: Slot range validation
        if (slotNumber < 1 || slotNumber > capacity) {
            System.out.println("Invalid slot number! Slot should be between 1 and " + capacity);
            return;
        }

        if (!slotToVehicle.containsKey(slotNumber)) {
            System.out.println("Slot number " + slotNumber + " is already empty.");
            return;
        }

        Vehicle vehicle = slotToVehicle.remove(slotNumber);
        regToSlot.remove(vehicle.regNumber);
        availableSlots.offer(slotNumber);

        System.out.println("Slot number " + slotNumber + " is now free.");
    }

    public void status() {
        if (slotToVehicle.isEmpty()) {
            System.out.println("Parking lot is empty.");
            return;
        }

        System.out.println("Slot No.    Registration No    Color");
        for (int slot = 1; slot <= capacity; slot++) {
            if (slotToVehicle.containsKey(slot)) {
                Vehicle vehicle = slotToVehicle.get(slot);
                System.out.printf("%-11d%-18s%s%n", slot, vehicle.regNumber, vehicle.color);
            }
        }
    }

    public void getSlotByRegistration(String regNumber) {
        if (regToSlot.containsKey(regNumber)) {
            System.out.println("Slot number: " + regToSlot.get(regNumber));
        } else {
            System.out.println("Not found.");
        }
    }
}

// Main class with menu
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter parking lot capacity: ");
        int capacity = sc.nextInt();
        sc.nextLine();  // consume newline

        ParkingLot lot = new ParkingLot(capacity);

        while (true) {
            System.out.println("\n--- Parking Lot Menu ---");
            System.out.println("1. Park a vehicle");
            System.out.println("2. Leave a slot");
            System.out.println("3. Show status");
            System.out.println("4. Find slot by registration number");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice;

            // ✅ Handle non-integer input gracefully
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter registration number: ");
                    String regNumber = sc.nextLine().trim();
                    System.out.print("Enter color: ");
                    String color = sc.nextLine().trim();
                    lot.parkVehicle(regNumber, color);
                    break;

                case 2:
                    System.out.print("Enter slot number to free: ");
                    try {
                        int slotNumber = Integer.parseInt(sc.nextLine().trim());
                        lot.leaveSlot(slotNumber);
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid slot number.");
                    }
                    break;

                case 3:
                    lot.status();
                    break;

                case 4:
                    System.out.print("Enter registration number: ");
                    String searchReg = sc.nextLine().trim();
                    lot.getSlotByRegistration(searchReg);
                    break;

                case 5:
                    System.out.println("Exiting. Thank you!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please choose a number between 1 and 5.");
            }
        }
    }
}

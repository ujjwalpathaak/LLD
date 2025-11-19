package ParkingLot;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

enum VehicleType {
    CAR,
    BIKE,
    TRUCK
};

enum PaymentMethod {
    UPI,
    CASH,
    CARD
};

// Vehicle
abstract class Vehicle {
    protected VehicleType type;
    protected String numberPlate;

    public Vehicle(VehicleType type, String numberPlate) {
        this.type = type;
        this.numberPlate = numberPlate;
    }

    public VehicleType getType() {
        return this.type;
    }

    public String getNumberPlate() {
        return this.numberPlate;
    }
}

class VehicleFactory {

    public Vehicle getObject(VehicleType vehicleType, String numberPlate) {
        switch (vehicleType) {
            case CAR:
                return new Car(numberPlate);

            case TRUCK:
                return new Truck(numberPlate);

            case BIKE:
                return new Bike(numberPlate);

            default:
                return null;
        }
    }
}

class VehicleManager {
    VehicleFactory factory;

    public VehicleManager() {
        this.factory = new VehicleFactory();
    }

    public Vehicle createVehicle(VehicleType vehicleType, String numberPlate) {
        return factory.getObject(vehicleType, numberPlate);
    }
}

class Bike extends Vehicle {
    public Bike(String number) {
        super(VehicleType.BIKE, number);
    }
}

class Car extends Vehicle {
    public Car(String number) {
        super(VehicleType.CAR, number);
    }
}

class Truck extends Vehicle {
    public Truck(String number) {
        super(VehicleType.TRUCK, number);
    }
}

// Pricing
class PricingManager {
    private PricingStrategy getPricingStrategy() {
        LocalDate today = LocalDate.now();
        DayOfWeek day = today.getDayOfWeek();

        List<LocalDate> holidays = List.of(
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 8, 15),
                LocalDate.of(2025, 10, 2));

        if (holidays.contains(today)) {
            return new HolidayPricing();
        }

        switch (day) {
            case SATURDAY:
            case SUNDAY:
                return new WeekendPricing();
            default:
                return new WeekdayPricing();
        }
    }

    public Double getPrice(Ticket ticket) {
        PricingStrategy strategy = getPricingStrategy();
        return strategy.getPrice(ticket);
    }
}

abstract class PricingStrategy {
    public Double getPrice(Ticket ticket) {
        LocalDateTime entryTime = ticket.getEntryTime();
        LocalDateTime exitTime = LocalDateTime.now();

        long minutes = Duration.between(entryTime, exitTime).toMinutes();
        double hours = Math.ceil(minutes / 60.0);

        // for demo adding +3
        return (hours + 3) * this.getMultiplier();
    }

    public abstract Double getMultiplier();
}

class WeekdayPricing extends PricingStrategy {
    @Override
    public Double getMultiplier() {
        return 10.0;
    }
}

class WeekendPricing extends PricingStrategy {
    @Override
    public Double getMultiplier() {
        return 15.0;
    }
}

class HolidayPricing extends PricingStrategy {
    @Override
    public Double getMultiplier() {
        return 20.0;
    }
}

// Payment
class PaymentManager {
    PricingManager pricingManger;

    public PaymentManager() {
        this.pricingManger = new PricingManager();
    }

    public void managePayment(Ticket ticket, PaymentMethod method) {
        Double price = this.pricingManger.getPrice(ticket);
        initiatePayment(price, method);
    }

    public void initiatePayment(Double price, PaymentMethod method) {
        PaymentStrategy strategy = getPaymentStrategy(method);
        strategy.processPayment(price);
    }

    private PaymentStrategy getPaymentStrategy(PaymentMethod method) {
        switch (method) {
            case UPI:
                return new UPIPayment();

            case CARD:
                return new CardPayment();

            case CASH:
                return new CashPayment();

            default:
                return null;
        }
    }
}

abstract class PaymentStrategy {
    public void processPayment(Double price) {
        this.takePayment(price);
    }

    public abstract void takePayment(Double price);
}

class UPIPayment extends PaymentStrategy {
    public void takePayment(Double price) {
        System.out.println("Processed Payment of " + price + " via UPI");
    }
}

class CardPayment extends PaymentStrategy {
    public void takePayment(Double price) {
        System.out.println("Processed Payment of " + price + " via CARD");
    }
}

class CashPayment extends PaymentStrategy {
    public void takePayment(Double price) {
        System.out.println("Processed Payment of " + price + " via CASH");
    }
}

// Ticket
class Ticket {
    protected Slot slot;
    protected LocalDateTime entryTime;
    protected Vehicle vehicle;

    public Ticket(Slot slot, Vehicle vehicle) {
        this.slot = slot;
        this.vehicle = vehicle;
        this.entryTime = LocalDateTime.now();
    };

    public LocalDateTime getEntryTime() {
        return this.entryTime;
    }

    public Slot getSlot() {
        return this.slot;
    }
}

class TicketManager {
    public Ticket createTicket(Slot slot, Vehicle vehicle) {
        return new Ticket(slot, vehicle);
    }
}

// Slot
class Slot {
    private boolean isBooked;
    private VehicleType vehicleType;

    public Slot(VehicleType vehicleType) {
        this.isBooked = false;
        this.vehicleType = vehicleType;
    }

    public boolean book(VehicleType vehicleType) {
        if (this.vehicleType == vehicleType && !this.isBooked) {
            this.isBooked = true;
            return true;
        }
        return false;
    }

    public VehicleType getType() {
        return this.vehicleType;
    }

    public boolean isFree() {
        return !this.isBooked;
    }

    public void freeSlot() {
        this.isBooked = false;
    }
}

class SlotManager {
    private ArrayList<Slot> slots = new ArrayList<>();

    public ArrayList<Slot> getSlots() {
        return this.slots;
    }

    public void addSlot(Slot slot) {
        this.slots.add(slot);
    }

    private boolean canBook(Slot slot, VehicleType vehicleType) {
        return slot.isFree() && slot.getType().equals(vehicleType);
    }

    private Slot findFreeSlot(VehicleType vehicleType) {
        for (Slot slot : slots) {
            if (canBook(slot, vehicleType)) {
                return slot;
            }
        }
        return null;
    }

    public Slot bookSlot(Vehicle vehicle) {
        Slot slot = findFreeSlot(vehicle.getType());
        if (slot != null && slot.book(vehicle.getType()))
            return slot;
        return null;
    }

    public void freeSlot(Ticket ticket) {
        ticket.getSlot().freeSlot();
    }
}

// singleton class
class ParkingLot {
    private static final ParkingLot INSTANCE = new ParkingLot();
    private PaymentManager paymentManager;
    private SlotManager slotsManager;
    private TicketManager ticketManager;
    private VehicleManager vehicleManager;

    private ParkingLot() {
        this.paymentManager = new PaymentManager();
        this.ticketManager = new TicketManager();
        this.slotsManager = new SlotManager();
        this.vehicleManager = new VehicleManager();
    }

    public static ParkingLot getInstance() {
        return INSTANCE;
    }

    public void setLayout(ArrayList<VehicleType> layout) {
        for (VehicleType type : layout) {
            this.slotsManager.addSlot(new Slot(type));
        }
    }

    public void removeVehicle(Ticket ticket, PaymentMethod method) {
        paymentManager.managePayment(ticket, method);
        this.slotsManager.freeSlot(ticket);
    }

    public Ticket addVehicle(VehicleType vehicleType, String numberPlate) {
        Vehicle vehicle = vehicleManager.createVehicle(vehicleType, numberPlate);
        Slot slot = this.slotsManager.bookSlot(vehicle);
        if (slot == null) {
            System.out.println("No available slot for " + numberPlate);
            return null;
        }
        return this.ticketManager.createTicket(slot, vehicle);
    }
}

public class Main {
   public static void main(String args[]) {
    ArrayList<VehicleType> layout = new ArrayList<>();
    layout.add(VehicleType.BIKE);
    layout.add(VehicleType.BIKE);
    layout.add(VehicleType.CAR);
    layout.add(VehicleType.CAR);
    layout.add(VehicleType.TRUCK);
    layout.add(VehicleType.TRUCK);

    ParkingLot parking = ParkingLot.getInstance();
    parking.setLayout(layout);

    Ticket t1 = parking.addVehicle(VehicleType.BIKE, "UP 16 AK 1640");
    Ticket t2 = parking.addVehicle(VehicleType.BIKE, "UP 32 BK 0021");
    Ticket t3 = parking.addVehicle(VehicleType.CAR, "HP 02 HR 1532");
    Ticket t4 = parking.addVehicle(VehicleType.CAR, "DL 01 AC 4590");
    Ticket t5 = parking.addVehicle(VehicleType.TRUCK, "BR 01 LF 1310");
    Ticket t6 = parking.addVehicle(VehicleType.TRUCK, "MH 14 TR 7890");
    Ticket t8 = parking.addVehicle(VehicleType.TRUCK, "GE 14 TR 7890");

    System.out.println("\n--- Vehicles parked successfully ---\n");

    if (t2 != null) parking.removeVehicle(t2, PaymentMethod.UPI);
    if (t3 != null) parking.removeVehicle(t3, PaymentMethod.CARD);
    if (t5 != null) parking.removeVehicle(t5, PaymentMethod.CASH);

    Ticket t7 = parking.addVehicle(VehicleType.CAR, "KA 09 CX 2234");
    if (t7 != null) {
        System.out.println("\nNew vehicle parked: " + t7.vehicle.getNumberPlate());
    }
}
}

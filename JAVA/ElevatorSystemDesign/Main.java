package ElevatorSystemDesign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

enum Direction {
    UP,
    DOWN
}

enum MovementState {
    STOPPED,
    MOVING
}

enum DoorState {
    OPENED,
    CLOSED
}

class Request {
    private Floor floor;
    private Direction direction;
    private Boolean requestedFloor;

    public Request(Floor floor, Direction direction) {
        this.floor = floor;
        this.direction = direction;
        this.requestedFloor = true;
    }

    public Request(Floor floor, Direction direction, Boolean requestedFloor) {
        this.floor = floor;
        this.direction = direction;
        this.requestedFloor = requestedFloor;
    }

    public Boolean isRequestedFloor() {
        return this.requestedFloor;
    }

    public Floor getFloor() {
        return this.floor;
    }

    public Direction getDirection() {
        return this.direction;
    }
}

class Floor {
    Integer level;
    Scheduler scheduler = Scheduler.getInstance();

    public Floor(Integer level) {
        this.level = level;
    }

    public Integer getLevel() {
        return this.level;
    }

    public void requestLift(Direction direction) {
        scheduler.newRequest(this, direction);
    }
};

class Lift {
    private Floor currentFloor;
    private Direction currentDirection;
    private MovementState currentMovementState;
    private DoorState currentDoorState;
    private Queue<Request> queue = new LinkedList<>();
    private Scheduler scheduler = Scheduler.getInstance();

    public void move() {
        // move according to the latest item in the queue and remove it from there
        Request newRequest = queue.remove();
        if (newRequest == null)
            return;
        currentFloor = newRequest.getFloor();
        currentDirection = newRequest.getDirection();
        // how to set movement?
    }

    public void requestFloor(Floor floor) {
        // find direction
        scheduler.newRequest(floor, Direction.UP);
    }

    public Direction getCurrentDirection() {
        return this.currentDirection;
    }

    public Floor getCurrentFloor() {
        return this.currentFloor;
    }
};

class Scheduler {
    private static Scheduler INSTANCE = new Scheduler();
    private Queue<Request> queue = new LinkedList<>();
    private ArrayList<Lift> lifts = new ArrayList<>();
    private ArrayList<Floor> floors = new ArrayList<>();

    private Scheduler() {
    };

    public static Scheduler getInstance() {
        return INSTANCE;
    }

    public void addNewLift(Lift lift) {
        lifts.add(lift);
    }

    public void addNewFloor(Floor floor) {
        floors.add(floor);
    }

    public void newRequest(Floor floor, Direction direction) {
        queue.add(new Request(floor, direction));
    }

    // main function - simulates 1 second. Each second equals 1 floor movement and
    // decision of pending items in queues
    public void tickTime() {
        // step 1 : move lifts 1 step using their queue
        for (Lift lift : lifts) {
            lift.move();
        }
        // step 2 : iterate over the main queue
        for (Request request : queue) {
            // Floor requestedFloor = request.getFloor();
            // step 3 : add the requests to respective lifts
            Lift idealLift = this.findIdealLift(request);
            // step 4 : modify request
            // get the lift ka current floor
            // see difference in destination floor
            // add that difference in lift ka queue
            // step 5 : empty the queue
            queue.remove();
        }
    }

    private Lift findIdealLift(Request request) {
        Floor requestedFloor = request.getFloor();
        Direction requestedDirection = request.getDirection();
        PriorityQueue<Lift, Integer> pq = new PriorityQueue<>();
        for (Lift lift : this.lifts) {
            // is lift going in correct direction
            Floor liftFloor = lift.getCurrentFloor();
            Direction floorWRTliftDirection = ((liftFloor.getLevel() - requestedFloor.getLevel()) > 0) ? Direction.DOWN
                    : Direction.UP;
            if (!lift.getCurrentDirection().equals(floorWRTliftDirection))
                continue;

        }

        return new Lift();
    }
};

public class Main {
    public static void main(String args[]) {
        Scheduler scheduler = Scheduler.getInstance();
        Floor floor1 = new Floor(1);
        Floor floor2 = new Floor(2);
        Floor floor3 = new Floor(3);
        Floor floor4 = new Floor(4);
        Floor floor5 = new Floor(5);
        Lift lift1 = new Lift();
        Lift lift2 = new Lift();
        Lift lift3 = new Lift();

        floor2.requestLift(Direction.DOWN);
        scheduler.tickTime();
        lift2.requestFloor(floor4);
    }
}

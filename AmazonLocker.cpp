#include <iostream>
#include <string>
#include <unordered_map>
#include <map>
#include <ctime>
#include <thread>
#include <chrono>
using namespace std;

enum class CompartmentSize {
    SMALL, MEDIUM, LARGE
};

enum class PackageSize {
    XSMALL, SMALL, MEDIUM, LARGE, XLARGE
};

enum class OrderStatus {
    CREATED, SHIPPED, DELIVERED, COMPLETED
};

class LockerService{
    unordered_map<int, vector<Locker*>> lockerLocations;
    
private:
public:
    void registerLocker(Locker* locker){
        lockerLocations[locker->location()].push_back(locker);
    }
    
    void assignLocker(Package *package){
        vector<*Locker> lockers = lockerLocations[package->pincode()];
        if(!lockers){
            cout << "No Lockers in this pincode" << endl;
        }
        for(auto &locker : lockers){
            if(locker.assignCompartment(package)){
                cout << "Compartment assigned to package";
            }else{
                cout << "No Compartment is free";
            }
        }
    }

    Locker* getLockerByPincode(int pincode){
        return lockerLocations[pincode] || cout << "No Locker Found for this Pincode" << endl;
    }

    void pickOrder(Order* order, int otp){
        Compartment* compartment
    }
};

class Locker{
private:
    map<CompartmentSize, vector<Compartment>> compartments;
    int pincode;

private:

public:
    Locker(map<CompartmentSize, int> mp, int pincode) {
        for (auto& [size, count] : mp) {
            while (count--) {
                compartments[size].emplace_back(Compartment(size));
            }
        }
        this->pincode = pincode;
    }
    
    bool assignCompartment (Package *package) {
        Compartment freeCompartment = this->getFreeCompartment(package->size());
        if(freeCompartment){
            freeCompartment->bookCompartment(package);
            return true;
        }
        return false;
    }

    void addOrder(Order* order){

    }
    
    int location() const {
        return this->pincode;
    }
};

class Compartment {
private:
    CompartmentSize size;
    Package package;
    bool isBooked;
public:
    Compartment(CompartmentSize size){
        this->size = size;
        this->isBooked = false;
    }

    void bookCompartment(Package *package){
        this->isBooked = true;
        this->package = package;
    }
};

class Package {
private:
    PackageSize size;
    Locker locker;
public:
    Package(PackageSize size) {
        this->size = size;
    }
};

class Order {
private:
    Package* package;
    int pincode;
    OrderStatus status;

public:
    Order(Package* package, int pincode){
        this->package = package;
        this->pincode = pincode;
        this->status = OrderStatus::CREATED;
    }

    bool updateStatus(OrderStatus newStatus){
        this->status = newStatus;
        if(newStatus == OrderStatus::DELIVERED)
    }

    int location(): static {
        return location;
    }
};

class User {
private:
    PackageSize size;
    Locker locker;
public:
    Package(PackageSize size) {
        this->size = size;
    }
};

class DeliveryAgent {
private:

public:
    void shipOrder(Order* order, Locker* locker){
        order.updateStatus(OrderStatus::SHIPPED);
        cout << "Out for delivery" << endl;
        locker.addOrder(order);
        order.updateStatus(OrderStatus::DELIVERED);
        cout << "Delivered" << endl;
    }
};

auto enumHash = [](auto value) {
    return static_cast<std::size_t>(value);
};

int main() {
    LockerService *lockerService = new LockerService();

    map<CompartmentSize, int> lockerAMap = {
        {CompartmentSize::SMALL, 3},
        {CompartmentSize::MEDIUM, 4},
        {CompartmentSize::LARGE, 5}
    };
    map<CompartmentSize, int> lockerBMap = {
        {CompartmentSize::SMALL, 2},
        {CompartmentSize::MEDIUM, 4},
        {CompartmentSize::LARGE, 8}
    };

    Locker *lockerA = new Locker(lockerAMap, 414834);
    Locker *lockerB = new Locker(lockerBMap, 500081);

    lockerService->registerLocker(lockerA);
    lockerService->registerLocker(lockerB);
    
    Package *packageA = new Package(PackageSize::XSMALL);
    Package *packageB = new Package(PackageSize::MEDIUM);

    Order *order = new Order(packageA, 500081);

    DeliveryAgent *deliveryAgentA = new DeliveryAgent();

    Locker* destination = lockerService->getLockerByPincode(order->location());

    deliveryAgentA.shipOrder(order, destination);

    lockerService->pickOrder(order, 123423);

    lockerService->pickOrder(order, 123321);

    return 0;
};
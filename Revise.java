// =====================
// DESIGN PATTERNS CHEAT SHEET
// =====================
// Covers: Strategy, Factory Method, Observer, Singleton, Builder, Decorator, Facade, State, Chain of Responsibility
// Each pattern = short realistic Java example + usage summary
// =====================

import java.util.*;

// ---------------------------------------------
// 1️⃣ STRATEGY PATTERN
// ---------------------------------------------
// Use when: You have multiple algorithms (sorting, payments, etc.)
// and want to swap them dynamically.
interface PaymentStrategy { void pay(int amount); }

class UpiPayment implements PaymentStrategy {
    public void pay(int a){ System.out.println("Paid ₹" + a + " via UPI"); }
}

class CardPayment implements PaymentStrategy {
    public void pay(int a){ System.out.println("Paid ₹" + a + " via Card"); }
}

class Checkout {
    private final PaymentStrategy strategy;
    Checkout(PaymentStrategy s){ this.strategy = s; }
    void process(int amt){ strategy.pay(amt); }
}

// ---------------------------------------------
// 2️⃣ FACTORY METHOD
// ---------------------------------------------
// Use when: You want subclasses to decide which object to create.
interface Notification { void notifyUser(); }

class EmailNotification implements Notification {
    public void notifyUser() { System.out.println("Sending Email..."); }
}

class SmsNotification implements Notification {
    public void notifyUser() { System.out.println("Sending SMS..."); }
}

abstract class NotificationFactory {
    abstract Notification createNotification();
}

class EmailFactory extends NotificationFactory {
    Notification createNotification() { return new EmailNotification(); }
}

class SmsFactory extends NotificationFactory {
    Notification createNotification() { return new SmsNotification(); }
}

// ---------------------------------------------
// 3️⃣ OBSERVER PATTERN
// ---------------------------------------------
// Use when: Many objects must be notified when one changes.
interface Observer { void update(String msg); }

class Channel {
    List<Observer> subs = new ArrayList<>();
    void subscribe(Observer o){ subs.add(o); }
    void upload(String v){ subs.forEach(s -> s.update(v)); }
}

class User implements Observer {
    private String name;
    User(String n){ name = n; }
    public void update(String v){ System.out.println(name + " notified of video: " + v); }
}

// ---------------------------------------------
// 4️⃣ SINGLETON
// ---------------------------------------------
// Use when: You need exactly one global instance (config, logger, etc.)
class AppConfig {
    private static final AppConfig INSTANCE = new AppConfig();
    private AppConfig() {}
    public static AppConfig getInstance() { return INSTANCE; }
}

// ---------------------------------------------
// 5️⃣ BUILDER PATTERN
// ---------------------------------------------
// Use when: Object has many optional parameters.
class UserBuilderExample {
    private String name, email;
    private int age;

    private UserBuilderExample(Builder b){
        name=b.name; email=b.email; age=b.age;
    }

    static class Builder {
        String name,email; int age;
        Builder name(String n){ name=n; return this; }
        Builder email(String e){ email=e; return this; }
        Builder age(int a){ age=a; return this; }
        UserBuilderExample build(){ return new UserBuilderExample(this); }
    }

    public String toString(){ return name + " (" + email + "), " + age; }
}

// ---------------------------------------------
// 6️⃣ DECORATOR PATTERN
// ---------------------------------------------
// Use when: Add functionality dynamically without changing class.
interface Report { String generate(); }

class BaseReport implements Report {
    public String generate(){ return "Base Report"; }
}

class ExcelDecorator implements Report {
    private Report report;
    ExcelDecorator(Report r){ report=r; }
    public String generate(){ return report.generate() + " + Excel Export"; }
}

// ---------------------------------------------
// 7️⃣ FACADE PATTERN
// ---------------------------------------------
// Use when: Simplify usage of multiple complex subsystems.
class Bank { void transfer(String u, int a){ System.out.println("Bank transfer ₹" + a + " for " + u); } }
class Wallet { void debit(String u, int a){ System.out.println("Wallet debit ₹" + a + " for " + u); } }

class PaymentFacade {
    private final Bank bank = new Bank();
    private final Wallet wallet = new Wallet();
    void pay(String user, int amt){
        wallet.debit(user, amt);
        bank.transfer(user, amt);
    }
}

// ---------------------------------------------
// 8️⃣ STATE PATTERN
// ---------------------------------------------
// Use when: Object behavior changes based on internal state.
interface State { void handle(); }

class Draft implements State { public void handle(){ System.out.println("Document in Draft state"); } }
class Published implements State { public void handle(){ System.out.println("Document is Published"); } }

class Document {
    private State state = new Draft();
    void setState(State s){ state = s; }
    void publish(){ state.handle(); }
}

// ---------------------------------------------
// 9️⃣ CHAIN OF RESPONSIBILITY
// ---------------------------------------------
// Use when: Pass request through multiple handlers until one handles it.
abstract class Handler {
    private Handler next;
    Handler linkWith(Handler n){ next=n; return n; }
    void handle(String req){ if(next!=null) next.handle(req); }
}

class AuthHandler extends Handler {
    void handle(String req){
        if(req.contains("auth")) System.out.println("Authenticated");
        super.handle(req);
    }
}

class LogHandler extends Handler {
    void handle(String req){
        System.out.println("Logging request: " + req);
        super.handle(req);
    }
}
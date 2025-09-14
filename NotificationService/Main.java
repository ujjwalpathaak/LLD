package NotificationService;
/*
 * Question:
 * Design a Notification System in Java that can send notifications via multiple channels (Email, SMS, Push).
 * Requirements:
 * The system should support multiple notification channels (Email, SMS, Push) and allow adding new ones in the future without modifying existing code.
 * Each notification has a message, recipient, and priority (HIGH/LOW).
 * High-priority notifications should use a retry mechanism until they succeed.
 * The system should log all notifications sent.
 * Client code should be able to trigger notifications without worrying about which channel is used or how retries/logging work.
 */
class Logger{
    public void log(String log){
        System.out.println("LOG: " + log);
    }
}

interface Service {
    void send(String message, String recipient);
}

class Email implements Service{
    @Override
    public void send(String message, String recepient){
        System.out.println("EMAIL: " + message + " to " + recepient);
    }
}

class Sms implements Service{
    @Override
    public void send(String message, String recepient){
        System.out.println("SMS: " + message + " to " + recepient);
    }
}

class Push implements Service{
    @Override
    public void send(String message, String recepient){
        System.out.println("PUSH: " + message + " to " + recepient);
    }
}

class NotificationFactory{
    public static Service getNotificationService(String type){
        return switch (type) {
            case "EMAIL" -> new Email();
            case "SMS" -> new Sms();
            case "PUSH" -> new Push();
            default -> throw new IllegalArgumentException("Unknown channel: " + type);
        };
    }
}

class Notification{
    public void send(String type, String message, String recepient, String proprity){
        Service service = NotificationFactory.getNotificationService(type);
        if("HIGH".equals(proprity)){
            System.out.println("Sent to retry mechanism");
            service.send(message, recepient);
        }else{
            service.send(message, recepient);
        }
    }
}

public class Main {
    public static void main(String[] args){
        Notification notif = new Notification();
        notif.send("EMAIL", "msg1", "rec1", "LOW");
        notif.send("SMS", "msg2", "rec2", "LOW");
        notif.send("PUSH", "msg3", "rec3", "LOW");
        notif.send("EMAIL", "msg1", "rec1", "HIGH");
        notif.send("SMS", "msg2", "rec2", "HIGH");
        notif.send("PUSH", "msg3", "rec3", "HIGH");
    }
}
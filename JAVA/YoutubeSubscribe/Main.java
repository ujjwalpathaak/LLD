package YoutubeSubscribe;

import java.util.ArrayList;
import java.util.List;

interface IObservable {
    void subscribe(IObserver observer);
    void unsubscribe(IObserver observer);
    void notifySubscribers(String message);
}

interface IObserver {
    void update(String message);
}

class CChannel implements IObservable {
    private List<IObserver> subscribers = new ArrayList<>();

    @Override
    public void subscribe(IObserver observer) {
        subscribers.add(observer);
        System.out.println(observer + " subscribed!");
    }

    @Override
    public void unsubscribe(IObserver observer) {
        subscribers.remove(observer);
        System.out.println(observer + " unsubscribed!");
    }

    @Override
    public void notifySubscribers(String message) {
        for (IObserver subscriber : subscribers) {
            subscriber.update(message);
        }
    }
}

class CSubscriber implements IObserver {
    private String name;

    public CSubscriber(String name) {
        this.name = name;
    }

    @Override
    public void update(String message) {
        System.out.println(name + " received notification: " + message);
    }

    @Override
    public String toString() {
        return name;
    }
}

public class Main {
    public static void main(String[] args) {
        CChannel mrBeast = new CChannel();
        CSubscriber ujjwal = new CSubscriber("Ujjwal");
        CSubscriber john = new CSubscriber("John");

        mrBeast.subscribe(ujjwal);
        mrBeast.subscribe(john);

        mrBeast.notifySubscribers("New video uploaded!");
        mrBeast.unsubscribe(john);
        mrBeast.notifySubscribers("Another video uploaded!");
    }
}
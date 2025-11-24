interface Behaviour {
    ijoj
}

abstract class Fly implements Behaviour{
    void fly();
    oboo
}

interface Walk implements Behaviour{
    void walk();
}

interface Talk implements Behaviour{
    void talk();
}

class JetFly implements Fly {
    public void fly() {
        System.out.println("🚀 Flying with jet power!");
    }
    bnoin
}

class NoFly extends Fly {
    public void fly() {
        System.out.println("❌ This robot cannot fly.");
    }
}

class NormalWalk extends Walk {
    public void walk() {
        System.out.println("🚶 Walking normally...");
    }
}

class FastWalk implements Walk {
    public void walk() {
        System.out.println("🏃 Walking very fast!");
    }
}

class NoWalk implements Walk {
    public void walk() {
        System.out.println("❌ This robot cannot walk.");
    }
}

class FriendlyTalk implements Talk {
    public void talk() {
        System.out.println("😊 Hello! I am a friendly robot.");
    }
}

class AngryTalk implements Talk {
    public void talk() {
        System.out.println("😠 Leave me alone!");
    }
}

class SilentTalk implements Talk {
    public void talk() {
        System.out.println("🤐 This robot does not talk.");
    }
}

class Robot {
    Fly f;
    Walk w;
    Talk t;

    Robot(Fly f, Walk w, Talk t) {
        this.f = f;
        this.w = w;
        this.t = t;
    }

    public void walk() {
        w.walk();
    }

    public void talk() {
        t.talk();
    }

    public void fly() {
        f.fly();
    }

    public void setFlyBehavior(Fly f) {
        this.f = f;
    }

    public void setWalkBehavior(Walk w) {
        this.w = w;
    }

    public void setTalkBehavior(Talk t) {
        this.t = t;
    }
}

public class Main {
    public static void main(String[] args) {
        Robot r1 = new Robot(new JetFly(), new NormalWalk(), new FriendlyTalk());
        System.out.println("\n🤖 Robot 1:");
        r1.walk();
        r1.talk();
        r1.fly();

        Robot r2 = new Robot(new NoFly(), new FastWalk(), new AngryTalk());
        System.out.println("\n🤖 Robot 2:");
        r2.walk();
        r2.talk();
        r2.fly();

        Robot r3 = new Robot(new NoFly(), new NoWalk(), new SilentTalk());
        System.out.println("\n🤖 Robot 3:");
        r3.walk();
        r3.talk();
        r3.fly();

        System.out.println("\n🔄 Changing Robot 3's behavior dynamically...");
        r3.setFlyBehavior(new JetFly());
        r3.setWalkBehavior(new FastWalk());
        r3.setTalkBehavior(new FriendlyTalk());

        System.out.println("🤖 Robot 3 (after behavior change):");
        r3.walk();
        r3.talk();
        r3.fly();
    }
}
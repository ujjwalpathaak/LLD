class Singleton {
    private static Singleton obj = null;

    /*
     * The constructor needs to be private as an obj is already made then
     * constructor is called
     */
    private Singleton() {
        System.out.println("New object created!");
    }

    public static Singleton getInstance() {
        if (obj == null) {
            synchronized (Singleton.class) {
                if (obj == null) {
                    obj = new Singleton();
                }
            }
        }
        return obj;
    }
}

// ThreadSafeEagerSingleton
// public class Singleton {
//     private static Singleton instance = new Singleton();

//     private Singleton() {
//         System.out.println("Singleton Constructor Called!");
//     }

//     public static Singleton getInstance() {
//         return instance;
//     }
// }

public class SingletonDP {
    public static void main(String[] args) {
        Singleton s1 = Singleton.getInstance();
        Singleton s2 = Singleton.getInstance();

        System.out.println(s1 == s2);
    }
}

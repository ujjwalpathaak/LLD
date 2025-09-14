enum BurgerType { VEG, NONVEG, EGG, ALOO, PANEER, CHICKEN }

interface Burger {
    void prepare();
}

interface MealFactory {
    Burger createBurger(BurgerType type);
}

// --- Burgers for Singh Burger Factory ---
class VegBurger implements Burger {
    public void prepare() {
        System.out.println("Preparing veg burger");
    }
}

class NonVegBurger implements Burger {
    public void prepare() {
        System.out.println("Preparing non-veg burger");
    }
}

class EggBurger implements Burger {
    public void prepare() {
        System.out.println("Preparing egg burger");
    }
}

// --- Burgers for King Burger Factory ---
class AlooBurger implements Burger {
    public void prepare() {
        System.out.println("Preparing aloo burger");
    }
}

class PaneerBurger implements Burger {
    public void prepare() {
        System.out.println("Preparing paneer burger");
    }
}

class ChickenBurger implements Burger {
    public void prepare() {
        System.out.println("Preparing chicken burger");
    }
}

// --- Factories ---
class SinghBurgerFactory implements MealFactory {
    public Burger createBurger(BurgerType type) {
        switch(type) {
            case VEG:
                return new VegBurger();
            case NONVEG:
                return new NonVegBurger();
            case EGG:
                return new EggBurger();
            default:
                throw new IllegalArgumentException("Unknown burger type: " + type);
        }
    }
}

class KingBurgerFactory implements MealFactory {
    public Burger createBurger(BurgerType type) {
        switch(type) {
            case ALOO:
                return new AlooBurger();
            case PANEER:
                return new PaneerBurger();
            case CHICKEN:
                return new ChickenBurger();
            default:
                throw new IllegalArgumentException("Unknown burger type: " + type);
        }
    }
}

// --- Main Driver ---
public class FactoryDP { 
    public static void main(String[] args) {
        // Using SinghBurgerFactory
        MealFactory singhFactory = new SinghBurgerFactory();
        Burger burger = singhFactory.createBurger(BurgerType.VEG);  
        burger.prepare();
        burger = singhFactory.createBurger(BurgerType.NONVEG);  
        burger.prepare();
        burger = singhFactory.createBurger(BurgerType.EGG);  
        burger.prepare();

        System.out.println("---------------------------");

        // Using KingBurgerFactory
        MealFactory kingFactory = new KingBurgerFactory();
        burger = kingFactory.createBurger(BurgerType.ALOO);  
        burger.prepare();
        burger = kingFactory.createBurger(BurgerType.PANEER);  
        burger.prepare();
        burger = kingFactory.createBurger(BurgerType.CHICKEN);  
        burger.prepare();
    }
}
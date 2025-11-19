class Grinder {
    public void grindBeans() {
        System.out.println("Grinding coffee beans...");
    }
}

class Boiler {
    public void heatWater() {
        System.out.println("Boiling water...");
    }
}

class Brewer {
    public void brewCoffee() {
        System.out.println("Brewing coffee...");
    }
}

class CoffeeMachineFacade{
    private Grinder grinder;
    private Boiler boiler;
    private Brewer brewer;

    public CoffeeMachineFacade() {
        this.grinder = new Grinder();
        this.boiler = new Boiler();
        this.brewer = new Brewer();
    }

    public void makeEspresso() {
        System.out.println("Preparing espresso...");
        grinder.grindBeans();
        boiler.heatWater();
        brewer.brewCoffee();
        System.out.println("Espresso ready ☕");
    }

    public void makeLatte() {
        System.out.println("Preparing latte...");
        grinder.grindBeans();
        boiler.heatWater();
        brewer.brewCoffee();
        System.out.println("Adding milk...");
        System.out.println("Latte ready ☕🥛");
    }
}

public class CoffeeMaker_Facade {
    public static void main(String args[]){
        CoffeeMachineFacade machine = new CoffeeMachineFacade();
        machine.makeEspresso();
        System.out.println("-------------------");
        machine.makeLatte();
    }
}

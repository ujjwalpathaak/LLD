package DecoratorPractice.CoffeeShop;

abstract class Coffee{
    public int cost(){
        return 25;
    }

    public String description(){
        return "coffee";
    }
}

class Espresso extends Coffee{
    @Override
    public int cost(){
        return super.cost() + 10;
    }

    @Override
    public String description(){
        return "espresso" + " " + super.description();
    }
}

class Latte extends Coffee{
    @Override
    public int cost(){
        return super.cost() + 15;
    }

    @Override
    public String description(){
        return "latte" + " " + super.description();
    }
}

abstract class CoffeeDecorator extends Coffee{
    Coffee base;
    
    public CoffeeDecorator(Coffee base){
        this.base = base;
    }
    
    @Override
    public int cost(){
        return base.cost();
    }
    
    @Override
    public String description(){
        return base.description();
    }
}

class AddMilk extends CoffeeDecorator{
    public AddMilk(Coffee base){
        super(base);
    }

    @Override
    public int cost(){
        return super.cost() + 25;
    }

    @Override
    public String description(){
        return "milk, " + super.description();
    }
}

class AddCream extends CoffeeDecorator{
    public AddCream(Coffee base){
        super(base);
    }

    @Override
    public int cost(){
        return super.cost() + 15;
    }

    @Override
    public String description(){
        return "cream, " + super.description();
    }
}

class AddSugar extends CoffeeDecorator{
    public AddSugar(Coffee base){
        super(base);
    }

    @Override
    public int cost(){
        return super.cost() + 10;
    }

    @Override
    public String description(){
        return "sugar, " + super.description();
    }
}

class AddSyrup extends CoffeeDecorator{
    public AddSyrup(Coffee base){
        super(base);
    }

    @Override
    public int cost(){
        return super.cost() + 15;
    }

    @Override
    public String description(){
        return "syrup, " + super.description();
    }
}

public class Main {
    public static void main(String args[]){
        Coffee espresso = new AddMilk(new Espresso());
        System.out.println(espresso.description() + " -> ₹" + espresso.cost());
        
        Coffee latte = new AddSyrup(new AddCream(new AddMilk(new Latte())));
        System.out.println(latte.description() + " -> ₹" + latte.cost());
    }
}

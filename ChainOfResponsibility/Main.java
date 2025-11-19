package ChainOfResponsibility;

abstract class LoanHandler{
    LoanHandler nextHandler;

    public abstract void handle(int amount);
    public void setNextHandler(LoanHandler handler){
        this.nextHandler = handler;
    }
}

class Manager extends LoanHandler{
    @Override
    public void handle(int amount){
        if(amount <= 10000){
            System.out.println("Loan amount of " + amount + " handled by Manager");
        }
        else{
            nextHandler.handle(amount);
        }
    }
}

class Director extends LoanHandler{
    @Override
    public void handle(int amount){
        if(amount >= 10000 && amount <= 100000){
            System.out.println("Loan amount of " + amount + " handled by Director");
        }
        else{
            nextHandler.handle(amount);
        }
    }
}

class CTO extends LoanHandler{
    @Override
    public void handle(int amount){
        if(amount >= 100000){
            System.out.println("Loan amount of " + amount + " handled by CTO");
        }
        else{
            nextHandler.handle(amount);
        }
    }
}

public class Main {
    public static void main(String args[]){
        CTO cto = new CTO();
        Manager manager = new Manager();
        Director director = new Director();
        
        manager.setNextHandler(director);
        director.setNextHandler(cto);

        manager.handle(5000);
        manager.handle(20000);
        manager.handle(105000);
    }
}

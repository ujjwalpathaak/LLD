package LoggerSystem;

enum LogLevel {
    INFO,
    WARN,
    ERROR,
    DEBUG,
}

abstract class LogHandler{
    public void handle(LogLevel level, String message){
        // make new log message
        // if handled by current handler handle, 
        // if not pass to next handler
    }
}

class InfoHandler{
    
};

class WarnHandler{
    
};

class ErrorHandler{
    
};

class DebugHandler{
    
};

class Logger{
    private static final Logger INSTANCE = new Logger();
    LogHandler handler;

    private Logger(){}

    public static Logger getInstance(){
        return INSTANCE;
    }

    public void setConfig(){
        // sets config somehow - basically I need to set
        // which all handlers will handle which level
    }

    public void log(LogLevel level, String message){
        this.handler.handle(level, message);
    }
}

public class Main {
    public static void main(String args[]){
        Logger logger = Logger.getInstance();

        logger.setConfig();

        logger.log(LogLevel.ERROR, "This is error message");
        logger.log(LogLevel.INFO, "This is info message");
        logger.log(LogLevel.DEBUG, "This is debug message");
    }    
}

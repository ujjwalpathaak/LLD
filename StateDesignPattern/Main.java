package StateDesignPattern;

interface State{
    public abstract void currentState();
}

class Playing extends State{

}

class Paused extends State{
}

class MusicPlayer{
    private State state;
    private String song;

    public void setSong(String song){
        this.song = song;
    }

    public void pause(){
        this.state = .pause();
    }

    public void play(String song){
        this.song = song;
        this.state = ;
    }
}

public class Main {
    public static void main(String args[]){
        MusicPlayer player = new MusicPlayer();
        player.play("Jab we met");
        player.pause();
    }
}

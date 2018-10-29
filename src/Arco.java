import java.util.*;
public class Arco {
    private State from;
    private State to;
    private char symbol;

    public Arco(State from, char symbol, State to){
        this.from = from;
        this.to = to;
        this.symbol = symbol;

    }

    public char getSymbol() {
        return symbol;
    }
    public State getTo(){
        return to;
    }
    public State getFrom(){
        return from;
    }
    public void setTo(State state){ this.to = state; }
    public void setFrom(State state){this.from = state;}

    public void print(){
        System.out.println(this.from.getNameStr() + " -- " + this.symbol + " --> " + this.to.getNameStr());
    }
}

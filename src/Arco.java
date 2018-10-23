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

    public void print(){
        System.out.println(this.from.getNameStr() + " -- " + this.symbol + " --> " + this.to.getNameStr());
    }
}

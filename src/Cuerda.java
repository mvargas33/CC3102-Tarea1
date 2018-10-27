import java.util.ArrayList;

public class Cuerda {
    private ArrayList<State> from;
    private ArrayList<State> to;
    private char symbol;

    public Cuerda(State from, char symbol, State to){
        this.from.add(from);
        this.to.add(to);
        this.symbol = symbol;

    }
    public char getSymbol() {
        return symbol;
    }
    public ArrayList<State> getTo(){
        return to;
    }
    public ArrayList<State> getFrom(){
        return from;
    }

    public Cuerda(ArrayList<State> from, char symbol, ArrayList<State> to){
        this.from=from;
        this.to=to;
        this.symbol=symbol;
    }
}

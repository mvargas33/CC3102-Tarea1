import java.util.ArrayList;

public class Cuerda {
    private QState from;
    private QState to;
    private char symbol;

    public Cuerda(QState from, char symbol, QState to){
        this.from = from;
        this.to = to;
        this.symbol = symbol;

    }
    public char getSymbol() {
        return symbol;
    }
    public QState getTo(){
        return to;
    }
    public QState getFrom(){
        return from;
    }

}

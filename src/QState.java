import java.util.ArrayList;

public class QState {
    private ArrayList<State> states;
    private ArrayList<Cuerda> cuerdas;
    private int name;
    private boolean end;
    private boolean start;

    public QState(int name, State s){
        this(name);
        this.states.add(s);
    }
    public QState(int name){
        this(name, new ArrayList<>());
    }
    public QState(int name, ArrayList<State> states){
        this(name, states,false, false);
    }

    public QState(int name, ArrayList<State> states, boolean start, boolean end){
        this.states = states;
        this.cuerdas = new ArrayList<>();
        this.name = name;
        this.end = end;
        this.start = start;
    }

    public ArrayList<State> getStates() {
        return states;
    }

    public ArrayList<Cuerda> getCuerdas() {
        return cuerdas;
    }

    public int getName() {
        return name;
    }

    public String getNameStr(){
        return String.valueOf(name);
    }

    public boolean isEnd() {
        return end;
    }

    public boolean isStart() {
        return start;
    }

    public void setEnd(){
        this.end = true;
    }
    public void setStart(){this.start = true;}

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        for(int i = 0; i < states.size(); i++){
            sb.append(states.get(i).getNameStr());sb.append(' ');
        }
        sb.append('}');
        return sb.toString();
    }
}

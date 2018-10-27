import java.util.*;
public class State {
    private ArrayList<Arco> arcos;
    private int name;
    private boolean end;
    private boolean start;
    
    public State(int name){
        this.name = name;
        this.start = false;
        this.end = false;
    }
    
    public State(int name, boolean start, boolean end){
        this.name = name;
        this.end = end;
        this.start = start;
    }
    public void addArco(Arco a){
        this.arcos.add(a);
    }

    public int getName(){
        return this.name;
    }

    public String getNameStr(){
        return String.valueOf(this.getName());
    }

    public boolean isEnd(){
        return this.end;
    }

    public boolean isStart(){
        return this.start;
    }

    public ArrayList<Arco> getArcos(){
        return this.arcos;
    }

    public void changeToStart(){
        this.start = true;
    }

    public void changeToEnd(){
        this.end = true;
    }

    
}

import java.util.*;

public class AFND {
    private ArrayList<State> K;     // Lista de estados
    private ArrayList<Arco> delta;  // Lista de arcos
    private String sigma;           // Universo de letras
    private State s;                // Estado inicial
    private ArrayList<State> F;     // Lista de estados finales


    public AFND(String sigma){
        this(new ArrayList<>(), new ArrayList<>(), sigma, null, new ArrayList<>());
    }
    public AFND(ArrayList<State> K, ArrayList<Arco> delta, String sigma, State s, ArrayList<State> F){
        this.K = K;
        this.delta = delta;
        this.sigma = sigma;
        this.s = s;
        this.F = F;
    }

    public ArrayList<State> getK() {
        return K;
    }
    public ArrayList<Arco> getDelta() {
        return delta;
    }
    public State getS(){
        return s;
    }
    public ArrayList<State> getF() {
        return F;
    }



    public boolean isInSigma(char c){
        int n = sigma.length();
        for(int i=0; i<n; i++){
            if(sigma.charAt(i) == c){
                return true;
            }
        }
        return false;
    }

    public ArrayList<State> Thompson(Tree node){
        if(node == null) {
            ArrayList<State> start_end = new ArrayList<State>();
            State start = new State(K.size() - 1);
            State end = new State(K.size() - 1);
            start_end.add(start);
            start_end.add(end);
            this.K.add(start);
            this.K.add(end);
            return start_end;
        }

        else if(node.right==null && node.left==null && isInSigma(node.name)){
            ArrayList<State> start_end = new ArrayList<State>();
            State start = new State(K.size() - 1);
            State end = new State(K.size() - 1);
            Arco arc= new Arco(start, node.name, end);
            start.addArco(arc);
            this.delta.add(arc);
            this.K.add(start);
            this.K.add(end);
            start_end.add(start);
            start_end.add(end);
            return start_end;
        }
        else if (node.left!=null && node.right!=null && node.name=='｜'){
            ArrayList<State> start_end = new ArrayList<State>();
            ArrayList<State> start_end_left = Thompson(node.left);
            ArrayList<State> start_end_right = Thompson(node.right);
            State start = new State(K.size() - 1);
            State end = new State(K.size() - 1);
            this.K.add(start);
            this.K.add(end);
            Arco arc1 = new Arco(start, '#', start_end_left.get(0));
            Arco arc2 = new Arco(start, '#', start_end_right.get(0));
            start.addArco(arc1);
            start.addArco(arc2);
            Arco arc3 = new Arco(start_end_left.get(1), '#', end);
            Arco arc4 = new Arco(start_end_right.get(1), '#', end);
            start_end_left.get(1).addArco(arc3);
            start_end_right.get(1).addArco(arc4);
            this.delta.add(arc1);
            this.delta.add(arc2);
            this.delta.add(arc3);
            this.delta.add(arc4);
            start_end.add(start);
            start_end.add(end);
            return start_end;

        }
        else if (node.left!=null && node.right!=null && node.name=='.'){
            ArrayList<State> start_end = new ArrayList<State>();
            ArrayList<State> start_end_left = Thompson(node.left);
            ArrayList<State> start_end_right = Thompson(node.right);
            Arco arc1 = new Arco(start_end_left.get(1), '#', start_end_right.get(0));
            start_end_left.get(1).addArco(arc1);
            this.delta.add(arc1);
            start_end.add(start_end_left.get(0));
            start_end.add(start_end_right.get(1));
            return start_end;

        }
        else if (node.left!=null && node.right==null && node.name=='*'){
            ArrayList<State> start_end = new ArrayList<State>();
            State start = new State(K.size() - 1);
            State end = new State(K.size() - 1);
            this.K.add(start);
            this.K.add(end);
            ArrayList<State> start_end_left = Thompson(node.left);
            Arco arc1 = new Arco(start, '#', start_end_left.get(0));
            Arco arc2 = new Arco(start_end_left.get(1), '#', start_end_left.get(0));
            Arco arc3 = new Arco(start_end_left.get(1), '#', end);
            Arco arc4 = new Arco(start, '#', end);
            start.addArco(arc1);
            start_end_left.get(1).addArco(arc2);
            start_end_left.get(1).addArco(arc3);
            start.addArco(arc4);
            this.delta.add(arc1);
            this.delta.add(arc2);
            this.delta.add(arc3);
            this.delta.add(arc4);
            return start_end;
        }else{
            System.out.println("kgzo en el arbol");
            return null;
        }
    }



}

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
            ArrayList<State> start_end = new ArrayList<>();
            State start = new State(K.size());
            start_end.add(start);
            State end = new State(K.size());
            start_end.add(end);
            this.K.add(start);
            this.K.add(end);
            return start_end;
        }
        else if(node.right==null && node.left==null && isInSigma(node.name)){
            ArrayList<State> start_end = new ArrayList<>();
            State start = new State(K.size());
            this.K.add(start);
            State end = new State(K.size());
            this.K.add(end);
            Arco arc= new Arco(start, node.name, end);
            start.addArco(arc);
            this.delta.add(arc);
            start_end.add(start);
            start_end.add(end);
            return start_end;

        }
        else if (node.left!=null && node.right!=null && node.name=='|'){
            ArrayList<State> start_end = new ArrayList<>();
            ArrayList<State> start_end_left = Thompson(node.left);
            ArrayList<State> start_end_right = Thompson(node.right);
            State start = new State(K.size());
            this.K.add(start);
            State end = new State(K.size());
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
            ArrayList<State> start_end = new ArrayList<>();
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
            ArrayList<State> start_end = new ArrayList<>();
            State start = new State(K.size());
            this.K.add(start);
            State end = new State(K.size());
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
            start_end.add(start);
            start_end.add(end);
            return start_end;
        }else{
            System.out.println("Problema en el arbol");
            return null;
        }
    }

    // Elimina del AFND los estados que nunca son alcanzados (aquellos que no tienen ningún arco llegando a sí mismos)
    public void cleanUpAFND(){
        for(int i = 0; i < this.K.size(); i++){                     // Para cada estado
            State s = this.K.get(i);
            boolean noneArrives = true;                             // Se asume que nada llega a eĺ
            for(int j = 0; j < this.delta.size(); j++){             // Se busca entre todos los arcos
                Arco a = delta.get(j);
                if(a.getTo().getName() == s.getName()){             // Si es recepción de alguien
                    noneArrives = false;                            // Si lo es, se ignorará
                }
            }
            if(noneArrives && !s.isStart()){                        // Si no, se obtienen todos sus arcos salientes
                ArrayList<Arco> arcos_inutiles = s.getArcos();
                for(int r = 0; r < arcos_inutiles.size(); r++){
                    Arco inutil = arcos_inutiles.get(r);
                    this.delta.remove(inutil);                      // Y se eliminan del AFND
                }
                K.remove(s);                                        // Luego se elimina el estado inútil
            }
        }
    }

    public void ERtoAFND(Tree tree){
        ArrayList<State> states = this.Thompson(tree);
        this.s = states.get(0);this.s.setToStart();
        this.F.add(states.get(1));F.get(0).setToEnd();
        this.cleanUpAFND();
    }

    public void print(){
        System.out.println(" -- AFND -- ");
        System.out.println("Estado inicial: " + this.s.getNameStr());
        StringBuilder finales = new StringBuilder();
        for(int y = 0; y < this.F.size(); y++){
            finales.append(F.get(y).getNameStr());finales.append(" ");
        }
        System.out.println("Estado final  : " + finales.toString());

        for(int i = 0; i < this.K.size(); i++){
            //K.get(i).print();
            ArrayList<Arco> a = K.get(i).getArcos();
            for(int j = 0; j < a.size(); j++){
                a.get(j).print();
            }
        }
    }

    public void addCyclesToStart(){
        for(int i = 0; i < this.K.size(); i++){         // Para cada estado en el AFND
            State s = K.get(i);                         // Rescatar el estado (incluye el inicial)
            Arco a = new Arco(s, '#', this.s);  // Crear un arco entre el y el estado inicial con epsilon
            this.delta.add(a);                          // Añadir el arco al AFND
        }
    }

}

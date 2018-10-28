import java.util.ArrayList;

public class AFD {
    private ArrayList<ArrayList<State>> QState;     // Lista de QuateumStates --> QuantumState: Lista de estados
    private ArrayList<Cuerda> delta;                // Lista de Cuerdas --> Cuerda: Arco entre QuantumStates
    private String sigma;                           // Alfabeto
    private State s;                                // Estado inicial
    private ArrayList<State> F;                     // Lista de estados finales
    private int q;

    public AFD(String sigma){
        this.QState = new ArrayList<>();
        this.delta = new ArrayList<>();
        this.sigma = sigma;
        this.s = null;
        this.F = new ArrayList<>();
        this.q = 0;
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

    public void ThtoAFD(ArrayList<State> k_nd){
        int n=k_nd.size();
        if (k_nd==null){
            return;
        }
        for (int x=0; x<n; x++){
            State state=k_nd.get(x);
            ArrayList<State> qstates= new ArrayList<>(); // primer estado cuantico
            qstates = scope(state);
            for(int q=0; q<qstates.size(); q++){
                State firststate=qstates.get(q);
                ArrayList<ArrayList<State>> newStates = new ArrayList<>();
                for (int i=0; i<sigma.length(); i++) {
                    char s = sigma.charAt(i);
                    ArrayList<State> newQState = new ArrayList<>();

                    ArrayList<Arco> acc = firststate.getArcos();
                    for (int u = 0; u < acc.size(); u++) {
                        Arco arr = acc.get(u);
                        if (arr.getSymbol() == s) {
                            State to = arr.getTo();
                            newQState.add(to);
                        }
                    }
                    if (!newQState.isEmpty()) {
                        newStates.add(newQState);
                    }
                }

                for(int r = 0; r < newStates.size(); r++){
                    ArrayList<State> oneState = newStates.get(r);
                    ThtoAFD(oneState);
                }



            }



        }

    }

    public ArrayList<State> scope(State state){
        ArrayList<Arco> arcs = state.getArcos();
        ArrayList<State> quantums = new ArrayList<>();
        quantums.add(state);

        for (int a =0; a<arcs.size(); a++){
            Arco arc=arcs.get(a);
            State fin=arc.getTo();
            if (arc.getSymbol()=='#' && !quantums.contains(fin)){
                quantums.add(fin);
                quantums.addAll(scope(fin));
            }

        }
        return quantums;
    }
}

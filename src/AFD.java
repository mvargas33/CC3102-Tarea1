import java.util.ArrayList;

public class AFD {
    private ArrayList<QState> QStates;  // Lista de QStates --> QState: Lista de estados
    private ArrayList<Cuerda> delta;    // Lista de Cuerdas --> Cuerda: Arco entre QStates
    private String sigma;               // Alfabeto
    private QState s;                   // Estado inicial
    private ArrayList<QState> F;        // Lista de estados finales
    private QState sumidero;            // Estado cuantico tipo sumidero
    private int q;

    public AFD(String sigma){
        this.QStates = new ArrayList<>();
        this.delta = new ArrayList<>();
        this.sigma = sigma;
        this.s = null;
        this.F = new ArrayList<>();
        this.sumidero = new QState(-1, new State(-1));
        delta.add(new Cuerda(sumidero, '$', sumidero));
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

    public void iterativeFinalQState(State estadoFinal){
        for(int i = 0; i < QStates.size(); i++){        // Para cada QState en el AFD
            QState qstate = QStates.get(i);
            ArrayList<State> s = qstate.getStates();    // Se extrae su lista de estados
            if(s.contains(estadoFinal)){                // Si contiene el estado final
                F.add(qstate);                          // Se marca como final
            }
        }
    }

    public QState exist(QState qstate){
        for(int i = 0; i < QStates.size(); i++){                // Para todos los estados del AFD
            QState aComparar = QStates.get(i);                  // Se obtiene el estado cuantico
            int numeroEstados = aComparar.getStates().size();   // Se calculan el numero de sus estados
            int statesInCommon = 0;                             // Se declaran en 0 los estados comunes
            if(numeroEstados == qstate.getStates().size()){     // Si tienen la misma cantidad de Estados
                for(int x = 0; x < numeroEstados; x++){         // Para cada estado
                    if(aComparar.getStates().contains(qstate.getStates().get(x))){  // Si el estado a comparar contiene el estado de qstate
                        statesInCommon++;                       // Se suma 1 en los estados comunes
                    }
                }
            }
            if(statesInCommon == numeroEstados){                // Si los estados comunes son todos
                return aComparar;                               // Se retorna el estado cuentico existente
            }
        }
        return null;
    }

    public void QStateRecursion(QState qState){
        for(int g = 0; g < sigma.length(); g++){                        // Para cada letra del alfabeto
            char c = sigma.charAt(g);                                   // Se rescata el símbolo
            QState newQState = new QState(this.QStates.size());         // Se crea un estado cuantico de llegada vacío
            for(int  j= 0; j < qState.getStates().size(); j++) {        // Para cada estado en la lista de estados de qState
                State stateInQState = qState.getStates().get(j);        // Se rescata el estado
                ArrayList<Arco> acc = stateInQState.getArcos();         // Se rescatan sus arcos
                for (int u = 0; u < acc.size(); u++) {                  // Para cada arco en el estado
                    Arco arr = acc.get(u);                              // Se rescata el arco
                    if (arr.getSymbol() == c) {                         // Si coincide con el símbolo buscado
                        State to = arr.getTo();                         // Se rescata el estado de llegada
                        if(!newQState.getStates().contains(to)){        // Si newQState no contiene al estado
                            newQState.getStates().add(to);              // Se añade al estado cuantico del símbolo a newQState
                            newQState.getStates().removeAll(scope(to)); // Se eliminan duplicados
                            newQState.getStates().addAll(scope(to));    // Se añaden las transiciones epsilon a newQState
                        }
                    }
                }
            }
            if(newQState.getStates().size() != 0){                      // Si el nuevo estado tiene al menos un estado
                QState check = exist(newQState);                        // Si el estado newQState existía se guarda en check
                if(check == null) {                                     // Si check es null, newQState no existe
                    this.QStates.add(newQState);                        // Se añade al AFD
                    Cuerda cuerda = new Cuerda(qState, c, newQState);   // Se crea una cuerda entre el estado cuantico original y el final con el símbolo analizado
                    this.delta.add(cuerda);                             // Se añade la cuerda al AFD
                    QStateRecursion(newQState);                         // Se crean nuevos estados cuanticos a partir del creado, si es posible
                }else{
                    Cuerda cuerda = new Cuerda(qState, c, check);       // Se crea una cuerda entre el estado cuantico original y el final con el símbolo analizado
                    this.delta.add(cuerda);                             // Se añade la cuerda al AFD, no se hace recursión porque ya se hizo!
                }
            }else{                                                      // Sino, creamos una cuerda entre el esato y el sumidero
                Cuerda haciaSumidero = new Cuerda(qState, c, sumidero); // Se crea la cuerda hacia el sumidero con el símbolo
                delta.add(haciaSumidero);                               // Se añade la cuerda hacia el sumidero al AFD
            }
        }
    }

    public void AFNDtoAFD(AFND nd){
        State estadoInicial = nd.getS();                                // Se rescata el estado inicial del AFND
        QState qState = new QState(this.QStates.size(), estadoInicial); // Se crea un estado cúantico con el estado como base
        s = qState;                                                     // Se declara como estado inicial
        qState.getStates().removeAll(scope(estadoInicial));             // Eliminar duplicados
        qState.getStates().addAll(scope(estadoInicial));                // Se añaden las transiciones epsilon al estado cuantico
        this.QStates.add(qState);                                       // Se añade el estado cuantico a AFD
        QStateRecursion(qState);                                        // Se generan los estados cuanticos creados a partir de qState
        ArrayList<State> f = nd.getF();                                 // Se extraen los estados finales
        for(int i = 0; i < f.size(); i++){                              // Para cada estado final del AFND
            iterativeFinalQState(f.get(i));                             // Se marcan como finales los del AFD
        }
    }


    // Retorna una lista de estados correspondiente a las transiciones epsilon de un estado
    public ArrayList<State> scope(State state){
        ArrayList<Arco> arcs = state.getArcos();        // Se obtienen los arcos de el estado
        ArrayList<State> quantums = new ArrayList<>();  // Se crea una lista de estados
        quantums.add(state);                            // Se añade el estado actual
        for (int a =0; a < arcs.size(); a++){           // Para cada arco en un estado
            Arco arc = arcs.get(a);
            State fin = arc.getTo();
            if (arc.getSymbol() == '#' && !quantums.contains(fin)){ // Si hay un arco con epsilon y la llegada no la agregué antes
                quantums.add(fin);                      // Se añade el estado de llegada
                quantums.removeAll(scope(fin));         // Se eliminan duplicados
                quantums.addAll(scope(fin));            // Añadir recursivamente las transciciones a partir de llegada
            }
        }
        return quantums;
    }

    public void print(){
        System.out.println(" -- AFD -- ");
        System.out.println("Estado inicial: " + s.getNameStr());
        StringBuilder finales = new StringBuilder();
        for(int y = 0; y < this.F.size(); y++){
            finales.append(F.get(y).toString());finales.append(" ");
        }
        System.out.println("Estados finales: " + finales);
        for(int i = 0; i < delta.size(); i++){
            delta.get(i).print();
        }
    }

}

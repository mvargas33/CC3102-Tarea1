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
        int a = QStates.size();
        for(int i = 0; i < QStates.size(); i++){        // Para cada QState en el AFD
            QState qstate = QStates.get(i);
            ArrayList<State> s = qstate.getStates();    // Se extrae su lista de estados
            if(s.contains(estadoFinal)){                // Si contiene el estado final
                qstate.setEnd();
                this.F.add(qstate);                          // Se marca como final
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
                            newQState.getStates().removeAll(scope(to, new ArrayList<>())); // Se eliminan duplicados
                            newQState.getStates().addAll(scope(to, new ArrayList<>()));    // Se añaden las transiciones epsilon a newQState
                        }
                    }
                }
            }
            if(newQState.getStates().size() != 0){                      // Si el nuevo estado tiene al menos un estado
                QState check = exist(newQState);                        // Si el estado newQState existía se guarda en check
                if(check == null) {                                     // Si check es null, newQState no existe
                    this.QStates.add(newQState);                        // Se añade al AFD
                    Cuerda cuerda = new Cuerda(qState, c, newQState);   // Se crea una cuerda entre el estado cuantico original y el final con el símbolo analizado
                    qState.getCuerdas().add(cuerda);
                    this.delta.add(cuerda);                             // Se añade la cuerda al AFD
                    QStateRecursion(newQState);                         // Se crean nuevos estados cuanticos a partir del creado, si es posible
                }else{
                    Cuerda cuerda = new Cuerda(qState, c, check);       // Se crea una cuerda entre el estado cuantico original y el final con el símbolo analizado
                    qState.getCuerdas().add(cuerda);
                    this.delta.add(cuerda);                             // Se añade la cuerda al AFD, no se hace recursión porque ya se hizo!
                }
            }else{                                                      // Sino, creamos una cuerda entre el esato y el sumidero
                Cuerda haciaSumidero = new Cuerda(qState, c, sumidero); // Se crea la cuerda hacia el sumidero con el símbolo
                qState.getCuerdas().add(haciaSumidero);
                delta.add(haciaSumidero);                               // Se añade la cuerda hacia el sumidero al AFD
            }
        }
    }

    public void AFNDtoAFD(AFND nd){
        State estadoInicial = nd.getS();                                // Se rescata el estado inicial del AFND
        QState qState = new QState(this.QStates.size(), estadoInicial); // Se crea un estado cúantico con el estado como base
        qState.getStates().removeAll(scope(estadoInicial, new ArrayList<>()));  // Eliminar duplicados
        qState.getStates().addAll(scope(estadoInicial, new ArrayList<>()));     // Se añaden las transiciones epsilon al estado cuantico
        this.QStates.add(qState);                                       // Se añade el estado cuantico a AFD
        this.s = qState;                                                     // Se declara como estado inicial
        this.s.setStart();
        QStateRecursion(qState);                                        // Se generan los estados cuanticos creados a partir de qState
        ArrayList<State> f = nd.getF();                                 // Se extraen los estados finales
        for(int i = 0; i < f.size(); i++){                              // Para cada estado final del AFND
            iterativeFinalQState(f.get(i));                             // Se marcan como finales los del AFD
        }
    }


    // Retorna una lista de estados correspondiente a las transiciones epsilon de un estado
    public ArrayList<State> scope(State state, ArrayList<State> quantums){
        if(!quantums.contains(state)) {
            quantums.add(state);                            // Se añade el estado actual
        }
        ArrayList<Arco> arcs = state.getArcos();            // Se obtienen los arcos de el estado
        for (int a =0; a < arcs.size(); a++){               // Para cada arco en un estado
            Arco arc = arcs.get(a);
            State fin = arc.getTo();                        // Se obtiene el estado de llegada
            if (arc.getSymbol() == '#' && !quantums.contains(fin)){ // Si hay un arco con epsilon y la llegada no la agregué antes
                quantums.add(fin);                          // Se añade el estado de llegada
                //quantums.removeAll(scope(fin, quantums));   // Se eliminan duplicados
                quantums.addAll(scope(fin, quantums));      // Añadir recursivamente las transciciones a partir de llegada
                quantums = eliminarRepetidos(quantums);
            }
        }

        return quantums;
    }

    public ArrayList<State> eliminarRepetidos(ArrayList<State> estados){
        ArrayList<State> s = new ArrayList<>();
        for(State estado : estados){
            if(!s.contains(estado)){
                s.add(estado);
            }
        }
        return s;
    }

    public void print(){
        System.out.println(" -- AFD -- ");
        System.out.println("Estado inicial: " + s.toString());
        StringBuilder finales = new StringBuilder();
        for(int y = 0; y < this.F.size(); y++){
            finales.append(F.get(y).toString());finales.append(" ");
        }
        System.out.println("Estados finales: " + finales);
        for(int i = 0; i < delta.size(); i++){
            delta.get(i).print();
        }
    }

    public ArrayList<Integer> run(String texto){
        ArrayList<Integer> marcas = new ArrayList<>();              // Lista vacía de marcas en el texto
        QState estadoActual = this.s;                               // Se comienza en estado inicial
        int posicionActual = 0;                                     // Posición actual del caracter que se lee del texto
        int posicionFinal = texto.length() - 1;                     // Posición final del caracter del texto
        while(posicionActual <= posicionFinal){                     // Hasta que no se recorra el texto completo
            ArrayList<Cuerda> cuerdas = estadoActual.getCuerdas();  //
            char s = texto.charAt(posicionActual);
            for(int j = 0; j < cuerdas.size(); j++){
                Cuerda c = cuerdas.get(j);
                if(c.getSymbol() == s){
                    estadoActual = c.getTo();break;
                }else if(c.getSymbol() == '$'){
                    estadoActual = c.getTo();break;
                }
            }
            if(estadoActual.isEnd()){
                marcas.add(posicionActual);
            }
            posicionActual++;
        }
        return marcas;
    }

}

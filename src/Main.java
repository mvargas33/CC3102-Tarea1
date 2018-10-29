import java.util.*;

public class Main {

    public static void main(String args []){
        String texto = "ababababab";

        // Recepción del input de la expresión regular

        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        String [] input = s.split("\n");
        char [] reg_exp = input[0].toCharArray(); // Regular expresion

        // Declaración del alfabeto y creación de un árbol de la Expresión Regular

        String universe = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 \n";
        Thompson th = new Thompson(universe, reg_exp);
        Tree tri = th.createTree();

        // Creación del AFND a partir del árbol de la Expresión Regular
/*
        AFND nd = new AFND(universe);   // Se define el alfabeto del AFND
        nd.ERtoAFND(tri);               // Se le pasa el árbol que contiene la ER y se crea el AFND
        nd.addLoopsToStart();           // Se añaden los loops al inicio
        //nd.print();

        // Creación del AFD a partir del AFND

        AFD d = new AFD(universe);      // Se define el alfabeto del AFD
        d.AFNDtoAFD(nd);                // Se crea el AFD a partir del AFND
        //d.print();                      // Print de AFD
        ArrayList<Integer> marcasFinales = d.run(texto);
*/
        /***********************************/

        AFND ndRev = new AFND(universe);
        ndRev.ERtoAFND(tri);
        ndRev.reverse();
        ndRev.print();

        AFD dRev = new AFD(universe);
        dRev.AFNDtoAFD(ndRev);
        dRev.print();
        System.out.println(dRev.run(reverseString(texto)));



    }

    public static String reverseString(String string){
        StringBuilder s = new StringBuilder();
        for(int i = string.length() - 1; i >= 0; i--){
            s.append(string.charAt(i));
        }
        return s.toString();
    }



}

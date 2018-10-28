import java.util.*;

public class Main {

    public static void main(String args []){

        // Recepción del input de la expresión regular

        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        String [] input = s.split(" ");
        char [] reg_exp = input[0].toCharArray(); // Regular expresion

        // Declaración del alfabeto y creación de un árbol de la Expresión Regular

        String universe = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 \n";
        Thompson th = new Thompson(universe, reg_exp);
        Tree tri = th.createTree();

        // Creación del AFND a partir del árbol de la Expresión Regular

        AFND nd = new AFND(universe);
        nd.Thompson(tri);
        nd.print();
        nd.cleanUpAFND();
        nd.print();


    }




}
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.util.*;
import java.io.*;
import java.nio.file.*;

public class Main {

    public static void main(String args []){
        String texto = readAllBytesJava7("./input.txt");    // Arreglar
        System.out.println(texto);

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

        AFND nd = new AFND(universe);   // Se define el alfabeto del AFND
        nd.ERtoAFND(tri);               // Se le pasa el árbol que contiene la ER y se crea el AFND
        nd.addLoopsToStart();           // Se añaden los loops al inicio
        //nd.print();

        // Creación del AFD a partir del AFND

        AFD d = new AFD(universe);      // Se define el alfabeto del AFD
        d.AFNDtoAFD(nd);                // Se crea el AFD a partir del AFND
        //d.print();                      // Print de AFD
        ArrayList<Integer> marcasFinales = d.run(texto);
        //System.out.println(marcasFinales);

        /***********************************/

        AFND ndRev = new AFND(universe);
        ndRev.ERtoAFND(tri);
        //ndRev.addLoopsToStart();
        ndRev.reverse();
        //ndRev.print();

        AFD dRev = new AFD(universe);
        dRev.AFNDtoAFD(ndRev);
        //dRev.print();
        ArrayList<Integer> marcasIniciales = dRev.runReverse(texto, marcasFinales);
        //System.out.println(marcasIniciales);
        //System.out.println(dRev.runReverse(texto));
        /*
        for( int i = 0; i < dRev.runrev(texto ,marcasFinales ).size() ; i++){
            System.out.println(dRev.runrev(texto ,marcasFinales ).get(i));
        }*/

        ArrayList<int[]> intervalos = getIntervalos(marcasIniciales, marcasFinales);
        printIntervalos(intervalos);


    }

    private static String readAllBytesJava7(String filePath){
        String content = "";
        try
        {
            content = new String ( Files.readAllBytes(Paths.get(filePath) ) );
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return content;
    }

    public static ArrayList<int[]> getIntervalos(ArrayList<Integer> inicios, ArrayList<Integer> finales){
        ArrayList<int[]> intervalos = new ArrayList<>();
        int n = finales.size();
        for(int i = 0; i < n; i++){
            int[] a = new int[2];
            a[0] = inicios.get(i);
            a[1] = finales.get(i);
            intervalos.add(a);
        }
        return intervalos;
    }

    public static void printIntervalos(ArrayList<int[]> intervalos){
        for(int i = 0; i < intervalos.size(); i++){
            int[] t = intervalos.get(i);
            System.out.print('[' + String.valueOf(t[0]) + ',' + String.valueOf(t[1]) + ']' + ' ');
        }
    }
}

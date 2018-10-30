import java.lang.reflect.Array;
import java.nio.file.Files;
import java.util.*;
import java.io.*;

public class Main {

    public static void main(String args []){
        //String texto = fileToString();
        String texto = fileToString("src/input.txt");

        char[] reg_exp = inputToCharArray();

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
        printIntervalos(intervalos, texto);


    }

    public static String fileToString(){
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            try {
                Scanner sc = new Scanner(System.in);
                System.out.println("Ingresar RUTA de archivo de texto .txt, ejemplo: 'src/'");
                String ruta = sc.nextLine();
                System.out.println("Ingresar NOMBRE de archivo de texto .txt, ejemplo: 'input.txt'");
                String archivo = sc.nextLine();
                FileInputStream fis = new FileInputStream(ruta + archivo);
                byte data[] = new byte[fis.available()];
                fis.read(data);
                fis.close();
                return new String(data);
            } catch (Exception e) {
                e.printStackTrace(); // Si hay error se imprime
            }
        }catch(Exception e) {
            e.printStackTrace(); // Si hay error se imprime
        }
        return "";
    }

    public static String fileToString(String archivo){
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            try {
                FileInputStream fis = new FileInputStream(archivo);
                byte data[] = new byte[fis.available()];
                fis.read(data);
                fis.close();
                return new String(data);
            } catch (Exception e) {
                e.printStackTrace(); // Si hay error se imprime
            }
        }catch(Exception e) {
            e.printStackTrace(); // Si hay error se imprime
        }
        return "";
    }

    public static char[] inputToCharArray(){
        System.out.println("Ingresar la expresión regular en correcta notación, ejemplo: |..aza*.tx");
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        String [] input = s.split("\n");
        return input[0].toCharArray(); // Regular expresion
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

    public static void printIntervalos(ArrayList<int[]> intervalos, String texto){
        for(int i = 0; i < intervalos.size(); i++){
            int[] t = intervalos.get(i);
            StringBuilder s = new StringBuilder();
            for(int j = t[0]; j <= t[1]; j++){
                s.append(texto.charAt(j));
            }
            String intervalo = "[ " + String.valueOf(t[0]) + "," + String.valueOf(t[1]) + " ]";
            if(intervalo.length() <= 7) {
                System.out.println("[ " + String.valueOf(t[0]) + "," + String.valueOf(t[1]) + " ]" + "\t\t: " + s.toString());
            }else{
                System.out.println("[ " + String.valueOf(t[0]) + "," + String.valueOf(t[1]) + " ]" + "\t: " + s.toString());
            }
        }
    }
}

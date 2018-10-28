import java.util.*;

public class Main {

    public static void main(String args []){
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        String [] input = s.split(" ");
        char [] reg_exp = input[0].toCharArray(); // Regular expresion

        String universe = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 \n";
        Thompson th = new Thompson(universe, reg_exp);
        Tree tri = th.createTree();



    }




}

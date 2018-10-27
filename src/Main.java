import java.util.*;
// DEEPIN




public class Main {

    public static void main(String args []){
        Scanner sc= new Scanner(System.in);
        String s=sc.nextLine();
        String [] input=s.split(" ");
        String text=input[0];
        char [] reg_exp = input[1].toCharArray(); // Regular expresion
        Tree tri = createTree(reg_exp);

    }
    public static boolean isInUniverse(char c){
        String universe = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 \n";
        int n = universe.length();
        for(int i=0; i<n; i++){
            if(universe.charAt(i) == c){
                return true;
            }
        }
        return false;
    }

    public static Tree createTree(char [] input){
        Stack<Tree> stack=new Stack();  // Stack vacío
        int n=input.length; // Calculo de simbolos
        for (int i=n; i>-1; i--){
          if(isInUniverse(input[i])){ // Si es simbolo
            Tree node = new Tree(input[i]); // Nodo con char
            stack.push(node); // Añadir al stack
          }else{
            stack = pushOperator(stack, input, i);  // Si es operador
            if(stack == null){  // Si no es nada, retorna error
              System.out.println("ERROR: La cagaste viejito --> Stack null al leer operando");
              return null;
            }
          }
        }
        Tree tree= stack.pop();  // Arbol final
        if(!stack.isEmpty()){ // Error
          System.out.println("ERROR: La cagaste viejita --> Stack no null al terminar");
          return null;
        }
        return tree;
    }


    // Retorna un stack de arboles con los operandos aplicados
    public static Stack<Tree> pushOperator(Stack<Tree> stack, char[] in, int i){
      char op =in[i];
      if (op==('*')){
        Tree lson = stack.pop();  // Sacar expresion del stack
        Tree node = new Tree(op, lson, null); // Crear nodo con op, e hijo izq expresion
        stack.push(node); // Se apila
        return stack;
      }else if (op==('|') || op==('.')){
        Tree ldaughter = stack.pop();
        Tree rdaughter = stack.pop();
        Tree node = new Tree(op, ldaughter, rdaughter);
        stack.push(node);
        return stack;
      }else{
        System.out.println("ERROR DE INPUT");
        return null;
      }
    }


}

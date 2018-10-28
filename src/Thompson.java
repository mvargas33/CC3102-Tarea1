import java.util.*;

public class Thompson {
    private String universe;
    private char[] reg_exp;
    private Tree reTree;

    public Thompson(String universe, char[] re){
        this.universe = universe;
        this.reg_exp = re;
    }

    public boolean isInUniverse(char c){
        int n = this.universe.length();
        for(int i=0; i<n; i++){
            if(this.universe.charAt(i) == c){
                return true;
            }
        }
        return false;
    }

    public Tree createTree(){
        Stack<Tree> stack = new Stack();  // Stack vacío
        int n = this.reg_exp.length; // Calculo de simbolos
        for (int i = n; i > -1; i--){
            if(isInUniverse(this.reg_exp[i])){ // Si es simbolo válido
                Tree node = new Tree(this.reg_exp[i]); // Nodo con char
                stack.push(node); // Añadir al stack
            }else{
                stack = pushOperator(stack, this.reg_exp, i);  // Si es operador
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

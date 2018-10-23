
public class Tree {
    public char name;
    public Tree right;
    public Tree left;

    public Tree(){
        this.name=' ';
    }

    public Tree(char c){
        this.name=c;
    }

    public Tree(char c, char l, char r){
        this.name=c;
        this.right=new Tree(r);
        this.left=new Tree(l);
    }

    public Tree(char c, Tree l, Tree r){
        this.name=c;
        this.right=r;
        this.left=l;
    }

    public void printTree(){
        System.out.println(this.left.name);
        System.out.println(this.name);
        System.out.println(this.right.name);
    }

}

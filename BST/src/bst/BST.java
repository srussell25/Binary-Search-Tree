package bst;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;
public class BST {
    public static void main(String[] args) throws FileNotFoundException { 
      
        int MAXSIZE = 32767;
          Scanner console = new Scanner(System.in);
          System.out.print("Enter the number of elements in the tree: ");
          int size = console.nextInt();
          binarySearchTree tree = new binarySearchTree();
          tree.buildTree(size);
          
          console.close();
          
          Random consoleRand = new Random();
          int[] randomKeys = new int[1000];
          int[] randomData = new int [1000];
          int i = 0;
          while (i<1000){
              randomKeys[i] = consoleRand.nextInt(MAXSIZE);
              randomData[i] = consoleRand.nextInt(MAXSIZE);
              i++;
          }
          long startTime = System.currentTimeMillis();
            i = 0;
            while(i < 1000){
                tree.naiveRMQ(tree.root, randomKeys[i], randomData[i]);
                i++;
            }
            long endTime = System.currentTimeMillis();
            System.out.println("Naive RMQ took " + (endTime - startTime) + " milliseconds");
          startTime = System.currentTimeMillis();
            i = 0;
          while (i <1000){
              tree.smartRMQ(tree.root, randomKeys[i], randomData[i]);
              i++;
          }
          endTime = System.currentTimeMillis();
          System.out.println("Smart RMQ took " + (endTime - startTime) 
          + " milliseconds to execute the same 1000 randomly generated queries");
          
//        try(
//            Scanner inFile = new Scanner(new FileReader("inputFile.txt"))){
//            int totalOps = inFile.nextInt();
//            for (int i = 0; i < totalOps; i++){
//                if(inFile.hasNext("(IN)")){
//                inFile.next();
//                tree.binarySearchInsert(inFile.nextInt(), inFile.nextInt());
//            }
//            else if(inFile.hasNext("(RMQ)")){
//                    inFile.next();
//                    tree.rangeMinQuery(inFile.nextInt(), inFile.nextInt());
//                    }
//            }
//        }
    }
    
}

class binarySearchTree{
    static class Node{
        public int key;
        public int mindata;
        public int data;
        public Node left;
        public Node right;
        public Node parent;
        
        public Node(int key, int data, Node parent){
            this.key = key;
            this.parent = parent;
            this.data = data;
            this.left = null;
            this.right = null;
        }
        public Node(int key, int data){ 
            this.key = key; 
            this.data = data;
            this.left = null;
            this.right = null;
            this.parent = null;
        }
    }
    
    Node root; 
    int size;
    
    binarySearchTree(){
        root = null;
        size = 0;
    }
    binarySearchTree(int key, int data){
        root = new Node(key, data);
        size = 1;
    }
    void binarySearchInsert(int key, int data){
        size++;
        root = insert(root, key, data);
    }
    void buildTree(int size){
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < size; i++){
            int MAXSIZE = 32767;
            Random rand = new Random();
            int rnKey = rand.nextInt(MAXSIZE);
            int rnData = rand.nextInt(MAXSIZE);
            binarySearchInsert(rnKey, rnData);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Buildtime for Binary String Tree of size " + size 
                + " was " + (endTime - startTime) + " milliseconds");
    }
    int getSize(){
        return size;
    }
    
    Node insert(Node node, int key, int data){
        if(node == null){
            node = new Node(key, data);
            node.mindata = node.data;
            return node;
        }
        else if (node.key < key){
            node.mindata = findMin(node.mindata, data);
            node.right = insert(node.right, key, data);
        }
        else{
            node.mindata = findMin(node.mindata, data);
            node.left = insert(node.left, key, data);
        }
        return node;
    }
    void setMinData(Node j){
        if (j != null)
            j.mindata = j.data;
        if(j.left != null)
            j.mindata = findMin(j.mindata, j.left.mindata);
        if (j.right != null)
            j.mindata = findMin(j.mindata, j.left.mindata);
        
    }
    void rangeMinQuery (int k1, int k2){
//        Global.currentSmallest = root.data;
//        naiveRMQ(root, k1, k2);
//        System.out.println(Global.currentSmallest);
        smartRMQ(root, k1, k2);
        System.out.println(Global.Min);
    }
    void naiveRMQ(Node node, int k1, int k2){
        if (node == null)
            return;
        if (node.key < k1 == false)
            naiveRMQ(node.left, k1, k2);
        if (node.key >= k1 && node.key <= k2){
            if(node.data < Global.currentSmallest)
                Global.currentSmallest = node.data;
        }
        if (node.key > k2 == false)
            naiveRMQ(node.right, k1, k2);
    }
    void smartRMQ(Node node, int k1, int k2){
        if(k1 > k2)
            return;
        Node x = node; 
        while((k1 <= x.key && x.key <= k2)==false){
            if (k1 < x.key && k2 < x.key)
                if(x.left == null)
                    break;
                else
                    x = x.left;
            else 
                if(x.right == null)
                    break;
                else 
                    x = x.right;
        }
        Global.Min = x.data;
        RangeMinRight(k1, x.left);
        RangeMinLeft(k2, x.right);
    }
    void RangeMinRight(int k1, Node j){
        if (j == null)
            return;
        else{
            if (k1 < j.key){
                if(j.left == null)
                    Global.Min = findMin(Global.Min, j.data);
                else
                    Global.Min = findMin(Global.Min, j.data, j.left.mindata);
                RangeMinRight(k1, j.left);
            }
            else if (k1 > j.key)
                RangeMinRight(k1, j.right);
            else{
                if(j.right == null)
                    Global.Min = findMin(Global.Min, j.data);
            else
                 Global.Min = findMin(Global.Min, j.data, j.right.mindata);
            }
        }
    }
    void RangeMinLeft(int k2, Node j){
        if (j == null)
            return;
        else{
            if (k2 < j.key){
                if(j.left == null)
                    Global.Min = findMin(Global.Min, j.data);
                else
                    Global.Min = findMin(Global.Min, j.data, j.left.mindata);
                RangeMinLeft(k2, j.right);
            }
            else if (k2 > j.key)
                RangeMinLeft(k2, j.left);
            else{
                if(j.left == null)
                    Global.Min = findMin(Global.Min, j.data);
                else 
                    Global.Min = findMin(Global.Min, j.data, j.left.mindata);
            }
        }
    }
    int findMin(int a, int b){
        if(a <= b)
            return a;
        else
            return b;
    }
    int findMin(int a, int b, int c){
        if(a <= b)
            if(a <= c)
                return a;
            else 
                return c;
        else if (b <= c)
            return b;
        else 
            return c;
    }
    
    void binaryInOrder(){
        System.out.println("inorder Traversal:");
        inOrder(root);
    }
    void inOrder(Node node){
        if (node == null)
            return;
        inOrder(node.left);
        System.out.println(node.key);
        inOrder(node.right);
        return;
    }
}

class Global{
    public static int currentSmallest;
    public static int Min;
}

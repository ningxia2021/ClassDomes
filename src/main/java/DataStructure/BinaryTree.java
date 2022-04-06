package DataStructure;

/**
 * 2022.04.06
 * 二叉树的实现
 * 前序遍历：即先遍历根节点，再遍历左子树节点，再遍历右子树节点；
 */

//定义二叉树的节点属性
class btNode {
    //        内容
    private char val;
    //        左子树
    private btNode leftChild;
    //    右子树
    private btNode rightChild;

    //    构造方法
    public btNode(char val) {
        this.val = val;
    }

    public char getVal() {
        return val;
    }

    public btNode getLeftChild() {
        return leftChild;
    }

    public btNode getRightChild() {
        return rightChild;
    }

    public void setVal(char val) {
        this.val = val;
    }

    public void setLeftChild(btNode leftChild) {
        this.leftChild = leftChild;
    }

    public void setRightChild(btNode rightChild) {
        this.rightChild = rightChild;
    }
}

class tree {
    //    构造一颗树型结构 用来演示一些功能
    public btNode creatTree() {
        btNode A = new btNode('A');
        btNode B = new btNode('B');
        btNode C = new btNode('C');
        btNode D = new btNode('D');
        btNode E = new btNode('E');
        btNode F = new btNode('F');
        btNode G = new btNode('G');
        btNode H = new btNode('H');
        A.setLeftChild(B);
        A.setRightChild(C);
        B.setLeftChild(D);
        B.setRightChild(E);
        C.setLeftChild(F);
        C.setRightChild(G);
        E.setRightChild(H);
        return A;
    }

    //    前序遍历 根>左子树>右子树 所以正确的顺序应该是 A-B-D-E-H-C-F-G
    public void preOrder(btNode root) {
        if (root == null) {
            return;
        }
        System.out.print(root.getVal() + " ");
        preOrder(root.getLeftChild());
        preOrder(root.getRightChild());
    }

    //    中序遍历 左子树>根>右子树  正确顺序应该是 D-B-E-H-A-F-C-G
    public void inOrder(btNode root) {
        if (root == null) {
            return;
        }
        inOrder(root.getLeftChild());
        System.out.print(root.getVal() + " ");
        inOrder(root.getRightChild());
    }


    //      后序遍历 左子树>右子树>根 正确顺序为 D-H-E-B-F-G-C-A
    public void postOrder(btNode root) {
        if (root == null){
            return;
        }
        postOrder(root.getLeftChild());
        postOrder(root.getRightChild());
        System.out.print(root.getVal()+" ");
    }
}

public class BinaryTree {
    public static void main(String[] args) {
        tree t = new tree();
        btNode root = t.creatTree();
        System.out.print("前序遍历 : ");
        t.preOrder(root);
        System.out.println();
        System.out.print("中序遍历 : ");
        t.inOrder(root);
        System.out.println();
        System.out.print("后序遍历 : ");
        t.postOrder(root);
    }
}

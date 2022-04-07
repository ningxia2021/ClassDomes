package DataStructure;

/**
 * 2022.04.06
 * 二叉树的实现
 * 前序遍历：即先遍历根节点，再遍历左子树节点，再遍历右子树节点；
 * 2022.04.07
 * 获取节点个数
 * 获取叶子节点个数
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
    int count = 0;
    //    定义成静态的变量  类名就可以直接访问 不需要new 实例访问
    static int leadCount = 0;
    static int k_Count = 0;

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
        if (root == null) {
            return;
        }
        postOrder(root.getLeftChild());
        postOrder(root.getRightChild());
        System.out.print(root.getVal() + " ");
    }

    //    获取树中节点个数
    public int getSize1(btNode root) {
        if (root != null) {
            count++;
            getSize1(root.getLeftChild());
            getSize1(root.getRightChild());
        }
        return count;
    }

    //    高级的获取节点方法 子问题思路
    public int getSize2(btNode root) {
        if (root == null) {
            return 0;
        }
        return getSize2(root.getLeftChild()) + getSize2(root.getRightChild()) + 1;
    }

    //    求叶子节点数量 子问题思路：左子树叶子节点+右子树叶子节点个数 = 总的叶子节点个数
    //                 递归思路: 判断是不是叶子节点 不是就继续遍历 是的话就++
    public int getLeafNodeCount1(btNode root) {
        if (root == null) {
            return 0;
        }
        if (root.getRightChild() == null && root.getRightChild() == null) {
            leadCount++;
        } else {
            getLeafNodeCount1(root.getLeftChild());
            getLeafNodeCount1(root.getRightChild());
        }
        return leadCount;
    }

    public int getLeafNodeCount2(btNode root) {
        if (root == null) {
            return 0;
        }
        if (root.getRightChild() == null && root.getRightChild() == null) {
//            满足条件的就是叶子节点 计数器+1
            return 1;
        }
//        这一步相当于实在遍历整个树 前面的条件实在筛选符合条件的内容
        return getLeafNodeCount2(root.getLeftChild()) + getLeafNodeCount2(root.getRightChild());
    }

    //    计算第K层的节点个数
    public void getKLevelNodeCount(btNode root, int k) {
        if (k==1){

                k_Count++;
            return;
        }
        if (k > 1) {
            getKLevelNodeCount(root.getLeftChild(),k-1);
            getKLevelNodeCount(root.getRightChild(),k-1);
        }
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
        int size = t.getSize1(root);
        System.out.println();
        System.out.print("获取结点的个数方法 (递归) : ");
        System.out.println(size);
        System.out.print("获取结点的个数方法 (子问题) : ");
        System.out.println(t.getSize2(root));
        System.out.print("叶子节点的个数 (递归) : ");
        System.out.println(t.getLeafNodeCount1(root));
        System.out.print("叶子节点的个数 (子问题) : ");
        System.out.println(t.getLeafNodeCount2(root));
        System.out.print("计算第K层的节点个数 : ");
        System.out.println(t.k_Count);
    }

}

package DataStructure;

import org.omg.CORBA.Object;

import java.util.Arrays;

/**
 * 【数组】实现栈
 *
 * @time 2022.03.26 23：50
 */
public class demo3 {
    //    采用泛型的方式实现多种数据形式的栈结构
    static class myStack<E> {
        /**
         * 定义数组及容量
         */
        private Object[] array;
        private int size;

        public myStack() {
            array = new Object[5];
            this.size=0;
        }

        /**
         * 栈的功能:
         * 1.判断是否为空
         */
        public boolean isEmpty() {
//        size==0 本身就是在做判断；结果为真 返回true；反之 返回false
            return size == 0;
        }

        /**
         * 2.判断是否需要扩容
         */
        public void Expansion() {
            if (array.length == size) {
                array = Arrays.copyOf(array, array.length + 5);
            } else {
                return;
            }
        }

        /**
         * 压栈 push
         */
        public void push(E e) {
//        校验是否需要扩容
            Expansion();
            array[size] = (Object) e;
            size++;
        }

        /**
         * 打印
         */
        public void display() {
            for (int i = 0; i < size; i++) {
                System.out.print(array[i] + " ");
            }
            System.out.println();
        }

        /**
         * 出栈
         */
        public E pop() {
            if (isEmpty()) {
                System.out.println("数组为空，适可而止");
                return null;
            }
            E t = peek();
            if (size > 0) {
                array[size - 1] = null;
                size--;
            }
            return t;
        }

        /**
         * 取栈顶元素 peek
         */
        public E peek() {
            E e = (E) array[size - 1];
            return e;
        }
    }


    public static void main(String[] args) {
        myStack<String> myStack = new myStack<String>();
        myStack.push("1");
//        myStack.push(2);
//        myStack.push(3);
//        myStack.push(4);
//        myStack.push(4);
//        myStack.push(4);
        myStack.display();
//        myStack.pop();
//        myStack.display();
////        myStack.pop();
////        myStack.display();
//        System.out.println(myStack.peek());

    }
}

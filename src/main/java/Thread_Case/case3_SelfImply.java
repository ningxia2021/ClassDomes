package Thread_Case;

/**
 * 自己手动实现阻塞队列
 * 1.先实现队列
 * 2.再加上线程安全
 * 3.再加上在实现阻塞
 */
class myBlockingQueue{
    /**
     * 基于数组实现一个循环队列
     */
//    数组
    private String[] data;
//    头标记位
    private int head;
//    尾标记位
    private int tail;
//    已经被占用的长度
    private int size;

//    初始化
    public myBlockingQueue(int max){
        data = new String[max];
        this.head = 0;
        this.tail = 0;
    }

//    接口
//    0.判断是否为空
    public boolean isEmpty(){
        if (head==tail){
            return true;
        }else {
            return false;
        }
    }
//    1.判断是否为满
    public boolean isFull(){
        if (tail+1 == head){
            return true;
        }else{
            return false;
        }
    }

//    2.入队列
    public void push(String ele){
        if (isFull()){
//            如果满了，就会阻塞，不能再继续入
            return;
        }
        data[tail] = ele;
        tail++;
        size++;
    }

//    3.出队列
    public String pull(){
        if (isEmpty()){
//            如果为空，就会阻塞，等待有元素进入队列
            return null;
        }
        String ele = data[head];
        head++;
        size--;
        return ele;
    }

//    4.size大小
    public Integer gerSize(){
        return size;
    }

}

public class case3_SelfImply {

    public static void main(String[] args) {
        myBlockingQueue queue = new myBlockingQueue(10);
        queue.push("9");
        queue.push("2");
        queue.push("3");
        queue.push("4");
        String ret = queue.pull();
        System.out.println(ret);
        System.out.println(queue.gerSize());

    }

}

package java8;

import java.util.Scanner;

/**
 * Use Methods
 * Arrays.sort(Type[] a)
 */
public class ArraysSort {
    public static void main(String[] args) {
        /**
         * 抽奖游戏
         */
        System.out.println("你想要有多少个数？");
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
//        System.out.println(a);
        System.out.println("你想要抽几次？");
        int b = scanner.nextInt();
//        System.out.println(b);
        //创建总数并初始化
        int[] numbers = new int[a];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = i+1;
        }
        //创建要存放抽取数字的数组
        int[] result = new int[b];
        System.out.println("下面自动出号");
        for (int i = 0;i<result.length;i++){
            //Math.random()方法可以随机生成0-1中的虽有数字
            int r = (int)(Math.random()*a);
            //将下表为R的元素从numbers中取出来
            result[i] = numbers[r];
            //因为不可以有相同额号
            numbers[r] = numbers[a-1];
            a--;
        }
        for (int c :  result) {
            System.out.print(c+"、");
        }
        System.out.println();
        java.util.Arrays.sort(result);
        for (int c :  result) {
            System.out.print(c+"、");
        }
    }
}

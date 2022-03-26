package java2;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TcpEchoClient_1 {
    //建立连接
    Socket socket = null;

    public TcpEchoClient_1(String Server_ip,int Server_port) throws IOException {
    socket = new Socket(Server_ip,Server_port);
    }

    //启动客户端
    public void start() {
        System.out.println("客户端上线");
        Scanner scanner = new Scanner(System.in);
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
                while (true) {
                    System.out.print("->");
                    String request = scanner.nextLine();
                    //构造请求并发送
                    bufferedWriter.write(request + "\n");
                    bufferedWriter.flush();
                    //接收服务端响应
                    String response = bufferedReader.readLine();
                    System.out.println(response);
                }
            }catch (IOException e) {
                    e.printStackTrace();
                }
    }

    public static void main(String[] args) throws IOException {
        TcpEchoClient_1 clent = new TcpEchoClient_1("127.0.0.1",9090);
        clent.start();
    }
}

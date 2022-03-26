package Java1;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TcpEchoClient {

    Socket socket = null;
    //绑定ip 和 端口， 与服务端建立连接
    public TcpEchoClient(String ServerIp , int ServerPort) throws IOException {
        socket = new Socket(ServerIp,ServerPort);
    }

    public void start(){
        System.out.println("客户端启动");
        Scanner scanner = new Scanner(System.in);
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            while(true){
                System.out.print("->");
                String request = scanner.nextLine();
                if (request.equals("exit")){
                    break;
                }
                //发送请求
                bufferedWriter.write(request+"\n");
                bufferedWriter.flush();
                //接收响应
                String response = bufferedReader.readLine();
                //打印
                System.out.println(response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        TcpEchoClient Client = new TcpEchoClient("127.0.0.1",9090);
        Client.start();
    }
}

package Java1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpEchoServer {
    //初始化服务器，绑定IP
    ServerSocket serverSocket = null;

    public TcpEchoServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    //启动服务器
    public void start() throws IOException {
        System.out.println("服务器已启动");
        while (true){
            //从内核中获取连接，如果没有连接被获取就会阻塞等待，
            Socket clientSocket = serverSocket.accept();
            //处理请求
            ProcessConnet(clientSocket);
        }
    }

    private void ProcessConnet(Socket clientSocket) {
        System.out.printf("[%s,%d] 客户端上线\n",clientSocket.getInetAddress().toString(),clientSocket.getPort());
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))){
           while(true){
               //读取请求并解析
               String request = bufferedReader.readLine();
               //根据请求计算响应
               String response = process(request);
               //返回响应
               bufferedWriter.write(response + "\n");
               bufferedWriter.flush();
               System.out.printf("[%s,%d], rep:%s, req:%s \n",clientSocket.getInetAddress().toString(),clientSocket.getPort(),request,response);
           }
        }catch (IOException e) {
            //e.printStackTrace();
            System.out.printf("[%s,%d] 客户端下线\n",clientSocket.getInetAddress().toString(),clientSocket.getPort());
        }
    }

    private String process(String request) {
        return request;
    }

    public static void main(String[] args) throws IOException {
        TcpEchoServer tcpEchoServer = new TcpEchoServer(9090);
        tcpEchoServer.start();
    }
}

package java2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpEchoTheadServer_1 {

    ServerSocket serverSocket = null;
    //绑定端口,完成服务器初始化
    public TcpEchoTheadServer_1(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void start() throws IOException {
        System.out.println("服务器启动");
        //从内核中接收连接,没有连接就会阻塞等待连接
        while(true){
            final Socket socketClient = serverSocket.accept();
            //处理连接
            Thread tt = new Thread(new Runnable() {
                @Override
                public void run() {
                    ProcessConnetion(socketClient);
                }
            });
            tt.start();
        }
    }

    private void ProcessConnetion(Socket socketClient) {
        System.out.printf("[%s,%d],客户端上线 \n",socketClient.getInetAddress().toString(),socketClient.getPort());
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()))) {
        while(true){
            //读取并解析请求，此处按行读取，客户端发送的请求也要遵循这一自定义协议
            String request = bufferedReader.readLine();
            //计算响应
            String response = process(request);
            //给客户端返回响应
            bufferedWriter.write(response+"\n");
            //刷新缓冲区
            bufferedWriter.flush();
            System.out.printf("[%s,%d],req:%s, reps:%s \n",socketClient.getInetAddress().toString(),socketClient.getPort(),request,response);
        }
        }catch (IOException e) {
            System.out.printf("[%s,%d],客户端下线",socketClient.getInetAddress().toString(),socketClient.getPort());
        }
    }

    private String process(String request) {
        return request;
    }

    public static void main(String[] args) throws IOException {
        TcpEchoTheadServer_1 server = new TcpEchoTheadServer_1(9090);
        server.start();
    }

}

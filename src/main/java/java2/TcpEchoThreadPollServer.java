package java2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TcpEchoThreadPollServer {
    //绑定端口
    ServerSocket serverSocket = null;
    public TcpEchoThreadPollServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void start() throws IOException {
        System.out.println("服务器启动");
        ExecutorService executorService = Executors.newCachedThreadPool();
        while(true){
            //获取连接
            final Socket clentRequent = serverSocket.accept();
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    processConnect(clentRequent);
                }
            });
        }
    }

    private void processConnect(Socket clentRequent) {
        System.out.printf("[%s,%d],客户端上线 \n",clentRequent.getInetAddress(),clentRequent.getPort());

        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clentRequent.getInputStream()));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(clentRequent.getOutputStream()))) {
            while(true){
                //读取请求并解析
                String request = bufferedReader.readLine();
                //处理请求
                String response = process(request);
                //返回响应
                bufferedWriter.write(response+"\n");
                bufferedWriter.flush();

                System.out.printf("[%s,%d],req:%s, reps:%s \n",clentRequent.getInetAddress(),clentRequent.getPort(),request,response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String process(String request) {
        return request;
    }

    public static void main(String[] args) throws IOException {
        TcpEchoThreadPollServer server = new TcpEchoThreadPollServer(9090);
        server.start();
    }
}

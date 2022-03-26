package java3;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HTTPServerV2 {
    ServerSocket serverSocket = null;

    public HTTPServerV2(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void start() throws IOException {
        System.out.println("服务器已经启动");
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        while (true) {
            final Socket socket = serverSocket.accept();
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    ProcessConnetion(socket);
                }
            });
        }
    }

    private void ProcessConnetion(Socket socket) {
        //按照HTTP协议来读取请求
        //1.读取请求并解析
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            //a)读取首行
            String firstLine = bufferedReader.readLine();
            String[] splitFirstLine = firstLine.split(" ");
            //B)解析内容出来
            String Method = splitFirstLine[0];
            String Url = splitFirstLine[1];
            String Version = splitFirstLine[2];
            //2.读取header
            Map<String, String> header = new HashMap<>();
            String flag = "";
            //按行读取 按： 分割
            while ((flag = bufferedReader.readLine()) != null && (flag.length()) != 0) {
                String[] splitFlag = flag.split(": ");
                header.put(splitFlag[0], splitFlag[1]);
            }
            //读取body（暂不考虑）

            //打印日志
            System.out.printf("%s,%s,%s\n", Method, Url, Version);
            for (Map.Entry<String, String> entry : header.entrySet()) {
                System.out.println(entry.getKey() + ":" + entry.getValue());
            }
            System.out.println();
            //3.计算响应
            String resp = null;
            if (Url.equals("/index")) {
                bufferedWriter.write(Version + " 200 OK\n");
                resp = "<h1>index</h1>";
            } else if (Url.equals("/not")) {
                bufferedWriter.write(Version + "404 Not Found \n");
                resp = "<h1>404 Not Found</h1>";
            } else if (Url.equals("/123")) {
                bufferedWriter.write(Version + " 303 See Other\n");
                bufferedWriter.write("Location: http://www.hao123.com\n");
                resp = "";
            }else if (Url.equals("/1")){
                bufferedWriter.write(Version+"200 OK\n");
                resp = "<h>111111111111</h>";
            }else if (Url.equals("/2")){
                bufferedWriter.write(Version+"200 OK\n");
                resp = "<h>2222222222</h>";
            }else if (Url.equals("/3")){
                bufferedWriter.write(Version+"200 OK\n");
                resp = "<h>333333333</h>";
            }
            else {
                bufferedWriter.write(Version + "default\n");
                resp = "<h1>default</h1>";
            }
            //4.返回至客户端
            bufferedWriter.write("Content-Type: text/html\n");
            bufferedWriter.write("Content-Length: " + resp.getBytes().length + "\n");
            bufferedWriter.write("\n");
            bufferedWriter.write(resp);
            bufferedWriter.flush();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        HTTPServerV2 SERVER2 = new HTTPServerV2(9090);
        SERVER2.start();
    }
}

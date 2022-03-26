package java3;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServerV1 {
    //绑定端口
    ServerSocket serverSocket = null;

    public HttpServerV1(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void start() throws IOException {
        System.out.println("服务器启动");
        //创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        while (true) {
            //获取连接
            final Socket client = serverSocket.accept();
            //线程池调用多线程方法
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    processConnect(client);
                }
            });
        }
    }

    private void processConnect(Socket client) {
        //HTTP协议是文本协议，所以还是要用字节流来处理信息交互
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))) {
            //下面按照HTTP协议进行操作
            //1.读取请求并解析
            //a)解析首行，三个部分使用空格分割
            String firstLine = bufferedReader.readLine();
            String[] firstLineToken = firstLine.split(" ");
            String method = firstLineToken[0];
            String url = firstLineToken[1];
            String version = firstLineToken[2];
            //b)解析header，按行读取，按照冒号+空格来分割键值对
            //因为header是键值对形式，所有这里new一个键值对集合header出来
            Map<String,String> headers = new HashMap<>();
            String line = null;
            while((line = bufferedReader.readLine()) != null && line.length() != 0){
                //数据切分
                String[] headTocken = line.split(": ");
                //数据保存至哈希表
                headers.put(headTocken[0],headTocken[1]);
            }
            //c)解析body 【暂不考虑】

            //加上日志 观察内容是否正确
            //首行
            System.out.printf("%s %s %s \n",method,url,version);
            //header
            for (Map.Entry<String,String> entry : headers.entrySet()) {
                System.out.println(entry.getKey()+":"+entry.getValue());
            }
            //空行
            System.out.println();
            //body

            //2.根据请求计算响应

            String resp = "";

            if(url.equals("/ok")){
                bufferedWriter.write(version+" 200 ok\n");
                resp = "<h1>hello man</h1>";
            }else if (url.equals("/seeother")){
                bufferedWriter.write(version+" 303 seeother\n");
                bufferedWriter.write("Location : http://www.baidu.com\n");
                resp = " ";
            }else if (url.equals("/notfound")){
                bufferedWriter.write(version+" 404 notfound\n");
                resp = "<h1>404 Not Found</h1>";
            }else{
                bufferedWriter.write(version+" 200 ok\n");
                resp = "<h1>default</h1>";
            }
            //3.把响应写回给客户端
            bufferedWriter.write("Content-Type:text/html\n");
            // 此处的长度, 不能写成 resp.length(), 得到的是字符的数目, 而不是字节的数目
            bufferedWriter.write("Content-Lenth:"+resp.getBytes().length+"\n");
            bufferedWriter.write("\n");
            bufferedWriter.write(resp);
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public static void main(String[] args) throws IOException {
        HttpServerV1 httpServerV1 = new HttpServerV1(9090);
        httpServerV1.start();
    }
}

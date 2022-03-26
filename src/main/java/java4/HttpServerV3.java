package java4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServerV3 {
    //绑定端口
    ServerSocket serverSocket = null;

    public HttpServerV3(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void start() throws IOException {
        System.out.println("服务器启动");
        while (true) {
            ExecutorService executorService = Executors.newCachedThreadPool();
            final Socket client = serverSocket.accept();
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    ProcessConnection(client);
                }
            });
        }
    }

    private void ProcessConnection(Socket client) {
        System.out.println("客户端上线");
        try {
            //读取解析请求
            HttpRequest request = HttpRequest.bulid(client.getInputStream());
            System.out.println("request" + request);
            HttpResponse response = HttpResponse.bulid(client.getOutputStream());
            response.setHeader("Content-Type", "text/html");
            //根据请求计算响应
            if (request.getUrl().startsWith("/hello")) {
                response.setVersion("HTTP/1.1");
                response.setStatus(200);
                response.setMessages("ok ");
                response.writeBody("<h1>hello</h1>");

            } else if (request.getUrl().startsWith("/calc")) {
                //计算参数内容
                //根据参数名称获取参数值
                String aStr = request.getParameter("a");
                String bStr = request.getParameter("b");
                int a = Integer.parseInt(aStr );
                int b = Integer.parseInt(bStr);
                int result = a+b;
                System.out.println("a:" + aStr +";"+"b:" + bStr);
                response.writeBody("<h1> result = " + result + "</h1>");
                response.setVersion("HTTP/1.1");
                response.setMessages("ok");
                response.setStatus(200);
            }else if (request.getUrl().startsWith("/cookieUser")){
                response.setStatus(200);
                response.setMessages("ok");
                response.setVersion("HTTP/1.1");
                response.setHeader("Set-Cookie","user = gaohua");
                response.writeBody("<h1>Set Cookie User</h1>");
            }else if(request.getUrl().startsWith("/cookieTime")){
                response.setMessages("ok");
                response.setVersion("HTTP/1.1");
                response.setStatus(200);
                response.setHeader("Set-Cookie","time = "+(System.currentTimeMillis()/1000));
                response.writeBody("<h1> Set Cookie Time</h1>");
            }
            else {
                response.setStatus(200);
                response.setMessages("ok");
                response.setVersion("HTTP/1.1");
                response.writeBody("<h1>default</h1>");
            }
            //上传响应
            response.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        HttpServerV3 serverV3 = new HttpServerV3(9090);
        serverV3.start();
    }
}




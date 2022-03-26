package java7;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private class User {
        private String name;
        private int age;
        private String tele;
    }

    private Map<String, User> Sessions = new HashMap<>();

    //绑定端口
    private ServerSocket serverSocket = null;
    //构造方法，使new 对象的时候绑定端口
    public HttpServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void Start() throws IOException {
        System.out.println("服务器已启动");
        //建立线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
        while (true) {
            //等待与客户端建立连接
            final Socket client = serverSocket.accept();
            System.out.println("客户端启动");
            executorService.execute(new Runnable() {
                @Override 
                public void run() {
                    System.out.println("启动线程");
                    ProcessConnection(client);
                    System.out.println("进入process");
                }
            });
        }
    }

    private void ProcessConnection(Socket client) {
        try {
            //初始化方法
            Httprequest request = Httprequest.build(client.getInputStream());
            HttpResponse response = HttpResponse.build(client.getOutputStream());
            //处理请求
            if (request.getMETHOD().equalsIgnoreCase("POST")) {
                System.out.println("post");
                doPOST(request, response);
            }
            if (request.getMETHOD().equalsIgnoreCase("GET")) {

                System.out.println("get");
                doGET(request, response);
            }
            //处理完请求 返回响应给客户端
            response.flush();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            try {
                //最后使用完Socket 记得要关闭
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void doGET(Httprequest request, HttpResponse response) throws IOException {
        if (request.getURL().startsWith("/index.html")) {
            System.out.println("进入get方法");
            //检查是否有SessionId
            String SessionId = request.getHEADERS("Cookie");
            if (SessionId == null) {
                //首次登录
                System.out.println("首次登录： "+SessionId);
                response.setVersion("HTTP/1.1");
                response.setStatus(200);
                response.setMessages("ok");
                response.setHeaders("Content-Type", "text/html; charset=utf-8");
                //找index.html文件  返回至网页
                InputStream inputStream = HttpServer.class.getClassLoader().getResourceAsStream("index.html");
                System.out.println("inputStream"+inputStream);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response.writeBody(line + "\n");
                }
                bufferedReader.close();
            } else {
                //已经由Cookie 直接登录即可
                response.setVersion("HTTP/1.1");
                response.setStatus(200);
                response.setMessages("已经登录无需重复登录");
                response.setHeaders("Content-Type", "text/html; charset=utf-8");
                response.writeBody("<html>");
                response.writeBody("<div>无需登录，已经登录过了</div>");
                response.writeBody("</html>");
            }
        }

        //下面写其他的path的方法
    }

    private void doPOST(Httprequest request, HttpResponse response) {
        if (request.getURL().startsWith("/login")) {
            String username = request.getPARAMETERS("username");
            String password = request.getPARAMETERS("password");
            if (username.equals("gaoh") && password.equals("123")) {
                response.setVersion("HTTP/1.1");
                response.setStatus(200);
                response.setMessages("登录成功" + "\n");
                response.setHeaders("Content-Type", "text/html; charset=utf-8");
                //搞Cookie
                String SessionId = UUID.randomUUID().toString();
                //为这个SessionId和用户名密码建立联系
                User user = new User();
                user.name="gaoh";user.age=18;user.tele="8513246";
                Sessions.put(SessionId,user);
                //给客户端返回去一个cookie回去
                response.setHeaders("Set-Cookie","SessionId="+SessionId);
                response.writeBody("<html>");
                response.writeBody("<div>欢迎登录 我的大兄弟 " + username + "</div>");
                response.writeBody("</html>");
            } else {
                response.setVersion("HTTP/1.1");
                response.setStatus(200);
                response.setMessages("登录失败");
                response.setHeaders("Content-Type", "text/html; charset=utf-8");
            }
        }
        //其他/path响应的内容
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = new HttpServer(9090);
        server.Start();
    }
}

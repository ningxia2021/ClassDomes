package java5;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 2022.03.03
 * Http服务器的阶段性胜利
 * java1-java5 就这一部分指的细看
 * 1.Http 2.Session 3.Cookie 4.哈希表 5.Tcp
 */
public class HttpServerV4 {

    //定义一个用户特征
    private class User {
        private String username;
        private int age;
        private String school;
        private String tele;

        @Override
        public String toString() {
            return "User{" +
                    "username='" + username + '\'' +
                    ", age=" + age +
                    ", school='" + school + '\'' +
                    ", tele='" + tele + '\'' +
                    '}';
        }
    }

    //创建一个放用户信息的session哈希表
    HashMap<String, User> sessions = new HashMap<>();

    //绑定端口
    private ServerSocket serverSocket = null;

    public HttpServerV4(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void start() throws IOException {
        System.out.println("服务器启动");
        //建立连接
        ExecutorService executorService = Executors.newCachedThreadPool();
        while (true) {
            final Socket client = serverSocket.accept();
            //处理连接
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    ProcessConnection(client);
                }
            });
        }
    }

    private void ProcessConnection(Socket client) {
        System.out.println("与客户端已建立连接");
        //读取并解析请求
        try {
            HttpRequestV4 request = HttpRequestV4.bulid(client.getInputStream());
            HttpResponseV4 response = HttpResponseV4.build(client.getOutputStream());
            //根据请求计算响应
            //这一块按照请求方法类型对做出的响应进行的分类处理
            if (request.getMETHOD().equalsIgnoreCase("GET")) {

                doGET(request, response);

            } else if (request.getMETHOD().equalsIgnoreCase("POST")) {

                doPOST(request, response);

            } else {

                response.setStatus(405);
                response.setMessages("Not Found Method");

            }
            //将响应反馈至客户端
            response.flush();

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void doPOST(HttpRequestV4 request, HttpResponseV4 response) {

        //处理login逻辑
        if (request.getURL().startsWith("/login")) {

            //验证登录
            String username = request.getParameters("username");
            String password = request.getParameters("password");

            if (username.equals("gh") && password.equals("123")) {

                System.out.println("登录成功");
                response.setVersion("HTTP/1.1");
                response.setStatus(200);
                response.setMessages("ok");
                response.setHeaders("Content-Type","text/html; charset=utf-8");

                //创建一个session来记录
                String sessionId = UUID.randomUUID().toString();

                User user = new User();
                user.username = "gh";
                user.age = 18;
                user.school = "西安科技";
                user.tele = "8513246";
                sessions.put(sessionId, user);
                System.out.println(sessionId);
                //将sessionId传给Cookie
                response.setHeaders("Set-Cookie", "sessionId=" + sessionId);


                response.Writebody("<html>");
                response.Writebody("<div>欢迎登录" + username + "</div>");
                response.Writebody("</html>");

            } else {
                System.out.println("登录失败");
                response.setVersion("HTTP/1.1");
                response.setStatus(403);
                response.setMessages("Forbidden");
                response.setHeaders("Content-Type","text/html; charset=utf-8");
                response.Writebody("<html>");
                response.Writebody("<div>登录失败</div>");
                response.Writebody("</html>");
            }
        }
        //处理其他path逻辑

    }

    private void doGET(HttpRequestV4 request, HttpResponseV4 response) throws IOException {
        if (request.getURL().startsWith("/index.html")) {

            String sessionId = request.getCookies("sessionId");
//            System.out.println("sessionId = "+sessionId);
            User user = sessions.get(sessionId);

            if (user == null|| sessionId == null) {
//                System.out.println("没有获取到sessionId，第一次登录");
                response.setVersion("HTTP/1.1");
                response.setStatus(200);
                response.setMessages("ok");
                /**
                 * Content-Type 这里一定要拼对，否则页面不会跳转
                 */
                response.setHeaders("Content-Type","text/html; charset=utf-8");

                //找这个[文件]
                InputStream inputStream = HttpServerV4.class.getClassLoader().getResourceAsStream("index.html");
                //放进 bufferedReader
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                //把文件内容写入到响应的body
                while ((line = bufferedReader.readLine()) != null) {
                    response.Writebody(line + "\n");
                }
                //关闭字符流
                bufferedReader.close();

            } else {
//                System.out.println("获取到sessionId");
                //已经登录
                response.setVersion("HTTP/1.1");
                response.setStatus(200);
                response.setMessages("ok");
                response.setHeaders("Content-Type","text/html; charset=utf-8");
                response.Writebody("<html>");
                response.Writebody("<div>您已经登录 , 无需再次登录!   用户名： " + user.username + "</div>");
                response.Writebody("<div>" + "学校： "+user.school + "  年龄： " + user.age + "  电话：" + user.tele + " </div>");
                response.Writebody("</html>");

            }

        }

    }

    public static void main(String[] args) throws IOException {
        HttpServerV4 serverV4 = new HttpServerV4(9090);
        serverV4.start();
    }
}

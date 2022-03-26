package java6;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 复现汤老师的HttpServerV3版本 第一次
 * 出现以下问题：
 * 1.form表单传的参数接收不到；因为Request 的getter方法传参那块弄错
 * 2.SessionId接收不到
 */
public class ReviewServer {

    private class User {
        private String name;
        private String password;

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }

    /**
     * 应该算是绑定，一种映射
     */
    private HashMap<String, User> Sessions = new HashMap<>();

    /**
     * 绑定端口
     */
    ServerSocket serverSocket = null;

    public ReviewServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void start() throws IOException {
        System.out.println("服务器已启动");
        //创建线程池
//        ExecutorService executorService = Executors.newCachedThreadPool();
        ExecutorService executorService = Executors.newCachedThreadPool();
        while (true) {
            //建立连接
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
        System.out.println("与客户端建立连接");
        try {
            /**
             * 初始化方法
             */
            HttpRequestReview request = HttpRequestReview.build(client.getInputStream());
            HttpResponseReview response = HttpResponseReview.build(client.getOutputStream());
            /**
             * 将请求按照方法类型进行分类处理
             */
            if (request.getMethod().equalsIgnoreCase("POST")) {

                doPOST(request, response);

            } else if (request.getMethod().equalsIgnoreCase("GET")) {

                doGET(request, response);

            } else {

                response.setStatus(403);
                response.setMessages("Not Found 403");

            }
            /**
             * 将响应上传至客户端
             */
            response.flush();

        }catch (IOException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void doGET(HttpRequestReview request, HttpResponseReview response) throws IOException {

        if (request.getUrl().startsWith("/index.html")) {
            /**
             * 先判断有没有sessionID,SessionId是保存再Cookies哈希表中的键
             * 进而看是不是第一次登录
             */
//            String SessionId = request.getCookies("SessionId");
            String SessionId = request.getHeaders("Cookie");
            System.out.println("SessionId="+SessionId);
            User user = Sessions.get(SessionId);

            if (SessionId == null ) {
                //返回一个html文件
                response.setVersion("HTTP/1.1");
                response.setStatus(200);
                response.setMessages("index.html");
                response.setHeaders("Content-Type", "text/html; charset=utf-8");
                /**
                 * 找文件 通过类对象的类加载器 至resource文件中找 资源文件
                 */
                InputStream inputStream = ReviewServer.class.getClassLoader().getResourceAsStream("index.html");
                /**
                 * 读文件
                 */
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                /**
                 * 显示文件,这里读的是html文件，写回body就会以html形式显示在网页上
                 */
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response.writeBody(line + "\n");
                }
                /**
                 * 用完记得关
                 */
                bufferedReader.close();
            } else {
                System.out.println("已经登录");
                response.setVersion("HTTP/1.1");
                response.setStatus(200);
                response.setMessages("登录界面");
                response.setHeaders("Content-Type", "text/html; charset=utf-8");
                response.writeBody("<html>");
                response.writeBody("<div> 您已经登录"+user.name+" </div>");
                response.writeBody("</html>");
            }
        }else {
            response.setStatus(404);
            response.setMessages("Not Found");
        }
    }

    private void doPOST(HttpRequestReview request, HttpResponseReview response) {

        if (request.getUrl().startsWith("/login")) {

//            String body = request.getBody();
//            System.out.println("method = "+request.getMethod());
//            System.out.println("body = "+body);
//            System.out.println("/login Parameters = "+request.getParameters("username"));
//            System.out.println("/login Parameters = "+request.getParameters("password"));

            /**
             * 提取请求中的用户名+密码
             * 与数据库中的记录进行比对验证 正确性
             */
            String username = request.getParameters("username");
            String password = request.getParameters("password");
            /**
             * 数据写死，验证代码正确性
             */
            System.out.println("用户名 = "+username);
            System.out.println("密码 = "+ password);

            if (username.equals("gh") && password.equals("123")) {

                System.out.println("登录成功");
                response.setVersion("HTTP/1.1");
                response.setStatus(200);
                response.setMessages("登录成功");
                response.setHeaders("Content-Type", "text/html; charset=utf-8");
                response.writeBody("<html>");
                response.writeBody("<div>登录成功了，欢迎" + username + " 入场</div>");
                response.writeBody("</html>");
                /**
                 *搞一个sessionId来代表用户名 密码
                 * 同时 把sessionId传给cookie
                 * 这样就避免了明文 用户民密码再cookie里
                 */
                String SessionId = UUID.randomUUID().toString();
                User user = new User();
                user.name = username;
                user.password = password;
                Sessions.put(SessionId, user);
//                System.out.println("Sessions = "+Sessions);
                /**
                 * 这里设置响应头键格式为Set-Cookie，才可以传给网站请求头Cookie字段
                 */
                response.setHeaders("Set-Cookie", SessionId);
                response.writeBody("<html>");
                response.writeBody("<div>username = " + user.name + "  password = " + user.password + "</div>" + "\n");
                response.writeBody("</html");

            } else {
                System.out.println("登录失败");
                response.setVersion("HTTP/1.1");
                response.setStatus(200);
                response.setMessages("登录失败");
                response.setHeaders("Content-Type", "text/html; charset=utf-8");
                response.writeBody("<html>");
                response.writeBody("<div>登录失败</div>");
                response.writeBody("</html>");
            }
        }

        //下面分支还可以写别的path 带来的不同逻辑
    }

    public static void main(String[] args) throws IOException {
        ReviewServer server = new ReviewServer(9090);
        server.start();
    }
}

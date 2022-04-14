package Servlet录播学习笔记;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@WebServlet("/postJson")
public class PostParameterJson extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json;charset=utf-8");

        String body = readBody(req);

        resp.getWriter().write(body);
    }

    private String readBody(HttpServletRequest req) throws IOException {
//        拿到流对象
        InputStream inputStream = req.getInputStream();
//        获取body的长度
        int contentLength = req.getContentLength();
//        定义body长度的缓冲区
        byte[] buffer = new byte[contentLength];
//        将流对象写入到buffer中
        inputStream.read(buffer);
//        返回连续的字符串
//        当前是把整个body视为一个整体的字符串了，实际上是需要解析json格式的body，从里面拿到需要的键值对
//        更好的办法是直接使用第三方库，Jackson
        return new String(buffer, "utf-8");
    }
}

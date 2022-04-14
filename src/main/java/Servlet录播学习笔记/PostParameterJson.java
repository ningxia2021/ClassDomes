package Servlet录播学习笔记;


import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

//通过这个类来表示解析后的结果
class JsonData{
    public int username;
    public int passwd;
}

@WebServlet("/postJson")
public class PostParameterJson extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//        resp.setContentType("application/json;charset=utf-8");

        String body = readBody(req);

//        使用Jackson来解析
//        先创建一个Jackson的核心对象
        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.readValue(转化谁, 转化成啥样)
//        此处的大致过程为：1.先把body转化为类似HashMap的键值对结构
//                      2.根据类对象(JsonData.class)获取要转换成啥样的类(都有哪些属性，每个属性的名字)
//                      3.拿着JsonData.class中的每个属性中名字去body转化处的HashMap里面查
//                      4.如果查到了，就把查询到的值赋值给JsonData.class里对应的属性。循环进行这个过程.
//                      5.既然这个过程是要对应名字和body中的key是一样的才可以匹配，那么就要在构造这个类的同时，要整属性和body中要的参数一致！
        JsonData jsonData = objectMapper.readValue(body, JsonData.class);
//        resp.getWriter().write(body);
        resp.getWriter().write(String.format("username : %s , passwd = %s <br>",jsonData.username,jsonData.passwd));
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
//        使用Jackson 需要引入maven依赖
//        使用Jackson解析Json的时候需要明确把body这个字符串转成一个啥样的对象

        return new String(buffer, "utf-8");
    }
}

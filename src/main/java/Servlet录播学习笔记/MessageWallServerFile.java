package Servlet录播学习笔记;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 实现数据持久化存储的方式  1.写入硬盘  2.写入数据库
 * 这里实现写入硬盘的操作
 */
class json {
    public String from;
    public String to;
    public String mes;
}

@WebServlet("/messageFile")
public class MessageWallServerFile extends HttpServlet {
    ObjectMapper objectMapper = new ObjectMapper();
    String filePath = "P:/Idea工作目录/java/mes.txt";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        拉取消息


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        提交消息
//        1.用jackson将json数据转为字符串
        json message = objectMapper.readValue(req.getInputStream(), json.class);
//        2.将消息存入mes.txt中
        save(message);
//        3.定义响应格式
        resp.setContentType("application/json;charset=utf-8");
//        4.返回状态信息
        resp.getWriter().write("{\"ok\":1}");
    }

    //        这里进行写文件操作
    private void save(json message) {
        System.out.println("文件写入！");
//        FileWriter 的使用类似与 PrintWriter
//        new FileWriter(filePath,true)   后面这个true表示每次追加写入；如果没有则每次写入都是清空之前的内容，然后再写入新内容♥
        try (FileWriter fileWriter = new FileWriter(filePath,true)) {
//            写入文件的格式也有很多格式，可以直接写入json 也可以写入行文本
            fileWriter.write(message.from+"\t"+message.to+"\t"+message.mes+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

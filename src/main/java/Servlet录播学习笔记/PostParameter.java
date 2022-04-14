package Servlet录播学习笔记;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/postParameter")
public class PostParameter extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        格式
        resp.setContentType("text/html;charset=utf-8");
//        post请求的参数在body中 query string格式
        String username = req.getParameter("username");
        String password = req.getParameter("password");
//        返回响应
        resp.getWriter().write(String.format("username : %s , password : %s <br>",username,password));
//        接下来去网页body中构造post请求
    }
}

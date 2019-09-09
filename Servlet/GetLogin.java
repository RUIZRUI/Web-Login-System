import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.net.URLDecoder;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/我今年278/GetLogin")
public class GetLogin extends HttpServlet{
    private static final long serialVersionUID = 1L;

    // 构造函数
    public GetLogin(){
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        // 如果不存在 session 会话，则创建一个 session
        HttpSession session = request.getSession(true);

        // 设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        response.setContentType("text/html; charset=utf-8");

        PrintWriter out = response.getWriter();
        String title = "当前登录信息";
        String docType = "<!DOCTYPE html>\n";
        out.println(docType +
            "<html lang='zh'>\n" +
            "<head><title>" + title + "</title></head>\n" +
            "<body style=\"background-image: url('http://qixqi.club/images/body.jpg')\">\n" +
            "<h1 align='center'>" + title + "</h1>\n");

        if(session.getAttribute("username") != null){   
            int id = (Integer)session.getAttribute("id");
            String username = URLDecoder.decode((String)session.getAttribute("username"), "UTF-8");
            String email = (String)session.getAttribute("email");
            String password = (String)session.getAttribute("password");
            Date register_time = (Date)session.getAttribute("register_time");
            Date last_login_time = (Date)session.getAttribute("last_login_time");
            int iconID = (Integer)session.getAttribute("iconID");
            out.println("<p align='center'>id: " + id + "<br />");
            out.println("username: " + username + "<br />");
            out.println("email: " + email + "<br />");
            out.println("password: " + password + "<br />");
            out.println("register_time: " + df.format(register_time) + "<br />");
            out.println("last_login_time: " + df.format(last_login_time) + "<br />");
            out.println("iconID: " + iconID + "<br /></p></body></html>");
        }else{
            out.println("<p align='center'>亲，你还没有登录呢</p></body></html>");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        doGet(request, response);
    }

}
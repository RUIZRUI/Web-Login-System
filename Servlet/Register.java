/**
 * 插入数据失败时会占用 id，造成 id 不连续
 * 实现注册成功后自动完成登录
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/我今年278/Register")
public class Register extends HttpServlet{
    private static final long serialVersionUID = 1L;
    // JDBC 驱动
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    // 数据库 URL 
    static final String DB_URL = "jdbc:mysql://localhost:3306/qqdb";

    // 数据库的用户名与密码
    static final String USER = "root";
    static final String PASS = "1214";

    // 构造函数
    public Register(){
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        // 如果不存在 session 会话，则创建一个 session 对象
        HttpSession session = request.getSession(true);

        Connection conn = null;
        PreparedStatement pst = null;

        // 设置时间格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 请求解决乱码
        request.setCharacterEncoding("utf-8");

        // 响应解决中文乱码
        response.setContentType("text/html; charset=utf-8");

        // 接收表单数据
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Date register_time = new Date();
        Date last_login_time = register_time;
        int iconID = 0;         // 假定默认为0

        PrintWriter out = response.getWriter();
        String title = "用户注册";
        String docType = "<!DOCTYPE html>\n";
        out.println(docType +
            "<html lang='zh'>\n" +
            "<head><title>" + title + "</title></head>\n" +
            "<body style=\"background-image: url('http://qixqi.club/images/body.jpg')\">\n" +
            "<h1 align='center'>" + title + "</h1>\n");

        try{
            // out.println("<p>1</p>\n");
            // 注册 JDBC 驱动器
            Class.forName(JDBC_DRIVER);
            // out.println("<p>2</p>\n");
            
            // 打开 mysql 连接
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            // out.println("<p>3</p>\n");

            // 执行 sql
            // 查询最大 id
            String sql;
            sql = "select max(id) from i278_user;";
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            int maxID = 0;      // 还未插入
            if(rs.next()){
                maxID = rs.getInt("max(id)");
                // out.println("<p align='center'>" + maxID + "</p>");
            }

            // 插入数据
            sql = "insert into i278_user(id, username, email, password, register_time, last_login_time, iconID) values (?, ?, ?, ?, ?, ?, ?);";
            pst = conn.prepareStatement(sql);
        
            // 传入参数
            pst.setInt(1, maxID + 1);
            pst.setString(2, username);
            pst.setString(3, email);
            pst.setString(4, password);
            pst.setString(5, df.format(register_time));
            pst.setString(6, df.format(last_login_time));
            pst.setInt(7, iconID);

            // 执行数据库更新操作，不需要 sql
            pst.executeUpdate();

            out.println("<p align='center'>注册成功</p>");
            // 获取信息到 Session
            session.setAttribute("id", maxID + 1);
            session.setAttribute("username", URLEncoder.encode(username, "UTF-8"));    // 中文编码
            session.setAttribute("email", email);
            session.setAttribute("password", password);
            session.setAttribute("register_time", register_time);
            // out.println("<p align='center'>register_time " + register_time + "</p>");       // 输出到毫秒
            session.setAttribute("last_login_time", last_login_time);
            session.setAttribute("iconID", iconID);
            // 设置最大保持连接时间
            session.setMaxInactiveInterval(600);         // 600s
            out.println("<p align='center'>设置session成功</p></body></html>");


            // 完成后关闭
            rs.close();
            pst.close();
            conn.close();
        } catch(SQLException se){
            // jdbc error
            out.println("<p align='center'>注册失败</p>");
            out.println("<p align='center'>" + se.toString() +  "</p>");
            out.println("<p align='center'>" + se.getMessage() + "</p></body></html>");
            se.printStackTrace();
        } catch(Exception e){
            // Class.forNaem() error
            out.println("<p align='center'>注册失败</p>");
            out.println("<p align='center'>" + e.toString() +  "</p>");
            out.println("<p align='center'>" + e.getMessage() + "</p></body></html>");
            e.printStackTrace();
        } finally{
            // 最后关闭资源
            try{
                if(pst != null){
                    pst.close();
                }
            }catch(SQLException se2){
                // 不处理
            }
            try{
                if(conn != null){
                    conn.close();
                }
            } catch(SQLException se){
                se.printStackTrace();
            }

        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        doGet(request, response);
    }

}

// a17411419150580
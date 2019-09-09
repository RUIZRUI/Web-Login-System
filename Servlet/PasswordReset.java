// 登录状态下，不应该使用此种方式，更换密码

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/我今年278/PasswordReset")
public class PasswordReset extends HttpServlet{
    private static final long serialVersionUID = 1L;
    // JDBC 驱动
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    // 数据库 URL 
    static final String DB_URL = "jdbc:mysql://localhost:3306/qqdb";

    // 数据库的用户名与密码
    static final String USER = "root";
    static final String PASS = "1214";

    // 构造函数
    public PasswordReset(){
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        Connection conn = null;
        PreparedStatement pst = null;

        // 请求解决乱码
        request.setCharacterEncoding("utf-8");

        // 响应解决乱码
        response.setContentType("text/html; charset=utf-8");

        // 接收表单数据
        String email = request.getParameter("email");

        PrintWriter out = response.getWriter();
        String title = "重设密码";
        String docType = "<!DOCTYPE html>\n";
        out.println(docType +
            "<html lang='zh'>\n" +
            "<head><title>" + title + "</title></head>\n" +
            "<body style=\"background-image: url('http://qixqi.club/images/body.jpg')\">\n" +
            "<h1 align='center'>" + title + "</h1>\n");  
            
        try{
            // 注册 JDBC 驱动器
            Class.forName(JDBC_DRIVER);

            // 打开 mysql 连接
            conn = DriverManager.getConnection(DB_URL, USER, PASS);      
            
            // 执行 sql 查询
            String sql;
            sql = "select * from i278_user where email = '" + email + "';";

            pst = conn.prepareStatement(sql);

            ResultSet rs = pst.executeQuery();

            if(rs.next()){
                out.println("<p align='center'>正在等待发送邮件...</p></body></html>");     // 不显示
                /* todo: 调用 SendEmail */
                try{
                    Thread.sleep(2000);     // 暂定两s，必须处理异常
                }catch(Exception e){
                    // out.println("<p align='center'>hello</p>");
                    System.exit(0);         // 退出程序
                }
                // url 
                String send_email_url = "我今年278";
                send_email_url = URLEncoder.encode(send_email_url, "UTF-8");   
                response.sendRedirect("http://qixqi.club:8080/" + send_email_url + "/SendEmail?email=" + email);
            }else{
                out.println("<p align='center'>用户尚未注册</p></body></html>");
            }

            // 关闭连接
            rs.close();
            pst.close();
            conn.close();
        } catch(SQLException se){
            // jdbc error
            out.println("<p align='center'>" + se.getMessage() + "</p></body></html>");
            se.printStackTrace();
        } catch(Exception e){
            // Class.forName() error
            out.println("<p align='center'>" + e.getMessage() + "</p></body></html>");
            e.printStackTrace();
        } finally{
            // 关闭资源
            try{
                if(pst != null){
                    pst.close();
                }
            } catch(SQLException se2){
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
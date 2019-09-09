import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/我今年278/PasswordResetNew")
public class PasswordResetNew extends HttpServlet{
    private static final long serialVersionUID = 1L;
    // JDBC 驱动
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    // 数据库 URL 
    static final String DB_URL = "jdbc:mysql://localhost:3306/qqdb";

    // 数据库的用户名与密码
    static final String USER = "root";
    static final String PASS = "1214";

    // 构造函数
    public PasswordResetNew(){
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
        String code = request.getParameter("code");
        String password = request.getParameter("password");

        PrintWriter out = response.getWriter();
        String title = "重设密码";
        String docType = "<!DOCTYPE html>\n";
        out.println(docType +
            "<html lang='zh'>\n" +
            "<head><title>" + title + "</title></head>\n" +
            "<body style=\"background-image: url('http://qixqi.club/images/body.jpg')\">\n" +
            "<h1 align='center'>" + title + "</h1>\n");  

        if(!checkCode(email, code, out)){
            out.println("<p align='center'>更改密码失败！</p>");
            out.println("</body></html>");
            return;
            // System.exit(-1);     // 太猛了，tomcat 异常退出
        }
        try{
            // 注册 JDBC 驱动器
            Class.forName(JDBC_DRIVER);

            // 打开 mysql 连接
            conn = DriverManager.getConnection(DB_URL, USER, PASS);      
            
            // 执行 sql 查询
            String sql; 
            sql = "update i278_user set password = ? where email = ?";
            pst = conn.prepareStatement(sql);

            // 传入参数
            pst.setString(1, password);
            pst.setString(2, email);

            // 更新
            pst.executeUpdate();

            out.println("<p align='center'>更改密码成功</p></body></html>");

            // 完成后关闭
            pst.close();
            conn.close();
        } catch(SQLException se){
            // jdbc error
            out.println("<p align='center'>更改密码失败</p>");
            out.println("<p align='center'>" + se.getMessage() + "</p></body></html>");
            se.printStackTrace();
        } catch(Exception e){
            // Class.forName() error
            out.println("<p align='center'>更改密码失败</p>");
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

    protected Boolean checkCode(String email, String code, PrintWriter out){
        Connection conn = null;
        PreparedStatement pst = null;
        
        Date now_time = new Date();

        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try{
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql;
            sql = "select code, start_time from i278_code where email = '" + email + "'";
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if(!rs.next()){
                out.println("<p align='center'>服务端没有此邮箱的验证码</p>");
                return false;
            }else if(!code.equals(rs.getString("code"))){
                out.println("<p align='center'>验证码和服务端的验证码不一致</p>");
                return false;
            }else{
                Date start_time = simpleFormat.parse(rs.getString("start_time"));
                long nowl = now_time.getTime();
                long startl = start_time.getTime();
                int minutes = (int)((nowl - startl) / (1000 * 60));
                if(minutes <= 10){
                    out.println("<p align='center'>验证码有效</p>");
                    return true;
                }else{
                    out.println("<p align='center'>超过10分钟，验证码失效</p>");
                    return false;
                }
            }
            // pst.close();
            // conn.close();
        } catch(SQLException se){
            out.println("<p align='center'>" + se.toString() + "</p>");
            se.printStackTrace();
            out.println("<p align='center'>核对验证码，出现异常</p>");
            return false;
        } catch(Exception e){
            out.println("<p align='center'>" + e.toString() + "</p>");
            e.printStackTrace();
            out.println("<p align='center'>核对验证码，出现异常</p>");
            return false;
        } finally{
            try{
                if(pst != null){
                    pst.close();
                } 
            } catch(SQLException se2){

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
}

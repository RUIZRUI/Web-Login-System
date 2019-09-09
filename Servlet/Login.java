/**
 * 易错，sql 查询时字段值要加引号
 * 存入 Cookie，将用户的全部信息存入，包括密码（如果不合适，后续处理）3
 * 用户名不应该包含 @，防止与邮箱重复
 * 处理用户重复登录，目前重复登录覆盖处理，后续处理
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

@WebServlet("/我今年278/Login")
public class Login extends HttpServlet{
    private static final long serialVersionUID = 1L;
    // JDBC 驱动
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    // 数据库 URL 
    static final String DB_URL = "jdbc:mysql://localhost:3306/qqdb";

    // 数据库的用户名与密码
    static final String USER = "root";
    static final String PASS = "1214";

    // 构造函数
    public Login(){
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        // 如果不存在 session 会话，则创建一个 session 对象
        HttpSession session = request.getSession(true);

        // 设置日期输出的格式
        SimpleDateFormat df =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Connection conn = null;
        PreparedStatement pst = null;

        // 请求解决乱码
        request.setCharacterEncoding("utf-8");

        // 响应解决乱码
        response.setContentType("text/html; charset=utf-8");

        // 接收表单数据
        String username = request.getParameter("login_field");
        String password = request.getParameter("password");

        PrintWriter out = response.getWriter();
        String title = "用户登录";
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
            sql = "select id, username, email, password, register_time, last_login_time, iconID from i278_user where (username = '" + username + "' or email = '" + username + "') and password = '" + password + "';";
            
            pst = conn.prepareStatement(sql);

            ResultSet rs = pst.executeQuery();

            if(rs.next()){
                out.println("<p align='center'>登录成功</p>");
                // 读取信息到 Session
                int Rid = rs.getInt("id");
                String Rusername = rs.getString("username");
                String Remail = rs.getString("email");
                String Rpassword = rs.getString("password");
                Date Rregister_time = rs.getTimestamp("register_time");
                Date Rlast_login_time = rs.getTimestamp("last_login_time");
                int RiconID = rs.getInt("iconID");
                // 设置 session
                session.setAttribute("id", Rid);
                session.setAttribute("username", URLEncoder.encode(Rusername, "UTF-8"));    // 中文编码
                session.setAttribute("email", Remail);
                session.setAttribute("password", Rpassword);
                session.setAttribute("register_time", Rregister_time);
                // out.println("<p align='center'>register_time " + Rregister_time + "</p>");       // 输出到毫秒
                session.setAttribute("last_login_time", Rlast_login_time);
                session.setAttribute("iconID", RiconID);
                // 设置最大保持连接时间
                session.setMaxInactiveInterval(600);         // 600s
                out.println("<p align='center'>设置session成功</p>");
                Date now = updateLogin(Rid);
                if(now != null){
                    out.println("<p align='center'>最后一次登录信息更新成功</p></body></html>");
                    session.setAttribute("last_login_time", now);
                }else{
                    out.println("<p align='center'>更新数据库异常</p></body></html>");
                }
            }else{
                out.println("<p align='center'>登录失败</p></body></html>");
            }
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

    private Date updateLogin(int id){             // 更新最后一次登录时间
        // 设置日期输出的格式
        SimpleDateFormat df =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        Connection conn = null;
        PreparedStatement pst = null;
        Date now = null;

        try{
            // 注册 JDBC 驱动器
            Class.forName(JDBC_DRIVER);

            // 打开 mysql 连接
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // 执行 sql 查询
            String sql;
            sql = "update i278_user set last_login_time = ? where id = ?";
            pst = conn.prepareStatement(sql);
            now = new Date();
            pst.setString(1, df.format(now));
            pst.setInt(2, id);
            pst.executeUpdate();
            
            pst.close();
            conn.close();
        } catch(SQLException se){
            // jdbc error
            now = null;
            se.printStackTrace();
        } catch(Exception e){
            // Class.forName() error
            now = null;
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
            return now;
        }
    }

}

// a17411419150580
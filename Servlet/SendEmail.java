// http://localhost:8080 中，绝对路径对应的是port 80, 还是 port 8080

import java.io.*;
import java.util.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

@WebServlet("/我今年278/SendEmail")
public class SendEmail extends HttpServlet{
    private static final long serialVersionUID = 1L;
    
    // 发件人的邮箱和密码
    private static String myEmailAccount = "zhengxiang4056@163.com";
    // public static String myEmailPassword = "Aa17411419150580";        // 密码
    private static String myEmailPassword = "a17411419150580";           // 授权码
    
    // 发件人的 SMTP 地址
    private static String myEmailSMTPHost = "smtp.163.com";

    // JDBC 驱动
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    // 数据库 URL 
    static final String DB_URL = "jdbc:mysql://localhost:3306/qqdb";

    // 数据库的用户名与密码
    static final String USER = "root";
    static final String PASS = "1214";    

    public SendEmail(){
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        Transport transport = null;     // 获取邮件传输对象

        // 请求解决乱码
        request.setCharacterEncoding("utf-8");

        // 响应解决乱码
        response.setContentType("text/html; charset=utf-8");

        // 获取收件人邮箱
        String receiveMailAccount = request.getParameter("email");

        // 创建参数配置
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");   // 使用协议
        props.setProperty("mail.smtp.host", myEmailSMTPHost);   // SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            // 请求认证

        Session session = Session.getInstance(props);
        session.setDebug(true);         // degub 模式，可以发送详细的 log

        PrintWriter out = response.getWriter();
        String title = "发送电子邮件";
        String res = "成功发送邮件，如果没有收到，请查看垃圾箱";
        String docType = "<!DOCTYPE html>\n";
        out.println(docType + 
            "<html lang='zh'>\n" +
            "<head><title>" + title + "</title>" + 
            // 异常时也跳转
            // "<meta http-equiv='refresh' content='3;URL=http://qixqi.club/我今年278/password_reset_new.html' />" +
            "</head>\n" +
            "<body style=\"background-image: url('http://qixqi.club/images/body.jpg')\">\n" + 
            "<h1 align='center'>" + title + "</h1>\n");
        // 产生6位随机数充当验证码
        String identifying_code = "";           // 验证码
        for(int i=0; i<6; i++){
            int temp = (int)(Math.random()*10);     // [0, 10)
            identifying_code += temp;
        }
        try{
            // 创建一封邮件
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myEmailAccount, "qixqi", "UTF-8"));
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMailAccount, "Dear User", "UTF-8"));
            message.setSubject("密码重置", "UTF-8");
            message.setContent("<!DOCTYPE html>" +
                "<html lang='zh'>" +
                "<head><title>密码重设</title>" + 
                "<style>" +
                ".words{" + 
                "   letter-spacing: 1px;" +
                "}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<h3 align='center'>密码重设</h3>" +
                "<div style='line-height: 30px; letter-spacing: 2px;'" +
                "<p><label class='words'>High, Dear user!</label><br />" +
                "忘记<label class='words'>QixQi</label>的密码了吗？别着急，以下的验证码可以帮助你找回密码： <br />" +
                "验证码: <label class='words'>" + identifying_code + "</label> （<label class='words'>10</label>分钟内有效）<br /><br />" +
                "如果这不是您的邮件请忽略，很抱歉打扰您，请原谅。<br /><br /><br />" +
                "<a href='http://qixqi.club'><label class='words'>QixQi</label></a><br />" +
                "首席攻城狮[<label class='words'>doge</label>]<br />" +
                "</p></div>" +
                "</body></html>"
                , "text/html; charset=utf-8");
            message.setSentDate(new Date());
            message.saveChanges();

            // 根据 Session 获取邮件传输对象
            transport = session.getTransport();

            // 连接邮件服务器
            transport.connect(myEmailAccount, myEmailPassword);

            // 发送邮件
            transport.sendMessage(message, message.getAllRecipients());

            out.println("<p align='center'>" + res + "</p>\n");
            saveCode(receiveMailAccount, identifying_code, out);
            out.println("<p align='center'>3s后页面跳转...</p>");
            out.println("<script language='javascript'>");
            out.println("   setTimeout(\"window.location='http://qixqi.club/我今年278/password_reset_new.html?email=" + receiveMailAccount + "'\", 3000);");     // 3秒
            out.println("</script>");
            out.println("</body></html>");
        } catch(MessagingException mex){
            out.println(mex.getMessage());
            mex.printStackTrace();
        } finally{
            // 关闭连接
            try{
                if(transport != null){
                    transport.close();
                }
            } catch(MessagingException mex){
                out.println(mex.getMessage());
                mex.printStackTrace();
            }
        }

    }    

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        doGet(request, response);
    }

    protected void saveCode(String email, String code, PrintWriter out){
        // 设置日期输出格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Connection conn = null;
        PreparedStatement pst = null;

        Date start_time = new Date();

        try{
            // 注册 JDBC
            Class.forName(JDBC_DRIVER);
            // 连接
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // 执行 sql 查询
            String sql;
            sql = "select * from i278_code where email = '" + email + "'";
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){      // 列表中有验证码，修改
                sql = "update i278_code set code = ?, start_time = ? where email = ?";
                pst = conn.prepareStatement(sql);
                // 传入参数
                pst.setString(1, code);
                pst.setString(2, df.format(start_time));
                pst.setString(3, email);
                // 更新
                pst.executeUpdate();
                out.println("<p align='center'>服务端修改验证码成功</p>");
            }else{              // 列表中没有验证码，添加
                sql = "insert into i278_code(email, code, start_time) values (?, ?, ?)";
                pst = conn.prepareStatement(sql);
                // 传入参数
                pst.setString(1, email);
                pst.setString(2, code);
                pst.setString(3, df.format(start_time));
                // 更新
                pst.executeUpdate();
                out.println("<p align='center'>服务端添加验证码成功</p>");
            }

            // 关闭连接
            rs.close();
            pst.close();
            conn.close();
        } catch(SQLException se){
            // jdbc error
            out.println("<p align='center'>服务端更新验证码异常</p>");
            out.println("<p align='center'>" + se.toString() + "</p>");
        } catch(Exception e){
            // Class.forName() error
            out.println("<p align='center'>服务端更新验证码异常</p>");
            out.println("<p align='center'>" + e.toString() + "</p>");
        } finally{
            // 最后关闭连接
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
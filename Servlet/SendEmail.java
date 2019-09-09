// http://localhost:8080 中，绝对路径对应的是port 80, 还是 port 8080

import java.io.*;
import java.util.*;

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
        String res = "成功发送消息，如果没有收到，请查看垃圾箱";
        String docType = "<!DOCTYPE html>\n";
        out.println(docType + 
            "<html lang='zh'>\n" +
            "<head><title>" + title + "</title></head>\n" +
            "<body style=\"background-image: url('http://qixqi.club/images/body.jpg')\">\n" + 
            "<h1 align='center'>" + title + "</h1>\n");
        try{
            // 创建一封邮件
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myEmailAccount, "qixqi", "UTF-8"));
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMailAccount, "Dear User", "UTF-8"));
            message.setSubject("密码重置", "UTF-8");
            message.setContent("<!DOCTYPE html>" +
                "<html lang='zh'>" +
                "<head><title>密码重设</title></head>" +
                "<body>" +
                "<h1 align='center'>密码重设</h1>" +
                "<p align='center'>点击链接重设密码</p>" +
                "<a href='http://qixqi.club/我今年278/password_reset_new.html?email=" + receiveMailAccount + "'>reset password</a>" +
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

}
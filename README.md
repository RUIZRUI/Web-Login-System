# Servlet 实现注册-登录-找回密码系统

效果: [http://qixqi.club/我今年278/login.html](http://zhengxiang4056.club/我今年278/login.html)

目前效果中使用验证码来找回密码

JavaScript 简单的本地检测数据格式，减轻服务器的压力

# 系统流程
register.html    注册信息提交给 Register

login.html       登录信息提交给 Login

password_reset.html    重设密码时将注册邮箱提交给 PasswordReset，判断是否已注册，若注册，转到 SendEmail，发送邮件，邮件中的超链接转到    password_reset_new.html，重设密码，提交给 PasswordResetNew 处理

# servlet

servlet 文件夹下， 

  -- GetLogin 获取登录信息
  -- Login    处理登录
  -- Logout   登出
  -- PasswordReset  重设密码 
  -- PasswordResetNew   
  -- Register 处理注册
  -- SendEmail  发送邮件
  
  
# SendEmail 设置
具体参考 [https://www.jianshu.com/p/d2be1d2a2fae](https://www.jianshu.com/p/d2be1d2a2fae)

# 使用
将 servlet 中的java源文件编译，然后添加到 tomcat下，修改web.xml，根据需要修改各个文件之间的链接关系即可

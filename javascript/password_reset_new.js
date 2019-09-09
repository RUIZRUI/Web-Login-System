var password = document.getElementById('password');
var email = document.getElementById('email');
var code = document.getElementById('code');
var confirm_password = document.getElementById('confirm_password');
var form = document.getElementById('reset_password').getElementsByTagName('form')[0];
var error_close = document.getElementById('error_message').getElementsByTagName('button')[0];

// 验证密码
function checkPassword(){
    var regNumber = /\d+/;          // 验证 0-9 的任意数字最少出现一次
    var regString = /[a-zA-Z]+/;    // 验证大小写字母任意字母最少出现一次

    if(password.value.length >= 8 && regNumber.test(password.value) && regString.test(password.value)){
        return true;
    }
    return false;
}

// 确认密码
function confirmPassword(){
    if(password.value != confirm_password.value){
        return false;
    }
    return true;
}

// 关闭错误提示信息
error_close.onclick = function(){
    document.getElementById('error_message').style.display = 'none';
};

// 表单提交检测
form.onsubmit = function(){
    if(checkCode() && checkPassword() && confirmPassword() && getEmail()){
        return true;
    }
    document.getElementById('error_message').style.display = 'block';
    return false;
};

// 解析url 获得 email
function getEmail(){
    // var url = 'http://localhost:8080';
    var url = window.location.href;
    var index = url.indexOf('?email=');
    if(index == -1){
        return false;
    }
    var real_email = url.substring(index + 7);
    if(real_email.trim() == ''){
        return false;
    }else{
        email.value = real_email;
        return true;
    }
}

// getEmail();      // 提交事件发生时执行

// 验证码
function checkCode(){
    var regAllNum = /^[0-9]+$/;         // 验证是否全是数字
    if(code.value.trim().length == 6 && regAllNum.test(code.value.trim())){
        return true;
    }
    return false;
}
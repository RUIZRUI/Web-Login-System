var identity = document.getElementById('login_field');
var password = document.getElementById('password');
var form = document.getElementById('user_login').getElementsByTagName('form')[0];
var error_close = document.getElementById('error_message').getElementsByTagName('button')[0];

// 检测用户名或邮箱不能为空
function checkIdentity(){       
    if(identity.value.trim() == ''){
        return false;
    }
    return true;
}

// 检测密码
function checkPassword(){
    var regNumber = /\d+/;          // 验证 0-9 的任意数字最少出现一次
    var regString = /[a-zA-Z]+/;    // 验证大小写字母任意字母最少出现一次

    if(password.value.length >= 8 && regNumber.test(password.value) && regString.test(password.value)){
        return true;
    }
    return false;
}

// 关闭错误提示信息
error_close.onclick = function(){
    document.getElementById('error_message').style.display = 'none';
};

form.onsubmit = function(){
    if(/*checkIdentity() &&*/ checkPassword()){
        return true;
    }
    document.getElementById('error_message').style.display = 'block';
    return false;
};
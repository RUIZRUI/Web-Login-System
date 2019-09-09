var username = document.getElementById('username');
var password = document.getElementById('password');
var email = document.getElementById('email');
var confirm_password = document.getElementById('confirm_password');
var form = document.getElementsByTagName('form')[0];
var message_close = document.getElementById('error_message').getElementsByTagName('button')[0];

// 检测用户名
function checkUserName(){
    if(username.value.trim() == ''){       // 用户民不能为空
        return false;
    }
    return true;
}

// 检测邮箱
function checkEmail(){
    if(email.value.trim() == ''){           // 邮箱不能为空
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

// 确认密码
function confirmPassword(){
    if(password.value != confirm_password.value){

        return false;
    }
    return true;
}

// 关闭错误验证信息
message_close.onclick = function(){
    document.getElementById('error_message').style.display = 'none';
};

// 表单提交事件
form.onsubmit = function(){
    if(checkUserName() && checkEmail() && checkPassword() && confirmPassword()){
        return true;
    }
    document.getElementById('error_message').style.display = 'block';
    return false;
};





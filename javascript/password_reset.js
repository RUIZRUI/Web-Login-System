var email = document.getElementById('email');
var form = document.getElementById('reback_password').getElementsByTagName('form')[0];
var error_close = document.getElementById('error_message').getElementsByTagName('button')[0];

// 验证邮箱
function checkEmail(){
    if(email.value.trim() == ''){
        return false;
    }
    return true;
}

// 关闭错误提示信息
error_close.onclick = function(){
    document.getElementById('error_message').style.display = 'none';
};

form.onsubmit = function(){
    if(checkEmail()){
        return true;
    }
    document.getElementById('error_message').style.display = 'block';
    return false;
};

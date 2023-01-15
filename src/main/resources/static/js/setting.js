$(function(){
    $("#uploadForm").submit(upload);
});

function upload() {
    // 更新头像访问路径
    $.post(
        CONTEXT_PATH + "/user/header/url",
        {"fileName":$("input[name='key']").val()},
        function(data) {
            data = $.parseJSON(data);
            if(data.code == 0) {
                window.location.reload();
            } else {
                alert(data.msg);
            }
        }
    );
}

function updatePassword() {

    let oldPassword = $("#old-password").val();
    let newPassword = $("#new-password").val();
    let confirmPassword = $("#confirm-password").val();

    if (newPassword != confirmPassword) {
        $("#newPassword").text("两次密码不一致！");
        return;
    }
    $.post(
        CONTEXT_PATH + "/user/updatePassword",
        {"oldPassword":oldPassword,"newPassword":newPassword},
        function(data) {
            data = $.parseJSON(data);
            if(data.code == 0) {
                countDownTime(data.msg);

            } else {
                $("#oldPassword").text(data.msg);
            }
        }
    );
    function logout() {
        $.get(
            CONTEXT_PATH + "/logout",
            function (){
                window.location = CONTEXT_PATH + "/login";
            }
        );
    }

    function countDownTime(data) {
        var sec = 2;
        time = setInterval(function() {
            sec --;
            alert(data + " " + sec + "s 后自动跳转到登录页面！" );
            if(sec == 0) {
                clearInterval(time);  // 清除定时器
                logout();
            }
        },1000)
    }
}

$(function(){
    bsCustomFileInput.init();
});
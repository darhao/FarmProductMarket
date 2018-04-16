//登录
function login() {
    $.ajax({
        url:"user/login",
        type:'post',
        data: {
            name: $('#username').val(),
            password: $('#password').val()
        },
        dataType: 'json',
        success:function (data) {
            if(data.result==='succeed'){
                window.location = 'production/goHome';
            } else if(data.result==='failed'){
               $("#error").css("display", "block");
            }
        }
    })
}

$(document).ready(function () {
    //点击登录
	$(window).keyup(function (e) {
        if (e.which === 13) {
            check();
        }
    });
    $("#login-btn").click(function () {
    	check();
    })
})

function check(){
	if($('#username').val() === '' || $('#password').val() === ''){
		$("#error").css("display", "block");
		return;
	}
    login();
}
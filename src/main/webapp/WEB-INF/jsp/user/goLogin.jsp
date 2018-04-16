<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<base href="/FarmProductMarket/">
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0,maximum-scale=1,user-scalable=0">
		<title>登录</title>
		<link rel="stylesheet" type="text/css" href="static/css/public/bootstrap.min.css">
		<link rel="stylesheet" type="text/css" href="static/css/public/font-awesome.min.css"/>
		<link rel="stylesheet" type="text/css" href="static/css/login.css"/>
	</head>
	
	<body class="yahei">
		
		<div>
			<a href="production/goHome"><span class="top-content fa fa-angle-left"> 登录</span></a>
		</div>
		
		<div id="container">
			<img src="static/img/logo.png" style="width: 100px;height: 100px;margin-bottom: 10px" alt="">
			<h5 class="login-errorMes" id="login-errorMes" style="color: #B22222;margin: 0 auto;font-size: 13px;display: none">账号或密码错误！</h5>
			<form role="form" action="" method="post" id="loginForm" class="loginCheckForm">
				<div class="form-group">
					<div class="e input-group">
						<span class="input-group-addon"><i class="fa-fw fa fa-user"></i></span>
						<input id="username" class="form-control" name="name" type="text" placeholder="请输入用户名"/>
					</div>
				</div>
				<div class="form-group">
					<div class="e input-group">
						<span class="input-group-addon"><i class="fa-fw fa fa-key"></i></span>
						<input id="password" class="form-control" name="password" type="password" placeholder="请输入密码"/>
					</div>
				</div>
			</form>
			
			<div id="error">请输入正确的用户名和密码</div>
			
			<div class="e">
				<button class="e btn btn-success form-control" id="login-btn">登录</button>
			</div>
		</div>

		<script src="static/js/public/jquery-2.1.1.js"></script>
		<script src="static/js/public/bootstrap-3.3.7.js"></script>
		<script src="static/js/login.js"></script>
	</body>
</html>

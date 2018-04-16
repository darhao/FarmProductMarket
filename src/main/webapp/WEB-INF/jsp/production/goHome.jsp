<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
<base href="/FarmProductMarket/">
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0,maximum-scale=1,user-scalable=0">
<title>主页</title>
<link rel="stylesheet" type="text/css"
	href="static/css/public/bootstrap.min.css">
<link rel="stylesheet" type="text/css"
	href="static/css/public/font-awesome.min.css" />
<link rel="stylesheet" type="text/css" href="static/css/main.css">
</head>

<body class="yahei">
	<div class="top">
		<div id="home">
			<span><i class="fa-fw fa fa-shopping-basket"></i></span> <span>农贸电商网站</span>
		</div>
		<a href="user/goLogin"><i class="fa-fw fa fa-user right"></i></a>
	</div>

	<div class="input-group search-bar">
		<span class="input-group-addon"><i class="fa-fw fa fa-search"></i></span>
		<input id="key-input" class="form-control" type="text"
			placeholder="请输入名称、类型或来源进行搜索" />
	</div>

	<table class="table table-striped result-table container">
		<thead>
			<tr>
				<th>名称</th>
				<th>称重</th>
				<th>类型</th>
				<th>来源</th>
				<th>时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody id="databody">
			<!-- Ajax -->
		</tbody>
	</table>

	<div class="slider-c">
		<div id="back">
			<i class="fa-fw fa fa-angle-left"></i>
		</div>
		<div id="pageInfo">1/1</div>
		<div id="forward">
			<i class="fa-fw fa fa-angle-right"></i>
		</div>
	</div>

	<!-- 模态框（Modal） -->
	<div class="modal fade" id="tip" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">提示</h4>
				</div>
				<div id="tipContent" class="modal-body">在这里添加一些文本</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">OK</button>
				</div>
			</div>
		</div>
	</div>

	<script src="static/js/public/jquery-2.1.1.js"></script>
	<script src="static/js/public/bootstrap-3.3.7.js"></script>
	<script src="static/js/main.js"></script>

</body>

</html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>404</title>
		<%@ include file="head.jsp"%>
	</head>
	<body>
		<main class="pulic-line-centent error-warp">
			<div class="icon-error"><img src="style/images/icon-404.png" alt=""/></div>
			<h2 class="error-title">哎呀！出错啦~</h2>
			<p class="error-con">你访问的页面已经离开地球啦</p>
			<div class="public-submit-type-1 public-form-item">
				<a href="#" class="public-submit-type-1">返回首页</a>
			</div>
		</main>
	</body>
</html>

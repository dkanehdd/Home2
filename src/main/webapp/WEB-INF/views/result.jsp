<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>
<body>
<div class="container">
	
	<div>이름 : ${dto.name }</div>
	<div>아이디 : ${dto.id }</div>
	<div>생일 : ${dto.birthday }</div>
	<div>폰 : ${dto.phone }</div>
	<div>성별 : ${dto.gender }</div>
	<div>이메일 : ${dto.email }</div>
	<img src="${image }" alt="이미지" />
</div>
</body>
</html>
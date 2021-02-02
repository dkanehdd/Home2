<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>Home</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@10"></script>

<script>
$(function() {
	$('#sendPhoneNumber').click(function() {
		var phoneNumber = $('#inputPhoneNumber').val();
		if(phoneNumber.length<11){
			Swal.fire('메세지 전송 실패','휴대폰번호를 정확히 입력해주세요','error');
			return;
		}
		Swal.fire('인증번호 발송 완료!');
		
		$.ajax({
			type : "GET",
			url : "./check/sendSMS",
			data : {
				"phoneNumber" : phoneNumber
			},
			success : function(res) {
				$('#checkBtn').click(function() {
					if ($.trim(res) == $('#inputCertifiedNumber').val()) {
						Swal.fire('인증성공!', '휴대폰 인증이 정상적으로 완료되었습니다.', 'success')
	
						// 인증성공후 요청명 기입
						//location.href='regiform'
					} else {
						Swal.fire({
							icon : 'error',
							title : '인증오류',
							text : '인증번호가 올바르지 않습니다!',
						})
					}
				})

			},
			error : function(e) {
				Swal.fire('메세지 전송 실패',e,'error');
			}
		})
	});
});
</script>
</head>
<body>
	<h2>본인인증 테스트</h2>
	<p>휴대폰 인증하기</p>
	<input type="text" id="inputPhoneNumber" placeholder="휴대폰번호 입력" maxlength="12"/><button type="button" id="sendPhoneNumber" >문자발송</button>
	<br /><br /><br />
	<p>인증번호입력</p>
	<input type="text" id="inputCertifiedNumber" placeholder="인증번호입력" /> <button type="button" id="checkBtn">확인</button>
	
	<a href="./naver">네이버로 로그인하기</a>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="LOGIN" />
<%@ include file="../common/head.jspf"%>

<section class="mt-8 text-xl">

	<!-- 내가 만들어 본 로그인 폼 -->
	<!-- 	<div class="login-card"> -->
	<!-- 		<h2>login</h2> -->
	<!-- 		<h3>Enter your credentials</h3> -->
	<!-- 		<form class="login-form" method="POST" action="../member/doLogin"> -->
	<!-- 			<input name="loginId" type="text" placeholder="아이디를 입력하세요" /> -->
	<!-- 			<input name="loginPw" type="text" placeholder="비밀번호를 입력하세요" /> -->
	<!-- 			<a href="#">Forgot your password?</a> -->
	<!-- 			<button type="submit" value="로그인">LOGIN</button> -->
	<!-- 		</form> -->
	<!-- 		<button class="mt-3 btn-text-link btn btn-active btn-ghost" type="button" onclick="history.back();">BACK</button> -->
	<!-- 	</div> -->


	<div class="container mx-auto px-3">
		<form class="table-box-type-1" method="POST" action="../member/doLogin">
			<input type="hidden" name="afterLoginUri" value="${param.afterLoginUri}" />
			<table>
				<colgroup>
					<col width="200" />
				</colgroup>

				<tbody>
					<tr>
						<th>아이디</th>
						<td>
							<input class="w-full input input-bordered  max-w-xs" name="loginId" type="text" placeholder="아이디를 입력하세요" />
						</td>
					</tr>
					<tr>
						<th>비밀번호</th>
						<td>
							<input class="w-full input input-bordered  max-w-xs" name="loginPw" type="text" placeholder="비밀번호를 입력하세요" />
						</td>
					</tr>
					<tr>
						<th></th>
						<td>
							<button class="btn btn-active btn-ghost" type="submit">로그인</button>
						</td>
					</tr>
					<tr>
						<th></th>
						<td>
							<a href="${rq.findLoginIdUri }" class="btn btn-active btn-ghost" type="submit">아이디 찾기</a>
							<a href="${rq.findLoginPwUri }" class="btn btn-active btn-ghost" type="submit">비밀번호 찾기</a>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>

	<div class="container mx-auto btns">
		<button class="btn-text-link btn btn-active btn-ghost" type="button" onclick="history.back();">뒤로가기</button>
	</div>

</section>
<%@ include file="../common/foot.jspf"%>
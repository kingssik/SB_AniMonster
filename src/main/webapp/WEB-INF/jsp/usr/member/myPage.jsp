<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="MYPAGE" />
<%@ include file="../common/head.jspf"%>
<%@ page import="com.khs.exam.demo.util.Ut"%>

<section class="mt-8 text-xl">
	<div class="container mx-auto px-3">
		<div class="table-box-type-1">
			<table>
				<colgroup>
					<col width="200" />
				</colgroup>
				<tbody>
					<tr>
						<th>가입일</th>
						<td>${rq.loginedMember.regDate }</td>
					</tr>
					<tr>
						<th>아이디</th>
						<td>${rq.loginedMember.loginId }</td>
					</tr>
					<tr>
						<th>이름</th>
						<td>${rq.loginedMember.name }</td>
					</tr>
					<tr>
						<th>닉네임</th>
						<td>${rq.loginedMember.nickname }</td>
					</tr>
					<tr>
						<th>전화번호</th>
						<td>${rq.loginedMember.cellphoneNum }</td>
					</tr>
					<tr>
						<th>이메일</th>
						<td>${rq.loginedMember.email }</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>

	<div class="container mx-auto btns mt-2">
		<button class="btn btn-active btn-ghost" type="button" onclick="history.back();">뒤로가기</button>
		<a href="../member/checkPassword?replaceUri=${Ut.getUriEncoded('../member/modify')}" class="btn btn-active btn-ghost">
			회원정보수정 </a>
		<a href="../member/checkPassword?replaceUri=${Ut.getUriEncoded('../member/doWithdraw')}"
			class="btn btn-active btn-ghost"
		> 회원 탈퇴 </a>
	</div>

</section>
<%@ include file="../common/foot.jspf"%>
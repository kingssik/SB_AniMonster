<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="ARTICLE DETAIL" />
<%@ include file="../common/head.jspf"%>

<script>
	const params = {};
	params.id = parseInt('${param.id}');
</script>

<script>
	function ArticleDetail__increaseHitCount() {
		const localStorageKey = 'article__' + params.id + '__alreadyView';
		if (localStorage.getItem(localStorageKey)) {
			return;
		}
		localStorage.setItem(localStorageKey, true);
		$.get('../article/doIncreaseHitCountRd', {
			id : params.id,
			ajaxMode : 'Y'
		}, function(data) {
			$('.article-detail__hit-count').empty().html(data.data1);
		}, 'json');
	}
	$(function() {
		// 실전코드
		//ArticleDetail__increaseHitCount();
		// 연습코드
		setTimeout(ArticleDetail__increaseHitCount, 2000);
	})
</script>


<section class="mt-8 text-xl">
	<div class="container mx-auto px-3">
		<div class="table-box-type-1">
			<table>
				<colgroup>
					<col width="200" />
				</colgroup>

				<tbody>
					<tr>
						<th>번호</th>
						<td>
							<div class="badge">${article.id }</div>
						</td>
					</tr>
					<tr>
						<th>작성날짜</th>
						<td>${article.regDate }</td>
					</tr>
					<tr>
						<th>수정날짜</th>
						<td>${article.updateDate }</td>
					</tr>
					<tr>
						<th>조회수</th>
						<td>
							<span class="badge article-detail__hit-count">${article.hitCount }</span>
						</td>
					</tr>
					<tr>
						<th>작성자</th>
						<td>${article.extra__writerName }</td>
					</tr>
					<tr>
						<th>추천</th>
						<td>
							<span class="badge ">${article.goodReactionPoint }</span>
							<c:if test="${actorCanMakeReaction}">
								<span>&nbsp;</span>
								<a
									href="/usr/reactionPoint/doGoodReaction?relTypeCode=article&relId=${param.id}&replaceUri=${rq.encodedCurrentUri} "
									class="btn btn-outline btn-xs"
								>좋아요 😄</a>
								<span>&nbsp;</span>
								<a
									href="/usr/reactionPoint/doBadReaction?relTypeCode=article&relId=${param.id}&replaceUri=${rq.encodedCurrentUri}"
									class="btn btn-outline btn-xs"
								>싫어요 🤢</a>
							</c:if>

							<c:if test="${actorCanCancelGoodReaction}">
								<span>&nbsp;</span>
								<a
									href="/usr/reactionPoint/doCancelGoodReaction?relTypeCode=article&relId=${param.id}&replaceUri=${rq.encodedCurrentUri} "
									class="btn btn-xs btn-primary"
								>좋아요 😄</a>
								<span>&nbsp;</span>
								<a onclick="alert(this.title); return false;" title="좋아요 취소좀" href="#" class="btn btn-outline btn-xs">싫어요 🤢</a>
							</c:if>

							<c:if test="${actorCanCancelBadReaction}">
								<span>&nbsp;</span>
								<a onclick="alert(this.title); return false;" title="싫어요 취소좀" href="#" class="btn btn-outline btn-xs">좋아요 😄</a>
								<span>&nbsp;</span>
								<a
									href="/usr/reactionPoint/doCancelBadReaction?relTypeCode=article&relId=${param.id}&replaceUri=${rq.encodedCurrentUri}"
									class="btn btn-xs btn-primary"
								>싫어요 🤢</a>
							</c:if>


						</td>
					</tr>
					<tr>
						<th>제목</th>
						<td>${article.title }</td>
					</tr>
					<tr>
						<th>내용</th>
						<td>${article.body }</td>
					</tr>
				</tbody>

			</table>
		</div>

		<div class="btns">
			<button class="btn-text-link btn btn-active btn-ghost" type="button" onclick="history.back();">뒤로가기</button>
			<c:if test="${article.extra__actorCanModify }">
				<a class="btn-text-link btn btn-active btn-ghost" href="../article/modify?id=${article.id }">수정</a>
			</c:if>
			<c:if test="${article.extra__actorCanDelete }">
				<a class="btn-text-link btn btn-active btn-ghost" onclick="if(confirm('진짜 삭제할거임?') == false) return false;"
					href="../article/doDelete?id=${article.id }"
				>삭제</a>
			</c:if>
		</div>
	</div>
</section>

<!-- 															 						-->

<script>
	// 댓글
	function ReplyWrite__submitForm(form) {
		var ReplyWrite__submitFormDone = false;

		if (ReplyWrite__submitFormDone) {
			return;
		}

		form.body.value = form.body.value.trim();

		if (form.body.value.length == 0) {
			alert('댓글을 입력하세요');
			form.body.focus();
			return;
		}

		// 		if (form.body.value.length < 2) {
		// 			alert('2글자 이상 입력하세요');
		// 			form.body.focus();
		// 			return;
		// 		}

		ReplyWrite__submitFormDone = true;
		form.submit();
	}
</script>

<section class="mt-5">
	<div class="container mx-auto px-3">
		<h2>댓글 작성</h2>
		<c:if test="${rq.logined }">
			<form class="table-box-type-1" method="POST" action="../reply/doWrite"
				onsubmit="ReplyWrite__submitForm(this); return false;"
			>
				<input type="hidden" name="relTypeCode" value="article" />
				<input type="hidden" name="relId" value="${article.id }" />
				<table class="table table-zebra w-full">
					<colgroup>
						<col width="200" />
					</colgroup>

					<tbody>
						<tr>
							<th>작성자</th>
							<td>${rq.loginedMember.nickname }</td>
						</tr>
						<tr>
							<th>내용</th>
							<td>
								<textarea required="required" class="textarea textarea-bordered w-full" type="text" name="body"
									placeholder="댓글을 입력하세요" rows="5"
								/></textarea>
							</td>
						</tr>
						<tr>
							<th></th>
							<td>
								<button class="btn btn-active btn-ghost" type="submit">댓글작성</button>
							</td>
						</tr>
					</tbody>

				</table>
			</form>
		</c:if>
		<c:if test="${rq.notLogined }">
			<a class="btn-text-link btn  btn-ghost" href="/usr/member/login">로그인</a> 후 이용하세요
		</c:if>
	</div>
</section>

<section class="mt-5">
	<div class="container mx-auto px-3">
		<h2>전체댓글(${repliesCount })</h2>
	</div>
</section>
<%@ include file="../common/foot.jspf"%>
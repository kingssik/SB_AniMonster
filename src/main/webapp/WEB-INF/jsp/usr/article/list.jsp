<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="${board.name } 게시판" />
<%@ include file="../common/head.jspf"%>

<section class="mt-8 text-xl">
	<div class="container mx-auto px-3">
		<div class="flex">
			<div>
				게시물 수 :
				<span class="badge">${articlesCount }</span>
			</div>
			<div class="flex-grow"></div>
			<form class="flex">
				<input type="hidden" name="boardId" value=${param.boardId } />

				<select data-value="${param.searchKeywordTypeCode }" name="searchKeywordTypeCode" class="select select-bordered">
					<option disabled="disabled">검색</option>
					<option value="title">제목</option>
					<option value="body">내용</option>
					<option value="title,body">제목 + 내용</option>
				</select>

				<input name="searchKeyword" type="text" class="ml-2 w-96 input input-borderd" placeholder="검색어를 입력하세요"
					maxlength="20" value="${param.searchKeyword }"
				/>
				<button type="submit" class="mx-2 btn btn-ghost">검색</button>

			</form>

			<form class="flex">
				<input type="hidden" name="boardId" value="${param.boardId}" />

				<select data-value="${sortCriteria }" name="sortCriteria" class="select select-bordered">
					<option disabled="disabled">정렬기준</option>
					<option value="regDate">최신순</option>
					<option value="hitCount">조회수</option>
					<option value="goodReactionPoint">추천순</option>
				</select>
				<button type="submit" class="ml-2 btn btn-ghost">정렬</button>
			</form>

		</div>

		<div class="table-box-type-1 mt-3">
			<table class="table table-fixed w-full">
				<colgroup>
					<col width="80" />
					<col width="140" />
					<col />
					<col width="140" />
					<col width="50" />
					<col width="50" />
				</colgroup>
				<thead>
					<tr>
						<th>번호</th>
						<th>날짜</th>
						<th>제목</th>
						<th>작성자</th>
						<th>조회수</th>
						<th>추천</th>
					</tr>
				</thead>
				<!--   Article list(normal)   -->
				<c:if test="${sortCriteria == '' }">
					<tbody>
						<c:forEach var="article" items="${articles }">
							<tr class="hover">
								<td>${article.id}</td>
								<td>${article.forPrintType1RegDate}</td>
								<td>
									<a class="hover:underline block w-full truncate" href="${rq.getArticleDetailUriFromArticleList(article) }">${article.title}</a>
								</td>
								<td>${article.extra__writerName}</td>
								<td>${article.hitCount}</td>
								<td>${article.goodReactionPoint}</td>
							</tr>
						</c:forEach>
					</tbody>
				</c:if>


				<!-- 최신순 정렬 -->
				<c:if test="${sortCriteria == 'regDate' }">
					<tbody>
						<c:forEach var="article" items="${articlesByRegDate }">
							<tr class="hover">
								<td>${article.id}</td>
								<td>${article.forPrintType1RegDate}</td>
								<td>
									<a class="hover:underline block w-full truncate" href="${rq.getArticleDetailUriFromArticleList(article) }">${article.title}</a>
								</td>
								<td>${article.extra__writerName}</td>
								<td>${article.hitCount}</td>
								<td>${article.goodReactionPoint}</td>
							</tr>
						</c:forEach>
					</tbody>
				</c:if>

				<!-- 추천순 정렬 -->
				<tbody>
					<c:if test="${sortCriteria == 'goodReactionPoint' }">
						<c:forEach var="article" items="${articlesByGoodReactionPoint }">
							<tr class="hover">
								<td>${article.id}</td>
								<td>${article.forPrintType1RegDate}</td>
								<td>
									<a class="hover:underline block w-full truncate" href="${rq.getArticleDetailUriFromArticleList(article) }">${article.title}</a>
								</td>
								<td>${article.extra__writerName}</td>
								<td>${article.hitCount}</td>
								<td>${article.goodReactionPoint}</td>
							</tr>
						</c:forEach>
					</c:if>
				</tbody>

				<!-- 조회수 정렬 -->
				<c:if test="${sortCriteria == 'hitCount' }">
					<tbody>
						<c:forEach var="article" items="${articlesByHitCount }">
							<tr class="hover">
								<td>${article.id}</td>
								<td>${article.forPrintType1RegDate}</td>
								<td>
									<a class="hover:underline block w-full truncate" href="${rq.getArticleDetailUriFromArticleList(article) }">${article.title}</a>
								</td>
								<td>${article.extra__writerName}</td>
								<td>${article.hitCount}</td>
								<td>${article.goodReactionPoint}</td>
							</tr>
						</c:forEach>
					</tbody>
				</c:if>
			</table>
		</div>
		<div class="page-menu mt-3 flex justify-center">
			<div class="btn-group">

				<c:set var="pageMenuLen" value="6" />
				<c:set var="startPage" value="${page - pageMenuLen >= 1 ? page- pageMenuLen : 1}" />
				<c:set var="endPage" value="${page + pageMenuLen <= pagesCount ? page + pageMenuLen : pagesCount}" />

				<c:set var="pageBaseUri" value="?boardId=${boardId }" />
				<c:set var="pageBaseUri" value="${pageBaseUri }&searchKeywordTypeCode=${param.searchKeywordTypeCode}" />
				<c:set var="pageBaseUri" value="${pageBaseUri }&searchKeyword=${param.searchKeyword}" />

				<c:if test="${startPage > 1}">
					<a class="btn btn-sm" href="${pageBaseUri }&page=1">1</a>
					<c:if test="${startPage > 2}">
						<a class="btn btn-sm btn-disabled">...</a>
					</c:if>
				</c:if>
				<c:forEach begin="${startPage }" end="${endPage }" var="i">
					<a class="btn btn-sm ${page == i ? 'btn-active' : '' }" href="${pageBaseUri }&page=${i }">${i }</a>
				</c:forEach>
				<c:if test="${endPage < pagesCount}">
					<c:if test="${endPage < pagesCount - 1}">
						<a class="btn btn-sm btn-disabled">...</a>
					</c:if>
					<a class="btn btn-sm" href="${pageBaseUri }&page=${pagesCount }">${pagesCount }</a>
				</c:if>
			</div>
		</div>
	</div>
</section>

<%@ include file="../common/foot.jspf"%>
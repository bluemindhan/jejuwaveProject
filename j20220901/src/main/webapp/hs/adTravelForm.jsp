<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	String context = request.getContextPath();
	String userId = (String) session.getAttribute("user_id");
	request.setAttribute("userId", userId);
%>
<c:import url="${context}/headerAdmin.jsp"></c:import>
<link rel="stylesheet" href="<%=context%>/hs/css/adminStyle.css">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR&display=swap" rel="stylesheet">
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript">

	function deleteChk(t_num, t_relevel, pageNum) {
		if (confirm("동행자 게시글을 삭제하시겠습니까?") == true){    //확인
		location.href= "<%=context%>/adTravelDelete.do?t_num="+ t_num +"&t_relevel="+ t_relevel+"&pageNum="+pageNum;
	 }else{   //취소
	     return false;
	 }
	}
	
</script>

	<!-- 네비게이션메뉴바 -->
	<nav id="nav" class="nav">
	<!-- 프로필 이미지 -->
		<div class="card" id="cardpro">
		  <img src="<%=context%>/hs_images/adminpro.png" class="card-img-top" alt="...">
		  <div class="card-body">
		    <h5 class="card-title">Admin</h5>
		    <p class="card-text">관리자입니다.</p>
		  </div>
		</div>
		<div class="d-grid gap-2 col-6" id="navmenu">
		  <button class="btn btn-primary" type="button" onclick="location.href='<%=context%>/admain.do'">홈</button>
		  <button class="btn btn-primary" type="button" onclick="location.href='<%=context%>/adMemList.do'">회원 관리</button>
		  <button class="btn btn-primary" type="button" onclick="location.href='<%=context%>/adTravelForm.do'">동행자 찾기 관리</button>
		  <button class="btn btn-primary" type="button" onclick="location.href='<%=context%>/adQnaForm.do'">Q&A 게시판 관리</button>
		  <button class="btn btn-primary" type="button" onclick="location.href='<%=context%>/adCommuForm.do'">회원커뮤니티 관리</button>
		</div>
	</nav>
	
	<!-- 본문 -->
	<article class="article" id="article">
		<div class="table-responsive">
		  <table class="table">
		    <caption>동행자 찾기</caption>
			  <thead>
			    <tr>
			      <th scope="col">번호</th>
			      <th scope="col">제목</th>
			      <th scope="col">아이디</th>
			      <th scope="col">테마</th>
			      <th scope="col">모집인원</th>
			      <th scope="col">모집완료여부</th>
			      <th scope="col">작성일</th>
			      <th scope="col">삭제</th>
			    </tr>
			  </thead>
			  <tbody>
			  	<c:if test="${totCnt > 0}">
			  		<c:forEach var="travel" items="${list}">
			  			<tr>
			  				<th scope="row" width="50">${startNum}</th>
			  				<td class="left" width="250" id="content"
			  				<c:if test="${travel.t_content eq '관리자 의해 삭제된 댓글입니다.'}">style="color:red;"</c:if>
			  				<c:if test="${travel.t_content eq '삭제된 댓글입니다.'}">style="color:blue;"</c:if>>
			  				
								<c:if test="${travel.t_relevel == 0}">
									<a href='<%=context%>/travelContent.do?t_num=${travel.t_num}'>${travel.t_title}</a>
									
									<c:if test="${travel.reply_cnt != 0 }">
										<span class="badge rounded-pill bg-secondary">${travel.reply_cnt}</span>
									</c:if>
								</c:if>
								<c:if test="${travel.t_relevel > 0}">
									<img src="<%=context%>/hs_images/adreply.png" width="${travel.t_relevel*10 }">
									${travel.t_content}
								</c:if>
							</td >
			  				<td width="100">${travel.user_id}</td>
			  				<td width="100">${travel.t_gubun}</td>
			  				<td width="100">
			  				<c:if test="${travel.t_person != 0}">
								${travel.t_person }
							</c:if>
							<c:if test="${travel.t_person == 0}">
								${travel.t_person= ""}
							</c:if>
							</td>
							<td width="100">
								<c:if test="${travel.t_dealstatus == '1'}">
									모집완료
								</c:if>
								<c:if test="${travel.t_dealstatus == '0'}">
									모집중
								</c:if>
							</td>
			  				<td width="100">
				  				<fmt:parseDate value="${travel.t_date}" var="dateFmt" pattern="yyyy-MM-dd HH:mm:ss"/>
				  				<fmt:formatDate value="${dateFmt}" pattern="yy-MM-dd"/>
			  				</td>
			  				<td width="100"><input type="button" value="삭제" onclick="deleteChk(${travel.t_num}, ${travel.t_relevel}, ${pageNum})"></td>
			  			</tr>
			  			<c:set var="startNum" value="${startNum - 1}"></c:set>
			  		</c:forEach>
			  	</c:if>
		  	<c:if test="${totCnt == 0}">
		  		<tr>
		  			<td colspan="7">동행자 찾기 게시글이 없습니다.</td>
		  		</tr>
		  	</c:if>
			  </tbody>
		  </table>
		</div>
		
		<!-- 페이지내비게이션 -->
		<nav class="pagenav" aria-label="Page navigation example">
		  <ul class="pagination">
		  	<c:if test="${startPage > blockSize}">
				<li class="page-item"><a class="page-link" href="<%=context%>/adTravelForm.do?pageNum=${startPage-blocksize}">이전</a></li>
			</c:if>
		    
		    <c:forEach var="i" begin="${startPage}" end="${endPage}">
				<li class="page-item"><a class="page-link" href="<%=context%>/adTravelForm.do?pageNum=${i}">${i}</a></li>
			</c:forEach>
		    
		    <c:if test="${endPage < pageCnt}">
				 <li class="page-item"><a class="page-link" href="<%=context%>/adTravelForm.do?pageNum=${startPage+blockSize }">다음</a></li>
			</c:if>
		  </ul>
		 </nav>
	</article>
	
<div style="margin-top: 100px, margin-left: 198px;"></div>
<c:import url="${context}/hs/footer.jsp"></c:import>
</body>
</html>
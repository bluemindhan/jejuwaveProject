<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

	function deleteChk() {
		if (confirm("Qna 댓글을 삭제하시겠습니까?") == true){    //확인
	     return true;
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
	  <form id="memform" action="<%=context%>/adQnaComDelete.do" method="post" name="frm" onsubmit="return deleteChk()">
	  <input type="hidden" name="b_num" value="${b_num}">
	  <input type="hidden" name="pageNum" value="${pageNum}">
	  
		  <c:if test="${comtot > 0}">
			<div class="input-group mb-3">
				  --&nbsp;&nbsp;총 댓글수는 ${comtot}개입니다.&nbsp;&nbsp;--
			</div>
		  	<c:forEach var="qc" items="${list}">
		  	<input type="hidden" name="com_num" value="${qc.com_num }">
		  	<input type="hidden" name="user_id" value="${qc.user_id}">
		  	<input type="hidden" name="com_date" value="${qc.com_date}">
		  	<input type="hidden" name="com_content" value="${qc.com_content}">
			<div class="input-group mb-3">
			  아이디 : ${qc.user_id}
			</div>
			<div class="input-group mb-3">
			  작성일 : ${qc.com_date}
			</div>
			<div class="input-group" id="textinfo">
			  <span class="input-group-text">댓글내용</span>
			  <textarea rows="3" cols="40" class="form-control" aria-label="With textarea" readonly="readonly">${qc.com_content}</textarea>
			</div>
			<div class="mybtn">
				<input class="btn btn-primary" type="submit" value="댓글삭제">
				<input class="btn btn-primary" type="button" value="이전" onclick="location.href='<%=context%>/adQnaForm.do?pageNum=${currentPage}'">
			</div>
		  	</c:forEach>
		  </c:if>
		  
		  <c:if test="${comtot == 0}">
	  		<div class="input-group mb-3">
				  댓글이 없습니다.
			</div>
			<div class="mybtn">
				<input class="btn btn-primary" type="button" value="이전" onclick="location.href='<%=context%>/adQnaForm.do?pageNum=${currentPage}'">
			</div>
		  </c:if>
	  </form>
	</article>
	
<div style="margin-top: 100px, margin-left: 198px;"></div>
<c:import url="${context}/hs/footer.jsp"></c:import>
</body>
</html>
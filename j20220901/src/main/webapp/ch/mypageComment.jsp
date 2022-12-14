<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JEJU WAVE 마이페이지</title>
<%
   String context = request.getContextPath();
%>
<c:import url="${context}/header.jsp"></c:import>

<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@900&display=swap" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR+Ligth:wght@300;900&display=swap" rel="stylesheet">
<link rel = "stylesheet" href ="<%=context%>/css/ch/mypageSetup.css?after">
<link rel = "stylesheet" href ="<%=context%>/css/ch/mypageComment.css?after">
<style type="text/css">
	table {
		width: 100%;
	}
</style>
<script type="text/javascript" src = "<%=context%>/js/jquery.js"></script>
<script type="text/javascript">
$(function(){
	$('#myComment_Travel').click(function(){
		
		$.ajax({
			url 	 : 'mypageCommentTravel.do',
			dataType : 'html',
			success	 : function(data){
						var e = $(data).find('#change_body');
						$('#change_body').html(e)
						}
		});
	
	});
	
});

$(function(){
	$('#myComment_qna').click(function(){
		$.ajax({
			url 	 : 'mypageComment.do',
			dataType : 'html',
			success	 : function(data){
						var e = $(data).find('#change_body');
						$('#change_body').html(e)
						}
		});
	
	});
	
});

// 버튼 클릭시 색 변경
$(function(){
	$('.myComment_button').click(function(){
		$('.myComment_button').css("background-color","rgb(233, 233, 233)");
		$('.myComment_button').css("color","black");
		$('.myComment_button').css("border","0");
		$(this).css("background-color","white");
		$(this).css("color","#FF3500");
		$(this).css("border","1px solid #FF3500");
		
	});
});


</script>

</head>
<body>

<main id = "content" style="background-color: rgb(248,248,248);">
	<div class = "mypage_set">
		<div class="Mypage_Main">
			<div class="Menu_section">
				<div class="Menu__profile">
					<div class="Profiles_image">
						<c:if test="${img_sub eq 'null'}">
							<span><img id="target_img" src="<%=context%>/images/vector_profile_default.svg"></span>
						</c:if>
						<c:if test="${img_sub ne 'null'}">
							<span><img id="target_img" src="<%=context%>/${img}" width = "200px"></span>
						</c:if>
						<span class = "user_id">	
						<c:if test="${user_id != null}">
							${user_id }
						</c:if>
						</span>
					</div>
				</div>
				<div class = "menu_link">
					<ul class="mypage_menu">
						<li class="link_mypage">
							<a class="link index_link" href="<%=context%>/mypageUpdate.do">
								<span class="mypage_name">계정관리</span>
							</a>
						</li>
						<li class="link_mypage">
							<a class="link index_link" href="<%=context%>/mypageTraveler.do">
								<span class="mypage_name">내 동행게시글</span>
							</a>
						</li>
						<li class="link_mypage">
							<a class="link index_link" href="<%=context%>/mypageCommunity.do">
								<span class="mypage_name">내 커뮤니티글</span>
							</a>
						</li>
						<li class="link_mypage">
							<a class="link index_link" href="<%=context%>/mypageBoard.do">
								<span class="mypage_name">내 Q&A글</span>
							</a>
						</li>
						<li class="link_mypage_comment">
							<a class="link index_link" href="<%=context%>/mypageComment.do">
								<span class="mypage_name" id ="comment_side">내 코멘트</span>
							</a>
						</li>
					</ul>
				</div>
			</div>
			<!-- 각 화면이 달라지는 부분 -->
			<div class="content_section">
			
				<button class = "myComment_button" id = "myComment_qna">
					<h2 class = "mypage_CommentMain">Q&A 내 댓글관리</h2>
				</button>
				<button class = "myComment_button" id = "myComment_Travel">
					<h2 class = "mypage_CommentMain">동행자게시판 내 댓글관리</h2>
				</button>
				<div class = "change_body" id = "change_body">
		 		<form action="<%=context%>/mypageAlldelete.do" method="get">
				<%-- <form action="<%=context%>/ch/Calendar.jsp" method="get"> --%>
					<c:if test="${ totCnt > 0}">
						<c:forEach var="board" items="${list }">
						<table>
							<tr>
								<td colspan="3" >
								<input type="text" id = "b_num" name = "b_num" value="${board.b_num }" hidden="true">
								<input type="text" id = "com_num" name = "com_num" value="${board.com_num }" hidden="true">
								<input type="checkbox" name="chk_com" value="${board.b_num },${board.com_num}">
									<div class = "c_content" style = "cursor: pointer" onclick="location.href='qnaWriteCheck.do?b_num=${board.b_num}';">
									${board.com_content}								
									</div>
								</td>
								<td class = "c_date">
									<fmt:formatDate value="${board.com_date}" pattern="yy-MM-dd"/>
								</td>
							</tr>
							<tr>
								<td>
									<div class = "button_updateform">
									<button type="button" class = "button_delete" onclick="location.href='mypageCommentDelete.do?b_num=${board.b_num}&com_num=${board.com_num }';">삭제</button>
									</div>
								</td>
							</tr>
						</table>
							<c:set var="startNum" value="${startNum - 1 }" />
						</c:forEach> 
					</c:if>
					<input type="submit" class = "allDel" value="삭제">
				</form>	
				<form action="mypageComment.do">
						<div class="search_form">
							<input type ="text"  class="search_input"	name="search" placeholder="댓글내용">
							<div class = "search_image">
								<input type ="submit" class = "search_submit" value="">
							</div>
						</div>
				</form>
					
				<div style="text-align: center; padding-top: 20px;">
				<c:if test="${startPage > blockSize }">
						<button class = "page_nation" type = "button" onclick="location.href='<%=context%>/mypageComment.do?pageNum=${startPage-1 }'"
						style ="
					    border: #eeee 2px solid;
					    background-color: white;
					    color: black;
					    padding: 10px 10px;
					    margin:3px;
					    font: bold 12px tahoma;
						">이전</button>
				</c:if>
				<c:forEach var="i" begin = "${startPage }" end = "${endPage }">
						<button class = "page_nation" type = "button" onclick="location.href='<%=context%>/mypageComment.do?pageNum=${i }'" 
						style ="
					    border: #eeee 2px solid;
					    background-color: white;
					    color: black;
					    padding: 10px 10px;
					    margin:3px;
					    font: bold 12px tahoma;
						">${i }</button>
				</c:forEach>
				<c:if test="${endPage < pageCnt }">
						<button class = "page_nation" type = "button" onclick="location.href='<%=context%>/mypageComment.do=${startPage+1 }'"
						style ="
					    border: #eeee 2px solid;
					    background-color: white;
					    color: black;
					    padding: 10px 10px;
					    margin:3px;
					    font: bold 12px tahoma;
						">[다음]</button>
				</c:if>
				</div>
				</div>
			</div>
		</div>
		</div>
</main>
<footer class="py-5 bg-dark" style="top: 180%;">
		<div class="container">
			<p class="m-0 text-center text-white">Copyright &copy; Your
				Website 2022</p>
		</div>
</footer>
</body>
</html>
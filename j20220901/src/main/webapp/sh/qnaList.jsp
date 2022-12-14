<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<%
String context = request.getContextPath();
%>
<title>JEJU WAVE Q&A</title>

<c:import url="${context}/header.jsp"></c:import>
<meta charset="UTF-8">



<link
   href="https://fonts.googleapis.com/css2?family=Nanum+Gothic&display=swap"
   rel="stylesheet">
<link rel="stylesheet" href="<%=context%>/css/sh/qnamain.css">
<link rel="stylesheet" href="<%=context%>/css/sh/qnaUpdate.css">
<link rel="stylesheet" href="<%=context%>/css/sh/qnaList.css">
<script src="http://code.jquery.com/jquery-latest.min.js"></script>



<!-- ----------------상단 고정이미지 ---------------  -->
<header class="py-5 mb-5"
   style="background-image: url('<%=context%>/sh_images/jeju_air44.jpg'); background-size: cover; margin-top: 150px;">
   <div class="container px-4 px-lg-5 mt-5 mb-5">
      <div class="text-center text-white">
         <h3 class="display-5 fw-bolder">QnA게시판</h3>
         <p class="lead fw-normal text-white-50 mb-0">여행에 대한 질문을 해보세요</p>
      </div>
   </div>
</header>

<script type="text/javascript">

function sorting(sort) {
 
   location.href='<%=context%>/qnaList.do?sort=' + sort;

   }
</script>


<body>

   <div style="margin-bottom: 400px; margin-top: 70px;" class="main">
      <table class="mainTable">



 <!-- 검색어 기능 -->
         <div style=" width: 1000px; margin-left: auto; margin-right: 200px; margin-bottom: 40px;" 
            class="col-md-auto Search__SearchInputWrappper-sc-1ef83fv-0 beOSqn">
            <form action="<%=context%>/qnaSearchList.do">
               <div width="300px"
                  class="Search__SearchInputWrappper-sc-1ef83fv-0 beOSqn">
                  <span
                     class="CommonIconSet__InitialIcon-sc-15eoam-0 CommonIconSet__MagnifierGrayIconContent-sc-15eoam-1 jZNHYY QjNCN"></span>

                  <input type="text" name="searchWord" placeholder="검색어를 입력하세요">
               </div>
               
               <div style="font-size: 14px; color: #808080" >정렬&nbsp&nbsp&nbsp| </div>
            </form>
             

            <div style="margin-left: 10px;"> 
            
            

               <!-- 정렬 -->
               <select class="select" name="sort" onchange="sorting(this.value);">
                  
                  <option value="0" disabled="disabled">정렬</option>
                  <option value="1" <c:if test= "${sort eq '1'}"> selected="selected" </c:if> >등록순</option>
                  <option value="2"<c:if test= "${sort eq '2'}"> selected="selected" </c:if> >댓글순</option>
               </select>

               <!-- 채택기다리는 글만보기 -->
               <button
                  onclick="location.href='<%=context %>/qnaList2.do?sort=${sort }'"
                  class="comment_button1">답변을 기다리는 질문만 보기</button>

            </div>





         </div>

      


         <c:if test="${totCnt > 0 }">
            <c:forEach var="board" items="${list}">
               <th style="vertical-align: super;">
                  <!-- 채택완료/답변대기 --> <c:if test="${board.b_success eq '채택완료' }">
                     <span>${board.b_success }</span>
                  </c:if> <c:if test="${board.b_success ne '채택완료' }">
                     <span style="color: #FF3500;">${board.b_success }</span>
                  </c:if>
               </th>
               <!-- 제목 -->
               <th class=" title2"><a
                  href='qnaWriteCheck.do?b_num=${board.b_num}&user_id=${user_id}'>
                     ${board.b_title}</a></th>
               <!--  내용 , 테마 -->
               <th><a
                  href='qnaWriteCheck.do?b_num=${board.b_num}&user_id=${user_id}'>
                   <span class="content1"><span class="theme">#${board.b_theme}</span>&nbsp; &nbsp; ${board.b_content }</span>
               </a></th>
               <!-- 아이디 -->
               <tr class="last">
                  <td></td>
                  <td><img src="<%=context%>/${board.fn_user_img}"
                     class="userIconColor-1 rounded-circle me-2  align-center bg-white"
                     width="40" height="40">${board.user_id}</td>
                  <!-- 해시태그 -->
                  <td><c:choose>
                        <c:when test="${null eq board.l_hash1 }">&nbsp; &nbsp; &nbsp;</c:when>
                        <c:otherwise>
                           <span class="hash" style="margin-left: 27px;">#${board.l_hash1}</span>&nbsp; &nbsp; &nbsp;</c:otherwise>
                     </c:choose> <c:choose>
                        <c:when test="${null eq board.l_hash2 }">&nbsp; &nbsp; &nbsp;</c:when>
                        <c:otherwise>
                           <span class="hash">#${board.l_hash2}</span>&nbsp; &nbsp; &nbsp;</c:otherwise>
                     </c:choose> <c:choose>
                        <c:when test="${null eq board.l_hash3 }">&nbsp; &nbsp; &nbsp;</c:when>
                        <c:otherwise>
                           <span class="hash">#${board.l_hash3}</span>&nbsp; &nbsp; &nbsp;</c:otherwise>
                     </c:choose>
                     </td>
                     <td>
                  <span style="font-size: 13px; color: gray;"> <img src="images/comm_icon.png" width="15" height="15" >&nbsp;${board.com_cnt }&nbsp;&nbsp;</span>
               </td>
               </tr>
               <c:set var="startNum" value="${startNum -1 }"></c:set>
            </c:forEach>
         </c:if>
         <c:if test="${totCnt ==0 }">
            <tr>
               <td colspan=7>데이터가 없네ㅜ</td>
            </tr>

         </c:if>

      </table>

      <!-- paging section-->
      <br> <br>
      <nav aria-label="Page navigation example"
         class="d-flex justify-content-center">
         <ul class="pagination">
            <c:if test="${startPage > blockSize}">
               <li class="page-item"><a class="page-link"
                  href='qnaList.do?pageNum=${startPage-blockSize}'
                  aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
               </a></li>
            </c:if>
            <c:forEach var="i" begin="${startPage }" end="${endPage }">
               <li class="page-item"><a class="page-link"
                  href='qnaList.do?sort=${sort}&pageNum=${i}'>${i}</a></li>
            </c:forEach>
            <c:if test="${endPage < pageCnt}">
               <li class="page-item"><a class="page-link"
                  href='qnaList.do?pageNum=${startPage+blockSize}' aria-label="Next">
                     <span aria-hidden="true">&raquo;</span>
               </a></li>
            </c:if>
         </ul>
      </nav>


   </div>



   <c:import url="${context}/footer.jsp"></c:import>
</body>
</html>
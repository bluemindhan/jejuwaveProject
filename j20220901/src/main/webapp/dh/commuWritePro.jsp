<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<% 
String context = request.getContextPath();
%>
</head>
<body>
	<c:if test="${results[0] > 0 && results[1] > 0}">
		<script type="text/javascript">
			alert("게시글 작성 완료");
			location.href="<%=context%>/commuList.do?pageNum=${pageNum }";
		</script>
	</c:if>
	<c:if test="${results[0] == 0 }">
		<script type="text/javascript">
			alert("글내용이나 태그을 잘 입력해주세요");
			location.href="<%=context%>/commuList.do?pageNum=${pageNum };
		</script>
	</c:if>
	<c:if test="${results[1] == 0 }">
		<script type="text/javascript">
			alert("조건에 맞는 사진을 첨부해주세요")
			location.href="<%=context%>/commuList.do?pageNum=${pageNum }
		</script>
	</c:if>
</body>
</html>
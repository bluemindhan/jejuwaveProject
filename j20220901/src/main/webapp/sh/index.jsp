<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<title>JEJU WAVE Q&A</title>
<head>
<%
   String context = request.getContextPath();
%>
<meta charset="UTF-8">

</head>
<body>
<script type="text/javascript">
    location.href="<%=context %>/qnaList.do?sort=1"; 
</script>
</body>
</html>
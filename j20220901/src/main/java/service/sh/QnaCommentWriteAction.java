package service.sh;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.Qna_Comment;
import dao.Qna_CommentDao;
import service.CommandProcess;

public class QnaCommentWriteAction implements CommandProcess {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("QnaCommentWriteAction start..");
		
		try {
			int com_num = 0;
			Qna_Comment comment = new Qna_Comment();
			comment.setCom_content(request.getParameter("com_content"));
			comment.setB_num(Integer.parseInt(request.getParameter("b_num")));
			comment.setCom_num(com_num);
			//로그인 아이디 받아오기
			HttpSession session = request.getSession();
			String user_id = (String) session.getAttribute("user_id");
			comment.setUser_id(user_id);
			
			Qna_CommentDao cd = Qna_CommentDao.getInstance();
			int result = cd.insert(comment);
			
			request.setAttribute("result", result);
			request.setAttribute("b_num", comment.getB_num());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return "sh/qnaCommentWrite.jsp";
	}

}

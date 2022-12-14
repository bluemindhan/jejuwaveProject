package service.ry;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Member;
import dao.MemberDao;
import service.CommandProcess;

public class IdCheckFormAction implements CommandProcess {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("IdCheckFormAction Start..");
		String user_id = request.getParameter("user_id");
		System.out.println("IdCheckFormAction user_id..->"+request.getParameter("user_id"));
		String pageNum = request.getParameter("pageNum");
		try {
			MemberDao md = MemberDao.getInstance();
			Member member = md.select(user_id);
			request.setAttribute("user_id", user_id);
			request.setAttribute("pageNum", pageNum);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "ry/idCheckForm.jsp";
	}

}

package service.hs;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.AdminDao;
import dao.Commu;
import service.CommandProcess;

public class AdCommuFormAction implements CommandProcess {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("AdCommuFormAction service start...");
		AdminDao ad = AdminDao.getInstance();
		try {
			int 	totCnt = ad.getCommuCnt();
			String pageNum = request.getParameter("pageNum");
			
			if (pageNum==null || pageNum.equals("")) {	pageNum = "1";	}
			int currentPage = Integer.parseInt(pageNum);     
			int pageSize 	= 10, blockSize = 10;
			int startRow	= (currentPage - 1) * pageSize + 1;        
			int endRow  	= startRow + pageSize - 1;              
			int startNum 	= totCnt - startRow + 1;
			
			List<Commu> list = ad.commuList(startRow, endRow);
			
			// 페이지의 수								
			int pageCnt   = (int) Math.ceil((double)totCnt/pageSize);			
			int startPage = (int)(currentPage-1)/blockSize*blockSize +1;
			int endPage   = startPage + blockSize -1;	
			// 공갈 Page 방지	
			if(endPage > pageCnt) endPage = pageCnt;
			
			request.setAttribute("list", list);
			request.setAttribute("pageNum", pageNum);
			request.setAttribute("totCnt", totCnt);
			request.setAttribute("currentPage", currentPage);
			request.setAttribute("startNum", startNum);
			request.setAttribute("blockSize", blockSize);
			request.setAttribute("pageCnt", pageCnt);
			request.setAttribute("startPage", startPage);
			request.setAttribute("endPage", endPage);
			request.setAttribute("pageNum", pageNum);
			request.setAttribute("startNum", startNum);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "hs/adCommuForm.jsp";
	}

}

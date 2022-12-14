package service.ry;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import dao.Member;
import dao.MemberDao;
import service.CommandProcess;

public class JoinProAction implements CommandProcess {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		System.out.println("JoinProAction Start..");
		int maxSize = 5 * 1024 * 1024;
		String fileSave = "/fileSave";
		// Servlet 상속 받지 못했을 떄 realPath 불러 오는 방법
		String realPath = request.getSession().getServletContext().getRealPath(fileSave);
	//	String realPath = getServletContext().getRealPath(fileSave);
		System.out.println("realPath->" + realPath);
		File fileSaveDir = new File(realPath);
		if (!fileSaveDir.exists()) { //저장경로에 해당 폴더가 없으면 만들기
			fileSaveDir.mkdirs();
		}
		MultipartRequest multi = 
	 	new MultipartRequest(request, realPath, maxSize, "utf-8", new DefaultFileRenamePolicy());
		Enumeration en = multi.getFileNames();
		// 서버에 저장된 파일 이름
		String serverSaveFilename = "";
		String upLoadFilename = "";
		while (en.hasMoreElements()) {
			//input 태그의 속성이 file인 태그의 name 속성값 :파라미터이름
			String parameterName = (String) en.nextElement();
			//서버에 저장된 파일 이름 
			serverSaveFilename = multi.getFilesystemName(parameterName);
			//전송전 원래의 파일 이름 
			String original = multi.getOriginalFileName(parameterName);
			//전송된 파일의 내용 타입 
			String type = multi.getContentType(parameterName);
			//전송된 파일속성이 file인 태그의 name 속성값을 이용해 파일객체생성 
			File file = multi.getFile(parameterName);
			System.out.println("real Path : " + realPath + "<br>");
			System.out.println("파라메터 이름 : " + parameterName + "<br>");
			System.out.println("실제 파일 이름 : " + original + "<br>");
			System.out.println("저장된 파일 이름 : " + serverSaveFilename + "<br>");
			System.out.println("파일 타입 : " + type + "<br>");
			if (file != null) {
				System.out.println("크기 : " + file.length() + "<br>");
			}
		}
		String fileusername = "fileSave\\"+serverSaveFilename;
		System.out.println("fileusername--> " + fileusername);
		String pageNum = request.getParameter("pageNum");
		String name = multi.getParameter("name");
		System.out.println("name ->"+ name);
		String title = multi.getParameter("title");
		System.out.println("title ->"+ title);
		int user_gubun = Integer.parseInt(multi.getParameter("user_gubun"));
		System.out.println("action user_gubun ->"+ user_gubun);
		
		upLoadFilename = realPath + "\\"+ serverSaveFilename;
		System.out.println("전달 upLoadFilename ->"+ upLoadFilename);
		
		try {
			MemberDao md = MemberDao.getInstance();
			Member member = new Member();
			member.setUser_id(multi.getParameter("user_id"));
			member.setUser_pw(multi.getParameter("user_pw"));
			member.setUser_email(multi.getParameter("user_email"));
			member.setUser_name(multi.getParameter("user_name"));
			member.setUser_info(multi.getParameter("user_info"));
			member.setUser_birth(multi.getParameter("user_birth"));
			member.setUser_gender(multi.getParameter("user_gender"));
			member.setUser_tel(multi.getParameter("user_tel"));
			member.setUser_gubun(user_gubun);
			
			member.setUser_img(fileusername);
			System.out.println("multi.getParameter(fileusername)"+fileusername);
			int result = md.insert(member);
			request.setAttribute("result", result);
			request.setAttribute("pageNum", pageNum);
			request.setAttribute("filename", "fileSave\\"+serverSaveFilename);
			request.setAttribute("upLoadFilename", upLoadFilename);
			request.setAttribute("title", title);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return "ry/joinPro.jsp";

	}
}

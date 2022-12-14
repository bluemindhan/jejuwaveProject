package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

public class MypageDao {
	private static final List<Mypage> List = null;
	private static MypageDao instance;
	private MypageDao() {}
	public static MypageDao getInstance() {
		if(instance == null) {
			instance = new MypageDao();
		}
		return instance;
	}
	private Connection getConnection() {
		Connection conn = null;
		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/OracleDB");
			conn = ds.getConnection();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println(conn);
		return conn;
	}
	public int getTotalCnt(String user_id, int t_dealstatus) throws SQLException {
		int max = 0;
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		
		String sql = "select count(*)from travel_board where user_id = ? and t_relevel = 0 ";
		if(t_dealstatus == 0) {sql += "and t_dealstatus = 0";}
		System.out.println("getTotalCnt sql ==> " + sql);
		try {
			// 모집중 조건 추가 됐을 경우
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user_id);
			System.out.println("getTotalCnt user id=>" + user_id);
			
			
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				max = rs.getInt(1);
				System.out.println("getTotalCnt ==> " + max);
			} else {
				System.out.println("getTotalCnt ==> 최댓값 실패(MypageDao)");
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if(rs != null) try {rs.close();} catch (Exception e) {} 
			if(pstmt != null)  try {pstmt.close();} catch (Exception e) {} 
			if(conn != null) try {conn.close();} catch (Exception e) {} 
		}
		return max;
	}
	
	public int getTotalCntBoard(String user_id, String search) throws SQLException {
		int max = 0;
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		
		String sql = "select count(*)from qna_board where user_id = ?";
		if(search != "fail") {
			sql += " and b_title like '%"+search+"%' or b_content like '%"+search+"%'";
			System.out.println("boardList search sql ==> " + sql);
		}
		
		
		try {	
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user_id);
			System.out.println("getTotalCntBoard user id=>" + user_id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				max = rs.getInt(1);
				System.out.println("getTotalCnt ==> 최댓값 추출성공(MypageDao)" + max);
			} else {
				System.out.println("getTotalCnt ==> 최댓값 실패(MypageDao)");
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if(rs != null) try {rs.close();} catch (Exception e) {} 
			if(pstmt != null)  try {pstmt.close();} catch (Exception e) {} 
			if(conn != null) try {conn.close();} catch (Exception e) {} 
		}
		return max;
	}
	
	
	public List<Mypage> travelList(String user_id, int startRow, int endRow, int t_dealstatus, String search) throws SQLException {
		List<Mypage> list = new ArrayList<Mypage>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * \r\n"
				+ " from (select rownum r, c.* \r\n"
				+ "      from (  select a.*, nvl(b.cnt,0) as reply_cnt  \r\n"
				+ "              from (select * from travel_board where t_Relevel= 0 and user_id = ?";
		// 버튼 클릭시 조건 추가 	// 버튼 클릭시 조건추가 (모집중인 글만 보기)
		if(t_dealstatus == 0) {sql += "and t_dealstatus = 0";}
		System.out.println("travelList t_dealstatus sql == " + sql);
	
		
		sql += ") a, \r\n"
				+ "                   (select t_ref, count(t_ref) as cnt  from travel_board where t_relevel !=0 group by t_ref) b \r\n"
				+ "              where a.t_ref = b.t_ref(+)  order by t_num desc\r\n"
				+ "           ) c) \r\n"
				+ " where   r between ? and ?";
		
		System.out.println("travelList sql ===> " + sql);
		
		// 검색창에 값을 입력했을경우
		if(search != "fail") {sql += " and t_title like '%"+search+"%' or t_content like '%"+search+"%'";}
		System.out.println("MypageDao travelList search = " + search);
		System.out.println("travelList search sql == " + sql);
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			System.out.println("pstmt == > " + pstmt);
			pstmt.setString(1, user_id);
			System.out.println("user_id == > " + user_id);
			pstmt.setInt(2, startRow);
			System.out.println("startRow == > " + startRow);
			pstmt.setInt(3, endRow);
			System.out.println("endRow == > " + endRow);
			rs = pstmt.executeQuery();
			
			
			
			System.out.println("travelList sql ===> " + sql);
			System.out.println("MypageDao travelList rs.next()->"+rs.next());
			do {
				Mypage mypage = new Mypage();
				
				mypage.setT_num			(rs.getInt("t_num"));
				mypage.setT_img			(rs.getString("t_img"));
				mypage.setUser_id		(rs.getString("user_id"));
				mypage.setT_title		(rs.getString("t_title"));
				mypage.setT_content		(rs.getString("t_content"));
				mypage.setT_gubun		(rs.getString("t_gubun"));
				mypage.setT_date		(rs.getString("t_date"));
				mypage.setT_person		(rs.getInt("t_person"));
				mypage.setT_start		(rs.getDate("t_start"));
				mypage.setT_end			(rs.getDate("t_end"));
				mypage.setT_dealstatus	(rs.getString("t_dealstatus"));
				mypage.setT_recnt (rs.getInt("reply_cnt"));
				
				list.add(mypage);
			} while(rs.next());
			System.out.println("list 값 찾기 :" + list);
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
			
		} finally {
			if(rs != null) try {rs.close();} catch (Exception e) {} 
			if(pstmt != null)  try {pstmt.close();} catch (Exception e) {} 
			if(conn != null) try {conn.close();} catch (Exception e) {} 
		}
		return list;
	}
	
	
	public int check(String user_id, String user_pw) throws SQLException {
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select  user_pw, user_gubun from member where user_id = ?";
		try {
			conn = getConnection();
			System.out.println("connection 연결" + conn);
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user_id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				Member member = new Member();
				String dbpasswd = rs.getString("user_pw");
				member.setUser_gubun(rs.getInt("user_gubun"));
				
				System.out.println("dbpasswd =>" + dbpasswd);
				System.out.println("user_pw : " + user_pw);
				System.out.println("db 탔음");
				
				if(dbpasswd.equals(user_pw)) result = 1;
				else result = 0;
			} else result = -1;
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if(rs != null) try {rs.close();} catch (Exception e) {} 
			if(pstmt != null)  try {pstmt.close();} catch (Exception e) {} 
			if(conn != null) try {conn.close();} catch (Exception e) {} 
		}
		
		return result;
	}
	
	public Member select(String user_id) throws SQLException {
		Member md = new Member();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select USER_INFO,USER_BIRTH,USER_TEL,USER_EMAIL,USER_IMG, USER_GUBUN from member where user_id = ?";
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user_id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				md.setUser_info		(rs.getString("user_info"));
				System.out.println(md.getUser_info());
				md.setUser_birth	(rs.getString("user_birth"));
				md.setUser_tel		(rs.getString("user_tel"));
				md.setUser_email	(rs.getString("user_email"));
				md.setUser_img		(rs.getString("user_img"));
				md.setUser_gubun	(rs.getInt("user_gubun"));
				System.out.println(md.getUser_img());
			}
		} catch (SQLException e) {
			System.out.println("MemberDao select문 오류 : " + e.getMessage());
		} finally {
			if(rs != null) 		try 	{rs.close();} 		catch (Exception e) {} 
			if(pstmt != null)  	try 	{pstmt.close();} 	catch (Exception e) {} 
			if(conn != null) 	try 	{conn.close();}		catch (Exception e) {} 
		}
		
		return md;
	}
	public String imageSelect(String user_id) throws SQLException {
		String img = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select USER_IMG from member where user_id = ?";
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user_id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				img = rs.getString(1);
			}
		} catch (SQLException e) {
			System.out.println("MemberDao select문 오류 : " + e.getMessage());
		} finally {
			if(rs != null) try {rs.close();} catch (Exception e) {} 
			if(pstmt != null)  try {pstmt.close();} catch (Exception e) {} 
			if(conn != null) try {conn.close();} catch (Exception e) {} 
		}
		
		return img;
	}
	public List<Mypage> boardList(String user_id, int startRow, int endRow, String search) throws SQLException {
		List<Mypage> list = new ArrayList<Mypage>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "select *\r\n"
				+ "from( select rownum r, c.*\r\n"
				+ "     from(select a.*,nvl(b.cnt,0) as b_reply_cnt\r\n"
				+ "             from (select * from qna_board where user_id = ?) a,\r\n"
				+ "                   (select b_num , count(b_num) as cnt from qna_comment group by b_num) b\r\n"
				+ "            where a.b_num = b.b_num(+) order by a.b_num desc\r\n"
				+ "        ) c)  \r\n"
				+ "where r between ? and ?";
		System.out.println("boardList sql : " + sql);
		
		// 검색창 조건
		if(search != "fail") {
			sql += " and b_title like '%"+search+"%' or b_content like '%"+search+"%'";
			System.out.println("boardList search sql ==> " + sql);
		}
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user_id);
			pstmt.setInt(2, startRow);
			pstmt.setInt(3, endRow);
			System.out.println("boardList user_id : " + user_id);
			System.out.println("boardList startRow : " + startRow);
			System.out.println("boardList endRow : " + endRow);
			ResultSet rs = pstmt.executeQuery();
			System.out.println("boardList ResultSet=> " + rs);
			System.out.println("boardList rs.next() : " + rs.next());
			do {
				Mypage mypage = new Mypage();
				System.out.println("boardlist는 실행완료 : " + rs);
				mypage.setB_num(rs.getInt("b_num"));
				System.out.println("DAO boardList setB_num : " + mypage.getB_num());
				mypage.setUser_id(rs.getString("user_id"));
				mypage.setB_date(rs.getDate("b_date"));
				mypage.setB_title(rs.getString("b_title"));
				mypage.setB_content(rs.getString("b_content"));
				mypage.setB_success(rs.getString("b_success"));
				mypage.setB_recnt(rs.getInt("b_reply_cnt"));
				System.out.println("boardList B_recnt ===> " + mypage.getB_recnt());
				list.add(mypage);
			} while(rs.next());
		} catch(Exception e) {
			System.out.println("boardList e.getMessage()->"+e.getMessage());
		} finally {
			if(pstmt != null)  try {pstmt.close();} catch (Exception e) {} 
			if(conn != null) try {conn.close();} catch (Exception e) {} 
		}
		System.out.println("DAO boardList list.size()->"+list.size());
		
		return list;
	}
	public int getTotalQnaCommentCnt(String user_id, String search) throws SQLException {
		int max = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select count(*) from qna_comment where user_id = ?";
		if(search != "fail") {
			sql += "and com_content like'%"+search+"%'";
		}
		
		System.out.println("getTotalQnaCommentCnt sql : " + sql);
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user_id);
			rs = pstmt.executeQuery();
			System.out.println("getTotalCommentCnt rs 실행되는지=> " + rs);
			if(rs.next()) {
				max = rs.getInt(1);
				System.out.println("getTotalCommentCnt : 실행완료" + max);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}  finally {
			if(rs != null) try {rs.close();} catch (Exception e) {} 
			if(pstmt != null)  try {pstmt.close();} catch (Exception e) {} 
			if(conn != null) try {conn.close();} catch (Exception e) {} 
		}
		return max;
	}
	public int getTotalTravelCommentCnt(String user_id, String search) throws SQLException {
		int max = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select count(*) from travel_board where user_id = ? and t_restep != 0 ";
		if(search != "fail") {
			sql+="and t_content like '%"+search+"%'";
		}
		
		
		System.out.println("getTotalTravelCommentCnt sql : " + sql);
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user_id);
			rs = pstmt.executeQuery();
			System.out.println("getTotalCommentCnt rs 실행되는지=> " + rs);
			if(rs.next()) {
				max = rs.getInt(1);
				System.out.println("getTotalCommentCnt : 실행완료" + max);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}  finally {
			if(rs != null) try {rs.close();} catch (Exception e) {} 
			if(pstmt != null)  try {pstmt.close();} catch (Exception e) {} 
			if(conn != null) try {conn.close();} catch (Exception e) {} 
		}
		return max;
	}
	public int getTotalCntCommunity(String user_id, String search) throws SQLException{
		int max = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select count(*) from community where user_id = ?";
		if(search != "fail") {
			sql += "and c_content like '%"+search+"%' or c_hash like '%"+search+"%'";
			System.out.println("communityList search sql => " + sql);
		}
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user_id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				max = rs.getInt(1);
			}
		} catch (Exception e) {
			System.out.println("getTotalCntCommunity 오류 : " + e.getMessage());
		} finally {
			if(rs != null) try {rs.close();} catch (Exception e) {} 
			if(pstmt != null)  try {pstmt.close();} catch (Exception e) {} 
			if(conn != null) try {conn.close();} catch (Exception e) {} 
		}
		
		return max;
	}
	public List<Mypage> communityList(String user_id, int startRow, int endRow, String search) throws SQLException {
		List<Mypage> list = new ArrayList<Mypage>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = " select * "
				+ " from ( "
				+ "        select rownum rn , a.* "
				+ "        from ( "
				+ "                select ci.c_num, c.user_id, c_hash, c_content, c_date , min(c_img_path) keep (DENSE_RANK last order by ci.c_num) as c_img_path "
				+ "                from community c, community_img ci "
				+ "                where c.c_num = ci.c_num "
				+ "                and user_id=? "
				+ "                group by ci.c_num, c_hash, c_content, c_date ,user_id "
				+ "                order by ci.c_num desc "
				+ "             ) a "
				+ "     ) "
				+ " where rn between ? and ?";
		System.out.println("communityList sql =>" + sql);
		
		if(search != "fail") {
			sql += "and c_content like '%"+search+"%' or c_hash like '%"+search+"%'";
			System.out.println("communityList search sql => " + sql);
		}
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user_id);
			pstmt.setInt(2, startRow);
			pstmt.setInt(3, endRow);
			rs = pstmt.executeQuery();
			System.out.println("communityList rs next() :   " + rs.next());
			do {
				Mypage mypage = new Mypage();
				mypage.setC_num(rs.getInt("c_num"));
				mypage.setC_content(rs.getString("c_content"));
				mypage.setC_date(rs.getDate("c_date"));
				mypage.setC_hash(rs.getString("c_hash"));
				mypage.setC_img_path(rs.getString("c_img_path"));
				list.add(mypage);
			} while (rs.next());
		} catch (Exception e) {
			System.out.println("communityList 오류  :  " + e.getMessage());
		} finally {
			if(rs != null) try {rs.close();} catch (Exception e) {} 
			if(pstmt != null)  try {pstmt.close();} catch (Exception e) {} 
			if(conn != null) try {conn.close();} catch (Exception e) {} 
		}
		
		return list;
		
	}
	public int update(String user_img, String user_info, String user_birth, String user_tel, String user_email, String user_id) throws SQLException {
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "Update member set user_img = ?, user_info = ?,"
				+ " user_birth = ?, user_tel = ?, user_email = ? where user_id = ?";
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user_img);
			pstmt.setString(2, user_info);
			pstmt.setString(3, user_birth);
			pstmt.setString(4, user_tel);
			pstmt.setString(5, user_email);
			pstmt.setString(6, user_id);
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("update 오류 : " + e.getMessage());
		} finally {
			if(pstmt != null)  try {pstmt.close();} catch (Exception e) {} 
			if(conn != null) try {conn.close();} catch (Exception e) {} 
		}
		
		return result;
		
	}
	public List<Mypage> commentList(String user_id, int startRow, int endRow, String search) throws SQLException {
		List<Mypage> list = new ArrayList<Mypage>();
		String sql = "select * "
				+ " from( "
				+ "    select rownum r, a.* "
				+ "    from (select * "
				+ "          from qna_comment "
				+ "          where user_id = ? "
				+ "          order by com_date desc "
				+ "          ) a ) "
				+ " where r BETWEEN ? and ?";
		System.out.println("commentList sql : " + sql);
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		if(search != "fail") {
			sql += "and com_content like'%"+search+"%'";
		}
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user_id);
			System.out.println("commentList user_id => " + user_id);
			pstmt.setInt(2, startRow);
			System.out.println("commentList startRow => " + startRow);
			pstmt.setInt(3, endRow);
			System.out.println("commentList endRow => " + endRow);
			rs = pstmt.executeQuery();
			System.out.println("commentList : " + rs);
			System.out.println("commentList rs.next () : " + rs.next());
			
			do{
				Mypage mypage = new Mypage();
				mypage.setB_num(rs.getInt("b_num"));
				mypage.setCom_num(rs.getInt("com_num"));
				mypage.setCom_content(rs.getString("com_content"));
				mypage.setCom_date(rs.getDate("com_date"));
				list.add(mypage);
				System.out.println("commentList list : " + list);
			} while(rs.next());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}  finally {
			if(rs != null) try {rs.close();} catch (Exception e) {} 
			if(pstmt != null)  try {pstmt.close();} catch (Exception e) {} 
			if(conn != null) try {conn.close();} catch (Exception e) {} 
		}
		
		return list;
	}
	public List<Mypage> commentList2(String user_id, int startRow, int endRow, String search) {
		List<Mypage> list = new ArrayList<Mypage>();
		String sql = "select * "
				+ "from( "
				+ "    select rownum r, a.* "
				+ "    from (select *"
				+ "          from travel_board "
				+ "          where user_id = ? "
				+ "          and t_relevel != 0 "
				+ "          order by t_date desc "
				+ "          ) a ) "
				+ "where r BETWEEN ? and ?";
		System.out.println("commentList2 sql2 : " + sql);
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		if(search != "fail") {
			sql+="and t_content like '%"+search+"%'";
		}
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user_id);
			System.out.println("commentList user_id => " + user_id);
			pstmt.setInt(2, startRow);
			System.out.println("commentList startRow => " + startRow);
			pstmt.setInt(3, endRow);
			System.out.println("commentList endRow => " + endRow);
			rs = pstmt.executeQuery();
			System.out.println("commentList : " + rs);
			System.out.println("commentList rs.next () : " + rs.next());
			
			do{
				Mypage mypage = new Mypage();
				mypage.setT_ref(rs.getInt("t_ref"));
				mypage.setT_num(rs.getInt("t_num"));
				mypage.setT_content(rs.getString("t_content"));
				mypage.setT_date(rs.getString("t_date"));
				list.add(mypage);
				System.out.println("commentList list : " + list);
			} while(rs.next());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}  finally {
			if(rs != null) try {rs.close();} catch (Exception e) {} 
			if(pstmt != null)  try {pstmt.close();} catch (Exception e) {} 
			if(conn != null) try {conn.close();} catch (Exception e) {} 
		}
		
		return list;
	}
	public int deleteTraveler(String t_num) {
		int result = 0 ;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "delete travel_board where t_num = ?";
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, t_num);
			result = pstmt.executeUpdate();
			
			if(result != 0 ) System.out.println("성공");
			else System.out.println("삭제실패");
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if(pstmt != null)  try {pstmt.close();} catch (Exception e) {} 
			if(conn != null) try {conn.close();} catch (Exception e) {} 
		}
		
		
		
		return result;
	}
	public int deleteboard(String b_num) {
		int result = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "delete qna_board where b_num = ?";
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, b_num);
			result = pstmt.executeUpdate();
			
			if(result != 0 ) System.out.println("deleteboard ---> 성공");
			else System.out.println("deleteboard ---> 삭제실패");
		} catch (Exception e) {
			System.out.println("deleteboard error ---> " + e.getMessage());
		} finally {
			if(pstmt != null)  try {pstmt.close();} catch (Exception e) {} 
			if(conn != null) try {conn.close();} catch (Exception e) {} 
		}
	
		return result;
	}
	public int deleteCommunity(String c_num) {
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "delete community where c_num = ?";
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, c_num);
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("MypageDao deleteCommunity 오류" +  e.getMessage());
		} finally {
			if(pstmt != null)  try {pstmt.close();} catch (Exception e) {} 
			if(conn != null) try {conn.close();} catch (Exception e) {} 
		}
		
		return result;
		
	}
	
	
	public int qnaComDelete(String b_num, String com_num) {
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "delete qna_comment where b_num = ? and com_num = ?";
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, b_num);
			System.out.println("MypageDao qnaComDelete b_num==> " + b_num);
			pstmt.setString(2, com_num);
			System.out.println("MypageDao qnaComDelete com_num==> " + com_num);
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("MypageDao qnaComDelete 오류" +  e.getMessage());
		} finally {
			if(pstmt != null)  try {pstmt.close();} catch (Exception e) {} 
			if(conn != null) try {conn.close();} catch (Exception e) {} 
		}
		return result;
		
	}
	
	public int TravelComDelete(String t_num) {
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "update travel_board set t_content='삭제된 댓글입니다.' where t_num=?";
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, t_num);
			result = pstmt.executeUpdate();
			if(result != 0) result = 1;
		} catch (Exception e) {
			System.out.println("MypageDao qnaComDelete 오류" +  e.getMessage());
		} finally {
			if(pstmt != null)  try {pstmt.close();} catch (Exception e) {} 
			if(conn != null) try {conn.close();} catch (Exception e) {} 
		}
		return result;
	}
	
	public int pwUpdate(String user_pw, String user_id) {
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "update member set user_pw = ? where user_id = ?";
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user_pw);
			pstmt.setString(2, user_id);
			result = pstmt.executeUpdate();
			if(result != 0) {
				System.out.println("MypageDao pwUpdate ==> 성공");
			}
		} catch (Exception e) {
			System.out.println("MypageDao pwUpdate 오류" + e.getMessage());
		} finally {
			if(pstmt != null)  try {pstmt.close();} catch (Exception e) {} 
			if(conn != null) try {conn.close();} catch (Exception e) {} 
		}
		return result;
	}
	
	public int trvAllDel(String[] chk_travel) {
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "delete travel_board where t_num=?";
		System.out.println("MypageDao trvAllDel sql =>" + sql);
		System.out.println("MypageDao chk_travel[i]");
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			for(int i= 0; i<chk_travel.length;i++) {
				pstmt.setString(1, chk_travel[i]);
				
				// addBatch에 담기
				pstmt.addBatch();
				
				// 파라미터 clear
				pstmt.clearParameters();
			}
			
			pstmt.executeBatch();
			conn.commit();	
			pstmt.clearBatch();
			
			result = 1;
			
		} catch (Exception e) {
			System.out.println("MypageDao trvAllDel 오류" + e.getMessage());
		} finally {
			if(pstmt != null)  try {pstmt.close();} catch (Exception e) {} 
			if(conn != null) try {conn.close();} catch (Exception e) {} 
		}
		
		return result;
	}
	public int CommAllDel(String[] chk_commu) {
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "delete community where c_num=?";
		System.out.println("MypageDao CommAllDel sql =>" + sql);
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			for(int i= 0; i<chk_commu.length;i++) {
				pstmt.setString(1, chk_commu[i]);
				
				// addBatch에 담기
				pstmt.addBatch();
				
				// 파라미터 clear
				pstmt.clearParameters();
			}
			
			pstmt.executeBatch();
			conn.commit();	
			pstmt.clearBatch();
			
			result = 1;
			
		} catch (Exception e) {
			System.out.println("MypageDao trvAllDel 오류" + e.getMessage());
		} finally {
			if(pstmt != null)  try {pstmt.close();} catch (Exception e) {} 
			if(conn != null) try {conn.close();} catch (Exception e) {} 
		}
		
		return result;
	}
	public int QnaAllDel(String[] chk_qna) {
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "delete qna_board where b_num=?";
		System.out.println("MypageDao chk_qna sql =>" + sql);
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			for(int i= 0; i<chk_qna.length;i++) {
				pstmt.setString(1, chk_qna[i]);
				
				// addBatch에 담기
				pstmt.addBatch();
				
				// 파라미터 clear
				pstmt.clearParameters();
			}
			
			pstmt.executeBatch();
			conn.commit();	
			pstmt.clearBatch();
			
			result = 1;
			
		} catch (Exception e) {
			System.out.println("MypageDao chk_qna 오류" + e.getMessage());
		} finally {
			if(pstmt != null)  try {pstmt.close();} catch (Exception e) {} 
			if(conn != null) try {conn.close();} catch (Exception e) {} 
		}
		
		return result;
	}
	public int ComAllDel(String[] com_result) {
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "delete qna_comment where b_num=? and com_num = ?";
		System.out.println("MypageDao chk_qna sql =>" + sql);
//		String chk_com1 = Arrays.toString(chk_com);
//		System.out.println("ComAllDel chk_com1 ==> " + chk_com1); String []
//		com_result = chk_com1.split(",");
//		System.out.println("MypageDao ComAllDel com_result ==> " + com_result);
		
//		System.out.println("ComAllDel chk_com.length => " + chk_com.length);
		
		System.out.println("MypageDao ComAllDel ==== com_result[0] : "+com_result[0]);
		System.out.println("MypageDao ComAllDel ==== com_result[1] : "+com_result[1]);
		System.out.println("MypageDao ComAllDel ==== com_result[2] : "+com_result[2]);
		System.out.println("MypageDao ComAllDel ==== com_result[3] : "+com_result[3]);
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			for(int i= 0; i<com_result.length;i+=2) {
				pstmt.setString(1, com_result[i]);
				pstmt.setString(2, com_result[i+1]);
				
				// addBatch에 담기
				pstmt.addBatch();
				
				// 파라미터 clear
				pstmt.clearParameters();
			}
			
			pstmt.executeBatch();
			conn.commit();	
			pstmt.clearBatch();
			
			result = 1;
			
		} catch (Exception e) {
			System.out.println("MypageDao chk_qna 오류" + e.getMessage());
		} finally {
			if(pstmt != null)  try {pstmt.close();} catch (Exception e) {} 
			if(conn != null) try {conn.close();} catch (Exception e) {} 
		}
		
		return result;
	}

	}
	


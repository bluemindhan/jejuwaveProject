package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class AdminDao {
	private static AdminDao instance;
	private AdminDao() {
	}
	public static AdminDao getInstance() {
		if(instance == null) {
			instance = new AdminDao();
		}
		return instance;
	}
	
	private Connection getConnection() {
		Connection conn = null;
		try {
			Context     ctx = new InitialContext();
			DataSource   ds = (DataSource) ctx.lookup("java:comp/env/jdbc/OracleDB");
					   conn = ds.getConnection();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}
	
	// 회원수
	public int getTotalCnt() throws SQLException {
		int tot = 0;
		Connection conn = null;
		Statement  stmt = null;
		ResultSet    rs = null;
		String sql 		= "select count(*) from member";
		
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			rs   = stmt.executeQuery(sql);
			if(rs.next()) tot = rs.getInt(1);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if(rs   != null)    rs.close();
			if(stmt != null)  stmt.close();
			if(conn != null)  conn.close();
		}
		return tot;
		
	}
	
	// 동행자 찾기 게시글 수
	public int getTravelCnt() throws SQLException {
		int tot = 0;
		Connection conn = null;
		Statement  stmt = null;
		ResultSet    rs = null;
		String sql 		= "select count(*) from travel_board";
		
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			rs   = stmt.executeQuery(sql);
			if(rs.next()) tot = rs.getInt(1);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if(rs   != null)    rs.close();
			if(stmt != null)  stmt.close();
			if(conn != null)  conn.close();
		}
		return tot;
	}
	
	// Qna 게시글 수
	public int getQnabrdCnt() throws SQLException {
		int tot = 0;
		Connection conn = null;
		Statement  stmt = null;
		ResultSet  	 rs = null;
		String      sql = "select count(*) from qna_board";
		
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			rs   = stmt.executeQuery(sql);
			if(rs.next()) tot = rs.getInt(1);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if(rs   != null)    rs.close();
			if(stmt != null)  stmt.close();
			if(conn != null)  conn.close();
		}
		return tot;
	}
	
	// qna 게시글에 따른 댓글 수
	public int getQnaComCnt(int b_num) throws SQLException {
		int tot = 0;
		Connection 		   conn = null;
		PreparedStatement pstmt = null;
		ResultSet 		     rs = null;
		String				sql = "select count(*) from qna_comment where b_num = ?";
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, b_num);
			rs = pstmt.executeQuery();
			if(rs.next()) tot = rs.getInt(1);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if( rs   != null) 	  rs.close();
			if( pstmt!= null ) pstmt.close();
			if( conn != null )  conn.close();
		}
		return tot;
	}
	
	// 회원 커뮤니티글 수
	public int getCommuCnt() throws SQLException {
		int tot = 0;
		Connection conn = null;
		Statement  stmt = null;
		ResultSet    rs = null;
		String      sql = "select count(*) from community";
		System.out.println("Dao getQnaCnt sql->"+sql);
		
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			rs   = stmt.executeQuery(sql);
			if(rs.next()) tot = rs.getInt(1);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if(rs   != null)    rs.close();
			if(stmt != null)  stmt.close();
			if(conn != null)  conn.close();
		}
		return tot;
	}
	
	// 회원 리스트
	public List<Member> memList(int startRow, int endRow) throws SQLException {
		List<Member> list = new ArrayList<Member>();
		Connection   	   conn = null;
		PreparedStatement pstmt = null;
		ResultSet 		     rs = null;
		String sql = "select * "
				+ " 	from  ( select rownum rn, a.*, board_count(a.user_id) board_count , comment_count(a.user_id) comment_count from (select * from member order by user_gubun) a)"
				+ " where rn between ? and ?";
		System.out.println("Dao memList sql->"+sql);

		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);;
			System.out.println("startRow->"+startRow);
			System.out.println("endRow  ->"+endRow);
			rs = pstmt.executeQuery();
				while(rs.next()) {
					Member m = new Member();
					m.setUser_id(rs.getString("user_id"));
					m.setUser_pw(rs.getString("user_pw"));
					m.setUser_email(rs.getString("user_email"));
					m.setUser_name(rs.getString("user_name"));
					m.setUser_info(rs.getString("user_info"));
					m.setUser_birth(rs.getString("user_birth"));
					m.setUser_gender(rs.getString("user_gender"));
					m.setUser_tel(rs.getString("user_tel"));
					m.setUser_gubun(rs.getInt("user_gubun"));
					m.setUser_img(rs.getString("user_img"));
					m.setBoard_count(rs.getInt("board_count"));
					m.setComment_count(rs.getInt("comment_count"));
					list.add(m);	
				}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if( rs   != null) 	  rs.close();
			if( pstmt!= null ) pstmt.close();
			if( conn != null )  conn.close();
		}
		return list;
	}
	
	// 회원 아이디에 따른 정보
	public Member select(String user_id) throws SQLException {
		Member m = new Member();
		Connection 		   conn = null;
		PreparedStatement pstmt = null;
		ResultSet 			 rs = null;
		String sql = "select * from member where user_id=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user_id);
			rs    = pstmt.executeQuery();
			if(rs.next()) {
				m.setUser_id(rs.getString("user_id"));
				m.setUser_pw(rs.getString("user_pw"));
				m.setUser_email(rs.getString("user_email"));
				m.setUser_name(rs.getString("user_name"));
				m.setUser_info(rs.getString("user_info"));
				m.setUser_birth(rs.getString("user_birth"));
				m.setUser_gender(rs.getString("user_gender"));
				m.setUser_tel(rs.getString("user_tel"));
				m.setUser_gubun(rs.getInt("user_gubun"));
				m.setUser_img(rs.getString("user_img"));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if( rs   != null) 	  rs.close();
			if( pstmt!= null ) pstmt.close();
			if( conn != null )  conn.close();
		}
		return m;
	}
	
	//회원리스트
	public List<Member> list() throws SQLException {
		List<Member> list = new ArrayList<Member>();
		Connection  	   conn = null;
		PreparedStatement pstmt = null;
		ResultSet			 rs = null;
		String sql = "select * from member";
		
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			rs	  = pstmt.executeQuery();
			if(rs.next()) {
				do {
					Member m = new Member();
					m.setUser_id(rs.getString("user_id"));
					m.setUser_pw(rs.getString("user_pw"));
					m.setUser_email(rs.getString("user_email"));
					m.setUser_name(rs.getString("user_name"));
					m.setUser_info(rs.getString("user_info"));
					m.setUser_birth(rs.getString("user_birth"));
					m.setUser_gender(rs.getString("user_gender"));
					m.setUser_tel(rs.getString("user_tel"));
					m.setUser_gubun(rs.getInt("user_gubun"));
					m.setUser_img(rs.getString("user_img"));
					list.add(m);
				} while (rs.next());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if( rs   != null) 	  rs.close();
			if( pstmt!= null ) pstmt.close();
			if( conn != null )  conn.close();
		}
		return list;
	}
	
	//회원 정보 수정
	public int update(Member member) throws SQLException {
		int result = 0;
		Connection 		   conn = null;
		PreparedStatement pstmt = null;
		String sql = "update member set user_gubun=? where user_id=?";
		
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, member.getUser_gubun());
			pstmt.setString(2, member.getUser_id());
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if(pstmt!= null) pstmt.close();
			if(conn != null)  conn.close();
		}
		return result;
	}
	
	//동행자 찾기 조회
	public List<Travel> travelList(int startRow, int endRow) throws SQLException {
		List<Travel> list = new ArrayList<Travel>();
		Connection 		   conn = null;
		PreparedStatement pstmt = null;
		ResultSet 			 rs = null;
		String sql = "select * "
					+ "		from (select rownum rn, a.*, reply_cnt(a.t_ref) reply_cnt from (select * from travel_board order by t_ref desc, t_restep) a) "
					+ "where rn between ? and ?";
		System.out.println("Dao travelList sql->"+sql);
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			rs 	  = pstmt.executeQuery();
			while(rs.next()) {
				Travel t = new Travel();
				t.setT_num(rs.getInt("t_num"));
				t.setUser_id(rs.getString("user_id"));
				t.setT_img(rs.getString("t_img"));
				t.setT_title(rs.getString("t_title"));
				t.setT_content(rs.getString("t_content"));
				t.setT_gubun(rs.getString("t_gubun"));
				t.setT_date(rs.getString("t_date"));
				t.setT_person(rs.getInt("t_person"));
				t.setT_start(rs.getString("t_start"));
				t.setT_end(rs.getString("t_end"));
				t.setT_dealstatus(rs.getString("t_dealstatus"));
				t.setT_ref(rs.getInt("t_ref"));
				t.setT_relevel(rs.getInt("t_relevel"));
				t.setT_restep(rs.getInt("t_restep"));
				t.setReply_cnt(rs.getInt("reply_cnt"));
				list.add(t);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if( rs   != null) 	  rs.close();
			if( pstmt!= null ) pstmt.close();
			if( conn != null )  conn.close();
		}
		return list;
	}
	
	
	// QnA 게시판 조회
	public List<Qna_Board> qnaList(int startRow, int endRow) throws SQLException {
		List<Qna_Board> list = new ArrayList<Qna_Board>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from \r\n"
				+ "    (select rownum rn, a.* from (select b.b_num, b.user_id, b.b_date, b.b_title, b.b_content, b.b_success, b.b_theme, h.l_hash1, h.l_hash2, h.l_hash3\r\n"
				+ "    from qna_board b, qna_hash h\r\n"
				+ "    where b.b_num = h.b_num\r\n"
				+ "    order by b.b_date desc) a) \r\n"
				+ "where rn between ? and ?";
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			System.out.println("startRow->"+startRow);
			System.out.println("endRow->"+endRow);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Qna_Board qb = new Qna_Board();
				qb.setB_num(rs.getInt("b_num"));
				qb.setUser_id(rs.getString("user_id"));
				qb.setB_date(rs.getDate("b_date"));
				qb.setB_title(rs.getString("b_title"));
				qb.setB_content(rs.getString("b_content"));
				qb.setB_success(rs.getString("b_success"));
				qb.setB_theme(rs.getString("b_theme"));
				qb.setL_hash1(rs.getString("l_hash1"));
				qb.setL_hash2(rs.getString("l_hash2"));
				qb.setL_hash3(rs.getString("l_hash3"));
				list.add(qb);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if(rs!=null) 	rs.close();
			if(pstmt!=null) pstmt.close();
			if(conn!=null)  conn.close();
		}
		return list;
	}
	
	// QnA 게시판 댓글 조회
	public List<Qna_Comment> qnaComList(int b_num) throws SQLException {
		List<Qna_Comment> list = new ArrayList<Qna_Comment>();
		Connection 		   conn = null;
		PreparedStatement pstmt = null;
		ResultSet 			 rs = null;
		String sql = "select * from qna_comment\r\n"
				   + "where b_num = ? \r\n"
				   + "order by com_num desc";
		
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, b_num);
			rs 	  = pstmt.executeQuery();
			while(rs.next()) {
				Qna_Comment qc = new Qna_Comment();
				qc.setB_num(rs.getInt("b_num"));
				System.out.println("b_num->"+b_num);
	            qc.setCom_num(rs.getInt("com_num"));
	            qc.setUser_id(rs.getString("user_id"));
	            qc.setCom_date(rs.getDate("com_date"));
	            qc.setCom_content(rs.getString("com_content"));
	            qc.setCom_choose(rs.getString("com_choose"));
	            System.out.println("qc->"+qc);
				list.add(qc);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if( rs   != null) 	  rs.close();
			if( pstmt!= null ) pstmt.close();
			if( conn != null )  conn.close();
		}
		return list;
	}
	
	//회원 커뮤니티
	public List<Commu> commuList(int startRow, int endRow) throws SQLException {
		List<Commu> list = new ArrayList<Commu>();
		Connection 		   conn = null;
		PreparedStatement pstmt = null;
		ResultSet			 rs = null;
		String sql = "select * "
				+ "		from (select rownum rn, a.* from (select * from community order by c_date desc) a) "
				+ " where rn between ? and ?";
		
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			System.out.println("startRow->"+startRow);
			System.out.println("endRow->"+endRow);
			rs 	  = pstmt.executeQuery();
			while(rs.next()) {
				Commu com = new Commu();
				com.setC_num(rs.getInt("c_num"));
				com.setUser_id(rs.getString("user_id"));
				com.setC_content(rs.getString("c_content"));
				com.setC_date(rs.getDate("c_date"));
				com.setC_hash(rs.getString("c_hash"));
				list.add(com);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if( rs   != null) 	  rs.close();
			if( pstmt!= null ) pstmt.close();
			if( conn != null )  conn.close();
		}
		return list;
	}
	
	// 동행자 게시글 삭제
	public int travelDelete(int t_num) throws SQLException {
		int result = 0;
		Connection 		   conn = null;
		PreparedStatement pstmt = null;
		String sql = "delete from travel_board where t_ref=?";
		
		try {
			conn   = getConnection();
			pstmt  = conn.prepareStatement(sql);
			pstmt.setInt(1, t_num);
			result = pstmt.executeUpdate();
			if(result > 0) result = 1;
			else result = 0;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if( pstmt!= null) pstmt.close();
			if( conn != null)  conn.close();
		}
		return result;
	}
	
	// 동행자 댓글 수정
	public int travelReplyDelete(int t_num) throws SQLException {
		int result = 0;
		Connection 		   conn = null;
		PreparedStatement pstmt = null;
		String sql = "update travel_board set t_content='관리자 의해 삭제된 댓글입니다.' where t_num=?";
		
		try {
			conn   = getConnection();
			pstmt  = conn.prepareStatement(sql);
			pstmt.setInt(1, t_num);
			result = pstmt.executeUpdate();
			if(result > 0) result = 1;
			else result = 0;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if( pstmt!= null) pstmt.close();
			if( conn != null)  conn.close();
		}
		return result;
	}
		
	//회원 커뮤니티 게시글 삭제
	public int commuDelete(int c_num) throws SQLException {
		int result = 0;
		Connection 		   conn = null;
		PreparedStatement pstmt = null;
		String sql = "delete from community where c_num=?";
		
		try {
			conn   = getConnection();
			pstmt  = conn.prepareStatement(sql);
			pstmt.setInt(1, c_num);
			result = pstmt.executeUpdate();
			if(result > 0) result = 1;
			else result = 0;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if( pstmt!= null) pstmt.close();
			if( conn != null)  conn.close();
		}
		return result;
	}
	
	// Qna 게시글 삭제
	public int qnaDelete(int b_num) throws SQLException {
		int result = 0;
		Connection 		   conn = null;
		PreparedStatement pstmt = null;
		String sql = "delete from qna_board where b_num=?";
		
		try {
			conn   = getConnection();
			pstmt  = conn.prepareStatement(sql);
			pstmt.setInt(1, b_num);
			result = pstmt.executeUpdate();
			if(result > 0) result = 1;
			else result = 0;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if( pstmt!= null) pstmt.close();
			if( conn != null)  conn.close();
		}
		return result;
	}
	
	// qna 댓글 삭제
	public int qnaComDelete(int b_num, int com_num) throws SQLException {
		int result = 0;
		Connection 		   conn = null;
		PreparedStatement pstmt = null;
		String sql = "delete from qna_comment where b_num=? and com_num=?";
		
		try {
			conn   = getConnection();
			pstmt  = conn.prepareStatement(sql);
			pstmt.setInt(1, b_num);
			pstmt.setInt(2, com_num);
			result = pstmt.executeUpdate();
			if(result > 0) result = 1;
			else result = 0;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if( pstmt!= null) pstmt.close();
			if( conn != null)  conn.close();
		}
		return result;
	}
	
	// 회원 정보 검색
	public List<Member> memSelect(String keyField, String keyWord) throws SQLException {
		List<Member> list = new ArrayList<Member>();
		Connection 		   conn = null;
		PreparedStatement pstmt = null;
		ResultSet 			 rs = null;
		
		String sql = "select * from member";
		
		// 검색 공백 제거
		if(keyWord != null && !keyWord.equals("")) {
			sql += " Where "+keyField.trim()+" Like '%"+keyWord.trim()+"%' order by user_id";		
		} else {
			sql += " order by user_id ";
		}
		System.out.println("Dao memSelect sql->"+sql);
		
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			rs    = pstmt.executeQuery();
			
			while(rs.next()) {
				Member m = new Member();
				m.setUser_id(rs.getString("user_id"));
				m.setUser_pw(rs.getString("user_pw"));
				m.setUser_email(rs.getString("user_email"));
				m.setUser_name(rs.getString("user_name"));
				m.setUser_info(rs.getString("user_info"));
				m.setUser_birth(rs.getString("user_birth"));
				m.setUser_gender(rs.getString("user_gender"));
				m.setUser_tel(rs.getString("user_tel"));
				m.setUser_gubun(rs.getInt("user_gubun"));
				m.setUser_img(rs.getString("user_img"));
				list.add(m);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if( rs   != null)	 rs.close();
			if( pstmt!= null) pstmt.close();
			if( conn != null)  conn.close();
		}
		return list;
	}
	
}

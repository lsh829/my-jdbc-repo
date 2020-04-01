package db.member2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class MemberDAOImpl implements MemberDAO {
	private Connection conn=DBConn.getConnection();
	
	@Override
	public int insertMember(MemberDTO dto) {
		int result=0;
		PreparedStatement pstmt = null;
		String sql;
		String sql2;
		
		// member1 테이블과 member2 테이블에 데이터 추가
		try {
			sql = "INSERT INTO member1(id,pwd,name) VALUES(?,?,?)";
			pstmt = conn.prepareStatement(sql);
			// IN 파라미처 값 설정
			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getPwd());
			pstmt.setString(3, dto.getName());
			pstmt.executeUpdate();
			pstmt.close();
			
			
			sql2 = "INSERT INTO member2(id,birth,email,tel)  VALUES(?,?,?,?)";
			// IN 파라미처 값 설정
			pstmt = conn.prepareStatement(sql2);
			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getBirth());
			pstmt.setString(3, dto.getEmail());
			pstmt.setString(4, dto.getTel());
			pstmt.executeUpdate();
			
			result=1;

			/*
			sql = "INSERT ALL ";
			sql += " INTO member1(id,pwd,name) VALUES(?,?,?) ";
			sql += " INTO member2(id,birth,email,tel) VALUES(?,?,?,?) ";
			sql += " SELECT * FROM dual";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getPwd());
			pstmt.setString(3, dto.getName());
			pstmt.setString(4, dto.getId());
			pstmt.setString(5, dto.getBirth());
			pstmt.setString(6, dto.getEmail());
			pstmt.setString(7, dto.getTel());
			result = pstmt.executeUpdate();
			 */

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		} 
		
		return result;
	}
	
	@Override
	public int updateMember(MemberDTO dto) {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		// id 조건에 맞는 member1 테이블과 member2 테이블에 데이터 수정
		try {
			sql = " UPDATE member1 SET pwd=? WHERE id=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getPwd());
			pstmt.setString(2, dto.getId());
			pstmt.executeUpdate();
				// ***** 절대로 executeUpdate() 안에 쿼리 삽입하면 안된다.*****
				// Statement 와의 차이점 ~
			
			pstmt.close();
			
			
			sql = " UPDATE member2 SET birth=?, email =?, tel=? WHERE id=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getBirth());
			pstmt.setString(2, dto.getEmail());
			pstmt.setString(3, dto.getTel());
			pstmt.setString(4, dto.getId());
			pstmt.executeUpdate();
				// ***** 절대로 executeUpdate() 안에 쿼리 삽입하면 안된다.*****
				// Statement 와의 차이점 ~
			
			
			result=1;
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		} 
		
		return result;
	}
	
	@Override
	public int deleteMember(String id) {
		PreparedStatement pstmt = null;
		String sql ;
		int result = 0;
		
		// id 조건에 맞는 member1 테이블과 member2 테이블에 데이터 삭제
		try {

			
			sql="DELETE FROM member2 WHERE id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.executeUpdate();
			pstmt.close();
			
			
			sql="DELETE FROM member1 WHERE id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.executeUpdate();
		
			result =1; //성공여부를 판단하기 위해 넘기는 result 값 
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		} 
		
		return result;
	}
	
	@Override
	public MemberDTO readMember(String id) {
		MemberDTO dto=null;
		PreparedStatement pstmt = null;
		ResultSet rs=null;
		
		StringBuilder sb = new StringBuilder();
		
		// id 조건에 맞는 member1 테이블과 member2 테이블에 데이터를 OUTER JOIN 해서 검색
		try {
			sb.append("SELECT m1.id, pwd, name");
			sb.append(" ,TO_CHAR(birth, 'YYYY-MM-DD') birth, email, tel ");
			sb.append("  FROM member1 m1 ");
			sb.append("  LEFT OUTER JOIN member2 m2 ON m1.id = m2.id");
			sb.append("  WHERE m1.id= ?");
            
            pstmt = conn.prepareStatement(sb.toString());
            pstmt.setString(1, id); //id값을 세팅해줘야한다. 
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new MemberDTO();
				dto.setId(rs.getString("id"));
				dto.setPwd(rs.getString("pwd"));
				dto.setName(rs.getString("name"));
				dto.setBirth(rs.getString("birth"));
				//dto.setBirth(rs.getDate("birth").toString());
				//  오라클 DATE형은 자바에서 java.sql.Date로 반환 받는다.
				//   java.sql.Date는 yyyy-mm-dd 형식으로 특화되어 있다.
				dto.setEmail(rs.getString("email"));
				dto.setTel(rs.getString("tel"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		} 
		
		return dto;
	}

	@Override
	public List<MemberDTO> listMember() {
		List<MemberDTO> list=new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs=null;
		StringBuilder sb = new StringBuilder();

		// member1 테이블과 member2 테이블의 전체 데이터를 OUTER JOIN 해서 전체 리스트
		try {
			sb.append("SELECT m1.id, pwd, name");
			sb.append(" ,TO_CHAR(birth, 'YYYY-MM-DD') birth, email, tel ");
			sb.append("  FROM member1 m1 ");
			sb.append("  LEFT OUTER JOIN member2 m2 ON m1.id = m2.id");
            
			pstmt = conn.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			
			
			while(rs.next()) {
				MemberDTO dto = new MemberDTO();
				dto.setId(rs.getString("id"));
				dto.setPwd(rs.getString("pwd"));
				dto.setName(rs.getString("name"));
				dto.setBirth(rs.getString("birth"));
				dto.setEmail(rs.getString("email"));
				dto.setTel(rs.getString("tel"));
				
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		} 
		
		return list;
	}
	
	@Override
	public List<MemberDTO> listMember(String val) {
		List<MemberDTO> list=new ArrayList<>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuilder sb = new StringBuilder();
		
		// member1 테이블과 member2 테이블의 전체 데이터를 OUTER JOIN 해서 검색
		try {
			sb.append("SELECT m1.id, pwd, name");
			sb.append(" ,TO_CHAR(birth, 'YYYY-MM-DD') birth, email, tel ");
			sb.append("  FROM member1 m1 ");
			sb.append("  LEFT OUTER JOIN member2 m2 ON m1.id = m2.id");
			// sb.append("  WHERE name LIKE '%' || '"+val+"' || '%' ");			
			sb.append("  WHERE INSTR(name, ? )>=1");
            
            pstmt = conn.prepareStatement(sb.toString());
            pstmt.setString(1, val);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				MemberDTO dto = new MemberDTO();
				dto.setId(rs.getString("id"));
				dto.setPwd(rs.getString("pwd"));
				dto.setName(rs.getString("name"));
				dto.setBirth(rs.getString("birth"));
				dto.setEmail(rs.getString("email"));
				dto.setTel(rs.getString("tel"));
				
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		} 
		
		return list;
	}
}

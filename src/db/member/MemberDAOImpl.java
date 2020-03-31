package db.member;

import java.sql.Connection;
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
		Statement stmt=null;
		StringBuilder sb;
		
		// member1 테이블과 member2 테이블에 데이터 추가
		try {
			stmt = conn.createStatement();
			
			sb=new StringBuilder();
			sb.append("INSERT INTO member1(id,pwd,name) VALUES(");
			sb.append("'"+dto.getId()+"'");
			sb.append(",'"+dto.getPwd()+"'");
			sb.append(",'"+dto.getName()+"'");
			sb.append(")");
			stmt.executeUpdate(sb.toString());
			
			sb=new StringBuilder();
			sb.append("INSERT INTO member2(id,birth,email,tel) VALUES(");
			sb.append("'"+dto.getId()+"'");
			sb.append(",'"+dto.getBirth()+"'");
			sb.append(",'"+dto.getEmail()+"'");
			sb.append(",'"+dto.getTel()+"'");
			sb.append(")");
			stmt.executeUpdate(sb.toString());
			
			result=1; // 성공하면 1을 반환 

/*
			sb=new StringBuilder();
			sb.append("INSERT ALL ");
			sb.append(" INTO member1(id,pwd,name) VALUES(");
			sb.append(" '"+dto.getId()+"'");
			sb.append(" ,'"+dto.getPwd()+"'");
			sb.append(" ,'"+dto.getName()+"'");
			sb.append(" )");
			sb.append(" INTO member2(id,birth,email,tel) VALUES(");
			sb.append(" '"+dto.getId()+"'");
			sb.append(" ,'"+dto.getBirth()+"'");
			sb.append(" ,'"+dto.getEmail()+"'");
			sb.append(" ,'"+dto.getTel()+"'");
			sb.append(" )");
			sb.append(" SELECT * FROM dual");
			
			result=stmt.executeUpdate(sb.toString()); 
				-- result 값은 0도 아니고 1도 아닙니다. 
				-- 성공하면 result값은 2가 된다. 
				-- 왜냐하면 2개의 테이블에 들어갔기 때문이다. 
 */
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(stmt!=null) {
				try {
					stmt.close();
				} catch (Exception e2) {
				}
			}
		} 
		
		return result;
	}
	
	@Override
	public int updateMember(MemberDTO dto) {
		int result=0;
		Statement stmt=null;
		StringBuilder sb;
		
		// id 조건에 맞는 member1 테이블과 member2 테이블에 데이터 수정
		try {
			stmt = conn.createStatement();
			
			sb=new StringBuilder();
			sb.append("UPDATE member1 SET pwd='"+dto.getPwd()+"'");
			sb.append("  WHERE id='"+dto.getId()+"'");
			result=stmt.executeUpdate(sb.toString());
			
			sb=new StringBuilder();
			sb.append("UPDATE member2 SET birth='"+dto.getBirth()+"'");
			sb.append(", email='"+dto.getEmail()+"'");
			sb.append(", tel='"+dto.getTel()+"'");
			sb.append("  WHERE id='"+dto.getId()+"'");
			stmt.executeUpdate(sb.toString());
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(stmt!=null) {
				try {
					stmt.close();
				} catch (Exception e2) {
				}
			}
		} 
		
		return result;
	}
	
	@Override
	public int deleteMember(String id) {
		int result=0;
		Statement stmt=null;
		String sql;
		
		// id 조건에 맞는 member1 테이블과 member2 테이블에 데이터 삭제
		try {
			stmt = conn.createStatement();
			
			sql="DELETE FROM member2 WHERE id='"+id+"'";
			stmt.executeUpdate(sql);
			
			sql="DELETE FROM member1 WHERE id='"+id+"'";
			result=stmt.executeUpdate(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(stmt!=null) {
				try {
					stmt.close();
				} catch (Exception e2) {
				}
			}
		} 
		
		return result;
	}
	
	@Override
	public MemberDTO readMember(String id) {
		MemberDTO dto=null;
		Statement stmt=null;
		ResultSet rs=null;
		StringBuilder sb = new StringBuilder();
		
		// id 조건에 맞는 member1 테이블과 member2 테이블에 데이터를 OUTER JOIN 해서 검색
		try {
			sb.append("SELECT m1.id, pwd, name");
			sb.append(" ,TO_CHAR(birth, 'YYYY-MM-DD') birth, email, tel ");
			sb.append("  FROM member1 m1 ");
			sb.append("  LEFT OUTER JOIN member2 m2 ON m1.id = m2.id");
			sb.append("  WHERE m1.id='"+id+"'");
            
            stmt = conn.createStatement();
			rs = stmt.executeQuery(sb.toString());
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
			if(stmt!=null) {
				try {
					stmt.close();
				} catch (Exception e2) {
				}
			}
		} 
		
		return dto;
	}

	@Override
	public List<MemberDTO> listMember() {
		List<MemberDTO> list=new ArrayList<>();
		Statement stmt=null;
		ResultSet rs=null;
		StringBuilder sb = new StringBuilder();

		// member1 테이블과 member2 테이블의 전체 데이터를 OUTER JOIN 해서 전체 리스트
		try {
			sb.append("SELECT m1.id, pwd, name");
			sb.append(" ,TO_CHAR(birth, 'YYYY-MM-DD') birth, email, tel ");
			sb.append("  FROM member1 m1 ");
			sb.append("  LEFT OUTER JOIN member2 m2 ON m1.id = m2.id");
            
            stmt = conn.createStatement();
			rs = stmt.executeQuery(sb.toString());
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
			if(stmt!=null) {
				try {
					stmt.close();
				} catch (Exception e2) {
				}
			}
		} 
		
		return list;
	}
	
	@Override
	public List<MemberDTO> listMember(String val) {
		List<MemberDTO> list=new ArrayList<>();
		Statement stmt=null;
		ResultSet rs=null;
		StringBuilder sb = new StringBuilder();
		
		// member1 테이블과 member2 테이블의 전체 데이터를 OUTER JOIN 해서 검색
		try {
			sb.append("SELECT m1.id, pwd, name");
			sb.append(" ,TO_CHAR(birth, 'YYYY-MM-DD') birth, email, tel ");
			sb.append("  FROM member1 m1 ");
			sb.append("  LEFT OUTER JOIN member2 m2 ON m1.id = m2.id");
			// sb.append("  WHERE name LIKE '%' || '"+val+"' || '%' ");			
			sb.append("  WHERE INSTR(name, '"+val+"')>=1");
            
            stmt = conn.createStatement();
			rs = stmt.executeQuery(sb.toString());
			
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
			if(stmt!=null) {
				try {
					stmt.close();
				} catch (Exception e2) {
				}
			}
		} 
		
		return list;
	}
}

package db.member1;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class MemberDAOImpl implements MemberDAO {
	private Connection conn = DBConn.getConnection();

	@Override
	public int insertMember(MemberDTO dto) {
		int result = 0;
		Statement stmt = null;
		StringBuilder sb = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();

		try {
			sb.append("INSERT INTO member1(");
			sb.append("id,pwd,name) VALUES(");
			sb.append("'" + dto.getId() + "',");
			sb.append("'" + dto.getPwd() + "',");
			sb.append("'" + dto.getName() + "')"); // 생일은 문자 처리

			stmt = conn.createStatement();
			result = stmt.executeUpdate(sb.toString());

			sb2.append("INSERT INTO member2(");
			sb2.append("id,birth,email,tel) VALUES(");
			sb2.append("'" + dto.getId() + "',");
			sb2.append("'" + dto.getBirth() + "',");
			sb2.append("'" + dto.getEmail() + "',");
			sb2.append("'" + dto.getTel() + "')");

			result = stmt.executeUpdate(sb2.toString());
			
// 쿼리 문에 집중한 또 다른 회원등록 방법 
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
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}

		}
		return result;
	}

	@Override
	public int updateMember(MemberDTO dto) {
		int result = 0;
		StringBuilder sb = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		Statement stmt = null;
		

		sb.append(" UPDATE member1 SET ");
		sb.append(" pwd = '" + dto.getPwd() + "', "); // 생일은 문자 처리
		sb.append(" name = '" + dto.getName() + "' ");
		sb.append(" WHERE id = '" + dto.getId() + "' ");

		sb2.append(" UPDATE member2 SET ");
		sb2.append(" birth = '" + dto.getBirth() + "', "); // 생일은 문자 처리
		sb2.append(" email = '" + dto.getEmail() + "', ");
		sb2.append(" tel = '" + dto.getTel() + "' ");
		sb2.append(" WHERE id = '" + dto.getId() + "' ");

		try {
			stmt = conn.createStatement();
			result = stmt.executeUpdate(sb.toString());
			result = stmt.executeUpdate(sb2.toString());
			// executeUpdate(sql); => 쿼리 실행 (INSERT, UPDATE, DELETE, CREATE, ALTER, DROP 등)

		} catch (SQLException e) { // DAO -- SQLException을 적어줘야한다.
			e.printStackTrace();
		}finally {
			if(stmt!=null) {
				try {
					stmt.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}

		return result;

	}

	@Override
	public int deleteMember(String id) {
		String sql;
		String sql2;
		Statement stmt = null;

		int result = 0;

		try{
			stmt = conn.createStatement();
			sql = "DELETE FROM member2 WHERE id= '" + id + "' ";
			sql2 = "DELETE FROM member1 WHERE id= '" + id + "' ";

			result = stmt.executeUpdate(sql);
			result = stmt.executeUpdate(sql2);

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(stmt!=null) {
				try {
					stmt.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	@Override
	public MemberDTO readMember(String id) {
		MemberDTO dto = null;
		StringBuilder sb = new StringBuilder();
		Statement stmt = null;
		ResultSet rs = null;
		
		
		

		try {
			//sb.append("SELECT member1.id, name, pwd, TO_CHAR(birth, 'YYYY-MM-DD') birth, email, tel ");
			//sb.append(" FROM member1, member2 ");
			//sb.append(" WHERE member1.id= member2.id"); //JOIN checking
			//sb.append(" AND member1.id= '" + id + "'");
			
			//JOIN 방식 추가적으로 공부할 필요가 있다. 
			sb.append("SELECT m1.id, pwd, name");
			sb.append(" ,TO_CHAR(birth, 'YYYY-MM-DD') birth, email, tel ");
			sb.append("  FROM member1 m1 ");
			sb.append("  LEFT OUTER JOIN member2 m2 ON m1.id = m2.id");
			sb.append("  WHERE m1.id='"+id+"'");

			
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sb.toString());

			if (rs.next()) {
				dto = new MemberDTO();
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));
				dto.setPwd(rs.getString("pwd"));
				dto.setBirth(rs.getString("birth"));
				//dto.setBirth(rs.getDate("birth").toString());
				//  오라클 DATE형은 자바에서 java.sql.Date로 반환 받는다.
				//   java.sql.Date는 yyyy-mm-dd 형식으로 특화되어 있다.
				//	 따라서, TO_CHAR(birth, 'YYYY-MM-DD') 가 필요가 없다. 
				dto.setEmail(rs.getString("email"));
				dto.setTel(rs.getString("tel"));
			}

		} catch (Exception e) { // finally 블록으로 닫아줘야만 한다.
			e.printStackTrace();
		} finally {
			// rs 먼저 닫기!
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}

				if (stmt != null) {
					try {
						stmt.close();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}

			}
		}

		return dto;
	}

	@Override
	public List<MemberDTO> listMember() {
		List<MemberDTO> list = new ArrayList<MemberDTO>();
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			// 생년월일 yyyy-mm-dd 형식으로 출력하기
			//sb.append(" SELECT member1.id, name, pwd, TO_CHAR(birth, 'YYYY-MM-DD') birth, email, tel ");
			//sb.append("	from member1, member2 ");
			//sb.append(" where member1.id= member2.id ");
			
			// member1 테이블과 member2 테이블의 전체 데이터를 OUTER JOIN 해서 전체 리스트
			sb.append("SELECT m1.id, pwd, name");
			sb.append(" ,TO_CHAR(birth, 'YYYY-MM-DD') birth, email, tel ");
			sb.append("  FROM member1 m1 ");
			sb.append("  LEFT OUTER JOIN member2 m2 ON m1.id = m2.id");

			stmt = conn.createStatement();
			rs = stmt.executeQuery(sb.toString());

			while (rs.next()) {
				MemberDTO dto = new MemberDTO();
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));
				dto.setPwd(rs.getString("pwd"));
				dto.setBirth(rs.getString("birth"));
				dto.setEmail(rs.getString("email"));
				dto.setTel(rs.getString("tel"));

				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return list;
	}

	@Override
	public List<MemberDTO> listMember(String val) {
		List<MemberDTO> list = new ArrayList<MemberDTO>();
		StringBuilder sb = new StringBuilder();
		Statement stmt = null;
		ResultSet rs = null;

		
		// member1 테이블과 member2 테이블의 전체 데이터를 OUTER JOIN 해서 검색
		try {
			
			//sb.append(" SELECT member1.id, name, pwd, TO_CHAR(birth, 'YYYY-MM-DD') birth, email, tel ");
			//sb.append("	FROM member1, member2 ");
			//sb.append(" WHERE member1.id= member2.id");
			//sb.append(" AND name LIKE '" + val + "' || '%' ");
			
			sb.append("SELECT m1.id, pwd, name");
			sb.append(" ,TO_CHAR(birth, 'YYYY-MM-DD') birth, email, tel ");
			sb.append("  FROM member1 m1 ");
			sb.append("  LEFT OUTER JOIN member2 m2 ON m1.id = m2.id");
			// sb.append("  WHERE name LIKE '%' || '"+val+"' || '%' ");			
			sb.append("  WHERE INSTR(name, '"+val+"')>=1");
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sb.toString());

			while (rs.next()) {
				MemberDTO dto = new MemberDTO();
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));
				dto.setPwd(rs.getString("pwd"));
				dto.setBirth(rs.getString("birth"));
				dto.setEmail(rs.getString("email"));
				dto.setTel(rs.getString("tel"));

				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return list;
	}

}

package db.member3;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

import oracle.jdbc.OracleTypes;

public class MemberDAOImpl implements MemberDAO {
	private Connection conn=DBConn.getConnection();
	
	@Override
	public int insertMember(MemberDTO dto) {
		int result=0;
		CallableStatement cstmt = null;
		String sql;
		String sql2;
		
		// member1 테이블과 member2 테이블에 데이터 추가
		try {
			
			sql = "{CALL insertMember1(?,?,?)}";
			cstmt = conn.prepareCall(sql);
			// IN 파라미처 값 설정
			cstmt.setString(1, dto.getId());
			cstmt.setString(2, dto.getPwd());
			cstmt.setString(3, dto.getName());
			cstmt.executeUpdate();
			cstmt.close();
			
			
			sql2 = "{CALL insertMember2(?,?,?,?)}";
			// IN 파라미처 값 설정
			cstmt = conn.prepareCall(sql2);
			cstmt.setString(1, dto.getId());
			cstmt.setString(2, dto.getBirth());
			cstmt.setString(3, dto.getEmail());
			cstmt.setString(4, dto.getTel());
			cstmt.executeUpdate();
			
			result=1;
			
			
			// 프로시져 insertMember3으로 짤 수 있습니다. 
			
			//sql = "{CALL insertMember3(?,?,?,?,?,?)}";
			//cstmt = conn.prepareCall(sql);
			//cstmt.setString(1, dto.getId());
			//cstmt.setString(2, dto.getPwd());
			//cstmt.setString(3, dto.getName());
			//cstmt.setString(4, dto.getBirth());
			//cstmt.setString(5, dto.getEmail());
			//cstmt.setString(6, dto.getTel());
			//cstmt.executeUpdate();
			
			//result=1;


		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(cstmt!=null) {
				try {
					cstmt.close();
				} catch (Exception e2) {
				}
			}
		} 
		
		return result;
	}
	
	@Override
	public int updateMember(MemberDTO dto) {
		int result=0;
		CallableStatement cstmt = null;
		String sql;
		
		// id 조건에 맞는 member1 테이블과 member2 테이블에 데이터 수정
		try {
			
			sql = "{CALL updateMember1(?,?)}";
			cstmt = conn.prepareCall(sql);
			
			cstmt.setString(1, dto.getId());
			cstmt.setString(2, dto.getPwd());
			
			cstmt.executeUpdate();
				// ***** 절대로 executeUpdate() 안에 쿼리 삽입하면 안된다.*****
				// Statement 와의 차이점 ~
			
			cstmt.close();
			
			
			sql = "{CALL updateMember2(?,?,?,?)}";
			cstmt = conn.prepareCall(sql);
			
			cstmt.setString(1, dto.getId());
			cstmt.setString(2, dto.getBirth());
			cstmt.setString(3, dto.getEmail());
			cstmt.setString(4, dto.getTel());
			
			cstmt.executeUpdate();
				// ***** 절대로 executeUpdate() 안에 쿼리 삽입하면 안된다.*****
				// Statement 와의 차이점 ~
			
			
			result=1;
			
			
		} catch (SQLException e) {
			e.printStackTrace(); 
			//member1에는 데이터가 있고 member2에는 데이터가 없는 경우, "등록부터해주십시요"라는 메세지 출력 필요 
		} finally {
			if(cstmt!=null) {
				try {
					cstmt.close();
				} catch (Exception e2) {
				}
			}
		} 
		
		return result;
	}
	
	@Override
	public int deleteMember(String id) {
		CallableStatement cstmt = null;
		String sql ;
		int result = 0;
		
		// id 조건에 맞는 member1 테이블과 member2 테이블에 데이터 삭제
		try {

			
			sql = "{CALL deleteMember2(?)}";
			cstmt = conn.prepareCall(sql);
			cstmt.setString(1, id);
			cstmt.executeUpdate();
			cstmt.close();
			
			
			sql = "{CALL deleteMember1(?)}";
			cstmt = conn.prepareCall(sql);
			cstmt.setString(1, id);
			cstmt.executeUpdate();

			result =1; //성공여부를 판단하기 위해 넘기는 result 값 
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(cstmt!=null) {
				try {
					cstmt.close();
				} catch (Exception e2) {
				}
			}
		} 
		
		return result;
	}
	
	@Override
	public MemberDTO readMember(String id) {
		MemberDTO dto=null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		String sql;
		
		// id 조건에 맞는 member1 테이블과 member2 테이블에 데이터를 OUTER JOIN 해서 검색
		try {
			sql = "{CALL findByIdMember(?,?)}";
			cstmt = conn.prepareCall(sql);
			cstmt.registerOutParameter(1, OracleTypes.CURSOR);
			cstmt.setString(2, id);
			cstmt.executeUpdate();
			
			rs = (ResultSet) cstmt.getObject(1);
			
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
			if(cstmt!=null) {
				try {
					cstmt.close();
				} catch (Exception e2) {
				}
			}
		} 
		
		return dto;
	}

	@Override
	public List<MemberDTO> listMember() {
		List<MemberDTO> list = new ArrayList<MemberDTO>();
		CallableStatement cstmt = null;
		ResultSet rs = null; // SELECT 문장에서만 ResultSet 이 있다.
		String sql;

		// member1 테이블과 member2 테이블의 전체 데이터를 OUTER JOIN 해서 전체 리스트
		try {
			sql = "{CALL listMember(?)}";
			cstmt = conn.prepareCall(sql);

			cstmt.registerOutParameter(1, OracleTypes.CURSOR);
			cstmt.executeUpdate();
			rs = (ResultSet) cstmt.getObject(1);
			
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
			if(cstmt!=null) {
				try {
					cstmt.close();
				} catch (Exception e2) {
				}
			}
		} 
		
		return list;
	}
	
	@Override
	public List<MemberDTO> listMember(String val) {
		List<MemberDTO> list=new ArrayList<>();
		CallableStatement  cstmt=null;
		ResultSet rs=null;
		String sql;
		
		// member1 테이블과 member2 테이블의 전체 데이터를 OUTER JOIN 해서 검색
		try {
			sql = "{CALL findByNameMember(?, ?)}";
            
			cstmt = conn.prepareCall(sql);
			cstmt.registerOutParameter(1, OracleTypes.CURSOR);
			cstmt.setString(2, val);
			cstmt.executeUpdate();
			rs = (ResultSet) cstmt.getObject(1);
			
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
			if(cstmt!=null) {
				try {
					cstmt.close();
				} catch (Exception e2) {
				}
			}
		} 
		
		return list;
	}
}

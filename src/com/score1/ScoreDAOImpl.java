package com.score1;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;


// return 0; -- 몇 개를 작업했는지 알고 싶을 경우, return이 있어야 한다. 

// ScoreDAO interface 를 구현한 ScoreDAOImpl 클래스 작성 

public class ScoreDAOImpl implements ScoreDAO{
	private Connection conn = DBConn.getConnection();

	//1)
	@Override
	public int insertScore(ScoreDTO dto) {
		int result = 0;
		Statement stmt = null; // 쿼리를 실행할 수 있는 객체  
		StringBuilder sb = new StringBuilder(); //StringBuilder -- 여러줄로 작성할 경우 써준다. 
		
		try {
			sb.append("INSERT INTO score(");
			sb.append("hak,name,birth,kor,eng,mat) VALUES(");
			sb.append("'"+dto.getHak()+"',");
			sb.append("'"+dto.getName()+"',");
			sb.append("'"+dto.getBirth()+"',"); //생일은 문자 처리 
			sb.append(dto.getKor()+",");
			sb.append(dto.getEng()+",");
			sb.append(dto.getMat()+")");
			
			stmt = conn.createStatement();
			result = stmt.executeUpdate(sb.toString());
					//executeUpdate(sql); => 쿼리 실행  (INSERT, UPDATE, DELETE, CREATE, ALTER, DROP 등) 

			
		} catch(SQLException e){ //DAO -- SQLException을 적어줘야한다. 
			e.printStackTrace();
		} finally {
			if(stmt != null) {
				try { 
					stmt.close();
				}catch (Exception e2) {
					e2.printStackTrace();
				}
			
		}
			
		}
		return result; // 몇 개를 작업했는지 알고 싶을 경우, return이 있어야 한다. 
	}
	
	
	//4) 내 힘으로 짜보기 
	@Override
	public int updateScore(ScoreDTO dto) {
		int result = 0;
		StringBuilder sb = new StringBuilder(); //StringBuilder -- 여러줄로 작성할 경우 써준다. 

		
		sb.append(" UPDATE score SET ");
		sb.append(" name= '" +dto.getName()+"',");
		sb.append(" birth = '"+dto.getBirth()+"', "); //생일은 문자 처리 
		sb.append(" kor = "+ dto.getKor()+", ");
		sb.append(" eng = "+ dto.getEng()+", ");
		sb.append(" mat =  "+ dto.getMat());
		sb.append(" WHERE hak = '"+dto.getHak()+"' ");
		
		
		try(Statement stmt = conn.createStatement()){
			result = stmt.executeUpdate(sb.toString());
					//executeUpdate(sql); => 쿼리 실행  (INSERT, UPDATE, DELETE, CREATE, ALTER, DROP 등) 
			
		} catch(SQLException e){ //DAO -- SQLException을 적어줘야한다. 
			e.printStackTrace();
		}
		return result; // 몇 개를 작업했는지 알고 싶을 경우, return이 있어야 한다.
		
		
	}

	//3) 내 힘으로 짜보기 
	@Override
	public int deleteScore(String hak) {
		String sql; 
		int result = 0;
		
		//자동 close가 된다. 
		try (Statement stmt = conn.createStatement()) {
			
			sql = "DELETE FROM score WHERE hak= '" + hak + "' "; //'' 위치 주의해서 살피기 *****
			result = stmt.executeUpdate(sql);
					//executeUpdate(sql); => 쿼리 실행  (INSERT, UPDATE, DELETE, CREATE, ALTER, DROP 등) 
			
		} catch(Exception e){ //DAO -- SQLException을 적어줘야한다. 
			e.printStackTrace();
		} 
		
		return result; // 몇 개를 작업했는지 알고 싶을 경우, return이 있어야 한다. 
	
	}
	
	//6)
	@Override
	public ScoreDTO readScore(String hak) {
		ScoreDTO dto = null;
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT hak, name, ");
		sb.append("	TO_CHAR(birth, 'YYYY-MM-DD') birth, ");
		sb.append(" kor, eng, mat, ");
		sb.append(" kor+eng+mat tot, (kor+eng+mat)/3 ave ");
		sb.append(" FROM score ");
		sb.append(" WHERE hak= '" + hak+ "'");
		
		
		try(Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sb.toString())){
			
			if(rs.next()) {
				dto = new ScoreDTO();
				dto.setHak(rs.getString("hak"));
				dto.setName(rs.getString("name"));
				dto.setBirth(rs.getString("birth"));
				dto.setKor(rs.getInt("kor"));
				dto.setEng(rs.getInt("eng"));
				dto.setMat(rs.getInt("mat"));
				dto.setTot(rs.getInt("tot"));
				dto.setAve(rs.getInt("ave"));
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	//5) 
	@Override
	public List<ScoreDTO> listScore(String name) {
		List<ScoreDTO> list = new ArrayList<ScoreDTO>(); 
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT hak, name, birth, kor, eng, mat, ");
		sb.append(" kor+eng+mat tot, (kor+eng+mat)/3 ave, ");
		sb.append(" RANK() OVER(ORDER BY kor+eng+mat DESC) rank ");
		//sb.append(" FROM score WHERE INSTR(name, '" + name + "') = 1 ");	
		sb.append(" FROM score ");
		sb.append(" WHERE name LIKE '"+name+"' || '%' "); //'김'씨로 시작 
		//sb.append(" FROM score WHERE LIKE '%' || '"+name+"' || '%' "); //'김'이 중간으로 들어가있을 경우도 출력
		
		//쿼리 문 정렬 하는 거 작업할 시 매우 중요. 
		
		try(Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sb.toString()) ) {
			
			
			while(rs.next()) { //DTO 객체는 1인당 1개씩 만들어진다. 
				ScoreDTO dto = new ScoreDTO();
				dto.setHak(rs.getString("hak"));
				dto.setName(rs.getString("name"));
				dto.setBirth(rs.getDate("birth").toString());
				dto.setKor(rs.getInt("kor"));
				dto.setEng(rs.getInt("eng"));
				dto.setMat(rs.getInt("mat"));
				dto.setTot(rs.getInt("tot"));
				dto.setAve(rs.getInt("ave"));
				dto.setRank(rs.getInt("rank"));
				
				list.add(dto); //1인의 정보를 차곡차곡 ArrayList에 저장 
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	
	//2)
	//DB속 데이터 다 가져오기 
	//여기에서만 석차를 구해야 한다. - 데이터 전체가 있어야 비교가능하기 때문이다.
	@Override
	public List<ScoreDTO> listScore() {
		List<ScoreDTO> list = new ArrayList<ScoreDTO>(); 
		Statement stmt = null;
		ResultSet rs = null; //SELECT 문장에서만 ResultSet 이 있다. 
		StringBuilder sb = new StringBuilder();
		
		try {
			//띄어쓰기 주의해서 필수로 하세요.
			sb.append("SELECT hak, name, birth, kor, eng, mat, ");
			sb.append(" kor+eng+mat tot, (kor+eng+mat)/3 ave, ");
			sb.append(" RANK() OVER(ORDER BY kor+eng+mat DESC) rank ");
			sb.append(" FROM score ");
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sb.toString());
				// executeQuery(sql) : SELECT 문 실행 
			
			while(rs.next()) { //DTO 객체는 1인당 1개씩 만들어진다. 
				ScoreDTO dto = new ScoreDTO();
				dto.setHak(rs.getString("hak"));
				dto.setName(rs.getString("name"));
				dto.setBirth(rs.getString("birth").toString());
				dto.setKor(rs.getInt("kor"));
				dto.setEng(rs.getInt("eng"));
				dto.setMat(rs.getInt("mat"));
				dto.setTot(rs.getInt("tot"));
				dto.setAve(rs.getInt("ave"));
				dto.setRank(rs.getInt("rank"));
				
				list.add(dto); //1인의 정보를 차곡차곡 ArrayList에 저장 
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			
			if(stmt!=null) {
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

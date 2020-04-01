package com.score2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

// return 0; -- 몇 개를 작업했는지 알고 싶을 경우, return이 있어야 한다. 

// ScoreDAO interface 를 구현한 ScoreDAOImpl 클래스 작성 

//PreparedStatement 로 수정한 소스 코드 

public class ScoreDAOImpl implements ScoreDAO {
	private Connection conn = DBConn.getConnection();

	// 1)
	@Override
	public int insertScore(ScoreDTO dto) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "INSERT INTO score(hak, name, birth, kor, eng, mat) VALUES(?,?,?,?,?,?)";
			// 가변적인 데이터를 처리하는 부분은 ? 로 처리하면 된다.
			pstmt = conn.prepareStatement(sql);

			// IN 파라미처 값 설정
			pstmt.setString(1, dto.getHak());
			pstmt.setString(2, dto.getName());
			pstmt.setString(3, dto.getBirth());
			pstmt.setInt(4, dto.getKor());
			pstmt.setInt(5, dto.getEng());
			pstmt.setInt(6, dto.getMat());

			// 쿼리 실행
			result = pstmt.executeUpdate();
			// ***** 절대로 executeUpdate() 안에 쿼리 삽입하면 안된다.*****
			// Statement 와의 차이점 ~

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return result; // 몇 개를 작업했는지 알고 싶을 경우, return이 있어야 한다.
	}

	// 2)
	@Override
	public int updateScore(ScoreDTO dto) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		// 권장하는 try-catch
		try {
			sql = "UPDATE score SET name=?, birth=?, kor=?, eng=?, mat=? WHERE hak=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getName());
			pstmt.setString(2, dto.getBirth());
			pstmt.setInt(3, dto.getKor());
			pstmt.setInt(4, dto.getEng());
			pstmt.setInt(5, dto.getMat());
			pstmt.setString(6, dto.getHak());

			// 쿼리 실행
			result = pstmt.executeUpdate();
				// ***** 절대로 executeUpdate() 안에 쿼리 삽입하면 안된다.*****
				// Statement 와의 차이점 ~

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}

		return result; // 몇 개를 작업했는지 알고 싶을 경우, return이 있어야 한다.

	}

	// 3)
	@Override
	public int deleteScore(String hak) {
		PreparedStatement pstmt = null;
		String sql ;
		int result = 0;
		
		try {
			//쿼리 작성 
			sql = "DELETE FROM score WHERE hak=? "; 
			//객체 생성
			pstmt = conn.prepareStatement(sql);
			//쿼리 값 부여
			pstmt.setString(1, hak);
			//쿼리 실행
			result = pstmt.executeUpdate();
				// ***** 절대로 executeUpdate() 안에 쿼리 삽입하면 안된다.*****
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		

		return result; // 몇 개를 작업했는지 알고 싶을 경우, return이 있어야 한다.

	}

	// 4)
	@Override
	public ScoreDTO readScore(String hak) {
		ScoreDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT hak, name, ");
		sb.append("	TO_CHAR(birth, 'YYYY-MM-DD') birth, ");
		sb.append(" kor, eng, mat, ");
		sb.append(" kor+eng+mat tot, (kor+eng+mat)/3 ave ");
		sb.append(" FROM score ");
		sb.append(" WHERE hak= ?");

		try {
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, hak);
			rs = pstmt.executeQuery();

			if (rs.next()) {
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

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return dto;
	}

	// 5)
	@Override
	public List<ScoreDTO> listScore(String name) {
		List<ScoreDTO> list = new ArrayList<ScoreDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT hak, name, birth, kor, eng, mat, ");
		sb.append(" kor+eng+mat tot, (kor+eng+mat)/3 ave, ");
		sb.append(" RANK() OVER(ORDER BY kor+eng+mat DESC) rank ");
		// sb.append(" FROM score WHERE INSTR(name, ?) = 1 ");
		sb.append(" FROM score ");
		sb.append(" WHERE name LIKE '%' || ? || '%' "); // '김'씨로 시작
		//sb.append(" WHERE name LIKE ? || '%' "); // '김'씨로 시작
		
		// 쿼리 문 정렬 하는 거 작업할 시 매우 중요.

		try {
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			
			while (rs.next()) { // DTO 객체는 1인당 1개씩 만들어진다.
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

				list.add(dto); // 1인의 정보를 차곡차곡 ArrayList에 저장
			}

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}

		return list;
	}

	// 6)
	// DB속 데이터 다 가져오기
	// 여기에서만 석차를 구해야 한다. - 데이터 전체가 있어야 비교가능하기 때문이다.
	@Override
	public List<ScoreDTO> listScore() {
		List<ScoreDTO> list = new ArrayList<ScoreDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null; // SELECT 문장에서만 ResultSet 이 있다.
		StringBuilder sb = new StringBuilder();

		try {
			// 띄어쓰기 주의해서 필수로 하세요.
			sb.append("SELECT hak, name, birth, kor, eng, mat, ");
			sb.append(" kor+eng+mat tot, (kor+eng+mat)/3 ave, ");
			sb.append(" RANK() OVER(ORDER BY kor+eng+mat DESC) rank ");
			sb.append(" FROM score ");

			pstmt = conn.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			

			while (rs.next()) { // DTO 객체는 1인당 1개씩 만들어진다.
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

				list.add(dto); // 1인의 정보를 차곡차곡 ArrayList에 저장
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

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}

			}

		}
		return list;
	}

}

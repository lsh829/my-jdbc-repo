package com.score3;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.util.DBConn;

import oracle.jdbc.OracleTypes;

// return 0; -- 몇 개를 작업했는지 알고 싶을 경우, return이 있어야 한다. 

// ScoreDAO interface 를 구현한 ScoreDAOImpl 클래스 작성 

// CallableStatement 로 수정한 소스 코드 : 프로시져 실행

public class ScoreDAOImpl implements ScoreDAO {
	private Connection conn = DBConn.getConnection();

	// 1)
	@Override
	public int insertScore(ScoreDTO dto) {
		int result = 0;
		CallableStatement cstmt = null;
		String sql;

		try {
			sql = "{CALL insertScore(?,?,?,?,?,?)}";
			// 가변적인 데이터를 처리하는 부분은 ? 로 처리하면 된다.
			cstmt = conn.prepareCall(sql);

			// IN 파라미처 값 설정
			cstmt.setString(1, dto.getHak());
			cstmt.setString(2, dto.getName());
			cstmt.setString(3, dto.getBirth());
			cstmt.setInt(4, dto.getKor());
			cstmt.setInt(5, dto.getEng());
			cstmt.setInt(6, dto.getMat());

			// 프로시져 실행
			cstmt.executeUpdate();
			// CallableStatement 의 executeUpdate() 리턴값은
			// INSERT 문 등을 실행 후 실행된 행수를 반환하지 않고
			// 프로시져 실행여부를 반환한다.
			result = 1; // 무조건 1. 성공여부 판단을 위해 만든 것

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (cstmt != null) {
				try {
					cstmt.close();
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
		CallableStatement cstmt = null;
		String sql;

		// 권장하는 try-catch
		try {
			// sql = "UPDATE score SET name=?, birth=?, kor=?, eng=?, mat=? WHERE hak=?";

			sql = "{CALL updateScore(?,?,?,?,?,?)}";
			// 가변적인 데이터를 처리하는 부분은 ? 로 처리하면 된다.
			cstmt = conn.prepareCall(sql);

			cstmt.setString(1, dto.getHak());
			cstmt.setString(2, dto.getName());
			cstmt.setString(3, dto.getBirth());
			cstmt.setInt(4, dto.getKor());
			cstmt.setInt(5, dto.getEng());
			cstmt.setInt(6, dto.getMat());

			// 프로시져의 파라미터 순서를 맞춰줘야합니다.
			// pHak IN score.hak%TYPE
			// ,pName IN score.name%TYPE
			// ,pBirth IN score.birth%TYPE
			// ,pKor IN score.kor%TYPE
			// ,pEng IN score.eng%TYPE
			// ,pMat IN score.mat%TYPE

			// 프로시져 실행
			cstmt.executeUpdate();
			// CallableStatement 의 executeUpdate() 리턴값은
			// INSERT 문 등을 실행 후 실행된 행수를 반환하지 않고
			// 프로시져 실행여부를 반환한다.
			result = 1; // 무조건 1. 성공여부 판단을 위해 만든 것

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cstmt != null) {
				try {
					cstmt.close();
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
		CallableStatement cstmt = null;
		String sql;
		int result = 0;

		try {
			// 쿼리 작성
			sql = "{CALL deleteScore(?)}";
			// 객체 생성
			cstmt = conn.prepareCall(sql);
			// 쿼리 값 부여
			cstmt.setString(1, hak);
			// 프로시져 실행
			cstmt.executeUpdate();
			// CallableStatement 의 executeUpdate() 리턴값은
			// INSERT 문 등을 실행 후 실행된 행수를 반환하지 않고
			// 프로시져 실행여부를 반환한다.
			result = 1; // 무조건 1. 성공여부 판단을 위해 만든 것

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cstmt != null) {
				try {
					cstmt.close();
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
		CallableStatement cstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			// select 주의해서 짜야합니다.
			sql = "{CALL findByHakScore(?,?)}";
			cstmt = conn.prepareCall(sql);

			// OUT 파라미터는 JDBC 타입을 설정
			// SYS_REFCURSOR => OracleTypes.CURSOR
			// 정수형 => OracleTypes.INTEGER

			cstmt.registerOutParameter(1, OracleTypes.CURSOR); // --Oracle.jdbc 선택

			// IN 파라미터는 값을 설정
			cstmt.setString(2, hak);

			// 모든 프로시져는 executeUpdate() 로 실행
			cstmt.executeUpdate();

			// OUT 파라미터 값 넘겨 받기 (값 가져오기)
			// CURSOR는 무조건 Object 로 !
			rs = (ResultSet) cstmt.getObject(1); // ResultSet은 CURSOR 하고 유사하다.

			// 프로시져 확인
			// pResult OUT SYS_REFCURSOR --pResult 에 담아주는 것.
			// , pHak IN VARCHAR2

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
		} finally {
			if (cstmt != null) {
				try {
					cstmt.close();
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
		CallableStatement cstmt = null;
		ResultSet rs = null;
		String sql;

		try {

			sql = "{CALL findByNameScore(?, ?)}";
			cstmt = conn.prepareCall(sql);

			// OUT 파라미터는 JDBC 타입을 설정
			// SYS_REFCURSOR => OracleTypes.CURSOR
			// 정수형 => OracleTypes.INTEGER

			cstmt.registerOutParameter(1, OracleTypes.CURSOR); // --Oracle.jdbc 선택

			// IN 파라미터는 값을 설정
			cstmt.setString(2, name);

			// 모든 프로시져는 executeUpdate() 로 실행
			cstmt.executeUpdate();

			// OUT 파라미터 값 넘겨 받기 (값 가져오기)
			// CURSOR는 무조건 Object 로 !
			rs = (ResultSet) cstmt.getObject(1); // ResultSet은 CURSOR 하고 유사하다.

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
				// dto.setRank(rs.getInt("rank"));

				list.add(dto); // 1인의 정보를 차곡차곡 ArrayList에 저장
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cstmt != null) {
				try {
					cstmt.close();
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
		CallableStatement cstmt = null;
		ResultSet rs = null; // SELECT 문장에서만 ResultSet 이 있다.
		String sql;

		// 프로시져 확인
		// pResult OUT SYS_REFCURSOR
		try {
			sql = "{CALL listScore(?)}";
			cstmt = conn.prepareCall(sql);

			// OUT 파라미터는 JDBC 타입을 설정
			// SYS_REFCURSOR => OracleTypes.CURSOR
			// 정수형 => OracleTypes.INTEGER

			cstmt.registerOutParameter(1, OracleTypes.CURSOR); // --Oracle.jdbc 선택

			// 모든 프로시져는 executeUpdate() 로 실행
			cstmt.executeUpdate();

			// OUT 파라미터 값 넘겨 받기 (값 가져오기)
			// CURSOR는 무조건 Object 로 !
			rs = (ResultSet) cstmt.getObject(1); // ResultSet은 CURSOR 하고 유사하다.

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

			if (cstmt != null) {
				try {
					cstmt.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}

			}

		}
		return list;
	}

	// 7)
	// 과목별 전체 평균 프로시져
	// pKor OUT NUMBER
	// ,pEng OUT NUMBER
	// ,pMat OUT NUMBER

	@Override
	public Map<String, Integer> averageScore() {
		Map<String, Integer> map = new HashMap<>();
		CallableStatement cstmt = null;
		String sql;

		try {
			sql = "{CALL averageScore(?,?,?)}";
			cstmt = conn.prepareCall(sql);

			cstmt.registerOutParameter(1, OracleTypes.INTEGER);
			cstmt.registerOutParameter(2, OracleTypes.INTEGER);
			cstmt.registerOutParameter(3, OracleTypes.INTEGER);

			cstmt.executeUpdate();
			int kor = cstmt.getInt(1);
			// Integer kor = (Integer) cstmt.getObject(1);
			int eng = cstmt.getInt(2);
			int mat = cstmt.getInt(3);

			map.put("kor", kor);
			map.put("eng", eng);
			map.put("mat", mat);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			if (cstmt != null) {
				try {
					cstmt.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return map;
	}

}

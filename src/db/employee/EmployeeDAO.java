package db.employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

import db.member2.MemberDTO;

public class EmployeeDAO {
	private Connection conn = DBConn.getConnection();

	// 사원 정보 등록
	public int insertEmployee(EmployeeDTO dto) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "INSERT INTO employee(sabeon,name,birth,tel) VALUES(?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			// IN 파라미처 값 설정
			pstmt.setString(1, dto.getSabeon());
			pstmt.setString(2, dto.getName());
			pstmt.setString(3, dto.getBirth());
			pstmt.setString(4, dto.getTel());
			// 쿼리문 실행
			pstmt.executeUpdate();

			result = 1;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}

		return result;
	}

	// 사원 정보 수정
	public int updateEmployee(EmployeeDTO dto) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE employee SET name=?, birth=?, tel=? WHERE sabeon=?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getName());
			pstmt.setString(2, dto.getBirth());
			pstmt.setString(3, dto.getTel());
			pstmt.setString(4, dto.getSabeon());
			pstmt.executeUpdate();

			result = 1;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}

		return result;
	}

	// 사번으로 가져와서 검색
	public EmployeeDTO readEmployee(String sabeon) {
		EmployeeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT e.sabeon, name");
			sb.append(" ,TO_CHAR(birth, 'YYYY-MM-DD') birth, tel ");
			sb.append("  FROM employee e ");
			sb.append("  LEFT OUTER JOIN salary s ON e.sabeon = s.sabeon");
			sb.append("  WHERE  e.sabeon= ?");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, sabeon); // id값을 세팅해줘야한다.
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new EmployeeDTO();
				dto.setSabeon(rs.getString("sabeon"));
				dto.setName(rs.getString("name"));
				dto.setBirth(rs.getDate("birth").toString());
				dto.setTel(rs.getString("tel"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}

		return dto;
	}

	// 전체 사원 리스트 출력
	public List<EmployeeDTO> listEmployee() {
		List<EmployeeDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT e.sabeon, name");
			sb.append(" ,TO_CHAR(birth, 'YYYY-MM-DD') birth, tel ");
			sb.append("  FROM employee e ");
			//sb.append("  LEFT OUTER JOIN salary s ON e.sabeon = s.sabeon");

			pstmt = conn.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				EmployeeDTO dto = new EmployeeDTO();
				dto.setSabeon(rs.getString("sabeon"));
				dto.setName(rs.getString("name"));
				dto.setBirth(rs.getString("birth"));
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
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}

		return list;
	}

	public List<EmployeeDTO> listEmployee(String name) {
		List<EmployeeDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT e.sabeon, name");
			sb.append(" ,TO_CHAR(birth, 'YYYY-MM-DD') birth, tel ");
			sb.append("  FROM employee e ");
			sb.append("  LEFT OUTER JOIN salary s ON e.sabeon = s.sabeon");
			sb.append("  WHERE INSTR(name, ? )>=1");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				EmployeeDTO dto = new EmployeeDTO();
				dto.setSabeon(rs.getString("sabeon"));
				dto.setName(rs.getString("name"));
				dto.setBirth(rs.getString("birth"));
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
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return list;
	}
}

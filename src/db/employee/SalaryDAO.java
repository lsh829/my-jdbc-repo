package db.employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.util.DBConn;

public class SalaryDAO {
	private Connection conn = DBConn.getConnection();

	public int insertSalary(SalaryDTO dto) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "INSERT INTO salary(salaryNum, sabeon, payDate, paymentDate, pay, sudang, tax, memo) "
					+ "VALUES(salary_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getSabeon());
			pstmt.setString(2, dto.getPayDate());
			pstmt.setString(3, dto.getPaymentDate());
			pstmt.setInt(4, dto.getPay());
			pstmt.setInt(5, dto.getSudang());
			pstmt.setInt(6, dto.getTax());
			pstmt.setString(7, dto.getMemo());

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

	// UPDATE
	public int updateSalary(SalaryDTO dto) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE salary SET payDate =? , paymentDate=?, pay=?, sudang=?, memo=? WHERE salaryNum = ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getPayDate());
			pstmt.setString(2, dto.getPaymentDate());
			pstmt.setInt(3, dto.getPay());
			pstmt.setInt(4, dto.getSudang());
			pstmt.setString(5, dto.getMemo());
			pstmt.setInt(6, dto.getSalaryNum());

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

	public int deleteSalary(int salaryNum) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "DELETE FROM salary WHERE salaryNum=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, salaryNum);
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

	// 급여 정보를 UPDATE할 때 해당 정보를 읽어오는 역할 o
	public SalaryDTO readSalary(int salaryNum) {
		SalaryDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT salaryNum, s.sabeon, name ,payDate");
			sb.append(" ,TO_CHAR(paymentDate, 'YYYY-MM-DD') paymentDate, pay, sudang, tax, pay+sudang tot, memo");
			sb.append("  FROM salary s ");
			sb.append("  LEFT OUTER JOIN employee e ON s.sabeon = e.sabeon");
			sb.append("  WHERE salaryNum= ?");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, salaryNum);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new SalaryDTO();
				dto.setSalaryNum(rs.getInt("salaryNum"));
				dto.setSabeon(rs.getString("sabeon"));
				dto.setName(rs.getString("name"));
				dto.setPayDate(rs.getString("payDate"));
				dto.setPaymentDate(rs.getString("paymentDate"));
				dto.setPay(rs.getInt("pay"));
				dto.setSudang(rs.getInt("sudang"));
				dto.setTax(rs.getInt("tax"));
				dto.setTot(rs.getInt("tot"));
				dto.setMemo(rs.getString("memo"));
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

	// 월별리스트
	public List<SalaryDTO> listSalary(String payDate) {
		List<SalaryDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {

			sb.append(" SELECT salaryNum, s.sabeon, name, payDate ");
			sb.append(" ,TO_CHAR(paymentDate, 'YYYY-MM-DD') paymentDate ");
			sb.append(" ,pay ,sudang ,pay+sudang tot, tax , pay+sudang-tax afterpay ");
			sb.append(" FROM salary s ");
			sb.append(" LEFT OUTER JOIN employee e ON s.sabeon = e.sabeon ");
			sb.append(" WHERE SUBSTR(payDate, 5)=SUBSTR(?, 5)");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, payDate);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				SalaryDTO dto = new SalaryDTO();
				dto.setSalaryNum(rs.getInt("salaryNum"));
				dto.setSabeon(rs.getString("sabeon"));
				dto.setName(rs.getString("name"));
				dto.setPayDate(rs.getString("payDate"));
				dto.setPaymentDate(rs.getString("paymentDate"));
				dto.setPay(rs.getInt("pay"));
				dto.setSudang(rs.getInt("sudang"));
				dto.setTot(rs.getInt("tot"));
				dto.setTax(rs.getInt("tax"));
				dto.setAfterPay(rs.getInt("afterPay"));

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

	// 사번 검색
	public List<SalaryDTO> listSalary(Map<String, Object> map) {
		List<SalaryDTO> list = new ArrayList<>();
		String payDate = (String) map.get("payDate");
		String sabeon = (String) map.get("sabeon");

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT salaryNum, s.sabeon, name ");
			sb.append(" ,payDate "); // varchar2 -- checking point
			sb.append(" ,TO_CHAR(paymentDate, 'YYYY-MM-DD') paymentDate");
			sb.append(" ,pay ,sudang , pay+sudang tot");
			sb.append("  FROM salary s ");
			sb.append("  LEFT OUTER JOIN employee e ON s.sabeon = e.sabeon");
			sb.append("  WHERE payDate=? AND s.sabeon=?");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, payDate);
			pstmt.setString(2, sabeon);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				SalaryDTO dto = new SalaryDTO();
				dto.setSalaryNum(rs.getInt("salaryNum"));
				dto.setSabeon(rs.getString("sabeon"));
				dto.setName(rs.getString("name"));
				dto.setPayDate(rs.getString("payDate"));
				dto.setPaymentDate(rs.getString("paymentDate"));
				dto.setPay(rs.getInt("pay"));
				dto.setSudang(rs.getInt("sudang"));
				dto.setTot(rs.getInt("tot"));

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

	// 전체 리스트
	public List<SalaryDTO> listSalary() {
		List<SalaryDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT salaryNum, s.sabeon, name ");
			sb.append(" ,payDate "); // varchar2 -- checking point
			sb.append(" ,TO_CHAR(paymentDate, 'YYYY-MM-DD') paymentDate");
			sb.append(" ,pay ,sudang , pay+sudang tot");
			sb.append("  FROM salary s ");
			sb.append("  LEFT OUTER JOIN employee e ON s.sabeon = e.sabeon");

			pstmt = conn.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				SalaryDTO dto = new SalaryDTO();
				dto.setSalaryNum(rs.getInt("salaryNum"));
				dto.setSabeon(rs.getString("sabeon"));
				dto.setName(rs.getString("name"));
				dto.setPayDate(rs.getString("payDate"));
				dto.setPaymentDate(rs.getString("paymentDate"));
				dto.setPay(rs.getInt("pay"));
				dto.setSudang(rs.getInt("sudang"));
				dto.setTot(rs.getInt("tot"));

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

	// 년도별 월별 전체 현황 리스트
	public List<GroupDTO> AnalysisSalary(String payYear) {
		List<GroupDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT  SUBSTR(?,1,4) year ");
			sb.append(" ,SUBSTR(paydate,5) month ");
			sb.append(" ,sum(pay+sudang-tax) sum_pay, trunc(avg(pay+sudang-tax)) avg_pay ");
			sb.append(" ,max(pay+sudang-tax) max_pay");
			sb.append(" FROM salary s JOIN employee e ON s.sabeon = e.sabeon ");
			sb.append(" GROUP BY SUBSTR(?,1,4), SUBSTR(paydate,5) ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, payYear);
			pstmt.setString(2, payYear);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				GroupDTO dto = new GroupDTO();
				dto.setYear(rs.getString("year"));
				dto.setMonth(rs.getString("month"));
				dto.setSum_pay(rs.getInt("sum_pay"));
				dto.setAvg_pay(rs.getInt("avg_pay"));
				dto.setMax_pay(rs.getInt("max_pay"));

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

package db.employee2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class EmployeeDAO {
	private Connection conn=DBConn.getConnection();
	
	public int insertEmployee(EmployeeDTO dto) {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql="INSERT INTO employee(sabeon, name, birth, tel) ";
			sql+=" VALUES (?, ?, ?, ?)";
			
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, dto.getSabeon());
			pstmt.setString(2, dto.getName());
			pstmt.setString(3, dto.getBirth());
			pstmt.setString(4, dto.getTel());
			
			result=pstmt.executeUpdate();
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
	
	public int updateEmployee(EmployeeDTO dto) {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql="UPDATE employee SET name=?, birth=?, tel=? ";
			sql+=" WHERE sabeon = ?";
			
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, dto.getName());
			pstmt.setString(2, dto.getBirth());
			pstmt.setString(3, dto.getTel());
			pstmt.setString(4, dto.getSabeon());
			
			result=pstmt.executeUpdate();
			
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
	
	public EmployeeDTO readEmployee(String sabeon) {
		EmployeeDTO dto=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			sb.append("SELECT sabeon, name ");
			sb.append("  ,TO_CHAR(birth, 'YYYY-MM-DD') birth, tel ");
			sb.append("  FROM employee");
			sb.append("  WHERE sabeon = ?");
			
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setString(1, sabeon);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new EmployeeDTO();
				dto.setSabeon(rs.getString("sabeon"));
				dto.setName(rs.getString("name"));
				dto.setBirth(rs.getString("birth"));
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
	
	public List<EmployeeDTO> listEmployee() {
		List<EmployeeDTO> list=new ArrayList<>();
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			sb.append("SELECT sabeon, name ");
			sb.append("  , TO_CHAR(birth, 'YYYY-MM-DD') birth, tel ");
			sb.append("  FROM employee");
			
			pstmt=conn.prepareStatement(sb.toString());

			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				EmployeeDTO dto = new EmployeeDTO();
				dto.setSabeon(rs.getString("sabeon"));
				dto.setName(rs.getString("name"));
				dto.setBirth(rs.getString("birth"));
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
	
	public List<EmployeeDTO> listEmployee(String name) {
		List<EmployeeDTO> list=new ArrayList<>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			sb.append("SELECT sabeon, name ");
			sb.append("  , TO_CHAR(birth, 'YYYY-MM-DD') birth, tel ");
			sb.append("  FROM employee");
			sb.append("  WHERE INSTR(name, ?)>=1");
			
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setString(1, name);
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				EmployeeDTO dto = new EmployeeDTO();
				dto.setSabeon(rs.getString("sabeon"));
				dto.setName(rs.getString("name"));
				dto.setBirth(rs.getString("birth"));
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

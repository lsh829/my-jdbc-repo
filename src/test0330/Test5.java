package test0330;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.util.DBConn;

//내 힘으로 Update 짜보기 

public class Test5 {
	public static void main(String[] args) {
		Connection conn = DBConn.getConnection();
		Statement stmt = null;
		//Statement 인터페이스 - 쿼리를 실행시킬 객체가 안에 존재한다. 
		String sql;
		int result = 0;
		
		try {
			stmt = conn.createStatement(); //Connection > conn 
	
		
			 sql = "UPDATE score SET name='이선화' WHERE hak='1111'";
			 //홍길동 -> 이선화로 수정되었음을 sqldeveloper에서 확인할 수 있습니다. 
			
			
			result = stmt.executeUpdate(sql); //select 빼고 다 executeUpdate 로 실행 
			
			System.out.println(result+"행이 수정 되었습니다.");
		}catch(SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(stmt!=null) {
				try {
					stmt.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}


}

package test0330;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.util.DBConn;

public class Test2 {
	public static void main(String[] args) {
		Connection conn = DBConn.getConnection();
		Statement stmt = null;
		//Statement 인터페이스 - 쿼리를 실행시킬 객체가 안에 존재한다. 
		String sql;
		int result = 0;
		
		try {
			stmt = conn.createStatement(); //Connection > conn 
	
			//1) 
			/*
			 * sql = "INSERT INTO score(hak, name, birth, kor, mat, eng)"
			 * +"VALUES ('1111','홍길동','2000-10-10',80,90,70)";
			 */
			//sqldeveloper 로 들어가서 확인하면 데이터 추가 됐음을 확인 가능하다. 
			
			//2) 
			/*
			 * sql = "INSERT INTO score(hak, name, birth, kor, mat, eng)" +
			 * "VALUES ('2222','홍길동','10-10-2000',80,90,70)";
			 */
			//ORA-01830: 날짜 형식의 지정에 불필요한 데이터가 포함되어 있습니다
			//TO_DATE 을 이용하면 데이터 추가 가능하다. 
			 
			//3) 
			 sql = "INSERT INTO score(hak, name, birth, kor, mat, eng)" 
						+"VALUES ('2222','심심해','1999-01-10',80,90,70)"; 
			//sqldeveloper 로 들어가서 확인하면 데이터 추가 됐음을 확인 가능하다. 
			 
			 //4) 
			/*
			 * sql = "INSERT INTO score(hak, name, birth, kor, mat, eng)"
			 * +"VALUES ('2222','심심해','1999-01-10',80,90,70);"; -- sql명령어 안에다가 ;넣으면 안된다.
			 * -- java.sql.SQLSyntaxErrorException: ORA-00933: SQL 명령어가 올바르게 종료되지 않았습니다
			 */
			
			result = stmt.executeUpdate(sql); //select 빼고 다 executeUpdate 로 실행 
			
			System.out.println(result+"행이 추가 되었습니다.");
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

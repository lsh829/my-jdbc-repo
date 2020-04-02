package test0402;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.util.DBConn;

public class Test3UPDATABLE {
	public static void main(String[] args) {
		//SELECT 문으로 수정 
		
		Connection conn = DBConn.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT hak, name, birth, kor, eng, mat FROM score";
			
			//sql = "SELECT * FROM score"; //처럼 * 사용하면 수정 불가 
										   //컬럼명을 나열해야만 한다.
			
			stmt = conn.createStatement(
					ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE
					);
			
			rs = stmt.executeQuery(sql);
			
			//첫번째 데이터  
			if(rs.next()) {
				System.out.print(rs.getString(1)+"\t");
				System.out.print(rs.getString(2)+"\t");
				System.out.print(rs.getString(3)+"\t");
				System.out.print(rs.getString(4)+"\t");
				System.out.print(rs.getString(5)+"\t");
				System.out.print(rs.getString(6)+"\n");
				
				//
				rs.updateString("name", "고고고");
				rs.updateInt("kor", 100);
				rs.updateInt("eng", 100);
				rs.updateInt("mat", 100);
				
				// updateRow() 와 만나야지만 , update가 가능하다. 
				rs.updateRow();
			}
			
			// 중간에 있는 위치의 사람 데이터를 볼 수 있다.
			if(rs.absolute(1)) { // (1) : 첫번째 데이터 | (10) : 열번째 데이터 
				System.out.print(rs.getString(1)+"\t");
				System.out.print(rs.getString(2)+"\t");
				System.out.print(rs.getString(3)+"\t");
				System.out.print(rs.getString(4)+"\t");
				System.out.print(rs.getString(5)+"\t");
				System.out.print(rs.getString(6)+"\n");
				
			}
			rs.close();
			stmt.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
